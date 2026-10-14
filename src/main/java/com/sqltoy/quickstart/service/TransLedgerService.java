package com.sqltoy.quickstart.service;

import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description transaction ledger service (updateFetch concurrency demos)
 * @author zhong
 * @version v1.0, Date:2021-01-13
 */
public interface TransLedgerService {
	public TransLedgerVO updateTrans(TransLedgerVO transVO);

	public TransLedgerVO updateSaveTrans(TransLedgerVO transVO);
}
