package com.sqltoy.quickstart.service.impl;

import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.model.TreeTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sqltoy.quickstart.service.OrganInfoService;
import com.sqltoy.quickstart.vo.OrganInfoVO;

/**
 * @project sqltoy-quickstart
 * @description organization service demo (tree-structured data)
 * @author zhongxuchen
 * @version v1.0, Date:2020-07-22
 */
@Service("organInfoService")
public class OrganInfoServiceImpl implements OrganInfoService {
	@Autowired
	SqlToyLazyDao sqlToyLazyDao;

	@Transactional
	public void saveOrganInfo(OrganInfoVO organInfoVO) {
		// save the organization first
		sqlToyLazyDao.saveOrUpdate(organInfoVO);
		// the id field is matched to the table primary key automatically;
		// NODE_ROUTE / NODE_LEVEL / IS_LEAF are standard names and need no extra setup
		// only the pid field must be told to the model (vo properties map to columns automatically)
		// idField can be omitted when it is the primary key
		sqlToyLazyDao.wrapTreeTableRoute(new TreeTableModel(organInfoVO).idField("organCode").pidField("organPid"));
	}

}
