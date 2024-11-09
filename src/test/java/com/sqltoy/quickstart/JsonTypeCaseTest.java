/**
 * 
 */
package com.sqltoy.quickstart;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSON;
import com.sqltoy.quickstart.vo.JsontypeShowcaeVO;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 演示json类型的操作
 * @author zhong
 * @version v1.0, Date:2020-11-26
 * @modify 2020-11-26,修改说明
 */
@SpringBootTest
public class JsonTypeCaseTest {
	@Autowired
	private SqlToyCRUDService sqlToyCRUDService;

	@Test
	public void testSave() {
		JsontypeShowcaeVO jsonTypeVO = new JsontypeShowcaeVO();
		jsonTypeVO.setId("100001");
		List<StaffInfoVO> details = new ArrayList<StaffInfoVO>();
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setStaffId("S190715001");
		staffInfo.setOrganId("C0002");
		staffInfo.setAddress("测试地址");
		details.add(staffInfo);
		jsonTypeVO.setStaffSet(details);
		sqlToyCRUDService.save(jsonTypeVO);
	}

	@Test
	public void testSaveOrUpdate() {
		List typeVOs = new ArrayList();
		JsontypeShowcaeVO dTypeVO = new JsontypeShowcaeVO();
		dTypeVO.setId("100001");
		List<StaffInfoVO> details = new ArrayList<StaffInfoVO>();
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setStaffId("S190715001");
		staffInfo.setOrganId("C0002");
		staffInfo.setAddress("测试地址");
		details.add(staffInfo);
		dTypeVO.setStaffSet(details);

		JsontypeShowcaeVO dTypeVO1 = new JsontypeShowcaeVO();
		List<StaffInfoVO> details1 = new ArrayList<StaffInfoVO>();
		StaffInfoVO staffInfo1 = new StaffInfoVO();
		staffInfo1.setStaffId("S190715001");
		staffInfo1.setOrganId("C0002");
		staffInfo1.setAddress("测试地址");
		details1.add(staffInfo1);
		dTypeVO1.setStaffSet(details1);
		typeVOs.add(dTypeVO);
		typeVOs.add(dTypeVO1);
		sqlToyCRUDService.saveOrUpdateAll(typeVOs);
	}

	@Test
	public void testUpdate() {
		JsontypeShowcaeVO dTypeVO = sqlToyCRUDService.load(new JsontypeShowcaeVO("100001"));
		List<StaffInfoVO> details = new ArrayList<StaffInfoVO>();
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setStaffId("S190715001");
		staffInfo.setOrganId("C0002");
		staffInfo.setAddress("测试地址1");
		details.add(staffInfo);
		dTypeVO.setStaffSet(details);
		System.err.println(JSON.toJSONString(dTypeVO));
		sqlToyCRUDService.update(dTypeVO);
	}

	@Test
	public void testLoad() {
		JsontypeShowcaeVO dTypeVO = sqlToyCRUDService.load(new JsontypeShowcaeVO("100001"));
		System.err.println(JSON.toJSONString(dTypeVO));
	}
}
