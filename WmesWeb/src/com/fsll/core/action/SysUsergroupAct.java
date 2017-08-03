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

import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysRole;
import com.fsll.core.entity.SysUsergroup;
import com.fsll.core.entity.SysUsergroupAdmin;
import com.fsll.core.service.SysUserGroupService;
import com.fsll.core.vo.SysUserGroupVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;

/*****
 * 用户组管理
 * 
 * @author mqzou 2016-07-19
 */
@Controller
public class SysUsergroupAct extends CoreBaseAct {

	@Autowired
	private SysUserGroupService sysUserGroupService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private DistributorService distributorService;

	/**
	 * 用户组分页列表
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		MemberSsoVO vo = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		model.put("curMember", vo);
		return "console/sys/usergroup/list";
	}

	/**
	 * 数据查询的方法
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String keyword = request.getParameter("keyword");
		String typeString=request.getParameter("type");
		SysUsergroup sysUsergroup = new SysUsergroup();
		MemberSsoVO vo = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		MemberAdmin memberAdmin=memberManageService.findAdminByMemberId(vo.getId());
		if (null != keyword && !"".equals(keyword)) {
			try {
				keyword = URLDecoder.decode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(null!=memberAdmin){
			String type=memberAdmin.getType();
			
		    if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type)) {
		    	//sysUsergroup.setType(type);
				//sysUsergroup.setRelateId(null!=memberAdmin.getIfafirm()?memberAdmin.getIfafirm().getId():"");
			}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type)) {
				//sysUsergroup.setType(type);
				//sysUsergroup.setRelateId(null!=memberAdmin.getDistributor()?memberAdmin.getDistributor().getId():"");
			}
		}
		if(null!=typeString && !"".equals(typeString)){
			//sysUsergroup.setType(typeString);
		}
		
		sysUsergroup.setName(keyword);
		jsonPaging = this.getJsonPaging(request);
		//MemberBase memberBase=memberBaseService.findById(vo.getId());
		//sysUsergroup.setCreateBy(memberBase);
		if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(memberAdmin.getType()))
		jsonPaging = sysUserGroupService.findAllUserGroup(jsonPaging, sysUsergroup);
		else {
			jsonPaging=sysUserGroupService.findAllUserGroupPro(jsonPaging, sysUsergroup);
		}
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * 新增用户组
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/add")
	public String roleAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// this.isMobileDevice(request,model);
		SysRole vo = new SysRole();
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("curMember", curMember);
		model.put("rolevo", vo);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/sys/usergroup/input";
	}

	/**
	 * 用户组明细(修改)
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/detail")
	public String roleDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		SysUsergroup vo = sysUserGroupService.findSysUsergroupById(id);
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		List list = memberManageService.getIfafirmlist();
		List distributorList = distributorService.findAllDistributor();
		model.put("curMember", curMember);
		model.put("usergrouvo", vo);
		model.put("ifafirmlist", list);
		model.put("distributorList", distributorList);
		return "console/sys/usergroup/input";

	}

	/**
	 * 保存用户组信息
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response, ModelMap model, SysUserGroupVO sysGroupVO) {
		SysUsergroup sysUsergroup = null;
		MemberBase memberBase = null;
		String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		sysGroupVO.setId(memberDataJSON.containsKey("id")?memberDataJSON.getString("id"):"");
		sysGroupVO.setDistributorId(memberDataJSON.containsKey("distributorId")? memberDataJSON.getString("distributorId"):"");
		sysGroupVO.setIfafirmId(memberDataJSON.containsKey("ifafirmId")?memberDataJSON.getString("ifafirmId"):"");
		sysGroupVO.setName(memberDataJSON.getString("name"));
		sysGroupVO.setType(memberDataJSON.containsKey("type")?memberDataJSON.getString("type"):"");
		sysGroupVO.setRemark(memberDataJSON.getString("remark"));
		
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if (null != sysGroupVO.getId() && !"".equals(sysGroupVO.getId())) {
			sysUsergroup = sysUserGroupService.findSysUsergroupById(sysGroupVO.getId());
			if (null == sysUsergroup.getCreateBy()) {
				
				memberBase = memberBaseService.findById(curMember.getId());
				//sysUsergroup.setCreateBy(memberBase);
				sysUsergroup.setCreateTime(new Date());
			} else {
				//memberBase = sysUsergroup.getCreateBy();
			}
			
			if (curMember.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM) {// 如果当前用户是系统用户组
				//sysUsergroup.setType(sysGroupVO.getType());
				
				if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(sysGroupVO.getType())){
					//sysUsergroup.setRelateId("0");
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(sysGroupVO.getType())) {
					//sysUsergroup.setRelateId(sysGroupVO.getIfafirmId());
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(sysGroupVO.getType())) {
					//sysUsergroup.setRelateId(sysGroupVO.getDistributorId());
				}
				
			}

		} else {
			sysUsergroup = new SysUsergroup();
			sysUsergroup.setCreateTime(new Date());
			//MemberSsoVO vo = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			memberBase = memberBaseService.findById(curMember.getId());
			//sysUsergroup.setCreateBy(memberBase);
			
			if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM) {// 系统用户组
				//sysUsergroup.setType(sysGroupVO.getType());
				
				if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(sysGroupVO.getType())){
					//sysUsergroup.setRelateId("0");
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(sysGroupVO.getType())) {
					//sysUsergroup.setRelateId(sysGroupVO.getIfafirmId());
				}else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(sysGroupVO.getType())) {
					//sysUsergroup.setRelateId(sysGroupVO.getDistributorId());
				}
				
			} else if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA) {// ifafirm用户组
				//sysUsergroup.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA);
				//MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(memberBase.getId());
				//sysUsergroup.setRelateId(null != memberAdmin.getIfafirm() ? memberAdmin.getIfafirm().getId() : "");
			} else if (memberBase.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR) {// distributor用户组
				//sysUsergroup.setType(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR);
				//MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(memberBase.getId());
				//sysUsergroup.setRelateId(null != memberAdmin.getDistributor() ? memberAdmin.getDistributor().getId() : "");
			}

		}
		
		sysUsergroup.setName(sysGroupVO.getName());
		sysUsergroup.setLastUpdate(new Date());
		sysUsergroup.setRemark(sysGroupVO.getRemark());
		sysUsergroup.setIsValid("1");
		sysUsergroup = sysUserGroupService.saveUsergroup(sysUsergroup);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != sysUsergroup) {
			obj.put("result", true);
			obj.put("message", "success");
			obj.put("id", sysUsergroup.getId());
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除一条用户组
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/delete", method = RequestMethod.POST)
	public void delelte(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		SysUsergroup usergroup = sysUserGroupService.findSysUsergroupById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != usergroup) {
			usergroup.setIsValid("0");
			if (sysUserGroupService.deleteUserGroup(usergroup)) {
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
	 * 获取用户组中的用户列表
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/usergroup/groupMember", method = RequestMethod.POST)
	public void getUserInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		if(null==id || "".equals(id)){
			toJsonString(response, jsonPaging);
			return;
		}
		
		SysUsergroup usergroup = sysUserGroupService.findSysUsergroupById(id);
		if (null != usergroup)
			usergroup.setName(keyWork);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysUserGroupService.findUserInGroup(jsonPaging, usergroup);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 获取不在某用户组中的用户列表
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/console/sys/usergroup/memberlist", method = RequestMethod.POST)
	public void getUserNotInRole(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String keyWork = request.getParameter("keyword");
		SysUsergroup role = sysUserGroupService.findSysUsergroupById(id);
		if (null != role)
			role.setName(keyWork);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysUserGroupService.findUserNotInGroup(jsonPaging, role);
		toJsonString(response, jsonPaging);

	}

	/**
	 * 用户分页列表
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/dialogMember")
	public String showMemberDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String roleId = request.getParameter("id");
		model.put("roleId", roleId);
		return "console/sys/usergroup/dialog_adduser";
	}

	/**
	 * 保存用户组用户
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/saveUser")
	public void saveRoleMember(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String members = request.getParameter("userIds");
		SysUsergroup usergroup = sysUserGroupService.findSysUsergroupById(id);
		List list = new ArrayList();
		String[] users = members.split(",");
		for (int i = 0; i < users.length; i++) {
			SysUsergroupAdmin sysUsergroupMember = new SysUsergroupAdmin();
			//MemberBase memberBase = memberBaseService.findById(users[i]);
			//sysUsergroupMember.setMember(memberBase);
			sysUsergroupMember.setGroup(usergroup);
			list.add(sysUsergroupMember);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysUserGroupService.saveGroupMember(list)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 删除用户组用户关系
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/deleteUser", method = RequestMethod.POST)
	public void deteleUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String memberId = request.getParameter("memberId");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysUserGroupService.deleteGroupMember(id, memberId)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 校验用户组名称的唯一性
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 */
	@RequestMapping(value = "/console/sys/usergroup/checkName", method = RequestMethod.POST)
	public void checkCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		Map<String, Object> obj = new HashMap<String, Object>();
		if (sysUserGroupService.checkGroupName(id, name)) {
			obj.put("result", true);
			obj.put("message", "success");
		} else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj, response);
	}

}
