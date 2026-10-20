package com.sqltoy.lesson;

import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;

/**
 * Lesson 9 on MySQL (docker-compose.yml: sqltoy-mysql, localhost:3307).
 *
 * MySQL-specific conversion paths: trim(from col) rewritten to trim(col) (mysql 9.3+
 * rejects the standard form with error 1064), find_in_set native passthrough (including
 * the "=2" position comparison), to_char -> date_format with the framework token
 * conventions (bare HH = 24-hour %H, mm = month %m), to_date -> STR_TO_DATE, 4-arg
 * instr -> REGEXP_INSTR, string_agg within group -> group_concat ORDER BY ... SEPARATOR.
 * Skipped automatically when the database is not reachable with the configured credentials.
 */
@SpringBootTest(classes = SqlToyApplication.class, properties = {
		"spring.datasource.url=" + Lesson09RealDbFunctionConvertTestBase.MYSQL_URL,
		"spring.datasource.username=" + Lesson09RealDbFunctionConvertTestBase.MYSQL_USER,
		"spring.datasource.password=" + Lesson09RealDbFunctionConvertTestBase.MYSQL_PASSWORD,
		"spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
		"spring.sql.init.mode=never" })
@EnabledIf("isDbReachable")
class Lesson09MysqlFunctionConvertTest extends Lesson09RealDbFunctionConvertTestBase {

	static boolean isDbReachable() {
		return dbReachable("com.mysql.cj.jdbc.Driver", MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
	}
}
