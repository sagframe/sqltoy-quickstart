package com.sqltoy.quickstart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.LightDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sqltoy.SqlToyApplication;
import com.sqltoy.quickstart.vo.OrganInfoVO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class ExecuteSql {
	@Autowired
	LightDao lightDao;

	// 基于对象传参模式
	@Test
	public void testExecuteSqlByVO() {
		String sql = "update sqltoy_organ_info set update_time=:updateTime where status=:status #[and organ_id=:organId]";
		OrganInfoVO organInfoVO = new OrganInfoVO();
		organInfoVO.setUpdateTime(LocalDateTime.now());
		organInfoVO.setStatus(1);
		// executeSql 中可以有#[] 根据值确定sql段是否参与执行
		lightDao.executeSql(sql, organInfoVO);
	}

	// 直接?传参
	@Test
	public void testExecuteSql() {
		String sql = "update sqltoy_organ_info set update_time=? where status=? #[and organ_id=?]";
		// executeSql 中可以有#[] 根据值确定sql段是否参与执行
		lightDao.executeSql(sql, null, new Object[] { LocalDateTime.now(), 1, null });
	}

	// 对象传参
	@Test
	public void testBatchUpdateByVO() {
		String sql = "update sqltoy_organ_info set update_time=:updateTime where organ_id=:organId";
		List<OrganInfoVO> entities = new ArrayList<OrganInfoVO>();
		OrganInfoVO organInfoVO = new OrganInfoVO();
		organInfoVO.setUpdateTime(LocalDateTime.now());
		organInfoVO.setOrganId("100011");
		entities.add(organInfoVO);
		// batch操作中sql不允许有#[] ，必须是明确的sql进行批量执行
		lightDao.batchUpdate(sql, entities);
	}

	// 非对象传参
	@Test
	public void testBatchUpdateByAry() {
		String sql = "update sqltoy_organ_info set update_time=? where organ_id=?";
		List entities = new ArrayList();
		List row = new ArrayList();
		row.add(LocalDateTime.now());
		row.add("100011");
		entities.add(row);
		// batch操作中sql不允许有#[] ，必须是明确的sql进行批量执行
		lightDao.batchUpdate(sql, entities);
	}
}
