/**
 * 
 */
package com.sqltoy.quickstart;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.config.model.Translate;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.model.EntityQuery;
import org.sagacity.sqltoy.model.PaginationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.sqltoy.quickstart.service.StaffInfoService;
import com.sqltoy.quickstart.vo.DeviceOrderVO;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 演示基于POJO类型的操作
 * @author zhong
 * @version v1.0, Date:2020-11-18
 * @modify 2020-11-18,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class EntityOptsCaseTest {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Autowired
	StaffInfoService staffInfoService;

	// 通过POJO的查询where和select 部分可以直接写字段名称和也可以写POJO字段的属性名称,也可以混合
	// 原理就是sqltoy会将字段属性名替换成对应的数据库字段名称

	/**
	 * @TODO 演示单表查询功能，其包含缓存翻译
	 */
	@Test
	public void testEntityQuery() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[ORDER_ID=:orderId] #[and ORGAN_ID in (:authedOrganIds)] #[and STAFF_ID in (:staffIds)] #[and TRANS_DATE>=:beginDate] #[and TRANS_DATE<:endDate]";
		// sqltoy中参数为null可以无需传参
		List<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				EntityQuery.create().where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null));
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// sqltoy中参数为null可以无需传参
	@Test
	public void testEntityQuerySkipNull() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[ORDER_ID=:orderId] #[and ORGAN_ID in (:authedOrganIds)] #[and STAFF_ID in (:staffIds)] #[and TRANS_DATE>=:beginDate] #[and TRANS_DATE<:endDate]";
		List<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				EntityQuery.create().where(where).names("authedOrganIds", "staffName", "beginDate")
						.values(authedOrgans, "陈", LocalDate.parse("2018-09-01")).top(10));
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 演示一下map传参模式
	@Test
	public void testEntityQueryMap() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[ORDER_ID=:orderId] #[and ORGAN_ID in (:authedOrganIds)] #[and STAFF_ID in (:staffIds)] #[and TRANS_DATE>=:beginDate] #[and TRANS_DATE<:endDate]";
		// sqltoy中参数为null可以无需传参
		Map params = new HashMap() {
			{
				put("authedOrganIds", authedOrgans);
				put("staffName", "陈");
				put("beginDate", LocalDate.parse("2018-09-01"));
			}
		};
		List<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				EntityQuery.create().where(where).paramsMap(params).top(20));
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 演示一下对象传参
	// 演示带缓存翻译等场景的POJO类型模式，其他功能请通过EntityQuery进行展开，可以基本代替xml中的功能
	@Test
	public void testEntityQueryHasTranslate() {
		String sql = "#[staffName like :staffName]#[and createTime>=:beginDate]#[and createTime<=:endDate]";
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setBeginDate(LocalDate.parse("2019-01-01"));
		staffInfo.setEndDate(LocalDate.now());
		staffInfo.setStaffName("陈");
		PaginationModel<StaffInfoVO> result = sqlToyLazyDao.findEntity(StaffInfoVO.class, new PaginationModel(10, 1L),
				EntityQuery.create().where(sql).values(staffInfo)
						// 字典缓存必须要设置cacheType
						// 单表对象查询需设置keyColumn构成select keyColumn as column模式
						.translates(new Translate("dictKeyName").setColumn("sexTypeName").setCacheType("SEX_TYPE")
								.setKeyColumn("sexType"))
						.translates(new Translate("organIdName").setColumn("organName").setKeyColumn("organId")));
		result.getRows().forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 取top记录
	@Test
	public void testEntityQueryTop() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[orderId=:orderId] #[and organId in (:authedOrganIds)] #[and staffId in (:staffIds)] #[and transDate>=:beginDate] #[and transDate<:endDate]";
		List<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				EntityQuery.create().where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null).top(20));
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 取随机记录
	@Test
	public void testEntityQueryRandom() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[orderId=:orderId] #[and organId in (:authedOrganIds)] #[and staffId in (:staffIds)] #[and transDate>=:beginDate] #[and transDate<:endDate]";
		List<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				EntityQuery.create().where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null).random(15));
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 简单分页
	@Test
	public void testEntityQueryPage() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[ORDER_ID=:orderId] #[and ORGAN_ID in (:authedOrganIds)] #[and STAFF_ID in (:staffIds)] #[and TRANS_DATE>=:beginDate] #[and TRANS_DATE<:endDate]";
		PaginationModel<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				new PaginationModel(10, 1L),
				EntityQuery.create().where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null));
		result.getRows().forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 演示可以指定字段
	@Test
	public void testEntityQueryFields() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[orderId=:orderId] #[and organId in (:authedOrganIds)] #[and staffId in (:staffIds)] #[and transDate>=:beginDate] #[and transDate<:endDate]";
		PaginationModel<DeviceOrderVO> result = sqlToyLazyDao.findEntity(DeviceOrderVO.class,
				new PaginationModel(10, 1L),
				// select 指定查询字段
				// 支持select().field1().field2() 链式模式,数据库表结构变更自动报错
				// 支持:select("field1","fields2")数组模式，书写较为麻烦，可以是POJO的属性名称
				// 支持:select("field1,fields2") 单字符串逗号分隔模式,书写比较简单，可以是POJO的属性名称
				EntityQuery.create().select("orderId,deviceType,organId").where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null));
		result.getRows().forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	// 演示取count记录
	@Test
	public void testEntityQueryCount() {
		String[] authedOrgans = { "100004", "100007" };
		String where = "#[orderId=:orderId] #[and organId in (:authedOrganIds)] #[and staffId in (:staffIds)] #[and transDate>=:beginDate] #[and transDate<:endDate]";
		Long result = sqlToyLazyDao.getCount(DeviceOrderVO.class,
				EntityQuery.create().where(where)
						.names("orderId", "authedOrganIds", "staffName", "beginDate", "endDate")
						.values(null, authedOrgans, "陈", LocalDate.parse("2018-09-01"), null));
		System.err.println(result);
	}

	/**
	 * 无查询条件
	 */
	@Test
	public void testEntityQueryCountNoConditions() {
		Long result = sqlToyLazyDao.getCount(DeviceOrderVO.class, null);
		System.err.println(result);
	}

	/**
	 * 因为涉及事务问题，数据库修改和删除等操作必须包含在service的事务上，所以要进入具体代码中阅读
	 */
	@Test
	public void testUpdateByEntityQuery() {
		staffInfoService.updateByQuery();
	}

	@Test
	public void testDeleteByEntityQuery() {
		staffInfoService.deleteByQuery();
	}
}
