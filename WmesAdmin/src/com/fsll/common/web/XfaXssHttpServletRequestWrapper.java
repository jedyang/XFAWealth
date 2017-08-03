/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 
 * 跨站脚本漏洞
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public class XfaXssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
  
    public XfaXssHttpServletRequestWrapper(HttpServletRequest request) {  
        super(request);  
    }  
  
    @Override  
    public String getHeader(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));  
    }  
  
    @Override  
    public String getQueryString() {  
        return StringEscapeUtils.escapeHtml4(super.getQueryString());  
    }  
  
    @Override  
    public String getParameter(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));  
    }  
  
    @Override  
    public String[] getParameterValues(String name) {  
        String[] values = super.getParameterValues(name);  
        if(values != null) {  
            int length = values.length;  
            String[] escapseValues = new String[length];  
            for(int i = 0; i < length; i++){  
                escapseValues[i] = StringEscapeUtils.escapeHtml4(values[i]);  
            }  
            return escapseValues;  
        }  
        return super.getParameterValues(name);  
    }
}
