package com.sqltoy.lesson.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.BusinessId;
import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Lesson 1: Redis-based business primary key.
 *
 * The key is composed as signature + date + zero-padded sequence, e.g.
 * "PO" + "261014" + "00000001" = PO26101400000001. The sequence comes from a
 * Redis INCR counter (SQLTOY_GL_ID:tableName:signature), so it is unique and
 * strictly increasing across all application nodes.
 */
@Data
@Accessors(chain = true)
@Entity(tableName = "lesson_device_order")
public class DeviceOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	// @BusinessId on the @Id field: the primary key IS the business key
	@Id
	@BusinessId(generator = "org.sagacity.sqltoy.plugins.id.impl.RedisIdGenerator", signature = "PO@df(yyMMdd)", length = 16)
	@Column(name = "ORDER_ID", length = 16)
	private String orderId;

	@Column(name = "BUYER", length = 20)
	private String buyer;

	@Column(name = "ORGAN_ID", length = 8)
	private String organId;

	@Column(name = "DEVICE_TYPE", length = 30)
	private String deviceType;

	@Column(name = "TRANS_DATE")
	private LocalDate transDate;

	@Column(name = "TOTAL_AMT", precision = 12, scale = 2)
	private BigDecimal totalAmt;

	@Column(name = "CREATE_BY", length = 22)
	private String createBy;

	@Column(name = "CREATE_TIME")
	private LocalDateTime createTime;

	@Column(name = "UPDATE_BY", length = 22)
	private String updateBy;

	@Column(name = "UPDATE_TIME")
	private LocalDateTime updateTime;

	@Column(name = "STATUS")
	private Integer status;
}
