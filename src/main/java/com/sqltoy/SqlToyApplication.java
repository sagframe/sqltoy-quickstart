package com.sqltoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @project sqltoy-quickstart
 * @description quickstart application entry point
 * @author zhongxuchen
 * @version v1.0, Date:2020-07-17
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.sqltoy.config", "com.sqltoy.quickstart" })
@EnableTransactionManagement
public class SqlToyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlToyApplication.class, args);
	}
}
