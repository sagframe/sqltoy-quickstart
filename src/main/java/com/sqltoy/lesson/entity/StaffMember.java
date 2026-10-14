package com.sqltoy.lesson.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Translate;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Hand-written entity for the lessons — no code generator required.
 * The 22-digit primary key is assigned by DefaultIdGenerator when left null.
 */
@Data
@Accessors(chain = true)
@Entity(tableName = "sqltoy_staff_info")
public class StaffMember implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(strategy = "generator", generator = "org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name = "STAFF_ID", length = 22)
	private String staffId;

	@Column(name = "STAFF_CODE", length = 22)
	private String staffCode;

	@Column(name = "STAFF_NAME", length = 30)
	private String staffName;

	@Column(name = "ORGAN_ID", length = 8)
	private String organId;

	@Column(name = "SEX_TYPE", length = 1)
	private String sexType;

	@Column(name = "ENTRY_DATE")
	private LocalDate entryDate;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "EMAIL", length = 60)
	private String email;

	@Column(name = "CREATE_BY", length = 22)
	private String createBy;

	@Column(name = "CREATE_TIME")
	private LocalDateTime createTime;

	@Column(name = "UPDATE_BY", length = 22)
	private String updateBy;

	@Column(name = "UPDATE_TIME")
	private LocalDateTime updateTime;

	// not a table column: filled in memory from the organIdName cache (Lesson 4)
	@Translate(cacheName = "organIdName", keyField = "organId")
	private String organName;

	// filled by the xml <translate> in lesson04_staff_with_names
	private String sexTypeName;
}
