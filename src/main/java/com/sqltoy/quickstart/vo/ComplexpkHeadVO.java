/**
 *@Generated by sagacity-quickvo 4.15
 */
package com.sqltoy.quickstart.vo;

import java.time.LocalDate;
import java.util.List;

import org.sagacity.sqltoy.config.annotation.SqlToyEntity;

import com.sqltoy.quickstart.vo.base.AbstractComplexpkHeadVO;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0
 * Table: sqltoy_complexpk_head,Remark:复合主键级联操作主表 	
 */
@SqlToyEntity
public class ComplexpkHeadVO extends AbstractComplexpkHeadVO {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8333098323861603937L;
	
	/** default constructor */
	public ComplexpkHeadVO() {
		super();
	}
	/*---begin-constructor-area---don't-update-this-area--*/
	/** pk constructor */
	public ComplexpkHeadVO(LocalDate transDate,String transCode)
	{
		this.transDate=transDate;
		this.transCode=transCode;
	}
	/*---end-constructor-area---don't-update-this-area--*/
	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		return super.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ComplexpkHeadVO clone() {
		try {
			return (ComplexpkHeadVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
