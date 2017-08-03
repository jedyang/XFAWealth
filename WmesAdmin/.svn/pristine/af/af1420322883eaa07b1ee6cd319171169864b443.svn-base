/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.service.IndexService;

/**
 * 控制器:系统首页
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Controller
public class IndexAct extends CoreBaseAct{
	
	@Resource
	private IndexService indexService;
	
	/**
	 * 系统首页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return "index";
	}
	/**
	 * 异步获取菜单
	 * @author scshi
	 * */
	@RequestMapping(value="sysMenuJson")
	public void sysMenuJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String menuJson = indexService.loadSysMenuJson(this.getLoginUser(request));
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("menuJson",menuJson);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 系统首页
	 */
	@RequestMapping(value = "/tab", method = RequestMethod.GET)
	public String tab(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return "index/tab";
	}
	
	/**
	 * 工作台
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return "index/main";
	}
		
}
