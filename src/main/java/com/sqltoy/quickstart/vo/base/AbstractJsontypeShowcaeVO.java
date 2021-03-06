/**
 *@Generated by sagacity-quickvo 4.18
 */
package com.sqltoy.quickstart.vo.base;

import java.io.Serializable;
import java.util.List;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.sqltoy.quickstart.vo.StaffInfoVO;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @version 1.0.0
 * Table: sqltoy_jsontype_showcae,Remark:JSON等特殊类型处理演示  
 */
@Schema(name="JsontypeShowcaeVO",description="JSON等特殊类型处理演示")
@Entity(tableName="sqltoy_jsontype_showcae",pk_constraint="PRIMARY")
public abstract class AbstractJsontypeShowcaeVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1203577228088229819L;
	
	/**
	 * jdbcType:VARCHAR
	 * 主键ID
	 */
	@Schema(name="ID",description="主键ID",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="ID",length=32L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String id;
	
	/**
	 * jdbcType:JSON
	 * 员工信息集合
	 */
	@Schema(name="STAFF_SET",description="员工信息集合",nullable=false)
	@Column(name="STAFF_SET",length=1073741824L,type=1021,nullable=false)
	protected List<StaffInfoVO> staffSet;
	
	/**
	 * jdbcType:DATETIME
	 * 创建日期
	 */
	@Schema(name="CREATE_TIME",description="创建日期",nullable=false)
	@Column(name="CREATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime createTime;
	
	/**
	 * jdbcType:DATETIME
	 * 修改时间
	 */
	@Schema(name="UPDATE_TIME",description="修改时间",nullable=false)
	@Column(name="UPDATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime updateTime;
	

	/** default constructor */
	public AbstractJsontypeShowcaeVO() {
	}
	
	/** pk constructor */
	public AbstractJsontypeShowcaeVO(String id)
	{
		this.id=id;
	}

	
	/**
	 *@param id the id to set
	 */
	public void setId(String id) {
		this.id=id;
	}
		
	/**
	 *@return the Id
	 */
	public String getId() {
	    return this.id;
	}
	
	/**
	 *@param staffSet the staffSet to set
	 */
	public void setStaffSet(List<StaffInfoVO> staffSet) {
		this.staffSet=staffSet;
	}
		
	/**
	 *@return the StaffSet
	 */
	public List<StaffInfoVO> getStaffSet() {
	    return this.staffSet;
	}
	
	/**
	 *@param createTime the createTime to set
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime=createTime;
	}
		
	/**
	 *@return the CreateTime
	 */
	public LocalDateTime getCreateTime() {
	    return this.createTime;
	}
	
	/**
	 *@param updateTime the updateTime to set
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime=updateTime;
	}
		
	/**
	 *@return the UpdateTime
	 */
	public LocalDateTime getUpdateTime() {
	    return this.updateTime;
	}



	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("id=").append(getId()).append("\n");
		columnsBuffer.append("staffSet=").append(getStaffSet()).append("\n");
		columnsBuffer.append("createTime=").append(getCreateTime()).append("\n");
		columnsBuffer.append("updateTime=").append(getUpdateTime()).append("\n");
		return columnsBuffer.toString();
	}
	
}
