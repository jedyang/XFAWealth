package com.fsll.core.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.SysLog;
import com.fsll.core.service.SysLogService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;

/**
 * 控制器:系统日志信息管理
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-6-27
 */

@Controller
@RequestMapping("/console/sys/log")
public class SysLogAct extends CoreBaseAct {

	@Autowired
	private SysLogService sysLogService;
	
    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/sys/log/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
        String adminType = memberAdmin.getType();
        jsonPaging = this.getJsonPaging(request);
        if(CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equalsIgnoreCase(adminType)){
	    	String loginCode = request.getParameter("loginCode");
	        String moduleType = request.getParameter("moduleType");
	//        String remark = request.getParameter("remark");
	        String startDate = request.getParameter("startDate");
	        String endDate = request.getParameter("endDate");
	
	    	SysLog log = new SysLog();
	    	log.setLoginCode(loginCode);
	//    	log.setNickName(loginCode);
	    	log.setModuleType(moduleType);
	//    	log.setRemark(remark);
	    	log.setStartTime(startDate);
	    	log.setEndTime(endDate);  	
	        
	        jsonPaging = sysLogService.findAll(jsonPaging, log);
        }else {
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
        SysLog log = sysLogService.findById(id);
        model.put("logVO", log);
        return "console/sys/log/input";
    }
}
