package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;

/**
 * Lesson 9 on SQL Server (docker-compose.yml: sqltoy-sqlserver, localhost:1434).
 *
 * SQLServer-specific conversion paths: date_trunc week via a DATEFIRST-independent
 * ISO-Monday anchor (plain DATEDIFF(week) would silently depend on SET DATEFIRST),
 * 4-arg instr -> REGEXP_INSTR (SQL Server 2025+), to_date -> convert(datetime,...,120),
 * to_char -> FORMAT with CAST AS datetime, trim(from col) -> rtrim(ltrim(col)), and
 * multi-segment group_concat rendered with null-guarded CONCAT instead of "+".
 * Skipped automatically when the database is not reachable with the configured credentials.
 */
@SpringBootTest(classes = SqlToyApplication.class, properties = {
		"spring.datasource.url=" + Lesson09RealDbFunctionConvertTestBase.SQLSERVER_URL,
		"spring.datasource.username=" + Lesson09RealDbFunctionConvertTestBase.SQLSERVER_USER,
		"spring.datasource.password=" + Lesson09RealDbFunctionConvertTestBase.SQLSERVER_PASSWORD,
		"spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver",
		"spring.sql.init.mode=never" })
@EnabledIf("isDbReachable")
class Lesson09SqlServerFunctionConvertTest extends Lesson09RealDbFunctionConvertTestBase {

	static boolean isDbReachable() {
		return dbReachable("com.microsoft.sqlserver.jdbc.SQLServerDriver", SQLSERVER_URL, SQLSERVER_USER,
				SQLSERVER_PASSWORD);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void groupConcatNumericSegmentsConcatNotAdd() {
		List rows = lightDao.find("lesson09_group_concat_numeric", noFilter());
		Object[] row = toArray(rows.get(0));
		System.out.println("[group_concat numeric] c12=" + row[0]);
		// "+" would silently ADD numeric columns (1+2 -> '3'); sqltoy renders a
		// null-guarded CONCAT instead, keeping the MySQL group_concat contract ('12')
		assertEquals("12", str(row[0]));
	}
}
