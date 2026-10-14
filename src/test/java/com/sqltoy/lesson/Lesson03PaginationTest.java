package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.lesson.entity.StaffMember;
import com.sqltoy.SqlToyApplication;

/**
 * Lesson 3 — pagination that optimizes itself.
 *
 * lesson03_staff_page uses:
 *  - page-optimize: the count is cached, so page 2 does not re-query it
 *  - @fast(): the inner query pages first, then joins sqltoy_organ_info
 *  - parallel="true": count and page rows execute concurrently
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson03PaginationTest {

	@Autowired
	LightDao lightDao;

	@Test
	void pagedQueryWithFastJoin() {
		Page pageModel = new Page();
		pageModel.setPageSize(5);
		Map<String, Object> noFilter = new HashMap<>();

		Page<StaffMember> page1 = lightDao.findPage(pageModel, "lesson03_staff_page",
				noFilter, StaffMember.class);
		System.out.println("page 1: recordCount=" + page1.getRecordCount() + ", rows=" + page1.getRows().size());
		page1.getRows().forEach(row -> System.out.println("  " + row.getStaffId() + "\t" + row.getStaffName()
				+ "\tentry " + row.getEntryDate() + "\t" + row.getOrganName()));

		assertEquals(19, page1.getRecordCount());
		assertEquals(5, page1.getRows().size());
		// newest employees first: Samuel Ma and Tina Cao's colleagues
		assertEquals("S0019", page1.getRows().get(0).getStaffId());

		// page 2 with identical conditions: the cached count is reused (see console)
		pageModel.setPageNo(2);
		Page<StaffMember> page2 = lightDao.findPage(pageModel, "lesson03_staff_page",
				noFilter, StaffMember.class);
		System.out.println("page 2: recordCount=" + page2.getRecordCount() + ", rows=" + page2.getRows().size());
		assertEquals(5, page2.getRows().size());
	}
}
