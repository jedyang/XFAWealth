/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.fsll.core.CoreConstants;

/**
 * 权限拦截器
 * 包括登录信息、权限信息、站点信息
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class AdminContextInterceptor extends HandlerInterceptorAdapter{	
	/**
	 * 进入方法前进入
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(auth){
			String uri = getURI(request);
			if (exclude(uri)) {
				return true;
			}
			Object obj = request.getSession().getAttribute(CoreConstants.FRONT_USER_LOGIN);
			if(obj!=null){  
	            return true;  
	        }else{
	        	response.sendRedirect(request.getContextPath()+loginUrl);
	        	return false;  
	        }
		}else{
			return true;
		}
	}

	/**
	 * 方法处理完成前进入
	 */
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler, ModelAndView mav)throws Exception {

	}

	/**
	 * 完成之后进入
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)throws Exception {

	}
	
    /**
     * 排除的URL
     * @param uri
     * @return
     */
	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(uri)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获得第三个路径分隔符的位置
	 * @param request
	 * @throws IllegalStateException 访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request)throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		if (!StringUtils.isBlank(ctxPath)) {
			int start = uri.indexOf('/',1);
			uri = uri.substring(start);
		}
		return uri;
	}
	
	private boolean auth = true;//是否需要验证的开关
	private String[] excludeUrls;//排除的url
	private String loginUrl;//登录的url
	
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	
	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
}