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
 * 百分比数字格式化标签
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author michael
 * @version 1.0
 * @date   2016年10月20日
 */
public class NumberPercentDirective implements TemplateDirectiveModel {
	public static final String PARAM_S = "s";//数字字符串对象
	public static final String PARAM_FORMAT = "f";//数字显示格式
	public static final String PARAM_REPLACE = "r";//当对象为null时的替代字符

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//数字字符串对象
		String s = DirectiveUtils.getString(PARAM_S, params);
		//数字格式，如果空则使用默认格式 PRICE=#,###.##
		String f = DirectiveUtils.getString(PARAM_FORMAT, params);
		//获取替代字符
		String r = DirectiveUtils.getString(PARAM_REPLACE, params);

		Writer out = env.getOut();
//		if (s != null) {
//			out.append(StrUtils.getPercentString(s, f));//转换出错会返回“”
//		}else{
//			out.append("-");
//		}
		out.append(PageHelper.getPercentString(s, f, r));
	}
}
