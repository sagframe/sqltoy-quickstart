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
