/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

import com.fsll.common.util.DateUtil;
import com.fsll.core.CoreConstants;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 日期格式化标签
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author michael
 * @version 1.0
 * @date   2016年10月20日
 */
public class DateFormatDirective implements TemplateDirectiveModel {
	public static final String PARAM_S = "s";//日期对象
	public static final String PARAM_TYPE = "t";//日期数据类型
	public static final String PARAM_FORMAT = "f";//日期显示格式
	public static final String PARAM_REPLACE = "r";//当对象为null时的替代字符

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取日期对象
		Date s = DirectiveUtils.getDate(PARAM_S, params);
		//获取日期数据类型：d - date, t - time, dt - datetime
		String t = DirectiveUtils.getString(PARAM_TYPE, params);
		//获取日期格式，如果空则使用CoreConstants里定义的默认格式 
		String f = DirectiveUtils.getString(PARAM_FORMAT, params);
		//获取替代字符
		String r = DirectiveUtils.getString(PARAM_REPLACE, params);
		
		if (null==f || "".equals(f)){//如果无设置则使用默认日期格式
			if ("d".equals(t))
				f = CoreConstants.DATE_FORMAT;
			else if ("t".equals(t))
				f = CoreConstants.TIME_FORMAT;
			else if ("dt".equals(t))
				f = CoreConstants.DATE_TIME_FORMAT;
			else f = CoreConstants.DATE_FORMAT;
		}
		Writer out = env.getOut();
		if (s != null) {
			//通过日期工具获取日期字符串，转换出错会返回空字符串“”
			out.append(DateUtil.getDateStr(s, f));
		}else{
			//没有数据时返回 -
			if (null==r) out.append("-");
			else out.append(r);
		}
	}
}
