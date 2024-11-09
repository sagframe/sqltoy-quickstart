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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="TransInfoHisVO",description="支付交易流水表")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_trans_info_his",comment="支付交易流水表",pk_constraint="PRIMARY")
public class TransInfoHisVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1888897225560208297L;
/*---begin-auto-generate-don't-update-this-area--*/	

	@Schema(name="transId",description="交易ID",nullable=false)
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="TRANS_ID",comment="交易ID",length=32L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String transId;

	@Schema(name="transCode",description="交易代码",nullable=false)
	@Column(name="TRANS_CODE",comment="交易代码",length=20L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String transCode;

	@Schema(name="transChannel",description="交易渠道",nullable=false)
	@Column(name="TRANS_CHANNEL",comment="交易渠道",length=20L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String transChannel;

	@Schema(name="transAmt",description="交易金额",nullable=false)
	@Column(name="TRANS_AMT",comment="交易金额",length=14L,scale=2,type=java.sql.Types.DECIMAL,nativeType="DECIMAL",nullable=false)
	private BigDecimal transAmt;

	@Schema(name="status",description="交易状态",nullable=false)
	@Column(name="STATUS",comment="交易状态",length=1L,type=java.sql.Types.INTEGER,nativeType="DECIMAL",nullable=false)
	private Integer status;

	@Schema(name="resultCode",description="交易返回码",nullable=false)
	@Column(name="RESULT_CODE",comment="交易返回码",length=20L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String resultCode;

	@Schema(name="transTime",description="交易时间",nullable=false)
	@Column(name="TRANS_TIME",comment="交易时间",length=19L,type=java.sql.Types.DATE,nativeType="DATETIME",nullable=false)
	private LocalDateTime transTime;

	@Schema(name="transDate",description="交易日期",nullable=false)
	@Column(name="TRANS_DATE",comment="交易日期",length=10L,type=java.sql.Types.DATE,nativeType="DATE",nullable=false)
	private LocalDate transDate;

	@Schema(name="userId",description="用户ID",nullable=false)
	@Column(name="USER_ID",comment="用户ID",length=32L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String userId;

	@Schema(name="cardNo",description="交易卡号",nullable=true)
	@Column(name="CARD_NO",comment="交易卡号",length=32L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=true)
	private String cardNo;
	/** default constructor */
	public TransInfoHisVO() {
	}
	
	/** pk constructor */
	public TransInfoHisVO(String transId)
	{
		this.transId=transId;
	}
/*---end-auto-generate-don't-update-this-area--*/
}
