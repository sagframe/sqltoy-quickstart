/**
 * 
 */
package com.sqltoy.quickstart;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.sqltoy.quickstart.vo.SecureCaseVO;

/**
 * @author zhongxuchen
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class SecureEncryptDecryptTest {

	@Autowired
	SqlToyCRUDService sqlToyCRUDService;
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Test
	public void save() {
		SecureCaseVO vo = new SecureCaseVO();
		vo.setStaffId("S0001");
		vo.setStaffName("测试");
		vo.setTelNo("13918765443");
		vo.setHomeAddress("江苏省南京市栖霞区公平街道解放小区345弄89号2987室");
		sqlToyCRUDService.save(vo);
	}

	@Test
	public void update() {
		SecureCaseVO entity = new SecureCaseVO();
		entity.setStaffId("S0001");
		entity.setStaffName("测试1");
		entity.setTelNo("13918765442");
		entity.setHomeAddress("江苏省苏州市栖霞区公平街道解放小区345弄89号2987室");
		sqlToyCRUDService.update(entity);
	}

	@Test
	public void load() {
		SecureCaseVO entity = sqlToyCRUDService.load(new SecureCaseVO("S0001"));
		System.err.println(JSON.toJSONString(entity));
	}

	// 演示结果类型是POJO的，自动将跟pojo上加密的属性名一致的解密
	@Test
	public void search() {
		List<SecureCaseVO> result = sqlToyLazyDao.findBySql("select * from sqltoy_secure_case", null,
				SecureCaseVO.class);
		System.err.println(JSON.toJSONString(result));
	}

	/**
	 * 演示在xml中通过<secure-decrypt columns="tel_no,home_address" /> 告知对哪些字段解密
	 */
	@Test
	public void searchByXML() {
		List result = sqlToyLazyDao.findBySql("qstart_secure_decrypt", null, null, null);
		System.err.println(JSON.toJSONString(result));
	}
}
