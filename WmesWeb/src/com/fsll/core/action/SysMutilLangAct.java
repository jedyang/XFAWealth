/**
 * 
 */
package com.fsll.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysLangConfig;
import com.fsll.core.service.SysMutilLangService;

/**
 * @author scshi_u330p
 * @date 20161228
 */
@Controller
@RequestMapping("/console/sys/mutilLang")
public class SysMutilLangAct extends CoreBaseAct{
	
	@Autowired
	private SysMutilLangService sysMutilLangService;
	/**
	 * 多语言配置页面
	 * @author scshi_u330p
	 * @date20161228
	 * */
	@RequestMapping(value = "/langPage", method = RequestMethod.GET)
	public String mutilLangPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		return "console/sys/mutilLang/langPage";
	}
	
	/**
	 * 多语言列表
	 * @author scshi_u330p
	 * @date 20161229
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public void loadLangProperty(HttpServletRequest request,HttpServletResponse response,ModelMap model,SysLangConfig sVo){
		jsonPaging=this.getJsonPaging(request);
		jsonPaging = sysMutilLangService.loadMutilLangPage(jsonPaging,sVo);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *初始化 sys_lang_config表
	 *@author scshi_u330p
	 *@date 20161229
	 * */
	@RequestMapping(value = "/readProperty", method = RequestMethod.POST)
	public void readProperty (HttpServletRequest request,HttpServletResponse response,ModelMap model){
		sysMutilLangService.readPropertyToDb();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 到处数据表到配置文件
	 * @author stxxx
	 * @date 20161229
	 * */
	@RequestMapping(value="exportDbToProp",method = RequestMethod.POST)
	public void exportDbToProp(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		sysMutilLangService.exportDbToProperty();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 配置文件增改
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	@RequestMapping(value="detailPage",method = RequestMethod.GET)
	public String detailPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String id = request.getParameter("id");
		SysLangConfig langObj = new SysLangConfig();
		if(StringUtils.isNotBlank(id)){
			langObj= sysMutilLangService.loadLangConfigById(id);
		}
		model.put("langObj", langObj);
		return "console/sys/mutilLang/detail";
	}
	
	/**
	 * 配置文件保存
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	@RequestMapping(value="propSave",method = RequestMethod.POST)
	public void propSave(HttpServletRequest request,HttpServletResponse response,ModelMap model,SysLangConfig langObj){
		sysMutilLangService.propSave(langObj);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}
	
	
}
