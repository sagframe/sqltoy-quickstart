package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.config.annotation.Translate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.lesson.entity.StaffMember;
import com.sqltoy.lesson.entity.TransLedger;

/**
 * Lesson 10 — two 6.0.12 features in action.
 *
 * 1) Programmatic cache translation on lambda queries: declare translates per
 * query instead of (or alongside) the @Translate annotation — same caches as
 * Lesson 4, no JOIN, works on both the single-table and join channels.
 *
 * 2) Entity-style updateFetch: lock a row by its primary key / unique props,
 * validate and mutate the entity inside a callback, get the updated entity
 * back in ONE database interaction. Unlike updateSaveFetch it never inserts —
 * a missing row simply returns null.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson10NewFeatures61Test {

	@Autowired
	LightDao lightDao;

	@Autowired
	TransactionTemplate transactionTemplate;

	// ------------------------------------------------------------------
	// 1) lambda programmatic cache translate (6.0.12)
	// ------------------------------------------------------------------

	@Test
	void lambdaKeyToTargetTranslate() {
		// key field translated into target field: sqltoy appends
		// "ORGAN_ID AS ORGAN_NAME" to the query columns automatically
		List<StaffMember> rows = lightDao.lambdaQuery(StaffMember.class)
			 .translate("organIdName", StaffMember::getOrganId, StaffMember::getOrganName)
			 .eq(StaffMember::getSexType, "F")
			 .find();
		assertTrue(rows.size() >= 2);
		rows.forEach(r -> assertEquals("R&D Center", r.getOrganName()));
	}

	@Test
	void lambdaSameColumnTranslate() {
		// same-column in-place translation: the column's raw value is the cache key
		// and is replaced by the translated value (no extra column added)
		List<StaffMember> rows = lightDao.lambdaQuery(StaffMember.class)
			 .translate("dictKeyName", StaffMember::getSexTypeName)
			 .eq(StaffMember::getStatus, 1)
			 .find();
		// every SEX_TYPE code (M/F) has become its label in place
		rows.forEach(r -> assertTrue("Male".equals(r.getSexTypeName()) || "Female".equals(r.getSexTypeName()),
				"unexpected sexTypeName: " + r.getSexTypeName()));
	}

	@Test
	void lambdaTranslatesFullModel() {
		// full Translate model: constructor + setters, supports cacheType and more
		List<StaffMember> rows = lightDao.lambdaQuery(StaffMember.class)
			 .translates(new Translate("organIdName").setKeyColumn("organId").setColumn("organName"))
			 .like(StaffMember::getStaffName, "Chen")
			 .find();
		assertEquals(2, rows.size());
		rows.forEach(r -> assertEquals("R&D Center", r.getOrganName()));
	}

	// ------------------------------------------------------------------
	// 2) entity-style updateFetch (6.0.12)
	// ------------------------------------------------------------------

	@Test
	void updateFetchEntityModifiesAndReturns() {
		TransLedger updated = transactionTemplate.execute(tx -> {
			TransLedger probe = new TransLedger();
			probe.setOrderId("LEDGER-01");
			try {
				// lock by primary key, validate, mutate the entity — no sql, no ResultSet
				return lightDao.updateFetch(probe, (entity, rowIndex) -> {
					int qty = entity.getQuantity();
					if (qty < 1) {
						throw new IllegalStateException("quantity underflow");
					}
					entity.setQuantity(qty - 1);
					entity.setAmt(entity.getAmt().subtract(BigDecimal.ONE));
				});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		assertNotNull(updated, "LEDGER-01 exists — entity must come back");
		System.out.println("after updateFetch: quantity=" + updated.getQuantity() + " amt=" + updated.getAmt());
	}

	@Test
	void updateFetchEntityMissingRowReturnsNullNoInsert() {
		TransLedger probe = new TransLedger();
		probe.setOrderId("LEDGER-NO-SUCH-ROW");
		TransLedger result = transactionTemplate.execute(tx -> {
			try {
				return lightDao.updateFetch(probe, (entity, rowIndex) -> entity.setQuantity(0));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		assertNull(result, "missing row returns null (no insert — that is updateSaveFetch's job)");
		// and nothing was inserted behind our back
		Long cnt = lightDao.find("select count(*) from lesson_trans_ledger where ORDER_ID=:id",
				java.util.Map.of("id", "LEDGER-NO-SUCH-ROW"), Long.class).get(0);
		assertEquals(Long.valueOf(0), cnt);
	}
}
