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
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.service.SysParamTypeService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;

/****
 * 基础数据类型管理
 * @author simon
 * @version 1.0
 * @date 2016-04-11
 */
@Controller
@RequestMapping("/console/sys/paramType")
public class SysParamTypeAct extends CoreBaseAct {
	@Autowired
	private SysParamTypeService paramTypeService;
	 
	/**
	 * 分页列表
	 * @author simon
	 * @date 2016-04-11 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/sys/paramType/list";
	}

	/**
	 * 数据查询的方法
	 * @author simon
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
    	MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
        String adminType = memberAdmin.getType();
        jsonPaging = this.getJsonPaging(request);
        if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equalsIgnoreCase(adminType)){
			String name=request.getParameter("name");
			SysParamType paramType=new SysParamType();
			if(null!=name&&!"".equals(name)){
				try {
					name=URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			paramType.setName(name);
//			jsonPaging=this.getJsonPaging(request);
			jsonPaging=paramTypeService.findAll(jsonPaging, paramType);
        }
        else {
			jsonPaging.setList(null);
		}
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
		SysParamType paramType=new SysParamType();
		model.addAttribute("paramType", paramType);
		return "console/sys/paramType/input";
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
	public void saveobj(SysParamType paramType,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String actionMsg="global.success";
		
		SysParamType updateparam=null;
		if(null!=paramType.getId()&&!"".endsWith(paramType.getId())){
			updateparam=paramTypeService.findById(paramType);
			isAdd=false;  
		}else{
			isAdd=true;
			paramType.setId(null);
		}
		if(null!=updateparam){
			updateparam.setName(paramType.getName());
			updateparam.setIsValid(paramType.getIsValid());
			updateparam.setTypeCode(paramType.getTypeCode());
			updateparam=paramTypeService.saveOrUpdate(updateparam,isAdd);
		}else{
			paramType=paramTypeService.saveOrUpdate(paramType,isAdd);
		}
		
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",msg);

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
	@RequestMapping(value = "/checkTypeNameExist")
	public void checkDomainExist(String name,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		if(null!=name&&!"".endsWith(name)){
			try {
				name=URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysParamType paramConfig =paramTypeService.findByParamTypeName(name);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(paramConfig != null){
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
	@RequestMapping(value = "/checkTypeCodeExist")
	public void checksiteNameExist(String code,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		if(null!=code&&!"".endsWith(code)){
			try {
				code=URLDecoder.decode(code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysParamType paramconfig=paramTypeService.findByCode(code);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(paramconfig != null){
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
		SysParamType paramType=new SysParamType();
	    String id=request.getParameter("id");
	    if(null!=id){
	    	paramType.setId(id);
	    	paramType=paramTypeService.findById(paramType);
	    }
	    model.addAttribute("paramType", paramType);
		return "console/sys/paramType/input";
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
		SysParamType paramType=new SysParamType();
		paramType.setId(ids);
		String actionMsg="global.success";
		paramType=paramTypeService.findById(paramType);
		if(null!=paramType.getSysParamConfigSet()&&!paramType.getSysParamConfigSet().isEmpty()){
			actionMsg="paramType.action.chilren.exist";//如果已经做的关联是不能删除数据的
		}else{
			paramTypeService.deleteObject(ids);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
		return null;
	}
	/****
	 * 修改基础数据状态的方法
	 * @author simon
	 * @date 2016-3-22
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveStatus", method = RequestMethod.GET)
	public void updatestatus(String ids,String status,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String actionMsg="global.success";
		paramTypeService.saveStatus(ids,status);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}

}
