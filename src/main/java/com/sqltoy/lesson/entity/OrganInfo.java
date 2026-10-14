package com.sqltoy.lesson.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity(tableName = "sqltoy_organ_info")
public class OrganInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORGAN_ID", length = 8)
	private String organId;

	@Column(name = "ORGAN_NAME", length = 60)
	private String organName;

	@Column(name = "SHOW_INDEX")
	private Integer showIndex;

	@Column(name = "UPDATE_TIME")
	private LocalDateTime updateTime;
}
