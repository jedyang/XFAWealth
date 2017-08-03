package com.fsll.wmes.role;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.entity.MemberRole;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.role.service.ConsoleRoleService;
import com.fsll.wmes.role.service.MemberRoleService;
import com.fsll.wmes.role.vo.MemberRoleVO;

/**
 * 工作台角色控制
 * @author qgfeng
 * @date 2016-11-21
 */
@Controller
@RequestMapping("/console/role")
public class ConsoleRoleAct extends CoreBaseAct{
	
	@Autowired
	private ConsoleRoleService consoleRoleService;
	
	
	/**
	 * 工作台角色管理列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "console/role/list";
	}
	
	/**
	 * 工作台角色分页获得记录
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,MemberConsoleRole role) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = consoleRoleService.findRoleByPage(jsonPaging, role);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 新增、修改角色
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/input")
	public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		model.put("roleId", roleId);
		MemberConsoleRole role = null;
		if(StringUtils.isNoneBlank(roleId)){
			role = consoleRoleService.findById(roleId);
		}
		model.put("role", role);
		return "console/role/input";
	}

	/**
	 * 配置菜单角色
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/roleMenu")
	public String roleMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		model.put("roleId", roleId);
		return "console/role/role_menu";
	}
	
	/**
	 * 获取角色的权限树
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/menuTree", method = RequestMethod.POST)
	public void getRoleMenuTree(HttpServletRequest request, HttpServletResponse response){
		String roleId=request.getParameter("roleId");
		String langCode=getLoginLangFlag(request);
		Map<String, Object> obj = new HashMap<String, Object>();
		List<MenuTreeVO> list=consoleRoleService.getMenuTree(roleId, langCode);
		obj.put("treeData", list);
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 保存角色
	 * @author Yan
	 * @date 2017-02-14
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> obj = new HashMap<String, Object>();
		//SysAdmin admin = this.getLoginUser(request);
		boolean result = false;
		try {
			String id=request.getParameter("id");
			String name=request.getParameter("name");
			String remark=request.getParameter("remark");
			String relateId=request.getParameter("relateId");
			String type=request.getParameter("type");
			String isValid=request.getParameter("isValid");
			Date date = new Date();
			MemberConsoleRole role;
			if(StringUtils.isNotBlank(id)){
				role = consoleRoleService.findById(id);
				role.setId(id);
			}else{
				role = new MemberConsoleRole();
				role.setId(null);
				role.setCreateTime(date);
			}
			role.setName(name);
			role.setRemark(remark);
			role.setRelateId(relateId);
			role.setType(type);
			role.setIsValid(isValid);
			role.setLastUpdate(date);
			role = consoleRoleService.saveConsoleRole(role);
			if(null!=role){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			
			JsonUtil.toWriter(obj,response);
		}
	}
	
	/**
	 * 保存角色菜单权限
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
	public void saveRoleMenu(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String roleId=request.getParameter("roleId");
			String menuIds=request.getParameter("menuIds");
			String[] menuId=menuIds.split(",");
			result = consoleRoleService.saveRoleMenu(menuId, roleId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			
			JsonUtil.toWriter(obj,response);
		}
	}
	
	/**
	 * 配置角色成员
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/roleUser")
	public String roleUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		model.put("roleId", roleId);
		return "console/role/role_user";
	}
	
	/**
	 * 获取角色中的成员列表
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/roleMember", method = RequestMethod.POST)
	public void findMemberInRole(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberAdmin) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = consoleRoleService.findMemberInRole(jsonPaging,memberAdmin);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 * 删除角色操作
	 * @author Yan
	 * @date 2017-02-14
	 */
	@RequestMapping(value = "/deleteConsoleRole", method = RequestMethod.POST)
	public void deleteConsoleRole(MemberConsoleRoleMember roleMember,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		String roleId = request.getParameter("id");
		if(StringUtils.isNotBlank(roleId)){
			MemberConsoleRole role = this.consoleRoleService.findById(roleId);
			if(null!=role){
				this.consoleRoleService.deleteConsoleRole(role);
				flag = true;
			}
		}
		if(flag){
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}
		obj.put("result", flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 删除角色成员操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/deleteRoleMember", method = RequestMethod.POST)
	public void deleteRoleMember(MemberConsoleRoleMember roleMember,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			consoleRoleService.deleteRoleMember(roleMember);
			obj.put("result",true);
		} catch (Exception e) {
			obj.put("result",false);
		}finally{
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 打开选择成员页面
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/dialogMember")
	public String showMemberDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("id");
		model.put("roleId", roleId);
		return "console/role/dialog_adduser";
	}
	
	/**
	 * 获取不在某角色中的成员列表
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/memberlist", method = RequestMethod.POST)
	public void getUserNotInRole(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberAdmin) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = consoleRoleService.findMemberAdminNotInRole(jsonPaging, memberAdmin);
		toJsonString(response, jsonPaging);
	}
	/**
	 * 保存角色成员
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/saveRoleMember")
	public void saveRoleMember(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String roleId = request.getParameter("roleId");
			String members = request.getParameter("userIds");
			String[] memberIds = members.split(",");
			result = consoleRoleService.saveRoleMember(roleId, memberIds);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
	}

}
