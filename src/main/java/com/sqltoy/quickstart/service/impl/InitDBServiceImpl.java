package com.sqltoy.quickstart.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.sagacity.sqltoy.callback.DataSourceCallbackHandler;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.DBProfile;
import org.sagacity.sqltoy.model.EntityQuery;
import org.sagacity.sqltoy.utils.DataSourceUtils;
import org.sagacity.sqltoy.utils.DateUtil;
import org.sagacity.sqltoy.utils.FileUtil;
import org.sagacity.sqltoy.utils.NumberUtil;
import org.sagacity.sqltoy.utils.SqlUtil;
import org.sagacity.sqltoy.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.service.InitDBService;
import com.sqltoy.quickstart.vo.DeviceOrderVO;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description legacy database initialization for the MySQL demo path
 * @author zhongxuchen
 * @version v1.0, Date:2020-07-17
 */
@Service("initDBService")
public class InitDBServiceImpl implements InitDBService {
	@Autowired
	private LightDao lightDao;

	@Transactional
	public void initDatabase(String dataSqlFile) {
		// load an init sql script (normally you would run it manually, see the cache-translate demos)
		final String sqlContent = FileUtil.readFileAsStr(dataSqlFile, "UTF-8");
		if (StringUtil.isBlank(sqlContent)) {
			return;
		}
		DataSourceUtils.processDataSource(lightDao.getSqlToyContext(), lightDao.getDataSource(),
				new DataSourceCallbackHandler() {
					@Override
					public void doConnection(Connection conn, DBProfile dbProfile) throws Exception {
						// executeBatchSql splits large sql scripts and executes them statement by statement
						SqlUtil.executeBatchSql(conn, sqlContent, 100, true);
					}
				});

	}

	@Transactional
	public Long initOrderData() {
		// step 1: clear the table (deleteByQuery refuses unconditional deletes, hence the 1=? trick)
		lightDao.deleteByQuery(DeviceOrderVO.class, EntityQuery.create().where("1=?").values(1));

		// simulate device orders
		List<DeviceOrderVO> orderInfos = new ArrayList<DeviceOrderVO>();
		int max = 1000;
		// load all staff (sqltoy forces a non-empty condition even for "load everything")
		List<StaffInfoVO> staffs = lightDao.findEntity(StaffInfoVO.class, EntityQuery.create().where("").values(""));
		StaffInfoVO staff;
		int[] days = { 10, 15, 20, 30, 60 };
		LocalDate nowTime = DateUtil.getDate();
		// read dictionary values straight from the translate cache — no database query
		List<Object[]> deviceTypes = new ArrayList<Object[]>(
				lightDao.getTranslateCache("dictKeyName", "DEVICE_TYPE").values());
		String[] psTypes = { "PO", "SO" };
		for (int i = 0; i < max; i++) {
			DeviceOrderVO orderVO = new DeviceOrderVO();
			staff = staffs.get(NumberUtil.getRandomNum(staffs.size() - 1));
			orderVO.setBuyer("C000" + i);
			orderVO.setSaler("S000" + i);
			orderVO.setStaffId(staff.getStaffId());
			orderVO.setOrganId(staff.getOrganId());
			orderVO.setTransDate(nowTime);
			orderVO.setDeliveryTerm(DateUtil.asLocalDate(DateUtil.addDay(nowTime, days[NumberUtil.getRandomNum(4)])));
			orderVO.setDeviceType(deviceTypes.get(NumberUtil.getRandomNum(deviceTypes.size() - 1))[0].toString());
			orderVO.setPsType(psTypes[NumberUtil.getRandomNum(1)]);
			orderVO.setTotalCnt(new BigDecimal(NumberUtil.getRandomNum(100, 400)));
			orderVO.setTotalAmt(orderVO.getTotalCnt().multiply(BigDecimal.valueOf(500)));
			orderVO.setStatus(1);
			orderVO.setCreateBy("S0001");
			orderVO.setUpdateBy("S0001");
			orderInfos.add(orderVO);
		}
		// transaction control belongs on the service method
		return lightDao.saveAll(orderInfos);
	}

}
