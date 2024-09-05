/**
 * 
 */
package com.sqltoy.quickstart;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.CacheMatchFilter;
import org.sagacity.sqltoy.model.ColumnMeta;
import org.sagacity.sqltoy.model.MapKit;
import org.sagacity.sqltoy.model.TableMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson2.JSON;
import com.sqltoy.SqlToyApplication;
import com.sqltoy.quickstart.service.OrganInfoService;
import com.sqltoy.quickstart.vo.OrganInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhongxuchen
 * @version v1.0, Date:2020-7-20
 * @modify 2020-7-20,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class TreeTableTest {
	@Autowired
	OrganInfoService organInfoService;

	@Autowired
	LightDao lightDao;

	/**
	 * @TODO 通过保存机构演示节点路径、节点等级、是否叶子节点等数据的生成
	 */
	@Test
	public void testWrapTreeTable() {
		OrganInfoVO organ = new OrganInfoVO();
		organ.setOrganId("100008");
		organ.setOrganCode("S100008");
		organ.setOrganPid("S100002");
		organ.setOrganName("新动力研发中心");
		organ.setStatus(1);
		organInfoService.saveOrganInfo(organ);
	}

	@Test
	public void searchTree() {
		List<OrganInfoVO> organs = lightDao.find("qstart_treeTable_search", MapKit.map("nodeRoute", "100007"),
				OrganInfoVO.class);
		for (OrganInfoVO vo : organs) {
			System.err.println(JSON.toJSONString(vo));
		}
	}

	@Test
	public void getTables() {
		List<TableMeta> tables = lightDao.tableApi().getTables(null, null, "%");
		for (TableMeta vo : tables) {
			System.err.println(JSON.toJSONString(vo));
		}
	}

	@Test
	public void getTableColumns() {
		List<ColumnMeta> tables = lightDao.tableApi().getTableColumns(null, null, "SQLTOY_DICT_DETAIL");
		for (ColumnMeta vo : tables) {
			System.err.println(JSON.toJSONString(vo));
		}
	}

	@Test
	public void testMatchKey() {
		String[] keys = lightDao.cacheMatchKeys(CacheMatchFilter.create().cacheName("organIdName").matchIndexs(1)
				.cacheKeyIndex(0).priorMatchEqual(true).matchSize(2), "新能源研究院");
		for (String key : keys) {
			System.err.println(key);
		}
	}
}
