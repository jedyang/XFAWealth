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
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysAdminService;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberRole;
import com.fsll.wmes.role.service.MemberRoleService;
import com.fsll.wmes.role.vo.MemberRoleVO;

/**
 * 前台角色管理
 * @author qgfeng
 * @date 2016-11-21
 */
@Controller
@RequestMapping("/console/memberRole")
public class MemberRoleAct  extends CoreBaseAct{
	
	@Autowired
	private MemberRoleService memberRoleService;
	@Autowired
	private SysAdminService sysAdminService;
	
	/**
	 * 前台角色管理列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "console/role/memberRole/list";
	}
	
	/**
	 * 前台角色分页获得记录
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,MemberRoleVO role) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = memberRoleService.findRoleByPage(jsonPaging, role);
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
		MemberRole role = memberRoleService.findById(roleId);
		if(role != null){
			MemberRoleVO roleVo = new MemberRoleVO(role);
			SysAdmin admin = sysAdminService.findById(roleVo.getCreateBy());
			roleVo.setCreateNickName(admin==null?"":admin.getNickName());
			model.put("role", roleVo);
		}
		return "console/role/memberRole/input";
	}
	
	/**
	 * 保存角色
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(MemberRole role,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberRole loadRole = memberRoleService.findById(role.getId());;
			if(loadRole == null){
				loadRole = new MemberRole();
				loadRole.setId(null);
				loadRole.setCreateTime(new Date());
				SysAdmin admin = (SysAdmin)request.getSession().getAttribute(CoreConstants.USER_LOGIN);
				loadRole.setCreateBy(admin.getId());
			}
			loadRole.setName(role.getName());
			loadRole.setCode(role.getCode());
			loadRole.setLastUpdate(new Date());
			role = memberRoleService.saveOrUpdate(loadRole);
			if(role != null){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
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
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String[] idArry = ids.split(",");
			for (String id : idArry) {
				if(StringUtils.isNotBlank(id)){
					memberRoleService.deleteRole(id);
				}
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
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
		return "console/role/memberRole/role_menu";
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
		List<MenuTreeVO> list=memberRoleService.getMenuTree(roleId, langCode);
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
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String roleId=request.getParameter("roleId");
			String menuIds=request.getParameter("menuIds");
			String[] menuId=menuIds.split(",");
			result = memberRoleService.saveRoleMenu(menuId, roleId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj,response);
		}
	}
}

