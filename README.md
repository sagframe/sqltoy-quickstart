# sqltoy-orm 快速上手项目
* quickstart只演示了部分功能,核心是让大家快速上手，详细功能参见文档
* 理论上来sqltoy可以解决您项目上全部数据库交互，我们的erp、数据平台、电商平台已经验证了这一点

# 学习步骤
## 1. 配置pom引入sqltoy的依赖

```xml
<dependency>
	<groupId>com.sagframe</groupId>
	<artifactId>sagacity-sqltoy-starter</artifactId>
	<version>4.16.7</version>
</dependency>
```

## 2. 配置正确pom build避免sql文件无法编译到classes下面
* 核心配置:src/main/java 下面的<include>**/*.xml</include>

```xml
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
		</includes>
	</testResource>
</testResources>
```

## 3. 配置application.yml，最好sqltoy部分直接copy

* 1、很多spring.sqltoy 开头写成了sqltoy
* 2、很多classpath拼错了
* 常规配置，核心要点:sqlResourcesDir 是路径名,多个路径用逗号分隔,不要填错

```
#完整路径:spring.sqltoy
spring:
   sqltoy:
        # 多个路径用逗号分隔(这里要注意是路径,sqltoy会自动向下寻找以sql.xml结尾的文件,不要写成classpath:com/**/*.sql.xml)
        sqlResourcesDir: classpath:com/sqltoy/quickstart
        # 默认值为classpath:sqltoy-translate.xml，一致则可以不用设置
        translateConfig: classpath:sqltoy-translate.xml
		# 默认开启函数自动替换功能 ,4.15.7设置为close会关闭，之前版本可以填Trim只开通单个函数模式
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
#obtainDataSource: org.sagacity.sqltoy.plugins.datasource.impl.DefaultObtainDataSourc
#spring.sqltoy.defaultDataSource=dataSource
spring.sqltoy.unifyFieldsHandler=com.sqltoy.plugins.SqlToyUnifyFieldsHandler
#spring.sqltoy.printSqlTimeoutMillis=200000
```

## 4. 编写springboot 主程序,注意@ComponentScan配置
* 参见:src/main/java 下面的SqlToyApplication

```java
package com.sqltoy.quickstart;

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
<!--set method 是否支持返回对象自身(默认是true),即: public VO setName(String name){this.name=name;return this;} -->
<property name="field.support.linked.set" value="true" />
<!-- 是否在抽象类中生成SelectFieldImpl内部类,默认值为true,需要sqltoy4.15.4版本以上	-->
<property name="generate.selectFields.class" value="true" />
<!-- schema 对照关系:mysql 对应  db 名称; oracle 对应 用户名称;   -->
<datasource name="quickstart" url="${db.url}"	driver="${db.driver_class}" schema="${db.schema}"
<tasks dist="../../src/main/java" encoding="UTF-8">
	<!-- include 是正则表达式匹配 -->
	<task active="true" author="zhongxuchen" include="^SQLTOY_\w+" datasource="quickstart" swagger-model="false">
		<!-- substr 表示截取表名的前缀部分(一般表会按模块增加前缀),如不截取则substr="" name="#{subName}VO" subName是约定词,VO这两个字符可以随意改变  -->
		<vo package="${project.package}.quickvo.vo" substr="Sqltoy" name="#{subName}VO" />
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

## 还有??
* 请阅读sqltoy下面的word文档说明
