/**
 *@Generated by sagacity-quickvo 4.18
 */
package com.sqltoy.quickstart.vo;

import org.sagacity.sqltoy.config.annotation.Secure;
import org.sagacity.sqltoy.config.annotation.SecureConfig;
import org.sagacity.sqltoy.config.annotation.SqlToyEntity;
import org.sagacity.sqltoy.model.SecureType;

import com.sqltoy.quickstart.vo.base.AbstractSecureCaseVO;

/**
 * @project sqltoy-quickstart
 * @author zhongxuchen
 * @version 1.0.0 Table: sqltoy_secure_case,Remark:安全加解密演示
 */
@SqlToyEntity
//这里telNoMask是对应telNo的脱敏13928766787 结果:139****6787，便于检索(无需则剔除)
@SecureConfig(secures = { @Secure(field = "telNo"), @Secure(field = "homeAddress"),
		@Secure(field = "telNoMask", secureType = SecureType.TEL, sourceField = "telNo"),
		@Secure(field = "homeAddressMask", secureType = SecureType.ADDRESS, sourceField = "homeAddress"), })
public class SecureCaseVO extends AbstractSecureCaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5349728950824234491L;

	/** default constructor */
	public SecureCaseVO() {
		super();
	}

	/*---begin-constructor-area---don't-update-this-area--*/
	/** pk constructor */
	public SecureCaseVO(String staffId) {
		this.staffId = staffId;
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
	public SecureCaseVO clone() {
		try {
			return (SecureCaseVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
