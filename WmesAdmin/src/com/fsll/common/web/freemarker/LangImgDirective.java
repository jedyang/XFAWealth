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
 * 获得不同语言的图片（文件格式： 文件名_语言编码.后缀 ，如： logo_en.png、logo_sc.png、logo_tc.png）
 * 
 * @author michael
 * @version 1.0
 * @date   2017-01-11
 */
public class LangImgDirective implements TemplateDirectiveModel {
	public static final String IMG_FILE = "f";//图片文件url
	public static final String IMG_KEY = "k";//图片文件的会话关键词
	public static final String IMG_LANG = "l";//语言
	public static final String IMG_REPLACE = "r";//如果查找不到url，则用此替代
	public static final String IMG_TYPE = "t";//图片类型：logo 单图,logos 多语言套图,bg 背景图,tab 标签logo
	
	@Autowired
    private HttpServletRequest request;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		String f = DirectiveUtils.getString(IMG_FILE, params);
		String k = DirectiveUtils.getString(IMG_KEY, params);
		String l = DirectiveUtils.getString(IMG_LANG, params);
		String r = DirectiveUtils.getString(IMG_REPLACE, params);
		String t = DirectiveUtils.getString(IMG_TYPE, params);

		//获取图片类型
		if (null==t){
			t="";
		}
		//获取语言
		if (null==l || "".equals(l)){
			l = StrUtils.getString(request.getSession().getAttribute(CoreConstants.LANG_CODE));
			if (null==l || "".equals(l))
				l = CommonConstants.DEF_LANG_CODE;
		}
		//获取图片文件url
		if (null==f) f = "";
		//获取关键词
		if (null==k) k = "";
		
		//如果没提供f，则从session中获取文件url
		if (f.isEmpty() && !k.isEmpty()){
			f = StrUtils.getString(request.getSession().getAttribute(k));
		}
		
		//根据语言编码，转换文件名
		if (!f.isEmpty()){
			if ("logos".equalsIgnoreCase(t)){//多语言套图
				int p = f.lastIndexOf(".");
				f = f.substring(0, p)+"_"+l+f.substring(p);
			}else if ("tab".equalsIgnoreCase(t)){//标签logo
				int p = f.lastIndexOf(".");
				f = f.substring(0, p)+"_s"+f.substring(p);
			}
		}else{
			//无图片路径，则用替代图片
			if (null!=r && !r.isEmpty())
				f = r;
		}
		
		//如最终无图片路径，则用配置中的默认图片代替
		if (f.isEmpty()){
			if ("tab".equalsIgnoreCase(t)){
				f = PropertyUtils.getPropertyValue(l, "login.default.logo_tab", null);
			}else if ("bg".equalsIgnoreCase(t)){
				f = PropertyUtils.getPropertyValue(l, "login.default.background", null);
			}else if ("logo".equalsIgnoreCase(t)){
				f = PropertyUtils.getPropertyValue(l, "login.default.logo", null);
			}else if ("logos".equalsIgnoreCase(t)){
				f = PropertyUtils.getPropertyValue(l, "login.default.logo", null);
			}
		}
		
		//处理上传的图片
		if (f.startsWith("/u")) {
			f = "/loadImgSrcByPath.do?filePath="+f;
		}
		
		Writer out = env.getOut();
		out.append(f);
	}
}
