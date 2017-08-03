/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.wmes.base.WmesBaseAct;

/****
 * 工作台首页管理
 * @author mjhuang
 * @version 1.0
 * @date 2016-06-20
 */
@Controller
public class ConsoleIndexAct extends WmesBaseAct{
	/**
	 * 工作台首页
	 */
	@RequestMapping(value = "/console/index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/index";
	}
	
	/**
	 * 工作台首页（新样式测试页面）
	 */
	@RequestMapping(value = "/console/indexDemo")
	public String indexDemo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/index/index";
	}
	
}
