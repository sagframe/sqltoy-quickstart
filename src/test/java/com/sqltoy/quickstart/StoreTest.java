/**
 * 
 */
package com.sqltoy.quickstart;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.LightDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.sqltoy.SqlToyApplication;
import com.sqltoy.quickstart.service.StaffInfoService;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2020年8月13日
 * @modify 2020年8月13日,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class StoreTest {
	@Autowired
	LightDao lightDao;
	@Autowired
	StaffInfoService staffInfoService;

	// 注意要先创建存储过程
//	CREATE PROCEDURE sp_showcase(IN userId int,IN endDate datetime )
//	BEGIN
//	 select * from sqltoy_staff_info;
//	END;

	@Test
	public void testCallStore() {
		List<StaffInfoVO> result = staffInfoService.callStore();
		for (StaffInfoVO staff : result) {
			System.err.println(JSON.toJSONString(staff));
		}
	}

	@Test
	public void testCallStoreBySql() {
		List<StaffInfoVO> result = (List<StaffInfoVO>) lightDao
				.executeStore("{ call sp_showcase(?,?)}", new Object[] { 1, null }, null, StaffInfoVO.class).getRows();
		for (StaffInfoVO staff : result) {
			System.err.println(JSON.toJSONString(staff));
		}
	}
}
