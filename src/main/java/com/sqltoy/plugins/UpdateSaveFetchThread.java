package com.sqltoy.plugins;

import org.sagacity.sqltoy.utils.NumberUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * Worker thread for the updateSaveFetch concurrency demo.
 *
 * @author zhong
 */
public class UpdateSaveFetchThread extends Thread {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private TransLedgerService transLedgerService;
	private int groupId;
	private int count;

	public UpdateSaveFetchThread(int groupId, int count, TransLedgerService transLedgerService) {
		this.groupId = groupId;
		this.count = count;
		this.transLedgerService = transLedgerService;
	}

	@Override
	public void run() {
		int failures = 0;
		for (int i = 0; i < count; i++) {
			try {
				Thread.sleep(NumberUtil.getRandomNum(3, 15));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String id = "S00000" + i;
			TransLedgerVO transVO = new TransLedgerVO(id);
			transVO.setOrderId(id);
			TransLedgerVO result = null;
			try {
				result = transLedgerService.updateSaveTrans(transVO);
				System.err.println(MAPPER.writeValueAsString(result));
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
