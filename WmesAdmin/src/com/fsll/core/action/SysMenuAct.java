package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.service.SysMenuService;

/****
 * 后台菜单管理
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
	    SysMenu parentMenu=sysMenuService.findMenuById(parentId);
	    if(null != parentMenu){
	    	menu.setParent(parentMenu);
	    }
		model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/sys/menu/input";
	}
	/**
	 * 编辑
	 * @author qgfeng
	 * @date modify 2017-1-19
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		SysMenu menu=sysMenuService.findMenuById(id);
		List<SysMenu> list = sysMenuService.findAllMenu();
		
	    model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/sys/menu/input";
	}
	
	/****
	 * 保存用户表单的方法
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面 modify qgfeng
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveMenu(SysMenu menu,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String parentId = request.getParameter("parentId");
			SysMenu parent = sysMenuService.findMenuById(parentId);
			if(null != parent){
				menu.setParent(parent);
			}
			SysMenu updateparam = sysMenuService.findMenuById(menu.getId());;
			if(null!=updateparam){
				updateparam.setParent(menu.getParent());
				updateparam.setNameSc(menu.getNameSc());
				updateparam.setNameTc(menu.getNameTc());
				updateparam.setNameEn(menu.getNameEn());
				updateparam.setOrderBy(menu.getOrderBy());
				updateparam.setWebUrl(menu.getWebUrl());
				updateparam.setIsValid(menu.getIsValid());
				updateparam.setCode(menu.getCode());
				updateparam=sysMenuService.saveOrUpdate(updateparam,false);
			}else{
				menu.setId(null);
				menu=sysMenuService.saveOrUpdate(menu,true);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 菜单删除
	 * @author qgfeng
	 * @date20161103
	 * */	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteFrontMenu(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String menuId = request.getParameter("id");
			result = sysMenuService.deleteMenu(menuId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}

	/**
	 * 获取系统菜单集合
	 * @author wwluo
	 * @data 2017-06-16
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String keyWord = request.getParameter("keyword");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = sysMenuService.findSysMenu(jsonPaging, keyWord);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 删除菜单（物理删除，包含子菜单）
	 * @author wwluo
	 * @date 2017-06-16
	 */
	@RequestMapping(value = "/deleteSysMenu", method = RequestMethod.POST)
	public void deleteSysMenu(String id, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Boolean flag = false;
		flag = sysMenuService.deleteSysMenu(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
}
