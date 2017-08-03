package com.fsll.wmes.company.service;

import java.util.List;

import com.fsll.core.vo.MenuTreeVO;

public interface MemberCompanyMenuService {

	/**
	 * 菜单数据获取
	 * @author wwluo
	 * @date 2017-06-23
	 * @param companyId company_info.id
	 * @param roleCode member_role.code
	 * @param langCode 多语言标识
	 * @param MenuTreeVO 
	 */
	public List<MenuTreeVO> getMenus(String companyId, String roleCode, String langCode);

	/**
	 * 保存菜单权限
	 * @author wwluo
	 * @date 2017-06-23
	 * @param companyId company_info.id
	 * @param roleCode member_role.code
	 * @param menuIds 菜单集合字符串
	 */
	public Boolean saveMenu(String companyId, String roleCode, String menuIds);

}
