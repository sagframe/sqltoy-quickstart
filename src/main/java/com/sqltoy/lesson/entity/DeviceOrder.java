package com.sqltoy.lesson.entity;

import java.io.Serial;
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
 * 第 1 课：基于 Redis 的业务主键。
 *
 * <p>主键由 前缀 + 日期 + 左补零序列号 组成，例如
 * <p>"PO" + "261014" + "00000001" = PO26101400000001。序列来自 Redis INCR
 * <p>计数器（<code>SQLTOY_GL_ID:tableName:signature</code），因此在所有应用节点间唯一且严格递增。
 */
@Data
@Accessors(chain = true)
@Entity(tableName = "lesson_device_order")
public class DeviceOrder implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 订单 ID
	 * <p><code>@BusinessId</code> 在 <code>@Id</code> 字段上：主键就是业务主键
	 */
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
