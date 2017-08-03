package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysRole;
import com.fsll.core.entity.SysRoleAdmin;

public interface SysRoleService {

	/**
	 * 获取角色列表
	 * @author mqzou
	 * @date 2016-07-11
	 * @param jsonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findAllRole(JsonPaging jsonPaging,SysRole sysRole);
	
	/**
	 * 获取角色列表(存储过程)
	 * @author mqzou
	 * @date 2016-07-22
	 * @param jsonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findAllRolePro(JsonPaging jsonPaging,SysRole sysRole);
	
	/**
	 * 根据Id获取角色信息
	 * @author mqzou
	 * @date 2016-07-11
	 * @param id
	 * @return
	 */
	public SysRole findRoleById(String id);
	
	/**
	 * 保存角色信息
	 * @author mqzou
	 * @date 2016-07-11
	 * @param sysRole
	 * @return
	 */
	public SysRole saveRole(SysRole sysRole);
	
	/**
	 * 删除一条角色信息
	 * @author mqzou
	 * @date 2016-07-11
	 * @param sysRole
	 * @return
	 */
	public SysRole deleteRole(SysRole sysRole);
	
	/**
	 * 获取角色下的所有用户列表
	 * @author mqzou
	 * @date 2016-07-12
	 * @param jsonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findUserInRole(JsonPaging jsonPaging,SysRole sysRole);
	
	/**
	 *  获取不在某个角色下的所有用户列表
	 * @author mqzou
	 * @date 2016-07-12
	 * @param jaonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findUserNotInRole(JsonPaging jaonPaging,SysRole sysRole);
	
	/**
	 * 批量添加用户到角色
	 *  @author mqzou
	 * @date 2016-07-12
	 * @param list
	 * @return
	 */
	public boolean saveRoleMember(List list);
	
	/**
	 * 删除角色用户关系
	 * @author mqzou
	 * @date 2016-07-12
	 * @param sysRoleMember
	 * @return
	 */
	public boolean deleteRoleUser(String roleId,String memberId);
	
	/**
	 * 校验角色编码的唯一性
	 * @author mqzou
	 * @date 2016-07-13
	 * @param roleId
	 * @param code
	 * @return
	 */
	public boolean checkRoleCode(String roleId,String code);
	
	/**
	 * 获取角色中包含的用户组列表
	 * @author mqzou
	 * @date 2016-07-21
	 * @param jsonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findGroupInRole(JsonPaging jsonPaging,SysRole sysRole);
	
	/**
	 * 获取不包含在角色中的用户组列表
	 * @author mqzou
	 * @date 2016-07-21
	 * @param jsonPaging
	 * @param sysRole
	 * @return
	 */
	public JsonPaging findGroupNotInRole(JsonPaging jsonPaging,SysRole sysRole);
	
	/**
	 * 保存角色用户组
	 * @author mqzou
	 * @date 2016-07-21
	 * @param list
	 * @return
	 */
	public boolean saveGroup(List list); 
	
	/**
	 * 删除角色用户组
	 * @author mqzou
	 * @date 2016-07-21
	 * @param roleId
	 * @param groupId
	 * @return
	 */
	public boolean deleteGroup(String roleId,String groupId);
	
	/**
	 * 获取权限菜单树
	 * @author mqzou
	 * @date 2016-07-28
	 * @param roleId
	 * @param parentRoleId
	 * @param langCode
	 * @return
	 */
	public List getMenuTree(String roleId,String parentRoleId,String langCode);
	
	/**
	 * 获取用户所在的角色
	 * @author mqzou
	 * @date 2016-07-28
	 * @param memberId
	 * @return
	 */
	public String getMemberRoleId(String memberId);
	
	/**
	 * 保存角色菜单权限
	 * @author mqzou
	 * @date 2016-07-28
	 * @param list
	 * @return
	 */
	public boolean saveRoleMenu(List list,String roleId);
	
	/**
	 * 获取自动填充搜索条件
	 * @author mqzou
	 * @date 2016-07-28
	 * @param keyWork
	 * @return
	 */
	public List findAutoNameList(String filed,String keyWork);
	
	/**
	 * 获取角色下的所有用户
	 * @author mqzou
	 * @date 2016-09-01
	 * @param roleId
	 * @return
	 */
	public List findUserInRole(String roleId);
	
}
