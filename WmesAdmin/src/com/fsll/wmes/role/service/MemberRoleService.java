package com.fsll.wmes.role.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberRole;
import com.fsll.wmes.role.vo.MemberRoleVO;

public interface MemberRoleService {
	/**
	 * 前台角色列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	public JsonPaging findRoleByPage(JsonPaging jsonPaging,MemberRoleVO role);
	
	/**
	 * 根据id查找MemberRole
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	public MemberRole findById(String id);
	
	/**
	 * 保存更新 MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	public MemberRole saveOrUpdate(MemberRole role);
	
	/**
	 * 删除角色(级联删除角色下的菜单权限)
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	public boolean deleteRole(String roleId);
	
	/**
	 * 根据角色查找菜单
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	public List<MenuTreeVO> getMenuTree(String roleId,String langCode);
	
	/**
	 * 保存角色菜单
	 * @author qgfeng
	 * @date 2016-11-22
	 * @param MenuIds 菜单id集合
	 * @param roleId 角色id
	 */
	public boolean saveRoleMenu(String[] menuIds,String roleId);

	/**
	 * 保存用户角色菜单
	 * @author wwluo
	 * @date 2017-05-31
	 * @param memberMenuId 用户菜单ID
	 * @param oldRoleCode 角色编码 member_role.code
	 * @param roleCode 角色编码 member_role.code
	 */
	public void saveMemberRoleMenu(String memberMenuId, String oldRoleCode, String roleCode);
	
}
