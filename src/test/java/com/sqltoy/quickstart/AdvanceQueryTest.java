package com.sqltoy.quickstart;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.dao.LightDao;
import org.sagacity.sqltoy.model.CacheArg;
import org.sagacity.sqltoy.model.MapKit;
import org.sagacity.sqltoy.model.Page;
import org.sagacity.sqltoy.model.ParamsFilter;
import org.sagacity.sqltoy.model.QueryExecutor;
import org.sagacity.sqltoy.model.QueryResult;
import org.sagacity.sqltoy.model.Summary;
import org.sagacity.sqltoy.model.SummaryGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSON;
import com.sqltoy.quickstart.service.InitDBService;
import com.sqltoy.quickstart.vo.DeviceOrderVO;
import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description 演示部分查询特性
 * @author zhongxuchen
 * @version v1.0, Date:2020-7-20
 * @modify 2020-7-20,修改说明
 */
@SpringBootTest
public class AdvanceQueryTest {
	@Autowired
	LightDao lightDao;

	@Autowired
	InitDBService initDBService;

	// 第一步，订单数据初始化
	@Test
	public void mockOrderData() {
		Long saveCnt = initDBService.initOrderData();
		System.err.println("创建模拟订单记录:" + saveCnt + " 条!");
	}

	/**
	 * 普通sql查询,本查询演示了缓存翻译、缓存条件匹配过滤(缓存在项目启动时加载配置，首次调用时加载数据,第二次调用时就会体现出缓存效率优势)
	 */
	@Test
	public void findBySql() {
		// 授权的机构
		String[] authedOrgans = { "100004", "100007" };
		List<DeviceOrderVO> result = lightDao.find("qstart_order_search",
				MapKit.keys("orderId", "authedOrganIds", "staffName", "beginDate", "endDate").values(null, authedOrgans,
						"陈", LocalDate.parse("2018-09-01"), null),
				DeviceOrderVO.class);
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	/**
	 * 分页查询 sqltoy 的分页特点: 1、具有快速分页能力，即先分页后关联，实现查询效率的提升
	 * 2、具有分页优化能力，即缓存总记录数，将分页2次查询变成1.3~1.5次 3、具有智能优化count查询能力: -->剔除order by提升性能;
	 * -->解析sql判断是否可以select count(1)替代原语句from前部分,避免直接select count(1) from
	 * (原sql),从而提升效率 (如：select decode(A,1,xxx),case when end from table 等计算变成select
	 * count(1) from table 则避免了不必要的计算)
	 */
	@Test
	public void findPage() {
		Page pageModel = new Page();
		StaffInfoVO staffVO = new StaffInfoVO();
		// 作为查询条件传参数
		staffVO.setStaffName("陈");
		// 使用了分页优化器
		// 第一次调用:执行count 和 取记录两次查询
		Page<StaffInfoVO> result = lightDao.findPage(pageModel, "qstart_fastPage", staffVO, StaffInfoVO.class);
		result.getRows().forEach((staff) -> {
			System.err.println(JSON.toJSONString(staff));
		});
		// 第二次调用:条件一致，不执行count查询
		result = lightDao.findPage(pageModel, "qstart_fastPage", staffVO, StaffInfoVO.class);
		System.err.println(JSON.toJSONString(result));
	}

	@Test
	public void findAllPage() {
		Page pageModel = new Page();
		pageModel.setPageNo(-1);
		StaffInfoVO staffVO = new StaffInfoVO();
		// 作为查询条件传参数
		staffVO.setStaffName("陈");
		// 使用了分页优化器
		// 第一次调用:执行count 和 取记录两次查询
		Page<StaffInfoVO> result = lightDao.findPage(pageModel, "qstart_fastPage", staffVO, StaffInfoVO.class);
		result.getRows().forEach((staff) -> {
			System.err.println(JSON.toJSONString(staff));
		});

	}

	@Test
	public void findPageByMap() {
		Page pageModel = new Page();
		String sql = "select t.*\r\n" + "			           from sqltoy_staff_info t\r\n"
				+ "			           where t.STATUS=1 ";
		Page<StaffInfoVO> result = lightDao.findPage(pageModel, sql, Maps.newHashMap("pageNo", 1),
				StaffInfoVO.class);
		result.getRows().forEach((staff) -> {
			System.err.println(JSON.toJSONString(staff));
		});

	}

	/**
	 * 取前多少条记录 topSize:如果是大于1的数字,则取其整数部分;如果小于1则表示按比例提取
	 */
	@Test
	public void findTop() {
		// topSize:
		// 授权的机构
		String[] authedOrgans = { "100005", "100007" };
		double topSize = 20;
		List<DeviceOrderVO> result = lightDao.findTop("qstart_order_search",
				MapKit.keys("orderId", "authedOrganIds", "staffName", "beginDate", "endDate").values(null, authedOrgans,
						"陈", "2018-09-01", null),
				DeviceOrderVO.class, topSize);
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	@Test
	public void findTopByQuery() {
		// topSize:
		// 授权的机构
		// String[] authedOrgans = { "100004", "100007" };
		// String[] paramNames = { "orderId", "authedOrganIds", "staffName",
		// "beginDate", "endDate" };
		// Object[] paramValues = { null, null, "陈", "2018-09-01", null };
		// 采用统一数据权限传参，可不用显式的传递authedOrgans
		String[] paramNames = { "orderId", "staffName", "beginDate", "endDate" };
		Object[] paramValues = { null, "陈", "2018-09-01", null };
		double topSize = 20;
		QueryResult result = lightDao.findTopByQuery(new QueryExecutor("qstart_order_search").names(paramNames)
				.values(paramValues).resultType(DeviceOrderVO.class), topSize);
		System.err.println(result.getExecuteTime());
		result.getRows().forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}

	/**
	 * 查询随机记录 randomSize:如果是大于1的数字,则取其整数部分;如果小于1则表示按比例提取
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void findByRandom() throws InterruptedException {
		for (int i = 2; i > 0; i--) {
			// 授权的机构
			String[] authedOrgans = { "100004", "100007" };
			double randomSize = 20;
			List<DeviceOrderVO> result = lightDao.findRandom("qstart_order_search",
					MapKit.keys("orderId", "authedOrganIds", "staffName", "beginDate", "endDate").values(null,
							authedOrgans, "陈", "2018-09-01", null),
					DeviceOrderVO.class, randomSize);
			System.err.println("======第[" + i + "]次取随机记录的结果输出====================");
			result.forEach((vo) -> {
				System.err.println(JSON.toJSONString(vo));
			});
			Thread.sleep(5000);
		}
	}

	@Test
	public void testColsRelativeCalculate() throws InterruptedException {
		List result = lightDao.find("qstart_cols_relative_case", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	@Test
	public void testRowsRelativeCalculate() throws InterruptedException {
		List result = lightDao.find("qstart_rows_relative_case", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	@Test
	public void testPivotList() throws InterruptedException {
		List result = lightDao.find("qstart_pivot_case", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	@Test
	public void testGroupSummary() throws InterruptedException {
		List result = lightDao.find("qstart_group_summary_case", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}
	//

	@Test
	public void testLinkCase() throws InterruptedException {
		List result = lightDao.find("qstart_link_case", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	@Test
	public void testLinkCaseSimple() throws InterruptedException {
		List result = lightDao.find("qstart_link_case_simple", null);
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	/**
	 * @TODO 测试sql中使用@loop组织数组型的条件语句拼接，应对特殊场景
	 * @throws InterruptedException
	 */
	@Test
	public void testLoopSql() throws InterruptedException {
		String[] paramNames = { "fields", "orderId", "staffIds", "startDates", "endDates" };
		Object[][] paramValues = null;
		paramValues = new Object[][] { { "BUYER", "SALER", "TRANS_DATE", "DELIVERY_TERM", "STAFF_ID" }, null,
				{ "S0010", "S0009" }, { "2020-09-01", "2020-09-10", "2020-02-20" },
				{ "2020-09-08", "2020-09-18", "2020-09-28" } };

		List result = lightDao.find("qstart_loop_sql", MapKit.keys(paramNames).values(paramValues));
		for (int i = 0; i < result.size(); i++) {
			System.err.println(JSON.toJSONString(result.get(i)));
		}
	}

	@Test
	public void testFindAll() {
		// sqlToyLazyDao.query().sql("").resultType(User.class).find();
		List<StaffInfoVO> staffs = lightDao.findEntity(StaffInfoVO.class, null);
		for (int i = 0; i < staffs.size(); i++) {
			System.err.println(JSON.toJSONString(staffs.get(i)));
		}
	}

	@Test
	public void testFindQuery() {

		// 授权的机构
		String[] authedOrgans = { "100005", "100007" };
		List result = lightDao.findByQuery(new QueryExecutor("qstart_order_search")
				.filters(new ParamsFilter("staffName")
						.cacheArg(new CacheArg("staffIdName").aliasName("staffIds").filterIndex(2).filterValues("1")))
				.resultType(DeviceOrderVO.class)
				.values(MapKit.keys("orderId", "authedOrganIds", "staffName", "beginDate", "endDate").values(null,
						authedOrgans, "陈", LocalDate.parse("2018-09-01"), null)))
				.getRows();
//		result.forEach((vo) -> {
//			System.err.println(JSON.toJSONString(vo));
//		});
	}

	@Test
	public void testFindQuerySummary() {
//		<value>
//		<![CDATA[
//		select t.fruit_name,t.order_month,t.sale_count,t.sale_price,t.total_amt 
//		from sqltoy_fruit_order t
//		order by t.fruit_name ,t.order_month
//		]]>
//	</value>
//	<summary sum-columns="sale_count,sale_price,total_amt" 
//		reverse="true" sum-site="top" skip-single-row="true">
//		<!-- 层级顺序保持从高到低 -->
//		<global sum-label="总计"  label-column="fruit_name" />
//		<group group-column="fruit_name" sum-label="小计" 
//			label-column="fruit_name" />
//	</summary>
		String sql = "select t.fruit_name,t.order_month,t.sale_count,t.sale_price,t.total_amt "
				+ "	from sqltoy_fruit_order t order by t.fruit_name ,t.order_month";
		List result = lightDao.findByQuery(new QueryExecutor(sql)
				.summary(new Summary().globalReverse(true).sumColumns("sale_count", "sale_price", "total_amt")
						.skipSingleRow(true).summaryGroups(new SummaryGroup().labelColumn("fruit_name").sumTitle("总计"),
								new SummaryGroup("fruit_name").labelColumn("fruit_name").sumTitle("小计"))))
				.getRows();
		result.forEach((vo) -> {
			System.err.println(JSON.toJSONString(vo));
		});
	}
}
