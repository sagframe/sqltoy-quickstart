/**
 * 
 */
package com.sqltoy.quickstart;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 演示链式操作
 * @author zhong
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
@SpringBootTest
public class LinkOptCaseTest {
	// 链式操作包含:save、load、update、batch(批量更新)、execute(sql执行)、store(存储过程)等

	@Autowired
	LightDao lightDao;

	// 数据库更新保存操作要放入service用事务包裹
	@Test
	public void testSave() {
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setStaffId("S2007");
		staffInfo.setStaffCode("S2007");
		staffInfo.setStaffName("测试员工9");
		staffInfo.setSexType("M");
		staffInfo.setEmail("test3@aliyun.com");
		staffInfo.setEntryDate(LocalDate.now());
		staffInfo.setStatus(1);
		staffInfo.setOrganId("100007");

		// 单条记录保存
		lightDao.save().one(staffInfo);
		// 动态设置数据库、批量、保存模式(ignore、update)
		// sqlToyLazyDao.save().dataSource(dataSource).saveMode(SaveMode.IGNORE).many(entities);
	}

	@Test
	public void testQuery() {
		StaffInfoVO staffVO = new StaffInfoVO();
		// 作为查询条件传参数
		staffVO.setStaffName("陈");
		// resultType可以额外指定结果类型
		List result = lightDao.query().sql("qstart_fastPage").entity(staffVO).resultType(StaffInfoVO.class).find();
	}

	@Test
	public void testPageQuery() {
		Page pageModel = new Page();
		StaffInfoVO staffVO = new StaffInfoVO();
		// 作为查询条件传参数
		staffVO.setStaffName("陈");
		Page result = lightDao.query().sql("qstart_fastPage").entity(staffVO).findPage(pageModel);
		// 查询可以链式操作
		//sqlToyLazyDao.query().sql("qstart_fastPage").rowhandler(rowCallbackHandler).dataSource(dataSource).entity(staffVO).findPage(pageModel);
	}

}
