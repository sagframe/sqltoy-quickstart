/**
 * 
 */
package com.sqltoy.quickstart.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import org.sagacity.sqltoy.callback.UpdateRowHandler;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.executor.QueryExecutor;
import org.sagacity.sqltoy.model.LockMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.service.TransShowcaseService;
import com.sqltoy.quickstart.vo.TransShowcaseVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2021-1-13
 * @modify 2021-1-13,修改说明
 */
@Service("transShowcaseService")
public class TransShowcaseServiceImpl implements TransShowcaseService {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Transactional
	public TransShowcaseVO updateTrans(TransShowcaseVO transVO) {
		transVO.setCreateBy("system");
		transVO.setCreateTime(LocalDateTime.now());
		transVO.setUpdateBy("system");
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		transVO.setUpdateTime(LocalDateTime.now());
		// sqlToyLazyDao.saveOrUpdate(transVO);
		String sql = "select * from sqltoy_trans_showcase t where t.order_id=?";
		List<TransShowcaseVO> result = (List<TransShowcaseVO>) sqlToyLazyDao.updateFetch(new QueryExecutor(sql)
				.values(transVO.getOrderId()).resultType(TransShowcaseVO.class).lock(LockMode.UPGRADE),
				new UpdateRowHandler() {
					public void updateRow(ResultSet rs, int index) throws Exception {
						int quantity = rs.getInt("QUANTITY");
						// 一般updateFetch会依托表中的现有值做一些逻辑处理,否则可以直接update
						rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + transVO.getQuantity());
						rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").add(transVO.getAmt()));
					}
				});
		if (result == null || result.isEmpty())
			return null;
		return result.get(0);
	}

}
