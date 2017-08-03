/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.action.front;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.MemberValidateInfo;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.PersonalInfoVO;

/**
 * @author michael
 * @date 20160623
 *	前台登陆
 */
@Controller
@RequestMapping("/front/member/personal")
public class FrontPersonalInfoAct  extends CoreBaseAct{

	public static final String COOKIE_USER_CODE = "_COOKIE_USER_CODE_";
	public static final String COOKIE_REMEMBER_ME = "_COOKIE_REMEMBER_ME_";
	
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private MemberBaseService memberBaseService;
    @Autowired
    private MemberManageServiceForConsole memberManageService;
    @Autowired
    private SysCountryService sysCountryService;
//    @Autowired
//    private SysParamService sysParamService;
    @Autowired
    private CrmCustomerService customerService;
//    @Autowired
//    private PortfolioHoldService portfolioHoldService;
    @Autowired
    private SysParamService sysParamService;
	/**
	 * 打开重置密码页面
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/resetpswstart", method = RequestMethod.GET)
	public String resetPassStart(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		this.saveOperLog(request,"","",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "打开重置密码页面");
		
		return "member/personal/resetpswstart";
	}

	/**
	 * 获取临时密码
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/sendtemppass", method = RequestMethod.POST)
	public void sendTempPass(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String email = request.getParameter("email");
        model.put("email", email);
		
		result.put("result",true);
		result.put("msg","");
		
		StringBuffer errMsg = new StringBuffer();
		String activeCode = StrUtils.getRandomString(6);//获取6位随机码
		ResultWS sendResult = sendResetPasswordEmail(email, activeCode);
		errMsg.append(sendResult.getErrorMsg());
		if(WSConstants.FAIL.equals(sendResult.getRet())){
			result.put("result",false);
			this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码失败");
		}else{
			errMsg.append(PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "发送临时密码成功");
		}
		result.put("msg",errMsg.toString());

		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * @author michael
	 * @date 20160712
	 * 发送重置密码邮件接口
	 * @param email	邮件接受地址
	 * @param activeCode 随机6位字符串（字母数字）
	 * @return 结果代码
	 * */
	private ResultWS sendResetPasswordEmail(String email, String validateCode) {
		return memberBaseService.saveAndSendValidateEmail("", email, 2, validateCode, validateCode);
	}
	
	/**
	 * 打开重置密码页面
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/goresetpsw", method = RequestMethod.GET)
	public String goResetPass(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		Map<String, Object> result = new HashMap<String, Object>();
		String email = request.getParameter("email");
        model.put("email", email);
		this.saveOperLog(request,"","",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "打开重置密码页面");
		return "member/personal/resetpsw";
	}
	
	/**
	 * 重置密码，进入完成页面
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/resetpsw", method = RequestMethod.POST)
	public void resetPass(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String email = StrUtils.getString(request.getParameter("email"));
		model.put("email", email);
		String tmpPass = StrUtils.getString(request.getParameter("tmpPass"));
		String newPass = StrUtils.getString(request.getParameter("newPass"));
		
		result.put("result",true);
		result.put("msg","");
		StringBuffer errMsg = new StringBuffer();
        //校验临时密码，并保存新密码
		if ("".equals(email)){
			result.put("result",false);
			errMsg.append("The email is required.<br>");
		}
		if ("".equals(tmpPass)){
			result.put("result",false);
			errMsg.append("The temporary password is required.<br>");
		}
		if ("".equals(newPass)){
			result.put("result",false);
			errMsg.append("The new password is required.<br>");
		}
		
		String md5password = "";
		if (errMsg.length()==0){//参数校验通过
			//获取用户信息
			MemberBase userBase = memberBaseService.findByEmail(email);
			if (null!=userBase){
				//检测用户状态
				if (null!=userBase.getStatus() && "1".equals(userBase.getStatus())){
					
					//检测临时密码是否正确
					try {
						List<MemberValidateInfo> infolist = memberBaseService.findMemberValidateInfo("", email, 2);
						if (!infolist.isEmpty()){
							MemberValidateInfo validateInfo = infolist.get(0);
							//校验码错误，或已过期
							if (!validateInfo.getValidateCode().equalsIgnoreCase(tmpPass)||(null!=validateInfo.getExpireTime() && validateInfo.getExpireTime().compareTo(new Date())<=0)){
								result.put("result",false);
								errMsg.append("Temporary password is wrong or expired.");
							}else{
								md5password = this.pwdEncoder.encodePassword(newPass);
								//新密码不能与旧密码相同
								if(md5password.equals(userBase.getPassword())){
									result.put("result",false);
									errMsg.append("New password canot be the same with old password.");
								}else{
									//保存密码
									userBase.setPassword(md5password);
									//清空登录失败次数/锁定状态/锁定日期
									userBase.setLoginCount(0);
									userBase.setLockStatus("0");
									userBase.setLockDate(null);
									memberBaseService.saveOrUpdate(userBase);
									errMsg.append(PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
									
								}
							}
						}else{
							result.put("result",false);
							errMsg.append("No validate record.");
						}
					} catch (Exception e) {
						// TODO: handle exception
						result.put("result",false);
						errMsg.append("Temporary password check error.");
					}
				}else{
					result.put("result",false);
					errMsg.append("The user is inactive.");
				}
			}else{
				result.put("result",false);
				errMsg.append("The user is not exist.");
			}
		}
		result.put("msg",errMsg.toString());
		model.put("errMsg", errMsg.toString());
		
		if ((Boolean)result.get("result")){
			this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "重置密码完成");
		}else{
			this.saveOperLog(request,email,"",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "重置密码失败");
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	

	/**
	 * 打开重置密码成功页面
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/resetpswok", method = RequestMethod.GET)
	public String resetPassOk(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		Map<String, Object> result = new HashMap<String, Object>();
        model.put("email", StrUtils.getString(request.getParameter("email")));
        model.put("errMsg", StrUtils.getString(request.getParameter("errMsg")));
        
		this.saveOperLog(request,"","",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "打开重置密码成功页面");
		
		return "member/personal/resetpswok";
	}
	
	/**
	 * 打开重置密码失败页面
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/resetpswfail", method = RequestMethod.GET)
	public String resetPassFail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		Map<String, Object> result = new HashMap<String, Object>();
        model.put("email", StrUtils.getString(request.getParameter("email")));
        model.put("errMsg", StrUtils.getString(request.getParameter("errMsg")));
        
		this.saveOperLog(request,"","",CoreConstants.FRONT_LOG_PERSONAL_RESET_PASS, "打开重置密码失败页面");
		
		return "member/personal/resetpswfail";
	}
	
	/**
	 * 修改密码页面加载
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/changepsw", method = RequestMethod.GET)
	public String changePass(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String id = loginUser.getId();//request.getParameter("id");
	        model.put("id", id);
	        MemberBase userBase = memberBaseService.findById(id);
			String loginCode = userBase==null?"":userBase.getLoginCode();
			this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_PERSONAL_INFO, "修改密码页面");
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/changepsw";
	}
	
	/**
	 * 修改密码页面--保存密码
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/savepass", method = RequestMethod.POST)
	public void savePass(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser!=null){
			StringBuffer errMsg = new StringBuffer();
			String loginCode = "";
			
			String id = loginUser.getId();//StrUtils.getString(request.getParameter("id"));
	        model.put("id", id);
	        
	        String oldpsw = StrUtils.getString(request.getParameter("oldpsw"));
	        String password = StrUtils.getString(request.getParameter("password"));
	        
	        if ("".equals(id)){
	        	errMsg.append("ID is required.<br>");
	        }
	        if ("".equals(oldpsw)){
	        	errMsg.append("Old password is required.<br>");
	        }
	        if ("".equals(password)){
	        	errMsg.append("New password is required.<br>");
	        }
	        
	        if (errMsg.toString().length()>0){
	        	result.put("result",false);
	        	
	        }else{
	        	String md5password = "";
		        MemberBase userBase = memberBaseService.findById(id);
				loginCode = userBase==null?"":userBase.getLoginCode();
				if (null!=userBase){
					//检测用户状态
					if (null!=userBase.getStatus() && "1".equals(userBase.getStatus())){
						//用户状态正常
						md5password = this.pwdEncoder.encodePassword(oldpsw);
				        //检测旧密码是否正确
				        if (!md5password.equalsIgnoreCase(userBase.getPassword())){
				        	errMsg.append("Old password is wrong.<br>");
				        }else{
				        
					        //保存新密码
					        md5password = this.pwdEncoder.encodePassword(password);
					        userBase.setPassword(md5password);
					        userBase = memberBaseService.saveOrUpdate(userBase);
							
							result.put("result",true);
				        }
						
						//errMsg.append(PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
					}else{
						result.put("result",false);
						errMsg.append("The user is inactive.<br>");
					}
				}else{
					result.put("result",false);
					errMsg.append("The user is not exist.<br>");
				}
				
	        }
	//		result.put("msg",errMsg.toString());
	        
	        if (errMsg.toString().length()>0){
	        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_PERSONAL_INFO, "保存密码失败");
	        	result.put("msg",errMsg.toString());
	        }else{
	        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_PERSONAL_INFO, "保存密码成功");
	        	result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
	        }
	        
			ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
			ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
		}
	}
	
	/**
	 * 获取个人信息页面加载
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String langCode = this.getLoginLangFlag(request);
			
			String userId = loginUser.getId();//request.getParameter("id");
	        model.put("id", userId);
	        
	        PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(userId, langCode);
	        if (null!=personalInfo && null!=personalInfo.getBaseInfo() && (null==personalInfo.getBaseInfo().getLangCode() || personalInfo.getBaseInfo().getLangCode().isEmpty())){
	        	
	        	personalInfo.getBaseInfo().setLangCode(langCode);
	        }
	        String curName=sysParamService.findNameByCode(personalInfo.getBaseInfo().getDefCurrency(), langCode);
	        personalInfo.setCurrencyName(curName);
	       /* if(null!=personalInfo){
	        	String gender=null!=personalInfo.getIndividualPerson()?personalInfo.getIndividualPerson().getGender():personalInfo.getIfaPerson().getGender();
	        	String urlString=personalInfo.getBaseInfo().getIconUrl();
//	        	personalInfo.getBaseInfo().setIconUrl(getUserHeadUrl(urlString,gender));
	        }*/
	        	
	       
	        
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
	        tempList=itemList;
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getLiveRegion(),"liveRegion");
	        model.put("liveRegion", itemList);
	        tempList=getSelectList(tempList, itemList);
	        model.put("allLiveRegion", tempList);

	        
	        String loginCode = personalInfo.getBaseInfo().getLoginCode();
	        model.put("personalInfo", personalInfo);
	       
	        
	        String privacy=personalInfo.getBaseInfo().getPrivacySetting();
	        HashMap<String, String> privacySetting=getPrivacySettings(privacy);
	        model.put("privacySetting",JsonUtil.toJson(privacySetting) );
	        
	        String dateFormat=personalInfo.getBaseInfo().getDateFormat();
	        if(null==dateFormat || "".equals(dateFormat))
	        	dateFormat=CommonConstants.FORMAT_DATE;
	        model.put("dateFormat", dateFormat);
	        model.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);
	        
	        model.put("memberType", loginUser.getMemberType());
	        
			this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_PERSONAL_INFO, "打开个人信息页面");
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/personalInfo";
	}
	
	
	
	/**
	 * 获取个人设置的信息
	 * @author mqzou 20170119
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/personalInfo", method = RequestMethod.POST)
	public void getPersonalInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
             String langCode = this.getLoginLangFlag(request);
			
			String userId = loginUser.getId();//request.getParameter("id");
			result.put("id", userId);
	        
	        PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(userId, langCode);
	        if(null==personalInfo.getBaseInfo().getNickName() || "".equals(personalInfo.getBaseInfo().getNickName()) ){
	        	personalInfo.getBaseInfo().setNickName(personalInfo.getBaseInfo().getLoginCode());
	        }
	        String dateFormat=personalInfo.getBaseInfo().getDateFormat();
	        if(null==dateFormat || "".equals(dateFormat))
	        	dateFormat=CommonConstants.FORMAT_DATE;
	        result.put("dateFormat", dateFormat);
	        result.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);
            personalInfo.setLastLogin(DateUtil.dateToDateString(personalInfo.getBaseInfo().getLoginTime(), dateFormat+" "+CommonConstants.FORMAT_TIME));
	        
            String curName=sysParamService.findNameByCode(personalInfo.getBaseInfo().getDefCurrency(), langCode);
	        personalInfo.setCurrencyName(curName);
	        
	        List<GeneralDataVO> itemList = new ArrayList<GeneralDataVO>();
	        List<GeneralDataVO> tempList=new ArrayList<GeneralDataVO>();

	
	        //兴趣爱好
	        itemList = findSysParameters("hobby_type", langCode);
	        tempList=itemList;
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getHobbyType(),"hobbyList");
	        result.put("hobbyList", itemList);
	        tempList=getSelectList(tempList, itemList);
	        result.put("allHobbyList", tempList);
	        
	        
	        itemList = findSysParameters("invest_field", langCode);
	        tempList=itemList;
	        //model.put("allInvestField", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getInvestField(),"investField");
	        result.put("investField", itemList);
	        tempList=getSelectList(tempList, itemList);
	        result.put("allInvestField", tempList);
	        
	        itemList = findSysParameters("investment_style", langCode);
	        tempList=itemList;
	      //  model.put("allInvestStyle", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getInvestStyle(),"investStyle");
	        result.put("investStyle", itemList);
	        tempList=getSelectList(tempList, itemList);
	        result.put("allInvestStyle", tempList);
	        
	        
	        itemList = findSysParameters("service_language", langCode);
	        tempList=itemList;
	       // model.put("languageList", itemList);
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getLanguageSpoken(),"languageSpoken");
	        result.put("languageSpoken", itemList);
	        tempList=getSelectList(tempList, itemList);
	        result.put("languageList", tempList);

	       
	        itemList = findSysParameters("service_region", langCode);
	       // model.put("allLiveRegion", itemList);
	        tempList=itemList;
	        itemList = getMyGeneralList(itemList, personalInfo.getBaseInfo().getLiveRegion(),"liveRegion");
	        result.put("liveRegion", itemList);
	        tempList=getSelectList(tempList, itemList);
	        result.put("allLiveRegion", tempList);
	        
	        result.put("personalInfo", personalInfo);

	        String privacy=personalInfo.getBaseInfo().getPrivacySetting();
	        HashMap<String, String> privacySetting=getPrivacySettings(privacy);
	        result.put("privacySetting",JsonUtil.toJson(privacySetting) );
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取个人信息页面加载
	 */
	@RequestMapping(value = "/myProfile", method = RequestMethod.GET)
	public String myProfile(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String langCode = this.getLoginLangFlag(request);
			
			String userId = loginUser.getId();//request.getParameter("id");
	        model.put("id", userId);
	        
	        PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(userId, langCode);
	        if (null!=personalInfo && null!=personalInfo.getBaseInfo()&& StringUtils.isBlank(personalInfo.getBaseInfo().getLangCode())){
	        	personalInfo.getBaseInfo().setLangCode(langCode);
	        }
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

	        
	        String loginCode = personalInfo.getBaseInfo().getLoginCode();
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
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/myProfile";
	}
	
	/**
	 * 更新当前session信息
	 * @author mqzou 2017-01-19
	 * @param request
	 */
	private void updateLoginUser(HttpServletRequest request){
		MemberBase loginUser=getLoginUser(request);
		MemberBase memberBase=memberBaseService.findById(loginUser.getId());
		loginUser=memberBase;
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		BeanUtils.copyProperties(loginUser, ssoVO);
		request.getSession().setAttribute(CoreConstants.FRONT_USER_SSO,ssoVO);
		request.getSession().setAttribute(CoreConstants.FRONT_USER_LOGIN,loginUser);

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
	 * 从基础参数列表中获取非指定编码的参数对象
	 * @param request
	 * @param response
	 * @param model
	 */
	private List<GeneralDataVO> getNonMyGeneralList(List<GeneralDataVO> list, String codes){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty()){
			if (codes!=null && codes.length()>0){
				List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
				for (GeneralDataVO v: list){
					if (!codeArr.contains(v.getItemCode()))
						result.add(v);
				}
			}else{
				result = list;
			}
		}
		return result;
	}
	
	/**
	 * 获取国家列表数据
	 * @param request
	 * @param response
	 * @param model
	 */
    @RequestMapping(value = "/getCountryList", method = RequestMethod.POST)
    public void getCountryList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String lang = this.getLoginLangFlag(request);
    	
        String keyword = toUTF8String(request.getParameter("keyword"));
        model.put("keyword", keyword);

        List<GeneralDataVO> countryList = memberBaseService.findCountryList(keyword, lang);
        model.put("countryList", countryList);
        
		ResponseUtils.renderHtml(response,JsonUtil.toJson(countryList));
    }
    
	/**
	 * 第三步个人信息保存
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/savePersonalInfo",method=RequestMethod.POST)
	public void savePersonalInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (loginUser!=null){
			try {
				
				String inputData = request.getParameter("inputData").replace("amp;", "");
		//		Map<String ,Object> baseMap = new HashMap<String ,Object>();
		//		baseMap.put("memberType", memberType);//0平台级用户,1-投资人，2-IFA,3-代理商
		//		baseMap.put("subMemberType", subMemberType);//11:独立投资账户;12:公司投资账户;13:FI账户;21:IFA个人,22:ifafirm;31:distributor',
		//		String memberId="";
				
				String[] inputDataList = inputData.split("&");
				Map<String,Object> memberMap = new HashMap<String,Object>();
				for(String memberStr:inputDataList){
					String[] memberStrArray = memberStr.split("=");
					memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
				}
				JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
				//保存基本信息
				MemberBase userBase = memberBaseService.findById(loginUser.getId());
			
				userBase.setNickName(getJsonObjectString(memberDataJSON,"nickName"));
				userBase.setMobileCode(getJsonObjectString(memberDataJSON,"mobileCode"));
				userBase.setMobileNumber(getJsonObjectString(memberDataJSON,"mobileNumber"));
				userBase.setWebchatCode(getJsonObjectString(memberDataJSON,"webchatCode"));
				userBase.setFacebookCode(getJsonObjectString(memberDataJSON,"facebookCode"));
				userBase.setLinkedInCode(getJsonObjectString(memberDataJSON,"linkedInCode"));
				userBase.setWeiboCode(getJsonObjectString(memberDataJSON,"weiboCode"));
				userBase.setTwitterCode(getJsonObjectString(memberDataJSON,"twitterCode"));
				userBase.setEmail(getJsonObjectString(memberDataJSON, "email"));
				userBase.setLastUpdate(new Date());
				userBase = memberBaseService.saveOrUpdate(userBase);
				
				String dateFormat=userBase.getDateFormat();
				if(null==dateFormat || "".equals(dateFormat)){
					dateFormat=CoreConstants.DATE_FORMAT;
				}
		        request.getSession().setAttribute(CoreConstants.FRONT_USER_DATE_FORMAT,dateFormat);

		        //更新会话中的名称信息
		        MemberSsoVO ssoVO = this.getLoginUserSSO(request);
		        ssoVO.setNickName(userBase.getNickName());
		        
				//保存附加信息
				
				//账户类型：0平台级用户,1-投资人,2-IFA,3-代理商
		        //帐户子类：11:独立投资账户;12:公司投资账户;13:FI账户;21:IFA个人,22:ifafirm;31:distributor
		        if (null!=userBase.getMemberType() && userBase.getMemberType()==1 && null!=userBase.getSubMemberType() && userBase.getSubMemberType()==11){
		        	//个人投资者
					MemberIndividual individual = memberBaseService.findIndividualMember(loginUser.getId());
//					individual.setNickName(memberDataJSON.getString("nickName"));
					individual.setLastName(getJsonObjectString(memberDataJSON,"lastName"));
					individual.setFirstName(getJsonObjectString(memberDataJSON,"firstName"));
					individual.setNameChn(getJsonObjectString(memberDataJSON,"nameChn"));
					individual.setGender(getJsonObjectString(memberDataJSON,"gender"));
					try {
						SysCountry sc = (SysCountry)sysCountryService.findById(memberDataJSON.getString("nationId"));
						individual.setNationality(sc);
					} catch (Exception e) {
						// TODO: handle exception
					}
	//				individual.setCountry(memberDataJSON.getString("country"));
					
		//			individual.setNationality(memberDataJSON.getString("nationality"));

					individual.setCertType(getJsonObjectString(memberDataJSON,"certTypeId"));
					individual.setCertNo(getJsonObjectString(memberDataJSON,"certNo"));
					individual.setEducation(getJsonObjectString(memberDataJSON,"educationId"));
					individual.setEmployment(getJsonObjectString(memberDataJSON,"employmentId"));
					individual.setOccupation(getJsonObjectString(memberDataJSON,"occupationId"));
					individual.setAddress(getJsonObjectString(memberDataJSON,"address"));
					individual.setBorn(DateUtil.getDate(memberDataJSON.getString("born"),dateFormat));
					individual.setLastUpdate(new Date());
					individual.setMember(userBase);
					memberManageService.saveIndividual(individual, userBase);
					
					
					//更新会话中的名称信息
					String enName = individual.getFirstName() == null ? "":individual.getFirstName();
					if(individual.getLastName() != null && !"".equals(individual.getLastName())){
						enName +=" "+individual.getLastName();
					}
					String chiName = individual.getNameChn();
					if(chiName == null || "".equals(chiName)){
						chiName = enName;
					}
					if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
						if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
							ssoVO.setMemberName(enName+"("+chiName+")");
						}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
							ssoVO.setMemberName(enName);
						}else if("".equals(enName) && !"".equals(chiName)){//只有中文
							ssoVO.setMemberName(chiName);
						}
					}else{// 中文版：显示中文名，如果没，则显示英文名
						if(!"".equals(chiName)){
							ssoVO.setMemberName(chiName);
						}else{
							ssoVO.setMemberName(enName);
						}
					}
					if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
						if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
							ssoVO.setMemberName(ssoVO.getNickName());
						}else{
							ssoVO.setMemberName(ssoVO.getLoginCode());
						}
					}
					
		        }else if (null!=userBase.getMemberType() && userBase.getMemberType()==2 && null!=userBase.getSubMemberType() && userBase.getSubMemberType()==21){
		        	//IFA用户
		        	MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
//					ifa.setNickName(memberDataJSON.getString("nickName"));
					ifa.setNameChn(getJsonObjectString(memberDataJSON,"nameChn"));
					ifa.setGender(getJsonObjectString(memberDataJSON,"gender"));
					ifa.setIntroduction(getJsonObjectString(memberDataJSON,"introduction"));
					ifa.setLastName(getJsonObjectString(memberDataJSON,"lastName"));
					ifa.setFirstName(getJsonObjectString(memberDataJSON,"firstName"));
					String introduction=getJsonObjectString(memberDataJSON,"introduction");
					String investLifeBegin=getJsonObjectString(memberDataJSON,"investLifeBegin");
					try {
						SysCountry sc = (SysCountry)sysCountryService.findById(memberDataJSON.getString("nationId"));
						ifa.setNationality(sc);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(null!=investLifeBegin && !"".equals(investLifeBegin)){
						ifa.setInvestLifeBegin(DateUtil.StringToDate(investLifeBegin, dateFormat));
					}
					ifa.setIntroduction(introduction);
					ifa.setCertType(getJsonObjectString(memberDataJSON,"certTypeId"));
					ifa.setCertNo(getJsonObjectString(memberDataJSON,"certNo"));
					ifa.setEducation(getJsonObjectString(memberDataJSON,"educationId"));
					ifa.setEmployment(getJsonObjectString(memberDataJSON,"employmentId"));
					ifa.setOccupation(getJsonObjectString(memberDataJSON,"occupationId"));
					ifa.setAddress(getJsonObjectString(memberDataJSON,"address"));

					ifa.setBorn(DateUtil.getDate(memberDataJSON.getString("born"),dateFormat));
					ifa.setLastUpdate(new Date());
					ifa.setMember(userBase);
					
					ifa = memberManageService.saveIfa(ifa);

					//更新会话中的名称信息
					String enName = ifa.getFirstName() == null ? "":ifa.getFirstName();
					if(ifa.getLastName() != null && !"".equals(ifa.getLastName())){
						enName +=" "+ifa.getLastName();
					}
					String chiName = ifa.getNameChn();
					if(chiName == null || "".equals(chiName)){
						chiName = enName;
					}
					if(ssoVO.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
						if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
							ssoVO.setMemberName(enName+"("+chiName+")");
						}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
							ssoVO.setMemberName(enName);
						}else if("".equals(enName) && !"".equals(chiName)){//只有中文
							ssoVO.setMemberName(chiName);
						}
					}else{// 中文版：显示中文名，如果没，则显示英文名
						if(!"".equals(chiName)){
							ssoVO.setMemberName(chiName);
						}else{
							ssoVO.setMemberName(enName);
						}
					}
					//都为空时
					if(ssoVO.getMemberName() == null || "".equals(ssoVO.getMemberName())){
						if(ssoVO.getNickName() != null && !"".equals(ssoVO.getNickName())){
							ssoVO.setMemberName(ssoVO.getNickName());//呢称不为空
						}else{
							ssoVO.setMemberName(ssoVO.getLoginCode());//呢称也为空
						}
					}
					
					try {
						IfaExtInfo extInfo = memberBaseService.findIfaExtInfo(ifa.getId());
						if (null==extInfo || null==extInfo.getId() || "".equals(extInfo.getId()))
							extInfo = new IfaExtInfo();
						extInfo.setIfa(ifa);
						extInfo.setHighlight(getJsonObjectString(memberDataJSON,"highlight"));
						extInfo.setLastUpdate(new Date());
						memberBaseService.saveOrUpdate(extInfo);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
		        }
		        
				request.getSession().setAttribute(CoreConstants.FRONT_USER_LOGIN,userBase);
				request.getSession().setAttribute(CoreConstants.FRONT_USER_SSO,ssoVO);
				request.getSession().setAttribute(CoreConstants.FRONT_USER_NAME,StrUtils.getString(ssoVO.getNickName(),ssoVO.getLoginCode()));
		        
				result.put("result",true);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));

			}
		}else{//未登录，跳转到登录页面
//			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * 保存base用户属性
	 * @author mqzou
	 * @date 2016-11-28
	 */
	@RequestMapping(value="/saveCharacter",method=RequestMethod.POST)
	public  void saveCharacter(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=loginUser){
			try {
				MemberBase userBase=memberBaseService.findById(loginUser.getId());
				String type=request.getParameter("type");
				String codeStr=request.getParameter("codes");
				if("languageSpoken".equals(type)){
					
					userBase.setLanguageSpoken(codeStr);
				}else if ("investmentField".equals(type)) {
					
					userBase.setInvestField(codeStr);
				}else if ("hobby".equals(type)) {
					userBase.setHobbyType(codeStr);
					
				}else if ("liveRegion".equals(type)) {
					userBase.setLiveRegion(codeStr);
					
				}else if ("investmentStyle".equals(type)) {
					userBase.setInvestStyle(codeStr);
					
				}
				userBase=memberBaseService.saveOrUpdate(userBase);
				result.put("result",true);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}else {
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * 保存Ifa用户属性
	 * @author mqzou
	 * @date 2016-11-28
	 */
	@RequestMapping(value="/savePersonalIfaInfo",method=RequestMethod.POST)
	public void savePersonalIfaInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		if(null!=loginUser){
			try {
				String inputData = request.getParameter("inputData").replace("amp;", "");
				String[] inputDataList = inputData.split("&");
				Map<String,Object> memberMap = new HashMap<String,Object>();
				for(String memberStr:inputDataList){
					String[] memberStrArray = memberStr.split("=");
					memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
				}
				JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
				MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
				String showPortfolio=getJsonObjectString(memberDataJSON, "showPortfolio");
				String investLifeBegin=getJsonObjectString(memberDataJSON, "investLifeBegin");
				if("on".equals(showPortfolio)){
					ifa.setSpaceShowPerformance(getJsonObjectString(memberDataJSON, "spaceShowPortfolio"));
				}else {
					ifa.setSpaceShowPerformance("");
				}
				String spaceShowPerformance=getJsonObjectString(memberDataJSON, "spaceShowPerformance");
				ifa.setSpaceShowPerformance("on".equals(spaceShowPerformance)?"1":"0");
				String spaceShowAum=getJsonObjectString(memberDataJSON, "spaceShowAum");
				ifa.setSpaceShowAum("on".equals(spaceShowAum)?"1":"0");
				if(null!=investLifeBegin && !"".equals(investLifeBegin)){
					ifa.setInvestLifeBegin(DateUtil.StringToDate(investLifeBegin, dateFormat));
				}
				
				
				
				/*String portfolioCriticalValue=getJsonObjectString(memberDataJSON, "portfolioCriticalValue");
				if(!"".equals(portfolioCriticalValue))
				ifa.setPortfolioCriticalValue(portfolioCriticalValue);
				String portfolioReturnValue=getJsonObjectString(memberDataJSON, "portfolioReturnValue");
				if(!"".equals(portfolioReturnValue))
				ifa.setPortfolioReturnValue(portfolioReturnValue);*/
				
				ifa=memberManageService.saveIfa(ifa);
				result.put("result",true);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}else {
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * 保存Ifa用户属性
	 * @author mqzou
	 * @date 2016-11-28
	 */
	@RequestMapping(value="/savePersonalInvestInfo",method=RequestMethod.POST)
	public void savePersonalIfaInfo1(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=loginUser){
			try {
				String inputData = request.getParameter("inputData").replace("amp;", "");
				String[] inputDataList = inputData.split("&");
				Map<String,Object> memberMap = new HashMap<String,Object>();
				for(String memberStr:inputDataList){
					String[] memberStrArray = memberStr.split("=");
					memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
				}
				JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
				MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
				/*String showPortfolio=getJsonObjectString(memberDataJSON, "showPortfolio");
				if("on".equals(showPortfolio)){
					ifa.setSpaceShowPortfolio(getJsonObjectString(memberDataJSON, "spaceShowPortfolio"));
				}else {
					ifa.setSpaceShowPortfolio("");
				}
				
				String spaceShowAum=getJsonObjectString(memberDataJSON, "spaceShowAum");
				ifa.setSpaceShowAum("on".equals(spaceShowAum)?"1":"0");
				*/
				String portfolioCriticalValue=getJsonObjectString(memberDataJSON, "portfolioCriticalValue");
				if(!"".equals(portfolioCriticalValue))
				ifa.setPortfolioCriticalValue(portfolioCriticalValue);
				String portfolioReturnValue=getJsonObjectString(memberDataJSON, "portfolioReturnValue");
				if(!"".equals(portfolioReturnValue))
				ifa.setPortfolioReturnValue(portfolioReturnValue);
				
				ifa=memberManageService.saveIfa(ifa);
				result.put("result",true);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}else {
			result.put("result",true);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * 获取jsonobject的值
	 * @author mqzou
	 * @date 2016-11-25
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	private String getJsonObjectString(JSONObject jsonObject,String key){
		try {
			return jsonObject.getString(key);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 保存用户的使用习惯设置
	 * @author mqzou
	 * @date 2016-11-28
	 */
	@RequestMapping(value="/savePersonPreference",method=RequestMethod.POST)
	public void savePersonPreference(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=loginUser){
			try {
				String inputData = request.getParameter("inputData").replace("amp;", "");
				String[] inputDataList = inputData.split("&");
				Map<String,Object> memberMap = new HashMap<String,Object>();
				for(String memberStr:inputDataList){
					String[] memberStrArray = memberStr.split("=");
					memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
				}
				JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
				MemberBase userBase = memberBaseService.findById(loginUser.getId());
				userBase.setDefCurrency(getJsonObjectString(memberDataJSON, "defCurrency"));
				userBase.setDefDisplayColor(getJsonObjectString(memberDataJSON, "defDisplayColor"));
				String dateFormat=getJsonObjectString(memberDataJSON, "dateFormat").replace("+", " ");
				userBase.setDateFormat(dateFormat);
				userBase.setLangCode(getJsonObjectString(memberDataJSON, "langCode"));
				userBase.setLastUpdate(new Date());
				userBase = memberBaseService.saveOrUpdate(userBase);
				

				if(null==dateFormat || "".equals(dateFormat)){
					dateFormat=CoreConstants.DATE_FORMAT;
				}
		        request.getSession().setAttribute(CoreConstants.FRONT_USER_DATE_FORMAT,dateFormat);
		        
				updateLoginUser(request);
				result.put("result",true);
				result.put("code","global.success.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success.save",null));
			} catch (Exception e) {
				
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}
		}else {
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
		}
		
		ResponseUtils.renderHtml(response,JsonUtil.toJson(result));
	}
	
	/**
	 * 我的待办
	 * @author tejay zhu
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/myBacklog",method=RequestMethod.GET)
	public String myBacklog(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
//		Map<String, Object> result = new HashMap<String, Object>();
		
		if (loginUser!=null){
			//TODO 读取待办列表
			/*List<WebToDo> webToDos = memberBaseService.findMyWebToDoList(loginUser, "", "", "1");
			List<WebToDoToReadVO> toDolist = new ArrayList();
			WebToDo toDo = null;
			for (int i = 0; i < webToDos.size(); i++) {
				toDo = webToDos.get(i);
				
				String toDoToReadType = null;
				SysParamVO sysParamVO = sysParamService.findParamByCode(this.getLoginLangFlag(request), toDo.getToDoType());
				toDoToReadType = sysParamVO.getName();
				
				WebToDoToReadVO vo = memberBaseService.findWebToDoToRead( toDo.getId() , this.getLoginLangFlag(request), "WebToDo");
				vo.setToDoToReadType(toDoToReadType);
				vo = memberBaseService.parseWebToDoToWebToDoToReadVO(toDo, vo);
				if (null==vo.getTitle() || "".equals(vo.getTitle())) vo.setTitle(toDo.getTitle());
				toDolist.add(vo);
			}
			
			
			//TODO 读取待阅列表
			List<WebToRead> webToReads = memberBaseService.findMyWebToReadList(loginUser);
			List<WebToDoToReadVO> toReadlist = new ArrayList();
			WebToRead toRead = null;
			for (int i = 0; i < webToReads.size(); i++) {
				toRead = webToReads.get(i);
				
				String toDoToReadType = null;
				SysParamVO sysParamVO = sysParamService.findParamByCode(this.getLoginLangFlag(request), toRead.getToReadType());
				toDoToReadType = sysParamVO.getName();
				
				WebToDoToReadVO vo = memberBaseService.findWebToDoToRead( toRead.getId() , this.getLoginLangFlag(request), "WebToRead");
				vo.setToDoToReadType(toDoToReadType);
				vo = memberBaseService.parseWebToReadToWebToDoToReadVO(toRead, vo);
				toReadlist.add(vo);
			}
			
			model.put("toDolist", toDolist);
			model.put("toReadlist", toReadlist);*/
			
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/myBacklog";
	}
	
	
	/**
	 * 设置待阅为已阅
	 * @author tejay zhu
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/setToReadDone", method = RequestMethod.POST)
	public void setToReadDone(HttpServletRequest request,HttpServletResponse response,ModelMap model){
//		String id = request.getParameter("id");
		
		Map<String, Object> obj = new HashMap<String, Object>();
		//obj.put("ret", memberBaseService.setToReadDone(id) );
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 修改投资风格
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/editInvestStyle", method = RequestMethod.GET)
	public String editInvestStyle(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){

	        model.put("id", loginUser.getId());
	      //  MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
	        
	      //投资风格（IFA）
	      //  List<GeneralDataVO> itemList = findSysParameters("investment_style", this.getLoginLangFlag(request));
	       /* model.put("investStyle", ifa.getInvestStyle());
	        model.put("styleList", getNonMyGeneralList(itemList, ifa.getInvestStyle()));
	        model.put("myInvestStyle", getMyGeneralList(itemList, ifa.getInvestStyle()));*/

		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/editInvestStyle";
	}
	
	/**
	 * 保存投资风格
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/saveInvestStyle", method = RequestMethod.GET)
	public String saveInvestStyle(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			//String investStyle = request.getParameter("investStyle");
			
//			MemberBase base = memberBaseService.findById(loginUser.getId());
//			setInvestStyle(investStyle);
//			memberBaseService.saveOrUpdate(base);
			
			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
		//	ifa.setInvestStyle(investStyle);
			memberManageService.saveIfa(ifa);
		}
		return editInvestStyle(request, response, model);
	}
	
	/**
	 * 修改擅长领域
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/editExpertType", method = RequestMethod.GET)
	public String editExpertType(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			model.put("id", loginUser.getId());
	       // MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());

	        //投资领域（IFA）
	       // List<GeneralDataVO> itemList = findSysParameters("expertise_type", this.getLoginLangFlag(request));
	      /*  model.put("expertType", ifa.getExpertiseType());
	        model.put("expertList", getNonMyGeneralList(itemList, ifa.getExpertiseType()));
	        model.put("myExpertList", getMyGeneralList(itemList, ifa.getExpertiseType()));*/
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/editExpertType";
	}

	/**
	 * 保存投资风格
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/saveExpertType", method = RequestMethod.GET)
	public String saveExpertType(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			//String expertType = request.getParameter("expertType");
			
//			MemberBase base = memberBaseService.findById(loginUser.getId());
//			base.setExpertiseType(expertType);
//			memberBaseService.saveOrUpdate(base);
			
			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
		//	ifa.setExpertiseType(expertType);
			memberManageService.saveIfa(ifa);
		}
		return editExpertType(request, response, model);
	}

	/**
	 * 修改兴趣爱好
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/editHobby", method = RequestMethod.GET)
	public String editHobby(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			model.put("id", loginUser.getId());
			MemberBase base = memberBaseService.findById(loginUser.getId());

	        List<GeneralDataVO> itemList = findSysParameters("hobby_type", this.getLoginLangFlag(request));
	        model.put("hobby", base.getHobbyType());
	        model.put("hobbyList", getNonMyGeneralList(itemList, base.getHobbyType()));
	        model.put("myhobbyList", getMyGeneralList(itemList, base.getHobbyType(),"hobbyList"));
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/editHobby";
	}
	
	/**
	 * 保存兴趣爱好
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/saveHobby", method = RequestMethod.GET)
	public String saveHobby(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser!=null){
			String hobby = request.getParameter("selected_value");
			
			MemberBase base = memberBaseService.findById(loginUser.getId());
			base.setHobbyType(hobby);
			memberBaseService.saveOrUpdate(base);
		}
		return editHobby(request, response, model);
	}
	
	/**
	 * 修改个人喜好
	 * @author qgfeng
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/editPreference", method = RequestMethod.GET)
	public String editPreference(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
			MemberBase base = memberBaseService.findById(loginUser.getId());
			model.put("memberBase", base);
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/editPreference";
	}
	
	/**
	 * 保存个人喜好
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/savePreference", method = RequestMethod.POST)
	public void savePreference(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
			String defCurrency = StrUtils.getString(request.getParameter("defCurrency"));
			String langCode = StrUtils.getString(request.getParameter("langCode"));
			String defDisplayColor = StrUtils.getString(request.getParameter("defDisplayColor"));
			String dateFormat = StrUtils.getString(request.getParameter("dateFormat"));
			//String privacyContact = StrUtils.getString(request.getParameter("privacyContact"));
			MemberBase base = memberBaseService.findById(loginUser.getId());
			base.setDateFormat(dateFormat);
			base.setDefCurrency(defCurrency);
			base.setLangCode(langCode);
			base.setDefDisplayColor(defDisplayColor);
		//	base.setPrivacyContact(privacyContact);
			memberBaseService.saveOrUpdate(base);
			updateLoginUser(request);
			result.put("result", true);
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 保存个人介绍
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/saveContentByField", method = RequestMethod.POST)
	public void saveContentByField(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
			String field = StrUtils.getString(request.getParameter("field"));
			String content = StrUtils.getString(request.getParameter("content"));
			if ("introduction".equals(field)){
				MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
				ifa.setIntroduction(content);
				memberManageService.saveIfa(ifa);
			}else if ("highlight".equals(field)){
				try {
					MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
					IfaExtInfo extInfo = memberBaseService.findIfaExtInfo(ifa.getId());
					if (null==extInfo || null==extInfo.getId() || "".equals(extInfo.getId()))
						extInfo = new IfaExtInfo();
					extInfo.setIfa(ifa);
					extInfo.setHighlight(content);
					extInfo.setLastUpdate(new Date());
					memberBaseService.saveOrUpdate(extInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			result.put("result", true);
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("msg","User is not login.");
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 修改个人头像页面加载
	 * @author qgfeng
	 * @date 2016-9-30
	 * @return
	 */
	@RequestMapping(value = "/changeIcon")
	public String changeIcon(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String iconUrl = "";
		String flag = request.getParameter("flag");
		String flagId = request.getParameter("flagId");
		if (loginUser!=null){
			MemberBase member = null;
			//头像url
			if(StringUtils.isNotBlank(flag) && StringUtils.isNotBlank(flagId)){
				if("crm".equals(flag)){
					CrmCustomer crmCustomer = customerService.findCustomerById(flagId);
					iconUrl = crmCustomer.getIconUrl();
					member = crmCustomer.getMember();
				}
			}else{
				member = memberBaseService.findById(loginUser.getId());
				iconUrl = member.getIconUrl();
			}
			//性别
			if(member!=null){
				PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(member.getId(),getLoginLangFlag(request));
			    String gender = "";
				if(member.getMemberType()==1){
			    	MemberIndividual indi = personalInfo.getIndividualPerson();
			    	gender = indi.getGender();
			    }else if(member.getMemberType()==2){
			    	MemberIfa ifa = personalInfo.getIfaPerson();
			    	gender = ifa.getGender();
			    }
				model.put("gender", gender);
			}
	        model.put("flag",flag);
	        model.put("flagId",flagId);
	        model.put("iconUrl",iconUrl);
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "member/personal/changeIcon";
	}
	
	/**
	 * 保存用户头像文件
	 * @author qgfeng
	 * @date 2016-9-30
	 */
	@RequestMapping(value="/saveIcon",method=RequestMethod.POST)
	public void saveIcon(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		//String ctxPath = request.getSession().getServletContext().getRealPath("/");
		String filePath = "";
		String gender = "";
		if (loginUser!=null){
			Integer memberType = loginUser.getMemberType();
			if(1 == memberType){
				MemberIndividual member = memberBaseService.findIndividualMember(loginUser.getId());
				if (null!=member) gender=member.getGender();
			}else if(2 == memberType){
				MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
				if (null!=ifa) gender=ifa.getGender();
			}
			try {
				String flag = request.getParameter("flag");
				String flagId = request.getParameter("flagId");
				
				String oldIconPath = "";
				
				String imgCode = request.getParameter("imgCode");
				imgCode = imgCode.replace(" ", "+");
				String oriFileName = "jpg";
				filePath = UploadUtil.getFileName(oriFileName,true,"portrait");
				File localFile = new File(filePath);
				if(!localFile.getParentFile().exists()){
					localFile.getParentFile().mkdirs();
				}
				//保存图片
				UploadUtil.changeToFile(imgCode, filePath);
				//存储地址至数据库
				if(StringUtils.isNotBlank(flag) && StringUtils.isNotBlank(flagId)){
					if("crm".equals(flag)){
						CrmCustomer crmCustomer = customerService.findCustomerById(flagId);
						oldIconPath = crmCustomer.getIconUrl();
						crmCustomer.setIconUrl(filePath);
						customerService.saveCustomer(crmCustomer);
					}
				}else{
					MemberBase menber = memberBaseService.findById(loginUser.getId());
					oldIconPath = menber.getIconUrl();
					menber.setIconUrl(filePath);
					memberBaseService.saveOrUpdate(menber);
				}
				//删除旧头像
				File file = new File(oldIconPath);
				if (file.isFile() && file.exists()) {  
				    file.delete();  
				}  
				result.put("result",true);
				String path = PageHelper.getUserHeadUrl(filePath,gender);
				result.put("filePath", path);
				result.put("code","global.success");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
				//修改session
				request.getSession().setAttribute(CoreConstants.FRONT_USER_ICONURL,path);
				updateLoginUser(request);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",false);
				result.put("code","global.failed.save");
				result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed.save",null));
			}finally{
				JsonUtil.toWriter(result, response);
			}
		}else{//未登录，跳转到登录页面
			result.put("result",false);
			result.put("code","error.noLogin");
			result.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null));
			JsonUtil.toWriter(result, response);
		}
	}
	
	/**
	 * 保存用户隐私设置
	 * @author mqzou
	 * @date 2016-11-30
	 */
	@RequestMapping(value="/savePrivacySetting",method=RequestMethod.POST)
	public void privacySetting(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		Map<String, Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
			MemberBase userBase=memberBaseService.findById(loginUser.getId());
			String fieldName=request.getParameter("field");
			String status=request.getParameter("status");
			String privacy=userBase.getPrivacySetting();
			if(null!=privacy && !"".equals(privacy)){
				HashMap<String, String> map=getPrivacySettings(privacy);
				map.put(fieldName, status);
				
				String privacyStr=map.toString().replace("=", ":").replace(",", ";").replace("{", "").replace("}", "").replace(" ", "");
				 /*String str = "";//用于存放遍历出来的字符串
		       	Iterator  iterator = map.keySet().iterator();
		        while (iterator.hasNext()) {
		            //2、拼接字符串
		            str +=iterator.next()+":"+ map.get(iterator.next()).toString()+";";
		        }*/
		        userBase.setPrivacySetting(privacyStr);
		        
			}else {
				userBase.setPrivacySetting(fieldName+":"+status+";");
			}
			memberBaseService.saveOrUpdate(userBase);
			result.put("result", true);
		}else {
			result.put("result", false);
			
		}
		JsonUtil.toWriter(result, response);
		
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
	 * 旧密码验证
	 * @author wwluo
	 * @date 2016-12-19
	 */
	@RequestMapping(value="/checkOldPwd",method=RequestMethod.POST)
	public void checkOldPwd(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		Map<String, Object> result=new HashMap<String, Object>();
		boolean flag = false;
		String msg = null;
		if(null!=loginUser){
			String loginPwd = request.getParameter("oldPwd");
			String md5password = this.pwdEncoder.encodePassword(loginPwd);
			if(loginUser.getPassword().equals(md5password)){
				flag = true;
			}else{
				msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.invalidPassword",null);
			}
		}else {
			flag = false;
			msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.noLogin",null);
		}
		result.put("flag", flag);
		result.put("msg",msg);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * AE账号删除
	 * @author scshi
	 * @date 20170505
	 * **/
	@RequestMapping(value = "/aeAccountDel", method = RequestMethod.POST)
	public void aeAccountDel(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
//			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			MemberSsoVO curLogin = this.getLoginUserSSO(request);
			if (null!=curLogin && null!=curLogin.getIfaId()){
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
	
	/**
	 * ifa AE账户详细信息（添加）页面
	 * @author scshi
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/aeAccountInput")
	public String aeAccountInput(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO curLogin = this.getLoginUserSSO(request);
		if (null!=curLogin && null!=curLogin.getIfaId()){
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
			MemberIfaIfafirm ifaIfafirm = ifaManageService.findIfaIfaFirmByIfaId(ifaId, null);
			model.put("ifafirmId", (null==ifaIfafirm||null==ifaIfafirm.getIfafirm())?"":ifaIfafirm.getIfafirm().getId());
			return "console/ifa/aeaccount_input";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
	}
}
