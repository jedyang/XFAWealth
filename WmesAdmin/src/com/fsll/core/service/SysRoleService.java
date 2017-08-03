package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysRole;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.core.vo.SysRoleVO;

public interface SysRoleService {

	/**
	 * 获取角色列表
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	public JsonPaging findAllRole(JsonPaging jsonPaging,SysRoleVO roleVO);
	
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
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	public SysRole findRoleById(String id);
	
	/**
	 * 更新保存角色
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	public SysRole saveOrUpdate(SysRole sysRole);
	
	/**
	 * 删除角色(物理删除)
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	public boolean deleteRole(String roleId);
	
	/**
	 *  获取角色下的所有用户列表
	 * @author qgfeng
	 * @date 2016-11-23
	 * @param roleId 角色id
	 * @param keyWord 关键字（账号、昵称）
	 */
	public JsonPaging findUserInRole(JsonPaging jsonPaging,String roleId,String keyWord);
	
	/**
	 * 获取不在某个角色下的所有用户列表
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	public JsonPaging findUserNotInRole(JsonPaging jaonPaging,String roleId,String keyWord);
	
	/**
	 * 批量添加用户到角色
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	public boolean saveRoleMember(String roldId,String[] adminIds);
	
	/**
	 * 删除角色用户关系
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	public boolean deleteRoleUser(String roleId,String adminId);
	
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
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	public List<MenuTreeVO> getMenuTree(String roleId,String langCode);
	
	/**
	 * 保存角色菜单
	 * @author qgfeng
	 * @date 2016-12-1
	 * @param MenuIds 菜单id集合
	 * @param roleId 角色id
	 */
	public boolean saveRoleMenu(String[] menuIds,String roleId);
	
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
	 * @param keyWord
	 * @return
	 */
	public List findAutoNameList(String filed,String keyWord);
	
	/**
	 * 获取角色下的所有用户
	 * @author mqzou
	 * @date 2016-09-01
	 * @param roleId
	 * @return
	 */
	public List findUserInRole(String roleId);
	
}
