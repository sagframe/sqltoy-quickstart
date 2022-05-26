# sqltoy-orm 快速上手项目
* quickstart只演示了部分功能,核心是让大家快速上手，详细功能参见文档
* 理论上来sqltoy可以解决您项目上全部数据库交互，我们的erp、数据平台、电商平台已经验证了这一点

# 学习步骤
## 1. 配置pom引入sqltoy的依赖

```xml
<dependency>
	<groupId>com.sagframe</groupId>
	<artifactId>sagacity-sqltoy-starter</artifactId>
	<version>5.1.42</version>
</dependency>

<!-- 
注意注意: 配置连接池依赖

1、druid模式
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid-spring-boot-starter</artifactId>
   <version>1.2.9</version>
</dependency>

2、springboot自带的hikari连接池需要额外增加依赖
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
	<version>2.6.8</version>
</dependency>
-->
```

## 2. 配置正确pom build避免sql文件无法编译到classes下面
* 核心配置:src/main/java 下面的<include>**/*.xml</include>

```xml
<resources>
	<resource>
		<directory>src/main/java</directory>
		<excludes>
			<exclude>**/*.java</exclude>
		</excludes>
		<includes>
			<include>**/*.xml</include>
		</includes>
	</resource>
	<resource>
		<directory>src/main/resources</directory>
		<includes>
			<include>**/*.xml</include>
			<include>**/*.properties</include>
			<include>**/*.yml</include>
			<include>**/*.sql</include>
			<include>**/*.jpg</include>
            <include>**/*.key</include>
		</includes>
	</resource>
</resources>
<testResources>
	<testResource>
		<directory>src/test/java</directory>
		<excludes>
			<exclude>**/*.java</exclude>
		</excludes>
		<includes>
			<include>**/*.xml</include>
		</includes>
	</testResource>
	<testResource>
		<directory>src/test/resources</directory>
		<includes>
			<include>**/*.xml</include>
			<include>**/*.properties</include>
			<include>**/*.yml</include>
			<include>**/*.sql</include>
                        <include>**/*.key</include>
		</includes>
	</testResource>
</testResources>
```

## 3. 配置application.yml，注意阅读配置上的备注
*  注意要点:sqlResourcesDir 是路径名,多个路径用逗号分隔,不要填错
* spring.sqltoy千万不要写成sqltoy漏掉spring开头

```
#完整路径:spring.sqltoy
spring:
   sqltoy:
        # 多个路径用逗号分隔(这里要注意是路径,sqltoy会自动向下寻找以sql.xml结尾的文件,不要写成classpath:com/**/*.sql.xml)
        sqlResourcesDir: classpath:com/sqltoy/quickstart
        # 默认值为classpath:sqltoy-translate.xml，一致则可以不用设置
        translateConfig: classpath:sqltoy-translate.xml
		# 默认开启跨数据库函数自动适配(如oracle的nvl,当数据库切到mysql时会自动替换成ifnull)
        #functionConverts: default
        # 默认为false，debug模式将打印执行sql,并自动检测sql文件更新并重新加载
        debug: true
        # 提供统一字段:createBy createTime updateBy updateTime 等字段补漏性(为空时)赋值(可选配置)
        unifyFieldsHandler: com.sqltoy.plugins.SqlToyUnifyFieldsHandler
        # sql执行超过多长时间则进行日志输出,用于监控哪些慢sql(可选配置:默认30秒)
        printSqlTimeoutMillis: 300000
        # 数据库保留字兼容处理(原则上不要使用数据库保留字,多个用逗号分隔)
        #reservedWords: maxvalue,minvalue
```

* 最简单配置(注意:spring.sqltoy开头)

```
#完整路径:spring.sqltoy
spring:
   sqltoy:
        # 多个路径用逗号分隔(注意这里填路径、路径!会自动相信寻找)
        sqlResourcesDir: classpath:com/sqltoy/quickstart
```
* properties 模式

```
# sqltoy config
spring.sqltoy.sqlResourcesDir=classpath:com/sqltoy/quickstart
# 默认配置就是classpath:sqltoy-translate.xml,一致情况下无需配置
spring.sqltoy.translateConfig=classpath:sqltoy-translate.xml
# 默认开启函数自动替换功能 ,4.15.7设置为close会关闭，之前版本可以填trim只开通单个函数模式
#spring.sqltoy.functionConverts=default
# 是否开启debug模式,在开发阶段建议为true,会打印sql
spring.sqltoy.debug=true
#项目中用到的数据库保留字定义,这里是举例，正常情况下不用定义
#spring.sqltoy.reservedWords=status,sex_type
# 设置获取数据源的策略实现类,只在多数据源场景下需要设置,DefaultDataSourceSelector是默认实现
#dataSourceSelector: org.sagacity.sqltoy.plugins.datasource.impl.DefaultDataSourceSelector
#spring.sqltoy.defaultDataSource=dataSource
spring.sqltoy.unifyFieldsHandler=com.sqltoy.plugins.SqlToyUnifyFieldsHandler
#spring.sqltoy.printSqlTimeoutMillis=200000
```

## 4. 编写springboot 主程序,注意@ComponentScan配置
* 参见:src/main/java 下面的SqlToyApplication

```java
package com.sqltoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * 
 * @project sqltoy-quickstart
 * @description quickstart 主程序入口
 * @author zhongxuchen 
 * @version v1.0, Date:2020年7月17日
 * @modify 2020年7月17日,修改说明
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.sqltoy.config", "com.sqltoy.quickstart" })
@EnableTransactionManagement
public class SqlToyApplication {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SqlToyApplication.class, args);
	}
}

```

## 5. 初始化数据库
* 参见src/test/java 下面的InitDataBaseTest,生成数据库表结构和初始化数据

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SqlToyApplication.class)
public class InitDataBaseTest {

	@Autowired
	private InitDBService initDBService;

	@Test
	public void testInitDB() {
		String dbSqlFile = "classpath:mock/quickstart_init.sql";
		System.err.println("开始执行数据库初始化!");
		initDBService.initDatabase(dbSqlFile);
	}
}
```

## 6. 利用quickvo生产VO(或POJO)
* 在出问题时关注dataSource中的schema、catalog配置,其他问题请参见quickvo.xml中的注释
* 将数据库驱动类放于tools/quickvo/libs下面
* 配置tools/quickvo/db.properties 文件

```properties
#############  db config ####################
jdbc.driver_class=com.mysql.cj.jdbc.Driver
# url characterEncoding=utf-8 param is need
jdbc.url=jdbc:mysql://192.168.56.109:3306/quickstart?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false
# mysql schema=dbname,oracle schema=username
jdbc.schema=quickstart
jdbc.username=quickstart
jdbc.password=quickstart
```

* 配置tools/quickvo/quickvo.xml 中的任务,关键部分如下

```xml
<!-- db配置文件 -->
<property file="db.properties" />
<property name="project.version" value="1.0.0" />
<property name="project.name" value="sqltoy-quickstart" />
<property name="project.package" value="com.sqltoy" />
<property name="include.schema" value="false" />
<!--set method 是否支持返回对象自身(默认是false),即: public VO setName(String name){this.name=name;return this;} -->
<property name="field.support.linked.set" value="true" />
<!-- 是否在抽象类中生成SelectFieldImpl内部类,默认值为false	-->
<property name="generate.selectFields.class" value="true" />
<!-- schema 对照关系:mysql 对应  db 名称; oracle 对应 用户名称;   -->
<!-- 注意:当在多schema或tablespace场景下，会出现一个表中出现重复字段，是因为schema和catalog 配置不正确，没有完成隔离   -->
<datasource name="quickstart" url="${db.url}" driver="${db.driver_class}" 
		schema="${db.schema}" catalog="${db.schema}" username="${db.username}" password="${db.password}" />
<tasks dist="../../src/main/java" encoding="UTF-8">
	<!-- include 是表名匹配的正则表达式  -->
	<task active="true" author="zhongxuchen" include="^SQLTOY_\w+" datasource="quickstart" swagger-model="false">
		<!-- substr 表示截取表名的前缀部分(一般表会按模块增加前缀),如不截取则substr="" name="#{subName}VO" subName是约定词,VO这两个字符可以随意改变  -->
		<entity package="${project.package}.quickvo.vo" substr="Sqltoy" name="#{subName}VO" />
	</task>
</tasks>
```

* 点击quickvo.bat 即可生产VO了,linux 或 mac 则执行quickvo.sh 
* windows环境下:

```
java -cp ./libs/* org.sagacity.quickvo.QuickVOStart quickvo.xml
```

* mac电脑:

```
java -cp ./libs/\* org.sagacity.quickvo.QuickVOStart ./quickvo.xml
```

# 源码导航
*  阅读的入口 src/test/java com.sqltoy.quickstart
* InitDataBaseTest 数据库初始化测试调用
* StaffInfoServiceTest 演示常规的CRUD
* TreeTableTest 演示树形表结构的构建和查询
* ShardingSearchTest 演示分表记录保存和查询(Sharding策略请参见src/main/java com.sqltoy.config.ShardingStrategyConfig )
* AdvanceQueryTest 查询相关的演示
* UniqueCaseTest 演示唯一性验证
* CascadeCaseTest 演示级联操作 
* LockCaseTest 演示锁记录修改操作
* StoreTest 演示存储过程调用
* JavaCodeSqlTest 演示在代码中写sql实现原本xml中的功能
* DTOConvertPOJOTest 演示在严格分层场景下DTO和POJO互转的范例
* LinkOptCaseTest 演示链式操作
* EntityOptsCaseTest 基于POJO类型的单表操作演示
* JsonTypeCaseTest 演示json等特殊类型的支持
* TransLedgerConcurrentTest 演示类似订单交易台账高并发事务处理
* ExecuteSql 演示executeSql和batchUpdate 执行sql更新或批量更新操作
* SecureEncryptDecryptTest 演示字段加解密功能

# 疑问解答
## 为什么要将*.sql.xml 放在java路径下?
* sqltoy推荐大家项目按照业务划分先分模块(消息中心、系统管理、订单管理等)后分层(web层、service)，sql文件放于模块中便于模块整体迁移和产品化
* 有利于开发过程，一般项目按模块分工，让开发者不需要不断的切换目录
* 当然这个是sqltoy推荐做法，开发者则可以根据自身实际情况而定,并非强制!

## 为什么缓存不用redis?
* 这里的缓存主要用于频繁的结果字段翻译，一旦你用了缓存翻译后你就会发现会在极为广泛的范围内使用，只有本地内存级缓存才能经得起频繁任意反复的调用，没有必要再来一个redis的IO消耗
* 你可以通过扩展实现ehcache+redis模式，来提升缓存的刷新实时性,但代价比较大造成了项目的复杂性,sqltoy提供的增量更新模式基本可以控制秒级更新 

## 为什么quickvo任务不是一个，而是按模块分多个任务
* sqltoy强调项目模块化，便于开发提炼出相对产品化的功能模块，逐步减少每次项目重复性工作，让不通过业务代码集中于一个模块下便于模块成熟后的抽离

## 如何分VO和POJO
* 请参见sqltoy-strict项目 https://github.com/sagframe/sqltoy-strict

## 如何分库分表
* 在本项目里面已经演示了分表
* 分库分表含事务型的范例: https://github.com/sagframe/sqltoy-showcase/tree/master/trunk/sqltoy-sharding

## 为什么dao不采用mybatis plus的接口模式？
* mybatis dao采用接口模式，是其向jpa方向靠拢的一种模式，而sqltoy本身就是jpa+查询模式，也就是说jpa向查询方向加强，正好相反！
* 什么接口?能够用接口来完成就是意味着可以用一个通用方法来代替！因此接口式dao的存在必要性就值得商榷！

```java
@Service("organInfoService")
public class OrganInfoServiceImpl implements OrganInfoService {
    //sqltoyLazyDao 就可以代替接口式的dao
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Transactional
	public void saveOrganInfo(OrganInfoVO organInfoVO) {
		// 先保存机构
		sqlToyLazyDao.saveOrUpdate(organInfoVO);
		// 设置树形表的节点路径等字段值,便于统一树形查询
		// id字段根据vo找表的主键会自动匹配上,其它的NODE_ROUTE\NODE_LEVEL\IS_LEAF 为标准命名无需额外设置
		//idField 如果是主键则无需设置
		sqlToyLazyDao.wrapTreeTableRoute(new TreeTableModel(organInfoVO).pidField("organPid"));
	}
}

```
* 考虑一些场景下dao仍然要做一些数据的封装处理(简化service层，将service尽量体现业务逻辑，减少一些dao的数据组装干扰)，sqltoy仍然可以写dao，但dao时实体类！

如下：实体类又有何不妥呢！清晰又可以针对一些特殊情况自己完善一些小处理，mybatis那种接口通过aop方式谈不上什么酷和高技术，不要被带到沟里去了，清晰、可维护、好拓展才是正道!

```java
@Repository("staffInfoDao")
public class StaffInfoDao extends SqlToyDaoSupport {
	/**
	 * @TODO 提供一个分页并动态设置缓存翻译的演示
	 * @param pageModel
	 * @param staffInfoVO
	 * @return
	 */
	public Page<StaffInfoVO> findStaff(Page<StaffInfoVO> pageModel, StaffInfoVO staffInfoVO) {
		// sql可以直接在代码中编写,复杂sql建议在xml中定义
		// 单表entity查询场景下sql字段可以写成java类的属性名称
		// 单表查询一般适用于接口内部查询
		String sql = "#[staffName like :staffName]#[and createTime>=:beginDate]#[and createTime<=:endDate]";
		return findPageEntity(pageModel,StaffInfoVO.class, EntityQuery.create().where(sql).values(staffInfoVO)
				// 字典缓存必须要设置cacheType
				// 单表对象查询需设置keyColumn构成select keyColumn as column模式
				.translates(new Translate("dictKeyName").setColumn("sexTypeName").setCacheType("SEX_TYPE")
						.setKeyColumn("sexType"))
				.translates(new Translate("organIdName").setColumn("organName").setKeyColumn("organId")));
	}
```

## 多数据源怎么弄?
* sqltoy可以配置默认数据源

```properties
spring.sqltoy.defaultDataSource=dataSourceName
```

* 如果是同类单据根据特定规则分多个库，请参见分库策略进行
* 通过多个lazyDao模式

```java
	 @Bean(name = "sqlToySkylineDao")
    public SqlToyLazyDao sqlToySkylineDao(@Qualifier("dataSourceSkyline") DataSource dataSource){
        SqlToyLazyDaoImpl dao = new SqlToyLazyDaoImpl();
        dao.setDataSource(dataSource);
        return dao;
    }

    @Bean(name = "sqlToyLazyDao")
    public SqlToyLazyDao sqlToyLazyDao(@Qualifier("dataSource") DataSource dataSource) {
        SqlToyLazyDaoImpl dao = new SqlToyLazyDaoImpl();
        dao.setDataSource(dataSource);
        return dao;
    }
```

* 通过lazyDao里面调用时指定dataSource,save、update、load等都有链式操作

```java
sqlToyLazyDao.save().dataSource(dataSource).saveMode(SaveMode.IGNORE).many(entities);
sqlToyLazyDao.query().sql("qstart_fastPage").dataSource(dataSource).entity(staffVO).findPage(pageModel);
```

## 我想通过包路径来实现不同数据库访问
* 请扩展实现org.sagacity.sqltoy.plugins.datasource.DataSourceSelector 接口
* 当前默认实现(你可以通过aop+ThreadLocal来修改实现)

```java
public class DefaultDataSourceSelector implements DataSourceSelector {

	@Override
	public DataSource getDataSource(ApplicationContext applicationContext, DataSource pointDataSouce,
			String sqlDataSourceName, DataSource injectDataSource, DataSource defaultDataSource) {
		// 第一优先:直接指定的数据源不为空
		if (pointDataSouce != null) {
			return pointDataSouce;
		}
		DataSource result = null;
		// 第二优先:sql中指定的数据源<sql id="xxx" datasource="xxxxDataSource">
		if (StringUtil.isNotBlank(sqlDataSourceName)) {
			result = getDataSourceBean(applicationContext, sqlDataSourceName);
		}
		// 第三优先:dao中autowired注入的数据源
		if (result == null) {
			result = injectDataSource;
		}
		// 第四优先:sqltoy 统一设置的默认数据源
		if (result == null) {
			result = defaultDataSource;
		}
		// 如果项目中只定义了唯一的数据源，则直接使用
		if (result == null) {
			Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
			// 只有一个dataSource,直接使用
			if (dataSources.size() == 1) {
				result = dataSources.values().iterator().next();
			}
		}
		return result;
	}
}
```
## sqltoy传参支持map吗？
* sqltoy传参可以三种

```java
    /**
	 * @todo 通过对象传参数,简化paramName[],paramValue[] 模式传参
	 * @param <T>
	 * @param sqlOrNamedSql 可以是具体sql也可以是对应xml中的sqlId
	 * @param entity        通过对象传参数,并按对象类型返回结果
	 * @return
	 */
	public <T extends Serializable> List<T> findBySql(final String sqlOrNamedSql, final T entity);
	
	/**
	 * @todo 通过给定sql、sql中的参数名、参数的数值以及返回结果的对象类型进行条件查询
	 */
	public <T> List<T> findBySql(final String sqlOrSqlId, final String[] paramsNamed, final Object[] paramsValue,
			final Class<T> voClass);
	/**
	 * @todo 通过map传参
	 */
    public <T> List<T> findBySql(final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> voClass);
```

## sqltoy必须返回VO吗?
* sqltoy返回结果可以是VO、map、二维List，List<Object[]>

```java
    /**
	 * @todo 通过给定sql、sql中的参数、参数的数值以及返回结果的对象类型进行条件查询
	 * @param sqlOrSqlId
	 * @param paramsNamed 如果sql是select * from table where xxx=?
	 *                    问号传参模式，paramNamed设置为null
	 * @param paramsValue 对应Named参数的值
	 * @param voClass     返回结果List中的对象类型(可以是VO、null:表示返回List<List>;HashMap.class,Array.class 返回List<Object[])
	 * @return
	 */
	public <T> List<T> findBySql(final String sqlOrSqlId, final String[] paramsNamed, final Object[] paramsValue,
			final Class<T> voClass);
```

## sqltoy 的sql必须写在xml中吗？
* sqltoy强调复杂sql放于xml中，但不限制您,如下面的代码第一个参数是sql或者sqlId，你可以直接传sql语句

```java
public <T> List<T> findBySql(final String sqlOrSqlId, final String[] paramsNamed, final Object[] paramsValue,
			final Class<T> voClass);
```

## 还有??
* 请阅读sqltoy下面的word文档说明
