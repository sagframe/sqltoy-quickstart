package com.sqltoy.lesson.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Typed result for the Lesson 5 aggregate query.
 */
@Data
public class OrderTotal {

	private BigDecimal totalAmt;
}
