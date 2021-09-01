/**
 * 
 */
package com.sqltoy.quickstart.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

import org.sagacity.sqltoy.callback.UpdateRowHandler;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.model.LockMode;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2021-1-13
 * @modify 2021-1-13,修改说明
 */
@Service("transLedgerService")
public class TransLedgerServiceImpl implements TransLedgerService {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Transactional
	public TransLedgerVO updateTrans(TransLedgerVO transVO) {
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		// lockMode.upgrade 对应for update 会等待前面的事务完成
		// lockMode.upgrade_skiplock 则不等待前面事务完成跳过相关事务
		// 如果lockMode不符合要求，可以在sql语句中直接写 for update xxx ,sqltoy则优先以你的优先
		String sql = "select * from SQLTOY_TRANS_LEDGER t where t.order_id=?";
		List<TransLedgerVO> result = (List<TransLedgerVO>) sqlToyLazyDao.updateFetch(new QueryExecutor(sql)
				.values(transVO.getOrderId()).resultType(TransLedgerVO.class).lock(LockMode.UPGRADE),
				new UpdateRowHandler() {
					public void updateRow(ResultSet rs, int index) throws Exception {
						int quantity = rs.getInt("QUANTITY");
						// 一般updateFetch会依托表中的现有值做一些逻辑处理,否则可以直接update
						rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + transVO.getQuantity());
						rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").add(transVO.getAmt()));
					}
				});
		if (result == null || result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public TransLedgerVO updateSaveTrans(TransLedgerVO transVO) {
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		return sqlToyLazyDao.updateSaveFetch(transVO, new UpdateRowHandler() {
			@Override
			public void updateRow(ResultSet rs, int index) throws Exception {
				// 一般updateFetch会依托表中的现有值做一些逻辑处理,否则可以直接update
				rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + transVO.getQuantity());
				rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").add(transVO.getAmt()));
			}
		}, "orderId");
	}

}
