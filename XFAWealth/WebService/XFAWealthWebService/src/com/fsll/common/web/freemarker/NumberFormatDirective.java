/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 数字格式化标签
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author michael
 * @version 1.0
 * @date   2016年10月20日
 */
public class NumberFormatDirective implements TemplateDirectiveModel {
	public static final String PARAM_S = "s";//数字字符串对象
	public static final String PARAM_FORMAT = "f";//数字显示格式
	public static final String PARAM_FROM_CURRENCY = "fCy";//源货币
	public static final String PARAM_TO_CURRENCY = "tCy";//目标货币
	public static final String PARAM_REPLACE = "r";//当对象为null时的替代字符

	@Autowired
    private HttpServletRequest request;
//	@Autowired
//	private BaseService baseService;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//数字字符串对象
		String s = DirectiveUtils.getString(PARAM_S, params);
		//数字格式，如果空则使用默认格式 PRICE=#,###.##
		String f = DirectiveUtils.getString(PARAM_FORMAT, params);
		//获取替代字符
		String r = DirectiveUtils.getString(PARAM_REPLACE, params);

		//源货币
		String fromCurrency = CommonConstants.DEF_CURRENCY;
		String from = StrUtils.getString(DirectiveUtils.getString(PARAM_FROM_CURRENCY, params));
		if("".equals(from)){
			//如无传入源货币，则取登录用户的默认货币
			try {
				from = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_CURRENCY));
			} catch (Exception e) {
				// TODO: handle exception
				from = "";
			}
			//如用户无登录或用户无设置默认货币，则设置系统默认货币
			if("".equals(from)){
				from = fromCurrency;
			}
		}
		
//		String to = StrUtils.getString(DirectiveUtils.getString(PARAM_TO_CURRENCY, params));
		
		//如果有设置转换的货币，则自动计算兑换率
		double rate = 0;//0时不计算
//		if (!"".equals(to))
//		try {
//			rate = baseService.findExchangeRate(from, to).getRate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			rate = 0;
//		}
		
		Writer out = env.getOut();
//		if (s != null) {
//			out.append(StrUtils.getNumberString(s, f, rate));//转换出错会返回“”
//		}else{
//			out.append("-");
//		}
		out.append(PageHelper.getNumberString(s, f, rate, r));
	}
}
