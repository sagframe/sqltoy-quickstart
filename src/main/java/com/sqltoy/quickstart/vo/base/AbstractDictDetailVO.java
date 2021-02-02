/**
 *@Generated by sagacity-quickvo 4.18
 */
package com.sqltoy.quickstart.vo.base;

import java.io.Serializable;
import java.util.List;
import org.sagacity.sqltoy.config.annotation.Entity;
import java.util.ArrayList;
import org.sagacity.sqltoy.callback.SelectFields;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @version 1.0.0
 * Table: sqltoy_dict_detail,Remark:字典明细表  
 */
@Entity(tableName="sqltoy_dict_detail",pk_constraint="PRIMARY")
public abstract class AbstractDictDetailVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3528862261885504362L;
	
	/**
	 * jdbcType:VARCHAR
	 * 字典KEY
	 */
	@Id
	@Column(name="DICT_KEY",length=50L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String dictKey;
	
	/**
	 * jdbcType:VARCHAR
	 * 字典类型代码
	 */
	@Id
	@Column(name="DICT_TYPE",length=50L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String dictType;
	
	/**
	 * jdbcType:VARCHAR
	 * 字典值
	 */
	@Column(name="DICT_NAME",length=200L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String dictName;
	
	/**
	 * jdbcType:DECIMAL
	 * 显示顺序
	 */
	@Column(name="SHOW_INDEX",length=8L,defaultValue="1",type=java.sql.Types.INTEGER,nullable=false)
	protected Integer showIndex;
	
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
	

	/** default constructor */
	public AbstractDictDetailVO() {
	}
	
	/** pk constructor */
	public AbstractDictDetailVO(String dictKey,String dictType)
	{
		this.dictKey=dictKey;
		this.dictType=dictType;
	}

	
	/**
	 *@param dictKey the dictKey to set
	 */
	public AbstractDictDetailVO setDictKey(String dictKey) {
		this.dictKey=dictKey;
		return this;
	}
		
	/**
	 *@return the DictKey
	 */
	public String getDictKey() {
	    return this.dictKey;
	}
	
	/**
	 *@param dictType the dictType to set
	 */
	public AbstractDictDetailVO setDictType(String dictType) {
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
	 *@param dictName the dictName to set
	 */
	public AbstractDictDetailVO setDictName(String dictName) {
		this.dictName=dictName;
		return this;
	}
		
	/**
	 *@return the DictName
	 */
	public String getDictName() {
	    return this.dictName;
	}
	
	/**
	 *@param showIndex the showIndex to set
	 */
	public AbstractDictDetailVO setShowIndex(Integer showIndex) {
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
	 *@param updateBy the updateBy to set
	 */
	public AbstractDictDetailVO setUpdateBy(String updateBy) {
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
	public AbstractDictDetailVO setUpdateTime(LocalDateTime updateTime) {
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
	public AbstractDictDetailVO setStatus(Integer status) {
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
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("dictKey=").append(getDictKey()).append("\n");
		columnsBuffer.append("dictType=").append(getDictType()).append("\n");
		columnsBuffer.append("dictName=").append(getDictName()).append("\n");
		columnsBuffer.append("showIndex=").append(getShowIndex()).append("\n");
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
		
	    public SelectFieldsImpl dictKey() {
	    	if (!fields.contains("dictKey")) {
				fields.add("dictKey");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl dictType() {
	    	if (!fields.contains("dictType")) {
				fields.add("dictType");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl dictName() {
	    	if (!fields.contains("dictName")) {
				fields.add("dictName");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl showIndex() {
	    	if (!fields.contains("showIndex")) {
				fields.add("showIndex");
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
