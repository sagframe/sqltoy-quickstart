package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.lesson.entity.StaffMember;
import com.sqltoy.SqlToyApplication;

/**
 * Lesson 2 — optional search filters without <if>/<foreach> blocks.
 *
 * Each #[...] fragment in lesson02_staff_search joins the SQL only when its
 * parameter is non-null. Watch the executed SQL in the console: it changes
 * with the parameter set, and every value goes through a JDBC "?" placeholder.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson02DynamicFiltersTest {

	@Autowired
	LightDao lightDao;

	@Test
	void optionalFilters() {
		// 1) name filter only — matches Alice Chen and Jack Chen
		Map<String, Object> nameOnly = MapKit.keys("staffName").values("%Chen%");
		List<StaffMember> byName = lightDao.find("lesson02_staff_search", nameOnly, StaffMember.class);
		System.out.println("filter: staffName like '%Chen%'  -> " + byName.size() + " rows");
		assertEquals(2, byName.size());

		// 2) organization filter only — everyone in R&D Center (100005)
		Map<String, Object> organOnly = MapKit.keys("organIds").values(new String[] { "100005" });
		List<StaffMember> byOrgan = lightDao.find("lesson02_staff_search", organOnly, StaffMember.class);
		System.out.println("filter: organ_id in (100005)    -> " + byOrgan.size() + " rows");
		assertEquals(8, byOrgan.size());

		// 3) several filters together
		Map<String, Object> combined = MapKit.keys("organIds", "beginDate", "endDate")
				.values(new String[] { "100005", "100007" }, LocalDate.parse("2021-01-01"), LocalDate.parse("2023-12-31"));
		List<StaffMember> combinedRows = lightDao.find("lesson02_staff_search", combined, StaffMember.class);
		System.out.println("filter: 2 organs + date range   -> " + combinedRows.size() + " rows");

		// 4) no filter at all — every active employee (status=0 Tina Cao is excluded)
		Map<String, Object> noFilter = new HashMap<>();
		List<StaffMember> everyone = lightDao.find("lesson02_staff_search", noFilter, StaffMember.class);
		System.out.println("no optional filter              -> " + everyone.size() + " rows");
		assertEquals(19, everyone.size());
	}
}
