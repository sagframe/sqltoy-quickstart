/**
 *@Generated by sagacity-quickvo 5.0
 */
package com.sqltoy.quickstart.vo;

import java.io.Serializable;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDate;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="StaffInfoVO",description="员工信息表")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_staff_info",comment="员工信息表",pk_constraint="PRIMARY")
public class StaffInfoVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8872936239782687517L;
/*---begin-auto-generate-don't-update-this-area--*/	

	@Schema(name="staffId",description="员工ID",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="STAFF_ID",comment="员工ID",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String staffId;

	@Schema(name="staffCode",description="工号",nullable=false)
	@Column(name="STAFF_CODE",comment="工号",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String staffCode;

	@Schema(name="staffName",description="姓名",nullable=false)
	@Column(name="STAFF_NAME",comment="姓名",length=30L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String staffName;

	@Schema(name="organId",description="部门",nullable=false)
	@Column(name="ORGAN_ID",comment="部门",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String organId;

	@Schema(name="sexType",description="性别",nullable=false)
	@Column(name="SEX_TYPE",comment="性别",length=1L,type=java.sql.Types.CHAR,nativeType="CHAR",nullable=false)
	private String sexType;

	@Schema(name="birthday",description="出生日期",nullable=true)
	@Column(name="BIRTHDAY",comment="出生日期",length=10L,type=java.sql.Types.DATE,nativeType="DATE",nullable=true)
	private LocalDate birthday;

	@Schema(name="entryDate",description="入职日期",nullable=false)
	@Column(name="ENTRY_DATE",comment="入职日期",length=10L,type=java.sql.Types.DATE,nativeType="DATE",nullable=false)
	private LocalDate entryDate;

	@Schema(name="termDate",description="离职日期",nullable=true)
	@Column(name="TERM_DATE",comment="离职日期",length=10L,type=java.sql.Types.DATE,nativeType="DATE",nullable=true)
	private LocalDate termDate;

	@Schema(name="photo",description="照片",nullable=true)
	@Column(name="PHOTO",comment="照片",length=2147483647L,type=java.sql.Types.BLOB,nativeType="LONGBLOB",nullable=true)
	private byte[] photo;

	@Schema(name="country",description="国家",nullable=true)
	@Column(name="COUNTRY",comment="国家",length=10L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String country;

	@Schema(name="censusRegister",description="籍贯",nullable=true)
	@Column(name="CENSUS_REGISTER",comment="籍贯",length=150L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String censusRegister;

	@Schema(name="address",description="家庭地址",nullable=true)
	@Column(name="ADDRESS",comment="家庭地址",length=250L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String address;

	@Schema(name="email",description="邮箱",nullable=true)
	@Column(name="EMAIL",comment="邮箱",length=100L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String email;

	@Schema(name="telNo",description="移动电话",nullable=true)
	@Column(name="TEL_NO",comment="移动电话",length=500L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String telNo;

	@Schema(name="post",description="岗位",nullable=true)
	@Column(name="POST",comment="岗位",length=20L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String post;

	@Schema(name="postGrade",description="职位级别",nullable=true)
	@Column(name="POST_GRADE",comment="职位级别",length=20L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String postGrade;

	@Schema(name="createBy",description="创建人",nullable=false)
	@Column(name="CREATE_BY",comment="创建人",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String createBy;

	@Schema(name="createTime",description="创建时间",nullable=false)
	@Column(name="CREATE_TIME",comment="创建时间",length=19L,type=java.sql.Types.DATE,nativeType="DATETIME",nullable=false)
	private LocalDateTime createTime;

	@Schema(name="updateBy",description="最后修改人",nullable=false)
	@Column(name="UPDATE_BY",comment="最后修改人",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String updateBy;

	@Schema(name="updateTime",description="最后修改时间",nullable=false)
	@Column(name="UPDATE_TIME",comment="最后修改时间",length=19L,type=java.sql.Types.DATE,nativeType="DATETIME",nullable=false)
	private LocalDateTime updateTime;

	@Schema(name="status",description="状态",nullable=false)
	@Column(name="STATUS",comment="状态",length=1L,defaultValue="1",type=java.sql.Types.INTEGER,nativeType="DECIMAL",nullable=false)
	private Integer status;
	/** default constructor */
	public StaffInfoVO() {
	}
	
	/** pk constructor */
	public StaffInfoVO(String staffId)
	{
		this.staffId=staffId;
	}
/*---end-auto-generate-don't-update-this-area--*/
	
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

	private DateRange dateRange;
}
