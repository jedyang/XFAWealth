package com.fsll.wmes.web.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.entity.WebBusLog;
import com.fsll.wmes.web.service.WebBusLogService;

/**
 * 控制器:系统日志信息管理
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-4 
 */

@Controller
@RequestMapping("/console/buslog")
public class WebBusLogAct extends CoreBaseAct {

	@Autowired
	private WebBusLogService webBusLogService;
	
    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/buslog/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	jsonPaging = this.getJsonPaging(request);	
  
    	//获取界面搜索参数
    	String loginCode = request.getParameter("loginCode");
        String moduleType = request.getParameter("moduleType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        
        //设置搜索条件
    	WebBusLog log = new WebBusLog();
    	log.setLoginCode(loginCode);
    	log.setModuleType(moduleType);
    	log.setStartTime(startDate);
    	log.setEndTime(endDate);
    	
        jsonPaging = webBusLogService.findAll(jsonPaging, log);
    	
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String id = request.getParameter("id");
        model.put("id", id);
        WebBusLog log = webBusLogService.findById(id);
        model.put("logVO", log);
        return "console/buslog/input";
    }
}
