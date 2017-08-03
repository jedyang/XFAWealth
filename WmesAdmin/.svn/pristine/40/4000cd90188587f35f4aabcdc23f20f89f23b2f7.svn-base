package com.fsll.core.action;

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
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysRole;
import com.fsll.core.service.SysRoleService;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.core.vo.SysRoleVO;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
/**
 * 系统台角色控制
 * @author qgfeng
 * @date 2016-11-21
 */
@Controller
@RequestMapping("/console/sysRole")
public class SysRoleAct extends CoreBaseAct{
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		return "console/sys/role/list";
	}
	
	/**
	 * 控制台角色分页获得记录
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,SysRoleVO roleVO) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findAllRole(jsonPaging, roleVO);
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
		SysRole role = sysRoleService.findRoleById(roleId);
		if(role != null){
			SysRoleVO roleVo = new SysRoleVO(role);
			model.put("role", roleVo);
		}
		return "console/sys/role/input";
	}
	
	/**
	 * 更新保存角色
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(SysRole role,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		try {
			SysRole loadRole = null;
			if(StringUtils.isNotBlank(role.getId())){
				loadRole = sysRoleService.findRoleById(role.getId());
			}else{
				SysAdmin admin = (SysAdmin)request.getSession().getAttribute(CoreConstants.USER_LOGIN);
				loadRole = new SysRole();
				loadRole.setCreateTime(new Date());
				loadRole.setId(null);
				loadRole.setCreateBy(admin);
			}
			loadRole.setName(role.getName());
			loadRole.setCode(role.getCode());
			loadRole.setRemark(role.getRemark());
			loadRole.setLastUpdate(new Date());
			loadRole.setIsValid(role.getIsValid());
			loadRole.setType(role.getType());
			role = sysRoleService.saveOrUpdate(loadRole);
			if(role != null){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			map.put("result", result);
			JsonUtil.toWriter(map, response);
		}
		
		
	}
	
	/**
	 * 删除角色操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		boolean result = sysRoleService.deleteRole(roleId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",result);
		JsonUtil.toWriter(obj, response);
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
		return "console/sys/role/role_menu";
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
		List<MenuTreeVO> list=sysRoleService.getMenuTree(roleId, langCode);
		obj.put("treeData", list);
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 保存角色菜单权限
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
	public void saveRoleMenu(HttpServletRequest request, HttpServletResponse response){
		String roleId=request.getParameter("roleId");
		String menuIds=request.getParameter("menuIds");
		String[] menuId=menuIds.split(",");
		boolean result = sysRoleService.saveRoleMenu(menuId, roleId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", result);
		if(result){
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"Operation failed",null));
		}
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 配置角色用户
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/roleUser")
	public String roleUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		model.put("roleId", roleId);
		return "console/sys/role/role_user";
	}

	/**
	 * 获取角色中的用户列表
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/roleMember", method = RequestMethod.POST)
	public void getUserInRole(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String keyWord = request.getParameter("keyword");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findUserInRole(jsonPaging,roleId,keyWord);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 * 删除角色用户操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/deleteRoleMember", method = RequestMethod.POST)
	public void deleteRoleMember(MemberConsoleRoleMember roleMember,HttpServletRequest request,HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String adminId = request.getParameter("adminId");
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			boolean result = sysRoleService.deleteRoleUser(roleId, adminId);
			obj.put("result",result);
		} catch (Exception e) {
			obj.put("result",false);
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 打开选择用户页面
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/dialogMember")
	public String showMemberDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("id");
		model.put("roleId", roleId);
		return "console/sys/role/dialog_adduser";
	}
	
	/**
	 * 获取不在某角色中的用户列表
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/memberlist", method = RequestMethod.POST)
	public void getUserNotInRole(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String keyWord = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findUserNotInRole(jsonPaging, roleId, keyWord);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 * 保存角色用户
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/saveRoleMember")
	public void saveRoleMember(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String userIds = request.getParameter("userIds");
		Map<String, Object> obj = new HashMap<String, Object>();
		String[] adminIds = userIds.split(",");
		boolean result = sysRoleService.saveRoleMember(roleId, adminIds);
		obj.put("result", result);
		if (result) {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"Operation failed",null));
		}
		JsonUtil.toWriter(obj, response);
	}

}
