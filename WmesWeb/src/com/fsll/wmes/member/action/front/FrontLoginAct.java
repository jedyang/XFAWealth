/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.action.front;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.sauronsoftware.base64.Base64;

import java.io.BufferedWriter;
import java.io.StringWriter;
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
import org.springframework.web.context.ContextLoader;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.CSRFTokenUtil;
import com.fsll.common.util.CookieUtils;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.SysSiteService;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.FrontMenuService;

/**
 * @author scshi
 * @date 20160623
 *	前台登陆
 */
@Controller
public class FrontLoginAct  extends CoreBaseAct{

	public static final String COOKIE_USER_CODE = "_COOKIE_USER_CODE_";
	public static final String COOKIE_REMEMBER_ME = "_COOKIE_REMEMBER_ME_";
	
	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private FrontMenuService frontMenuService;
	
	@Autowired
	private MemberManageServiceForConsole memberManageServiceForConsole;
	
	@Autowired
	private SysSiteService sysSiteService;

    //@Autowired
    //private ITraderSendService iTraderSendService;
    //@Autowired
    //private OmsAccountService omsAccountService;
    //@Autowired
    //private OmsAimaService omsAimaService;
	//@Autowired
	//protected SysLogService sysLogService;
    
	@Autowired
    private CompanyInfoService companyInfoService;
	/**
	 * 登录页面加载
	 */
	@RequestMapping(value = "/front/viewLogin")
	public String viewLogin (HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//this.saveOperLog(request,"test","",CoreConstants.FRONT_USER_LOGIN, "打开登陆页面");
		//CompanyInfo companyInfo = companyInfoService.findById("1");
        //System.out.println(companyInfo.getCode());
		//companyInfo.setCode(DateUtil.getCurDateTime());
		//companyInfoService.saveOrUpdate(companyInfo);	
		/*
		SysLog sysLog = new SysLog();
		sysLog.setLoginCode("test");
		sysLog.setNickName("test");
		sysLog.setIp(this.getRemoteHost(request));
		sysLog.setModuleType("test");
		sysLog.setRemark("test");
		sysLog.setCreateTime(new Date());
		sysLogService.saveOrUpdate(sysLog);
		*/
		//SysLog sysLog = sysLogService.findById("40280a1959ca78bf0159ca7a3b9e0000");
		//sysLog.setCreateTime(new Date());
		//sysLogService.saveOrUpdate(sysLog);	
		//String code = "833890001";//00500005 822800001
		
		//String code = "833810000";
		/*
		String code = "833890001";
		String pwd = "a123456B";
		String aeCode = "00700001";
		String type = "0";
		String sessionId = "SSMSrFZFtxgl=3Lx9L9GrSNTJerhMseIOJpbstfbLDPNypjHIQbFt6VajkigJgIrz0zJSkYmoUr7PXe";
		String tradeId = "SSMSxjXJo3vyRBUmlksN";
		String fromMemberId = "40280ad65601004c0156010188390022";//833890001
		String toMemberId = "40280ad65601004c0156010188390022";//833890001
		String pinValue = "6,2,5";
		int queryDayNum = 5;
		String startDate = "2016/05/26";
		String endDate = "2016/12/29";
		String toCode = "833890001";
		*/
		//MemberAccountSso sso = iTraderSendService.saveLogin(request,type,aeCode, pwd);
//		System.out.println(sso.getPinPos());
		//pinValue = sso.getPinPos();
		//iTraderSendService.saveLogout(request,type,toCode);
		//boolean ssof = iTraderSendService.savePin(request,type,code,pinValue);
		//System.out.println("pin chk----"+ssof);
		//iTraderSendService.saveVerify(request,code);
		//iTraderSendService.saveCurrencyAccount(request, code);//失败
		//iTraderSendService.saveAccount(request,code,toCode);
		//iTraderSendService.savePosition(request, code, toCode);
		//iTraderSendService.saveBodData(request, code,toCode);
		//iTraderSendService.saveAccountBind(request, toCode, "1", new Date());
		//iTraderSendService.saveAccountUnBind(request, toCode);
		//iTraderSendService.saveOrderHistoryByDay(request,fromMemberId,toMemberId,queryDayNum);
		//iTraderSendService.saveOrderTrade(request,fromMemberId,toMemberId);
		//iTraderSendService.saveOrderHistoryByDate(request, code,toCode,startDate, endDate);
		//omsAimaService.saveCurrencyAccount(request,code);
		
		//iTraderSendService.saveAccountPosition(request, fromMemberId, toMemberId);
		
/*		OmsQueryOrderVO queryOrderVO = new OmsQueryOrderVO();
		queryOrderVO.setCreatorNo(code);
		queryOrderVO.setAccountNo(code);
		queryOrderVO.setXfaOrderNO("170112000001");*/
		//iTraderSendService.saveQueryOrderBook(request,queryOrderVO);
		//买基金
		/*
		OmsAddOrderVO orderVO = new OmsAddOrderVO();
		orderVO.setCreatorNo(aeCode);
		orderVO.setOrderType("1");
		orderVO.setAccountNo(code);
		orderVO.setSymbolCode("TEM0043");//ABE0018 TEM0043  TEM0056 TEM0057 TEM0058 TEM0059
		orderVO.setLimitedPrice(1111.0);
		orderVO.setQuantity(1.0);
		orderVO.setTranType("0");
		orderVO.setOrderNO("40280a705a3aa699015a3aa85c740007");
		orderVO.setAeCode(aeCode);
		orderVO.setTradingPassword(pwd);
		orderVO.setTranFeeRate(5.0);
		orderVO.setTranFeeMini(50.0);
		orderVO.setSwitchFlag("0");
		iTraderSendService.addOrder(request, orderVO);
		*/
		//撤单
/*		OmsDelOrderVO orderDelVO = new OmsDelOrderVO();
		orderDelVO.setCreatorNo(code);
		orderDelVO.setOrderNO("1");
		orderDelVO.setAccountNo(code);
		orderDelVO.setAeCode(aeCode);
		orderDelVO.setOmsOrderNO("170120000001");
		orderDelVO.setTradingPassword(pwd);*/
		//iTraderSendService.deleteOrder(request, orderDelVO);
		
		//卖基金
/*		orderVO = new OmsAddOrderVO();
		orderVO.setCreatorNo(code);
		orderVO.setOrderType("1");
		orderVO.setAccountNo(code);
		orderVO.setSymbolCode("TEM0043");
		orderVO.setLimitedPrice(12.71);
		orderVO.setQuantity(300.0);
		orderVO.setTranType("1");
		orderVO.setOrderNO("1");
		orderVO.setAeCode(aeCode);
		orderVO.setTradingPassword(pwd);*/
		//iTraderSendService.saveCalOrder(request, orderVO);
		
		//股票
		/*
		OmsAddOrderVO orderVO = new OmsAddOrderVO();
		orderVO.setCreatorNo(code);
		orderVO.setOrderType("2");
		orderVO.setAccountNo(code);
		orderVO.setSymbolCode("02823");
		orderVO.setLimitedPrice(14.0);
		orderVO.setQuantity(1300.0);
		orderVO.setTranType("0");
		orderVO.setOrderNO("xfa_id_1001");
		orderVO.setAeCode(aeCode);
		orderVO.setTradingPassword(pwd);
		*/
		//iTraderSendService.addOrder(request,orderVO);
		//iTraderSendService.saveCalOrder(request, orderVO);
		
		//运营公司
		String companyCode = request.getParameter("c");
		if (StrUtils.isEmpty(companyCode)) //如果没 参数，取默认值；
			if (StrUtils.isEmpty(getSessionCompanyCode(request)))
				companyCode = PropertyUtils.getConfPropertyValue(CoreConstants.DEFAULT_COMPANY);
			else
				companyCode = getSessionCompanyCode(request);//如果已选定过运营公司，则保持选定
					
		CompanyInfoVO companyInfo = companyInfoService.findVoByCode(companyCode);
		setCompanyInfo(request, companyInfo);
//		request.getSession().setAttribute(CoreConstants.FRONT_COMPANY,companyInfo);
		
		String name = "";
		//记住账号两周
		String rememberMeValue = CookieUtils.getCookie(request,COOKIE_REMEMBER_ME);
		if(!"".equals(rememberMeValue)){
			model.put("rememberMe",rememberMeValue);
			if("1".equals(rememberMeValue)){
				name = CookieUtils.getCookie(request,COOKIE_USER_CODE);
			}
		}else{
			model.put("rememberMe","0");
		}
		String loginCode = request.getParameter("loginCode");	
		if (StringUtils.isNotBlank(loginCode)) {
			MemberBase base = memberBaseService.findByCode(loginCode);
			if(base != null){
				model.put("failCount",base.getFailCount());
			}
		}
		String errorMsg = request.getParameter("errorMsg") == null ? null : request.getParameter("errorMsg");		
		model.put("errorMsg",errorMsg);
		model.put("loginCode",name);
		model.addAttribute("csrf", CSRFTokenUtil.getTokenForSession(request.getSession()));
		this.saveOperLog(request,name,"",CoreConstants.FRONT_USER_LOGIN, "打开登陆页面");
		
		//如果当前使用的运营公司有设置登录页面，则跳转至该页面
		if (null!=companyInfo && null!=companyInfo.getId() && !StrUtils.isEmpty(companyInfo.getLoginUrl()))
			return companyInfo.getLoginUrl();
		
		return "login";//default page
	}
	
    /**
	 * 登录权限验证
	 */
	@RequestMapping(value = "/front/login" , method = RequestMethod.POST)
	public String login(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!verifyCSRFToken(request)){
			map.put("errorMsg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.csrftError",null));
			map.put("result", false);
			map.put("key", "csrf");
			JsonUtil.toWriter(map, response);
			return null;
		}
		
		String errorMsgPlus = "";
		String loginCode = request.getParameter("name");
		String loginPwd = request.getParameter("pwd");
		String vercode = request.getParameter("vercode");
		loginCode = Base64.decode(loginCode, "UTF-8");
		loginPwd = Base64.decode(loginPwd, "UTF-8");
		if(loginCode == null || loginPwd == null ||"".equals(loginCode)||"".equals(loginPwd)){
			//errorMsgPlus = this.loginFailCount(loginCode);
			this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "用户名或密码不能为空");
			map.put("errorMsg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.noEmpty",null));
			map.put("result", false);
			JsonUtil.toWriter(map, response);
			return null;
		}
		
		String randomCode = (String) request.getSession().getAttribute("randStr");
        if (null!=vercode && !checkCode(randomCode, vercode)) {
        	//errorMsgPlus = this.loginFailCount(loginCode);
        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "验证码错误");
        	map.put("errorMsg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.validCode.error",null));
        	map.put("result", false);
        	JsonUtil.toWriter(map, response);
        	return null;
        }
        
		String md5password = this.pwdEncoder.encodePassword(loginPwd);
		
		//校验帐号是否在其他PC端登录
		
		/***
		boolean isOtherPcLogin = memberBaseService.checkOtherSSOExist(loginCode,this.getRemoteHost(request),"web");
		if(isOtherPcLogin){
			model.put("loginCode", loginCode);
        	model.put("errorMsg", "不能在两台PC，用同一个账号登录");
        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "在两台PC，用同一个账号登录");
        	return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		*/
		
		// TODO 避免开发阶段ip冲突（可能会存在多个127.0.0.1 IP），暂时注释代码    
		/*
		 * //不能在同一台PC上面，用两个不同账号登录，判断当前IP是否存在token
		boolean isEnptyToken = memberBaseService.checkSSOExist(this.getRemoteHost(request), "web");
		if(isEnptyToken){
			//判断token持有是否是当前登录账号
			boolean isLogin = memberBaseService.checkSSOyLoginCode(this.getRemoteHost(request), "web",loginCode);
			if(!isLogin){
				model.put("loginCode", loginCode);
	        	model.put("errorMsg", "请先注销原来的登录，再换账号登录");
	        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "在同一台PC上面，用两个不同账号登录");
	        	return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
			}
		}*/
		// END
		
		//判断当前用户是否在此公司范围
		String companyId = getSessionCompanyId(request);
		if (!companyInfoService.checkIfValidUserByLoginCode(companyId,loginCode)){
			this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "该账号非指定运营公司客户");
			map.put("errorMsg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"member.login.noRelateWithOperatingCompany",null));
			map.put("result", false);
			JsonUtil.toWriter(map, response);
			return null;
		}			

		MemberSsoVO ssoAccount = memberBaseService.saveLoginAuth(request, loginCode, md5password, "web");
		if(ssoAccount != null && WSConstants.SUCCESS.equals(ssoAccount.getRet())){
			String rememberMe = request.getParameter("rememberMe");
			if("1".equals(rememberMe)){
				CookieUtils.addCookie(request,response,COOKIE_USER_CODE,loginCode,-1,"");
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"1",-1,"");
			}else{
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"0",-1,"");
			}
			MemberBase account = memberBaseService.findByCode(loginCode,md5password);
			request.getSession().setAttribute(CoreConstants.FRONT_USER_LOGIN,account);
			request.getSession().setAttribute(CoreConstants.FRONT_USER_LOGIN_MEMBER,account.getId());
			request.getSession().setAttribute(CoreConstants.FRONT_USER_SSO,ssoAccount);
			request.getSession().setAttribute(CoreConstants.FRONT_USER_NAME,StrUtils.getString(ssoAccount.getNickName(),ssoAccount.getLoginCode()));
			request.getSession().setAttribute(CoreConstants.LANG_CODE,(null==ssoAccount.getLangCode()?CommonConstants.DEF_LANG_CODE:ssoAccount.getLangCode().toLowerCase()));
			request.getSession().setAttribute(CoreConstants.FRONT_USER_ICONURL,PageHelper.getUserHeadUrl(ssoAccount.getIconUrl(),ssoAccount.getGender()));
			request.getSession().setAttribute(CoreConstants.FRONT_USER_TYPE,ssoAccount.getSubMemberType());
			request.getSession().setAttribute(CoreConstants.FRONT_USER_LAST_LOGIN,ssoAccount.getLastLoginTime());
			request.getSession().setAttribute(CoreConstants.FRONT_USER_CURRENCY,StrUtils.filterNullString(ssoAccount.getDefCurrency(),CommonConstants.DEF_CURRENCY));
			request.getSession().setAttribute(CoreConstants.FRONT_USER_DATE_FORMAT,StrUtils.filterNullString(ssoAccount.getDateFormat(),CoreConstants.DATE_FORMAT));
			request.getSession().setAttribute(CoreConstants.FRONT_USER_UP_DOWN_COLOR,StrUtils.filterNullString(ssoAccount.getDefDisplayColor(),CommonConstants.DEF_DISPLAY_COLOR));
			
			this.saveOperLog(request,loginCode,ssoAccount.getNickName(),CoreConstants.FRONT_LOG_LOGIN, "登录成功");
			
			//判断当前登录人角色
			String currentRole = CoreConstants.FRONT_USER_ROLE_GUEST;
			if (null!=ssoAccount.getSubMemberType() && ssoAccount.getSubMemberType()==21){
				currentRole = CoreConstants.FRONT_USER_ROLE_IFA;
			}else if (null!=ssoAccount.getSubMemberType() && ssoAccount.getSubMemberType()==11){
				currentRole = CoreConstants.FRONT_USER_ROLE_INVESTOR;
			}else if (null!=ssoAccount.getSubMemberType() && ssoAccount.getSubMemberType()==22){
				currentRole = CoreConstants.FRONT_USER_ROLE_IFA_FIRM;
				MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(ssoAccount.getId());
				request.getSession().setAttribute(CoreConstants.CONSOLE_USER_ADMIN,admin);
			}else if (null!=ssoAccount.getSubMemberType() && ssoAccount.getSubMemberType()==31){
				currentRole = CoreConstants.FRONT_USER_ROLE_DISTRIBUTOR;
				MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(ssoAccount.getId());
				request.getSession().setAttribute(CoreConstants.CONSOLE_USER_ADMIN,admin);
			}else if (null!=ssoAccount.getSubMemberType() && ssoAccount.getSubMemberType()==41){//sysadmin
				currentRole = CoreConstants.FRONT_USER_SYS_ADMIN;
			}else{
				currentRole = CoreConstants.FRONT_USER_ROLE_GUEST;
			}
			request.getSession().setAttribute(CoreConstants.FRONT_USER_ROLE, currentRole);
			request.getSession().setAttribute("role", currentRole);
			
			//获取用户菜单
			List<MemberMenu> memus = frontMenuService.getFrontMenus(companyId, currentRole, null, "1");
			request.getSession().setAttribute(CoreConstants.FRONT_USER_MENU, memus);
			String lastUrl = null;
			if("1".equals(account.getRiskDeclareFlag())){
				lastUrl = this.getLastVisitUrl(request, response);
				if(StringUtils.isBlank(lastUrl)){
					lastUrl = this.getFullPath(request) + "index.do";
				}
			}else{
				lastUrl = this.getFullPath(request) + "front/disclaimer.do";
			}
			map.put("result", true);
			map.put("directTo",lastUrl);
			JsonUtil.toWriter(map, response);
			return null;
		}else{
			if(null!=ssoAccount && WSConstants.CODE1008.equals(ssoAccount.getErrorCode())){
				map.put("errorMsg", WSConstants.MSG1008);
				this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "账号不存在已禁用");
			}else if(ssoAccount != null && WSConstants.CODE1013.equals(ssoAccount.getErrorCode())){
				map.put("errorMsg", WSConstants.MSG1013);
				this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "账号已锁定");
			}else if(ssoAccount != null && WSConstants.CODE1004.equals(ssoAccount.getErrorCode())){//账号未激活
				map.put("errorMsg", WSConstants.MSG1004);
				this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, WSConstants.MSG1004);
			}else if(ssoAccount != null ){
				Object[] objs = this.loginFailCount(request,loginCode,this.getRemoteHost(request));
				int lessCount = Integer.parseInt(objs[0].toString());
				errorMsgPlus = objs[1].toString();
				if(ssoAccount != null && StringUtils.isBlank(errorMsgPlus)){
					errorMsgPlus = ssoAccount.getErrorMsg();
				}
				map.put("lessCount", lessCount);
				map.put("errorMsg",errorMsgPlus);
	        	this.saveOperLog(request,loginCode,"",CoreConstants.FRONT_LOG_LOGIN, "用户名或者密码错误");
			}
			map.put("result", false);
			JsonUtil.toWriter(map, response);
			return null;
		}
	}
	
	
	/**
	 * 登出处理
	 */
	@RequestMapping(value = "/front/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//删除sso信息
		MemberBase logUser = (MemberBase) request.getSession().getAttribute(CoreConstants.FRONT_USER_LOGIN);
		if(logUser != null){
			memberBaseService.delMemberSso(logUser.getLoginCode(), this.getRemoteHost(request), "web");
		}
		request.getSession().invalidate();
		this.delLastVisitUrl(request, response);
		return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
	}
	
	
	/**
	 * 
	 * @author scshi_u330p
	 * @date 20161208
	 * 登录失败次数累加
	 * param loginCode 登录账号
	 * return Integer 登录失败次数；
	 * */
	private Object[] loginFailCount(HttpServletRequest request,String loginCode,String ip){
		SysSite sysSite = sysSiteService.findSysSiteById(CommonConstants.MAIN_SITE_ID);
		Object[] objs = new Object[2];
		String returnMsg = "";
		//MemberBase base = memberBaseService.saveLoginFailPlus(loginCode);
		MemberBase base = memberBaseService.findByCode(loginCode);
		if(null!=base){
			Integer failTime = base.getFailCount();
			
			if(3 == failTime){
				String content = null;
				try {
					String langCode = base.getLangCode();
					if (StringUtils.isBlank(langCode)) {
						langCode = CommonConstants.DEF_LANG_CODE;
					}
					Map<String, Object> map =new HashMap<String, Object>();
					map.put("basePath", this.getFullPath(request));
					map.put("base", base);
					map.put("ip", ip);
					map.put("resetPwd", "front/member/personal/resetpswstart.do");//找回密码链接
					map.put("home","index.do");//网站首页链接
					Configuration configuration = new Configuration();
					configuration.setServletContextForTemplateLoading( ContextLoader.getCurrentWebApplicationContext().getServletContext() , "/WEB-INF/common/");
					Template template = configuration.getTemplate("landing_anomalies_mail_template_" + langCode + ".html","UTF-8");//邮件模板
					StringWriter stringWriter = new StringWriter();
					BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
					template.setEncoding("UTF-8");
					template.setOutputEncoding("UTF-8");
					template.process(map, bufferedWriter);
					content = stringWriter.toString();
					bufferedWriter.flush();
					bufferedWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				this.sendEmail("login", base, base, base.getEmail(), PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.landing.anomailies",null), content, base.getId());
			}
			Integer lessCount = sysSite.getLoginFailCount()-failTime;
			if(0 >= lessCount){
				returnMsg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.error.refix",null)+sysSite.getLoginFailCount()+PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.error.infix",null)+sysSite.getLoginRetryHour()+PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.error.suffix",null);
			}else{
				returnMsg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.error",null)+sysSite.getLoginRetryHour()+PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"sys.login.remaining.count",null)+lessCount;
				
			}
			objs[0]=lessCount;
			objs[1]=returnMsg;
		}
		
//		return returnMsg;
		return objs;
	}
	
    /*
     * 检测验证码输入是否成功 parement String,String 接收参数：生成验证码，输入验证码
     * @Return boolean false--验证失败 true---验证成功   
     */
    private boolean checkCode(String randomCode, String validate) {
        boolean blnFlag = false;
        if (validate != null && !"".equals(validate) && randomCode != null && !"".equals(randomCode) && validate.equals(randomCode)) {
            blnFlag = true;
        }
        return blnFlag;
    }
    
    /**
     * 设置运营公司信息到session
     * @param companyInfo
     */
    private void setCompanyInfo(HttpServletRequest request, CompanyInfoVO companyInfo){
    	if (null!=companyInfo && null!=companyInfo.getId()){
    		String lang = StrUtils.getString(request.getSession().getAttribute(CoreConstants.LANG_CODE));
    		companyInfo.setInfos(lang);
    		
//			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY,companyInfo);
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_ID,companyInfo.getId());
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_CODE,companyInfo.getCode());
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_LOGO,StrUtils.getString(companyInfo.getLogoUrl(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.logo", null))));
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_LOGO_TAB,StrUtils.getString(companyInfo.getLogoUrl(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.logo_tab", null))));
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_CSS,StrUtils.getString(companyInfo.getCssUrl(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.css", null))));
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_BACKGROUND,StrUtils.getString(companyInfo.getBackgroundUrl(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.background", null))));
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_LOGIN_PAGE,companyInfo.getLoginUrl());
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_NAME,companyInfo.getName());
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME,StrUtils.getString(companyInfo.getSysName(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.sysname", null))));
			
			if (null!=companyInfo.getCompanyInfoEn()){
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_EN,StrUtils.getString(companyInfo.getCompanyInfoEn().getSysName(), 
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_EN, "login.default.sysname", null))));
			}else{
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_EN,
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_EN, "login.default.sysname", null)));
			}
			if (null!=companyInfo.getCompanyInfoEn()){
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_SC,StrUtils.getString(companyInfo.getCompanyInfoSc().getSysName(), 
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_SC, "login.default.sysname", null))));
			}else{
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_SC,
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_SC, "login.default.sysname", null)));
			}
			if (null!=companyInfo.getCompanyInfoEn()){
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_TC,StrUtils.getString(companyInfo.getCompanyInfoTc().getSysName(), 
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_TC, "login.default.sysname", null))));
			}else{
				request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_SYSNAME_TC,
						StrUtils.getString(PropertyUtils.getPropertyValue(CommonConstants.LANG_CODE_TC, "login.default.sysname", null)));
			}
			
			request.getSession().setAttribute(CoreConstants.FRONT_COMPANY_COPYRIGHT,StrUtils.getString(companyInfo.getCopyright(), 
					StrUtils.getString(PropertyUtils.getPropertyValue(lang, "login.default.copyright", null))));
    	}
    }
	/**
	 * 取得会话中运营公司的id
	 * @param request
	 * @return
	 */
    private String getSessionCompanyId(HttpServletRequest request){
    	try {
    		return StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_ID));
        } catch (Exception e) {
	        // TODO: handle exception
        	e.printStackTrace();
        }
    	return null;
    }
    /**
	 * 取得会话中运营公司的code
	 * @param request
	 * @return
	 */
    private String getSessionCompanyCode(HttpServletRequest request){
    	try {
        	return StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_CODE));    		
        } catch (Exception e) {
	        // TODO: handle exception
        	e.printStackTrace();
        }
    	return null;
    }
    
    /**
	 * 免责声明确认页面
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/front/disclaimer")
	public String disclaimer(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "login/disclaimer";
	}
    
    /**
   	 * 免责声明确认操作
   	 * @param request
   	 * @author wwluo
   	 * @return
   	 */
    @RequestMapping(value = "/front/comfirmDisclaimer")
    public void comfirmDisclaimer(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String instruction = request.getParameter("instruction"); // 1：agree，0：reject
    	String riskDeclareFlag = request.getParameter("rememberMe"); // 1不显示,0或者其他显示
    	String directTo = null;
    	if ("1".equals(instruction)) {
    		if("1".equals(riskDeclareFlag)){
    			MemberBase loginUser = this.getLoginUser(request);
    			loginUser.setRiskDeclareFlag("1");
    			loginUser.setLastUpdate(new Date());
    			memberBaseService.saveOrUpdate(loginUser);
    		}
    		directTo = this.getLastVisitUrl(request, response);
    		if(StringUtils.isBlank(directTo)){
    			directTo = this.getFullPath(request) + "index.do";
    		}
		}else{
			MemberBase logUser = (MemberBase) request.getSession().getAttribute(CoreConstants.FRONT_USER_LOGIN);
			if(logUser != null){
				memberBaseService.delMemberSso(logUser.getLoginCode(), this.getRemoteHost(request), "web");
			}
			request.getSession().invalidate();
			this.delLastVisitUrl(request, response);
			directTo =  this.getFullPath(request) + "front/viewLogin.do";
		}
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("directTo", directTo);
    	JsonUtil.toWriter(result, response);
    }
    
    
    
    
    
    
    
    
}
