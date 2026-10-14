package com.sqltoy.plugins;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sagacity.sqltoy.model.DataAuthFilterConfig;
import org.sagacity.sqltoy.model.IgnoreCaseSet;
import org.sagacity.sqltoy.model.IgnoreKeyCaseMap;
import org.sagacity.sqltoy.plugins.IUnifyFieldsHandler;

/**
 * @project sqltoy-showcase
 * @description demo of unified audit-field assignment
 * @author chenrenfei <a href="mailto:zhongxuchen@gmail.com">contact the author</a>
 * @version id:SqlToyUnifyFieldsHandler.java, Revision:v1.0, Date:2018-01-18
 */
public class SqlToyUnifyFieldsHandler implements IUnifyFieldsHandler {
	private String defaultUserName = "system-auto";

	@Override
	public Map<String, Object> createUnifyFields() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		LocalDateTime now = LocalDateTime.now();
		// fetch the current user from your security context (Spring Security etc.)
		String userId = getUserId();
		// standard audit columns; fields that do not exist on a table are ignored automatically
		map.put("createBy", userId);
		map.put("createTime", now);
		map.put("updateBy", userId);
		map.put("updateTime", now);
		return map;
	}

	@Override
	public Map<String, Object> updateUnifyFields() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("updateBy", getUserId());
		map.put("updateTime", LocalDateTime.now());
		return map;
	}

	/**
	 * fields that must be updated even when null; return null when there are none
	 */
	@Override
	public IgnoreCaseSet forceUpdateFields() {
		return null;
	}

	/**
	 * unified data-authentication parameters and privilege-escalation checks;
	 * reference them in sql as: and tenant_id in (:authedTenantIds)
	 */
	@Override
	public IgnoreKeyCaseMap<String, DataAuthFilterConfig> dataAuthFilters() {
		IgnoreKeyCaseMap<String, DataAuthFilterConfig> map = new IgnoreKeyCaseMap<String, DataAuthFilterConfig>();
		// simulated authorized organizations
		Set<String> organIds = new HashSet<String>();
		organIds.add("100005");
		organIds.add("100007");
		// use :authedOrganIds in sql to reference the authorized organizations
		map.put("authedOrganIds", new DataAuthFilterConfig().setForcelimit(true).setValues(organIds));
		return map;
	}

	/**
	 * @todo fetch the current user id
	 * @return
	 */
	private String getUserId() {
		// return the current user from Spring Security in a real project, e.g.:
		// return (SpringSecurityUtils.getCurrentUser() != null) ?
		// SpringSecurityUtils.getCurrentUser().getId() : defaultUserName;
		return defaultUserName;
	}

	/**
	 * @return the defaultUserName
	 */
	public String getDefaultUserName() {
		return defaultUserName;
	}

	/**
	 * @param defaultUserName the defaultUserName to set
	 */
	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}

}
