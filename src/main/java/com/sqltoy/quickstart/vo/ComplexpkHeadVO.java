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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.sagacity.sqltoy.config.annotation.OneToMany;
import java.util.List;
import java.util.ArrayList;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="ComplexpkHeadVO",description="复合主键级联操作主表")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_complexpk_head",comment="复合主键级联操作主表",pk_constraint="PRIMARY")
public class ComplexpkHeadVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8333098323861603937L;
/*---begin-auto-generate-don't-update-this-area--*/	
	/**
	 * jdbcType:DATE
	 */
	@Schema(name="transDate",description="交易日期",nullable=false)
	@Id
	@Column(name="TRANS_DATE",comment="交易日期",length=10L,type=java.sql.Types.DATE,nullable=false)
	private LocalDate transDate;
	
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name="transCode",description="业务代码",nullable=false)
	@Id
	@Column(name="TRANS_CODE",comment="业务代码",length=30L,type=java.sql.Types.VARCHAR,nullable=false)
	private String transCode;
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="totalCnt",description="总数量",nullable=false)
	@Column(name="TOTAL_CNT",comment="总数量",length=12L,type=java.sql.Types.DECIMAL,nullable=false)
	private BigDecimal totalCnt;
	
	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name="totalAmt",description="总金额",nullable=false)
	@Column(name="TOTAL_AMT",comment="总金额",length=12L,type=java.sql.Types.DECIMAL,nullable=false)
	private BigDecimal totalAmt;
	
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
	 * 主键关联子表信息
	 */
	@OneToMany(fields={"transDate","transCode"},mappedFields={"transDate","transId"},delete=true)
	private List<ComplexpkItemVO> complexpkItemVOs=new ArrayList<ComplexpkItemVO>();

	/** default constructor */
	public ComplexpkHeadVO() {
	}
	
	/** pk constructor */
	public ComplexpkHeadVO(LocalDate transDate,String transCode)
	{
		this.transDate=transDate;
		this.transCode=transCode;
	}
/*---end-auto-generate-don't-update-this-area--*/
}
