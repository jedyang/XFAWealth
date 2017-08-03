package com.fsll.wmes.ifafirm.action.console;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.service.MyFirmService;
import com.fsll.wmes.ifafirm.vo.MemberIfaIfafirmVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * 控制器:我的公司管理
 * @author wwluo
 * @version 1.0
 * @date 2016-08-30
 */
@Controller
@RequestMapping(value = "/console/myfirm")
public class ConsoleMyfirmAct extends WmesBaseAct{

	@Autowired
	private MyFirmService myFirmService;
	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private AccessoryFileService accessoryFileService;
	@Autowired
	private MemberBaseService baseService;
	
	
	/**
	 * 跳转我的公司管理页面
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/myfirm", method = RequestMethod.GET)
	public String myFirmManager(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifaFirmId = request.getParameter("ifaFirmId");
		//权限控制
		MemberSsoVO curMember = getLoginUserSSO(request); // (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
			MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
			if(null != memberAdmin && null != memberAdmin.getIfafirm())
				ifaFirmId = memberAdmin.getIfafirm().getId();
		}
		MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
        if(StringUtils.isNoneEmpty(ifaFirmId)){
        	MemberIfafirm ifafirm = ifafirmService.findById(ifaFirmId);
        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifaFirmId);
        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifaFirmId);
        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifaFirmId);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	if(null != ifafirmSc){
        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
        	}
        	if(null != ifafirmTc){
        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
        	}
        	if(null != ifafirmEn){
        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
        	}
        }
        model.put("ifafirm", memberIfafirmVO);
		
		return "console/ifafirm/myfirm/myfirm";
	}
	
	/**
	 * ifafirm删除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/deleteIfa", method = RequestMethod.POST)
	public void deleteIfa(HttpServletRequest request,HttpServletResponse response,Model model) {
		String ifaId = request.getParameter("ifaId");
		boolean flag = false;
		flag = myFirmService.deleteIfa(ifaId);
		Map map = new HashMap();
		map.put("flag", flag);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 跳转选择ifa弹窗
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/selectIfadialog", method = RequestMethod.GET)
	public String selectIfadialog(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return "console/ifafirm/myfirm/selectIfaDialog";
	}
	
	/**
	 * ifa管理员设置（设置&移除）
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/setIfaAdmin", method = RequestMethod.POST)
	public void setIfaAdmin(HttpServletRequest request,HttpServletResponse response,Model model) {
		String ifaId = request.getParameter("ifaId");
		String isAdmin = request.getParameter("isAdmin");
		boolean flag = false;
		flag = myFirmService.saveIfaAdmin(ifaId,isAdmin);
		Map map = new HashMap();
		map.put("flag", flag);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 跳转选择 distributor 弹窗
	 * @author wwluo
	 * @date 2016-09-05 
	 */
	@RequestMapping(value = "/selectDistributordialog", method = RequestMethod.GET)
	public String selectDistributordialog(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return "console/ifafirm/myfirm/selectDistributordialog";
	}
	
	/**
	 * 获取代理商    distributor
	 * @author wwluo
	 * @date 2016-09-05 
	 */
	@RequestMapping(value = "/distributorJson", method = RequestMethod.POST)
	public void getDistributorJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String companyName = request.getParameter("companyName");
		String entityType = request.getParameter("entityType");
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = myFirmService.getDistributorJson(jsonPaging,langCode,companyName,entityType);
		this.toJsonString(response, jsonPaging);
	}
	
	
	/**
	 * 加载公司logo
	 * @author wwluo
	 * @date 2016-09-05 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/loadLogo", method = RequestMethod.POST)
	public void loadLogo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
		//权限控制
		if(StringUtils.isBlank(ifafirmId)){
			MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
				MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
				if(null != memberAdmin && null != memberAdmin.getIfafirm())
					ifafirmId = memberAdmin.getIfafirm().getId();
			}
		}
		AccessoryFile accessoryFile = new AccessoryFile();
		if(StringUtils.isNotBlank(ifafirmId)){
			MemberIfafirm memberifafirm = myFirmService.getIfafirmById(ifafirmId);
			if(null != memberifafirm){
				String accessoryId = memberifafirm.getFirmLogo();
				if(StringUtils.isNotBlank(accessoryId))
					accessoryFile = accessoryFileService.findAccessoryFileById(accessoryId);
			}
		}
		Map map = new HashMap();
		map.put("accessoryFile", accessoryFile);
		JsonUtil.toWriter(map, response);
	}
	
	//-------- firmAdmin --------
	
	/**
	 * firm member分页列表
	 * @author qgfeng
	 * @date 2016-11-9
	 * @return
	 */
	@RequestMapping(value = "/member/list")
	public String member(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//只允许ifaFirm超级管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null || curAdmin.getIfafirm()==null){
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
		return "console/ifafirm/myfirm/member_list";
	}

	/**
	 * firm member数据查询的方法
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	@RequestMapping(value = "/member/listJson", method = RequestMethod.POST)
	public void memberListJson(HttpServletRequest request, HttpServletResponse response,MemberAdminVO adminVo) {
		jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		adminVo.setIfafirmId(curAdmin.getIfafirm().getId());
		jsonPaging = myFirmService.findFirmMember(jsonPaging, adminVo);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * firm member 启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/member/validOperate", method = RequestMethod.POST)
	public void memberValidOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String isValid = request.getParameter("isValid");
			String[] idArry = ids.split(",");
			if(idArry!=null && idArry.length>0){
				for (String id : idArry) {
					MemberBase base = myFirmService.getFirmMemberById(id);
					if(base!=null){
						base.setIsValid(isValid);
						baseService.saveOrUpdate(base);
					}
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
	 * 新增修改firm member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/input/{id}")
	public String memberInput(@PathVariable("id")String id, ModelMap model) {
		if(StringUtils.isNotBlank(id)){
			MemberBase memberBase = myFirmService.getFirmMemberById(id);
			model.put("memberBase",memberBase);
		}
		return "console/ifafirm/myfirm/member_input";
	}
	
	/**
	 * 保存Firm Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/save", method = RequestMethod.POST)
	public String saveMember(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberVo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			//检查是否管理员
			if (curAdmin==null || curAdmin.getParent()!=null){
				obj.put("msg", "非管理员账号");
				throw new Exception("非管理员账号");
			}
			MemberBase base = null;
			if(StringUtils.isBlank(memberVo.getMemberId())){
				base = new MemberBase();
				base.setIsValid("1");
				base.setStatus("1");
				base.setCreateTime(new Date());
				base.setPassword(pwdEncoder.encodePassword(memberVo.getPassword()));
				base.setMemberType(2);
				base.setSubMemberType(22);
			}else{
				base = baseService.findById(memberVo.getMemberId());
				//修改了密码
				if(StringUtils.isNotBlank(memberVo.getRepassword())){
					base.setPassword(pwdEncoder.encodePassword(memberVo.getRepassword()));
				}
			}
			base.setLoginCode(memberVo.getLoginCode());
			base.setEmail(memberVo.getEmail());
			base.setMobileNumber(memberVo.getMobileNumber());
			base.setNickName(memberVo.getNickName());
			base.setLastUpdate(new Date());
			MemberAdmin firmMember = myFirmService.getFirmMemberByMid(curAdmin.getIfafirm().getId(),memberVo.getMemberId());
			if(firmMember == null){
				firmMember = new MemberAdmin();
				firmMember.setIfafirm(curAdmin.getIfafirm());
				firmMember.setMember(base);
				firmMember.setType("1");
				firmMember.setParent(curAdmin);
			}
			firmMember.setMember(base);
			firmMember.setIfafirm(curAdmin.getIfafirm());
			firmMember = myFirmService.saveOrUpdateFirmMember(firmMember);
			if(firmMember != null){
				result = true;
			}
			//保存用户和运营公司的关系
			if (firmMember != null && firmMember.getMember() != null && !firmMember.getMember().getId().isEmpty()){
				String companyId = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_ID));
				CompanyInfo company = new CompanyInfo();
				company.setId(companyId);

				MemberCompany memberCompany = new MemberCompany();
				memberCompany.setCompany(company);
				memberCompany.setMember(firmMember.getMember());
				baseService.saveMemberCompany(memberCompany);
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("msg", e.getMessage());
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}
	
	/**
	 * 删除Firm Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/delete", method = RequestMethod.POST)
	public String deleteMember(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			result = myFirmService.deleteFirmMemberByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}
	
	/**
	 * 跳转我的公司管理页面
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/teamManager", method = RequestMethod.GET)
	public String teamManager(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			return "console/ifafirm/myfirm/myfirm_list";
		}else{
			return "redirect:" + this.getFullPath(request) + "front/index.do";
		}
	}
	
	/**
	 * 获取该公司所有ifa
	 * @author wwluo
	 * @date 2016-09-02 
	 */
	@RequestMapping(value = "/ifalist", method = RequestMethod.POST)
	public void getIfaInIfaFirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		jsonPaging = this.getJsonPaging(request);
	    MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			String ifafirmId = memberAdmin.getIfafirm().getId();
			String isAdminAccount = request.getParameter("isAdminAccount");
			String langCode = this.getLoginLangFlag(request);
			if(StringUtils.isNotBlank(ifafirmId)){
			    jsonPaging = myFirmService.findIfaByIfaFirm(jsonPaging, ifafirmId, langCode, isAdminAccount);
			}
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取该公司下的组织架构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/getOrg", method = RequestMethod.POST)
	public void getFirmOrgData(HttpServletRequest request,HttpServletResponse response,Model model) {
	    MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
	    List<IfafirmTeam> teams = null;
	    if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			teams = myFirmService.getOrg(memberAdmin.getIfafirm().getId());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("teams", teams);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取该公司下团队成员
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/getIfafirmTeamIfa", method = RequestMethod.POST)
	public void getIfafirmTeamIfa(HttpServletRequest request,HttpServletResponse response,Model model) {
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		jsonPaging = this.getJsonPaging(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			String teamId = request.getParameter("teamId");
			String keyWord = request.getParameter("keyWord");
			jsonPaging = myFirmService.getIfafirmTeamIfa(memberAdmin.getIfafirm().getId(), teamId, keyWord, jsonPaging);
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 增加、修改团队
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/addOrg", method = RequestMethod.POST)
	public void addOrg(HttpServletRequest request,HttpServletResponse response,Model model) {
		String teamId = request.getParameter("teamId");
		String parentId = request.getParameter("parentId");
		String name = toUTF8String(request.getParameter("name"));
		String code = toUTF8String(request.getParameter("code"));
		String orderByStr = toUTF8String(request.getParameter("orderBy"));
		String remark = toUTF8String(request.getParameter("remark"));
		Integer orderBy = null;
		if (StringUtils.isNotBlank(orderByStr)) {
			orderBy = Integer.valueOf(orderByStr);
		}
		IfafirmTeam team = null;
		Boolean flag = false;
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			if (StringUtils.isNotBlank(teamId)) {
				team = ifafirmService.getIfafirmTeam(teamId);
			}
			if(team != null){
				team.setName(name);
			}else{
				team = new IfafirmTeam();
				team.setIfafirm(memberAdmin.getIfafirm());
				team.setIsValid("1");
				team.setName(name);
				team.setCode(code);
				team.setOrderBy(orderBy);
				team.setReamrk(remark);
				if (StringUtils.isNotBlank(parentId)) {
					IfafirmTeam parentTeam = ifafirmService.getIfafirmTeam(parentId);
					if(parentTeam != null){
						team.setParent(parentTeam);
						if(parentTeam.getClassLayer() != null){
							team.setClassLayer(parentTeam.getClassLayer() + 1);
						}
					}
				}
			}
			team = ifafirmService.saveOrUpdateTeam(team, false);
			flag = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("team", team);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 删除组织机构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/deleteOrg", method = RequestMethod.POST)
	public void deleteOrg(HttpServletRequest request,HttpServletResponse response,Model model) {
		String teamId = request.getParameter("teamId");
		Boolean flag = false;
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		if (memberAdmin != null){
			flag = myFirmService.deleteOrg(teamId);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 设置团队负责人
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/setLeader", method = RequestMethod.POST)
	public void setLeader(HttpServletRequest request,HttpServletResponse response,Model model) {
		String ifaId = request.getParameter("ifaId");
		String teamId = request.getParameter("teamId");
		String isSupervisor = request.getParameter("isSupervisor");
		Boolean flag = false;
		flag = myFirmService.saveLeader(ifaId, teamId, isSupervisor);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 团队移除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/delIfa", method = RequestMethod.POST)
	public void delIfa(HttpServletRequest request,HttpServletResponse response,Model model) {
		String ifaId = request.getParameter("ifaId");
		String teamId = request.getParameter("teamId");
		Boolean flag = false;
		flag = myFirmService.delIfa(ifaId, teamId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取待审批的IFA
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/getMemberIfaIfafirm", method = RequestMethod.POST)
	public void getMemberIfaIfafirm(MemberIfaIfafirmVO memberIfaIfafirmVO,HttpServletRequest request,HttpServletResponse response,Model model) {
		String langCode = this.getLoginLangFlag(request);
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		jsonPaging = this.getJsonPaging(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			String dateFomat = memberAdmin.getMember().getDateFormat();
			if (StringUtils.isBlank(dateFomat)) {
				dateFomat = CommonConstants.FORMAT_DATE_TIME;
			}
			jsonPaging = myFirmService.getMemberIfaIfafirm(memberAdmin.getIfafirm().getId(), memberIfaIfafirmVO, dateFomat, langCode, jsonPaging);
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * IFA审批操作
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/confirmIfa", method = RequestMethod.POST)
	public void confirmIfa(String ifaId, String status, HttpServletRequest request,HttpServletResponse response,Model model) {
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		MemberIfaIfafirm memberIfaIfafirm = null;
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			String opinion = toUTF8String(request.getParameter("opinion"));
			memberIfaIfafirm = myFirmService.saveMemberIfaIfafirm(memberAdmin, ifaId, status, opinion);
			if(memberIfaIfafirm != null){
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("memberIfaIfafirm", memberIfaIfafirm);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * IFA团队更换
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@RequestMapping(value = "/updateIfaTeam", method = RequestMethod.POST)
	public void updateIfaTeam(String ifaId, HttpServletRequest request,HttpServletResponse response,Model model) {
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		IfafirmTeamIfa teamIfa = null;
		if (memberAdmin != null && "1".equals(memberAdmin.getType())){
			String newTeamId = request.getParameter("newTeamId");
			String oldTeamId = request.getParameter("oldTeamId");
			Integer count = myFirmService.updateIfaTeam(memberAdmin.getIfafirm().getId(), ifaId, oldTeamId, newTeamId);
			if(count != null && count > 0){
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("teamIfa", teamIfa);
		JsonUtil.toWriter(result, response);
	}
}
