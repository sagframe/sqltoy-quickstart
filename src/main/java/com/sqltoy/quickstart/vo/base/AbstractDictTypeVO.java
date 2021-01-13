/**
 *@Generated by sagacity-quickvo 4.17
 */
package com.sqltoy.quickstart.vo.base;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.callback.SelectFields;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import java.time.LocalDateTime;
import org.sagacity.sqltoy.config.annotation.OneToMany;
import com.sqltoy.quickstart.vo.DictDetailVO;

/**
 * @project sqltoy-quickstart
 * @version 1.0.0
 * Table: sqltoy_dict_type,Remark:字典分类表  
 */
@Entity(tableName="sqltoy_dict_type",pk_constraint="PRIMARY")
public abstract class AbstractDictTypeVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4214731613767317059L;
	
	/**
	 * jdbcType:VARCHAR
	 * 字典类型代码
	 */
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="DICT_TYPE",length=50L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String dictType;
	
	/**
	 * jdbcType:VARCHAR
	 * 字典类型名称
	 */
	@Column(name="DICT_TYPE_NAME",length=100L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String dictTypeName;
	
	/**
	 * jdbcType:VARCHAR
	 * 说明
	 */
	@Column(name="COMMENTS",length=500L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String comments;
	
	/**
	 * jdbcType:DECIMAL
	 * 显示顺序
	 */
	@Column(name="SHOW_INDEX",length=8L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	protected Integer showIndex;
	
	/**
	 * jdbcType:VARCHAR
	 * 创建人
	 */
	@Column(name="CREATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String createBy;
	
	/**
	 * jdbcType:DATETIME
	 * 创建时间
	 */
	@Column(name="CREATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime createTime;
	
	/**
	 * jdbcType:VARCHAR
	 * 最后修改人
	 */
	@Column(name="UPDATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String updateBy;
	
	/**
	 * jdbcType:DATETIME
	 * 最后修改时间
	 */
	@Column(name="UPDATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime updateTime;
	
	/**
	 * jdbcType:DECIMAL
	 * 状态
	 */
	@Column(name="STATUS",length=1L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	protected Integer status;
	

	/**
	 * 主键关联子表信息
	 */
	@OneToMany(fields={"dictType"},mappedTable="sqltoy_dict_detail",mappedColumns={"DICT_TYPE"},mappedFields={"dictType"},orderBy="showIndex desc")
	protected List<DictDetailVO> dictDetailVOs=new ArrayList<DictDetailVO>();

	/** default constructor */
	public AbstractDictTypeVO() {
	}
	
	/** pk constructor */
	public AbstractDictTypeVO(String dictType)
	{
		this.dictType=dictType;
	}

	
	/**
	 *@param dictType the dictType to set
	 */
	public AbstractDictTypeVO setDictType(String dictType) {
		this.dictType=dictType;
		return this;
	}
		
	/**
	 *@return the DictType
	 */
	public String getDictType() {
	    return this.dictType;
	}
	
	/**
	 *@param dictTypeName the dictTypeName to set
	 */
	public AbstractDictTypeVO setDictTypeName(String dictTypeName) {
		this.dictTypeName=dictTypeName;
		return this;
	}
		
	/**
	 *@return the DictTypeName
	 */
	public String getDictTypeName() {
	    return this.dictTypeName;
	}
	
	/**
	 *@param comments the comments to set
	 */
	public AbstractDictTypeVO setComments(String comments) {
		this.comments=comments;
		return this;
	}
		
	/**
	 *@return the Comments
	 */
	public String getComments() {
	    return this.comments;
	}
	
	/**
	 *@param showIndex the showIndex to set
	 */
	public AbstractDictTypeVO setShowIndex(Integer showIndex) {
		this.showIndex=showIndex;
		return this;
	}
		
	/**
	 *@return the ShowIndex
	 */
	public Integer getShowIndex() {
	    return this.showIndex;
	}
	
	/**
	 *@param createBy the createBy to set
	 */
	public AbstractDictTypeVO setCreateBy(String createBy) {
		this.createBy=createBy;
		return this;
	}
		
	/**
	 *@return the CreateBy
	 */
	public String getCreateBy() {
	    return this.createBy;
	}
	
	/**
	 *@param createTime the createTime to set
	 */
	public AbstractDictTypeVO setCreateTime(LocalDateTime createTime) {
		this.createTime=createTime;
		return this;
	}
		
	/**
	 *@return the CreateTime
	 */
	public LocalDateTime getCreateTime() {
	    return this.createTime;
	}
	
	/**
	 *@param updateBy the updateBy to set
	 */
	public AbstractDictTypeVO setUpdateBy(String updateBy) {
		this.updateBy=updateBy;
		return this;
	}
		
	/**
	 *@return the UpdateBy
	 */
	public String getUpdateBy() {
	    return this.updateBy;
	}
	
	/**
	 *@param updateTime the updateTime to set
	 */
	public AbstractDictTypeVO setUpdateTime(LocalDateTime updateTime) {
		this.updateTime=updateTime;
		return this;
	}
		
	/**
	 *@return the UpdateTime
	 */
	public LocalDateTime getUpdateTime() {
	    return this.updateTime;
	}
	
	/**
	 *@param status the status to set
	 */
	public AbstractDictTypeVO setStatus(Integer status) {
		this.status=status;
		return this;
	}
		
	/**
	 *@return the Status
	 */
	public Integer getStatus() {
	    return this.status;
	}


	/**
	 * @return the dictDetailVOs
	 */
	public List<DictDetailVO> getDictDetailVOs() {
		return this.dictDetailVOs;
	}
	
	public void setDictDetailVOs(List<DictDetailVO> dictDetailVOs)	{
		this.dictDetailVOs=dictDetailVOs;
	}

	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("dictType=").append(getDictType()).append("\n");
		columnsBuffer.append("dictTypeName=").append(getDictTypeName()).append("\n");
		columnsBuffer.append("comments=").append(getComments()).append("\n");
		columnsBuffer.append("showIndex=").append(getShowIndex()).append("\n");
		columnsBuffer.append("createBy=").append(getCreateBy()).append("\n");
		columnsBuffer.append("createTime=").append(getCreateTime()).append("\n");
		columnsBuffer.append("updateBy=").append(getUpdateBy()).append("\n");
		columnsBuffer.append("updateTime=").append(getUpdateTime()).append("\n");
		columnsBuffer.append("status=").append(getStatus()).append("\n");
		return columnsBuffer.toString();
	}
	
	/**
	 * @TODO create entityQuery fields
	 */
	public static SelectFieldsImpl select() {
		return new SelectFieldsImpl();
	}
	
	public static class SelectFieldsImpl extends SelectFields {
		private List<String> fields = new ArrayList<String>();

		@Override
		public String[] getSelectFields() {
			String[] result = new String[fields.size()];
			fields.toArray(result);
			return result;
		}
		
	    public SelectFieldsImpl dictType() {
	    	if (!fields.contains("dictType")) {
				fields.add("dictType");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl dictTypeName() {
	    	if (!fields.contains("dictTypeName")) {
				fields.add("dictTypeName");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl comments() {
	    	if (!fields.contains("comments")) {
				fields.add("comments");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl showIndex() {
	    	if (!fields.contains("showIndex")) {
				fields.add("showIndex");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl createBy() {
	    	if (!fields.contains("createBy")) {
				fields.add("createBy");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl createTime() {
	    	if (!fields.contains("createTime")) {
				fields.add("createTime");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl updateBy() {
	    	if (!fields.contains("updateBy")) {
				fields.add("updateBy");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl updateTime() {
	    	if (!fields.contains("updateTime")) {
				fields.add("updateTime");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl status() {
	    	if (!fields.contains("status")) {
				fields.add("status");
			}
	    	return this;
	    }
    
	}
}
