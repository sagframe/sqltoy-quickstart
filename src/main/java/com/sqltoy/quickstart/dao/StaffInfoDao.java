package com.sqltoy.quickstart.dao;

import org.sagacity.sqltoy.config.model.Translate;
import org.sagacity.sqltoy.model.EntityQuery;
import org.sagacity.sqltoy.model.Page;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.sagacity.sqltoy.support.SpringDaoSupport;
import org.springframework.stereotype.Repository;

import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * Demo of how to write a DAO. Normally sqltoy needs no DAO layer at all —
 * services call LightDao / SqlToyCRUDService directly.
 *
 * @author zhongxuchen
 * @version 1.0.0, Date:2020-07-16
 */
@Repository("staffInfoDao")
public class StaffInfoDao extends SpringDaoSupport {
	/**
	 * @TODO paged single-table query with cache translate set up programmatically
	 */
	public Page<StaffInfoVO> findStaff(Page<StaffInfoVO> pageModel, StaffInfoVO staffInfoVO) {
		// sql can be written inline in code; for complex sql prefer the xml form
		// in single-table entity queries the sql fields can be java property names
		String sql = "#[staffName like :staffName]#[and createTime>=:beginDate]#[and createTime<=:endDate]";
		return findPageEntity(pageModel, StaffInfoVO.class, EntityQuery.create().where(sql).values(staffInfoVO)
				// dictionary caches must set cacheType;
				// single-table entity queries need keyColumn to form the "select keyColumn as column" pattern
				.translates(new Translate("dictKeyName").setColumn("sexTypeName").setCacheType("SEX_TYPE")
						.setKeyColumn("sexType"))
				.translates(new Translate("organIdName").setColumn("organName").setKeyColumn("organId")));
	}

	/**
	 * @TODO paged query by sqlId with cache translate set up programmatically
	 */
	public Page<StaffInfoVO> findStaff1(Page<StaffInfoVO> pageModel, StaffInfoVO staffInfoVO) {
		// QueryExecutor is used when passing a raw sql or a sqlId
		return super.findPageByQuery(pageModel, new QueryExecutor("qstart_findStaff", staffInfoVO)
				.translates(new Translate("organIdName").setColumn("organName").setIndex(2))).getPageResult();
	}
}
