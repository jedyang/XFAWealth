/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.action;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.sauronsoftware.base64.Base64;

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
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.SysAdminService;
import com.fsll.core.service.SysSiteService;
import com.fsll.core.service.impl.SysAdminServiceImpl;
import com.fsll.wmes.entity.MemberBase;

/**
 * 控制器:登陆管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Controller
public class LoginAct extends CoreBaseAct{
	
	public static final String COOKIE_USER_NAME = "_COOKIE_USER_NAME_";
	public static final String COOKIE_USER_PWD = "_COOKIE_USER_PWD_";
	public static final String COOKIE_REMEMBER_ME = "_COOKIE_REMEMBER_ME_";
	
	@Autowired
	private SysAdminService sysAdminService;

	/**
	 * 登录页面加载
	 */
	@RequestMapping(value = "/viewLogin", method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String name = "";
		String pwd = "";
		String rememberMeValue = CookieUtils.getCookie(request,COOKIE_REMEMBER_ME);
		if(!"".equals(rememberMeValue)){
			model.put("rememberMe",rememberMeValue);
			if("1".equals(rememberMeValue)){
				name = CookieUtils.getCookie(request,COOKIE_USER_NAME);
				pwd = CookieUtils.getCookie(request,COOKIE_USER_PWD);
			}
		}else{
			model.put("rememberMe","0");
		}
		model.addAttribute("csrf", CSRFTokenUtil.getTokenForSession(request.getSession()));
		String loginCode = request.getParameter("loginCode");
		if (StringUtils.isNotBlank(loginCode)) {
			SysAdmin base = sysAdminService.findByCode(loginCode);
			if(base != null){
				model.put("loginCount",base.getFailCount());
			}
		}
		model.put("name",name);
		model.put("pwd",pwd);
		return "login";
	}
	
	/**
	 * 登录权限验证
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		/*if(!virifyCSRFToken(request)){
			return "redirect:"+this.getFullPath(request)+"viewLogin.do";
		}*/
		
		String name = request.getParameter("name") == null ? "" : request.getParameter("name");
		String pwd =request.getParameter("pwd") == null ? "" : request.getParameter("pwd");
		String vercode = request.getParameter("vercode");
		Integer loginCount = request.getParameter("loginCount") == null ? 0 : Integer.parseInt(request.getParameter("loginCount"));
		
        /** ** 添加 只能含有汉字、数字、字母、下划线、中划线,不能以下划线开头和结尾 *** */
		/**
		if (!name.matches("^(?!_)(?!.*?_$)[a-zA-Z@.&~%#0-9_-\u4e00-\u9fa5]+$") || !pwd.matches("^(?!_)(?!.*?_$)[a-zA-Z@.&~%#0-9_-\u4e00-\u9fa5]+$")) {
            // 添加操作日志
            loginCount++;
            model.put("errorMsg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.userpwdFormat",null));
            model.put("loginCount",loginCount);
            return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
        **/
		boolean bol=false;
		if (loginCount >= 3) {// 三次登陆失败后，增加验证码验证
        	String randomCode = (String) request.getSession().getAttribute("randStr");
            if (!checkCode(randomCode, vercode)) {
            	loginCount++;
            	model.put("loginCode", Base64.decode(name, "UTF-8"));
            	model.put("pwd", Base64.decode(pwd, "UTF-8"));
            	model.put("errorMsg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.exceptionCaptcha",null));
            	model.put("loginCount",loginCount);
            	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
            }
        }
		name = Base64.decode(name, "UTF-8");
		pwd = Base64.decode(pwd, "UTF-8");
		String md5password = this.pwdEncoder.encodePassword(pwd).toUpperCase();
		SysAdmin admin = sysAdminService.checkExist(name,md5password);
		if(admin != null){
			String rememberMe = request.getParameter("rememberMe");
			if("1".equals(rememberMe)){
				CookieUtils.addCookie(request,response,COOKIE_USER_NAME,name,-1,"");
				CookieUtils.addCookie(request,response,COOKIE_USER_PWD,pwd,-1,"");
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"1",-1,"");
			}else{
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"0",-1,"");
			}
			request.getSession().setAttribute(CoreConstants.USER_LOGIN,admin);
			request.getSession().setAttribute(CoreConstants.LANG_CODE,StringUtils.isBlank(admin.getLangCode())?"sc":admin.getLangCode());
			request.getSession().setAttribute(CoreConstants.USER_NAME,admin.getNickName());
			return "redirect:"+this.getFullPath(request)+"index.do";
		}else{
			SysAdmin sysAdmin = sysAdminService.findByCode(name);
			if(sysAdmin!=null){
				loginCount++;
				sysAdmin.setFailCount(loginCount);
				sysAdminService.saveOrUpdate(sysAdmin, null);	
				model.put("errorMsg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.badCredentials",null));
	        	model.put("loginCount",loginCount);
	        	model.put("loginCode", name);
			}else{
				model.put("errorMsg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.usernameNotExist",null));
			}
			
        	
			return "redirect:"+this.getFullPath(request)+"viewLogin.do";
		}
	}
	
	/**
	 * 登出处理
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		request.getSession().invalidate();
		return "redirect:"+this.getFullPath(request)+"viewLogin.do";
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
	
}
