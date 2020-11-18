/**
 * 
 */
package com.sqltoy.quickstart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @project sqltoy-quickstart
 * @description 演示基于POJO类型的操作
 * @author zhong
 * @version v1.0, Date:2020-11-18
 * @modify 2020-11-18,修改说明
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class EntityOptsCaseTest {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Test
	public void testEntityQuery() {

	}

	@Test
	public void testEntityQueryPage() {

	}

	@Test
	public void testEntityQueryCount() {

	}
}
