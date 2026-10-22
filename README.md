English | [简体中文](README.zh-CN.md)

# SqlToy Quick Start

[![CI](https://github.com/sagframe/sqltoy-quickstart/actions/workflows/ci.yml/badge.svg)](https://github.com/sagframe/sqltoy-quickstart/actions/workflows/ci.yml)
[![SqlToy](https://img.shields.io/badge/SqlToy-6.0.9-blue)](https://github.com/sagframe/sagacity-sqltoy)
![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)

**Five runnable lessons that take you from CRUD to cross-database SQL — with nothing to install.** Four [going-further lessons](#going-further) cover high-contention ledgers, management reports, a function-conversion deep dive and lambda conditions; an optional [Lesson 9](#lesson-9--the-same-sql-on-real-databases) re-runs the same SQL against real docker databases.

Each lesson is a JUnit test in the [`com.sqltoy.lesson`](src/test/java/com/sqltoy/lesson) package, phrased as an everyday problem rather than a feature name. The project runs on an in-memory H2 database (schema and seed data load automatically) plus an embedded Redis for Lesson 1, so it works right after cloning:

```bash
git clone https://github.com/sagframe/sqltoy-quickstart.git
cd sqltoy-quickstart
mvn test
```

| Lesson | The everyday problem | What you will see |
| --- | --- | --- |
| [1](#lesson-1--crud-without-dao-boilerplate-and-a-redis-business-key) | CRUD without DAO boilerplate; order numbers like `PO26101400000001` | `LightDao`, elastic update, `@BusinessId` backed by Redis |
| [2](#lesson-2--optional-search-filters-without-if-blocks) | A search form with optional filters | `#[...]` dynamic conditions — no `<if>`/`<foreach>` |
| [3](#lesson-3--pagination-that-optimizes-itself) | Pagination that stays fast on big tables | count rewriting, cached count, `@fast()` page-first join |
| [4](#lesson-4--skip-the-dictionary-join) | Codes need names — without JOINing the dictionary table | cache translate, resolved in memory |
| [5](#lesson-5--one-sql-multiple-databases) | The same SQL must run on several databases | `nvl(...)` auto-converted to `coalesce(...)`, `@virtualTable()` |

> Requirements: JDK 17+. Everything runs on an in-memory H2 database and an embedded Redis — nothing to install. To connect a real database, see [Using a real database](#using-a-real-database).

---

## Lesson 1 — CRUD without DAO boilerplate (and a Redis business key)

Inject `LightDao` (or `SqlToyCRUDService`) and operate on POJOs directly — no DAO classes, no SQL for routine operations. [`Lesson01CrudAndBusinessIdTest`](src/test/java/com/sqltoy/lesson/Lesson01CrudAndBusinessIdTest.java):

```java
@Autowired
LightDao lightDao;

// insert: the 22-digit id is generated when left null;
// createBy/createTime are auto-filled by the UnifyFieldsHandler
StaffMember staff = new StaffMember().setStaffCode("L001").setStaffName("Lesson One")
        .setOrganId("100005").setSexType("M").setEntryDate(LocalDate.now()).setStatus(1);
lightDao.save(staff);

// elastic update: null fields are skipped — one interaction, safe under concurrency
staff.setEmail("lesson1@example.com");
lightDao.update(staff);

// load / delete by primary key
StaffMember loaded = lightDao.load(new StaffMember().setStaffId(staff.getStaffId()));
lightDao.delete(new StaffMember().setStaffId(staff.getStaffId()));
```

Console output (real run):

```text
generated staffId      = 1791988840534000001481
auto-filled createBy   = system-auto
auto-filled createTime = 2026-10-14T22:40:40.537
```

**Business primary keys from Redis.** Sequential, rule-based keys (prefix + date + zero-padded sequence) come from a Redis `INCR` counter, so they are unique and strictly increasing across all application nodes. One annotation on the entity:

```java
@Id
@BusinessId(generator = "org.sagacity.sqltoy.plugins.id.impl.RedisIdGenerator",
            signature = "PO@df(yyMMdd)", length = 16)
@Column(name = "ORDER_ID", length = 16)
private String orderId;
```

```java
lightDao.save(order);   // orderId is assigned before the insert
System.out.println(order.getOrderId());
```

```text
orderId #1 = PO26101400000001
orderId #2 = PO26101400000002
orderId #3 = PO26101400000003
```

The test starts an **embedded Redis** on an automatically chosen free port — no local Redis install needed, and a busy port can never break the lesson. sqltoy only requires a bean named `redisTemplate` (auto-configured by `spring-boot-starter-data-redis`); the application itself starts fine without a reachable Redis until keys are actually generated.

🔗 Deep dive: [Object CRUD](https://sagframe.github.io/sqltoy-docs/#/en/crud/sqltoy_crud) · [Primary key strategies](https://sagframe.github.io/sqltoy-docs/#/en/primarykey/sqltoy_primarykey) · [Generating entities from a database: quickvo](https://sagframe.github.io/sqltoy-docs/#/en/prepare/quickvo)

## Lesson 2 — Optional search filters without `if` blocks

A search form where every filter is optional. In SqlToy each condition is wrapped in `#[...]` and simply joins the SQL when its parameter is set. The sql lives in [`lesson.sql.xml`](src/main/java/com/sqltoy/lesson/lesson.sql.xml):

```xml
<sql id="lesson02_staff_search">
    <value><![CDATA[
    select t.STAFF_ID,t.STAFF_CODE,t.STAFF_NAME,t.ORGAN_ID,t.SEX_TYPE,t.ENTRY_DATE
    from sqltoy_staff_info t
    where t.STATUS=1
    #[and t.ORGAN_ID in (:organIds)]
    #[and t.STAFF_NAME like :staffName]
    #[and t.ENTRY_DATE>=:beginDate]
    #[and t.ENTRY_DATE<=:endDate]
    ]]></value>
</sql>
```

```java
// only staffName set → one condition; add organIds → both conditions
// pass the bare value "Chen": sqltoy auto-wraps like parameters with % on both sides
List<StaffMember> rows = lightDao.find("lesson02_staff_search",
        MapKit.keys("staffName").values("Chen"), StaffMember.class);
```

The four parameter combinations from the test, and the SQL each one produced (debug log, values shown inlined for readability — execution itself always uses JDBC `?` placeholders):

```text
filter: staffName like '%Chen%'  -> 2 rows
  ... where t.STATUS=1  and t.STAFF_NAME like '%Chen%'

filter: organ_id in (100005)     -> 8 rows
  ... where t.STATUS=1  and t.ORGAN_ID in ('100005')

filter: 2 organs + date range    -> 6 rows
  ... where t.STATUS=1  and t.ORGAN_ID in ('100005','100007')
       and t.ENTRY_DATE>=CAST('2021-01-01' AS DATE)  and t.ENTRY_DATE<=CAST('2023-12-31' AS DATE)

no optional filter               -> 19 rows
  ... where t.STATUS=1
```

<details>
<summary><b>The same form in MyBatis, for comparison</b></summary>

```xml
<select id="lesson02_staff_search" resultMap="BaseResultMap">
 select * from sqltoy_staff_info t
 <where>
    <if test="organIds!=null">
        and t.ORGAN_ID in
        <foreach collection="organIds" item="organId" separator="," open="(" close=")">
            #{organId}
        </foreach>
    </if>
    <if test="staffName!=null">
        and t.STAFF_NAME like #{staffName}
    </if>
    <if test="beginDate!=null">
        and t.ENTRY_DATE>=#{beginDate}
    </if>
    <if test="endDate!=null">
        and t.ENTRY_DATE&lt;=#{endDate}
    </if>
 </where>
</select>
```

One `<if>` (plus `<foreach>` for collections) per filter, and the SQL gets chopped into fragments that no database client can execute as-is. The SqlToy version above is complete — and you can paste it straight into your DB console.

Small ergonomic details worth noticing: a `like` parameter without `%` is wrapped automatically on both sides (user input containing `%`/`_` is escaped), and `l-like`/`r-like` filters wrap one side only so the query can still use an index.
</details>

🔗 Deep dive: [Dynamic SQL specification](https://sagframe.github.io/sqltoy-docs/#/en/query/dynamic_sql)

## Lesson 3 — Pagination that optimizes itself

```xml
<sql id="lesson03_staff_page">
    <page-optimize parallel="true" alive-max="100" alive-seconds="120" />
    <value><![CDATA[
    select t1.*,t2.ORGAN_NAME
    from @fast(
        select t.STAFF_ID,t.STAFF_CODE,t.STAFF_NAME,t.ORGAN_ID,t.ENTRY_DATE
        from sqltoy_staff_info t
        where t.STATUS=1
        #[and t.STAFF_NAME like :staffName]
        order by t.ENTRY_DATE desc
    ) t1
    left join sqltoy_organ_info t2 on t1.ORGAN_ID=t2.ORGAN_ID
    ]]></value>
</sql>
```

```java
Page pageModel = new Page();
pageModel.setPageSize(5);
Page<StaffMember> page1 = lightDao.findPage(pageModel, "lesson03_staff_page", noFilter, StaffMember.class);
```

What each mechanism bought us (from the test run):

```text
page 1: recordCount=19, rows=5
  S0019  Samuel Ma    entry 2026-01-12  Sales Dept
  S0018  Ruth Han     entry 2025-07-21  R&D Center
  S0017  Quinn Feng   entry 2025-03-03  Head Office
  S0016  Peter Lin    entry 2024-10-08  Sales Dept
  S0015  Olivia Gao   entry 2024-06-17  R&D Center
```

- **count optimization**: the count is never `select count(1) from (your whole sql)` — the order by is stripped and the select columns are cut;
- **`@fast()`**: the executed SQL on page 2 shows the inner query paging *first*, then joining — the join never sees the full table:

```text
select t1.*,t2.ORGAN_NAME from
  ( select ... from sqltoy_staff_info t where t.STATUS=1
    order by t.ENTRY_DATE desc  limit 5 offset 5) t1
left join sqltoy_organ_info t2 on t1.ORGAN_ID=t2.ORGAN_ID
```

- **`page-optimize`**: page 2 reuses the cached count for identical conditions — the log prints `pagination query count value:19` without running a count query again.

🔗 Deep dive: [Pagination](https://sagframe.github.io/sqltoy-docs/#/en/query/pagination)

## Lesson 4 — Skip the dictionary JOIN

`organ_id` and `sex_type` are codes; the UI needs names. Traditionally you JOIN the dictionary table on every query. With cache translate the SQL contains **no JOIN at all** — names are resolved in memory after the query returns:

```xml
<sql id="lesson04_staff_with_names">
    <translate cache="organIdName" columns="organName" />
    <translate cache="dictKeyName" cache-type="SEX_TYPE" columns="sexTypeName" />
    <value><![CDATA[
    select t.STAFF_ID,t.STAFF_NAME,t.ORGAN_ID,t.ORGAN_ID organName,t.SEX_TYPE,t.SEX_TYPE sexTypeName
    from sqltoy_staff_info t
    where t.STATUS=1
    #[and t.STAFF_NAME like :staffName]
    ]]></value>
</sql>
```

The caches are ordinary SQL queries over the dictionary tables, declared once in [`sqltoy-translate.xml`](src/main/resources/sqltoy-translate.xml) and refreshed incrementally. Output of the test run:

```text
staffId  organId -> organName      sexType -> sexTypeName
  S0001  100005 -> R&D Center      F -> Female
  S0010  100005 -> R&D Center      M -> Male
```

Annotation form for a VO field: `@Translate(cacheName = "organIdName", keyField = "organId")`.

🔗 Deep dive: [Cache translate in action](https://sagframe.github.io/sqltoy-docs/#/en/quickstart/translates)

## Lesson 5 — One SQL, multiple databases

This lesson runs Oracle-style SQL **on H2**. With `functionConverts=default`, function adaptation happens when the sql is loaded:

```xml
<sql id="lesson05_nvl_style">
    <value><![CDATA[
    select nvl(sum(t.TOTAL_AMT),0) total_amt
    from lesson_device_order t
    where t.STATUS=1
    ]]></value>
</sql>
```

H2 has no `nvl` function — the debug log shows what actually ran:

```text
select coalesce(sum(t.TOTAL_AMT),0) total_amt from lesson_device_order t where t.STATUS=1
```

`@virtualTable()` handles the `from dual` family of differences: it expands to `from dual` on Oracle-style databases and is stripped on H2/MySQL/PostgreSQL, so one statement runs everywhere:

```text
HQ       Head Office   <- literal row, @virtualTable() stripped on H2
100001   Head Office
100005   R&D Center
100007   Sales Dept
```

Change nothing but the datasource URL and these statements keep working — this is the mechanism behind SqlToy's [cross-database support](https://sagframe.github.io/sqltoy-docs/#/en/dialect/sqltoy_function) across 25 dialects.

🔗 Deep dive: [Dialect adaptation & function conversion](https://sagframe.github.io/sqltoy-docs/#/en/dialect/sqltoy_function)

---

## Going further

Three more lessons for the problems that show up after the basics: high-contention ledgers, management reports, and the function-conversion machinery behind Lesson 5. Same format, same runnable tests.

### Lesson 6 — Race-proof ledgers in one round-trip

The classic lost-update bug: thread A reads `quantity=100`, thread B reads `100`, both write `99` — one deduction vanishes. `updateFetch` prevents it: **lock → compute against the current row → update → return**, one database interaction, no window for a race ([`Lesson06LedgerConcurrencyTest`](src/test/java/com/sqltoy/lesson/Lesson06LedgerConcurrencyTest.java)):

```java
// inside a transaction; LockMode.UPGRADE maps to "for update"
List<TransLedger> rows = lightDao.updateFetch(
        new QueryExecutor("select t.ORDER_ID,t.QUANTITY,t.AMT from lesson_trans_ledger t where t.ORDER_ID=?")
                .values("LEDGER-01").resultType(TransLedger.class).lock(LockMode.UPGRADE),
        new UpdateRowHandler() {
            public void updateRow(java.sql.ResultSet rs, int index) throws Exception {
                // deduct against the CURRENT server-side value, not a stale read
                rs.updateInt("QUANTITY", rs.getInt("QUANTITY") - 1);
            }
        });
```

The test fires **8 threads × 50 deductions** at a 1000-unit ledger and asserts the exact result (real run):

```text
threads=8 x 50 deductions, failures=0
remaining quantity = 600 (expected 600), remaining amount = 499600.00
```

`updateSaveFetch` is the sibling for insert-or-update ledgers: when the row does not exist it inserts (skipping the handler), on later calls it locks and updates in place — `0 → 5` in the test output.

🔗 Deep dive: [Object CRUD — updateFetch](https://sagframe.github.io/sqltoy-docs/#/en/crud/sqltoy_crud)

### Lesson 7 — Reports without ETL

Month-per-column pivots, subtotals and month-over-month percentages — declared in xml over the same sales query, zero Java code ([`Lesson07ReportTest`](src/test/java/com/sqltoy/lesson/Lesson07ReportTest.java)):

```xml
<sql id="lesson07_sales_mom">
    <value><![CDATA[
    select t.PRODUCT,t.ORDER_MONTH,t.SALE_COUNT,t.TOTAL_AMT
    from lesson_monthly_sales t
    order by t.PRODUCT,t.ORDER_MONTH
    ]]></value>
    <!-- months become column headings, two measures under each month -->
    <pivot start-column="SALE_COUNT" end-column="TOTAL_AMT"
        group-columns="PRODUCT" category-columns="ORDER_MONTH" />
    <!-- month-over-month between the pivoted columns -->
    <cols-chain-relative group-size="2" relative-indexs="1" start-column="1" format="#.00%" />
</sql>
```

Real output of the three transforms:

```text
pivot (months as columns):
  Laptop   120  96000.00   135  108000.00   150  120000.00
  Printer   80  24000.00    72   21600.00    90   27000.00
  Server    30  45000.00    42   63000.00    36   54000.00

summary (grand total + per-product subtotals):
  Total     -    755  558600.00
  Subtotal  -    405  324000.00
  Laptop    2026-01  120   96000.00

month-over-month (pivot + relative columns):
  Laptop   120  96000.00  -     135  108000.00  112.50%   150  120000.00  111.10%
  Server    30  45000.00  -      42   63000.00  140.00%    36   54000.00   85.70%
```

🔗 Deep dive: [Complex queries — pivot / summary / relative](https://sagframe.github.io/sqltoy-docs/#/en/query/sqltoy_complex_query)

### Lesson 8 — Function conversion deep dive

Lesson 5 showed `nvl → coalesce`. Lesson 8 widens the lens: statements deliberately written in a "foreign" dialect — MySQL's `find_in_set`, Oracle's `to_char`/`to_date`, standard `trim(from ...)` — all run on the zero-install H2 database, because `functionConverts=default` rewrites them at load time ([`Lesson08FunctionConvertTest`](src/test/java/com/sqltoy/lesson/Lesson08FunctionConvertTest.java)). What the debug log shows being executed:

```text
find_in_set('b','a,b,c')             -> a position expression returning 2 (MySQL contract kept)
substr('hello',0,2)                  -> substring('hello',1,2) = 'he'  (literal start 0 unified)
to_char('…','yyyy-mm-dd HH:mi:ss')   -> to_char(CAST(… AS TIMESTAMP),'yyyy-mm-dd HH24:mi:ss')
to_date('2026-10-20','yyyy-mm-dd')   -> parsedatetime('2026-10-20','yyyy-MM-dd')  (mm = month!)
trim(from '  padded  ')              -> trim(both ' ' from '  padded  ')
```

Conventions worth memorizing: in format models bare `HH` is 24-hour and lowercase `hh` is 12-hour, `mm` is month and `mi` is minute; `find_in_set` returns the integer element position in a select list and accepts both boolean (`>0`) and position (`=2`) comparisons in WHERE.

🔗 Deep dive: [Dialect adaptation & function conversion](https://sagframe.github.io/sqltoy-docs/#/en/dialect/sqltoy_function)

### Lesson 10 — Lambda conditions: Lesson 2's filters as code

Lesson 2 wrote optional filters as `#[...]` in XML; the lambda API expresses the same conditions with entity getter references (`StaffMember::getStatus`) — column names are checked against the entity at assembly time, and every value still binds through a JDBC `?` ([`Lesson10LambdaQueryTest`](src/test/java/com/sqltoy/lesson/Lesson10LambdaQueryTest.java)). Terminals do the work: `find / getOne / findPage / count / exists` for queries, `set / setExpr` for updates — and calling a join step (`leftJoin(...).on(...)`) switches the same builder to a LEFT JOIN without changing the entry point:

```java
List<StaffMember> rows = lightDao.lambdaQuery(StaffMember.class)
        .leftJoin(DeviceOrder.class)
        .on(StaffMember::getOrganId, DeviceOrder::getOrganId)
        .eq(StaffMember::getStatus, 1)
        .eq(DeviceOrder::getDeviceType, "SERVER")
        .find();
```

Guard rails included: `getOne()` throws instead of silently returning row 1 when several match, and an update with no where condition throws unless you state `.matchAll()`.

The wildcard demo — the same search-box value, two intents (`likeExact` arrived in 6.0.9; rows are `100%OFF` and `100XOFF`):

```java
.like(DeviceOrder::getBuyer, "100%")       // % is a wildcard pattern -> 2 rows
.likeExact(DeviceOrder::getBuyer, "100%")  // % is a literal          -> 1 row
```

Expression updates let the database do the math on the current value — no read-modify-write race:

```java
lightDao.lambdaUpdate(DeviceOrder.class)
        .setExpr(DeviceOrder::getTotalAmt, "totalAmt * ?", new BigDecimal("1.1"))
        .eq(DeviceOrder::getOrderId, "EXPR-01")
        .update();
```

🔗 Deep dive: [Lambda queries and updates](https://sagframe.github.io/sqltoy-docs/#/en/query/lambda_query)

---

## Using a real database

Everything runs on the in-memory H2 database by default, so the project works right after cloning. To connect a real database instead: add its JDBC driver to `pom.xml`, point `spring.datasource` in [`application.yml`](src/main/resources/application.yml) at it, and create the demo tables — [`mock/h2/schema-h2.sql`](src/main/resources/mock/h2/schema-h2.sql) holds the full DDL (translate it to your database's dialect, or let [quickvo](#generate-entities-from-the-database-quickvo) reverse-engineer your own schema). sqltoy adapts to 25 dialects automatically (that is what Lesson 5 demonstrates).

### Lesson 9 — The same SQL on real databases

Lesson 8 runs on H2; Lesson 9 proves the "change nothing but the datasource URL" promise against four real databases. [`docker-compose.yml`](docker-compose.yml) defines them (PostgreSQL, SQL Server, Oracle 23ai, MySQL — ports and credentials match the tests):

```bash
docker compose up -d    # first pull takes a while (the Oracle image is ~9 GB)
mvn test                # the Lesson09* tests now run against the real databases
```

Each [`Lesson09*`](src/test/java/com/sqltoy/lesson) test class boots its own Spring context against one container; the statements are literal-based, so no schema seeding is needed, and a class skips itself when its container is not running — plain `mvn test` stays green without docker. The debug log prints each dialect's converted SQL, e.g. `trim(from '  padded  ')` becomes `trim('  padded  ')` on Oracle/MySQL (both reject the standard form) and `rtrim(ltrim(…))` on SQL Server; `date_trunc('week',…)` becomes `TRUNC(…,'IW')` on Oracle and a DATEFIRST-independent `DATEADD/DATEDIFF` anchor on SQL Server; `string_agg(…) within group (…)` becomes the native in-paren `string_agg(x,',' order by x)` on PostgreSQL.

🔗 Deep dive: [Dialect adaptation & function conversion](https://sagframe.github.io/sqltoy-docs/#/en/dialect/sqltoy_function)

### Generate entities from the database (quickvo)

The lesson entities are deliberately hand-written so the first run needs no tooling. In a real project you usually start from an **existing** database — that is what [quickvo](https://sagframe.github.io/sqltoy-docs/#/en/prepare/quickvo) is for: it reverse-engineers POJOs directly from live tables, complete with `@Id`/`@Column` metadata (including column comments and lengths pulled from the database), Lombok and Swagger annotations.

Try it right now — no live database needed. quickvo boots its own in-memory H2 schema (the **very lesson tables** you just queried, from [`mock/h2/schema-h2.sql`](src/main/resources/mock/h2/schema-h2.sql)) through the JDBC url and regenerates the exhibit package [`com.sqltoy.lesson.gen`](src/main/java/com/sqltoy/lesson/gen):

```bash
mvn quickvo:quickvo
```

What the output looks like — the generated [`StaffInfoVO`](src/main/java/com/sqltoy/lesson/gen/StaffInfoVO.java):

```java
@Generated by quickvo-maven-plugin 2.0.3
@Data
@Accessors(chain = true)
@SqlToyEntity
@Entity(tableName="SQLTOY_STAFF_INFO",comment="Staff information",pk_constraint="PK_SQLTOY_STAFF")
public class StaffInfoVO implements Serializable {
    @Schema(description = "Staff ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
    @Column(name="STAFF_ID",comment="Staff ID",length=22L,type=JdbcTypes.VARCHAR,nativeType="CHARACTER VARYING",nullable=false)
    private String staffId;

    @Column(name="ENTRY_DATE",comment="Entry date",length=10L,type=JdbcTypes.DATE,nativeType="DATE",nullable=false)
    private LocalDate entryDate;
    // ... one annotated field per table column, standard audit columns included
}
```

In a real project you point the datasource in [`quickvo.xml`](src/main/resources/quickvo.xml) at your own database — typically a legacy one you are migrating — add its JDBC driver, and run the same command. Prefer the standalone tool without Maven? See the [`tools/`](tools) directory. [`GeneratedEntityTest`](src/test/java/com/sqltoy/lesson/GeneratedEntityTest.java) proves the generated code is fully functional: CRUD through a generated VO works out of the box, including the primary-key generator and audit-field auto-fill.

🔗 Deep dive: [quickvo](https://sagframe.github.io/sqltoy-docs/#/en/prepare/quickvo) · [quickvo-maven-plugin](https://sagframe.github.io/sqltoy-docs/#/en/prepare/quickvo_plugin)

## Project layout

```text
src/main/java/com/sqltoy/lesson/     Hand-written lesson entities, DTO and lesson.sql.xml (start here)
src/main/java/com/sqltoy/lesson/gen/ quickvo-generated exhibit: the same tables, generated code
src/main/resources/mock/h2/          H2 schema (with comments) + seed data
src/test/java/com/sqltoy/lesson/     Lesson01–Lesson10 (runnable in any IDE)
tools/                               Standalone quickvo setup (no Maven needed)
docker-compose.yml                   The four real databases behind Lesson 9 (optional)
```

## Learn more

- 📖 [SqlToy documentation (English)](https://sagframe.github.io/sqltoy-docs/#/en/)
- 🧭 [SqlToy ORM main repository](https://github.com/sagframe/sagacity-sqltoy)
- 🎛️ Full-feature demos: [sqltoy-showcase](https://github.com/sagframe/sqltoy-showcase) (sharding, multi-datasource, NoSQL…)

## Feedback

**Have a use case or suggestion?** We welcome feedback, bug reports, and contributions from the Java community — open an [issue](https://github.com/sagframe/sqltoy-quickstart/issues) or start a discussion in the [main repository](https://github.com/sagframe/sagacity-sqltoy).
