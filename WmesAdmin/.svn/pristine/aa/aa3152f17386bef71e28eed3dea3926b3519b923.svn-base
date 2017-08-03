/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Csrf生成工具类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-10-21
 */
public class CSRFTokenUtil {
	/**
     * token名称
     */
	private static final String CSRF_PARAM_NAME = "CSRFToken";

    /**
     * 本地存储token的session名称
     */
    public static final  String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenUtil.class.getName() + ".xfatoken";

    public static String getTokenForSession(HttpSession session) {
        String token = null;
        synchronized (session) {
            token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
            if (null == token) {
                token = UUID.randomUUID().toString();
                session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
            }
        }
        return token;
    }

    /**
     * 从request中获得token的值
     * 
     * @param request
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(CSRF_PARAM_NAME);
    }

    private CSRFTokenUtil() {
    	
    };
}
