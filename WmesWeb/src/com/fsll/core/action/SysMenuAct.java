package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.service.SysMenuService;

/****
 * 基础数据类型管理
 * @author tan
 * @version 1.0
 * @date 2016-07-25
 */
@Controller
@RequestMapping("/console/sys/menu")
public class SysMenuAct extends CoreBaseAct {
	@Autowired
	private SysMenuService sysMenuService;
	 
	/**
	 * 分页列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/sys/menu/list";
	}

	/**
	 * 数据查询的方法
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String keyword=request.getParameter("keyword");
		SysMenu menu=new SysMenu();
		if(null!=keyword&&!"".equals(keyword)){
			try {
				keyword=URLDecoder.decode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		menu.setNameSc(keyword);
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=sysMenuService.findAll(jsonPaging, menu);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/****
	 * 添加用户表单的方法
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		SysMenu menu=new SysMenu();
		List<SysMenu> list = sysMenuService.findAllMenu();

	    String parentId = request.getParameter("parentId");
	    if(null!=parentId){
	    	SysMenu parentMenu = new SysMenu();
	    	parentMenu.setId(parentId);	    	
	    	parentMenu=sysMenuService.findMenuById(parentMenu);
	    	menu.setParent(parentMenu);
	    }
	    
		model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/sys/menu/input";
	}
	
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		SysMenu menu=new SysMenu();
		List<SysMenu> list = sysMenuService.findAllMenu();
	    String id=request.getParameter("id");
	    if(null!=id){
	    	menu.setId(id);
	    	menu=sysMenuService.findMenuById(menu);
	    }
	    model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/sys/menu/input";
	}
	
	/****
	 * 保存用户表单的方法
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveobj(SysMenu menu,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String actionMsg="global.success";
		
		String parentId = request.getParameter("parentId");
		if(null != parentId && !"".equals(parentId) ){
			SysMenu parent = new SysMenu();
			parent.setId(parentId);
			parent = sysMenuService.findMenuById(parent);
			menu.setParent(parent);
		}
		
		SysMenu updateparam=null;
		if(null!= menu.getId() &&!"".endsWith(menu.getId())){
			updateparam=sysMenuService.findMenuById(menu);
			isAdd=false;  
		}else{
			isAdd=true;
			menu.setId(null);
		}
		if(null!=updateparam){
			updateparam.setParent(menu.getParent());
			updateparam.setNameSc(menu.getNameSc());
			updateparam.setNameTc(menu.getNameTc());
			updateparam.setNameEn(menu.getNameEn());
			updateparam.setOrderBy(menu.getOrderBy());
			updateparam.setWebUrl(menu.getWebUrl());
			updateparam.setIsValid(menu.getIsValid());
			updateparam.setCode(menu.getCode());
			updateparam=sysMenuService.saveOrUpdate(updateparam,isAdd);
		}else{
			menu=sysMenuService.saveOrUpdate(menu,isAdd);
		}
		
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",msg);

		JsonUtil.toWriter(obj, response);
	}
	
//	/****
//	 * 验证是否已经 存在的方法
//	 * @param request 用户请求对象
//	 * @param response 用户请求对象
//	 * @param model 存储对象
//	 * @return 返回添加用户页面
//	 */
//	@RequestMapping(value = "/checkTypeNameExist")
//	public void checkDomainExist(String name,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		if(null!=name&&!"".endsWith(name)){
//			try {
//				name=URLDecoder.decode(name, "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
//		SysParamType paramConfig =paramTypeService.findByParamTypeName(name);
//		Map<String, Object> obj = new HashMap<String, Object>();
//		if(paramConfig != null){
//			obj.put("valid",false);
//		}else{
//			obj.put("valid",true);
//		}
//		JsonUtil.toWriter(obj, response);
//	}
//	
//	/****
//	 * 验证域名是否已经 存在的方法
//	 * @author simon
//	 * @date 2016-3-23
//	 * @param request 用户请求对象
//	 * @param response 用户请求对象
//	 * @param model 存储对象
//	 * @return 返回添加用户页面
//	 */
//	@RequestMapping(value = "/checkTypeCodeExist")
//	public void checksiteNameExist(String code,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		if(null!=code&&!"".endsWith(code)){
//			try {
//				code=URLDecoder.decode(code, "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
//		SysParamType paramconfig=paramTypeService.findByCode(code);
//		Map<String, Object> obj = new HashMap<String, Object>();
//		if(paramconfig != null){
//			obj.put("valid",false);
//		}else{
//			obj.put("valid",true);
//		}
//		JsonUtil.toWriter(obj, response);
//	}

//	/****
//	 * 删除APP用户的方法
//	 * @author simon
//	 * @date 2016-3-22
//	 * @param request 用户请求对象
//	 * @param response 用户请求对象
//	 * @param model 存储对象
//	 * @return 返回添加用户页面
//	 */
//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
//	public String deleteObj(String ids,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		SysParamType paramType=new SysParamType();
//		paramType.setId(ids);
//		String actionMsg="global.success";
//		paramType=paramTypeService.findById(paramType);
//		if(null!=paramType.getSysParamConfigSet()&&!paramType.getSysParamConfigSet().isEmpty()){
//			actionMsg="paramType.action.chilren.exist";//如果已经做的关联是不能删除数据的
//		}else{
//			paramTypeService.deleteObject(ids);
//		}
//		Map<String, Object> obj = new HashMap<String, Object>();
//		obj.put("result",true);
//		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
//		JsonUtil.toWriter(obj, response);
//		return null;
//	}
//	/****
//	 * 修改基础数据状态的方法
//	 * @author simon
//	 * @date 2016-3-22
//	 * @param request 用户请求对象
//	 * @param response 用户请求对象
//	 * @param model 存储对象
//	 * @return 返回添加用户页面
//	 */
//	@RequestMapping(value = "/saveStatus", method = RequestMethod.GET)
//	public void updatestatus(String ids,String status,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
//		String actionMsg="global.success";
//		paramTypeService.saveStatus(ids,status);
//		Map<String, Object> obj = new HashMap<String, Object>();
//		obj.put("result",true);
//		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
//		JsonUtil.toWriter(obj, response);
//	}

}
