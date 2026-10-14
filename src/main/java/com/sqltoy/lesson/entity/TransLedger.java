package com.sqltoy.lesson.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Lesson 6: a high-concurrency ledger row.
 *
 * updateFetch / updateSaveFetch lock the row, apply changes derived from the
 * current values, and return the updated result — all in one database interaction.
 */
@Data
@Accessors(chain = true)
@Entity(tableName = "lesson_trans_ledger")
public class TransLedger implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORDER_ID", length = 16)
	private String orderId;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "AMT", precision = 12, scale = 2)
	private BigDecimal amt;

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
