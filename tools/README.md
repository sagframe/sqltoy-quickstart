# 独立版 quickvo(不依赖 Maven)

项目根 pom 已把 quickvo 配成 Maven 插件:在项目根目录执行 `mvn quickvo:quickvo` 即可(配置见
[`../src/main/resources/quickvo.xml`](../src/main/resources/quickvo.xml))。本目录是
[quickvo 文档](https://sagframe.github.io/sqltoy-docs/#/prepare/quickvo) 所述的**独立工具**等价配置——
适合在不碰项目构建环境的场合生成实体(例如 DBA 的机器、或迁移前对着遗留库逆向)。

English: this directory is the standalone-tool counterpart of the Maven plugin config, as
referenced by the quickvo documentation. Use it to generate POJOs/VOs without Maven.

## 一次性准备

1. 从 [releases](https://gitee.com/sagacity/sagacity-quickvo/releases) 下载 `sagacity-quickvo.jar`,放入 `libs/`;
2. 把目标数据库的 JDBC 驱动 jar 也放入 `libs/`(跑本目录自带的 H2 演示配置需要 h2 jar,
   如 Maven Central 的 `com.h2database:h2:2.4.240`);
3. 按需调整 [`quickvo.xml`](quickvo.xml):自带配置连接与课程同款的内存 H2 表结构
   (`../src/main/resources/mock/h2/schema-h2.sql`),重新生成演示包 `com.sqltoy.lesson.gen`;
   对着自己的库生成时替换 `datasource` 的 url/driver/schema(文件内有 MySQL 示例注释)。

## 运行

```bash
# Windows
quickvo.bat

# Linux / Mac
chmod +x quickvo.sh && ./quickvo.sh
```

生成结果写入 `../src/main/java`(由 quickvo.xml 的 `tasks dist` 决定)。
