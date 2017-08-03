/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获得语言资源文件
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public class LangResDirective implements TemplateDirectiveModel {
	//public static final String LANG_FLAG = "l";
	public static final String LANG_KEY = "k";
	public static final String LANG_LANG = "lang"; //指定语言
	public static final String LANG_REPLACE = "r";//如果查找不到，则用此替代
	public static final String LANG_ARG0 = "arg0";
	public static final String LANG_ARG1 = "arg1";
	public static final String LANG_ARG2 = "arg2";
	@Autowired
    private HttpServletRequest request;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//String l = DirectiveUtils.getString(LANG_FLAG, params);
		String k = DirectiveUtils.getString(LANG_KEY, params);
		String r = DirectiveUtils.getString(LANG_REPLACE, params);

		String lang = DirectiveUtils.getString(LANG_LANG, params);
		String arg0 = DirectiveUtils.getString(LANG_ARG0, params);
		String arg1 = DirectiveUtils.getString(LANG_ARG1, params);
		String arg2 = DirectiveUtils.getString(LANG_ARG2, params);
		List<String> args = new ArrayList<String>();
		if(arg0 != null) {
			args.add(arg0);
		}
		if(arg1 != null) {
			args.add(arg1);
		}
		if(arg2 != null) {
			args.add(arg2);
		}
		String l = CommonConstants.DEF_LANG_CODE;
		Object obj = request.getSession().getAttribute(CoreConstants.LANG_CODE);
		if(lang != null){
			l = lang;
		}else if(obj != null){
			l = (String)obj;
		}
		String keyValue = PropertyUtils.getPropertyValue(l, k,args.toArray());
		Writer out = env.getOut();
		if(null!=keyValue && !keyValue.isEmpty()) {
			out.append(keyValue);
		}else{
			if (null!=r && !r.isEmpty())
				out.append(r);
			else
				out.append("");
		}
	}
}
