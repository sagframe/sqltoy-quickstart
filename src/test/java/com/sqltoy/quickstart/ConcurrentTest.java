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
import com.sqltoy.quickstart.service.TransShowcaseService;
import com.sqltoy.quickstart.vo.TransShowcaseVO;

/**
 * @project sqltoy-quickstart
 * @description 进行多线程并发数据更新测试
 * @author zhong
 * @version v1.0, Date:2021-1-13
 * @modify 2021-1-13,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class ConcurrentTest {
	@Autowired
	TransShowcaseService transShowcaseService;

	@Autowired
	SqlToyCRUDService sqlToyCrudService;

	@Test
	public void testConcurrent() {
		TransShowcaseVO transVO = new TransShowcaseVO();
		transVO.setOrderId("S00001");
		transVO.setCreateBy("system");
		transVO.setCreateTime(LocalDateTime.now());
		transVO.setUpdateBy("system");
		transVO.setQuantity(1);
		transVO.setAmt(BigDecimal.ONE);
		transVO.setUpdateTime(LocalDateTime.now());
		sqlToyCrudService.saveOrUpdate(transVO);
		for (int i = 0; i < 200; i++) {
			UpdateFetchThread thread = new UpdateFetchThread(i + 1, "S00001", transShowcaseService);
			thread.start();
		}
		// 保持线程
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
