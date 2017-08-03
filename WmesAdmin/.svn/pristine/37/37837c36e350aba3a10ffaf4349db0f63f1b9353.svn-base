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
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.strategy.service.StrategyCountService;

/**
 * 控制器:观点信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-8-19
 */

@Controller
@RequestMapping("/console/strategy/count")
public class ConsoleStrategyCountAct extends WmesBaseAct {
	
	@Autowired
	private StrategyCountService strategyCountService;
	
    
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getInsightInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("infoId");
    	StrategyCount obj = strategyCountService.findInsightById(id);
    	model.put("infoVO", obj);
    	return "console/strategy/count/input";
    }
}
