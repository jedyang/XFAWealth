package com.fsll.wmes.role.action;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.role.service.MemberConsoleRoleService;
/**
 * 工作台角色管理
 * @author qgfeng
 * @date 2016-12-8
 */
@Controller
@RequestMapping(value = "/console/role")
public class MemberConsoleRoleAct extends CoreBaseAct{
	
	@Autowired
	private MemberConsoleRoleService consoleRoleService;
	
	/**
	 * 角色列表页面
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//只允许工作台管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null){
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		if(curAdmin.getParent()==null){
			model.put("editable",true);
		}
		return "console/role/list";
	}
	
	/**
	 * 控制台角色分页获得记录
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,MemberConsoleRole role) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin != null){
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = consoleRoleService.findRoleByPage(jsonPaging, role,curAdmin);
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/validOperate", method = RequestMethod.POST)
	public void validOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String ids = request.getParameter("ids");
			String isValid = request.getParameter("isValid");
			String[] idArry = ids.split(",");
			for (String id : idArry) {
				if(StringUtils.isNotBlank(id)){
					MemberConsoleRole role = consoleRoleService.findRoleById(id);
					if(role!=null){
						role.setIsValid(isValid);
						consoleRoleService.saveOrUpdateRole(role);
					}
				}
			}
			obj.put("result",true);
		} catch (Exception e) {
			obj.put("result",false);
			e.printStackTrace();
		}finally{
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 控制台角色新增、修改角色页面
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/input/{roleId}")
	public String input(@PathVariable("roleId")String roleId, ModelMap model) {
		MemberConsoleRole role = consoleRoleService.findRoleById(roleId);
		if(role==null){
			role = new MemberConsoleRole();
		}
		model.put("role", role);
		return "console/role/input";
	}
	
	/**控制台角色表单保存操作
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(MemberConsoleRole role,HttpServletRequest request,HttpServletResponse response) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			MemberConsoleRole loadRole = consoleRoleService.findRoleById(role.getId());
			//新增
			if(loadRole == null){
				loadRole = new MemberConsoleRole();
				loadRole.setCreateBy(getLoginUser(request));
				loadRole.setCreateTime(new Date());
				role.setId(null);
			}
			loadRole.setName(role.getName());
			loadRole.setRemark(role.getRemark());
			loadRole.setLastUpdate(new Date());
			loadRole.setIsValid(role.getIsValid());
			if(StringUtils.isNotBlank(curAdmin.getType())){
				//ifaFrim admin
				if(curAdmin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA ) && curAdmin.getIfafirm()!=null){
					loadRole.setType("1");
					loadRole.setRelateId(curAdmin.getIfafirm().getId());
				}else if(curAdmin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR) && curAdmin.getDistributor()!=null){
					//代理商admin
					loadRole.setType("2");
					loadRole.setRelateId(curAdmin.getDistributor().getId());
					
				}
			}
			//只有admin才能保存
			if(StringUtils.isNotBlank(loadRole.getRelateId())){
				role = consoleRoleService.saveOrUpdateRole(loadRole);
				obj.put("result",true);
				obj.put("id", role.getId());
			}else{
				obj.put("result",false);
				obj.put("msg","noPower");
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("result",false);
			obj.put("msg","error");
			return null;
		}finally{
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 删除角色操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			for (String id : idArry) {
				if(StringUtils.isNotBlank(id)){
					consoleRoleService.deleteRole(id);
				}
			}
			obj.put("result",true);
		} catch (Exception e) {
			obj.put("result",false);
			e.printStackTrace();
		}finally{
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 配置菜单角色
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/roleMenu/{roleId}")
	public String roleMenu(@PathVariable("roleId")String roleId, ModelMap model) {
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
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		String roleId=request.getParameter("roleId");
		String langCode=getLoginLangFlag(request);
		Map<String, Object> obj = new HashMap<String, Object>();
		List<MenuTreeVO> list = null;
		if(curAdmin!=null && curAdmin.getParent()==null){
			list=consoleRoleService.getMenuTree(roleId, curAdmin, langCode);
		}
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
		boolean result = consoleRoleService.saveRoleMenu(menuId, roleId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", result);
		if(result){
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else{
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"Operation failed",null));
		}
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 配置角色成员
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/roleUser/{roleId}")
	public String roleUser(@PathVariable("roleId")String roleId, ModelMap model) {
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
	 * 删除角色成员操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/deleteRoleMember", method = RequestMethod.POST)
	public String deleteRoleMember(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String memberId = request.getParameter("memberId");
		String roleId = request.getParameter("roleId");
		try {
			MemberConsoleRoleMember rm = consoleRoleService.findConsoleRoleMemberById(memberId, roleId);
			if(rm != null){
				consoleRoleService.deleteRoleMember(rm);
			}
			obj.put("result",true);
		} catch (Exception e) {
			obj.put("result",false);
		}
		JsonUtil.toWriter(obj, response);
		return null; 
	}
	
	/**
	 * 打开角色选择成员页面
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/dialogMember")
	public String showMemberDialog(HttpServletRequest request, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("roleId");
		model.put("roleId", roleId);
		return "console/role/dialog_adduser";
	}
	
	/**
	 * 获取不在某角色中的成员列表
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/memberlist", method = RequestMethod.POST)
	public void getUserNotInRole(HttpServletRequest request, HttpServletResponse response,MemberAdminVO vo) {
		MemberSsoVO curMember = getLoginUserSSO(request);
		//ifaFrim admin
		if(curMember.getSubMemberType()!=null && curMember.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFAFIRM){
			vo.setType("1");
			vo.setIfafirmId(curMember.getIfafirmId());
		}
		//代理商admin
		if(curMember.getSubMemberType()!=null && curMember.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_DISTRIBUTOR){
			vo.setType("2");
			vo.setDistributorId(curMember.getDistributorId());
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = consoleRoleService.findMemberAdminNotInRole(jsonPaging,vo);
		toJsonString(response, jsonPaging);
	}
	/**
	 * 保存角色成员
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	@RequestMapping(value = "/saveRoleMember")
	public void saveRoleMember(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String members = request.getParameter("userIds");
		Map<String, Object> obj = new HashMap<String, Object>();
		String[] memberIds = members.split(",");
		boolean result = consoleRoleService.saveRoleMember(roleId, memberIds);
		obj.put("result", result);
		if (result) {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"Operation failed",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
}
