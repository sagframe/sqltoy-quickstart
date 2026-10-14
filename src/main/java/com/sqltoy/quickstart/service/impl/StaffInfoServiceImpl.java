package com.sqltoy.quickstart.service.impl;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.sagacity.sqltoy.callback.UpdateRowHandler;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.EntityQuery;
import org.sagacity.sqltoy.model.EntityUpdate;
import org.sagacity.sqltoy.model.LockMode;
import org.sagacity.sqltoy.model.Page;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.dao.StaffInfoDao;
import com.sqltoy.quickstart.service.StaffInfoService;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description staff service demo
 * @author zhongxuchen
 * @version v1.0, Date:2020-07-20
 */
@Service("staffInfoService")
public class StaffInfoServiceImpl implements StaffInfoService {
	// for demo purposes only; normally no DAO is needed
	@Autowired
	StaffInfoDao staffInfoDao;

	@Autowired
	LightDao lightDao;

	public Page<StaffInfoVO> queryStaff(Page<StaffInfoVO> pageModel, StaffInfoVO staffInfoVO) {
		return staffInfoDao.findStaff(pageModel, staffInfoVO);
	}

	// updateFetch targets ledgers (inventory, funds): lock the row, apply logic, update, return the result
	/**
	 * @TODO demo of updateFetch usage (illustrative only, not a realistic scenario)
	 */
	@Transactional
	public List<StaffInfoVO> updateFetch() {
		StaffInfoVO staffInfo = new StaffInfoVO();
		staffInfo.setStaffId("S2029");
		staffInfo.setStaffCode("S2029");
		staffInfo.setStaffName("Staff 2029");
		staffInfo.setOrganId("100007");
		staffInfo.setSexType("M");
		staffInfo.setEmail("test8@139.com");
		staffInfo.setEntryDate(LocalDate.now());
		staffInfo.setStatus(1);

		String sql = "select t.STAFF_ID,t.STAFF_NAME,t.ADDRESS,t.ENTRY_DATE,t.ORGAN_ID,t.ORGAN_ID ORGAN_NAME,t.UPDATE_TIME"
				+ " from sqltoy_staff_info t where staff_id=?";
		return lightDao.updateFetch(new QueryExecutor(sql).values("S2029").resultType(StaffInfoVO.class),
				new UpdateRowHandler() {
					public void updateRow(ResultSet rs, int index) throws Exception {
						String staffName = rs.getString("STAFF_NAME");
						// updateFetch typically builds on existing column values; otherwise a plain update would do
						if (staffName.contains("Staff")) {
							rs.updateString("ADDRESS", rs.getString("ADDRESS") + " updated");
						}
						rs.updateObject("UPDATE_TIME", LocalDateTime.now());
					}
				});
	}

	@Transactional
	public void updateLockStaff(String id, String address) {
		StaffInfoVO staffInfo = lightDao.load(new StaffInfoVO(id), LockMode.UPGRADE);
		staffInfo.setAddress(address);
		lightDao.update(staffInfo);
	}

	@Transactional
	public List<StaffInfoVO> callStore() {
		return lightDao.executeStore("{call sp_showcase(?,?)}", new Object[] { 1, null }, null, StaffInfoVO.class)
				.getRows();
	}

	/**
	 * @TODO demo: condition-based update without writing sql
	 */
	@Transactional
	public Long updateByQuery() {
		return lightDao.updateByQuery(StaffInfoVO.class,
				EntityUpdate.create().set("createBy", "chenrenfei").where("staffName like ?").values("Chen%"));
	}

	/**
	 * @TODO demo: condition-based delete without writing sql
	 */
	@Transactional
	public Long deleteByQuery() {
		return lightDao.deleteByQuery(StaffInfoVO.class, EntityQuery.create().where("status=?").values(0));
	}
}
