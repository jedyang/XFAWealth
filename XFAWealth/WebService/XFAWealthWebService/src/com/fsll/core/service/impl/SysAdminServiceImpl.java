package com.fsll.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysAdminService;

@Service("sysAdminService")
public class SysAdminServiceImpl extends BaseService implements SysAdminService{

	/**
     * 获取实体 SysAdmin
     * @author wwluo
     * @date 2017-03-20
	 * @param sysAdminId
     */
	@Override
	public SysAdmin findById(String sysAdminId) {
		SysAdmin admin = null;
		if (StringUtils.isNotBlank(sysAdminId)) {
			admin = (SysAdmin) this.baseDao.get(SysAdmin.class, sysAdminId);
		}
		return admin;
	}

}
