/**
 *@Generated by sagacity-quickvo 5.0
 */
package com.sqltoy.quickstart.vo;

import java.io.Serializable;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Indexes;
import org.sagacity.sqltoy.config.annotation.Index;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.sagacity.sqltoy.config.annotation.Foreign;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 
 */
@Schema(name="DictDetailVO",description="字典明细表")
@Data
@Accessors(chain = true)
@Entity(tableName="sqltoy_dict_detail",comment="字典明细表",pk_constraint="PRIMARY")
@Indexes(indexes={@Index(name="FK_DICT_TYPE_REF_ITEM",columns={"DICT_TYPE"},sortTypes={"ASC"})
})
public class DictDetailVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4263323295248169755L;
/*---begin-auto-generate-don't-update-this-area--*/	

	@Schema(name="dictKey",description="字典KEY",nullable=false)
	@Id
	@Column(name="DICT_KEY",comment="字典KEY",length=50L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String dictKey;

	@Schema(name="dictType",description="字典类型代码",nullable=false)
	@Id
	@Foreign(table="sqltoy_dict_type",field="DICT_TYPE",deleteRestict=1,updateRestict=1,constraintName="FK_DICT_TYPE_REF_ITEM")
	@Column(name="DICT_TYPE",comment="字典类型代码",length=50L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String dictType;

	@Schema(name="dictName",description="字典值",nullable=false)
	@Column(name="DICT_NAME",comment="字典值",length=200L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String dictName;

	@Schema(name="showIndex",description="显示顺序",nullable=false)
	@Column(name="SHOW_INDEX",comment="显示顺序",length=8L,defaultValue="1",type=java.sql.Types.INTEGER,nativeType="DECIMAL",nullable=false)
	private Integer showIndex;

	@Schema(name="updateBy",description="最后修改人",nullable=false)
	@Column(name="UPDATE_BY",comment="最后修改人",length=22L,type=java.sql.Types.VARCHAR,nativeType="VARCHAR",nullable=false)
	private String updateBy;

	@Schema(name="updateTime",description="最后修改时间",nullable=false)
	@Column(name="UPDATE_TIME",comment="最后修改时间",length=19L,type=java.sql.Types.DATE,nativeType="DATETIME",nullable=false)
	private LocalDateTime updateTime;

	@Schema(name="status",description="状态",nullable=false)
	@Column(name="STATUS",comment="状态",length=1L,defaultValue="1",type=java.sql.Types.INTEGER,nativeType="DECIMAL",nullable=false)
	private Integer status;
	/** default constructor */
	public DictDetailVO() {
	}
	
	/** pk constructor */
	public DictDetailVO(String dictKey,String dictType)
	{
		this.dictKey=dictKey;
		this.dictType=dictType;
	}
/*---end-auto-generate-don't-update-this-area--*/
}
