/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.interceptor;

import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 防sql注入的拦截器
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-10-21
 */
public class SqlInjectIntercepter implements HandlerInterceptor {
	//private static final String injStr = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
	private String injStr = "and|exec|insert|select|delete|update|count|truncate|declare";
	private StringTokenizer injtoken = new StringTokenizer(injStr,"|");  
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object arg2) throws Exception {
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				  while(injtoken.hasMoreElements()){
					  String sqlStr = injtoken.nextToken(); 
					  if (values[i].indexOf(sqlStr) >= 0){
						  response.setContentType("text/html;charset=utf-8");
						  response.getWriter().print(" illegal character<br><a href='#' onclick='history.Go(-1)'>back</a>");
						  return false;
					  }
				  }
			}
		}
		return true;
	}
}
