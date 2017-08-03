package com.fsll.wmes.role.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/***
 * 接口：控制台角色
 * @author mqzou
 * @date 2016-11-11
 */
public interface MemberConsoleRoleService {
	
	/**
	 *  分页获取控制台角色列表
	 * @author qgfeng
	 * @date 2016-12-8
	 * @param role 条件查询
	 * @param admin 当前登入人权限
	 */
	public JsonPaging findRoleByPage(JsonPaging jsonPaging,MemberConsoleRole role,MemberAdmin admin);
	
	/**
	 * 根据id查找角色 MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	public MemberConsoleRole findRoleById(String id);
	
	/**
	 * 保存更新控制台角色 MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	public MemberConsoleRole saveOrUpdateRole(MemberConsoleRole role);
	

	/**
	 * 删除角色(物理删除) 级联删除角色下的菜单权限
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	public boolean deleteRole(String roleId);
	
	/**
	 * 根据角色查找菜单
	 * @author qgfeng
	 * @date 2016-12-8
	 * @param roleId 角色id
	 * @param langCode
	 * @param curMember 当前用户（权限信息）
	 */
	public List<MenuTreeVO> getMenuTree(String roleId,MemberAdmin admin,String langCode);
	
	/**
	 * 保存角色菜单
	 * @author qgfeng
	 * @date 2016-11-22
	 * @param MenuIds 菜单id集合
	 * @param roleId 角色id
	 */
	public boolean saveRoleMenu(String[] menuIds,String roleId);
	
	/**
	 *  获取角色下的所有MemberBase列表（分页）
	 * @author qgfeng
	 * @date 2016-12-8
	 * @param consoleRoleId 角色id
	 * @param keyWord 关键字（账号、昵称）
	 */
	public JsonPaging findMemberInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin);
	
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
	 * 批量添加成员到角色
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	public boolean saveRoleMember(String roldId,String[] memberIds);
 
	/**
	 * 获取角色中的所有成员
	 * @author mqzou
     * @date 2016-11-11
	 * @param roleId
	 * @return
	 */
  public List<MemberConsoleRoleMember> findConsoleRoleMembers(String roleId);
  
  /**
   * 获取成员角色关系
   * @author mqzou
     * @date 2016-11-17
   * @param userId
   * @param roleId
   * @return
   */
  public MemberConsoleRoleMember findConsoleRoleMemberById(String userId,String roleId);
}
