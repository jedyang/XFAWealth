/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.base.action;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import com.fsll.common.CommonConstants;
import com.fsll.common.security.PwdEncoder;
import com.fsll.common.util.CookieUtils;
import com.fsll.common.util.DateUtil;

/**
 * 控制器基类：通用模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class BaseAct{
	
    protected static final String SUCCESS = "success";//成功
    protected static final String ERROR = "error";//失败常量
    
	@Autowired
	protected PwdEncoder pwdEncoder;//密码加密接口类
    
//	@InitBinder  
//	public void initBinder(WebDataBinder binder) {  
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true:允许输入空值，false:不能为空值  
//	}
    
	/***
	 * url传递过来的加密字符统一解密
	 * @return
	 */
	public String getDecodeStr(String originalStr)
	{
		try{
			String tmpStr = originalStr;
			if(null != tmpStr && !"".equals(tmpStr)){
				tmpStr = java.net.URLDecoder.decode(java.net.URLDecoder.decode(tmpStr, "UTF-8"), "UTF-8");
			}else{
				tmpStr = "";
			}
	  	    return tmpStr;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
    
    /**
     * 获得基本路径
     * @param request
     * @return
     */
    protected String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
        return path;
    }
    
    /**
     * 保存最近的url地址到cookie中,当用户登录超时进入系统时,可以直接跳到指定页面中
     * @param request
     * @param response
     */
    protected void saveLastVisitUrl(HttpServletRequest request,HttpServletResponse response){
    	StringBuffer urlSb = request.getRequestURL();
    	String qParam = request.getQueryString();
    	if(qParam != null && !"".equals(qParam)){
    		if(urlSb.indexOf("?")>-1){
    			urlSb.append("&").append(qParam);
    		}else{
    			urlSb.append("?").append(qParam);
    		}
    	}
		CookieUtils.addCookie(request,response,"_COOKIE_LAST_URL_",urlSb.toString(),-1,"");
    }
    
    /**
     * 获得上次最近保存的url
     * @param request
     * @param response
     */
    protected String getLastVisitUrl(HttpServletRequest request,HttpServletResponse response){
    	return CookieUtils.getCookie(request,"_COOKIE_LAST_URL_");
    }
    
    /**
     * 清空上次最近保存的url
     * @param request
     * @param response
     */
    protected void delLastVisitUrl(HttpServletRequest request,HttpServletResponse response){
    	CookieUtils.cancleCookie(request, response, "_COOKIE_LAST_URL_", null);
    }
    
    /**
     * 获得全路径
     * @param request
     * @return
     */
    protected String getFullPath(HttpServletRequest request) {
		String fullPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        return fullPath;
    }
    
    /**
     * 判断当前请求来源于何种设备
     * android : 所有android设备
     * mac os : iphone ipad
     * windows phone:Nokia等windows系统的手机
     */
	protected void  isMobileDevice(HttpServletRequest request,ModelMap model){
		boolean isMobile = false;
		String requestHeader = request.getHeader("user-agent");
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        if(requestHeader == null)isMobile = false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>-1){
            	isMobile = true;
            }
        }
		model.put("isMobile",isMobile);
	}
    
    /**
     * 校验非空
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotEmpty(String field, String value, Map<String, Object> model) {
        if (isEmpty(value)) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * 自定义校验
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyCustom(String field, boolean value, Map<String, Object> model) {
        if (value) {
            model.put(ERROR, "verify.custom." + field);
            return true;
        }
        return false;
    }

    /**
     * 校验非空
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotEmpty(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * 校验小于
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected boolean verifyNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value <= specific) {
            model.put(ERROR, "verify.notGreaterThen." + field);
            return true;
        }
        return false;
    }

    /**
     * 校验大于
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected boolean verifyNotLongThen(String field, String value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value.length() > specific) {
            model.put(ERROR, "verify.notLongThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected boolean verifyNotLessThen(String field, String value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value.length() < specific) {
            model.put(ERROR, "verify.notLessThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotMobile(String field, String value, Map<String, Object> model) {
        if (verifyNotMobile(value)) {
            model.put(ERROR, "verify.notMobile." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean verifyNotMobile(String value) {
        Pattern p = Pattern.compile(CommonConstants.MOBILE_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotEMail(String field, String value, Map<String, Object> model) {
        if (verifyNotEMail(value)) {
            model.put(ERROR, "verify.notEmail." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean verifyNotEMail(String value) {
        Pattern p = Pattern.compile(CommonConstants.EMAIL_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }
    
    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotEMailAndMobile(String field, String value, Map<String, Object> model) {
        if (verifyNotEMail(value) && verifyNotMobile(value)) {
            model.put(ERROR, "verify.notEmailAndMobile." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean verifyNotNumber(String value) {
        Pattern p = Pattern.compile(CommonConstants.NUMBER_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyNotExist(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notExist." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean verifyHasExist(String field, Object value, Map<String, Object> model) {
        if (notEmpty(value)) {
            model.put(ERROR, "verify.hasExist." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return
     */
    protected boolean verifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return
     */
    protected boolean verifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * 
     * @param var
     * @return
     */
    protected boolean notEmpty(String var) {
        return isNotBlank(var);
    }

    /**
     * 
     * @param var
     * @return
     */
    protected static boolean notEmpty(Object var) {
        return null != var;
    }
    
    /**
     * 
     * @param var
     * @return
     */
    protected boolean notEmpty(Object[] var) {
        return null != var && 0 < var.length;
    }
    
	protected String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}
	
	/**
	 * 根据周期获取开始日期
	 * @param periodCode
	 * @return
	 */
	protected String getStartDate(String periodCode) {
		String startDate = "";
		if(periodCode.indexOf("return_period_code_")>-1){
			String period = periodCode.replace("return_period_code_", "");
			if("YTD".equalsIgnoreCase(period)){
				startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());
			}else if("LAUNCH".equalsIgnoreCase(period)){
				startDate = "";
			}else {
				String num = period.replace("M", "").replace("Y", "");
				
				int unit;
				try {
					unit = Integer.parseInt(num);
				} catch (Exception e) {
					// TODO: handle exception
					unit = 1;
				}
				
				if(period.indexOf("W")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.DATE, -(unit*7));
				}
				
				if(period.indexOf("M")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.MONTH, -unit);
				}
				
				if(period.indexOf("Y")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.YEAR, -unit);
				}
			}		
		}
		return startDate;
	}
    
}
