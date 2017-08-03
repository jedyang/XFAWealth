/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 跨站脚本漏洞
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public class XfaXssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XfaXssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
    	
    }
}
