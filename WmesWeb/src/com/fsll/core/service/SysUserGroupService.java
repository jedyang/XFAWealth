package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysRoleUsergroup;
import com.fsll.core.entity.SysUsergroup;
import com.fsll.core.entity.SysUsergroupAdmin;

public interface SysUserGroupService {

	/**
	 * 根据id获取用户组信息
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @return
	 */
	public SysUsergroup findSysUsergroupById(String id);
	
	/**
	 * 获取用户组列表
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @return
	 */
	public JsonPaging findAllUserGroup(JsonPaging jsonPaging,SysUsergroup sysUsergroup);
	
	/**
	 * 获取用户组列表(存储过程)
	 * @author mqzou
	 * @date 2016-07-22
	 * @param jsonPaging
	 * @param sysUsergroup
	 * @return
	 */
	public JsonPaging findAllUserGroupPro(JsonPaging jsonPaging,SysUsergroup sysUsergroup);
	
	/**
	 * 保存用户组信息
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @return
	 */
	public SysUsergroup saveUsergroup(SysUsergroup sysUsergroup);
	
	/**
	 * 保存用户组信息
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @return
	 */
	public boolean saveUsergroupMember(List list);
	
	/**
	 * 删除一条用户组
	 * @author mqzou
	 * @date 2016-07-20
	 * @param sysUsergroup
	 * @return
	 */
	public boolean deleteUserGroup(SysUsergroup sysUsergroup);
	
	/**
	 * 获取用户组中包含的用户列表
	 * @author mqzou
	 * @date 2016-07-20
	 * @param jsonPaging
	 * @param sysUsergroup
	 * @return
	 */
	public JsonPaging findUserInGroup(JsonPaging jsonPaging,SysUsergroup sysUsergroup);
	
	/**
	 * 获取不包含在某用户组中的用户列表
	 * @author mqzou
	 * @date 2016-07-20
	 * @param jsonPaging
	 * @param sysUsergroup
	 * @return
	 */
	public JsonPaging findUserNotInGroup(JsonPaging jsonPaging,SysUsergroup sysUsergroup);
	
	/**
	 * 保存用户和用户组的关系
	 * @author mqzou
	 * @date 2016-07-20
	 * @param list
	 * @return
	 */
	public boolean saveGroupMember(List list);
	
	/**
	 * 删除用户和用户组的关系
	 * @author mqzou
	 * @date 2016-07-20
	 * @param groupId
	 * @param memberId
	 * @return
	 */
	public boolean deleteGroupMember(String groupId,String memberId);
	
	/**
	 * 校验用户组名称的唯一性
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkGroupName(String id,String name);
	
	

}
