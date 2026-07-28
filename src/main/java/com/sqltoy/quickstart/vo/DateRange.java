/**
 * 
 */
package com.sqltoy.quickstart.vo;

import java.time.LocalDate;

/**
 * @project sqltoy-quickstart
 * @description 请在此说明类的功能
 * @author zhong
 * @version v1.0, Date:2020-12-3
 * @modify 2020-12-3,修改说明
 */

public class DateRange {

	private String staffName;
	private LocalDate beginDate;

	private LocalDate endDate;

	/**
	 * @return the beginDate
	 */
	public LocalDate getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public DateRange setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
		return this;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public DateRange setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * @return the staffName
	 */
	public String getStaffName() {
		return staffName;
	}

	/**
	 * @param staffName the staffName to set
	 */
	public DateRange setStaffName(String staffName) {
		this.staffName = staffName;
		return this;
	}

}
