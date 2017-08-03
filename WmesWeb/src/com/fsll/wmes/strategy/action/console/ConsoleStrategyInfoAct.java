package com.fsll.wmes.strategy.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.strategy.service.StrategyInfoService;

/**
 * 控制器:基金信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/console/strategy/info")
public class ConsoleStrategyInfoAct extends WmesBaseAct {
	
	@Autowired
	private StrategyInfoService strategyInfoService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/strategy/info/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String keyword = request.getParameter("keyword");
	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = strategyInfoService.findAll(jsonPaging, keyword);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getProposalDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("id");
    	StrategyInfo obj = strategyInfoService.findById(id);
    	model.put("strategyVO", obj);
    	return "console/strategy/info/input";
    }
}
