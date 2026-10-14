-- H2 schema for the quickstart lessons (same table names as the MySQL script mock/quickstart_init.sql)
DROP TABLE IF EXISTS sqltoy_organ_info;
CREATE TABLE sqltoy_organ_info (
    ORGAN_ID   VARCHAR(8)  NOT NULL,
    ORGAN_NAME VARCHAR(60),
    SHOW_INDEX INT DEFAULT 0,
    UPDATE_TIME TIMESTAMP,
    CONSTRAINT pk_sqltoy_organ PRIMARY KEY (ORGAN_ID)
);

DROP TABLE IF EXISTS sqltoy_staff_info;
CREATE TABLE sqltoy_staff_info (
    STAFF_ID   VARCHAR(22) NOT NULL,
    STAFF_CODE VARCHAR(22),
    STAFF_NAME VARCHAR(30),
    ORGAN_ID   VARCHAR(8),
    SEX_TYPE   CHAR(1),
    ENTRY_DATE DATE,
    STATUS     INT DEFAULT 1,
    EMAIL      VARCHAR(60),
    CREATE_BY  VARCHAR(22),
    CREATE_TIME TIMESTAMP,
    UPDATE_BY  VARCHAR(22),
    UPDATE_TIME TIMESTAMP,
    CONSTRAINT pk_sqltoy_staff PRIMARY KEY (STAFF_ID)
);

DROP TABLE IF EXISTS sqltoy_dict_detail;
CREATE TABLE sqltoy_dict_detail (
    DICT_TYPE   VARCHAR(30) NOT NULL,
    DICT_KEY    VARCHAR(30) NOT NULL,
    DICT_NAME   VARCHAR(60),
    STATUS      INT DEFAULT 1,
    SHOW_INDEX  INT DEFAULT 0,
    UPDATE_TIME TIMESTAMP,
    CONSTRAINT pk_sqltoy_dict PRIMARY KEY (DICT_TYPE, DICT_KEY)
);

-- Lesson 1: this table's primary key is a Redis-generated business key like PO26101400000001
DROP TABLE IF EXISTS lesson_device_order;
CREATE TABLE lesson_device_order (
    ORDER_ID    VARCHAR(16) NOT NULL,
    BUYER       VARCHAR(20),
    ORGAN_ID    VARCHAR(8),
    DEVICE_TYPE VARCHAR(30),
    TRANS_DATE  DATE,
    TOTAL_AMT   DECIMAL(12, 2),
    STATUS      INT DEFAULT 1,
    CREATE_TIME TIMESTAMP,
    CONSTRAINT pk_lesson_order PRIMARY KEY (ORDER_ID)
);
