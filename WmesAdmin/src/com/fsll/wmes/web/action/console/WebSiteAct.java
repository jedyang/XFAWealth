package com.fsll.wmes.web.action.console;

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
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.web.service.WebSiteService;

/**
 * 控制器:网站管理
 * @author wwluo
 * @version 1.0
 * @date 2016-08-01
 */
@Controller
public class WebSiteAct extends WmesBaseAct{
	
	@Autowired
	 private WebSiteService webSiteService;
	
	/**
	 * 跳转设置页面
	 * @author wwluo
	 * @date 2016-08-01
	 */
	@RequestMapping(value = "/console/website/list")
	public String mailServiceList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String typeCode = request.getParameter("type");
		List<Object> sysParamConfig = webSiteService.findByCode(typeCode);
		String configJson = JsonUtil.toJson(sysParamConfig);
		model.put("typeCode", typeCode);
		model.put("configJson", configJson);
		return "console/website/input";
	}
	
	/**
	 * 更新设置
	 * @author wwluo
	 * @date 2016-08-01
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/console/website/update", method = RequestMethod.POST)
	public void mailServiceupdate(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
        String listJson = request.getParameter("listObj");
        String lang = request.getParameter("lang");
        List<Map> list = JsonUtil.toListMap(listJson);
        int count = webSiteService.update(list,lang);
        Map<String, Object> obj = new HashMap<String, Object>();
        if(count == list.size())
		    obj.put("flag",true);
        else
        	obj.put("flag",false);
		JsonUtil.toWriter(obj, response);
	}
	
}
