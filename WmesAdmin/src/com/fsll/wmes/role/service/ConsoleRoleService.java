package com.fsll.wmes.role.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.member.vo.MemberAdminVO;

public interface ConsoleRoleService {
	/**
	 * 分页获取工作台角色列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	public JsonPaging findRoleByPage(JsonPaging jsonPaging,MemberConsoleRole role);
	/**
	 * 根据id查找MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	public MemberConsoleRole findById(String id);
	
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
	 *  获取角色下的所有成员列表
	 * @author qgfeng
	 * @date 2016-11-23
	 * @param roleId 角色id
	 * @param keyWord 关键字（账号、昵称）
	 */
	public JsonPaging findMemberInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin);
	
	/**
	 * 删除角色
	 * @author Yan
	 * @date 2017-02-14
	 */
	public void deleteConsoleRole(MemberConsoleRole info);
	
	/**
	 * 删除角色成员
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	public void deleteRoleMember(MemberConsoleRoleMember roleMember);
	
	/**
	 * 获取不在某个角色下、并所属某个ifafrim或代理商的所有用户列表
	 * @author qgfeng
	 * @date 2016-11-23
	 * @param consoleRoleId 角色id
	 * @param type:1 是firm 2是代理商
	 * @param keyWord 关键字（账号、昵称）
	 */
	public JsonPaging findMemberAdminNotInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin);
	
	/**
	 * 添加角色
	 * @author Yan
	 * @date 2017-02-14
	 */
	public MemberConsoleRole saveConsoleRole(MemberConsoleRole info);
	
	/**
	 * 批量添加成员到角色
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	public boolean saveRoleMember(String roldId,String[] memberIds);

}
