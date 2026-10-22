package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.exception.DataAccessException;
import org.sagacity.sqltoy.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.lesson.entity.DeviceOrder;
import com.sqltoy.lesson.entity.StaffMember;

/**
 * Lesson 10 — lambda conditions: Lesson 2's filters, written as code.
 *
 * Lesson 2 built optional filters with #[...] in XML; the lambda API expresses
 * the same conditions with entity getter references (StaffMember::getStatus),
 * so column names are checked against the entity at assembly time and every
 * value still goes through a JDBC "?" placeholder. Watch the console: the
 * printed SQL is the same shape XML produces.
 *
 * Guard rails demonstrated here (both throw by design):
 * - getOne() with multiple matches throws instead of silently returning row 1;
 * - an update with no where condition throws unless you say .matchAll().
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson10LambdaQueryTest {

	@Autowired
	LightDao lightDao;

	// rows created by the wildcard/update demos, removed after each test
	private static final String DEMO_TYPE = "LAMBDA-DEMO";

	@AfterEach
	void cleanOwnRows() {
		lightDao.lambdaDelete(DeviceOrder.class).eq(DeviceOrder::getDeviceType, DEMO_TYPE).delete();
	}

	@Test
	void conditionsPaginationAndCount() {
		// 1) conditions on the seed data: active "Chen" staff
		List<StaffMember> chens = lightDao.lambdaQuery(StaffMember.class)
				.eq(StaffMember::getStatus, 1)
				.like(StaffMember::getStaffName, "Chen")
				.find();
		System.out.println("active staff named Chen -> " + chens.size() + " rows");
		assertEquals(2, chens.size());

		// 2) combined filters + count/exists terminals
		long recent = lightDao.lambdaQuery(StaffMember.class)
				.in(StaffMember::getOrganId, new String[] { "100005", "100007" })
				.between(StaffMember::getEntryDate, LocalDate.parse("2021-01-01"), LocalDate.parse("2023-12-31"))
				.count();
		System.out.println("R&D/Sales staff entered 2021-2023 -> " + recent + " rows");
		assertEquals(6L, recent);
		assertTrue(lightDao.lambdaQuery(StaffMember.class).eq(StaffMember::getOrganId, "100005").exists());
		assertFalse(lightDao.lambdaQuery(StaffMember.class).eq(StaffMember::getStaffName, "Nobody").exists());

		// 3) pagination: Page(pageSize, pageNo) — first page of 5, newest first
		Page<StaffMember> page = lightDao.lambdaQuery(StaffMember.class)
				.eq(StaffMember::getStatus, 1)
				.orderByDesc(StaffMember::getEntryDate)
				.findPage(new Page(5, 1));
		System.out.println("page 1/5 of active staff -> " + page.getRows().size() + " rows, total "
				+ page.getRecordCount());
		assertEquals(5, page.getRows().size());
		assertEquals(19L, page.getRecordCount());
	}

	@Test
	void getOneIsStrict() {
		// unique key -> the row
		StaffMember alice = lightDao.lambdaQuery(StaffMember.class)
				.eq(StaffMember::getStaffId, "S0001")
				.getOne();
		assertEquals("Alice Chen", alice.getStaffName());

		// "Chen" matches TWO rows: a silent first-row return would hide a
		// condition defect, so sqltoy throws instead
		assertThrows(DataAccessException.class,
				() -> lightDao.lambdaQuery(StaffMember.class).like(StaffMember::getStaffName, "Chen").getOne());
	}

	/**
	 * like() copies the value into the pattern and, when the value already
	 * contains %, treats it as your hand-written pattern (sqltoy never pads a
	 * second layer) — so a user-typed "100%" acts as the wildcard pattern
	 * "starts with 100". The likeExact family (6.0.9+) always takes the value
	 * literally: % is escaped, only rows containing the characters "100%"
	 * match. Same search box, two intents:
	 */
	@Test
	void likeExactTreatsWildcardsAsLiteral() {
		saveDemoOrder("WILD-01", "100%OFF", new BigDecimal("10.00"));
		saveDemoOrder("WILD-02", "100XOFF", new BigDecimal("10.00"));

		// "100%" typed by a user in a search box
		long likePercent = lightDao.lambdaQuery(DeviceOrder.class)
				.eq(DeviceOrder::getDeviceType, DEMO_TYPE)
				.like(DeviceOrder::getBuyer, "100%")
				.count();
		long exactPercent = lightDao.lambdaQuery(DeviceOrder.class)
				.eq(DeviceOrder::getDeviceType, DEMO_TYPE)
				.likeExact(DeviceOrder::getBuyer, "100%")
				.count();
		System.out.println("buyer ~ '100%': like -> " + likePercent + " rows (wildcard), likeExact -> "
				+ exactPercent + " rows (literal)");
		// like: the % is a wildcard, "100XOFF" matches too
		assertEquals(2L, likePercent);
		// likeExact: only the row literally containing "100%" matches
		assertEquals(1L, exactPercent);
	}

	@Test
	void expressionUpdate() {
		DeviceOrder order = saveDemoOrder("EXPR-01", "10%OFF", new BigDecimal("100.00"));

		// set a fixed value
		lightDao.lambdaUpdate(DeviceOrder.class)
				.set(DeviceOrder::getStatus, 2)
				.eq(DeviceOrder::getOrderId, order.getOrderId())
				.update();

		// setExpr: TOTAL_AMT = TOTAL_AMT * ? — the database does the math on the
		// current value, no read-modify-write race
		lightDao.lambdaUpdate(DeviceOrder.class)
				.setExpr(DeviceOrder::getTotalAmt, "totalAmt * ?", new BigDecimal("1.1"))
				.eq(DeviceOrder::getOrderId, order.getOrderId())
				.update();

		DeviceOrder after = lightDao.load(new DeviceOrder().setOrderId(order.getOrderId()));
		System.out.println("after setExpr totalAmt*1.1 -> " + after.getTotalAmt() + ", status -> " + after.getStatus());
		assertEquals(0, after.getTotalAmt().compareTo(new BigDecimal("110.0000")));
		assertEquals(2, after.getStatus().intValue());
	}

	@Test
	void noWhereUpdateNeedsMatchAll() {
		// updating every row by accident is the classic footgun: sqltoy refuses
		// unless you state the intent with .matchAll()
		assertThrows(DataAccessException.class,
				() -> lightDao.lambdaUpdate(DeviceOrder.class).set(DeviceOrder::getStatus, 9).update());
	}

	/**
	 * Calling a join step switches the same builder to the join channel: the
	 * generated SQL is a LEFT JOIN and the result maps back onto StaffMember.
	 */
	@Test
	void joinQuery() {
		// staff of organs that have SERVER orders (Sales 100007 x1, R&D 100005 x1)
		List<StaffMember> rows = lightDao.lambdaQuery(StaffMember.class)
				.leftJoin(DeviceOrder.class)
				.on(StaffMember::getOrganId, DeviceOrder::getOrganId)
				.eq(StaffMember::getStatus, 1)
				.eq(DeviceOrder::getDeviceType, "SERVER")
				.find();
		System.out.println("active staff in organs with SERVER orders -> " + rows.size() + " rows");
		assertEquals(15, rows.size());
	}

	private DeviceOrder saveDemoOrder(String orderId, String buyer, BigDecimal amount) {
		DeviceOrder order = new DeviceOrder().setOrderId(orderId).setBuyer(buyer).setOrganId("100007")
				.setDeviceType(DEMO_TYPE).setTransDate(LocalDate.now()).setTotalAmt(amount).setStatus(1);
		lightDao.save(order);
		return order;
	}
}
