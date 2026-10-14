package com.sqltoy.plugins;

import org.sagacity.sqltoy.utils.NumberUtil;

import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description worker thread for the updateFetch concurrency demo
 * @author zhong
 * @version v1.0, Date:2021-01-13
 */
public class UpdateFetchThread extends Thread {
	private TransLedgerService transLedgerService;
	private String orderId;
	private int groupId;

	public UpdateFetchThread(int groupId, String orderId, TransLedgerService transLedgerService) {
		this.groupId = groupId;
		this.orderId = orderId;
		this.transLedgerService = transLedgerService;
	}

	@Override
	public void run() {
		int failures = 0;
		int count = 50;
		for (int i = 0; i < count; i++) {
			try {
				Thread.sleep(NumberUtil.getRandomNum(3, 15));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			TransLedgerVO transVO = new TransLedgerVO();
			transVO.setOrderId(orderId);
			TransLedgerVO result = null;
			try {
				result = transLedgerService.updateTrans(transVO);
				if (result == null) {
					failures++;
				}
			} catch (Exception e) {
				failures++;
			}
		}
		System.err.println("groupId=" + groupId + "; executions=" + count + "; succeeded=" + (count - failures));
	}

}
