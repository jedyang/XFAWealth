/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获得语言资源文件
 * 
 * @author michael
 * @version 1.0
 * @date   2016-11-11
 */
public class LoadLangResDirective implements TemplateDirectiveModel {
	public static final String LANG_KEY = "k";
	public static final String LANG_LANG = "l";

	@Autowired
    private HttpServletRequest request;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		//String l = DirectiveUtils.getString(LANG_FLAG, params);
		String k = DirectiveUtils.getString(LANG_KEY, params);
		String l = DirectiveUtils.getString(LANG_LANG, params);

		if (null==l || "".equals(l)){
			l = StrUtils.getString(request.getSession().getAttribute(CoreConstants.LANG_CODE));
			if (null==l || "".equals(l))
				l = CommonConstants.DEF_LANG_CODE;
		}

		if (null==k) k = "";
		
		Properties properties = PropertyUtils.getPropertyByLang(l, k);
//		String jsonString = JSONUtils.valueToString(PropertyUtils.getPropertyMap(properties));
//		Writer out = env.getOut();
//		if(jsonString != null) {
//			out.append(jsonString);
//		}else{
//			out.append("");
//		}
		JSONObject jsonObject = JSONObject.fromObject(PropertyUtils.getPropertyMap(properties));
		Writer out = env.getOut();
		if(jsonObject != null) {
			out.append(jsonObject.toString());
		}else{
			out.append("");
		}
	}
}
