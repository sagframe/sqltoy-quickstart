/**
 *@Generated by sagacity-quickvo 5.0
 */
package com.sqltoy.quickstart.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.OneToMany;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0
 */
@Schema(name = "DictTypeVO", description = "字典分类表")
@Data
@Accessors(chain = true)
@Entity(tableName = "sqltoy_dict_type", comment = "字典分类表", pk_constraint = "PRIMARY")
public class DictTypeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7652087330184138442L;
	/*---begin-auto-generate-don't-update-this-area--*/
	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name = "dictType", description = "字典类型代码", nullable = false)
	@Id(strategy = "generator", generator = "org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name = "DICT_TYPE", comment = "字典类型代码", length = 50L, type = java.sql.Types.VARCHAR, nullable = false)
	private String dictType;

	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name = "dictTypeName", description = "字典类型名称", nullable = false)
	@Column(name = "DICT_TYPE_NAME", comment = "字典类型名称", length = 100L, type = java.sql.Types.VARCHAR, nullable = false)
	private String dictTypeName;

	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name = "comments", description = "说明", nullable = true)
	@Column(name = "COMMENTS", comment = "说明", length = 500L, type = java.sql.Types.VARCHAR, nullable = true)
	private String comments;

	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name = "showIndex", description = "显示顺序", nullable = false)
	@Column(name = "SHOW_INDEX", comment = "显示顺序", length = 8L, defaultValue = "1", type = java.sql.Types.INTEGER, nullable = false)
	private Integer showIndex;

	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name = "createBy", description = "创建人", nullable = false)
	@Column(name = "CREATE_BY", comment = "创建人", length = 22L, type = java.sql.Types.VARCHAR, nullable = false)
	private String createBy;

	/**
	 * jdbcType:DATETIME
	 */
	@Schema(name = "createTime", description = "创建时间", nullable = false)
	@Column(name = "CREATE_TIME", comment = "创建时间", length = 19L, type = java.sql.Types.DATE, nullable = false)
	private LocalDateTime createTime;

	/**
	 * jdbcType:VARCHAR
	 */
	@Schema(name = "updateBy", description = "最后修改人", nullable = false)
	@Column(name = "UPDATE_BY", comment = "最后修改人", length = 22L, type = java.sql.Types.VARCHAR, nullable = false)
	private String updateBy;

	/**
	 * jdbcType:DATETIME
	 */
	@Schema(name = "updateTime", description = "最后修改时间", nullable = false)
	@Column(name = "UPDATE_TIME", comment = "最后修改时间", length = 19L, type = java.sql.Types.DATE, nullable = false)
	private LocalDateTime updateTime;

	/**
	 * jdbcType:DECIMAL
	 */
	@Schema(name = "status", description = "状态", nullable = false)
	@Column(name = "STATUS", comment = "状态", length = 1L, defaultValue = "1", type = java.sql.Types.INTEGER, nullable = false)
	private Integer status;

	/**
	 * 主键关联子表信息
	 * notNullField 为将查询结果封装成父子对象关系时，过来掉因join时子表是否有数据提供一个判断依据
	 */
	@OneToMany(fields = { "dictType" }, mappedFields = {
			"dictType" }, delete = true, orderBy = "showIndex desc", notNullField = "dictKey")
	private List<DictDetailVO> dictDetailVOs = new ArrayList<DictDetailVO>();

	/** default constructor */
	public DictTypeVO() {
	}

	/** pk constructor */
	public DictTypeVO(String dictType) {
		this.dictType = dictType;
	}
	/*---end-auto-generate-don't-update-this-area--*/
}
