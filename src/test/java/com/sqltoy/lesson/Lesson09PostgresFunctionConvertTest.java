package com.sqltoy.lesson;

import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;

/**
 * Lesson 9 on PostgreSQL (docker-compose.yml: sqltoy-postgres, localhost:5433).
 *
 * PG-specific conversion paths: find_in_set via strpos, to_date -> to_timestamp (so the
 * time part survives), string_agg within group -> the native in-paren order by form,
 * to_char literal cast ('...'::timestamp), regexp_instr for the 4-arg instr (PG 15+).
 * Skipped automatically when the database is not reachable with the configured credentials.
 */
@SpringBootTest(classes = SqlToyApplication.class, properties = {
		"spring.datasource.url=" + Lesson09RealDbFunctionConvertTestBase.PG_URL,
		"spring.datasource.username=" + Lesson09RealDbFunctionConvertTestBase.PG_USER,
		"spring.datasource.password=" + Lesson09RealDbFunctionConvertTestBase.PG_PASSWORD,
		"spring.datasource.driver-class-name=org.postgresql.Driver",
		"spring.sql.init.mode=never" })
@EnabledIf("isDbReachable")
class Lesson09PostgresFunctionConvertTest extends Lesson09RealDbFunctionConvertTestBase {

	static boolean isDbReachable() {
		return dbReachable("org.postgresql.Driver", PG_URL, PG_USER, PG_PASSWORD);
	}
}
