package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.lesson.entity.StaffMember;
import com.sqltoy.SqlToyApplication;

/**
 * Lesson 4 — cache translation: skip the dictionary JOIN.
 *
 * lesson04_staff_with_names contains no JOIN at all. The organIdName and
 * SEX_TYPE dictionary caches (see sqltoy-translate.xml) resolve codes to names
 * in memory after the query returns.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson04CacheTranslateTest {

	@Autowired
	LightDao lightDao;

	@Test
	void namesResolvedWithoutJoin() {
		List<StaffMember> rows = lightDao.find("lesson04_staff_with_names",
				MapKit.keys("staffName").values("%Chen%"), StaffMember.class);

		System.out.println("staffId  organId -> organName      sexType -> sexTypeName");
		rows.forEach(r -> System.out.println("  " + r.getStaffId() + "  " + r.getOrganId() + " -> "
				+ r.getOrganName() + "  " + r.getSexType() + " -> " + r.getSexTypeName()));

		assertEquals(2, rows.size());
		assertEquals("R&D Center", rows.get(0).getOrganName());
		assertEquals("Female", rows.get(0).getSexTypeName());
		assertTrue(rows.stream().anyMatch(r -> "Male".equals(r.getSexTypeName())));
	}
}
