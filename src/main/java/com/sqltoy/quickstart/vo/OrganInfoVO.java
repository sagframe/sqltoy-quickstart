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
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="OrganInfoVO",description="机构信息表")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_organ_info",comment="机构信息表",pk_constraint="PRIMARY")
public class OrganInfoVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8373756153547203019L;
/*---begin-auto-generate-don't-update-this-area--*/	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="organId",description="机构ID",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="ORGAN_ID",comment="机构ID",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	private String organId;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="organName",description="机构名称",nullable=false)
	@Column(name="ORGAN_NAME",comment="机构名称",length=100L,type=java.sql.Types.VARCHAR,nullable=false)
	private String organName;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="organCode",description="机构代码",nullable=false)
	@Column(name="ORGAN_CODE",comment="机构代码",length=20L,type=java.sql.Types.VARCHAR,nullable=false)
	private String organCode;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="costNo",description="成本中心代码",nullable=true)
	@Column(name="COST_NO",comment="成本中心代码",length=20L,type=java.sql.Types.VARCHAR,nullable=true)
	private String costNo;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="organPid",description="父机构ID",nullable=false)
	@Column(name="ORGAN_PID",comment="父机构ID",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	private String organPid;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="nodeRoute",description="节点路径",nullable=true)
	@Column(name="NODE_ROUTE",comment="节点路径",length=200L,type=java.sql.Types.VARCHAR,nullable=true)
	private String nodeRoute;
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="nodeLevel",description="节点等级",nullable=true)
	@Column(name="NODE_LEVEL",comment="节点等级",length=1L,type=java.sql.Types.INTEGER,nullable=true)
	private Integer nodeLevel;
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="isLeaf",description="是否叶子节点",nullable=true)
	@Column(name="IS_LEAF",comment="是否叶子节点",length=1L,type=java.sql.Types.INTEGER,nullable=true)
	private Integer isLeaf;
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="showIndex",description="显示顺序",nullable=false)
	@Column(name="SHOW_INDEX",comment="显示顺序",length=8L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	private Integer showIndex;
	
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
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="status",description="状态",nullable=false)
	@Column(name="STATUS",comment="状态",length=1L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	private Integer status;
	
	/** default constructor */
	public OrganInfoVO() {
	}
	
	/** pk constructor */
	public OrganInfoVO(String organId)
	{
		this.organId=organId;
	}
/*---end-auto-generate-don't-update-this-area--*/
}
