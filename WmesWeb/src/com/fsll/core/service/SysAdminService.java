package com.fsll.core.service;

import com.fsll.core.entity.SysAdmin;

public interface SysAdminService {

	/**
     * 获取实体 SysAdmin
     * @author wwluo
     * @date 2017-03-20
	 * @param sysAdminId
     */
	public SysAdmin findById(String sysAdminId);
}
