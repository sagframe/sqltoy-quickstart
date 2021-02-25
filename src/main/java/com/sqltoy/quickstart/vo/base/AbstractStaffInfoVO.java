/**
 *@Generated by sagacity-quickvo 4.18
 */
package com.sqltoy.quickstart.vo.base;

import java.io.Serializable;
import java.util.List;
import org.sagacity.sqltoy.config.annotation.Entity;
import java.util.ArrayList;
import org.sagacity.sqltoy.callback.SelectFields;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @version 1.0.0
 * Table: sqltoy_staff_info,Remark:员工信息表  
 */
@Schema(name="StaffInfoVO",description="员工信息表")
@Entity(tableName="sqltoy_staff_info",pk_constraint="PRIMARY")
public abstract class AbstractStaffInfoVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3492673222889622055L;
	
	/**
	 * jdbcType:VARCHAR
	 * 员工ID
	 */
	@Schema(name="STAFF_ID",description="员工ID",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="STAFF_ID",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String staffId;
	
	/**
	 * jdbcType:VARCHAR
	 * 工号
	 */
	@Schema(name="STAFF_CODE",description="工号",nullable=false)
	@Column(name="STAFF_CODE",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String staffCode;
	
	/**
	 * jdbcType:VARCHAR
	 * 姓名
	 */
	@Schema(name="STAFF_NAME",description="姓名",nullable=false)
	@Column(name="STAFF_NAME",length=30L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String staffName;
	
	/**
	 * jdbcType:VARCHAR
	 * 部门
	 */
	@Schema(name="ORGAN_ID",description="部门",nullable=false)
	@Column(name="ORGAN_ID",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String organId;
	
	/**
	 * jdbcType:CHAR
	 * 性别
	 */
	@Schema(name="SEX_TYPE",description="性别",nullable=false)
	@Column(name="SEX_TYPE",length=1L,type=java.sql.Types.CHAR,nullable=false)
	protected String sexType;
	
	/**
	 * jdbcType:DATE
	 * 出生日期
	 */
	@Schema(name="BIRTHDAY",description="出生日期",nullable=true)
	@Column(name="BIRTHDAY",length=10L,type=java.sql.Types.DATE,nullable=true)
	protected LocalDate birthday;
	
	/**
	 * jdbcType:DATE
	 * 入职日期
	 */
	@Schema(name="ENTRY_DATE",description="入职日期",nullable=false)
	@Column(name="ENTRY_DATE",length=10L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDate entryDate;
	
	/**
	 * jdbcType:DATE
	 * 离职日期
	 */
	@Schema(name="TERM_DATE",description="离职日期",nullable=true)
	@Column(name="TERM_DATE",length=10L,type=java.sql.Types.DATE,nullable=true)
	protected LocalDate termDate;
	
	/**
	 * jdbcType:LONGBLOB
	 * 照片
	 */
	@Schema(name="PHOTO",description="照片",nullable=true)
	@Column(name="PHOTO",length=2147483647L,type=java.sql.Types.BLOB,nullable=true)
	protected byte[] photo;
	
	/**
	 * jdbcType:VARCHAR
	 * 国家
	 */
	@Schema(name="COUNTRY",description="国家",nullable=true)
	@Column(name="COUNTRY",length=10L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String country;
	
	/**
	 * jdbcType:VARCHAR
	 * 籍贯
	 */
	@Schema(name="CENSUS_REGISTER",description="籍贯",nullable=true)
	@Column(name="CENSUS_REGISTER",length=150L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String censusRegister;
	
	/**
	 * jdbcType:VARCHAR
	 * 家庭地址
	 */
	@Schema(name="ADDRESS",description="家庭地址",nullable=true)
	@Column(name="ADDRESS",length=250L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String address;
	
	/**
	 * jdbcType:VARCHAR
	 * 邮箱
	 */
	@Schema(name="EMAIL",description="邮箱",nullable=true)
	@Column(name="EMAIL",length=100L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String email;
	
	/**
	 * jdbcType:VARCHAR
	 * 移动电话
	 */
	@Schema(name="TEL_NO",description="移动电话",nullable=true)
	@Column(name="TEL_NO",length=20L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String telNo;
	
	/**
	 * jdbcType:VARCHAR
	 * 岗位
	 */
	@Schema(name="POST",description="岗位",nullable=true)
	@Column(name="POST",length=20L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String post;
	
	/**
	 * jdbcType:VARCHAR
	 * 职位级别
	 */
	@Schema(name="POST_GRADE",description="职位级别",nullable=true)
	@Column(name="POST_GRADE",length=20L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String postGrade;
	
	/**
	 * jdbcType:VARCHAR
	 * 创建人
	 */
	@Schema(name="CREATE_BY",description="创建人",nullable=false)
	@Column(name="CREATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String createBy;
	
	/**
	 * jdbcType:DATETIME
	 * 创建时间
	 */
	@Schema(name="CREATE_TIME",description="创建时间",nullable=false)
	@Column(name="CREATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime createTime;
	
	/**
	 * jdbcType:VARCHAR
	 * 最后修改人
	 */
	@Schema(name="UPDATE_BY",description="最后修改人",nullable=false)
	@Column(name="UPDATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String updateBy;
	
	/**
	 * jdbcType:DATETIME
	 * 最后修改时间
	 */
	@Schema(name="UPDATE_TIME",description="最后修改时间",nullable=false)
	@Column(name="UPDATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime updateTime;
	
	/**
	 * jdbcType:DECIMAL
	 * 状态
	 */
	@Schema(name="STATUS",description="状态",nullable=false)
	@Column(name="STATUS",length=1L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	protected Integer status;
	

	/** default constructor */
	public AbstractStaffInfoVO() {
	}
	
	/** pk constructor */
	public AbstractStaffInfoVO(String staffId)
	{
		this.staffId=staffId;
	}

	
	/**
	 *@param staffId the staffId to set
	 */
	public AbstractStaffInfoVO setStaffId(String staffId) {
		this.staffId=staffId;
		return this;
	}
		
	/**
	 *@return the StaffId
	 */
	public String getStaffId() {
	    return this.staffId;
	}
	
	/**
	 *@param staffCode the staffCode to set
	 */
	public AbstractStaffInfoVO setStaffCode(String staffCode) {
		this.staffCode=staffCode;
		return this;
	}
		
	/**
	 *@return the StaffCode
	 */
	public String getStaffCode() {
	    return this.staffCode;
	}
	
	/**
	 *@param staffName the staffName to set
	 */
	public AbstractStaffInfoVO setStaffName(String staffName) {
		this.staffName=staffName;
		return this;
	}
		
	/**
	 *@return the StaffName
	 */
	public String getStaffName() {
	    return this.staffName;
	}
	
	/**
	 *@param organId the organId to set
	 */
	public AbstractStaffInfoVO setOrganId(String organId) {
		this.organId=organId;
		return this;
	}
		
	/**
	 *@return the OrganId
	 */
	public String getOrganId() {
	    return this.organId;
	}
	
	/**
	 *@param sexType the sexType to set
	 */
	public AbstractStaffInfoVO setSexType(String sexType) {
		this.sexType=sexType;
		return this;
	}
		
	/**
	 *@return the SexType
	 */
	public String getSexType() {
	    return this.sexType;
	}
	
	/**
	 *@param birthday the birthday to set
	 */
	public AbstractStaffInfoVO setBirthday(LocalDate birthday) {
		this.birthday=birthday;
		return this;
	}
		
	/**
	 *@return the Birthday
	 */
	public LocalDate getBirthday() {
	    return this.birthday;
	}
	
	/**
	 *@param entryDate the entryDate to set
	 */
	public AbstractStaffInfoVO setEntryDate(LocalDate entryDate) {
		this.entryDate=entryDate;
		return this;
	}
		
	/**
	 *@return the EntryDate
	 */
	public LocalDate getEntryDate() {
	    return this.entryDate;
	}
	
	/**
	 *@param termDate the termDate to set
	 */
	public AbstractStaffInfoVO setTermDate(LocalDate termDate) {
		this.termDate=termDate;
		return this;
	}
		
	/**
	 *@return the TermDate
	 */
	public LocalDate getTermDate() {
	    return this.termDate;
	}
	
	/**
	 *@param photo the photo to set
	 */
	public AbstractStaffInfoVO setPhoto(byte[] photo) {
		this.photo=photo;
		return this;
	}
		
	/**
	 *@return the Photo
	 */
	public byte[] getPhoto() {
	    return this.photo;
	}
	
	/**
	 *@param country the country to set
	 */
	public AbstractStaffInfoVO setCountry(String country) {
		this.country=country;
		return this;
	}
		
	/**
	 *@return the Country
	 */
	public String getCountry() {
	    return this.country;
	}
	
	/**
	 *@param censusRegister the censusRegister to set
	 */
	public AbstractStaffInfoVO setCensusRegister(String censusRegister) {
		this.censusRegister=censusRegister;
		return this;
	}
		
	/**
	 *@return the CensusRegister
	 */
	public String getCensusRegister() {
	    return this.censusRegister;
	}
	
	/**
	 *@param address the address to set
	 */
	public AbstractStaffInfoVO setAddress(String address) {
		this.address=address;
		return this;
	}
		
	/**
	 *@return the Address
	 */
	public String getAddress() {
	    return this.address;
	}
	
	/**
	 *@param email the email to set
	 */
	public AbstractStaffInfoVO setEmail(String email) {
		this.email=email;
		return this;
	}
		
	/**
	 *@return the Email
	 */
	public String getEmail() {
	    return this.email;
	}
	
	/**
	 *@param telNo the telNo to set
	 */
	public AbstractStaffInfoVO setTelNo(String telNo) {
		this.telNo=telNo;
		return this;
	}
		
	/**
	 *@return the TelNo
	 */
	public String getTelNo() {
	    return this.telNo;
	}
	
	/**
	 *@param post the post to set
	 */
	public AbstractStaffInfoVO setPost(String post) {
		this.post=post;
		return this;
	}
		
	/**
	 *@return the Post
	 */
	public String getPost() {
	    return this.post;
	}
	
	/**
	 *@param postGrade the postGrade to set
	 */
	public AbstractStaffInfoVO setPostGrade(String postGrade) {
		this.postGrade=postGrade;
		return this;
	}
		
	/**
	 *@return the PostGrade
	 */
	public String getPostGrade() {
	    return this.postGrade;
	}
	
	/**
	 *@param createBy the createBy to set
	 */
	public AbstractStaffInfoVO setCreateBy(String createBy) {
		this.createBy=createBy;
		return this;
	}
		
	/**
	 *@return the CreateBy
	 */
	public String getCreateBy() {
	    return this.createBy;
	}
	
	/**
	 *@param createTime the createTime to set
	 */
	public AbstractStaffInfoVO setCreateTime(LocalDateTime createTime) {
		this.createTime=createTime;
		return this;
	}
		
	/**
	 *@return the CreateTime
	 */
	public LocalDateTime getCreateTime() {
	    return this.createTime;
	}
	
	/**
	 *@param updateBy the updateBy to set
	 */
	public AbstractStaffInfoVO setUpdateBy(String updateBy) {
		this.updateBy=updateBy;
		return this;
	}
		
	/**
	 *@return the UpdateBy
	 */
	public String getUpdateBy() {
	    return this.updateBy;
	}
	
	/**
	 *@param updateTime the updateTime to set
	 */
	public AbstractStaffInfoVO setUpdateTime(LocalDateTime updateTime) {
		this.updateTime=updateTime;
		return this;
	}
		
	/**
	 *@return the UpdateTime
	 */
	public LocalDateTime getUpdateTime() {
	    return this.updateTime;
	}
	
	/**
	 *@param status the status to set
	 */
	public AbstractStaffInfoVO setStatus(Integer status) {
		this.status=status;
		return this;
	}
		
	/**
	 *@return the Status
	 */
	public Integer getStatus() {
	    return this.status;
	}



	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("staffId=").append(getStaffId()).append("\n");
		columnsBuffer.append("staffCode=").append(getStaffCode()).append("\n");
		columnsBuffer.append("staffName=").append(getStaffName()).append("\n");
		columnsBuffer.append("organId=").append(getOrganId()).append("\n");
		columnsBuffer.append("sexType=").append(getSexType()).append("\n");
		columnsBuffer.append("birthday=").append(getBirthday()).append("\n");
		columnsBuffer.append("entryDate=").append(getEntryDate()).append("\n");
		columnsBuffer.append("termDate=").append(getTermDate()).append("\n");
		columnsBuffer.append("photo=").append(getPhoto()).append("\n");
		columnsBuffer.append("country=").append(getCountry()).append("\n");
		columnsBuffer.append("censusRegister=").append(getCensusRegister()).append("\n");
		columnsBuffer.append("address=").append(getAddress()).append("\n");
		columnsBuffer.append("email=").append(getEmail()).append("\n");
		columnsBuffer.append("telNo=").append(getTelNo()).append("\n");
		columnsBuffer.append("post=").append(getPost()).append("\n");
		columnsBuffer.append("postGrade=").append(getPostGrade()).append("\n");
		columnsBuffer.append("createBy=").append(getCreateBy()).append("\n");
		columnsBuffer.append("createTime=").append(getCreateTime()).append("\n");
		columnsBuffer.append("updateBy=").append(getUpdateBy()).append("\n");
		columnsBuffer.append("updateTime=").append(getUpdateTime()).append("\n");
		columnsBuffer.append("status=").append(getStatus()).append("\n");
		return columnsBuffer.toString();
	}
	
	/**
	 * @TODO create entityQuery fields
	 */
	public static SelectFieldsImpl select() {
		return new SelectFieldsImpl();
	}
	
	public static class SelectFieldsImpl extends SelectFields {
		private List<String> fields = new ArrayList<String>();

		@Override
		public String[] getSelectFields() {
			String[] result = new String[fields.size()];
			fields.toArray(result);
			return result;
		}
		
	    public SelectFieldsImpl staffId() {
	    	if (!fields.contains("staffId")) {
				fields.add("staffId");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl staffCode() {
	    	if (!fields.contains("staffCode")) {
				fields.add("staffCode");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl staffName() {
	    	if (!fields.contains("staffName")) {
				fields.add("staffName");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl organId() {
	    	if (!fields.contains("organId")) {
				fields.add("organId");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl sexType() {
	    	if (!fields.contains("sexType")) {
				fields.add("sexType");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl birthday() {
	    	if (!fields.contains("birthday")) {
				fields.add("birthday");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl entryDate() {
	    	if (!fields.contains("entryDate")) {
				fields.add("entryDate");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl termDate() {
	    	if (!fields.contains("termDate")) {
				fields.add("termDate");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl photo() {
	    	if (!fields.contains("photo")) {
				fields.add("photo");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl country() {
	    	if (!fields.contains("country")) {
				fields.add("country");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl censusRegister() {
	    	if (!fields.contains("censusRegister")) {
				fields.add("censusRegister");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl address() {
	    	if (!fields.contains("address")) {
				fields.add("address");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl email() {
	    	if (!fields.contains("email")) {
				fields.add("email");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl telNo() {
	    	if (!fields.contains("telNo")) {
				fields.add("telNo");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl post() {
	    	if (!fields.contains("post")) {
				fields.add("post");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl postGrade() {
	    	if (!fields.contains("postGrade")) {
				fields.add("postGrade");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl createBy() {
	    	if (!fields.contains("createBy")) {
				fields.add("createBy");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl createTime() {
	    	if (!fields.contains("createTime")) {
				fields.add("createTime");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl updateBy() {
	    	if (!fields.contains("updateBy")) {
				fields.add("updateBy");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl updateTime() {
	    	if (!fields.contains("updateTime")) {
				fields.add("updateTime");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl status() {
	    	if (!fields.contains("status")) {
				fields.add("status");
			}
	    	return this;
	    }
    
	}
}
