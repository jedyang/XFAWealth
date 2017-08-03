/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.action.console;

import it.sauronsoftware.base64.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.CSRFTokenUtil;
import com.fsll.common.util.CookieUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * 控制器:工作台登陆管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Controller
public class ConsoleLoginAct extends CoreBaseAct{
	
	public static final String COOKIE_USER_CODE = "_COOKIE_USER_CODE_";
	public static final String COOKIE_REMEMBER_ME = "_COOKIE_REMEMBER_ME_";
	
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberManageServiceForConsole memberManageServiceForConsole;
	
	/**
	 * 登录页面加载
	 */
	@RequestMapping(value = "/console/viewLogin", method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String loginCode = "";
		String rememberMeValue = CookieUtils.getCookie(request,COOKIE_REMEMBER_ME);
		if(!"".equals(rememberMeValue)){
			model.put("rememberMe",rememberMeValue);
			if("1".equals(rememberMeValue)){
				loginCode = CookieUtils.getCookie(request,COOKIE_USER_CODE);
			}
		}else{
			model.put("rememberMe","0");
		}
		String errorMsg = request.getParameter("errorMsg") == null ? null : request.getParameter("errorMsg");
		model.addAttribute("csrf", CSRFTokenUtil.getTokenForSession(request.getSession()));
		model.put("errorMsg",errorMsg);
		model.put("name",loginCode);
		this.saveSysLog(request,loginCode,"",CoreConstants.CONSOLE_LOG_LOGIN, "打开登陆页面");
		return "console/login";
	}
	
	/**
	 * 登录权限验证
	 */
	@RequestMapping(value = "/console/login")
	public String login(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		if(!virifyCSRFToken(request)){
			return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
		}
		String loginCode = request.getParameter("name");
		String loginPwd = request.getParameter("pwd");
		String vercode = request.getParameter("vercode");
		Integer loginCount = request.getParameter("loginCount") == null ? 0 : Integer.parseInt(request.getParameter("loginCount"));
		
		if(loginCode == null || loginPwd == null ){
			this.saveSysLog(request,loginCode,"",CoreConstants.CONSOLE_LOG_LOGIN, "登录名或密码不能为空");
			return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
		}
		
        /** ** 添加 只能含有汉字、数字、字母、下划线、中划线,不能以下划线开头和结尾 *** */
        if (loginCount >= 3) {// 三次登陆失败后，增加验证码验证
        	String randomCode = (String) request.getSession().getAttribute("randStr");
            if (!checkCode(randomCode, vercode)) {
            	loginCount++;
            	model.put("errorMsg","用户名或者密码错误");
            	model.put("loginCount",loginCount);
            	this.saveSysLog(request,loginCode,"",CoreConstants.CONSOLE_LOG_LOGIN, "三次登陆失败后");
            	return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
            }
        }
        loginCode = Base64.decode(loginCode, "UTF-8");
        loginPwd = Base64.decode(loginPwd, "UTF-8");
		String md5password = this.pwdEncoder.encodePassword(loginPwd);
		MemberSsoVO ssoVO = memberBaseService.saveLoginAuth(request, loginCode, md5password, "web", "1");
		if(ssoVO != null){
			String rememberMe = request.getParameter("rememberMe");
			if("1".equals(rememberMe)){
				CookieUtils.addCookie(request,response,COOKIE_USER_CODE,loginCode,-1,"");
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"1",-1,"");
			}else{
				CookieUtils.addCookie(request,response,COOKIE_REMEMBER_ME,"0",-1,"");
			}
			request.getSession().setAttribute(CoreConstants.CONSOLE_USER_LOGIN,ssoVO);
			request.getSession().setAttribute(CoreConstants.CONSOLE_USER_NAME,ssoVO.getNickName());
			request.getSession().setAttribute(CoreConstants.LANG_CODE,ssoVO.getLangCode());

			MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(ssoVO.getId());
			request.getSession().setAttribute(CoreConstants.CONSOLE_USER_ADMIN,admin);
			
			this.saveSysLog(request,loginCode,ssoVO.getNickName(),CoreConstants.CONSOLE_LOG_LOGIN, "登录成功");
			return "redirect:"+this.getFullPath(request)+"console/index.do";
		}else{
			loginCount++;
        	model.put("errorMsg","用户名或者密码错误");
        	model.put("loginCount",loginCount);
        	this.saveSysLog(request,loginCode,"",CoreConstants.CONSOLE_LOG_LOGIN, "用户名或者密码错误");
			return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
		}
	}
	/**
	 * 登出处理
	 */
	@RequestMapping(value = "/console/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		request.getSession().invalidate();
		return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
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
