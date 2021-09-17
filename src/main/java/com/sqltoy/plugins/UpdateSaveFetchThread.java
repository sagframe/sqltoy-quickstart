/**
 * 
 */
package com.sqltoy.plugins;

import org.sagacity.sqltoy.utils.NumberUtil;

import com.alibaba.fastjson.JSON;
import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @author zhong
 *
 */
public class UpdateSaveFetchThread extends Thread {

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
		int meter = 0;
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
				System.err.println(JSON.toJSONString(result));
				if (result == null) {
					meter++;
				}
			} catch (Exception e) {
				meter++;
			}
		}
		System.err.println("groupId=" + groupId + ";执行次数=" + count + ";完成量=" + (count - meter));
	}

}
