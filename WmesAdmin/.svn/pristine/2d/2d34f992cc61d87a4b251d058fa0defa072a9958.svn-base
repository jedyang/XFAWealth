/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysAdminService;

/**
 * 控制器:用户管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Controller
@RequestMapping("/sys/admin")
public class SysAdminAct extends CoreBaseAct{
	
	@Autowired
	private SysAdminService sysAdminService;
	
	/**
	 * 分页列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "sys/admin/list";
	}
	
	/**
	 * 分页获得记录
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,SysAdmin admin) {
		String searchKey = request.getParameter("searchKeyWord");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysAdminService.findByPage(jsonPaging, admin, searchKey);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 表单新增或者修改
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		SysAdmin sysAdmin = new SysAdmin();
		String id = request.getParameter("id");
		if(id != null){
			sysAdmin = sysAdminService.findById(id);
		}
		model.put("sysAdmin",sysAdmin);
		return "sys/admin/admin_input";
	}
	
	/**
	 * 表单保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(SysAdmin sysAdmin,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String repassword = request.getParameter("repassword");
		if(StringUtils.isBlank(sysAdmin.getId())){
			sysAdmin.setId(null);
			sysAdmin.setPassword(pwdEncoder.encodePassword(sysAdmin.getPassword()));
		}else{
			//不为空则修改了密码
			if(StringUtils.isNotBlank(repassword)){
				sysAdmin.setPassword(pwdEncoder.encodePassword(repassword));
			}
		}
		sysAdmin = sysAdminService.saveOrUpdate(sysAdmin,this.getLoginUser(request));
		obj.put("result",true);
		JsonUtil.toWriter(obj, response);
		return null; 
	}
	
	
	/**
	 * 激活/停用操作
	 */
	@RequestMapping(value = "/saveIsValid")
	public String saveIsValid(String ids,String isValid,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		sysAdminService.saveIsValid(ids,isValid);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
		return null; 
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping(value = "/delete")
	public String delete(String ids,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		sysAdminService.delete(ids);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
		return null; 
	}
	
	/**判断账号是否存在*/
	@RequestMapping(value = "/checkUnique")
	public void checkUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String loginCode = request.getParameter("loginCode");
		String adminId = request.getParameter("adminId");
		SysAdmin sysAdmin = sysAdminService.checkUnique(loginCode, adminId);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(sysAdmin != null){
			obj.put("valid",false);
		}else{
			obj.put("valid",true);
		}
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 语言切换
	 */
	@RequestMapping(value = "/changLang", method = RequestMethod.GET)
	public String changLang(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String adminId = request.getParameter("adminId");
		String langFlag = request.getParameter("langFlag");
		if(adminId != null){
			SysAdmin sysAdmin = sysAdminService.findById(adminId);
			sysAdmin.setLangCode(langFlag);
			sysAdminService.saveOrUpdate(sysAdmin,this.getLoginUser(request));
			request.getSession().setAttribute(CoreConstants.USER_LOGIN,sysAdmin);
			request.getSession().setAttribute(CoreConstants.LANG_CODE,langFlag);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
		return null; 
	}
	
	/**
	 * 修改密码
	 * 
	 * */
	@RequestMapping(value = "/editPsw", method = RequestMethod.GET)
	public String editPsw(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		SysAdmin loginUser = this.getLoginUser(request);
		String loginId = loginUser.getId();
		model.put("id", loginId);
		return "sys/admin/editPsw";
	}
	
	/**
	 * 验证密码--用于修改面貌
	 * */
	@RequestMapping(value = "/checkOraginPsw")
	public void checkOraginPsw(HttpServletRequest request,HttpServletResponse response,ModelMap model,String id,String oldPassword){
		String md5OldPsw = this.pwdEncoder.encodePassword(oldPassword).toUpperCase();
		boolean flag = sysAdminService.checkOraginPsw(id,md5OldPsw);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("valid",flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 新密码保存
	 * */
	@RequestMapping(value="/saveNewPsw",method=RequestMethod.POST)
	public void saveNewPsw (HttpServletRequest request,HttpServletResponse response,ModelMap model,String password){
		SysAdmin loginUser = this.getLoginUser(request);
		String md5New = this.pwdEncoder.encodePassword(password).toUpperCase();
		loginUser.setPassword(md5New);
		this.save(loginUser, request, response, model);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 账号信息与权限信息
	 * @author scshi
	 * @date 20160412
	 * */
	@RequestMapping(value = "/tab", method = RequestMethod.GET)
	public String tab(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String adminId = request.getParameter("id");
		model.put("id",adminId);
		return "sys/admin/tab";
	}
	/***
	 * 账号信息页面
	 * */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		SysAdmin sysAdmin = sysAdminService.findById(id);
		model.put("SysAdmin",sysAdmin);
		return "sys/admin/edit";
	}
	
	/**
	 * 账号角色页面
	 * */
	@RequestMapping(value = "/roleAuth", method = RequestMethod.GET)
	public String roleAuth(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String adminId = request.getParameter("id");
		model.put("adminId",adminId);
		return "sys/admin/roleAuth";
	}
}
