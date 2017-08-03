package com.fsll.wmes.web.action.console;

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
import com.fsll.wmes.entity.WfProcedureAction;
import com.fsll.wmes.entity.WfProcedureInfo;
import com.fsll.wmes.web.service.WorkFlowService;

/*****
 * 邮件管理
 * @author 林文伟
 * 2016-06-23
 */
@Controller
public class WorkFlowAct extends CoreBaseAct {

	@Autowired
	 private WorkFlowService workflowService;
	 
	
	/**
	 * 分页列表
	 * @author 林文伟
	 * @date 2016-06-29
	 */
	@RequestMapping(value = "/console/workflow/info_list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/workflow/info_list";
	}
	
	/**
	 * 流程的动作列表
	 * @author 林文伟
	 * @date 2016-06-29
	 */
	@RequestMapping(value = "/console/workflow/action_list")
	public String actionList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String procedureId=request.getParameter("procedureid");
		model.put("procedureId", procedureId);
		return "console/workflow/action_list";
	}
	/**
	 * 查看明细页面
	 * @author 林文伟
	 * @date 2016-06-23 
	 */
	 @RequestMapping(value = "/console/workflow/detail")
	 public String view(HttpServletRequest request,HttpServletResponse
	 response,ModelMap model) {
	 String id=request.getParameter("id");
	 model.put("id", id);
	 WfProcedureInfo obj = workflowService.findById(id);
	 
	 model.put("WfProcedureInfoVO", obj);
	 return "console/workflow/info_form";
	 }
	/**
	 * 数据查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 * 
	 * modify 流程编码查询   rqwang 170510
	 */
	@RequestMapping(value = "/console/workflow/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String name=request.getParameter("name");
		String invokecode = request.getParameter("invokecode");
		if(null!=name&&!"".equals(name)){
			try {
				name=URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(null!= invokecode && !"".equals(invokecode)){
			try {
				invokecode=URLDecoder.decode(invokecode, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		WfProcedureInfo info=new WfProcedureInfo();
		info.setProcedureName(name);
		info.setInvokCode(invokecode);
		
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=workflowService.findAll(jsonPaging, info);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 通过流程ID获取所有动作
	 * @author mqzou
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/workflow/listActionJson", method = RequestMethod.POST)
	public void listActionJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String procedureId=request.getParameter("procedureid");
		//WfProcedureAction action=new WfProcedureAction();
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=workflowService.findActionAll(jsonPaging, procedureId);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 流程表单
	 * @author 林文伟
	 * @date 2016-06-29
	 */
	@RequestMapping(value = "/console/workflow/info_form")
	public String infoForm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/workflow/info_form";
	}
	
	/****
	 * 保存用户表单的方法
	 * @author 
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/console/workflow/saveinfo", method = RequestMethod.POST)
	public void saveInfo(WfProcedureInfo info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		String invokeCode=request.getParameter("invokecode");
		info.setId(id);
		info.setProcedureName(name);
		info.setInvokCode(invokeCode);
		if(null!=info.getId()&&!"".endsWith(info.getId())){

			isAdd=false;  
		}else{
			isAdd=true;
			info.setId(null);
			info.setIsDel("S");

		}

		info=workflowService.saveOrUpdate(info,isAdd);
		
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除流程
	 * @author 林文伟
	 * @date 2016-6-28
	 */
	@RequestMapping(value = "/console/workflow/deleteinfo", method = RequestMethod.POST)
	public void delInfo(WfProcedureInfo info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String procedureid=request.getParameter("procedureid");
		Boolean rs = workflowService.deleteById(procedureid);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		if(rs)
		{
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} 
		else
		{
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		}
		
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
}

