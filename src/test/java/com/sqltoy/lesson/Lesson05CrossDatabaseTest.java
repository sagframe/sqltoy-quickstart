package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.lesson.dto.OrderTotal;
import com.sqltoy.lesson.entity.OrganInfo;

/**
 * Lesson 5 — one SQL, multiple databases.
 *
 * This lesson runs Oracle-style SQL on H2. With functionConverts=default:
 *  - nvl(...) is rewritten to coalesce(...) when the sql is loaded
 *    (the console shows the converted SQL actually executed)
 *  - @virtualTable() expands to "from dual" on Oracle-family databases and is
 *    stripped on H2/MySQL/PostgreSQL, so the same sql file runs everywhere
 * Change nothing but the datasource URL and these statements keep working.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson05CrossDatabaseTest {

	@Autowired
	LightDao lightDao;

	@Test
	void oracleFunctionRunsOnH2() {
		// "nvl" does not exist in H2 — sqltoy converts it to coalesce at load time
		Map<String, Object> noFilter = new HashMap<>();
		List<OrderTotal> rows = lightDao.find("lesson05_nvl_style", noFilter, OrderTotal.class);
		System.out.println("nvl(sum(TOTAL_AMT),0) executed on H2 = " + rows.get(0).getTotalAmt());
		assertEquals(0, rows.get(0).getTotalAmt().compareTo(BigDecimal.valueOf(294100)));
	}

	@Test
	void virtualTableWorksOnEveryDatabase() {
		Map<String, Object> noFilter = new HashMap<>();
		List<OrganInfo> rows = lightDao.find("lesson05_virtual_table", noFilter, OrganInfo.class);
		rows.forEach(r -> System.out.println(r.getOrganId() + "\t" + r.getOrganName()));
		// the literal row (from the @virtualTable() select) + the 3 seeded organs
		assertEquals(4, rows.size());
		assertTrue(rows.stream().anyMatch(r -> "HQ".equals(r.getOrganId())));
	}
}
