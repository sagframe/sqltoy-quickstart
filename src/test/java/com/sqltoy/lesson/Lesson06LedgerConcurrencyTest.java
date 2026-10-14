package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.callback.UpdateRowHandler;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.LockMode;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.lesson.entity.TransLedger;

/**
 * Lesson 6 — race-proof ledgers in one round-trip.
 *
 * The classic lost-update bug: thread A reads quantity=100, thread B reads 100,
 * both write 99 — one deduction vanishes. updateFetch prevents it by locking,
 * updating and returning in a single database interaction; the UpdateRowHandler
 * computes the new value from the CURRENT row state on the server side.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson06LedgerConcurrencyTest {

	@Autowired
	LightDao lightDao;

	@Autowired
	TransactionTemplate transactionTemplate;

	private static final String DEDUCT_SQL = "select t.ORDER_ID,t.QUANTITY,t.AMT"
			+ " from lesson_trans_ledger t where t.ORDER_ID=?";

	private int deductOne() {
		// updateFetch must run inside a transaction: the locked update is committed with it
		@SuppressWarnings("unchecked")
		List<TransLedger> rows = (List<TransLedger>) transactionTemplate.execute(tx -> {
			try {
				return lightDao.updateFetch(
						new QueryExecutor(DEDUCT_SQL).values("LEDGER-01").resultType(TransLedger.class)
								.lock(LockMode.UPGRADE),
						new UpdateRowHandler() {
							public void updateRow(java.sql.ResultSet rs, int index) throws Exception {
								rs.updateInt("QUANTITY", rs.getInt("QUANTITY") - 1);
								rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").subtract(BigDecimal.ONE));
							}
						});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return (rows == null || rows.isEmpty()) ? 1 : 0;
	}

	@Test
	void concurrentDeductionLosesNothing() throws Exception {
		int threads = 8;
		int perThread = 50;
		AtomicInteger failures = new AtomicInteger();
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(threads);
		ExecutorService pool = Executors.newFixedThreadPool(threads);

		for (int t = 0; t < threads; t++) {
			pool.submit(() -> {
				try {
					start.await();
					for (int i = 0; i < perThread; i++) {
						failures.addAndGet(deductOne());
					}
				} catch (Exception e) {
					failures.incrementAndGet();
				} finally {
					done.countDown();
				}
			});
		}
		start.countDown();
		done.await(60, TimeUnit.SECONDS);
		pool.shutdownNow();

		TransLedger ledger = lightDao.load(new TransLedger().setOrderId("LEDGER-01"));
		System.out.println("threads=" + threads + " x " + perThread + " deductions, failures=" + failures.get());
		System.out.println("remaining quantity = " + ledger.getQuantity() + " (expected "
				+ (1000 - threads * perThread) + "), remaining amount = " + ledger.getAmt());
		assertEquals(0, failures.get());
		assertEquals(1000 - threads * perThread, ledger.getQuantity());
	}

	@Test
	void updateSaveFetchInsertsWhenAbsent() {
		// row does not exist: updateSaveFetch inserts it, then later calls update in place
		TransLedger created = transactionTemplate.execute(tx -> lightDao.updateSaveFetch(
				new TransLedger().setOrderId("LEDGER-NEW").setQuantity(0).setAmt(BigDecimal.ZERO).setStatus(1),
				new UpdateRowHandler() {
					@Override
					public void updateRow(java.sql.ResultSet rs, int index) throws Exception {
						rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + 5);
					}
				}, "orderId"));

		TransLedger second = transactionTemplate.execute(tx -> lightDao.updateSaveFetch(
				new TransLedger().setOrderId("LEDGER-NEW").setStatus(1),
				new UpdateRowHandler() {
					@Override
					public void updateRow(java.sql.ResultSet rs, int index) throws Exception {
						rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + 5);
					}
				}, "orderId"));

		System.out.println("created quantity = " + created.getQuantity() + ", after second call = "
				+ second.getQuantity());
		assertNotNull(lightDao.load(new TransLedger().setOrderId("LEDGER-NEW")));
		// the insert path skips the handler; the second call updates in place: 0 + 5
		assertEquals(5, second.getQuantity());
	}
}
