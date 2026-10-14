package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.lesson.gen.StaffInfoVO;

/**
 * The exhibit package com.sqltoy.lesson.gen is produced by `mvn quickvo:quickvo`.
 * This test proves the generated code is fully functional: CRUD works out of the box,
 * the primary-key generator and the audit-field auto-fill engage as with hand-written entities.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class GeneratedEntityTest {

	@Autowired
	LightDao lightDao;

	@Autowired
	SqlToyCRUDService crudService;

	@Test
	void quickvoGeneratedEntityIsFullyFunctional() {
		StaffInfoVO staff = new StaffInfoVO();
		staff.setStaffCode("GEN01");
		staff.setStaffName("Generated Entity");
		staff.setOrganId("100005");
		staff.setSexType("F");
		staff.setEntryDate(LocalDate.now());
		staff.setStatus(1);

		// insert: generated 22-digit id + audit fields auto-filled
		crudService.save(staff);
		assertNotNull(staff.getStaffId());
		System.out.println("generated id  = " + staff.getStaffId());
		System.out.println("createBy      = " + staff.getCreateBy());
		System.out.println("createTime    = " + staff.getCreateTime());
		assertEquals("system-auto", staff.getCreateBy());

		// load by the generated PK constructor
		StaffInfoVO loaded = crudService.load(new StaffInfoVO(staff.getStaffId()));
		assertEquals("Generated Entity", loaded.getStaffName());
		assertEquals("100005", loaded.getOrganId());

		// elastic update and delete
		loaded.setStaffName("Generated Entity Updated");
		crudService.update(loaded);
		assertEquals("Generated Entity Updated", crudService.load(new StaffInfoVO(staff.getStaffId())).getStaffName());
		crudService.delete(new StaffInfoVO(staff.getStaffId()));
		assertNull(crudService.load(new StaffInfoVO(staff.getStaffId())));
	}
}
