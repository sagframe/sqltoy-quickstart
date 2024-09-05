/**
 *@Generated by sagacity-quickvo 4.13
 */
package com.sqltoy.quickstart.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.SqlToyEntity;

import com.sqltoy.quickstart.pojo.base.AbstractStaffInfo;

/**
 * @project sqltoy-showcase
 * @author zhongxuchen
 * @version 1.0.0
 * Table: sqltoy_staff_info,Remark:员工信息表 	
 */
@SqlToyEntity
public class StaffInfo extends AbstractStaffInfo {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8872936239782687517L;
	
	/** default constructor */
	public StaffInfo() {
		super();
	}
	/*---begin-constructor-area---don't-update-this-area--*/
	/** pk constructor */
	public StaffInfo(String staffId)
	{
		this.staffId=staffId;
	}

	/** minimal constructor */
	public StaffInfo(String staffId,String staffCode,String staffName,String organId,String sexType,LocalDate entryDate,String createBy,LocalDateTime createTime,String updateBy,LocalDateTime updateTime,Integer status)
	{
		this.staffId=staffId;
		this.staffCode=staffCode;
		this.staffName=staffName;
		this.organId=organId;
		this.sexType=sexType;
		this.entryDate=entryDate;
		this.createBy=createBy;
		this.createTime=createTime;
		this.updateBy=updateBy;
		this.updateTime=updateTime;
		this.status=status;
	}

	/** full constructor */
	public StaffInfo(String staffId,String staffCode,String staffName,String organId,String sexType,LocalDate birthday,LocalDate entryDate,LocalDate termDate,byte[] photo,String country,String censusRegister,String address,String email,String telNo,String post,String postGrade,String createBy,LocalDateTime createTime,String updateBy,LocalDateTime updateTime,Integer status)
	{
		this.staffId=staffId;
		this.staffCode=staffCode;
		this.staffName=staffName;
		this.organId=organId;
		this.sexType=sexType;
		this.birthday=birthday;
		this.entryDate=entryDate;
		this.termDate=termDate;
		this.photo=photo;
		this.country=country;
		this.censusRegister=censusRegister;
		this.address=address;
		this.email=email;
		this.telNo=telNo;
		this.post=post;
		this.postGrade=postGrade;
		this.createBy=createBy;
		this.createTime=createTime;
		this.updateBy=updateBy;
		this.updateTime=updateTime;
		this.status=status;
	}

	/*---end-constructor-area---don't-update-this-area--*/
	
	private LocalDate beginDate;
	
	private LocalDate endDate;
	
	/**
	 * 机构名称
	 */
	private String organName;
	
	/**
	 * 性别名称
	 */
	private String sexTypeName;
	
	/**
     *@todo vo columns to String
     */
    @Override
	public String toString() {
		return super.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public StaffInfo clone() {
		try {
			return (StaffInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getSexTypeName() {
		return sexTypeName;
	}
	public void setSexTypeName(String sexTypeName) {
		this.sexTypeName = sexTypeName;
	}
	public LocalDate getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
