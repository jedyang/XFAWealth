/**
 * 
 */
package com.fsll.wmes.ifafirm.action.console;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.service.SysCountryService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.vo.IfaSearchItemVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.PersonalInfoVO;

/**
 * @author michael
 * 
 */
@Controller
@RequestMapping("/console/ifafirm/info")
public class ConsoleIfaFirmInfoAct extends WmesBaseAct {

	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
	private MemberManageServiceForConsole memberManageService;
	@Autowired
	private SysCountryService sysCountryService;

	@Autowired
	private MemberBaseService memberBaseService;

	@Autowired
	private AccessoryFileService accessoryFileService;
	
	/**
	 * IFA Firm首页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String ifaFirmHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifaFirm/ifaFirmHome";
	}

	/**
	 * IFA Firm详细信息
	 * @author zxtan
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/firmDetail", method = RequestMethod.GET)
	public String ifaFirmInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifaFirmId = "";//test 待删除
		//权限控制
		MemberSsoVO curMember = getLoginUserSSO(request); // (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
			MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
			if(null != memberAdmin && null != memberAdmin.getIfafirm())
				ifaFirmId = memberAdmin.getIfafirm().getId();
		}

//		String ifaFirmId = "8080804056a201d90156b01c3aeb0018";//test 待删除
		
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
        	memberIfafirmVO.setFirmLogo(PageHelper.getLogoUrl(ifafirm.getFirmLogo(),"F"));
        }
        
        List<IfaSearchItemVO> countryList = sysCountryService.findAllCountryList(getLoginLangFlag(request));
        
        model.put("ifafirm", memberIfafirmVO);
        model.put("countryList", countryList);
        
		return "console/ifafirm/firm_detail";
	}
	
	/****
	 * 保存公司信息的方法
	 * @author zxtan
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveFirmInfo", method = RequestMethod.POST)
	public void saveFirmInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			boolean isAdd =false;
			String id=request.getParameter("id");
			String entitytype=request.getParameter("entitytype");
			String registerNo=request.getParameter("registerNo");
			String isFinancial=request.getParameter("isFinancial");
			String giin=request.getParameter("giin");
			String incorporationPlace=request.getParameter("incorporationPlace");
			String registeredAddress=request.getParameter("registeredAddress");
			String mailingAddress=request.getParameter("mailingAddress");
			String country=request.getParameter("country");
			
			String naturePurpose=request.getParameter("naturePurpose");
			String incorporationDate=request.getParameter("incorporationDate");
			String website=request.getParameter("website");
			String superCheckType = request.getParameter("superCheckType");
			String companyNameSc=request.getParameter("companyNameSc");
			String companyNameTc=request.getParameter("companyNameTc");
			String companyNameEn=request.getParameter("companyNameEn");
			
			String fileList=request.getParameter("imgList");
	
			
			MemberIfafirm info = new MemberIfafirm();
			info.setId(id);
			info.setCompanyName(companyNameSc);
			info.setEntityType(entitytype);
			info.setRegisterNo(registerNo);
			info.setIsFinancial(isFinancial);
			info.setGiin(giin);
			info.setIncorporationPlace(incorporationPlace);
			info.setRegisteredAddress(registeredAddress);
			info.setMailingAddress(mailingAddress);
			info.setCountry(country);
			
			info.setNaturePurpose(naturePurpose);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				java.util.Date date=sdf.parse(incorporationDate);
				info.setIncorporationDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			info.setWebsite(website);
			info.setSuperCheckType(superCheckType);
			info.setIsValid("1");
			
			if(null!=info.getId()&&!"".endsWith(info.getId())){
	
				isAdd=false;  
			}else{
				isAdd=true;
				info.setId(null);
	
			}
			// 保存公司logo
			if (null != info.getId()){
				String[] valArr = fileList.split("\\|");
	//			String fileType = valArr[0];
	//			String fileName = valArr[1];
				String filePath = valArr[3];
				if (!StrUtils.isEmpty(filePath)) info.setFirmLogo(filePath);
			}
			
			info=ifafirmService.saveOrUpdate(info,isAdd);
			
			
			//修改多语言信息
			if(null != info){
				String pinyin = ChineseToEnglish.getPingYin(companyNameSc);
				MemberIfafirmSc memberIfafirmSc = new MemberIfafirmSc();
					memberIfafirmSc.setId(info.getId());
					memberIfafirmSc.setCompanyName(companyNameSc);
					memberIfafirmSc.setPinYin(pinyin);
				memberIfafirmSc = ifafirmService.saveOrUpdateFirmSc(memberIfafirmSc);
				
				MemberIfafirmTc memberIfafirmTc = new MemberIfafirmTc();
					memberIfafirmTc.setId(info.getId());
					memberIfafirmTc.setCompanyName(companyNameTc);
					memberIfafirmTc.setPinYin(pinyin);
				memberIfafirmTc = ifafirmService.saveOrUpdateFirmTc(memberIfafirmTc);
				
				MemberIfafirmEn memberIfafirmEn = new MemberIfafirmEn();
					memberIfafirmEn.setId(info.getId());
					memberIfafirmEn.setCompanyName(companyNameEn);
					memberIfafirmEn.setPinYin(pinyin);
				memberIfafirmEn = ifafirmService.saveOrUpdateFirmEn(memberIfafirmEn);
			}
			//end
			
	//		// 保存公司logo
	//		if (!StrUtils.isEmpty(fileList) && null != info && null != info.getId()){
	//			String[] valArr = fileList.split("\\|");
	//			String fileType = valArr[0];
	//			String fileName = valArr[1];
	//			String filePath = valArr[3];
	//			AccessoryFile accessoryFile = new AccessoryFile();
	//			accessoryFile.setFileName(fileName);
	//			accessoryFile.setFilePath(filePath);
	//			accessoryFile.setFileType(fileType);
	//			accessoryFile.setModuleType("member_ifafirm");
	//			accessoryFile.setLangCode(this.getLoginLangFlag(request));
	//			accessoryFile.setRelateId(info.getId());
	//			accessoryFile.setCreateBy(this.getLoginUser(request));
	//			accessoryFileService.saveAccessoryFile(accessoryFile);
	//		}
			
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	@RequestMapping(value = "/teamDetail", method = RequestMethod.GET)
	public String ifaFirmTeamInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request,model);
		
		String ifaFirmId = "";//test 待删除
		//权限控制
		MemberSsoVO curMember = getLoginUserSSO(request); // (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
			MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
			if(null != memberAdmin && null != memberAdmin.getIfafirm())
				ifaFirmId = memberAdmin.getIfafirm().getId();
		}

//		String ifaFirmId = "8080804056a201d90156b01c3aeb0018";//test 待删除
		
//		String ifafirmId = request.getParameter("ifafirmId");	
		MemberIfafirm memberIfafirm = ifafirmService.findById(ifaFirmId,getLoginLangFlag(request));
		if(null != memberIfafirm){
			model.put("companyName",memberIfafirm.getCompanyName());
		}
		model.put("ifafirmId",ifaFirmId);
        
		return "console/ifafirm/firm_team";
	}
	
	@RequestMapping(value = "/inputTeam", method = RequestMethod.GET)
	public String inputTeam(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifafirmId=request.getParameter("ifafirmId");
			model.put("ifafirmId", curAdmin.getIfafirm().getId());
			String parentId=request.getParameter("parentId");
			model.put("parentId", parentId);
			String teamId=request.getParameter("teamId");
			model.put("teamId", teamId);
			String classLayer= StrUtils.getString(request.getParameter("classLayer"),"1");
			model.put("classLayer", classLayer);
			
			return "console/ifafirm/firm_team_input";
		}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
	}
	
	/**
	 * IFAFirmTeam获取公司的团队数据
	 * @author zxtan
	 * @date 2016-12-7 
	 */
	@RequestMapping(value = "/listfirmteamJson", method = RequestMethod.POST)
	public void listFirmTeamJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String keyword=request.getParameter("keyword");
//			String ifafirmid=request.getParameter("ifafirmid");
			IfafirmTeam info=new IfafirmTeam();
//			MemberIfafirm firm = new MemberIfafirm();
//			firm.setId(ifafirmid);
			info.setIfafirm(curAdmin.getIfafirm());
			info.setName(keyword);
			List<IfafirmTeam> teamList = ifafirmService.findTeamAll(info);
			obj.put("result",true);
			obj.put("teamJson",JsonUtil.toJson(teamList));
		}else {
			obj.put("result",false);
			obj.put("teamJson","");
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 检查团队编码是否已存在（用于bootstrapValidator 的 remote校验方法）
	 * @author michael
	 * @date 2016-12-7 
	 */
	@RequestMapping(value = "/checkTeamIfExist", method = RequestMethod.POST)
	public void checkTeamIfExist(HttpServletRequest request,HttpServletResponse response,ModelMap model, String code) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String id = StrUtils.getString(request.getParameter("id"));
		IfafirmTeam team = ifafirmService.findTeamByCode(code);
		
		//valid: true 表示合法，验证通过, false 表示不合法，验证不通过
		if (null!=team && null!=team.getId() && !StrUtils.isEmpty(team.getId()))
			if (id.equals(team.getId()))
				obj.put("valid",true);//如果返回的是自己
			else
				obj.put("valid",false);//已存在
		else
			obj.put("valid",true);
		JsonUtil.toWriter(obj, response);//需要的返回结果一定是json格式
	}
	
	/**
	 * IFAFirmTeam 新增、编辑团队节点
	 * @author zxtan
	 * @date 2016-12-06
	 */
	@RequestMapping(value="/addTeamInfo")
	public void addIfafirmTeamInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				info.setIfafirm(curAdmin.getIfafirm());
				if(StringUtils.isNoneBlank(info.getId())){
					//edit
					IfafirmTeam team = ifafirmService.getIfafirmTeam(info.getId());
					if(team!=null){
						team.setName(info.getName());
						team.setCode(info.getCode());
						team.setReamrk(info.getReamrk());
						team.setOrderBy(info.getOrderBy());
						ifafirmService.saveOrUpdateTeam(team, false);
					}
				}else{
					//add
					info.setId(null);
					if(info.getParent() != null && "".equals( info.getParent().getId()))
					{
						info.setParent(null);
					}
					info.setIsValid("1");
					info = ifafirmService.saveOrUpdateTeam(info, true);
				}
				result = true;
				obj.put("teamId", info.getId());
			}else {
				obj.put("result",false);
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/****
	 * 删除团队节点
	 * @author zxtan
	 * @date 2016-12-06
	 */
	@RequestMapping(value = "/delTeamInfo", method = RequestMethod.POST)
	public void delIfafirmTeamInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				String id=request.getParameter("id");
				result = ifafirmService.delIfafirmTeamInfo(id);//物理删除,检查是否有成员
	//			result = ifafirmService.delIfafirmTeamInfoLogical(id);//逻辑删除
				IfafirmTeam team = new IfafirmTeam();
				team.setId(id);
				
				if(!result){
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"ifafirm.team.delete.tip1",null));
				}
			}else{
				result = false;
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 弹出团队成员管理窗口
	 * @author zxtan
	 * @date 2016-12-07
	 */
	@RequestMapping(value = "/dialogTeamUser")
	public String dialogTeamUserShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String teamid=request.getParameter("teamid");
//			String ifafirmid=request.getParameter("ifafirmid");
			model.put("teamid", teamid);
			model.put("ifafirmid", curAdmin.getIfafirm().getId());
			
			return "console/ifafirm/firm_team_user";
		}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
	}
	
	/***
	 * 团队成员管理tab/可添加成员列表
	 * @author scshi
	 * date 20170626
	 * */
	@RequestMapping(value = "/teamUserManagementPage")
	public String teamUserManagementPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		String teamid=request.getParameter("teamid");
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		model.put("teamid", teamid);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			model.put("ifafirmid", curAdmin.getIfafirm().getId());
		}
		return "console/ifafirm/teamUserManagement";
	}
	
	/**
	 * 弹出团队成员管理窗口
	 * @author zxtan
	 * @date 2016-12-07
	 */
	@RequestMapping(value = "/dialogTeamUserSelect")
	public String dialogTeamUserSelect(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String teamid=request.getParameter("teamid");
//			String ifafirmid=request.getParameter("ifafirmid");
			model.put("teamid", teamid);
			model.put("ifafirmid", curAdmin.getIfafirm().getId());
			
			return "console/ifafirm/firm_team_user_select";
		}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
	}
	
	/**
	 * 给团队添加成员
	 * */
	@RequestMapping(value="/addTeamMemberIfa")
	public void addTeamMemberIfa(IfafirmTeamIfa info,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaid=request.getParameter("ifaid");
			String teamid=request.getParameter("teamid");
//			String ifafirmid=request.getParameter("ifafirmid");
			boolean isAdd =false;
			//判断该成员是否存在在该团队中
			IfafirmTeamIfa ifafirmTeamIfa = ifafirmService.checkIfaIsExistInTeam(curAdmin.getIfafirm().getId(), teamid, ifaid);
			if(null!=ifafirmTeamIfa)
			{
				isAdd=true;
			} 
			else
			{
				info.setId(null);
				
				MemberIfafirm memberIfafirm = new MemberIfafirm();
				memberIfafirm.setId(curAdmin.getIfafirm().getId());
				info.setIfafirm(memberIfafirm);
	
				IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
				ifafirmTeam1.setId(teamid);
				info.setTeam(ifafirmTeam1);
				
				MemberIfa memberIfa = new MemberIfa();
				memberIfa.setId(ifaid);
				info.setIfa(memberIfa);
	//			info.setIfaId(ifaid);
	//			info.setIfafirmId(ifafirmid);
	//			info.setTeamId(teamid);
				info.setIsSupervisor("0");
				isAdd=true;
				info=ifafirmService.saveOrUpdateTeamIfa(info, isAdd);
			}
					
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * IfafirmTeamIfa 删除团队成员
	 * delIfafirmTeamIfa
	 * @author qgfeng
	 * @date 2016-11-17
	 */
	@RequestMapping(value = "/delIfafirmTeamIfa", method = RequestMethod.POST)
	public void delIfafirmTeamIfa(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				if(StringUtils.isNoneBlank(id)){
					ifafirmService.delIfafirmTeamIfa(id);
				}
				obj.put("result",true);
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			}else{
				obj.put("result",false);
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			}
		} catch (Exception e) {
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}finally{
			ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}
	}
	
	/**
	 * 通过公司与团队ID获取其所有成员数据
	 * */
	@RequestMapping(value="/listTeamMemberIfaJson")
	public void getTeamMemberIfaByTeamId(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

			String keyword=request.getParameter("keyword");
			String teamid=request.getParameter("teamid");
			String ifafirmid=request.getParameter("ifafirmid");
	//		String ifaid=request.getParameter("ifaid");
			
			jsonPaging=this.getJsonPaging(request);
			jsonPaging=ifafirmService.findAllTeamIfa(jsonPaging, ifafirmid,teamid,keyword);
			this.toJsonString(response, jsonPaging);
		}else {
			this.toJsonString(response, null);
		}
	}
	
	
	/**
	 * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
	 * */
	@RequestMapping(value="/listKeyToIFAJson")
	public void getIFAKeyList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

			String ifafirmid=request.getParameter("ifafirmid");
			String keyword=request.getParameter("keyword");
			List<MemberIfa> list = ifafirmService.getIFAKeyList(ifafirmid, keyword);
			String ifaJson = JsonUtil.toJson(list);
			obj.put("result",true);
			obj.put("ifaJson",ifaJson);
			JsonUtil.toWriter(obj, response);
			//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
	 * */
	@RequestMapping(value="/listToSelectIFAJson")
	public void getIFAListForSelect(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

//			String ifafirmId=request.getParameter("ifafirmid");
			String teamId=request.getParameter("teamid");
			String keyword=request.getParameter("keyword");
			jsonPaging=this.getJsonPaging(request);
			jsonPaging = ifafirmService.getIFAListForSelect(jsonPaging, curAdmin.getIfafirm().getId(), teamId, keyword);
			List<MemberIfa> list = jsonPaging.getList();
			String ifaJson = JsonUtil.toJson(list);
			obj.put("result",true);
			obj.put("ifaJson",ifaJson);
			//ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}else {
			obj.put("result",false);
			obj.put("ifaJson","");
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/****
	 * 启动与取消团队成员的sv操作
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/dealSupervisor", method = RequestMethod.POST)
	public void dealSupervisor(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			//boolean isAdd =false;
			String id=request.getParameter("id");
	
			ifafirmService.dealSupervisor(id);
			
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 保存用户表单的方法
	 * @author 
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveteammemberifainfo", method = RequestMethod.POST)
	public void saveTeamMemberifaInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

		//boolean isAdd =false;
			String list=request.getParameter("list");
			String teamid=request.getParameter("teamid");
			String ifafirmid=request.getParameter("ifafirmid");
			
			//ifafirmService.delTeamMemberIfa(teamid, ifafirmid);
			
			String[] array = list.split(",");//
			for (int i = 0 ; i <array.length ; i++ ) 
			{
				String ifaId = array[i];
				if (!StrUtils.isEmpty(ifaId)){
					IfafirmTeamIfa ifa = new IfafirmTeamIfa();
					//isAdd = true;
					ifa.setId(null);
					
					MemberIfafirm memberIfafirm = new MemberIfafirm();
					memberIfafirm.setId(ifafirmid);
					ifa.setIfafirm(memberIfafirm);
		
					IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
					ifafirmTeam1.setId(teamid);
					ifa.setTeam(ifafirmTeam1);
					
					MemberIfa memberIfa = new MemberIfa();
					memberIfa.setId(ifaId);
					//ifa.setIfafirmId(ifafirmid);
					ifa.setIfa(memberIfa);
					//ifa.setTeamId(teamid);
					ifa.setIsSupervisor("0");
		
					ifafirmService.saveOrUpdateTeamMemberifa(ifa,true);
				}
			}
			
			obj.put("result",true);
			obj.put("ifaJson",list);
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 * IFA Firm-获取customer列表
	 * @author zxtan
	 * @date 2016-12-13
	 */
	@RequestMapping(value = "/customerList", method = RequestMethod.GET)
	public String customerList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "console/ifafirm/customer_list";
	}
	
	/**
	 * 通过公司ID获取其所有客户数据
	 * */
	@RequestMapping(value="/listCustomerJson", method = RequestMethod.POST)
	public void listCustomerJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String ifafirmId = "";//test 待删除
		//权限控制
		MemberSsoVO curMember = getLoginUserSSO(request); // (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
			MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
			if(null != memberAdmin && null != memberAdmin.getIfafirm())
				ifafirmId = memberAdmin.getIfafirm().getId();
		}
		String keyword =  request.getParameter("keyword");
		keyword = toUTF8String(keyword);
//		try {
//			keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=ifafirmService.findFirmCustomerList(jsonPaging, ifafirmId, keyword);
		this.toJsonString(response, jsonPaging);
	}

//	/**
//	 * IFA Firm-获取customer信息
//	 * @author zxtan
//	 * @date 2016-12-13
//	 */
//	@RequestMapping(value = "/customerInfo", method = RequestMethod.GET)
//	public String viewCustomerInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        return "console/ifafirm/customer_info";
//	}
	
	/**
	 * 获取个人信息页面加载
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/customerInfo", method = RequestMethod.GET)
	public String info(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//MemberBase loginUser = getLoginUser(request);
		String memberId = request.getParameter("memberId");
		if (memberId != null){
			String langCode = this.getLoginLangFlag(request);
			
			//String userId = loginUser.getId();//request.getParameter("id");
	        model.put("id", memberId);
	        
	        PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(memberId, langCode);
	        
	       
	        
	        //国家列表
	        List<GeneralDataVO> itemList = memberBaseService.findCountryList("", langCode);
	        List<GeneralDataVO> tempList=new ArrayList<GeneralDataVO>();
	        model.put("countryList", itemList);
	
	        //证件类型
	        itemList = findSysParameters("cert_type", langCode);
	        personalInfo.setCertType(getNameFromGeneralList(itemList, personalInfo.getCertTypeId()));
	        model.put("certList", itemList);
	
	        //教育程度
	        itemList = findSysParameters("education", langCode);
	        personalInfo.setEducation(getNameFromGeneralList(itemList, personalInfo.getEducationId()));
	        model.put("educationList", itemList);
	
	        //就业情况
	        itemList = findSysParameters("employment", langCode);
	        personalInfo.setEmployment(getNameFromGeneralList(itemList, personalInfo.getEmploymentId()));
	        model.put("employmentList", itemList);
	
	        //职业分类
	        itemList = findSysParameters("occupation", langCode);
	        personalInfo.setOccupation(getNameFromGeneralList(itemList, personalInfo.getOccupationId()));
	        model.put("occupationList", itemList);
	        
	        //兴趣爱好
	        itemList = findSysParameters("hobby_type", langCode);
	        tempList=itemList;
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getHobbyType(),"hobbyList");
	        model.put("hobbyList", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("allHobbyList", tempList);
	        
	        
	        itemList = findSysParameters("invest_field", langCode);
	        tempList=itemList;
	        //model.put("allInvestField", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getInvestField(),"investField");
	        model.put("investField", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("allInvestField", tempList);
	        
	        itemList = findSysParameters("investment_style", langCode);
	        tempList=itemList;
	      //  model.put("allInvestStyle", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getInvestStyle(),"investStyle");
	        model.put("investStyle", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("allInvestStyle", tempList);
	        
	        itemList=findSysParameters("currency_type", langCode);
	        model.put("currencyType", itemList);
	        
	        itemList = findSysParameters("service_language", langCode);
	        tempList=itemList;
	       // model.put("languageList", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getLanguageSpoken(),"languageSpoken");
	        model.put("languageSpoken", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("languageList", tempList);

	       
	        itemList = findSysParameters("service_region", langCode);
	       // model.put("allLiveRegion", itemList);
	        tempList=itemList;
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getLiveRegion(),"liveRegion");
	        model.put("liveRegion", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("allLiveRegion", tempList);

	        
	        String loginCode = (personalInfo==null)?"":personalInfo.getBaseInfo().getLoginCode();
	        model.put("personalInfo", personalInfo);
	       
//	        if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==loginUser.getMemberType() && CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType() ){
//	        	MemberSsoVO ssoVO = this.getLoginUserSSO(request);
//	        	List<PortfolioInfo> portfolioList= portfolioHoldService.findPortfolioByIFA(ssoVO.getIfaId());
//	        	model.put("portfolioList", portfolioList);
//	        }
	        
	        String privacy=personalInfo.getBaseInfo().getPrivacySetting();
	        HashMap<String, String> privacySetting=getPrivacySettings(privacy);
	        model.put("privacySetting",JsonUtil.toJson(privacySetting) );
	        
			this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_PERSONAL_INFO, "打开个人信息页面");
		}
//		else{//未登录，跳转到登录页面
//			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
//		}
		return "member/personal/personalInfo";
	}
	
	/**
	 * 从基础参数列表中获取基础参数名称
	 * @param request
	 * @param response
	 * @param model
	 */
	public String getNameFromGeneralList(List<GeneralDataVO> list, String id){
		if (list!=null && !list.isEmpty() && id!=null && id.length()>0)
			for (GeneralDataVO v: list){
				if (id.equals(v.getItemCode()))
					return v.getName();
			}
		return "";
	}
	
	/**
	 * 提取未被选择的属性
	 * @author mqzou 2016-11-28
	 * @param allList
	 * @param existsList
	 * @return
	 */
	private List<GeneralDataVO> getSelectList(List<GeneralDataVO> allList,List<GeneralDataVO> existsList){
		if(null!=allList && null!=existsList){
			Iterator it=allList.iterator();
			while (it.hasNext()) {
				GeneralDataVO vo = (GeneralDataVO) it.next();
				if(existsList.contains(vo)){
					it.remove();
				}
			}
		}
		
		return allList;
	}
	
	/**
	 * 从基础参数列表中获取指定编码的参数对象
	 * @param request
	 * @param response
	 * @param model
	 */
	private List<GeneralDataVO> getMyGeneralList(List<GeneralDataVO> list, String codes,String type){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty() && codes!=null && codes.length()>0){
			List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
			List<String> codeList=new ArrayList<String>();
			Iterator it=codeArr.iterator();
			while (it.hasNext()) {
				String object = (String) it.next();
				codeList.add(object);
			}
			for (GeneralDataVO v: list){
				if (codeArr.contains(v.getItemCode())){
					result.add(v);
				 //  int index=	codeArr.indexOf(v.getItemCode());
					codeList.remove(v.getItemCode());
				}
			}
			if("hobbyList".equals(type)  && !codeList.isEmpty()){//爱好因可自定义需要特殊处理 modify by mqzou 2016-11-29
				 it=codeList.iterator();
				while (it.hasNext()) {
					String str = (String) it.next();
					GeneralDataVO vo=new GeneralDataVO();
					vo.setName(str.replace("{", "").replace("}", ""));
					result.add(vo);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 将用户的隐私设置转换为hashMap
	 * @author mqzou
	 * @date 2016-11-30
	 * @param privacySetting
	 * @return
	 */
	private HashMap<String, String> getPrivacySettings(String privacySetting){
		HashMap<String, String> map=new HashMap<String, String>();
		if(null!=privacySetting && !"".equals(privacySetting)){
			String[] settings=privacySetting.split(";");
			for (String string : settings) {
				String[] setting=string.split(":");
				if(setting.length==2){
					map.put(setting[0].trim(), setting[1].trim());
				}
			}
		}
		
		return map;
	}
	
	
	/**
	 * IFA Firm-获取customer列表
	 * @author zxtan
	 * @date 2016-12-13
	 */
	@RequestMapping(value = "/accountList", method = RequestMethod.GET)
	public String accountList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "console/ifafirm/account_list";
	}
	
	/**
	 * 通过公司ID获取其所有客户数据
	 * */
	@RequestMapping(value="/listAccountJson", method = RequestMethod.POST)
	public void listAccountJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String ifafirmId = "";//test 待删除
		//权限控制
		MemberSsoVO curMember = getLoginUserSSO(request); // (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()){
			MemberAdmin memberAdmin = memberManageService.findAdminByMemberId(curMember.getId());
			if(null != memberAdmin && null != memberAdmin.getIfafirm())
				ifafirmId = memberAdmin.getIfafirm().getId();
		}
		String status =  request.getParameter("status");
		String keyword =  request.getParameter("keyword");
		keyword = toUTF8String(keyword);
//		try {
//			keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=ifafirmService.findFirmAccountList(jsonPaging, ifafirmId, keyword, status);
		this.toJsonString(response, jsonPaging);
	}
	
}
