package com.sqltoy.quickstart.service;

/**
 * @project sqltoy-showcase
 * @description database initialization (legacy MySQL path)
 * @author chenrenfei <a href="mailto:zhongxuchen@gmail.com">contact the author</a>
 * @version id:InitDataService.java, Revision:v1.0, Date:2019-08-06
 */
public interface InitDBService {
	/**
	 * @TODO execute an init sql script against the database
	 */
	public void initDatabase(String dataSqlFile);

	public Long initOrderData();
}
