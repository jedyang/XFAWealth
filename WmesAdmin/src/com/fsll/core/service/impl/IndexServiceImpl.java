/**
 * 首页权限验证
 */
package com.fsll.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.IndexService;

/**
 * @author scshi
 * @date 20160412
 *	首页接口实现
 */
@Service("indexService")
//@Transactional
public class IndexServiceImpl extends BaseService implements IndexService {
	
	/**
	 * 获取登陆用户菜单权限
	 * @author scshi
	 * @date 20160412
	 * */
	@Override
	public String loadSysMenuJson(SysAdmin loginUser) {
		String result="";
		return result;
	}
	
}
