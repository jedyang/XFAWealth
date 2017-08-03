package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.entity.SysRole;
import com.fsll.core.entity.SysRoleAdmin;
import com.fsll.core.entity.SysRoleMenu;
import com.fsll.core.entity.SysRoleUsergroup;
import com.fsll.core.entity.SysUsergroup;
import com.fsll.core.service.SysMenuService;
import com.fsll.core.service.SysRoleService;
import com.fsll.core.service.SysUserGroupService;
import com.fsll.core.vo.SysRoleVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;

/*****
 * 角色管理
 * 
 * @author mqzou 2016-07-11
 */
@Controller
public class SysRoleAct extends CoreBaseAct {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private SysUserGroupService sysUserGroupService;
	@Autowired
	private DistributorService distributorService;
	
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 角色管理分页列表
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		model.put("curMember", curMember);
		return "console/sys/role/list";
	}

	/**
	 * 新增角色
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/add")
	public String roleAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		SysRole vo = new SysRole();
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("rolevo", vo);
		model.put("curMember", curMember);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/sys/role/input";
	}

	/**
	 * 角色明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/detail")
	public String roleDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		SysRole vo = sysRoleService.findRoleById(id);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("rolevo", vo);
		model.put("curMember", curMember);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/sys/role/input";

	}

	/**
	 * 数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/listJson", method = RequestMethod.POST)
	public void listJsonI(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String keyword = request.getParameter("name");
		String code=request.getParameter("code");
		String typeString=request.getParameter("type");
		SysRole sysRole = new SysRole();
		MemberSsoVO vo = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(vo.getId());
		if (null != keyword && !"".equals(keyword)) {
			try {
				keyword = URLDecoder.decode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (null != code && !"".equals(code)) {
			try {
				code = URLDecoder.decode(code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (null != memberAdmin) {
			String type = memberAdmin.getType();
			
			 if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type)) {
				sysRole.setType(type);
				//sysRole.setRelateId(null != memberAdmin.getIfafirm() ? memberAdmin.getIfafirm().getId() : "");
			} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type)) {
				sysRole.setType(type);
				//sysRole.setRelateId(null != memberAdmin.getDistributor() ? memberAdmin.getDistributor().getId() : "");
			}

		}
		if(null!=typeString && !"".equals(typeString)){
			sysRole.setType(typeString);
		}
		sysRole.setName(keyword);
		sysRole.setCode(code);
		//MemberBase memberBase = memberBaseService.findById(vo.getId());
		//sysRole.setCreateBy(memberBase);
		jsonPaging = this.getJsonPaging(request);

		if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(memberAdmin.getType()))
			jsonPaging = sysRoleService.findAllRole(jsonPaging, sysRole);
		else {
			jsonPaging = sysRoleService.findAllRolePro(jsonPaging, sysRole);
		}
		
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * 保存角色信息
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response, ModelMap model, SysRoleVO sysRoleVO) {
		SysRole sysRole = null;
		MemberBase memberBase = null;
		String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		
		sysRoleVO.setId(memberDataJSON.containsKey("id")?memberDataJSON.getString("id"):"");
		sysRoleVO.setCode(memberDataJSON.getString("code"));
		sysRoleVO.setDistributorId(memberDataJSON.containsKey("distributorId")?memberDataJSON.getString("distributorId"):"");
		sysRoleVO.setIfafirmId(memberDataJSON.containsKey("ifafirmId")?memberDataJSON.getString("ifafirmId"):"");
		sysRoleVO.setName(memberDataJSON.getString("name"));
		sysRoleVO.setRemark(memberDataJSON.getString("remark"));
		sysRoleVO.setType(memberDataJSON.containsKey("type")?memberDataJSON.getString("type"):"");
		
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if (null != sysRoleVO.getId() && !"".equals(sysRoleVO.getId())) {
			sysRole = sysRoleService.findRoleById(sysRoleVO.getId());
			if (null == sysRole.getCreateBy()) {
				
				memberBase = memberBaseService.findById(curMember.getId());
				//sysRole.setCreateBy(memberBase);
				sysRole.setCreateTime(new Date());
			}else {
				//memberBase=sysRole.getCreateBy();
			}
			
			if (curMember.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM) {// 如果当前用户是系统角色
				sysRole.setType(sysRoleVO.getType());

				if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(sysRoleVO.getType())){
					//sysRole.setRelateId("0");
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(sysRoleVO.getType())) {
					//sysRole.setRelateId(sysRoleVO.getIfafirmId());
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(sysRoleVO.getType())) {
					//sysRole.setRelateId(sysRoleVO.getDistributorId());
				}
				
			}

		} else {
			sysRole = new SysRole();
			sysRole.setCreateTime(new Date());
			//MemberSsoVO vo = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			memberBase = memberBaseService.findById(curMember.getId());
			//sysRole.setCreateBy(memberBase);
			
			if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM) {// 系统角色
				sysRole.setType(sysRoleVO.getType());

				if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(sysRoleVO.getType())){
					//sysRole.setRelateId("0");
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(sysRoleVO.getType())) {
					//sysRole.setRelateId(sysRoleVO.getIfafirmId());
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(sysRoleVO.getType())) {
					//sysRole.setRelateId(sysRoleVO.getDistributorId());
				}
				
			} else if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA) {// ifafirm角色
				sysRole.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA);
				//MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(memberBase.getId());
				//sysRole.setRelateId(null != memberAdmin.getIfafirm() ? memberAdmin.getIfafirm().getId() : "");
			} else if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR) {// distributor角色
				sysRole.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR);
				//MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(memberBase.getId());
				//sysRole.setRelateId(null != memberAdmin.getDistributor() ? memberAdmin.getDistributor().getId() : "");
			}
		}
		

		sysRole.setCode(sysRoleVO.getCode());
		sysRole.setName(sysRoleVO.getName());
		sysRole.setLastUpdate(new Date());
		sysRole.setRemark(sysRoleVO.getRemark());
		sysRole.setIsValid("1");
		sysRole = sysRoleService.saveRole(sysRole);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != sysRole) {
			obj.put("result", true);
			obj.put("message", "success");
			obj.put("id", sysRole.getId());
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除一条角色
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/delete", method = RequestMethod.POST)
	public void delelte(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		SysRole role = sysRoleService.findRoleById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != role) {
			role.setIsValid("0");
			role = sysRoleService.deleteRole(role);
			if (null != role) {
				obj.put("result", true);
				obj.put("message", "success");
			} else {
				obj.put("result", false);
				obj.put("message", "failure");
			}

		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);

	}

	/**
	 * 获取角色中的用户列表
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/role/roleMember", method = RequestMethod.POST)
	public void getUserInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		SysRole role = sysRoleService.findRoleById(roleId);
		if (null != role)
			role.setName(keyWork);
		else {
			jsonPaging=new JsonPaging();
			toJsonString(response, jsonPaging);
			return;
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findUserInRole(jsonPaging, role);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 获取不在某角色中的用户列表
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/role/memberlist", method = RequestMethod.POST)
	public void getUserNotInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		SysRole role = sysRoleService.findRoleById(roleId);
		if (null != role)
			role.setName(keyWork);
		else {
			jsonPaging=new JsonPaging();
			toJsonString(response, jsonPaging);
			return;
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findUserNotInRole(jsonPaging, role);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 打开选择用户页面
	 * 
	 * @author mqzou
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/sys/role/dialogMember")
	public String showMemberDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("id");
		model.put("roleId", roleId);
		return "console/sys/role/dialog_adduser";
	}

	/**
	 * 打开选择用户组页面
	 * 
	 * @author mqzou
	 * @date 2016-07-21
	 */
	@RequestMapping(value = "/console/sys/role/dialogGroup")
	public String showGroupDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("id");
		model.put("roleId", roleId);
		return "console/sys/role/dialog_addgroup";
	}

	/**
	 * 保存角色用户
	 * 
	 * @author mqzou
	 * @date 2016-07-12
	 */
	@RequestMapping(value = "/console/sys/role/saveUser")
	public void saveRoleMember(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		String members = request.getParameter("userIds");
		SysRole sysRole = sysRoleService.findRoleById(roleId);
		List list = new ArrayList();
		String[] users = members.split(",");
		for (int i = 0; i < users.length; i++) {
			SysRoleAdmin sysRoleMember = new SysRoleAdmin();
		//	MemberBase memberBase = memberBaseService.findById(users[i]);
			//sysRoleMember.setMember(memberBase);
			sysRoleMember.setRole(sysRole);
			list.add(sysRoleMember);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysRoleService.saveRoleMember(list)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除角色用户关系
	 * 
	 * @author mqzou
	 * @date 2016-07-12
	 */
	@RequestMapping(value = "/console/sys/role/deleteUser", method = RequestMethod.POST)
	public void deteleUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		String memberId = request.getParameter("memberId");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysRoleService.deleteRoleUser(roleId, memberId)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 校验角色编码的唯一性
	 * 
	 * @author mqzou
	 * @date 2016-07-12
	 */
	@RequestMapping(value = "/console/sys/role/checkCode", method = RequestMethod.POST)
	public void checkCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		String code = request.getParameter("code");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysRoleService.checkRoleCode(roleId, code)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 获取角色中的用户组列表
	 * 
	 * @author mqzou
	 * @date 2016-07-21
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/role/roleGroup", method = RequestMethod.POST)
	public void getGroupInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		SysRole role = sysRoleService.findRoleById(roleId);
		if (null != role)
			role.setName(keyWork);
		else {
			jsonPaging=new JsonPaging();
			toJsonString(response, jsonPaging);
			return;
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findGroupInRole(jsonPaging, role);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 获取不在某角色中的用户组列表
	 * 
	 * @author mqzou
	 * @date 2016-07-21
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/role/grouplist", method = RequestMethod.POST)
	public void getGroupNotInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		SysRole role = sysRoleService.findRoleById(roleId);
		if (null != role)
			role.setName(keyWork);
		else {
			jsonPaging=new JsonPaging();
			toJsonString(response, jsonPaging);
			return;
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysRoleService.findGroupNotInRole(jsonPaging, role);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 保存角色用户组
	 * 
	 * @author mqzou
	 * @date 2016-07-21
	 */
	@RequestMapping(value = "/console/sys/role/saveGroup")
	public void saveRoleGroup(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		String members = request.getParameter("groupIds");
		SysRole sysRole = sysRoleService.findRoleById(roleId);
		List list = new ArrayList();
		String[] groups = members.split(",");
		for (int i = 0; i < groups.length; i++) {
			SysRoleUsergroup sysRoleUsergroup = new SysRoleUsergroup();
			SysUsergroup sysUsergroup = sysUserGroupService.findSysUsergroupById(groups[i]);
			sysRoleUsergroup.setGroup(sysUsergroup);
			sysRoleUsergroup.setRole(sysRole);
			list.add(sysRoleUsergroup);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysRoleService.saveGroup(list)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除角色用户关系
	 * 
	 * @author mqzou
	 * @date 2016-07-12
	 */
	@RequestMapping(value = "/console/sys/role/deleteGroup", method = RequestMethod.POST)
	public void deteleGroup(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		String groupId = request.getParameter("groupId");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysRoleService.deleteGroup(roleId, groupId)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 获取角色的权限树
	 * 
	 * @author mqzou
	 * @date 2016-07-28
	 */
	@RequestMapping(value = "/console/sys/role/menuTree", method = RequestMethod.POST)
	public void getRoleMenuTree(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String roleId=request.getParameter("roleId");
		String langCode=getLoginLangFlag(request);
		SysRole sysRole=sysRoleService.findRoleById(roleId);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(null!=sysRole){
			String memberId=null!=sysRole.getCreateBy()?sysRole.getCreateBy().getId():"";
			String parentRoleId=sysRoleService.getMemberRoleId(memberId);
			List list=sysRoleService.getMenuTree(roleId, parentRoleId, langCode);

			obj.put("treeData", list);
			
			
		}
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 保存角色菜单权限
	 * 
	 * @author mqzou
	 * @date 2016-07-28
	 */
	@RequestMapping(value = "/console/sys/role/saveMenu", method = RequestMethod.POST)
	public void saveRoleMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String roleId=request.getParameter("roleId");
		String menuIds=request.getParameter("menuIds");
		List list=new ArrayList();
		SysRole sysRole=sysRoleService.findRoleById(roleId);
		String[] menuId=menuIds.split(",");
		if(null!=menuId ){
			for (String id : menuId) {
				SysMenu sysMenu=new SysMenu();
				sysMenu.setId(id);
				sysMenu=sysMenuService.findMenuById(sysMenu);
				SysRoleMenu sysRoleMenu=new SysRoleMenu();
				sysRoleMenu.setMenu(sysMenu);
				sysRoleMenu.setRole(sysRole);
				list.add(sysRoleMenu);
			
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		if(sysRoleService.saveRoleMenu(list, roleId)){
			obj.put("result", true);
			obj.put("message", "success");
		}else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj,response);
		
	}
	
	/**
	 * 获取自动填充查询条件列表
	 * 
	 * @author mqzou
	 * @date 2016-08-09
	 */
	@RequestMapping(value = "/console/sys/role/autoList", method = RequestMethod.POST)
	public void autoComplete(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String type=request.getParameter("type");
		String keyWord=request.getParameter("keyWord");
		String field="";
		if("name".equals(type)){
			field="name";
		}else if ("code".equals(type)) {
			field="code";
		}
		List list=sysRoleService.findAutoNameList(field, keyWord);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		JsonUtil.toWriter(obj,response);
	}
}
