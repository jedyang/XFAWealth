package com.fsll.wmes.crm.action.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.CrmCustomerVO;
import com.fsll.wmes.crm.vo.CrmExistingCustomerVO;
import com.fsll.wmes.crm.vo.CrmProspectCustomerVO;
import com.fsll.wmes.crm.vo.CrmSelectVO;
import com.fsll.wmes.distributor.service.DistributorOrgService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmCustomerGroup;
import com.fsll.wmes.entity.CrmCustomerGroupRelationship;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.service.OrderHistoryService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.MemberInfoVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.PortfolioAssetsVO;

/**
 * 控制器:Ifa pipeline
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-9-6
 */

@Controller
@RequestMapping("/front/crm/pipeline")
public class FrontCrmPipelineAct extends WmesBaseAct {
	@Autowired
	private InvestorService investorService;

	@Autowired
	private CrmProposalService crmProposalService;

	@Autowired
	private PortfolioInfoService portfolioInfoService;

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private CrmCustomerService crmCustomerService;

	@Autowired
	private SysParamService sysParamService;

	@Autowired
	private DistributorOrgService distributorOrgService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;

	@Autowired
	private FundInfoService fundInfoService;
	// @Autowired
	// private IfaInfoService ifaInfoService;

	// @Autowired
	// private ITraderSendService iTraderSendService;

	/**
	 * 分页列表
	 */
	@RequestMapping(value = "/clientList", method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		// modify by mqzou 2016-10-28
		List<SysParamVO> styleList = sysParamService.getParamList(CommonConstants.DEF_LANG_CODE, "investment_style");
		List<SysParamVO> intresteList = sysParamService.getParamList(CommonConstants.DEF_LANG_CODE, "hobby_type");
		List<SysParamVO> regionList = sysParamService.getParamList(CommonConstants.DEF_LANG_CODE, "region");
		model.put("styleList", styleList);
		model.put("intresteList", intresteList);
		model.put("regionList", regionList);
		return "ifa/crm/myPipeline";// 页面摆放路径
	}

	/**
	 * 分页列表
	 */
	@RequestMapping(value = "/clientListNew", method = RequestMethod.GET)
	public String newList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);

		MemberSsoVO sso = getLoginUserSSO(request);

		if (null != sso) {
			List<MemberDistributor> distributorList = distributorOrgService.findAllDistributors();
			model.put("distributorList", distributorList);
			String langCode = sso.getLangCode();
			if (null == langCode || "".equals(langCode))
				langCode = CommonConstants.DEF_LANG_CODE;

			List<SysParamVO> langList = sysParamService.getParamList(langCode, "service_language");
			List<SysParamVO> styleList = sysParamService.getParamList(langCode, "investment_style");
			List<SysParamVO> intresteList = sysParamService.getParamList(langCode, "hobby_type");
			List<SysParamVO> regionList = sysParamService.getParamList(langCode, "service_region");
			model.put("styleList", styleList);
			model.put("intresteList", intresteList);
			model.put("regionList", regionList);
			model.put("langList", langList);

			// String expiredAeCode = getValidAccountNo(request);
			// //expiredAeCode = "ae00001";
			//
			// model.put("expiredAeCode", expiredAeCode);

			MemberBase loginUser = getLoginUser(request);
			String displayColor = "";
			if (null != loginUser)
				displayColor = loginUser.getDefDisplayColor();
			if (null == displayColor || "".equals(displayColor)) {
				displayColor = CommonConstants.DEF_DISPLAY_COLOR;
			}
			model.put("displayColor", displayColor);

			return "ifa/crm/myPipeline_new";// 页面摆放路径
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	// /**
	// * 获取失效账号
	// * @author zxtan
	// * @date 2017-01-09
	// * @param request
	// * @return
	// */
	// private String getValidAccountNo(HttpServletRequest request){
	// MemberBase memberBase = getLoginUser(request);
	// if(null != memberBase){
	// AccountValidVO validVO = iTraderSendService.saveCheckExistValid(request,
	// memberBase.getId());
	// if(null != validVO){
	// String expiredAeCode = validVO.getValidAccountNo();
	// return expiredAeCode;
	// }
	// }
	// return null;
	// }

	@Autowired
	private MemberBaseService memberBaseService;

	/**
	 * 分页获得记录 modidfy by mqzou 2016-10-25
	 */
	@RequestMapping(value = "/clientListJsonForIfa", method = RequestMethod.POST)
	public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String areaId = request.getParameter("areaId");
		String period = request.getParameter("period");
		String saleStageId = request.getParameter("saleStageId");
		String keyword = request.getParameter("keyword");
		String character = request.getParameter("char");

		// 测试数据
		// ifaMemberId = "";

		jsonPaging = this.getJsonPaging(request);
		jsonPaging = crmCustomerService.findCustomerListForIfa(jsonPaging, ifaMemberId, areaId, period, saleStageId, keyword, character);
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 获取投资人列表
	 * 
	 * @author mqzou 2016-10-25
	 */
	@RequestMapping(value = "/getInverstorList")
	public void getInverstorList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		String langCode = request.getParameter("langCode");
		String invStyle = request.getParameter("invStyle");
		String intrest = request.getParameter("intrest");
		String noIfa = request.getParameter("noIfa");
		String region = request.getParameter("region");
		jsonPaging = crmCustomerService.findInverstorNotInCrm(jsonPaging, curMember, langCode, invStyle, intrest, noIfa, region);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * 获取投资人的会员信息
	 * 
	 * @author mqzou 2016-11-02
	 */
	@RequestMapping(value = "/getMemberInfo")
	public void getMemberInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String memberId = request.getParameter("memberId");
		String langCode = getLoginLangFlag(request);
		MemberIndividual individual = memberBaseService.findIndividualMember(memberId);
		MemberInfoVO vo = new MemberInfoVO();
		String dateFormat = loginUser.getDateFormat();
		if (null == dateFormat || "".equals(dateFormat))
			dateFormat = CommonConstants.FORMAT_DATE;
		vo.setDateFormat(dateFormat);
		if (null != individual) {
			String educationCode = individual.getEducation();
			String education = sysParamService.findNameByCode(educationCode, langCode);
			vo.setEducation(education);
			vo.setEmail(individual.getMember().getEmail());
			vo.setFacebookCode(individual.getMember().getFacebookCode());
			vo.setWeiboCode(individual.getMember().getWeiboCode());
			vo.setTwitterCode(individual.getMember().getTwitterCode());
			String fieldCode = individual.getMember().getInvestField();
			String field = sysParamService.findNameByCode(fieldCode, langCode);
			List<String> fieldList = getParamsList(field);
			vo.setFavoriteInvestmentField(fieldList);
			String hobbyCode = individual.getMember().getHobbyType();
			String[] hobbyArray=hobbyCode.split(",");
			List<String> hobbyList=new ArrayList<String>();
			for (String hobby : hobbyArray) {
				if(hobby.startsWith("{") && hobby.endsWith("}")){
					hobbyList.add(hobby.substring(1,hobby.length()-1));
				}
			}
			String hobbies = sysParamService.findNameByCode(hobbyCode, langCode);
			List hList=getParamsList(hobbies);
			hList.addAll(hobbyList);
			vo.setHobby(hList);
			vo.setIconUrl(PageHelper.getUserHeadUrl(individual.getMember().getIconUrl(), individual.getGender()));
			vo.setIndividualId(individual.getId());
			String styleCode = individual.getMember().getInvestStyle();
			String styles = sysParamService.findNameByCode(styleCode, langCode);
			vo.setInvestmentStyle(getParamsList(styles));
			vo.setLastLoginDate(DateUtil.getDateStr(individual.getMember().getLoginTime()));
			 vo.setLineCode(individual.getMember().getLinkedInCode());
			
			vo.setLoginCode(individual.getMember().getLoginCode());
			vo.setMemberId(individual.getMember().getId());
			vo.setMobileNumber(individual.getMember().getMobileNumber());
			vo.setMobileCode(individual.getMember().getMobileCode());
			vo.setNickName(individual.getMember().getNickName());
			if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
				vo.setNationality(null != individual.getNationality() ? individual.getNationality().getNameEn() : "");
			} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
				vo.setNationality(null != individual.getNationality() ? individual.getNationality().getNameSc() : "");
			} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
				vo.setNationality(null != individual.getNationality() ? individual.getNationality().getNameTc() : "");
			}
			String occupationCode = individual.getOccupation();
			String occupation = sysParamService.findNameByCode(occupationCode, langCode);
			vo.setOccupation(occupation);
			
			String employmentCode=individual.getEmployment();
			String employment=sysParamService.findNameByCode(employmentCode, langCode);
			vo.setEmployment(employment);
			
			vo.setRegistrationDate(DateUtil.getDateStr(individual.getMember().getCreateTime()));
			vo.setResidence("");
			vo.setWebchatCode(individual.getMember().getWebchatCode());
			vo.setGender(individual.getGender());
			String privacy = individual.getMember().getPrivacySetting();
			HashMap<String, String> privacySetting = getPrivacySettings(privacy);
			vo.setPrimarySetting(JsonUtil.toJson(privacySetting));
			
			String languageSpokenCode=individual.getMember().getLanguageSpoken();
			String languageSpoken = sysParamService.findNameByCode(languageSpokenCode, langCode);
			vo.setLanguageSpoken(getParamsList(languageSpoken));
			
			String regionCode=individual.getMember().getLiveRegion();
			String region=sysParamService.findNameByCode(regionCode, langCode);
			vo.setLiveRegion(getParamsList(region));
			
		} else {
			MemberIfa ifa = memberBaseService.findIfaMember(memberId);
			if (null != ifa) {
				String educationCode = ifa.getEducation();
				String education = sysParamService.findNameByCode(educationCode, langCode);
				vo.setEducation(education);
				vo.setEmail(ifa.getMember().getEmail());
				vo.setFacebookCode(ifa.getMember().getFacebookCode());
				vo.setWeiboCode(ifa.getMember().getWeiboCode());
				vo.setTwitterCode(ifa.getMember().getTwitterCode());
				String fieldCode = ifa.getMember().getInvestField();
				String field = sysParamService.findNameByCode(fieldCode, langCode);
				List<String> fieldList = getParamsList(field);
				vo.setFavoriteInvestmentField(fieldList);
				String hobbyCode = ifa.getMember().getHobbyType();
				String hobbies = sysParamService.findNameByCode(hobbyCode, langCode);
				vo.setHobby(getParamsList(hobbies));
				vo.setIconUrl(PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender()));
				vo.setIndividualId(ifa.getId());
				String styleCode = ifa.getMember().getInvestStyle();
				String styles = sysParamService.findNameByCode(styleCode, langCode);
				vo.setInvestmentStyle(getParamsList(styles));
				vo.setLastLoginDate(DateUtil.getDateStr(ifa.getMember().getLoginTime()));
				// vo.setLineCode(individual.getMember().getLineCode());
				vo.setLoginCode(ifa.getMember().getLoginCode());
				vo.setMemberId(ifa.getMember().getId());
				vo.setMobileCode(ifa.getMember().getMobileCode());
				vo.setMobileNumber(ifa.getMember().getMobileNumber());
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					vo.setNationality(null != ifa.getNationality() ? ifa.getNationality().getNameEn() : "");
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setNationality(null != ifa.getNationality() ? ifa.getNationality().getNameSc() : "");
				} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setNationality(null != ifa.getNationality() ? ifa.getNationality().getNameTc() : "");
				}
				String occupationCode = ifa.getOccupation();
				String occupation = sysParamService.findNameByCode(occupationCode, langCode);
				vo.setOccupation(occupation);
				
				String employmentCode=ifa.getEmployment();
				String employment=sysParamService.findNameByCode(employmentCode, langCode);
				vo.setEmployment(employment);
				
				vo.setRegistrationDate(DateUtil.getDateStr(ifa.getMember().getCreateTime()));
				vo.setResidence("");
				vo.setWebchatCode(ifa.getMember().getWebchatCode());
				vo.setGender(ifa.getGender());
				String privacy = ifa.getMember().getPrivacySetting();
				HashMap<String, String> privacySetting = getPrivacySettings(privacy);
				vo.setPrimarySetting(JsonUtil.toJson(privacySetting));
				
				String languageSpokenCode=ifa.getMember().getLanguageSpoken();
				String languageSpoken = sysParamService.findNameByCode(languageSpokenCode, langCode);
				vo.setLanguageSpoken(getParamsList(languageSpoken));
				
				String regionCode=ifa.getMember().getLiveRegion();
				String region=sysParamService.findNameByCode(regionCode, langCode);
				vo.setLiveRegion(getParamsList(region));
				// vo.setPrimarySetting(JsonUtil.toJson(ifa.getMember().getPrivacySetting()));
			}
		}
		JsonUtil.toWriter(vo, response);
	}

	/**
	 * 将用户的隐私设置转换为hashMap
	 * 
	 * @author mqzou
	 * @date 2016-11-30
	 * @param privacySetting
	 * @return
	 */
	private HashMap<String, String> getPrivacySettings(String privacySetting) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (null != privacySetting && !"".equals(privacySetting)) {
			String[] settings = privacySetting.split(";");
			for (String string : settings) {
				String[] setting = string.split(":");
				if (setting.length == 2) {
					map.put(setting[0].trim(), setting[1].trim());
				}
			}
		}

		return map;
	}

	/**
	 * 字符串转list
	 * 
	 * @author mqzou 2016-11-02
	 * @param str
	 * @return
	 */
	private List<String> getParamsList(String str) {
		List<String> list = new ArrayList<String>();
		if (null != str && !"".equals(str)) {
			String[] strings = str.split(",");
			Collections.addAll(list, strings);
		}
		return list;

	}

	/**
	 * 添加客户
	 * 
	 * @author mqzou 2016-10-26
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/addCustomer")
	public void addCustomer(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != curMember) {
			CrmCustomer crm = new CrmCustomer();
			String memberId = request.getParameter("memberId");
			String salesStageId = request.getParameter("salesStageId");
			String clientType = request.getParameter("clientType");
			MemberIfa ifa = memberBaseService.findIfaMember(curMember.getId());
			MemberBase member = memberBaseService.findById(memberId);
			crm.setClientType(clientType);// 准客户
			crm.setCreateTime(DateUtil.getCurDate());
			crm.setLastUpdate(DateUtil.getCurDate());
			crm.setIconUrl(member.getIconUrl());
			crm.setIfa(ifa);
			crm.setIsValid("1");
			crm.setMember(member);
			crm.setNickName(member.getNickName());
			crm.setSalesStageId(salesStageId);

			crm = crmCustomerService.saveCustomer(crm);
			if (null != crm) {
				result.put("result", true);
			} else {
				result.put("result", false);
			}

		} else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 分页获得记录
	 */
	@RequestMapping(value = "/existingClientListJsonForIfa", method = RequestMethod.POST)
	public String existingClientListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String areaId = request.getParameter("areaId");
		String period = request.getParameter("period");
		String clientStatus = request.getParameter("clientStatus");
		String keyword = request.getParameter("keyword");
		String character = request.getParameter("char");

		// 测试数据
		// ifaMemberId = "";

		jsonPaging = this.getJsonPaging(request);
		jsonPaging = crmCustomerService.findExistingCustomerListForIfa(jsonPaging, ifaMemberId, areaId, period, clientStatus, keyword, character);
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 提醒信息
	 */
	@RequestMapping(value = "/clientRemind", method = RequestMethod.POST)
	public String getCustomerRemindDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String clientType = request.getParameter("clientType");
		String period = request.getParameter("period");
		// 测试数据
		// ifaMemberId = "";

		Long birthdayRemind = crmCustomerService.findCustomerBirthdayRemindForIfa(ifaMemberId, clientType, period);
		Long appointmentRemind = crmCustomerService.findCustomerAppointmentRemindForIfa(ifaMemberId, clientType, period);

		Map map = new HashMap<String, Integer>();
		map.put("birthday", birthdayRemind);
		map.put("appointment", appointmentRemind);

		JsonUtil.toWriter(map, response);
		return null;
	}

	/**
	 * 提醒信息
	 */
	@RequestMapping(value = "/existingClientRemind", method = RequestMethod.POST)
	public String getExistingCustomerRemindDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String clientType = request.getParameter("clientType");
		String period = request.getParameter("period");
		// 测试数据
		// ifaMemberId = "";

		Long birthdayRemind = crmCustomerService.findCustomerBirthdayRemindForIfa(ifaMemberId, clientType, period);
		Long appointmentRemind = crmCustomerService.findCustomerAppointmentRemindForIfa(ifaMemberId, clientType, period);
		Long portfolioRemind = crmCustomerService.findCustomerPortfolioRemindForIfa(ifaMemberId, clientType);
		Long kycRemind = crmCustomerService.findCustomerKYCRemindForIfa(ifaMemberId, clientType, period);

		Map map = new HashMap<String, Integer>();
		map.put("birthday", birthdayRemind);
		map.put("appointment", appointmentRemind);
		map.put("portfolio", portfolioRemind);
		map.put("kyc", kycRemind);

		JsonUtil.toWriter(map, response);
		return null;
	}

	/**
	 * 提醒信息
	 */
	@RequestMapping(value = "/updateDetail", method = RequestMethod.POST)
	public String updateDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String updateType = request.getParameter("updateType");
		// String saleStageId = request.getParameter("saleStageId");
		String id = request.getParameter("customerId");
		String saleStageId = request.getParameter("saleStageId");

		CrmCustomer obj = crmCustomerService.findCustomerById(id);
		if (null != obj) {
			if ("setImportant".equalsIgnoreCase(updateType)) {
				String isImportant;
				if (obj.getIsImportant() != null) {
					isImportant = "1".equals(obj.getIsImportant()) ? "0" : "1";
				} else {
					isImportant = "1";
				}
				// obj.setIsImportant(isImportant);
				// obj = crmCustomerService.saveCustomer(obj);

				crmCustomerService.updateImportant(obj.getId(), isImportant);
			} else if ("delete".equalsIgnoreCase(updateType)) {
				crmCustomerService.deleteCustomer(id, saleStageId);
			} else if ("setRemark".equalsIgnoreCase(updateType)) {
				String remark = request.getParameter("remark");
				crmCustomerService.updateRemark(id, remark);
			} else if ("setStageId".equalsIgnoreCase(updateType)) {
				crmCustomerService.updateStageId(id, saleStageId);
			}
		}

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("result", true);

		JsonUtil.toWriter(map, response);
		return null;
	}

	/**
	 * 详细信息
	 */
	@RequestMapping(value = "/clientDetail", method = RequestMethod.GET)
	public String getCustomerDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String customerId = request.getParameter("customerId");
		MemberBase loginUser = getLoginUser(request);

		String dateFormat = loginUser.getDateFormat();
		if (null == dateFormat || "".equals(dateFormat))
			dateFormat = CommonConstants.FORMAT_DATE;

		model.put("dateTimeFormat", dateFormat + " " + CommonConstants.FORMAT_TIME);
		model.put("dateFormat", dateFormat);

		CrmCustomerVO customerVO = crmCustomerService.findCustomerDetailForIfa(customerId, langCode);

		// 正式客户才需要查看持仓资产
		/*
		 * String check=request.getParameter("check"); if(check==null ||
		 * check=="0"){ AccountValidVO
		 * validVO=iTraderSendService.saveCheckExistValid(request,
		 * loginUser.getId()); if(null!=validVO){ model.put("checkList",
		 * validVO.getValidAccountNo()); model.put("accountType",
		 * CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA); }
		 * 
		 * }
		 */
		Date born = DateUtil.StringToDate(customerVO.getBorn(), DateUtil.DEFAULT_DATE_FORMAT);
		customerVO.setBorn(DateUtil.dateToDateString(born, dateFormat));
		MemberBase member = memberBaseService.findById(customerVO.getMemberId());

		String webchatCode = null != member.getWebchatCode() && !"".equals(member.getWebchatCode()) ? member.getWebchatCode() : "N/A";
		String linkedinCode = null != member.getLinkedInCode() && !"".equals(member.getLinkedInCode()) ? member.getLinkedInCode() : "N/A";
		String facebookCode = null != member.getFacebookCode() && !"".equals(member.getFacebookCode()) ? member.getFacebookCode() : "N/A";
		String qqCode = null != member.getQqCode() && !"".equals(member.getQqCode()) ? member.getQqCode() : "N/A";
		String weiboCode = null != member.getWeiboCode() && !"".equals(member.getWeiboCode()) ? member.getWeiboCode() : "N/A";
		String twitterCode = null != member.getTwitterCode() && !"".equals(member.getTwitterCode()) ? member.getTwitterCode() : "N/A";
		String mobileNumber = null != member.getMobileNumber() && !"".equals(member.getMobileNumber()) ? member.getMobileNumber() : "N/A";
		String email = null != member.getEmail() && !"".equals(member.getEmail()) ? member.getEmail() : "N/A";
		String nameChn = null != customerVO.getNameChn() && !"".equals(customerVO.getNameChn()) ? customerVO.getNameChn() : "N/A";
		String nickName = null != member.getNickName() && !"".equals(member.getNickName()) ? member.getNickName() : "N/A";
		String lastName = null != customerVO.getLastName() && !"".equals(customerVO.getLastName()) ? customerVO.getLastName() : "N/A";
		String firstName = null != customerVO.getFirstName() && !"".equals(customerVO.getFirstName()) ? customerVO.getFirstName() : "N/A";

		customerVO.setWebchatCode(!"".equals(webchatCode) ? webchatCode : "N/A");
		customerVO.setLinkedinCode(!"".equals(linkedinCode) ? linkedinCode : "N/A");
		customerVO.setFacebookCode(!"".equals(facebookCode) ? facebookCode : "N/A");
		customerVO.setQqCode(!"".equals(qqCode) ? qqCode : "N/A");
		customerVO.setWeiboCode(!"".equals(weiboCode) ? weiboCode : "N/A");
		customerVO.setTwitterCode(!"".equals(twitterCode) ? twitterCode : "N/A");
		customerVO.setMobileNumber(!"".equals(mobileNumber) ? mobileNumber : "N/A");
		customerVO.setEmail(!"".equals(email) ? email : "N/A");

		String privateSetting = member.getPrivacySetting();
		//客户设置了隐私显示并且非正式客户
		if (null != privateSetting && !"1".equals(customerVO.getClientType())) {
			privateSetting=privateSetting.toLowerCase();
			if (privateSetting.contains("webchat_code:0")) {
				customerVO.setWebchatCode(getPrivateStr(webchatCode, "N/A"));
			}
			if (privateSetting.contains("linkedin_code:0")) {
				customerVO.setLinkedinCode(getPrivateStr(linkedinCode, "N/A"));
			}
			if (privateSetting.contains("facebook_code:0")) {
				customerVO.setFacebookCode(getPrivateStr(facebookCode, "N/A"));
			}
			if (privateSetting.contains("qq_code:0")) {
				customerVO.setQqCode(getPrivateStr(qqCode, "N/A"));
			}
			if (privateSetting.contains("weibo_code:0")) {
				customerVO.setWeiboCode(getPrivateStr(weiboCode, "N/A"));
			}
			if (privateSetting.contains("twitter_code:0")) {
				customerVO.setTwitterCode(getPrivateStr(twitterCode, "N/A"));
			}

			if (privateSetting.contains("last_name:0")) {
				customerVO.setLastName(getPrivateStr(lastName, "N/A"));
			}
			if (privateSetting.contains("first_name:0")) {
				customerVO.setFirstName(getPrivateStr(firstName, "N/A"));
			}
			if (privateSetting.contains("email:0")) {
				customerVO.setEmail(getPrivateStr(email, "N/A"));
			}
			if (privateSetting.contains("name_chn:0")) {
				customerVO.setNameChn(getPrivateStr(nameChn, "N/A"));
			}
			if (privateSetting.contains("nick_name:0")) {
				customerVO.setNickName(getPrivateStr(nickName, "N/A"));
			}
			if (privateSetting.contains("mobile_number:0")) {
				customerVO.setMobileNumber(getPrivateStr(mobileNumber, "N/A"));
			}

		}
		// customerVO.setIconUrl(getUserHeadUrl(customerVO.getIconUrl(),
		// customerVO.getGender()));
		Date lastLoginTime = member.getLoginTime();
		if (null != lastLoginTime) {
			String lastLogin = DateUtil.getDateTimeGoneFormatStr(lastLoginTime, langCode, "");
			customerVO.setLastLogin(lastLogin);
		}

		// 兴趣爱好
		List<GeneralDataVO> itemList = findSysParameters("hobby_type", langCode);

		itemList = getMyGeneralList(itemList, member.getHobbyType(), "hobbyList");
		model.put("hobbyList", itemList);

		itemList = findSysParameters("invest_field", langCode);
		itemList = getMyGeneralList(itemList, member.getInvestField(), "investField");
		model.put("investField", itemList);

		itemList = findSysParameters("investment_style", langCode);
		itemList = getMyGeneralList(itemList, member.getInvestStyle(), "investStyle");
		model.put("investStyle", itemList);

		itemList = findSysParameters("service_language", langCode);
		itemList = getMyGeneralList(itemList, member.getLanguageSpoken(), "languageSpoken");
		model.put("languageSpoken", itemList);

		itemList = findSysParameters("service_region", langCode);
		itemList = getMyGeneralList(itemList, member.getLiveRegion(), "liveRegion");
		model.put("liveRegion", itemList);

		model.put("customerVO", customerVO);

		String currency = loginUser.getDefCurrency();
		if (null == currency || "".equals(currency))
			currency = CommonConstants.DEF_CURRENCY;
		model.put("currency", currency);
		String currencyName=sysParamService.findNameByCode(currency, langCode);
		model.put("currencyName", currencyName);

		String displayColor = loginUser.getDefDisplayColor();
		if (null == displayColor && "".equals(displayColor)) {
			displayColor = CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);

		return "ifa/crm/clientDetail";
	}

	private String getPrivateStr(String string, String filter) {
		return !filter.equals(string) && null != string ? "*****" + string.substring(string.length() - 1, string.length()) : "N/A";
	}

	/**
	 * 从基础参数列表中获取指定编码的参数对象
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	private List<GeneralDataVO> getMyGeneralList(List<GeneralDataVO> list, String codes, String type) {
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list != null && !list.isEmpty() && codes != null && codes.length() > 0) {
			List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes, ",", ""));
			List<String> codeList = new ArrayList<String>();
			Iterator it = codeArr.iterator();
			while (it.hasNext()) {
				String object = (String) it.next();
				codeList.add(object);
			}
			for (GeneralDataVO v : list) {
				if (codeArr.contains(v.getItemCode())) {
					result.add(v);
					// int index= codeArr.indexOf(v.getItemCode());
					codeList.remove(v.getItemCode());
				}
			}
			if ("hobbyList".equals(type) && !codeList.isEmpty()) {// 爱好因可自定义需要特殊处理
																	// modify by
																	// mqzou
																	// 2016-11-29
				it = codeList.iterator();
				while (it.hasNext()) {
					String str = (String) it.next();
					GeneralDataVO vo = new GeneralDataVO();
					vo.setName(str.replace("{", "").replace("}", ""));
					result.add(vo);
				}
			}
		}

		return result;
	}

	/**
	 * 投资账号列表 modify by mqzou 2016-12-31
	 */
	@RequestMapping(value = "/accountListJson", method = RequestMethod.POST)
	public String getAccountListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO ssoVO = getLoginUserSSO(request);
		String langCode = getLoginLangFlag(request);
		String customerMemberId = request.getParameter("customerMemberId");
		CrmCustomer customer = crmCustomerService.findCustomerById(customerMemberId);
		String currency = ssoVO.getDefCurrency();
		if (null == currency || "".equals(currency)) {
			currency = CommonConstants.DEF_CURRENCY;
		}
		AccountVO newVo = new AccountVO();

		// 新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
		// newVo.setOpenStatus("and  r.`open_status`='3' and r.`is_valid`='1'");

		// newVo.setIsValid("1");
		newVo.setBaseCurrency(currency);
		newVo.setMemberId(customer.getMember().getId());
		newVo.setOpenStatus("and  r.`member_id`='" + customer.getMember().getId() + "'");
		newVo.setIfaId(ssoVO.getIfaId());
		jsonPaging = this.getJsonPaging(request);
		jsonPaging.setRows(100);
		jsonPaging.setSort("a.distributor_id");
		jsonPaging.setOrder("asc");
		jsonPaging = investorService.findAccountList(jsonPaging, newVo, langCode);

		/*
		 * List<InvestorAccountDistributorVO> list =
		 * investorService.findInvestorDistributor(ifaMemberId,
		 * customerMemberId);
		 * 
		 * jsonPaging.setList(list);
		 */

		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 投资方案列表
	 */
	@RequestMapping(value = "/proposalListJson", method = RequestMethod.POST)
	public String getProposalListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		// request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
		String langCode = this.getLoginLangFlag(request);
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String customerMemberId = request.getParameter("customerMemberId");
		CrmCustomer customer = crmCustomerService.findCustomerById(customerMemberId);
		if(memberAdmin != null && memberAdmin.getIfafirm() != null){
			MemberIfafirm ifafirm = memberAdmin.getIfafirm();
			if(customer.getIfa().getIfafirm() != null 
					&& ifafirm.getId().equals(customer.getIfa().getIfafirm().getId())){
				ifaMemberId = customer.getIfa().getMember().getId();
			}
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = crmProposalService.findProposalListForIfa(jsonPaging, ifaMemberId, customer.getMember().getId(), langCode);
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 投资组合列表
	 */
	@RequestMapping(value = "/portfolioListJson", method = RequestMethod.POST)
	public String getPortfolioListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// MemberAdmin memberAdmin = (MemberAdmin)
		// request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
		MemberBase curMember = this.getLoginUser(request);
	//	String ifaMemberId = curMember.getId();
		String customerMemberId = request.getParameter("customerMemberId");
		CrmCustomer customer = crmCustomerService.findCustomerById(customerMemberId);
		String currency = curMember.getDefCurrency();
		if (null == currency || "".equals(currency))
			currency = CommonConstants.DEF_CURRENCY;
		MemberSsoVO ssoVO=getLoginUserSSO(request);

		// 测试数据
		// ifaid ='40280a25559f785501559f79211a0002'
		// ifaMemberId = "40280a25559b852e01559b8615da0002";
		// customerMemberId = "40280ad65601004c0156010188370004";
		
		jsonPaging = this.getJsonPaging(request);
		JsonPaging paging=jsonPaging;
		paging.setList(null);
		List<PortfolioAssetsVO> portfolioList=portfolioHoldService.getPortfolioAssets(customer.getMember().getId(),"",ssoVO.getIfaId(),"",currency);
		if(null!=portfolioList && !portfolioList.isEmpty()){
			double totalAssets=0;
			Iterator it=portfolioList.iterator();
			while (it.hasNext()) {
				PortfolioAssetsVO vo = (PortfolioAssetsVO) it.next();
				double assets=null!=vo.getTotalAssets() && !"".equals(vo.getTotalAssets())?Double.valueOf(vo.getTotalAssets()):0;
				totalAssets+=assets;
			}
			if(totalAssets>0){
				Iterator iterator=portfolioList.iterator();
				while (iterator.hasNext()) {
					PortfolioAssetsVO vo = (PortfolioAssetsVO) iterator.next();
					double assets=null!=vo.getTotalAssets() && !"".equals(vo.getTotalAssets())?Double.valueOf(vo.getTotalAssets()):0;
				    String weight=StrUtils.getNumberString(assets/totalAssets*100, "###0.00");
				    vo.setAssetRate(Double.valueOf(weight));
				}
			}
		}
		paging.setList(portfolioList);
		
		this.toJsonString(response, paging);
		return null;
	}

	/**
	 * 投资组合列表
	 */
	@RequestMapping(value = "/orderListJson", method = RequestMethod.POST)
	public String getOrderListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// MemberAdmin memberAdmin = (MemberAdmin)
		// request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);

		String langCode = this.getLoginLangFlag(request);
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String customerMemberId = request.getParameter("customerMemberId");
		CrmCustomer customer = crmCustomerService.findCustomerById(customerMemberId);
		String period = request.getParameter("period");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String status = request.getParameter("status");

		OrderHistory orderHistory = new OrderHistory();
		if ("1day".equals(period)) {
			beginDate = DateUtil.getCurDateStr();
			orderHistory.setBeginDate(DateUtil.getDate(beginDate));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("1week".equals(period)) {
			orderHistory.setBeginDate(DateUtil.getInternalDateByDay(DateUtil.getCurDate(), -7));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("1month".equals(period)) {
			orderHistory.setBeginDate(DateUtil.getInternalDateByMon(DateUtil.getCurDate(), -1));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("2month".equals(period)) {
			orderHistory.setBeginDate(DateUtil.getInternalDateByMon(DateUtil.getCurDate(), -2));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("6month".equals(period)) {
			orderHistory.setBeginDate(DateUtil.getInternalDateByMon(DateUtil.getCurDate(), -6));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("1year".equals(period)) {
			orderHistory.setBeginDate(DateUtil.getInternalDateByYear(DateUtil.getCurDate(), -1));
			orderHistory.setEndDate(DateUtil.getCurDate());
		} else if ("other".equals(period)) {
			if (null != beginDate && !"".equals(beginDate)) {
				orderHistory.setBeginDate(DateUtil.getDate(beginDate));
			}
			if (null != endDate && !"".equals(endDate)) {
				orderHistory.setEndDate(DateUtil.getInternalDateByDay(DateUtil.getDate(endDate), 1));
			}
		}

		orderHistory.setStatus(status);

		// 测试数据
		// ifaid ='40280a25559f785501559f79211a0002'
		// ifaMemberId = "40280a25559b852e01559b8615da0002";
		// customerMemberId = "40280ad65601004c0156010188370004";

		jsonPaging = this.getJsonPaging(request);
		jsonPaging = orderHistoryService.findOrderHistoryListForIfa(jsonPaging, langCode, ifaMemberId, customer.getMember().getId(), orderHistory);
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 我的客户选择页面
	 * 
	 * @author mqzou
	 * @date 2016-09-23
	 */
	@RequestMapping(value = "/dialogSelectClient")
	public String dialogSelectClient(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase memberBase = getLoginUser(request);
		if (null != memberBase) {
			String elementId = request.getParameter("class");
			MemberIfa ifa = memberBaseService.findIfaMember(memberBase.getId());
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = crmCustomerService.findCustomerByIfa(jsonPaging, ifa);
			List<CrmSelectVO> list = new ArrayList<CrmSelectVO>();
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CrmCustomer crm = (CrmCustomer) it.next();
				CrmSelectVO vo = new CrmSelectVO();
				if (null != crm.getNickName() && "" != crm.getNickName()) {
					vo.setNickName(crm.getNickName());
				} else {
					vo.setNickName(crm.getMember().getNickName());
				}
				if (null != crm.getIconUrl() && "" != crm.getIconUrl()) {
					vo.setIconUrl(crm.getIconUrl());
				} else {
					if (null != crm.getMember().getIconUrl() && "" != crm.getMember().getIconUrl()) {
						vo.setIconUrl(crm.getMember().getIconUrl());
					} else {
						vo.setIconUrl("");
					}
				}
				vo.setId(crm.getId());
				vo.setMemberId(crm.getMember().getId());
				list.add(vo);
			}
			model.put("crmList", list);
			model.put("total", jsonPaging.getTotal());
			model.put("class", elementId);
		}

		return "ifa/crm/dialogSelectClient";
	}

	/**
	 * 分页获得分组
	 * 
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@RequestMapping(value = "/crmGroupListJson", method = RequestMethod.POST)
	public String getCrmGroupListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// MemberBase curMember = this.getLoginUser(request);
		// String ifaMemberId = curMember.getId();

		String ifaId = "";
		MemberSsoVO ssoVO = this.getLoginUserSSO(request);
		if (null == ssoVO)
			return null;

		if (2 == ssoVO.getMemberType() && 21 == ssoVO.getSubMemberType()) {
			ifaId = ssoVO.getIfaId();
		}
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = crmCustomerService.findGroupList(jsonPaging, ifaId);
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 保存分组信息
	 * 
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@RequestMapping(value = "/saveCrmGroup", method = RequestMethod.POST)
	public String saveCrmGroup(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String ifaId = "";
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();

		MemberSsoVO ssoVO = this.getLoginUserSSO(request);
		if (null != ssoVO && 2 == ssoVO.getMemberType() && 21 == ssoVO.getSubMemberType()) {
			ifaId = ssoVO.getIfaId();
		}
		String groupId = request.getParameter("groupId");
		String groupName = request.getParameter("groupName");
		CrmCustomerGroup group = new CrmCustomerGroup();
		if (!"".equals(groupId)) {
			group = crmCustomerService.findGroupById(groupId);
		}

		if (null == group || "".equals(group.getId())) {
			MemberIfa ifa = memberBaseService.findIfaMember(ifaMemberId);
			ifa.setId(ifaId);
			group = new CrmCustomerGroup();
			group.setId("");
			Date date = new Date();
			group.setCreateTime(date);
			group.setIfa(ifa);
			group.setIsValid("1");
		}

		group.setGroupName(groupName);
		group.setLastUpdate(new Date());

		Boolean flag = true;
		CrmCustomerGroup crmCustomerGroup = crmCustomerService.saveGroup(group);
		if (null != crmCustomerGroup) {
			flag = true;
		} else {
			flag = false;
		}

		Map<String, Object> statuts = new HashMap<String, Object>();
		statuts.put("result", flag);
		JsonUtil.toWriter(statuts, response);

		return null;
	}

	/**
	 * 删除分组信息
	 * 
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@RequestMapping(value = "/deleteCrmGroup", method = RequestMethod.POST)
	public String deleteCrmGroup(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String groupId = request.getParameter("groupId");
		Boolean flag = crmCustomerService.deleteGroup(groupId);

		Map<String, Object> statuts = new HashMap<String, Object>();
		statuts.put("result", flag);
		JsonUtil.toWriter(statuts, response);

		return null;
	}

	/**
	 * 保存用户分组关系信息
	 * 
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@RequestMapping(value = "/saveCrmGroupRelationship", method = RequestMethod.POST)
	public String saveCrmGroupRelationship(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String groupId = request.getParameter("groupId");
		String customerId = request.getParameter("customerId");

		CrmCustomer customer = crmCustomerService.findCustomerById(customerId);
		CrmCustomerGroup group = crmCustomerService.findGroupById(groupId);
		CrmCustomerGroupRelationship relationship = crmCustomerService.findGroupRelationshipByCustomerId(customerId);

		if (null == relationship) {
			relationship = new CrmCustomerGroupRelationship();
			relationship.setId("");
			relationship.setCustomer(customer);
			relationship.setGroup(group);
			crmCustomerService.saveGroupRelationship(relationship);
		} else {
			if(null != relationship.getGroup() && relationship.getGroup().getId().equals(groupId)){
				crmCustomerService.deleteGroupRelationship(relationship.getId());
			}else {
				relationship.setCustomer(customer);
				relationship.setGroup(group);
				crmCustomerService.saveGroupRelationship(relationship);
			}
			
		}

		Boolean flag = true;

		Map<String, Object> statuts = new HashMap<String, Object>();
		statuts.put("result", flag);
		JsonUtil.toWriter(statuts, response);

		return null;
	}

	/**
	 * 我的客户个性设置
	 * 
	 * @author wwluo
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/characterSetting", method = RequestMethod.POST)
	public void characterSetting(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		String lang = this.getLoginLangFlag(request);
		CrmCustomer customer = null;
		CrmCustomerVO crmCustomerVO = new CrmCustomerVO();
		String customerId = request.getParameter("customerId");
		if (StringUtils.isNotBlank(customerId)) {
			customer = crmCustomerService.findCustomerById(customerId);
			if (customer != null) {
				BeanUtils.copyProperties(customer, crmCustomerVO);
				String character = customer.getCharacter();
				List<GeneralDataVO> clientCharacters = this.findSysParameters("client_character", lang);
				List<GeneralDataVO> hobbyTypes = this.findSysParameters("hobby_type", lang);
				if (StringUtils.isNotBlank(character)) {
					Map<String, Object> map = parameterConversion(clientCharacters, character);
					character = (String) map.get("character");
					crmCustomerVO.setCharacterName(character);
					result.put("clientCharacters", map.get("newGeneral"));
				} else {
					result.put("clientCharacters", clientCharacters);
				}
				String hobby = customer.getHobby();
				if (StringUtils.isNotBlank(hobby)) {
					Map<String, Object> map = parameterConversion(hobbyTypes, hobby);
					hobby = (String) map.get("character");
					crmCustomerVO.setHobbyName(hobby);
					result.put("hobbyTypes", map.get("newGeneral"));
				} else {
					result.put("hobbyTypes", hobbyTypes);
				}
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("customer", crmCustomerVO);
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 个性设置code参数转换
	 * 
	 * @author wwluo
	 * @date 2016-11-16
	 */
	private Map<String, Object> parameterConversion(List<GeneralDataVO> generalDataVOs, String character) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (generalDataVOs != null && !generalDataVOs.isEmpty()) {
			List<GeneralDataVO> newGeneralDataVOs = new ArrayList<GeneralDataVO>();
			StringBuffer characterBuffer = new StringBuffer();
			if (character.indexOf(",") > -1) {
				String[] characters = character.split(",");
				for (int i = 0; i < characters.length; i++) {
					boolean isParameter = false;
					for (GeneralDataVO parameter : generalDataVOs) {
						if (characters[i].equals(parameter.getItemCode())) {
							characterBuffer.append(parameter.getName() + ",");
							isParameter = true;
						}
					}
					if (!isParameter) {
						characters[i] = characters[i].replaceAll("\\{", "");
						characters[i] = characters[i].replaceAll("\\}", "");
						characterBuffer.append(characters[i] + ",");
					}
				}
				for (GeneralDataVO parameter : generalDataVOs) {
					boolean isSelect = false;
					for (int i = 0; i < characters.length; i++) {
						if (characters[i].equals(parameter.getItemCode())) {
							isSelect = true;
							break;
						}
					}
					if (!isSelect) {
						GeneralDataVO newParameter = new GeneralDataVO();
						BeanUtils.copyProperties(parameter, newParameter);
						newGeneralDataVOs.add(newParameter);
					}
				}
				map.put("character", characterBuffer.substring(0, characterBuffer.length() - 1));
				map.put("newGeneral", newGeneralDataVOs);
				return map;
			} else {
				boolean isParameter = false;
				for (GeneralDataVO parameter : generalDataVOs) {
					if (character.equals(parameter.getItemCode())) {
						characterBuffer.append(parameter.getName());
						isParameter = true;
					} else {
						GeneralDataVO newParameter = new GeneralDataVO();
						BeanUtils.copyProperties(parameter, newParameter);
						newGeneralDataVOs.add(newParameter);
					}
				}
				if (!isParameter) {
					character = character.replaceAll("\\{", "");
					character = character.replaceAll("\\}", "");
					characterBuffer.append(character);
				}
				map.put("character", characterBuffer.toString());
				map.put("newGeneral", newGeneralDataVOs);
				return map;
			}
		} else {
			return map;
		}
	}

	/**
	 * 保存我的客户个性设置
	 * 
	 * @author wwluo
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/saveCharacterSetting", method = RequestMethod.POST)
	public void saveCharacterSetting(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		CrmCustomer customer = null;
		String customerId = request.getParameter("customerId");
		if (StringUtils.isNotBlank(customerId)) {
			customer = crmCustomerService.findCustomerById(customerId);
			if (customer != null) {
				String character = request.getParameter("character");
				String hobby = request.getParameter("hobby");
				String special = request.getParameter("special");
				String dislike = request.getParameter("dislike");
				customer.setCharacter(character);
				customer.setHobby(hobby);
				customer.setSpecial(special);
				customer.setDislike(dislike);
				customer = crmCustomerService.saveCustomer(customer);
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("customer", customer);
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 分页获得记录
	 */
	@RequestMapping(value = "/prospectListJson", method = RequestMethod.POST)
	public String prospectListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		if (null == curMember)
			return null;
		String ifaMemberId = curMember.getId();
		String langCode = this.getLoginLangFlag(request);
//		String period = request.getParameter("period");
		String saleStageId = request.getParameter("saleStageId");
		String keyword = request.getParameter("keyword");

		List<CrmProspectCustomerVO> voList = crmCustomerService.findPropectCustomerList(langCode, ifaMemberId, saleStageId, keyword);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", voList.size());
		result.put("rows", voList);
		JsonUtil.toWriter(result, response);

		return null;
	}

	/**
	 * 分页获得记录
	 */
	@RequestMapping(value = "/existingListJson", method = RequestMethod.POST)
	public String existingListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String ifaMemberId = curMember.getId();
		String langCode = this.getLoginLangFlag(request);
//		String period = request.getParameter("period");
		String clientStatus = request.getParameter("clientStatus");
		String keyword = request.getParameter("keyword");
		String toCurrency = request.getParameter("toCurrency");
		if (null == toCurrency || "".equals(toCurrency)) {
			toCurrency = CommonConstants.DEF_CURRENCY;
		}
		List<CrmExistingCustomerVO> voList = crmCustomerService.findExistingCustomerList(langCode, ifaMemberId, clientStatus, keyword, toCurrency);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", voList.size());
		result.put("rows", voList);
		JsonUtil.toWriter(result, response);

		return null;
	}

	/**
	 * 保存客户昵称
	 * 
	 * @author mqzou 2017-01-02
	 */
	@RequestMapping(value = "/saveNickName", method = RequestMethod.POST)
	public void saveCusNickName(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String nickName = request.getParameter("nickName");
		MemberSsoVO ssoVO = getLoginUserSSO(request);
		CrmCustomer customer = crmCustomerService.findCustomerById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != customer) {
			if (customer.getIfa().getId().equals(ssoVO.getIfaId())) {
				customer.setNickName(nickName);
				customer.setLastUpdate(DateUtil.getCurDate());
				crmCustomerService.saveCustomer(customer);
				result.put("result", true);
			} else {
				result.put("result", false);
			}
		} else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}

}
