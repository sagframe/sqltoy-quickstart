/**
 * 
 */
package com.sqltoy.quickstart.service;

import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2021-1-13
 * @modify 2021-1-13,修改说明
 */
public interface TransLedgerService {
	public TransLedgerVO updateTrans(TransLedgerVO transVO);
	
	public TransLedgerVO updateSaveTrans(TransLedgerVO transVO);
}
