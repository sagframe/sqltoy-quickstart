[English](README.md) | 简体中文

# SqlToy 快速上手

**5 节可直接运行的课程：从 CRUD 到跨数据库 SQL，零安装。**

默认使用 H2 内存库（建表与初始化数据自动加载）+ 嵌入式 Redis（第 1 课），clone 后即可运行：

```bash
git clone https://github.com/sagframe/sqltoy-quickstart.git
cd sqltoy-quickstart
mvn test
```

| 课程 | 解决的日常问题 | 演示能力 |
| --- | --- | --- |
| [1](https://github.com/sagframe/sqltoy-quickstart#lesson-1--crud-without-dao-boilerplate-and-a-redis-business-key) | 不写 DAO 的 CRUD；`PO26101400000001` 式业务单号 | LightDao、弹性 update、`@BusinessId` + Redis |
| [2](https://github.com/sagframe/sqltoy-quickstart#lesson-2--optional-search-filters-without-if-blocks) | 搜索表单的可选条件 | `#[...]` 动态条件，无 `<if>/<foreach>` |
| [3](https://github.com/sagframe/sqltoy-quickstart#lesson-3--pagination-that-optimizes-itself) | 大表分页不降速 | count 智能改写、缓存 count、`@fast()` 先分页后关联 |
| [4](https://github.com/sagframe/sqltoy-quickstart#lesson-4--skip-the-dictionary-join) | 码值翻译名称、消灭字典 JOIN | 缓存翻译，内存完成 |
| [5](https://github.com/sagframe/sqltoy-quickstart#lesson-5--one-sql-multiple-databases) | 同一套 SQL 跑多种数据库 | `nvl→coalesce` 自动转换、`@virtualTable()` |

> **进阶两课（Going further）**：[第 6 课](https://github.com/sagframe/sqltoy-quickstart#lesson-6--race-proof-ledgers-in-one-round-trip)高并发台账——`updateFetch` 一次交互完成锁定→扣减→返回，测试 8 线程×50 次并发扣减零丢失；[第 7 课](https://github.com/sagframe/sqltoy-quickstart#lesson-7--reports-without-etl)报表零 ETL——`<pivot>/<summary>/<cols-chain-relative>` 声明式完成行转列、小计与环比。

> 环境要求：JDK 17+。每课的代码、真实运行输出与深入文档链接见[英文 README](README.md)。

## quickvo：从数据库生成实体

课程实体为教学手写；真实项目通常从**已有数据库**出发，用 [quickvo](https://sagframe.github.io/sqltoy-docs/#/en/prepare/quickvo) 逆向生成带 `@Id`/`@Column`（含库中注释与长度）、Lombok、Swagger 注解的 POJO。本仓库的演示包 `com.sqltoy.lesson.gen` 就是对课程同款表生成的结果：

```bash
mvn quickvo:quickvo
```

## 切换真实数据库

默认 H2 内存库开箱即用。连接真实库：在 `pom.xml` 加对应 JDBC 驱动，修改 `application.yml` 的 `spring.datasource`，并按 [`mock/h2/schema-h2.sql`](src/main/resources/mock/h2/schema-h2.sql) 的 DDL 在目标库建表（sqltoy 自动适配 25 种数据库方言，见第 5 课）。

## 更多资源

- 📖 [SqlToy 文档](https://sagframe.github.io/sqltoy-docs/#/) · [英文文档](https://sagframe.github.io/sqltoy-docs/#/en/)
- 🧭 [SqlToy ORM 主仓库](https://github.com/sagframe/sagacity-sqltoy)
- 🎛️ 全功能演示：[sqltoy-showcase](https://github.com/sagframe/sqltoy-showcase)（分库分表、多数据源、NoSQL…）

欢迎反馈与贡献：[提 issue](https://github.com/sagframe/sqltoy-quickstart/issues) 或在[主仓库](https://github.com/sagframe/sagacity-sqltoy)发起讨论。
