/**
 * 
 */
package com.sqltoy.quickstart;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sqltoy.plugins.UpdateFetchThread;
import com.sqltoy.quickstart.service.TransLedgerService;
import com.sqltoy.quickstart.vo.TransLedgerVO;

/**
 * @project sqltoy-quickstart
 * @description 进行多线程并发数据更新测试
 * @author zhong
 * @version v1.0, Date:2021-1-13
 * @modify 2021-1-13,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class TransLedgerConcurrentTest {
	@Autowired
	TransLedgerService transLedgerService;

	@Autowired
	SqlToyCRUDService sqlToyCrudService;

	@Test
	public void testConcurrent() {
		TransLedgerVO transVO = new TransLedgerVO();
		transVO.setId("S00001");
		transVO.setOrderId("S00001");
		transVO.setCreateBy("system");
		transVO.setCreateTime(LocalDateTime.now());
		transVO.setUpdateBy("system");
		transVO.setQuantity(0);
		transVO.setAmt(BigDecimal.ZERO);
		transVO.setUpdateTime(LocalDateTime.now());
		// 先创建记录
		sqlToyCrudService.saveOrUpdate(transVO);

		// 开始并发演示
		// 200个并行度，每个并行下面循环50个(间隔3~15毫秒)，每执行一次+1,最终结果应该10000
		// transLedgerService 锁默认为UPGRADE，当变成UPGRADE_SKIPLOCK时结果是错误的
		for (int i = 0; i < 200; i++) {
			UpdateFetchThread thread = new UpdateFetchThread(i + 1, "S00001", transLedgerService);
			thread.start();
		}
		// 保持线程
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
