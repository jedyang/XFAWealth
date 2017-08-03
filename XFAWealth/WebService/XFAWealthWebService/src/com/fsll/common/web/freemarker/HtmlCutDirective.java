/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.fsll.common.util.StrUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * HTML文本提取并截断
 * 需要拦截器com.fsll.common.web.ProcessTimeFilter支持
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public class HtmlCutDirective implements TemplateDirectiveModel {
	public static final String PARAM_S = "s";
	public static final String PARAM_LEN = "len";
	public static final String PARAM_APPEND = "append";
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		String s = DirectiveUtils.getString(PARAM_S, params);
		Integer len = DirectiveUtils.getInt(PARAM_LEN, params);
		String append = DirectiveUtils.getString(PARAM_APPEND, params);
		if (s != null) {
			Writer out = env.getOut();
			if (len != null) {
				out.append(StrUtils.htmlCut(s, len, append));
			} else {
				out.append(s);
			}
		}
	}
}
