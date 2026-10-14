package com.sqltoy.config;

import java.util.HashMap;

import org.sagacity.sqltoy.plugins.sharding.ShardingStrategy;
import org.sagacity.sqltoy.plugins.sharding.impl.DefaultShardingStrategy;
import org.sagacity.sqltoy.plugins.sharding.impl.HashShardingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @project sqltoy-quickstart
 * @description table-sharding strategy demo (database sharding works the same way)
 * @author zhongxuchen
 * @version v1.0, Date:2020-07-22
 */
@Configuration
public class ShardingStrategyConfig {

	/**
	 * @TODO demo: split a real-time table and a history table
	 */
	@Bean(name = "realHisTable", initMethod = "initialize")
	public ShardingStrategy realHisTable() {
		DefaultShardingStrategy strategy = new DefaultShardingStrategy();
		// 14-day table plus a history table; "1,15,90" would mean multiple intervals
		strategy.setDays("14");
		HashMap<String, String> tableMap = new HashMap<String, String>();
		// queries are written against the 15d table;
		// the value can be a comma-separated list matching days, e.g. SQLTOY_TRANS_INFO_90,SQLTOY_TRANS_INFO_HIS
		tableMap.put("SQLTOY_TRANS_INFO_15D", "SQLTOY_TRANS_INFO_HIS");
		strategy.setTableNamesMap(tableMap);
		return strategy;
	}

	/**
	 * @TODO demo: database sharding by weight
	 */
	@Bean(name = "weightDataSource", initMethod = "initialize")
	public ShardingStrategy weightDataSource() {
		DefaultShardingStrategy strategy = new DefaultShardingStrategy();
		HashMap<String, Integer> dataSourceWeight = new HashMap<String, Integer>();
		// access weight per database
		dataSourceWeight.put("dataSource", 70);
		// dataSourceWeight.put("slave1", 30);
		strategy.setDataSourceWeight(dataSourceWeight);
		// seconds between database availability checks; <= 0 disables auto-checking
		strategy.setCheckSeconds(180);
		return strategy;
	}

	/**
	 * @TODO demo: database sharding by hash modulo
	 */
	@Bean(name = "hashDataSource", initMethod = "initialize")
	public ShardingStrategy hashDataSource() {
		HashShardingStrategy strategy = new HashShardingStrategy();
		HashMap<String, String> dataSourceMap = new HashMap<String, String>();
		// shard by hash modulo
		dataSourceMap.put("0", "dataSource");
		// the same database simulates several ones here
		// dataSourceMap.put("1", "slave1");
		// dataSourceMap.put("2", "slave2");
		strategy.setDataSourceMap(dataSourceMap);
		return strategy;
	}
}
