/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取用户头像url标签
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author michael
 * @version 1.0
 * @date   2016年10月20日
 */
public class UserHeaderDirective implements TemplateDirectiveModel {
	public static final String PARAM_URL = "u";//头像url
	public static final String PARAM_TYPE = "t";//用户类型：u - 个人用户, f - IFA Firm, d - Distributor, c - Company
	public static final String PARAM_GENDER = "g";//性别

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//头像url
		String u = DirectiveUtils.getString(PARAM_URL, params);
		if (null==u) u = "";
		//用户类型：u - 个人用户，f - IFA Firm，d - Distributor
		String t = DirectiveUtils.getString(PARAM_TYPE, params);
		if (null==t || "".equals(t)) t = "u";//默认个人用户
		//性别
		String g = DirectiveUtils.getString(PARAM_GENDER, params);
		if (null==g) g = "";
		
		Writer out = env.getOut();

		if ("F".startsWith(t.toUpperCase()) || "D".startsWith(t.toUpperCase()) || "C".startsWith(t.toUpperCase()))
			out.append(PageHelper.getLogoUrl(u, t));
		else
			out.append(PageHelper.getUserHeadUrl(u, g));
	}
}
