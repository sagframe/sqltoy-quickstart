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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @project sqltoy-quickstart
 * @version 1.0.0
 * Table: sqltoy_trans_showcase,Remark:事务演示表  
 */
@Entity(tableName="sqltoy_trans_showcase",pk_constraint="PRIMARY")
public abstract class AbstractTransShowcaseVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5815042127480095831L;
	
	/**
	 * jdbcType:VARCHAR
	 * ID
	 */
	@Id(strategy="generator",generator="org.sagacity.sqltoy.plugins.id.impl.DefaultIdGenerator")
	@Column(name="ID",length=32L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String id;
	
	/**
	 * jdbcType:VARCHAR
	 * 订单ID
	 */
	@Column(name="ORDER_ID",length=32L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String orderId;
	
	/**
	 * jdbcType:INT
	 * 数量
	 */
	@Column(name="QUANTITY",length=10L,type=java.sql.Types.INTEGER,nullable=false)
	protected Integer quantity;
	
	/**
	 * jdbcType:DECIMAL
	 * 金额
	 */
	@Column(name="AMT",length=12L,type=java.sql.Types.DECIMAL,nullable=false)
	protected BigDecimal amt;
	
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
	

	/** default constructor */
	public AbstractTransShowcaseVO() {
	}
	
	/** pk constructor */
	public AbstractTransShowcaseVO(String id)
	{
		this.id=id;
	}

	
	/**
	 *@param id the id to set
	 */
	public AbstractTransShowcaseVO setId(String id) {
		this.id=id;
		return this;
	}
		
	/**
	 *@return the Id
	 */
	public String getId() {
	    return this.id;
	}
	
	/**
	 *@param orderId the orderId to set
	 */
	public AbstractTransShowcaseVO setOrderId(String orderId) {
		this.orderId=orderId;
		return this;
	}
		
	/**
	 *@return the OrderId
	 */
	public String getOrderId() {
	    return this.orderId;
	}
	
	/**
	 *@param quantity the quantity to set
	 */
	public AbstractTransShowcaseVO setQuantity(Integer quantity) {
		this.quantity=quantity;
		return this;
	}
		
	/**
	 *@return the Quantity
	 */
	public Integer getQuantity() {
	    return this.quantity;
	}
	
	/**
	 *@param amt the amt to set
	 */
	public AbstractTransShowcaseVO setAmt(BigDecimal amt) {
		this.amt=amt;
		return this;
	}
		
	/**
	 *@return the Amt
	 */
	public BigDecimal getAmt() {
	    return this.amt;
	}
	
	/**
	 *@param createBy the createBy to set
	 */
	public AbstractTransShowcaseVO setCreateBy(String createBy) {
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
	public AbstractTransShowcaseVO setCreateTime(LocalDateTime createTime) {
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
	public AbstractTransShowcaseVO setUpdateBy(String updateBy) {
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
	public AbstractTransShowcaseVO setUpdateTime(LocalDateTime updateTime) {
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
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("id=").append(getId()).append("\n");
		columnsBuffer.append("orderId=").append(getOrderId()).append("\n");
		columnsBuffer.append("quantity=").append(getQuantity()).append("\n");
		columnsBuffer.append("amt=").append(getAmt()).append("\n");
		columnsBuffer.append("createBy=").append(getCreateBy()).append("\n");
		columnsBuffer.append("createTime=").append(getCreateTime()).append("\n");
		columnsBuffer.append("updateBy=").append(getUpdateBy()).append("\n");
		columnsBuffer.append("updateTime=").append(getUpdateTime()).append("\n");
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
		
	    public SelectFieldsImpl id() {
	    	if (!fields.contains("id")) {
				fields.add("id");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl orderId() {
	    	if (!fields.contains("orderId")) {
				fields.add("orderId");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl quantity() {
	    	if (!fields.contains("quantity")) {
				fields.add("quantity");
			}
	    	return this;
	    }
    
	    public SelectFieldsImpl amt() {
	    	if (!fields.contains("amt")) {
				fields.add("amt");
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
    
	}
}
