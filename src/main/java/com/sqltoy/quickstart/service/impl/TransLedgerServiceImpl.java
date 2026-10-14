package com.sqltoy.quickstart.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

import org.sagacity.sqltoy.callback.UpdateRowHandler;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.LockMode;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description transaction ledger service (updateFetch / updateSaveFetch demos)
 * @author zhong
 * @version v1.0, Date:2021-01-13
 */
@Service("transLedgerService")
public class TransLedgerServiceImpl implements TransLedgerService {
	@Autowired
	LightDao lightDao;

	@Transactional
	public TransLedgerVO updateTrans(TransLedgerVO transVO) {
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		// LockMode.UPGRADE maps to "for update" and waits for the preceding transaction;
		// LockMode.UPGRADE_NOWAIT / SKIP LOCKED variants do not wait;
		// you can also write "for update xxx" directly in the sql — sqltoy gives yours priority
		String sql = "select * from SQLTOY_TRANS_LEDGER t where t.order_id=?";
		@SuppressWarnings("unchecked")
		List<TransLedgerVO> result = (List<TransLedgerVO>) lightDao.updateFetch(new QueryExecutor(sql)
				.values(transVO.getOrderId()).resultType(TransLedgerVO.class).lock(LockMode.UPGRADE),
				new UpdateRowHandler() {
					public void updateRow(ResultSet rs, int index) throws Exception {
						// updateFetch typically builds on existing column values; otherwise a plain update would do
						rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + transVO.getQuantity());
						rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").add(transVO.getAmt()));
					}
				});
		if (result == null || result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * updateSaveFetch targets strongly transactional, high-concurrency ledgers
	 * (inventory, funds) and completes everything in one database interaction:
	 * 1. load and lock the row by primary key
	 * 2. insert when the record does not exist
	 * 3. apply an incremental update when it exists
	 * 4. return the updated result
	 */
	@Override
	public TransLedgerVO updateSaveTrans(TransLedgerVO transVO) {
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		return lightDao.updateSaveFetch(transVO, new UpdateRowHandler() {
			@Override
			public void updateRow(ResultSet rs, int index) throws Exception {
				// updateFetch typically builds on existing column values; otherwise a plain update would do
				rs.updateInt("QUANTITY", rs.getInt("QUANTITY") + transVO.getQuantity());
				rs.updateBigDecimal("AMT", rs.getBigDecimal("AMT").add(transVO.getAmt()));
			}
		}, "orderId");
	}

}
