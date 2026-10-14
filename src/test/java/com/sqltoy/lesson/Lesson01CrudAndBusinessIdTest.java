package com.sqltoy.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.sqltoy.lesson.entity.DeviceOrder;
import com.sqltoy.lesson.entity.StaffMember;
import com.sqltoy.SqlToyApplication;

import redis.embedded.RedisServer;

/**
 * Lesson 1 — CRUD without DAO boilerplate, and a Redis-generated business primary key.
 *
 * The embedded Redis started below replaces a real Redis install: sqltoy's
 * RedisIdGenerator only needs a bean named "redisTemplate" and a reachable port.
 */
@SpringBootTest(classes = SqlToyApplication.class)
class Lesson01CrudAndBusinessIdTest {

	private static RedisServer redis;

	// starts before the Spring context: sqltoy picks the port up via spring.data.redis
	@DynamicPropertySource
	static void startEmbeddedRedis(DynamicPropertyRegistry registry) {
		try {
			redis = new RedisServer(16379);
			redis.start();
		} catch (Exception e) {
			throw new IllegalStateException("failed to start embedded redis", e);
		}
		registry.add("spring.data.redis.port", () -> 16379);
	}

	@AfterAll
	static void stopEmbeddedRedis() throws Exception {
		if (redis != null) {
			redis.stop();
		}
	}

	@Autowired
	LightDao lightDao;

	@Autowired
	SqlToyCRUDService crudService;

	@Test
	void crudWithoutDaoClasses() {
		// insert: staffId is null, so the 22-digit generator assigns it;
		// createBy/createTime are filled by the configured UnifyFieldsHandler
		StaffMember staff = new StaffMember().setStaffCode("L001").setStaffName("Lesson One")
				.setOrganId("100005").setSexType("M").setEntryDate(LocalDate.now()).setStatus(1)
				.setEmail("lesson1@example.com");
		crudService.save(staff);
		System.out.println("generated staffId      = " + staff.getStaffId());
		System.out.println("auto-filled createBy   = " + staff.getCreateBy());
		System.out.println("auto-filled createTime = " + staff.getCreateTime());

		// load by primary key
		StaffMember loaded = crudService.load(new StaffMember().setStaffId(staff.getStaffId()));
		assertEquals("Lesson One", loaded.getStaffName());

		// elastic update: null fields are skipped — one interaction, safe under concurrency
		loaded.setEmail("lesson1-updated@example.com");
		crudService.update(loaded);
		StaffMember reloaded = crudService.load(new StaffMember().setStaffId(staff.getStaffId()));
		assertEquals("lesson1-updated@example.com", reloaded.getEmail());
		// organId was null on the update entity, so it keeps its old value
		assertEquals("100005", reloaded.getOrganId());

		crudService.delete(new StaffMember().setStaffId(staff.getStaffId()));
		assertEquals(0, lightDao.loadByIds(StaffMember.class, staff.getStaffId()).size());
	}

	@Test
	void redisBusinessPrimaryKey() {
		List<String> orderIds = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			DeviceOrder order = new DeviceOrder().setBuyer("C1000" + i).setOrganId("100007")
					.setDeviceType("LAPTOP").setTransDate(LocalDate.now())
					.setTotalAmt(BigDecimal.valueOf(9800 + i)).setStatus(1);
			lightDao.save(order);
			orderIds.add(order.getOrderId());
			System.out.println("orderId #" + i + " = " + order.getOrderId());
		}
		// PO + yyMMdd + 8-digit zero-padded Redis sequence
		orderIds.forEach(id -> assertTrue(id.matches("PO\\d{14}"), "unexpected key: " + id));
	}
}
