package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
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
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.SysSiteService;

/****
 * 语言设置管理
 * @author simon
 * @version 1.0
 * @date 2016-04-11
 */
@Controller
@RequestMapping("/console/sys/site")
public class SysSiteAct extends CoreBaseAct {
	@Autowired
	private SysSiteService sysSiteService;
	 
	/**
	 * 分页列表
	 * @author simon
	 * @date 2016-04-11 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/sys/site/list";
	}

	/**
	 * 数据查询的方法
	 * @author simon
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String name=request.getParameter("siteName");
		SysSite site=new SysSite();
		if(null!=name&&!"".equals(name)){
			try {
				name=URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		site.setSiteName(name);
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=sysSiteService.findAll(jsonPaging, site);
		this.toJsonString(response, jsonPaging);
	}
	/****
	 * 添加用户表单的方法
	 * @author simon
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		SysSite site=new SysSite();
		model.addAttribute("site", site);
		return "console/sys/site/input";
	}
	/****
	 * 保存用户表单的方法
	 * @author simon
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveobj(SysSite site,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		SysSite updatesite=null;
		if(null!=site.getId()&&!"".endsWith(site.getId())){
			updatesite=sysSiteService.findSiteById(site);
			isAdd=false;  
		}else{
			isAdd=true;
			site.setId(null);
		}
		if(null!=updatesite){
			updatesite.setDomain(site.getDomain());
			updatesite.setIsMaster(site.getIsMaster());
			updatesite.setDefLang(site.getDefLang());
			updatesite.setShortName(site.getShortName());
			updatesite.setSiteName(site.getSiteName());
			updatesite.setSitePath(site.getSitePath());
			updatesite=sysSiteService.saveOrUpdate(updatesite,isAdd);
		}else{
			site=sysSiteService.saveOrUpdate(site,isAdd);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	/****
	 * 验证域名是否已经 存在的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/checkdomainExist")
	public void checkDomainExist(String domain,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		if(null!=domain&&!"".endsWith(domain)){
			try {
				domain=URLDecoder.decode(domain, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysSite site =sysSiteService.findBydomain(domain);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(site != null){
			obj.put("valid",false);
		}else{
			obj.put("valid",true);
		}
		JsonUtil.toWriter(obj, response);
	}
	/****
	 * 验证域名是否已经 存在的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/checksiteNameExist")
	public void checksiteNameExist(String siteName,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		if(null!=siteName&&!"".endsWith(siteName)){
			try {
				siteName=URLDecoder.decode(siteName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysSite site=sysSiteService.findBysiteName(siteName);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(site != null){
			obj.put("valid",false);
		}else{
			obj.put("valid",true);
		}
		JsonUtil.toWriter(obj, response);
	}
	/****
	 * 编辑用户表单的方法
	 * @author simon
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		SysSite site=new SysSite();
	    String id=request.getParameter("id");
	    if(null!=id){
	    	site.setId(id);
	    	site=sysSiteService.findSiteById(site);
	    }
	    model.addAttribute("site", site);
		return "console/sys/site/input";
	}
	
	/****
	 * 删除APP用户的方法
	 * @author simon
	 * @date 2016-3-22
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteObj(String ids,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		sysSiteService.deleteObject(ids);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		JsonUtil.toWriter(obj, response);
		return null;
	}

}
