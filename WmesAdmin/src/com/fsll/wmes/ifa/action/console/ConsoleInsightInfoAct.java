package com.fsll.wmes.ifa.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.ifa.service.InsightInfoService;

/**
 * 控制器:观点信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-18
 */

@Controller
@RequestMapping("/console/insight/info")
public class ConsoleInsightInfoAct extends WmesBaseAct {
	
	@Autowired
	private InsightInfoService insightInfoService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/insight/info/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String keyword = request.getParameter("keyword");
    	String lang = getLoginLangFlag(request);
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = insightInfoService.findAllInsight(jsonPaging, keyword, lang);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getInsightInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("id");
    	String lang = getLoginLangFlag(request);
    	InsightInfo obj = insightInfoService.findInsightById(id, lang);
    	model.put("infoVO", obj);
    	return "console/insight/info/input";
    }
}
