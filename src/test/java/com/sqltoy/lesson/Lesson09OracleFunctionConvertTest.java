package com.sqltoy.lesson;

import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;

/**
 * Lesson 9 on Oracle (docker-compose.yml: sqltoy-oracle, localhost:1521/freepdb1).
 *
 * Oracle-specific conversion paths: trim(from col) rewritten to trim(col) (Oracle 23ai
 * rejects the standard form with ORA-30000), left/right(str,0) -> substr(str,1,0) i.e.
 * empty (a substr(str,-0) rewrite would return the WHOLE string), to_char literal first
 * arg wrapped into to_date, find_in_set via instr + || concatenation, string_agg ->
 * listagg within group, date_trunc week -> TRUNC(...,'IW').
 * Skipped automatically when the database is not reachable with the configured credentials.
 */
@SpringBootTest(classes = SqlToyApplication.class, properties = {
		"spring.datasource.url=" + Lesson09RealDbFunctionConvertTestBase.ORACLE_URL,
		"spring.datasource.username=" + Lesson09RealDbFunctionConvertTestBase.ORACLE_USER,
		"spring.datasource.password=" + Lesson09RealDbFunctionConvertTestBase.ORACLE_PASSWORD,
		"spring.datasource.driver-class-name=oracle.jdbc.OracleDriver",
		"spring.sql.init.mode=never" })
@EnabledIf("isDbReachable")
class Lesson09OracleFunctionConvertTest extends Lesson09RealDbFunctionConvertTestBase {

	static boolean isDbReachable() {
		return dbReachable("oracle.jdbc.OracleDriver", ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
	}
}
