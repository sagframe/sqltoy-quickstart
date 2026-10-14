package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
 * Lesson 7 — reports without ETL.
 *
 * Three xml-only transforms over the same sales query:
 *  - pivot: months become column headings (two measures under each month)
 *  - summary: per-product subtotals and a grand-total row
 *  - cols-chain-relative: month-over-month percentages between pivot columns
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson07ReportTest {

	@Autowired
	LightDao lightDao;

	private Map<String, Object> noFilter() {
		return new HashMap<>();
	}

	@Test
	@SuppressWarnings("rawtypes")
	void pivotMonthsIntoColumns() {
		List rows = lightDao.find("lesson07_sales_pivot", noFilter());
		System.out.println("pivot: " + rows.size() + " rows");
		rows.forEach(r -> System.out.println("  " + row(r)));
		// one row per product; columns = product + 3 months x 2 measures
		assertEquals(3, rows.size());
		Object[] first = toArray(rows.get(0));
		assertEquals(7, first.length);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void subtotalsAndGrandTotal() {
		List rows = lightDao.find("lesson07_sales_summary", noFilter());
		System.out.println("summary: " + rows.size() + " rows");
		rows.forEach(r -> System.out.println("  " + row(r)));
		// grand total + 3 x (subtotal + 3 detail rows)
		assertEquals(13, rows.size());
		boolean hasTotal = false, hasSubtotal = false;
		for (Object r : rows) {
			String label = String.valueOf(toArray(r)[0]);
			hasTotal |= "Total".equals(label);
			hasSubtotal |= "Subtotal".equals(label);
		}
		assertTrue(hasTotal);
		assertTrue(hasSubtotal);
	}

	@Test
	@SuppressWarnings("rawtypes")
	void monthOverMonthColumns() {
		List rows = lightDao.find("lesson07_sales_mom", noFilter());
		System.out.println("month-over-month: " + rows.size() + " rows");
		rows.forEach(r -> System.out.println("  " + row(r)));
		// pivot columns + one relative column per month (after the first)
		assertEquals(3, rows.size());
	}

	private Object[] toArray(Object r) {
		return (r instanceof Object[]) ? (Object[]) r : ((List<?>) r).toArray();
	}

	private String row(Object r) {
		// pivot and summary rows come back as List (raw rows would be Object[])
		Object[] a = toArray(r);
		StringBuilder sb = new StringBuilder();
		for (Object v : a) {
			sb.append(v == null ? "-" : v).append("\t");
		}
		return sb.toString();
	}
}
