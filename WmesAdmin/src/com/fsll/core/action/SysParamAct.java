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
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.service.SysParamService;
import com.fsll.core.service.SysParamTypeService;

/****
 * 基础数据管理
 * 
 * @author simon
 * @version 1.0
 * @date 2016-04-11
 */
@Controller
@RequestMapping("/console/sys/param")
public class SysParamAct extends CoreBaseAct {
	@Autowired
	private SysParamService paramService;
	
	@Autowired
	private SysParamTypeService paramTypeService;

	/**
	 * 分页列表
	 * 
	 * @author simon
	 * @date 2016-04-11
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		
		List<SysParamType> list = paramTypeService.findAllParamType();
		model.put("typeListVO", list);
		
		return "console/sys/param/list";
	}

	/**
	 * 数据查询的方法
	 * 
	 * @author simon
	 * @date 2016-03-17
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
        jsonPaging = this.getJsonPaging(request);
		String name = request.getParameter("name");
		String typeId = request.getParameter("typeId");
		SysParamType paramType = new SysParamType();
		paramType.setId(typeId);
		paramType = paramTypeService.findById(paramType);
		SysParamConfig paramConfig = new SysParamConfig();
		if (null != name && !"".equals(name)) {
			try {
				name = URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		paramConfig.setConfigCode(name);
		paramConfig.setNameSc(name);
		paramConfig.setNameTc(name);
		paramConfig.setNameEn(name);
		paramConfig.setType(paramType);
		jsonPaging = paramService.findAll(jsonPaging, paramConfig);
		this.toJsonString(response, jsonPaging);
	}

	/****
	 * 添加用户表单的方法
	 * 
	 * @author simon
	 * @date 2016-3-18
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		SysParamConfig param = new SysParamConfig();
		List<SysParamType> paramTypeList = paramService.getAllParamTypeList();
		model.addAttribute("paramTypList", paramTypeList);
		model.addAttribute("param", param);
		return "console/sys/param/input";
	}

	/***
	 * 
	 * @author simon
	 * @date 2016-3-28
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
	public void loadTreeMedthom(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String treeStr = paramService.getLoadParamTree();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("treeStr", treeStr);
		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/****
	 * 保存用户表单的方法
	 * 
	 * @author simon
	 * @date 2016-3-18
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveobj(SysParamConfig paramconfig, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
	/*	
	 * String postData=request.getParameter("postData");
		String[] inputDataList = postData.split("&");
		Map<String,Object> memberMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			memberMap.put(memberStrArray[0], memberStrArray.length<2?"":memberStrArray[1]);
		}
		JSONObject memberDataJSON = (JSONObject)JSONSerializer.toJSON(memberMap);
		paramconfig.setId(memberDataJSON.getString("id"));
		paramconfig.setConfigCode(memberDataJSON.getString("id"));
		paramconfig.setConfValueEn(memberDataJSON.getString("id"));
		paramconfig.setConfValueSc(memberDataJSON.getString("id"));
		paramconfig.setConfValueTc(memberDataJSON.getString("id"));
		paramconfig.setIsValid(memberDataJSON.getString("id"));
		paramconfig.setNameEn(memberDataJSON.getString("id"));
		paramconfig.setNameSc(memberDataJSON.getString("id"));
		paramconfig.setNameTc(memberDataJSON.getString("id"));
		//paramconfig.setOrderBy(memberDataJSON.getString("id"));
		//paramconfig.setParent(memberDataJSON.getString("id"));
		paramconfig.setParentCode(memberDataJSON.getString("id"));
		paramconfig.setParentId(memberDataJSON.getString("id"));
		paramconfig.setParentName(memberDataJSON.getString("id"));
		//paramconfig.setType(memberDataJSON.getString("id"));
		paramconfig.setTypeCode(memberDataJSON.getString("id"));
		paramconfig.setTypeName(memberDataJSON.getString("id"));
		paramconfig.setXh(memberDataJSON.getString("id"));*/
		
		boolean isAdd = false;
		String typeid = request.getParameter("typeid");
		String parentId = request.getParameter("parentId");
		SysParamType type = new SysParamType();
		SysParamConfig parent = null;
		if (null != typeid && !"".endsWith(typeid)) {
			type.setId(typeid);
			type = paramService.findParamTypeById(type);
		}
		if (null != parentId && !"".endsWith(parentId)) {
			parentId = parentId.split(",")[0];
			parent = new SysParamConfig();
			parent.setId(parentId);
			parent = paramService.findById(parent);
		}
		SysParamConfig updateparam = null;
		if (null != paramconfig.getId() && !"".endsWith(paramconfig.getId())) {
			updateparam = paramService.findParamConfigById(paramconfig);
			isAdd = false;
		} else {
			isAdd = true;
			paramconfig.setId(null);
			paramconfig.setType(type);
		}
		if (null != updateparam) {
			updateparam.setConfigCode(paramconfig.getConfigCode());
			updateparam.setConfValueSc(paramconfig.getConfValueSc());
			updateparam.setConfValueTc(paramconfig.getConfValueTc());
			updateparam.setConfValueEn(paramconfig.getConfValueEn());
			updateparam.setNameSc(paramconfig.getNameSc());
			updateparam.setNameTc(paramconfig.getNameTc());
			updateparam.setNameEn(paramconfig.getNameEn());
			updateparam.setOrderBy(paramconfig.getOrderBy());
			if (ParentIsSelfObj(parent, updateparam)) {
				updateparam.setParent(parent);
			}
			updateparam.setIsValid(paramconfig.getIsValid());
			updateparam.setType(type);
			updateparam = paramService.saveOrUpdate(updateparam, isAdd);
		} else {
			paramconfig.setParent(parent);
			paramconfig = paramService.saveOrUpdate(paramconfig, isAdd);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("msg", PropertyUtils.getPropertyValue(
				this.getLoginLangFlag(request), "global.success", null));
		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/***
	 * 判断子节点和父节点是同一个
	 * 
	 * @author simon
	 * @date 2016-4-12
	 * @param parent
	 * @param updateobj
	 * @return
	 */
	private static boolean ParentIsSelfObj(SysParamConfig parent,
			SysParamConfig updateobj) {
		return null != parent && !parent.getId().endsWith(updateobj.getId());
	}

	/**
	 * 根据当前节点查询所有的父节点的方法
	 * 
	 * @author simon
	 * @date 2016-03-17
	 */
	@RequestMapping(value = "/nodeName", method = RequestMethod.GET)
	public void nodelist(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String nodeName = "";
		String paramId = request.getParameter("paramId");
		SysParamConfig paramconfig = new SysParamConfig();
		List<SysParamConfig> parentlist = new ArrayList<SysParamConfig>();
		if (null != paramId && !"".endsWith(paramId)) {
			paramId = paramId.split(",")[0];
			paramconfig.setId(paramId);
			paramconfig = paramService.findById(paramconfig);
			List<SysParamConfig> templist = new ArrayList<SysParamConfig>();
			parentlist.add(paramconfig);
			templist = getNodeName(paramconfig);
			if (null != templist && !templist.isEmpty()) {
				parentlist.addAll(templist);
			}
		}
		for (int i = parentlist.size() - 1; i != -1; i--) {
			if (i > 0) {
				nodeName += parentlist.get(i).getNameSc() + "/";
			} else {
				nodeName += parentlist.get(i).getNameSc();
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("nodeName", nodeName);
		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/***
	 * 根据节点拿到所有的子节点的方法
	 * 
	 * @author simon
	 * @date 2016-4-11
	 * @param companyobj
	 * @return
	 */
	private List<SysParamConfig> getNodeName(SysParamConfig paramconfig) {
		List<SysParamConfig> listtemp = new ArrayList<SysParamConfig>();
		List<SysParamConfig> listtemp1 = new ArrayList<SysParamConfig>();
		if (null != paramconfig && null != paramconfig.getParent()) {
			SysParamConfig tempobj = paramconfig.getParent();
			listtemp.add(tempobj);
			if (null != tempobj.getParent()) {
				listtemp1 = getNodeName(tempobj);
			}
			if (null != listtemp1 && !listtemp1.isEmpty()) {
				listtemp.addAll(listtemp1);
			}

		}
		return listtemp;
	}

	/****
	 * 验证域名是否已经 存在的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/checkParamNameExist")
	public void checkDomainExist(String name, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (null != name && !"".endsWith(name)) {
			try {
				name = URLDecoder.decode(name, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysParamConfig paramConfig = paramService.findBySysParamName(name);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (paramConfig != null) {
			obj.put("valid", false);
		} else {
			obj.put("valid", true);
		}
		JsonUtil.toWriter(obj, response);
	}

	/****
	 * 验证域名是否已经 存在的方法
	 * 
	 * @author simon
	 * @date 2016-3-23
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/checkConfigCodeExist")
	public void checksiteNameExist(String code, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (null != code && !"".endsWith(code)) {
			try {
				code = URLDecoder.decode(code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SysParamConfig paramconfig = paramService.findByCode(code);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (paramconfig != null) {
			obj.put("valid", false);
		} else {
			obj.put("valid", true);
		}
		JsonUtil.toWriter(obj, response);
	}

	/****
	 * 编辑用户表单的方法
	 * 
	 * @author simon
	 * @date 2016-3-18
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String edit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		SysParamConfig paramConfig = new SysParamConfig();
		List<SysParamType> paramTypeList = new ArrayList<SysParamType>();
		String id = request.getParameter("id");
		List<SysParamConfig> parentlist = new ArrayList<SysParamConfig>();
		String nodeName = "";
		if (null != id) {
			paramConfig.setId(id);
			paramConfig = paramService.findById(paramConfig);
			paramTypeList = paramService.getAllParamTypeList();
			if (!"".endsWith(id)) {
				id = id.split(",")[0];
				List<SysParamConfig> templist = new ArrayList<SysParamConfig>();
				parentlist.add(paramConfig);
				templist = getNodeName(paramConfig);
				if (null != templist && !templist.isEmpty()) {
					parentlist.addAll(templist);
				}
			}
			for (int i = parentlist.size() - 1; i != 0; i--) {
				if (i > 0) {
					nodeName += parentlist.get(i).getNameSc() + "/";
				} else {
					nodeName += parentlist.get(i).getNameSc();
				}
			}
		}
		if (null != paramConfig.getParent() && isNullName(nodeName)) {
			paramConfig.getParent().setNameSc(nodeName);
		}
		model.addAttribute("paramTypList", paramTypeList);
		model.addAttribute("param", paramConfig);
		return "console/sys/param/input";
	}

	/***
	 * 不能为空的方法
	 * 
	 * @author simon
	 * @date 2016-4-14
	 * @param nodeName
	 * @return
	 */
	public static boolean isNullName(String nodeName) {
		return null != nodeName && !"".endsWith(nodeName);
	}

	/****
	 * 删除APP用户的方法
	 * 
	 * @author simon
	 * @date 2016-3-22
	 * @param request
	 *            用户请求对象
	 * @param response
	 *            用户请求对象
	 * @param model
	 *            存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteObj(String ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		SysParamConfig paramConfig = new SysParamConfig();
		paramConfig.setId(ids);
		String message = "global.success";
		boolean exists = !paramService.checkChildrenExist(paramConfig);
		if (exists) {// 不存在子节点才让它去删除
			paramService.deleteObject(ids);
		} else {
			message = "param.children.exit";
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("msg", PropertyUtils.getPropertyValue(
				this.getLoginLangFlag(request), message, null));
		JsonUtil.toWriter(obj, response);
		return null;
	}

	/****
	 * 修改基础数据状态的方法
	 * 
	 * @author simon
	 * @date 2016-3-22
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveStatus", method = RequestMethod.GET)
	public void updatestatus(String ids, String status,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		paramService.saveStatus(ids, status);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("msg", PropertyUtils.getPropertyValue(
				this.getLoginLangFlag(request), "global.success", null));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 根据语言和typecode获取基础数据
	 * @author mqzou
	 * @date 2016-7-7
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/paramList", method = RequestMethod.POST)
	public void getParamList(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		String type= request.getParameter("type");
		String langCode=this.getLoginLangFlag(request);
		List list=paramService.getParamList(langCode, type);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", list);
		obj.put("msg", "");
		JsonUtil.toWriter(obj, response);
	}

}
