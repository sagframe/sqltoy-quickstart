/**
 *@Generated by sagacity-quickvo 5.0
 */
package com.sqltoy.quickstart.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Secure;
import org.sagacity.sqltoy.config.annotation.SecureConfig;
import org.sagacity.sqltoy.model.SecureType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="SecureCaseVO",description="安全加解密演示")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_secure_case",comment="安全加解密演示",pk_constraint="PRIMARY")
//这里telNoMask是对应telNo的脱敏13928766787 结果:139****6787，便于检索(无需则剔除)
@SecureConfig(secures = { @Secure(field = "telNo"), @Secure(field = "homeAddress"),
		@Secure(field = "telNoMask", secureType = SecureType.TEL, sourceField = "telNo"),
		@Secure(field = "homeAddressMask", secureType = SecureType.ADDRESS, sourceField = "homeAddress"), })
public class SecureCaseVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5349728950824234491L;
/*---begin-auto-generate-don't-update-this-area--*/	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="staffId",description="工号",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="STAFF_ID",comment="工号",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	private String staffId;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="staffName",description="姓名",nullable=false)
	@Column(name="STAFF_NAME",comment="姓名",length=30L,type=java.sql.Types.VARCHAR,nullable=false)
	private String staffName;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="telNo",description="移动电话",nullable=false)
	@Column(name="TEL_NO",comment="移动电话",length=500L,type=java.sql.Types.VARCHAR,nullable=false)
	private String telNo;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="telNoMask",description="电话检索",nullable=false)
	@Column(name="TEL_NO_MASK",comment="电话检索",length=30L,type=java.sql.Types.VARCHAR,nullable=false)
	private String telNoMask;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="homeAddress",description="家庭地址",nullable=false)
	@Column(name="HOME_ADDRESS",comment="家庭地址",length=500L,type=java.sql.Types.VARCHAR,nullable=false)
	private String homeAddress;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="homeAddressMask",description="家庭地址检索",nullable=false)
	@Column(name="HOME_ADDRESS_MASK",comment="家庭地址检索",length=100L,type=java.sql.Types.VARCHAR,nullable=false)
	private String homeAddressMask;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="createBy",description="创建人",nullable=false)
	@Column(name="CREATE_BY",comment="创建人",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	private String createBy;
	
	/**
	 * jdbcType:DATETIME
	 */
	@Schema(name="createTime",description="创建时间",nullable=false)
	@Column(name="CREATE_TIME",comment="创建时间",length=19L,type=java.sql.Types.DATE,nullable=false)
	private LocalDateTime createTime;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="updateBy",description="最后修改人",nullable=false)
	@Column(name="UPDATE_BY",comment="最后修改人",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	private String updateBy;
	
	/**
	 * jdbcType:DATETIME
	 */
	@Schema(name="updateTime",description="最后修改时间",nullable=false)
	@Column(name="UPDATE_TIME",comment="最后修改时间",length=19L,type=java.sql.Types.DATE,nullable=false)
	private LocalDateTime updateTime;
	
	/** default constructor */
	public SecureCaseVO() {
	}
	
	/** pk constructor */
	public SecureCaseVO(String staffId)
	{
		this.staffId=staffId;
	}
/*---end-auto-generate-don't-update-this-area--*/
}
