package com.fsll.wmes.web.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.WebInterfaceLog;
import com.fsll.wmes.web.service.WebInterfaceLogService;

/**
 * 控制器:系统日志信息管理
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-4 
 */

@Controller
@RequestMapping("/console/interfacelog")
public class WebInterfaceLogAct extends CoreBaseAct {

	@Autowired
	private WebInterfaceLogService webInterfaceLogService;
	
    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/interfacelog/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	jsonPaging = this.getJsonPaging(request);
    	MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
        String adminType = memberAdmin.getType();
        if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(adminType)) {
        	String invokerType = request.getParameter("invokerType");
            String moduleType = request.getParameter("moduleType");
            String resultFlag = request.getParameter("resultFlag");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");

        	WebInterfaceLog log = new WebInterfaceLog();
        	log.setInvokerType(invokerType);
        	log.setModuleType(moduleType);
        	log.setResultFlag(resultFlag);
        	log.setStartTime(startDate);
        	log.setEndTime(endDate);    	
        	
//            jsonPaging = this.getJsonPaging(request);
            
            jsonPaging = webInterfaceLogService.findAll(jsonPaging, log);
		}
        else {
			jsonPaging.setList(null);
		}
    	

        
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
        WebInterfaceLog log = webInterfaceLogService.findById(id);
        model.put("logVO", log);
        return "console/interfacelog/input";
    }
}
