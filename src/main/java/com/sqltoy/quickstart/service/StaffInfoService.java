package com.sqltoy.quickstart.service;

import java.util.List;

import org.sagacity.sqltoy.model.Page;

import com.sqltoy.quickstart.vo.StaffInfoVO;

/**
 * @project sqltoy-quickstart
 * @description staff information service
 * @author zhong
 * @version v1.0, Date:2020-07-17
 */
public interface StaffInfoService {
	public Page<StaffInfoVO> queryStaff(Page<StaffInfoVO> pageModel, StaffInfoVO staffInfoVO);

	/**
	 * @TODO demo: locked query, update, and return of the updated result in one interaction
	 */
	public List<StaffInfoVO> updateFetch();

	/**
	 * @TODO demo: lock a record and update it
	 */
	public void updateLockStaff(String id, String address);

	public List<StaffInfoVO> callStore();

	/**
	 * @TODO demo: condition-based update without writing sql
	 */
	public Long updateByQuery();

	/**
	 * @TODO demo: condition-based delete without writing sql
	 */
	public Long deleteByQuery();
}
