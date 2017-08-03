/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.fsll.common.util.PageHelper;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 百分比数字格式化标签，包含颜色格式参数
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author michael
 * @version 1.0
 * @date   2016年10月20日
 */
public class NumberColorDirective implements TemplateDirectiveModel {
	public static final String PARAM_S = "s";//数字字符串对象
	public static final String PARAM_FORMAT = "f";//数字显示格式
	public static final String PARAM_COLOR = "c";//数字颜色组合
	public static final String PARAM_REPLACE = "r";//当对象为null时的替代字符

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//数字字符串对象
		String s = DirectiveUtils.getString(PARAM_S, params);
		//数字格式，如果空则使用默认格式 PRICE=#,###.##
		String f = DirectiveUtils.getString(PARAM_FORMAT, params);
		//数字颜色组合
		String c = DirectiveUtils.getString(PARAM_COLOR, params);
		//获取替代字符
		String r = DirectiveUtils.getString(PARAM_REPLACE, params);

		Writer out = env.getOut();
//		if (s != null) {
//			double d = StrUtils.getDoubleVal(s);
//			//暂时两种颜色格式，1 : 涨“绿”跌“红”，2 : 涨“红”跌“绿”
//			if (c==null || "".equals(c) || (!"1".equals(c) && !"2".equals(c))) c = CommonConstants.DEF_DISPLAY_COLOR;
//			String style = "funds_search_positive";
//			if ("1".equals(c)){//涨“绿”跌“红”
//				if (d>0) 
//					style = "funds_search_positive";//“绿”
//				else if (d<0) 
//					style = "funds_search_negative";//“红”
//				else //d==0
//					style = "funds_search_positive";//“绿”
//			}else if ("2".equals(c)){//涨“红”跌“绿”
//				if (d>0) 
//					style = "funds_search_negative";//“红”
//				else if (d<0) 
//					style = "funds_search_positive";//“绿”
//				else //d==0
//					style = "funds_search_negative";//“红”
//			}else{
//				if (d>0) 
//					style = "funds_search_positive";//“绿”
//				else if (d<0) 
//					style = "funds_search_negative";//“红”
//				else //d==0
//					style = "funds_search_positive";//“绿”
//			}
//			out.append("<font class='"+style+"'>"+StrUtils.getPercentString(s, f)+"</font>");//转换出错会返回“”
//		}else{
//			out.append("-");
//		}
		out.append(PageHelper.getDisplayString(s, f, c, r));
	}
}
