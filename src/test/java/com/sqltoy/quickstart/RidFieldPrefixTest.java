/**
 * 
 */
package com.sqltoy.quickstart;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.model.EntityQuery;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.sqltoy.quickstart.vo.DictDetailVO;
import com.sqltoy.quickstart.vo.DictTypeVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2020-12-24
 * @modify 2020-12-24,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class RidFieldPrefixTest {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Autowired
	SqlToyCRUDService sqlToyCRUDService;

	@Test
	public void testSave() {
		DictTypeVO dictType = new DictTypeVO();
		dictType.setDictType("SEX_TYPE");
		dictType.setDictTypeName("性别");
		dictType.setStatus(1);
		sqlToyCRUDService.save(dictType);
	}

	@Test
	public void testSaveCascade() {
		DictTypeVO dictType = new DictTypeVO();
		dictType.setDictType("SEX_TYPE");
		dictType.setDictTypeName("性别");
		dictType.setStatus(1);

		List<DictDetailVO> details = new ArrayList<DictDetailVO>();
		DictDetailVO detail1 = new DictDetailVO();
		detail1.setDictKey("F");
		detail1.setDictName("女");
		detail1.setStatus(1);
		details.add(detail1);

		DictDetailVO detail2 = new DictDetailVO();
		detail2.setDictKey("M");
		detail2.setDictName("男");
		detail2.setStatus(1);
		details.add(detail2);
		dictType.setDictDetailVOs(details);

		sqlToyCRUDService.updateCascade(dictType);
	}

	@Test
	public void testUpdate() {
		DictTypeVO dictType = new DictTypeVO();
		dictType.setDictType("SEX_TYPE");
		dictType.setDictTypeName("性别");
		dictType.setComments("性别测试");
		dictType.setStatus(1);
		sqlToyCRUDService.update(dictType);
	}

	@Test
	public void testLoad() {
		DictTypeVO dictType = sqlToyCRUDService.loadCascade(new DictTypeVO("SEX_TYPE"));
		System.err.println(JSON.toJSONString(dictType));
	}

	@Test
	public void testSearch() {

		List<DictTypeVO> dictType = sqlToyLazyDao.findBySql("select * from sqltoy_dict_type", new DictTypeVO());
		System.err.println(JSON.toJSONString(dictType));
	}

	@Test
	public void testFindEntity() {
		List<DictTypeVO> dictType = sqlToyLazyDao.findEntity(DictTypeVO.class,
				EntityQuery.create().where("dictTypeName like ?").values("性"));
		System.err.println(JSON.toJSONString(dictType));
	}
}
