package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Lesson 9 (base) — Lesson 8's cross-database promise, checked against real databases.
 *
 * docker-compose.yml starts the four containers used here (postgres / sqlserver /
 * oracle / mysql; ports and credentials match the subclasses). Each concrete subclass
 * boots a full Spring context with spring.sqltoy.functionConverts=default (inherited
 * from application.yml) pointed at one database, so every statement goes through the
 * same load-time conversion pipeline as production and is then executed for real.
 * The statements are literal-based (no tables), so no schema seeding is needed
 * (spring.sql.init.mode=never in each subclass). When a container is not running the
 * subclass is skipped automatically — plain `mvn test` works without docker.
 *
 * What the dialects demonstrate (see the per-test comments and the debug SQL log):
 *  - find_in_set: integer position in projection, "=2" comparison in WHERE (all four)
 *  - substr: literal start 0 unified to 1 (all four)
 *  - to_char: bare HH = 24-hour; literal first arg cast per dialect
 *  - to_date: time part preserved (pg to_timestamp, sqlserver convert 120, mysql STR_TO_DATE)
 *  - trim(from col): rewritten where the database rejects the standard form (oracle/mysql)
 *  - date_trunc week: ISO Monday everywhere (sqlserver via a DATEFIRST-independent anchor)
 *  - instr 4-arg: REGEXP_INSTR (sqlserver/mysql) / regexp_instr (pg 15+) / native (oracle)
 *  - left/right(str,0): empty, not the whole string (oracle substr rewrite)
 *  - group_concat / string_agg: within-group and multi-segment forms per dialect
 */
abstract class Lesson09RealDbFunctionConvertTestBase {

	// Connection coordinates of the four docker-compose.yml databases, in ONE place:
	// the subclasses reference them in their @SpringBootTest properties and in the
	// reachability probe. If your containers differ, adjust here only.
	protected static final String PG_URL = "jdbc:postgresql://localhost:5433/postgres";
	protected static final String PG_USER = "postgres";
	protected static final String PG_PASSWORD = "Sqltoy@2026";

	protected static final String SQLSERVER_URL = "jdbc:sqlserver://localhost:1434;databaseName=master;encrypt=false";
	protected static final String SQLSERVER_USER = "sa";
	protected static final String SQLSERVER_PASSWORD = "Sqltoy@2026";

	protected static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/freepdb1";
	protected static final String ORACLE_USER = "system";
	protected static final String ORACLE_PASSWORD = "Sqltoy@2026";

	protected static final String MYSQL_URL = "jdbc:mysql://localhost:3307/sqltoy?sslMode=DISABLED&allowPublicKeyRetrieval=true&characterEncoding=utf8";
	protected static final String MYSQL_USER = "root";
	protected static final String MYSQL_PASSWORD = "Sqltoy@2026";

	@Autowired
	LightDao lightDao;

	protected Map<String, Object> noFilter() {
		return new HashMap<>();
	}

	@Test
	@SuppressWarnings("rawtypes")
	void findInSetProjectionReturnsIntegerPosition() {
		List rows = lightDao.find("lesson09_find_in_set_projection", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[find_in_set projection] pos=" + row[0] + ", miss=" + row[1]);
		assertEquals(2, ((Number) row[0]).intValue());
		assertEquals(0, ((Number) row[1]).intValue());
	}

	@Test
	@SuppressWarnings("rawtypes")
	void findInSetPositionComparisonInWhere() {
		List rows = lightDao.find("lesson09_find_in_set_position_cmp", noFilter());
		System.out.println("[find_in_set =2] rows=" + rows.size());
		assertEquals(1, rows.size());
		assertEquals("b", str(toArray(rows.get(0))[0]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void substrZeroStartNormalizedToOne() {
		List rows = lightDao.find("lesson09_substr_zero_start", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[substr] s0=" + row[0] + ", s2=" + row[1]);
		assertEquals("he", str(row[0]));
		assertEquals("lo", str(row[1]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void toCharBareHourIs24Hour() {
		List rows = lightDao.find("lesson09_to_char_24h", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[to_char] t24=" + row[0] + ", t12=" + row[1]);
		assertEquals("2026-10-20 14:30:45", str(row[0]));
		assertEquals("2026-10-20 02:30:45", str(row[1]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void toDatePreservesTimePart() {
		List rows = lightDao.find("lesson09_to_date_time", noFilter());
		Object[] row = toArray(rows.get(0));
		assertNotNull(row[0]);
		String ts = norm(row[0]);
		System.out.println("[to_date] ts=" + ts);
		// pg's plain to_date would silently truncate the time to 00:00:00, so sqltoy
		// switches to to_timestamp there; pg reads back as timestamptz shifted to the JVM
		// zone, so assert on the parts that are stable across whole-hour zone shifts:
		// the date and minute:second
		assertTrue(ts.contains("2026-10-20"), "date part must be 2026-10-20, got: " + ts);
		assertTrue(ts.contains("30:45"), "time part must be preserved (not truncated), got: " + ts);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void trimFromStandardFormRewritten() {
		List rows = lightDao.find("lesson09_trim_from", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[trim(from)] t=" + row[0]);
		assertEquals("padded", str(row[0]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void dateTruncWeekIsIsoMonday() {
		List rows = lightDao.find("lesson09_datetrunc_week", noFilter());
		Object[] row = toArray(rows.get(0));
		assertNotNull(row[0]);
		String wk = norm(row[0]);
		System.out.println("[date_trunc week] wk=" + wk);
		// 2026-10-20 is a Tuesday; ISO Monday anchoring must yield 2026-10-19
		// (a naive DATEDIFF(week) on sqlserver follows SET DATEFIRST — default Sunday —
		// and would silently return 2026-10-18)
		assertTrue(wk.contains("2026-10-19"), "week must start on ISO Monday 2026-10-19, got: " + wk);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void instrFourArgSecondOccurrence() {
		List rows = lightDao.find("lesson09_instr_4arg", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[instr 4-arg] p=" + row[0]);
		// second 'o' of 'hello world' sits at position 8
		assertEquals(8, ((Number) row[0]).intValue());
	}

	@Test
	@SuppressWarnings("rawtypes")
	void leftRightZeroLengthIsEmpty() {
		List rows = lightDao.find("lesson09_left_right_zero", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[left/right 0] l0=" + row[0] + ", r0=" + row[1] + ", l2=" + row[2]);
		// oracle returns NULL for substr(str,1,0), the other dialects return '' —
		// both mean "empty" (a substr(str,-0) rewrite would return the WHOLE string on oracle)
		assertTrue(row[0] == null || str(row[0]).isEmpty(), "left(str,0) must be empty, got: " + row[0]);
		assertTrue(row[1] == null || str(row[1]).isEmpty(), "right(str,0) must be empty, got: " + row[1]);
		assertEquals("he", str(row[2]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void stringAggWithinGroupConverted() {
		List rows = lightDao.find("lesson09_string_agg_within_group", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[string_agg within group] xs=" + row[0]);
		// pg: within group -> string_agg(x,',' order by x); oracle: listagg;
		// mysql: group_concat(... ORDER BY ... SEPARATOR); sqlserver: native passthrough
		assertEquals("a,b,c", str(row[0]));
	}

	@Test
	@SuppressWarnings("rawtypes")
	void groupConcatMultiSegmentConcatenates() {
		List rows = lightDao.find("lesson09_group_concat_multi_segment", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[group_concat multi-segment] c12=" + row[0]);
		assertEquals("12", str(row[0]));
	}

	/**
	 * Guard for @EnabledIf: probes with the EXACT JDBC url/credentials the test uses,
	 * not just an open TCP port — an unrelated database on the same port (or a stopped
	 * container) skips the whole class instead of failing its Spring context and
	 * turning the build red.
	 */
	protected static boolean dbReachable(String driverClass, String url, String username, String password) {
		try {
			// surefire's isolated classloader does not auto-register JDBC drivers
			// with DriverManager, so load the driver explicitly before probing
			Class.forName(driverClass);
			DriverManager.setLoginTimeout(5);
			try (Connection connection = DriverManager.getConnection(url, username, password)) {
				return connection.isValid(3);
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected Object[] toArray(Object r) {
		return (r instanceof Object[]) ? (Object[]) r : ((List<?>) r).toArray();
	}

	protected String str(Object v) {
		return v == null ? null : String.valueOf(v);
	}

	// normalize ISO 'T' separators so Timestamp/LocalDateTime/OffsetDateTime all compare alike
	protected String norm(Object v) {
		return String.valueOf(v).replace('T', ' ');
	}
}
