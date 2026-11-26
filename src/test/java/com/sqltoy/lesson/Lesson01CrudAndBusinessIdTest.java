package com.sqltoy.lesson;

import java.math.BigDecimal;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * 第 1 课 — 无 DAO 样板代码的 CRUD，以及 Redis 生成的业务主键。
 *
 * <p>下方启动的嵌入式 Redis 可替代本机安装的 Redis：sqltoy 的
 * <p> <code>RedisIdGenerator</code> 只需要一个名为 <code>redisTemplate</code> 的 Bean，以及可连通的端口。
 */
@DisplayName("第 1 课 — 无 DAO 样板代码的 CRUD，以及 Redis 生成的业务主键。")
@SpringBootTest(classes = SqlToyApplication.class)
@Slf4j
class Lesson01CrudAndBusinessIdTest {

	/** PO + yyMMdd + 8 位 Redis 序列 */
	private static final Pattern ORDER_ID_PATTERN = Pattern.compile("PO\\d{14}");

    /**
	 * 嵌入式 Redis 实例
	 */
	private static RedisServer redis;

	final private LightDao lightDao;
	final private SqlToyCRUDService crudService;

    /**
	 * 构造函数
	 * @param lightDao LightDao 实例
	 * @param crudService SqlToyCRUDService 实例
	 */
	@Autowired
    Lesson01CrudAndBusinessIdTest(LightDao lightDao, SqlToyCRUDService crudService) {
        this.lightDao = lightDao;
        this.crudService = crudService;
    }

    /**
	 * 启动嵌入式 Redis
	 * <p>在 Spring 上下文启动之前：sqltoy 通过 spring.data.redis 获取端口
	 */
	@DynamicPropertySource
	static void startEmbeddedRedis(DynamicPropertyRegistry registry) {
		// 让操作系统选择一个空闲端口：一个固定的端口会将一个无关的本地服务变成一个硬错误
		int port;
		try (var probe = new ServerSocket(0)) {
			port = probe.getLocalPort();
		} catch (Exception e) {
			throw new IllegalStateException("no free port for embedded redis", e);
		}

		// 启动嵌入式 Redis
		try {
			redis = new RedisServer(port);
			redis.start();
		} catch (Exception e) {
			throw new IllegalStateException("failed to start embedded redis", e);
		}
		// 添加端口到动态属性注册表
		registry.add("spring.data.redis.port", () -> port);
	}

	@AfterAll
	static void stopEmbeddedRedis() throws Exception {
		if (redis != null) {
			redis.stop();
		}
	}

	@Test
	@DisplayName("无 DAO 样板代码的 CRUD")
	void crudWithoutDaoClasses() {
		// staffId 为 null，所以 22 位生成器分配它；
		// createBy/createTime 由配置的 UnifyFieldsHandler 填充
		final var staff = new StaffMember()
				.setStaffCode("L001")
				.setStaffName("Lesson One")
				.setOrganId("100005")
				.setSexType("M")
				.setEntryDate(LocalDate.now())
				.setStatus(1)
				.setEmail("lesson1@example.com");

		// 保存
		crudService.save(staff);

	    // 打印生成的 staffId 和自动填充的 createBy/createTime
        log.info("generated staffId      = {}", staff.getStaffId());
        log.info("auto-filled createBy   = {}", staff.getCreateBy());
        log.info("auto-filled createTime = {}", staff.getCreateTime());

		// 通过主键加载
		final var loaded = crudService.load(new StaffMember().setStaffId(staff.getStaffId()));
		assertEquals("Lesson One", loaded.getStaffName());

		// 弹性更新：null 字段会被跳过 — 一次交互，在并发环境下安全
		loaded.setEmail("lesson1-updated@example.com");
		crudService.update(loaded);

		final var reloaded = crudService.load(new StaffMember().setStaffId(staff.getStaffId()));
		assertEquals("lesson1-updated@example.com", reloaded.getEmail());
		// organId 在更新实体时为 null，所以它保持其旧值
		assertEquals("100005", reloaded.getOrganId());

		crudService.delete(new StaffMember().setStaffId(staff.getStaffId()));
		assertNull(lightDao.loadById(StaffMember.class, staff.getStaffId()));
	}

	@Test
	@DisplayName("Redis 生成的业务主键")
	void redisBusinessPrimaryKey() {
		final var orderNum = 3;
		List<String> orderIds = new ArrayList<>(orderNum);
		for (var i = 1; i <= orderNum; i++) {
			final var order = new DeviceOrder()
					.setBuyer("C1000" + i)
					.setOrganId("100007")
					.setDeviceType("LAPTOP")
					.setTransDate(LocalDate.now())
					.setTotalAmt(BigDecimal.valueOf(9800 + i))
					.setStatus(1);
			lightDao.save(order);
			orderIds.add(order.getOrderId());
            log.info("orderId #{} = {}", i, order.getOrderId());
		}
		// 验证订单 ID：PO + yyMMdd + 8 位 Redis 序列
		orderIds.forEach(id -> assertTrue(ORDER_ID_PATTERN.matcher(id).matches(), "unexpected key: " + id));
	}
}
