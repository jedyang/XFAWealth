package com.fsll.wmes.ifa.action.console;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.ProposalVO;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifa.vo.IfaDistributorVO;
import com.fsll.wmes.ifa.vo.IfaProposalVO;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.service.OrderHistoryService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.web.service.WebFriendService;

/**
 * 控制器:ifa信息管理
 * @author qgfeng
 * @version 1.0
 * @date 2016-08-15
 */
@Controller
@RequestMapping("/console/ifa")
public class IfaManageAct extends WmesBaseAct{
	
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private IfafirmManageService iFAFrimService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysCountryService sysCountryService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private CrmProposalService crmProposalService;
	@Autowired
	private PortfolioInfoService portfolioInfoService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	@Autowired
	private OrderHistoryService orderHistoryService;
	
	/**
	 * ifa分页列表页面
	 * @author mqzou  midify qgfeng
	 * @date 2016-08-17
	 */
	@RequestMapping(value = "/list")
	public String ifalist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		//只允许ifaFirm管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null || curAdmin.getIfafirm()==null){
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
		return "console/ifa/ifa_list";
	}
	
	/**
	 * ifa分页列表
	 * @author qgfeng
	 * @date 2016-12-13
	 */
	@RequestMapping(value = "/ifalist", method = RequestMethod.POST)
	public void getIfaInIfaFirm(HttpServletRequest request, HttpServletResponse response,IfaVO ifaVo) {
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			ifaVo.setIfafirmId(curAdmin.getIfafirm().getId());
			String langCode=this.getLoginLangFlag(request);
		    jsonPaging=ifaManageService.findIfaByIfaFirm(jsonPaging, ifaVo, langCode);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * ifa详细信息（添加）
	 * @author qgfeng
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/input")
	public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			MemberIfafirm ifaFirm = iFAFrimService.findById(curAdmin.getIfafirm().getId(), langCode);
			String id = request.getParameter("id");
			model.put("id", id);
			MemberIfa ifa = ifaManageService.findIfaById(id);
			if(null != ifa){
				IfaVO vo = new IfaVO(ifa,null);
				model.put("ifavo", vo);
			}
			model.put("ifaFirm", ifaFirm);
			return "console/ifa/ifa_input";
		}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
	}
	
	/**
	 * 保存Ifa
	 * @author qgfeng
	 * @date 2016-11-3
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveIfa(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String jsonData=request.getParameter("jsonData");
			if(StringUtils.isBlank(jsonData)){
				return null;
			}
			IfaVO ifaVO = (IfaVO) JSONObject.toBean(JSONObject.fromObject(jsonData), IfaVO.class);
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				MemberIfa memberIfa = null;
				MemberBase memberBase = null;
				if (StringUtils.isNotBlank(ifaVO.getId())) {
					memberIfa = ifaManageService.findIfaById(ifaVO.getId());
					memberIfa.setLastUpdate(new Date());
					memberBase = memberIfa.getMember();
					memberBase.setLastUpdate(new Date());
					//修改了密码
					if(StringUtils.isNotBlank(ifaVO.getRepassword())){
						memberBase.setPassword(pwdEncoder.encodePassword(ifaVO.getRepassword()));
					}
				} else {
					memberIfa = new MemberIfa();
					memberBase = new MemberBase();
					//IFA Firm管理员新增ifa，自动通过审批
					memberIfa.setIfafirm(curAdmin.getIfafirm());
					memberIfa.setCompanyType("2");
					memberIfa.setIfafirmStatus("1");
					memberBase.setMemberType(2);
					memberBase.setSubMemberType(21);
					memberBase.setIsValid("1");
					memberBase.setStatus("1");
					memberBase.setCreateTime(new Date());
					memberBase.setPassword(pwdEncoder.encodePassword(ifaVO.getPassword()));
				}
				memberIfa.setFirstName(ifaVO.getFirstName());
				memberIfa.setLastName(ifaVO.getLastName());
				memberIfa.setBorn(DateUtil.getDate(ifaVO.getBorn()));
				memberIfa.setCertNo(ifaVO.getCertNo());
				memberIfa.setCertType(ifaVO.getCertType());
				if (StringUtils.isNoneBlank(ifaVO.getCountry())) {
					SysCountry country = sysCountryService.findBycountryId(ifaVO.getCountry());
					memberIfa.setCountry(country);
				}
				if (StringUtils.isNoneBlank(ifaVO.getNationality())) {
					SysCountry country = sysCountryService.findBycountryId(ifaVO.getNationality());
					memberIfa.setNationality(country);
				} 
				if (StringUtils.isNoneBlank(ifaVO.getPrimaryResidence())) {
					SysCountry country = sysCountryService.findBycountryId(ifaVO.getPrimaryResidence());
					memberIfa.setPrimaryResidence(country);
				} 
				memberIfa.setEducation(ifaVO.getEducation());
				memberIfa.setEmployment(ifaVO.getEmployment());
				memberIfa.setGender(ifaVO.getGender());
				memberIfa.setNameChn(ifaVO.getNameChn());
				memberIfa.setLastUpdate(new Date());
				
				memberIfa.setOccupation(ifaVO.getOccupation());
				memberIfa.setTelephone(ifaVO.getTelephone());
				memberIfa.setAddress(ifaVO.getAddress());
				memberIfa.setCeNumber(ifaVO.getCeNumber());
				memberIfa.setAppellation(ifaVO.getAppellation());
				memberIfa.setCfaNumber(ifaVO.getCfaNumber());
				memberIfa.setCfpNumber(ifaVO.getCfpNumber());
				memberIfa.setCompanyType(ifaVO.getCompanyType());
				
				memberIfa.setIntroduction(ifaVO.getIntroduction());
				memberIfa.setPosition(ifaVO.getPosition());		
				memberBase.setStatus(ifaVO.getStatus());
				
				memberBase.setLoginCode(StrUtils.getString(ifaVO.getLoginCode()));
				memberBase.setNickName(StrUtils.getString(ifaVO.getNickName()));
				memberBase.setEmail(StrUtils.getString(ifaVO.getEmail()));
				memberBase.setMobileNumber(ifaVO.getMobileNumber());
				memberBase.setLastUpdate(new Date());
				memberBase.setMobileCode(ifaVO.getMobileCode());
				memberBase.setIsValid("1");
				memberIfa.setMember(memberBase);
				memberIfa = ifaManageService.saveOrUpdateIFA(memberIfa);
				if(memberIfa != null && curAdmin.getIfafirm() != null){
					//先将旧的记录设为无效
					ifaManageService.updateIfaIfaFirmToInvalid(memberIfa.getId());
					
					//保存ifa与ifaFrim关联关系
					MemberIfaIfafirm releInfo = ifaManageService.getIfaIfaFirmByIFrimId(ifaVO.getId(),curAdmin.getIfafirm().getId());
					if(releInfo == null){//默认通过审批
						releInfo = new MemberIfaIfafirm();
						releInfo.setCreateTime(new Date());
						releInfo.setLastUpdate(new Date());
						releInfo.setBeginDate(new Date());
						releInfo.setEndDate(null);
						releInfo.setCheckDate(new Date());
						releInfo.setCheckStatus("1");
						releInfo.setIsValid("1");
						releInfo.setCheckOpinion("Created by IFA Firm");
					}
					releInfo.setIfa(memberIfa);
					releInfo.setIfafirm(curAdmin.getIfafirm());
					ifaManageService.saveOrUpdateIfaIfafirm(releInfo);
				}
				//保存用户和运营公司的关系
				if (memberIfa != null && memberIfa.getMember() != null && !memberIfa.getMember().getId().isEmpty()){
					String companyId = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_ID));
					CompanyInfo company = new CompanyInfo();
					company.setId(companyId);
	
					MemberCompany memberCompany = new MemberCompany();
					memberCompany.setCompany(company);
					memberCompany.setMember(memberIfa.getMember());
					memberBaseService.saveMemberCompany(memberCompany);
				}
				result = true;
			}else {
				result = false;
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}
	
	/**
	 * ifa启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/validOperate", method = RequestMethod.POST)
	public void memberValidOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				String ids = request.getParameter("ids");
				String isValid = request.getParameter("isValid");
				String[] idArry = ids.split(",");
				if(idArry!=null && idArry.length>0){
					for (String id : idArry) {
						MemberIfa ifa = ifaManageService.findIfaById(id);
						if(ifa!=null && ifa.getMember()!=null){
							MemberBase base = ifa.getMember();
							base.setIsValid(isValid);
							memberBaseService.saveOrUpdate(base);
						}
					}
				}
				result = true;
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
	 * ifa团队 跳转显示
	 * @author wwluo midify qgfeng
	 * @date 2016-08-15 
	 */
	@RequestMapping(value = "/teamList", method = RequestMethod.GET)
	public String teamList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaId = request.getParameter("ifaId");
			MemberIfa ifa = ifaManageService.findIfaById(ifaId);
			model.put("ifa", ifa);
			return "console/ifa/ifa_team_list";
		}else{
    		return "redirect:"+this.getFullPath(request)+"front/index.do";
    	}
	}
	/**
	 * ifa团队数据 
	 * @author qgfeng
	 * @date 2017-1-10
	 */
	@RequestMapping(value = "/teamJson", method = RequestMethod.POST)
	public void IfaTeamList(HttpServletRequest request,HttpServletResponse response) {
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaId = request.getParameter("ifaId");
			String keyword = request.getParameter("keyword");
			jsonPaging = ifaManageService.findTeamByIfaId(jsonPaging,ifaId,keyword);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * ifaFirm团队 跳转显示
	 * @author wwluo midify qgfeng
	 * @date 2016-08-16 
	 */
	@RequestMapping(value = "/firmTeamList", method = RequestMethod.GET)
	public String firmTeamList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaId = request.getParameter("ifaId");
			JsonPaging ifaTeamJson = new JsonPaging(); 
			ifaTeamJson = ifaManageService.findTeamByIfaId(ifaTeamJson, ifaId,null);
			model.put("ifaTeamJson", JsonUtil.toJson(ifaTeamJson));
			model.put("ifaId", ifaId);
	        return "console/ifa/ifafirm_team_list";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/index.do";
		}
	}
	
	/**
	 * ifaFirm团队 数据
	 * @author qgfeng
	 * @date 2017-1-10
	 */
	@RequestMapping(value = "/firmTeamJson", method = RequestMethod.POST)
	public void ifaFirmTeam(HttpServletRequest request, HttpServletResponse response) {
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			jsonPaging = ifaManageService.findTeamByIfaFirm(jsonPaging, curAdmin.getIfafirm().getId());
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
	}
	/**
	 * 加入团队（ifa关联team）
	 * @author wwluo
	 * @date 2016-08-16 
	 */
	@RequestMapping(value = "/addTeam", method = RequestMethod.POST)
	public void addTeam(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
//			String ifaFirmId = request.getParameter("ifafirmId");
			String teamId = request.getParameter("teamId");
			String ifaId = request.getParameter("ifaId");
			boolean flag = false;
			flag = ifaManageService.saveIfafirmTeamIfa(curAdmin.getIfafirm().getId(),teamId,ifaId);
		    obj.put("flag",flag);
		}else {
			obj.put("flag",false);
		}
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 * 获取ifa关联的"团队"
	 * @author wwluo
	 * @date 2016-08-15 
	 */
	@RequestMapping(value = "/ifa_team", method = RequestMethod.POST)
	public void getIfaTeam(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String ifaId = request.getParameter("ifaId");
			StringBuffer jsonBuffer = new StringBuffer();
			if(StringUtils.isNotBlank(ifaId)){
				jsonPaging = ifaManageService.findTeamByIfaId(jsonPaging, ifaId,null);
				jsonBuffer.append(JsonUtil.toJson(jsonPaging));
			}
			obj.put("status",true);
			obj.put("ifaTeamJson", jsonBuffer.toString());
			obj.put("ifaId", ifaId);
		}else {
			obj.put("status",false);
			obj.put("ifaTeamJson", "");
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * ifa取消关联team
	 * @author wwluo
	 * @date 2016-08-16 
	 */
	@RequestMapping(value = "/removeTeam", method = RequestMethod.POST)
	public void removeTeam(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String teamId = request.getParameter("teamId");
		String ifaId = request.getParameter("ifaId");
		boolean flag = false;
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			if(StringUtils.isNotBlank(teamId) && StringUtils.isNotBlank(ifaId))
				flag = ifaManageService.removeIfafirmTeamIfa(teamId,ifaId);
			obj.put("flag",flag);
		}else {
			obj.put("flag",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	
	//------ end Team --------
	
	/**
	 * 获取ifa的客户列表并跳转显示
	 * @author qgfeng
	 * @date 2016-11-18
	 */
	@RequestMapping(value = "/ifaInverstor")
	public String ifaInverstor(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String ifaId = request.getParameter("ifaId");
		model.put("ifaId", ifaId);
		return "console/ifa/ifa_inverstor";
	}

	/**
	 * 获取ifa的客户列表
	 * @author qgfeng
	 * @date 2016-11-18
	 */
	@RequestMapping(value = "/inverstor/listJson")
	public void findInvestorByIfa(HttpServletRequest request, HttpServletResponse response,IfaVO ifaVO) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
	    jsonPaging=this.getJsonPaging(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String langCode = getLoginLangFlag(request);
			jsonPaging = crmCustomerService.findInvestorByIfa(jsonPaging, ifaVO, langCode);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
	}
	
	//-----end ifa客户-----
	
	 /**
     * IFA策略列表页面
     * @author mqzou
     * @date 2016-08-19
     */
    @RequestMapping(value = "/strategylist")
    public String strategyList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request, model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	        String ifaId = request.getParameter("ifaId");
	        model.put("ifaId", ifaId);
	        // 地区列表
			List<GeneralDataVO> regionList = findSysParameters("region", this.getLoginLangFlag(request));
			model.put("regionList", regionList);
	
			// 主题分类
			List<GeneralDataVO> sectorList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
			model.put("sectorList", sectorList);
	
	        //风险等级
	        List<GeneralDataVO> riskList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
	        model.put("riskList", riskList);
	        return "console/ifa/strategylist";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/index.do";
		}
    }
	
	
	/**
     * IFA策略列表
     * @author mqzou , midified by qgfeng
     * @date 2016-08-19
     */
    @RequestMapping(value = "/strategylistJson", method = RequestMethod.POST)
    public void findStrategyList(HttpServletRequest request, HttpServletResponse response) {
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	        String sectors = StrUtils.getString(request.getParameter("sectors"));
	        String regions = StrUtils.getString(request.getParameter("regions"));
	        String riskLevel = StrUtils.getString(request.getParameter("riskLevel"));
	        String ifaId = StrUtils.getString(request.getParameter("ifaId"));
	        String keyword = StrUtils.getString(request.getParameter("keyword"));
	        
	        String langCode = getLoginLangFlag(request);
	        
	        StrategyInfoVO infoVO = new StrategyInfoVO();
			infoVO.setRiskLevel(riskLevel);
			infoVO.setSector(sectors);
			infoVO.setGeoAllocation(regions);
	        infoVO.setStrategyName(keyword);
	        infoVO.setCreator(curAdmin.getMember().getId());
	        
	        if (null != ifaId && !"".equals(ifaId)) {                                                                                                 
	            MemberIfa ifa = ifaManageService.findIfaById(ifaId);
	            if (null != ifa)
	            	infoVO.setCreatorId(ifa.getMember().getId());
	        }
	        
	        jsonPaging = ifaManageService.findStrategyList(jsonPaging, infoVO, langCode);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
    }

    /**
     * IFA策略详细信息页面
     * @author mqzou
     * @date 2016-08-22
     */
    @RequestMapping(value = "/strategyinfo")
    public String strategyDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model)  {
    	this.isMobileDevice(request, model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	        String id = request.getParameter("id");
	        String langCode=getLoginLangFlag(request);
	        StrategyInfo info=ifaManageService.findStragyById(id);
	        StrategyInfoVO vo=new StrategyInfoVO();
	        if(null!=info){
	            vo.setId(info.getId());
	            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
	            vo.setCreateTime(sdf.format(info.getCreateTime()));
	            vo.setCreator(null!=info.getCreator()?info.getCreator().getNickName():"" );
	            vo.setDescription(info.getDescription());
	            vo.setGeoAllocation(info.getGeoAllocation());
	            vo.setGeoAllocationName(sysParamService.findNameByCode(info.getGeoAllocation(), langCode));
	            vo.setInvestmentGoal(info.getInvestmentGoal());
	            vo.setSector(info.getSector());
	            vo.setSectorName(sysParamService.findNameByCode(info.getSector(), langCode));
	            vo.setStrategyName(info.getStrategyName());
	            vo.setSuitability(info.getSuitability());
	        }
	        model.put("DataVo", vo);
	        return "console/ifa/strategy_input";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/index.do";
		}
    }
    
    /**
     * IFA创建的投资组合列表页面
     * @author mqzou midify qgfeng
     * @date 2016-08-25
     */
    @RequestMapping(value = "/portfolioList")
    public String showPofolioList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	this.isMobileDevice(request, model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	        String ifaId = request.getParameter("ifaId");
	        model.put("ifaId", ifaId);
	         // 地区列表
	 		List<GeneralDataVO> regionList = findSysParameters("region", this.getLoginLangFlag(request));
	 		model.put("regionList", regionList);
	
	 		// 主题分类
	 		List<GeneralDataVO> sectorList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
	 		model.put("sectorList", sectorList);
	
	        //风险等级
	        List<GeneralDataVO> riskList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
	        model.put("riskList", riskList);
	    	return "console/ifa/portfolio_list";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/index.do";
		}
    }
    /**
     * IFA创建的投资组合列表
     * @author mqzou midify qgfeng
     * @date 2016-08-25
     */
    @RequestMapping(value = "/pofolioJsonList")
    public void pofolioList(HttpServletRequest request, HttpServletResponse response,CriteriaVO criteria){
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	    	String langCode=getLoginLangFlag(request);
//	    	String ifaId = StrUtils.getString(request.getParameter("ifaId"));
//	    	//获取发布日期条件
//	        MemberIfa ifa = ifaManageService.findIfaById(ifaId);
//	        if(ifa!=null && ifa.getMember()!=null){
//	        	criteria.setUserId(ifa.getMember().getId());
//	        }
	    	criteria.setCurrUser(curAdmin.getMember());//设置当前登录人,firm 
	    	criteria.setUserId(curAdmin.getMember().getId());
	    	criteria.setFirm(curAdmin.getIfafirm());//获取当前登录的ifa firm
	    	jsonPaging=ifaManageService.findPofolioList(jsonPaging, criteria, langCode);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
    }
    
    /**
     * IFA观点
     * @author qgfeng
     * @date 2016-08-25
     */
    @RequestMapping(value = "/insight")
    public String insight(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	this.isMobileDevice(request, model);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	    	String ifaId = request.getParameter("ifaId");
	        model.put("ifaId", ifaId);
	    	// 地区列表
	 		List<GeneralDataVO> regionList = findSysParameters("region", this.getLoginLangFlag(request));
	 		model.put("regionList", regionList);
	
	 		// 主题分类
	 		List<GeneralDataVO> sectorList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
	 		model.put("sectorList", sectorList);
	    	return "console/ifa/ifa_insight";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/index.do";
		}
    }
    /**
     * 获取观点数据
     * @author qgfeng
     * @date 2016-12-26
     */
    @RequestMapping(value = "/insightList")
    public void insightList(HttpServletRequest request, HttpServletResponse response,CriteriaVO criter){
	    jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
	    	String langCode=getLoginLangFlag(request);
	    	String ifaId = StrUtils.getString(request.getParameter("ifaId"));
	        MemberIfa ifa = ifaManageService.findIfaById(ifaId);
	        if(ifa!=null && ifa.getMember()!=null){
	        	criter.setUserId(ifa.getMember().getId());
	        }
	        criter.setFirm(curAdmin.getIfafirm());
	      //获取发布日期条件
			String startDate = "";
			String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);
			if ("1D".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.DATE, -1);
			if ("1W".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			if ("1M".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			if ("3M".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			if ("6M".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			if ("1Y".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			if ("2Y".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
			if ("3Y".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
			if ("5Y".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
			if ("10Y".equals(criter.getPeriod())) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
			if ("YTD".equals(criter.getPeriod())) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来
			//开始日期统一提前一天
	        if (!"".equals(startDate)){
	        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);
	        }
	        criter.setStartDate(startDate);
	        criter.setEndDate(endDate);
	    	jsonPaging=ifaManageService.findInsightList(jsonPaging, criter, langCode);
		}else{
			jsonPaging.setList(null);
		}
	    this.toJsonString(response, jsonPaging);
    }
    

	/**
	 * 跳转ifa审批页面
	 * @author qgfeng
	 * @date 2016-12-15
	 */
    @RequestMapping(value = "/approval_list", method = RequestMethod.GET)
    public String approvalList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/ifa/ifa_approval_list";
    }
	
	/**
	 * 保存审批操作
	 * @author qgfeng
	 * @date 2016-12-15
	 */
	@RequestMapping(value = "/approval/approvalStatus", method = RequestMethod.POST)
	public void approvalStatus(HttpServletRequest request,HttpServletResponse response,IfaVO ifaVO)  {
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			ifaVO.setIfafirmId(curAdmin.getIfafirm().getId());
			boolean flag = ifaManageService.saveApprovalStatus(ifaVO);
		    obj.put("flag",flag);
		}else {
			obj.put("flag",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
		
		
	}
	
	/**
	 * 跳转ifa ae账户
	 * @author qgfeng
	 * @date 2016-12-15
	 */
    @RequestMapping(value = "/aeAccount", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
    	String ifaId = request.getParameter("ifaId");
    	model.put("ifaId", ifaId);
    	MemberIfa ifa = ifaManageService.findIfaById(ifaId);
    	model.put("ifa", ifa);
        return "console/ifa/ifa_aeaccount";
    }
    /**
     * ifa ae账户json查询
     * @author qgfeng
     * @date 2017-1-20
     */
    @RequestMapping(value = "/aeAccountJson")
    public void aeAccountJson(HttpServletRequest request, HttpServletResponse response,IfaDistributorVO searchVo){
	    jsonPaging=this.getJsonPaging(request);
	    jsonPaging=ifaManageService.findIfaDisList(jsonPaging,searchVo);//已包含 ifaId
	    this.toJsonString(response, jsonPaging);
    }
    
	/**
	 * ifa AE账户详细信息（添加）页面
	 * @author qgfeng
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/aeAccountInput")
	public String aeAccountInput(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String id = request.getParameter("id");
			IfaDistributor vo = ifaManageService.getIfaDisById(id);
			if (null==vo){
				vo = new IfaDistributor();
				vo.setIsValid("0");
			}
			model.put("vo", vo);
			
			String ifaId = request.getParameter("ifaId");
			if (null!=vo.getIfa() && !StrUtils.isEmpty(vo.getIfa().getId())){
				ifaId = vo.getIfa().getId();
			}
			model.put("ifaId", ifaId);

			model.put("ifafirmId", curAdmin.getIfafirm().getId());
			return "console/ifa/aeaccount_input";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
	}
	/**
	 * ifa AE账户保存操作
	 * @author qgfeng
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/saveAeaccount", method = RequestMethod.POST)
	public void saveAeaccount(HttpServletRequest request, HttpServletResponse response,IfaDistributorVO vo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			//MemberAdmin curAdmin = getLoginMemberAdmin(request);
			//if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
				IfaDistributor info = ifaManageService.getIfaDisById(vo.getId());
				if(info == null){
					info = new IfaDistributor();
					info.setCreateTime(new Date());
				}
				info.setLastUpdate(new Date());
				info.setAeCode(vo.getAeCode());
				info.setIsValid(vo.getIsValid());
				MemberIfa ifa = ifaManageService.findIfaById(vo.getIfaId());
				MemberDistributor distributor = distributorService.findDistributorById(vo.getDistributorId());
				if(ifa != null){
					info.setIfa(ifa);
				}
				if(distributor != null){
					info.setDistributor(distributor);
				}
				info = ifaManageService.saveIfaDis(info);
				if(info != null){
					result = true;
				}
//			}else {
//				result = false;
//				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	@RequestMapping(value = "/aeAccountDel", method = RequestMethod.POST)
	public void aeAccountDel(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){

				String id = request.getParameter("id");
				result = ifaManageService.deleteIfaDis(id);
			}else {
				result = false;
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
	}
    //---------公共-------------
    /**
	 * 获取自动填充查询条件列表(IFA)当前ifafrim下
	 * @author qgfeng
	 * @date 2016-12-15
	 */
	@RequestMapping(value = "/autoIfaList", method = RequestMethod.POST)
	public void autoCompleteIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			String keyWord=request.getParameter("keyWord");
			List<AutoCompleteVO> list = null;
			if(StringUtils.isNotBlank(keyWord)){
				list=ifaManageService.findAutoIfa(keyWord,curAdmin.getIfafirm().getId());
			}
			obj.put("list", list);
		}else {
			obj.put("list", null);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 弹出ifa选择
	 * @author qgfeng
	 * @date 2017-1-18
	 */
	@RequestMapping(value = "/selectIfa")
	public String dialogSelectIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String isSingle = request.getParameter("isSingle");//是否单选1位单选，否则默认多选
		model.put("isSingle", isSingle);
		return "console/ifa/dialog_select_ifa";
	}
	
	/**
	 * 获取ifafirm信息
	 * @author wwluo
	 * @date 2016-08-16 
	 */
	@RequestMapping(value = "/ifa_firm_detail", method = RequestMethod.GET)
	public String ifaFirmInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			model.put("ifafirm", curAdmin.getIfafirm());
			return "console/ifa/ifaFirm_info_input";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
	}
	
    /**
	 * 更换客户IFA（客户数据复制到新IFA，旧数据设冻结）
	 * @author michael
	 * @date 2017-3-1
	 */
	@RequestMapping(value = "/ifaMigrate", method = RequestMethod.POST)
	public void ifaClientMigrate(HttpServletRequest request, HttpServletResponse response, ModelMap model, String fromMemberId, String toMemberId){
		Map<String, Object> obj = new HashMap<String, Object>();
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		MemberBase curMember = this.getLoginUser(request);
		if (null!=curAdmin && null!=curAdmin.getIfafirm() && !StrUtils.isEmpty(curAdmin.getIfafirm().getId())){
			if (!StrUtils.isEmpty(fromMemberId) && !StrUtils.isEmpty(toMemberId)){
				//复制好友
				webFriendService.migrateIfaFriend(fromMemberId, toMemberId, curMember);
				
				//复制CRM记录
				crmCustomerService.migrateIfaCustomer(fromMemberId, toMemberId, curMember);
				
				//修改investor_account投资账户的ifa
				investorService.migrateIfaCustomerAccount(fromMemberId, toMemberId, curMember);
				
				//修改方案记录
				crmProposalService.migrateIfaCrmProposal(fromMemberId, toMemberId, curMember);
				
				//修改组合记录
				portfolioInfoService.migrateIfaPortfolio(fromMemberId, toMemberId, curMember);
				
				//修改持仓记录
				portfolioHoldService.migrateIfaPortfolioHold(fromMemberId, toMemberId, curMember);
				portfolioHoldService.migrateIfaPortfolioHoldAccount(fromMemberId, toMemberId, curMember);
				
				//修改订单记录
				orderHistoryService.migrateOrderHistory(fromMemberId, toMemberId, curMember);
				
				obj.put("result", true);
			}else{
				obj.put("result", false);
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.required",
						new String[]{PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"member.register.ifa",null)}));
			}
		}else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj,response);
	}
	
	
	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public String analysis(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "console/ifafirm/ifa/analysis";
	}
	
	@RequestMapping(value = "/analysisDistributor", method = RequestMethod.GET)
	public String analysisDistributor(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "console/ifafirm/ifa/analysisDistributor";
	}
	
	@RequestMapping(value = "/ifaAdministration", method = RequestMethod.GET)
	public String ifaAdministration(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "console/ifa/ifaAdministration";
	}
	/**
	 * ifa推荐的策略列表
	 * @author mqzou 2017-06-07
	 */
	@RequestMapping(value="/ifaRecommendStrategy")
	public String ifaRecommendStrategy(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		return "console/ifa/ifa_recommend_strategy";
	}
	
	/**
	 * ifa推荐的策略列表数据
	 * @author mqzou 2017-06-07
	 */
	@RequestMapping(value="/ifaRecommendStrategyJson")
	public void ifaRecommendStrategyJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		String ifaFirmId=ssoVO.getIfafirmId();
		String period=request.getParameter("period");
		String keyword=request.getParameter("keyword");
		if(null!=keyword && !"".equals(keyword)){
			keyword=toUTF8String(keyword);
		}
		String startDate="";
		String endDate=DateUtil.getCurDateStr(Calendar.DATE, 1);
		if ("1W".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		}
		else if ("1M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		}
		else if ("3M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		}
		else if ("6M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		}
		else if ("1Y".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		}
		else if(null!=period && !"".equals(period)) {
			String[] dates = period.split("to");
			if (null != dates && dates.length ==2) {
				startDate = dates[0].trim();
				endDate = dates[1].trim();
			}
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" r.createTime");
		jsonPaging=ifaManageService.findIfaFirmRecommendStrategy(jsonPaging, ifaFirmId, keyword, startDate, endDate, langCode);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * ifa推荐的组合列表
	 * @author mqzou 2017-06-07
	 */
	@RequestMapping(value="/ifaRecommendPortfolio")
	public String ifaRecommendPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String displayColor=loginUser.getDefDisplayColor();
		if(null==displayColor || "".equals( displayColor)){
			displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("defDisplayColor", displayColor);
		return "console/ifa/ifa_recommend_portfolio";
	}
	/**
	 * ifa推荐的组合列表数据
	 * @author mqzou 2017-06-07
	 */
	@RequestMapping(value="/ifaRecommendPortfolioJson")
	public void ifaRecommendPortfolioJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		String ifaFirmId=ssoVO.getIfafirmId();
		String period=request.getParameter("period");
		String keyword=request.getParameter("keyword");
	    String riskLevel=request.getParameter("risk");
		if(null!=keyword && !"".equals(keyword)){
			keyword=toUTF8String(keyword);
		}
		String startDate="";
		String endDate=DateUtil.getCurDateStr(Calendar.DATE, 1);
		if ("1W".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		}
		else if ("1M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		}
		else if ("3M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		}
		else if ("6M".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		}
		else if ("1Y".equals(period)){
			startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		}
		else if(null!=period && !"".equals(period)) {
			String[] dates = period.split("to");
			if (null != dates && dates.length ==2) {
				startDate = dates[0].trim();
				endDate = dates[1].trim();
			}
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" r.createTime");
		jsonPaging=ifaManageService.findIfaFirmRecommendPortfolio(jsonPaging, ifaFirmId, keyword, riskLevel, "", startDate, endDate, langCode);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
     * IFA创建的投资方案列表页面
     * @author wwluo 
     * @date 2016-08-25
     */
    @RequestMapping(value = "/proposalList")
    public String showProposalList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			return "console/ifa/proposal_list";
		}else{
			return "redirect:" + this.getFullPath(request) + "front/index.do";
		}
    }
    
    /**
     * IFA创建的投资方案列表
     * @author wwluo
     * @date 2016-08-25
     */
    @RequestMapping(value = "/proposalJsonList")
    public void findProposalList(IfaProposalVO ifaProposalVO,HttpServletRequest request, HttpServletResponse response,ProposalVO searchVO){
	    MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		jsonPaging = this.getJsonPaging(request);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			String langCode = this.getLoginLangFlag(request);
			jsonPaging = ifaManageService.getProposals(memberAdmin, jsonPaging, ifaProposalVO, langCode);
		}
		JsonUtil.toWriter(jsonPaging, response);
    }
}
