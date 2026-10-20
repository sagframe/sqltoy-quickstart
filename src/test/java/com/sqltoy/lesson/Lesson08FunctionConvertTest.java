package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;

/**
 * Lesson 8 — function conversion deep dive, still zero-install (H2).
 *
 * Lesson 5 showed the idea with nvl -> coalesce. Here every statement is written
 * on purpose in a "foreign" dialect — Oracle to_char/to_date, MySQL
 * find_in_set/date_format/substr, standard trim(from ...) — and sqltoy rewrites it
 * at load time into something H2 understands. debug=true prints the converted SQL
 * of every test, so you can read exactly what ran.
 *
 * Framework conventions these tests rely on (they hold across all dialects):
 *  - find_in_set keeps the MySQL contract everywhere: integer element position in
 *    projection context, boolean/position comparisons in WHERE context
 *  - in date format models bare HH means 24-hour, lowercase hh means 12-hour;
 *    mm is month and mi is minute (Oracle semantics), Java-style HH:mm is normalized
 *  - substr with a literal start of 0 is unified to "from the beginning" (start 1)
 *  - string literals passed to date functions are cast to the right type per dialect
 *
 * The real-database counterpart of this lesson is Lesson 9 (see docker-compose.yml).
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson08FunctionConvertTest {

	@Autowired
	LightDao lightDao;

	private Map<String, Object> noFilter() {
		return new HashMap<>();
	}

	@Test
	@SuppressWarnings("rawtypes")
	void findInSetProjectionReturnsIntegerPosition() {
		List rows = lightDao.find("lesson08_find_in_set_projection", noFilter());
		// select find_in_set('b','a,b,c') pos, find_in_set('x','a,b,c') miss
		Object[] row = toArray(rows.get(0));
		System.out.println("find_in_set projection: pos=" + row[0] + ", miss=" + row[1]);
		// H2 has no find_in_set: sqltoy rewrites the projection to a position
		// expression, keeping the MySQL contract — 'b' is element 2 of 'a,b,c'
		assertEquals(2, ((Number) row[0]).intValue());
		// not found -> 0
		assertEquals(0, ((Number) row[1]).intValue());
	}

	@Test
	@SuppressWarnings("rawtypes")
	void findInSetPositionComparisonInWhere() {
		List rows = lightDao.find("lesson08_find_in_set_position_cmp", noFilter());
		System.out.println("find_in_set(...)=2 matched " + rows.size() + " rows");
		// position 2 in '100001,100005,100007' is 100005: the 8 seeded staff of that organ
		assertEquals(8, rows.size());
		for (Object r : rows) {
			// select t.STAFF_ID,t.STAFF_NAME,t.ORGAN_ID
			assertEquals("100005", String.valueOf(toArray(r)[2]));
		}
	}

	@Test
	@SuppressWarnings("rawtypes")
	void findInSetBooleanFormsKeepWorking() {
		List rows = lightDao.find("lesson08_find_in_set_boolean", noFilter());
		System.out.println("find_in_set(...)>0 matched " + rows.size() + " rows");
		// organs 100001 (4 active staff) + 100005 (8 staff)
		assertEquals(12, rows.size());
	}

	@Test
	@SuppressWarnings("rawtypes")
	void substrZeroStartNormalizedToOne() {
		List rows = lightDao.find("lesson08_substr_zero_start", noFilter());
		// select substr('hello',0,2) s0, substring('hello',0,3) s1, substr('hello',-2) s2
		Object[] row = toArray(rows.get(0));
		System.out.println("substr zero/negative start: s0=" + row[0] + ", s1=" + row[1] + ", s2=" + row[2]);
		// databases disagree about start 0 (mysql: empty, oracle: start 1, pg/sqlserver:
		// one character short, db2: error) — sqltoy unifies a literal 0 to "from the
		// beginning"; native H2 substring('hello',0,2) would return 'h'
		assertEquals("he", row[0]);
		assertEquals("hel", row[1]);
		// negative start = last n characters (mysql contract, guarded rewrite)
		assertEquals("lo", row[2]);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void toCharBareHourIs24HourOnH2() {
		List rows = lightDao.find("lesson08_to_char_24h", noFilter());
		// select to_char(...,'yyyy-mm-dd HH:mi:ss') t24, to_char(...,'yyyy-mm-dd hh:mi:ss') t12
		Object[] row = toArray(rows.get(0));
		System.out.println("to_char: t24=" + row[0] + ", t12=" + row[1]);
		// bare HH follows the framework convention (24-hour); H2's native to_char
		// prints 12-hour for HH, so sqltoy rewrites it to HH24 — and casts the
		// string literal first argument to TIMESTAMP (H2 rejects varchar there)
		assertEquals("2026-10-20 14:30:45", row[0]);
		// lowercase hh keeps the 12-hour semantics
		assertEquals("2026-10-20 02:30:45", row[1]);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void toDateMonthTokenFixedOnH2() {
		List rows = lightDao.find("lesson08_to_date_h2", noFilter());
		// select to_date('2026-10-20','yyyy-mm-dd') d, to_date('... 14:30:45','...') ts
		Object[] row = toArray(rows.get(0));
		System.out.println("to_date: d=" + row[0] + ", ts=" + row[1]);
		// to_date becomes parsedatetime on H2, and the Oracle model must be mapped to
		// Java tokens: mm is MONTH (MM), mi is minute (mm) — feeding 'yyyy-mm-dd'
		// straight to Java would parse the month as minutes
		assertNotNull(row[0]);
		// the driver may hand back Timestamp ("... 14:30:45.0") or OffsetDateTime
		// ("...T14:30:45+08:00"), so normalize the ISO 'T' before comparing
		assertTrue(String.valueOf(row[0]).replace('T', ' ').startsWith("2026-10-20"),
				"month token must parse as October, got: " + row[0]);
		assertTrue(String.valueOf(row[1]).replace('T', ' ').startsWith("2026-10-20 14:30:45"),
				"time part must be preserved, got: " + row[1]);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void dateFormatOracleStyleNormalized() {
		List rows = lightDao.find("lesson08_date_format_oracle_style", noFilter());
		// select date_format(...,'yyyy-mm-dd') df1, date_format(...,'yyyy-mm-dd HH:mm:ss') df2
		Object[] row = toArray(rows.get(0));
		System.out.println("date_format: df1=" + row[0] + ", df2=" + row[1]);
		// date_format is MySQL syntax; the Oracle/Java-style format is normalized to
		// %tokens first, then mapped to H2's formatdatetime — a raw 'yyyy-mm-dd' would
		// hit Java tokens where mm means minute
		assertEquals("2026-10-20", row[0]);
		assertEquals("2026-10-20 14:30:45", row[1]);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void trimFromStandardFormRewritten() {
		List rows = lightDao.find("lesson08_trim_from", noFilter());
		// select trim(from '  padded  ') t, trim(leading ...) l, trim(trailing ...) r
		Object[] row = toArray(rows.get(0));
		System.out.println("trim: t=" + row[0] + ", l=" + row[1] + ", r=" + row[2]);
		// trim(from col) is standard SQL but several databases reject it; sqltoy
		// rewrites the keyword-less form explicitly (on H2: trim(both ' ' from col))
		assertEquals("padded", row[0]);
		assertEquals("helloxx", row[1]);
		assertEquals("xxhello", row[2]);
	}

	private Object[] toArray(Object r) {
		return (r instanceof Object[]) ? (Object[]) r : ((List<?>) r).toArray();
	}
}
