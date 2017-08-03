package com.fsll.wmes.crm.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.MemberAdmin;

/**
 * 控制器:投资方案信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-6-28
 */

@Controller
@RequestMapping("/console/crm/proposal")
public class ConsoleCrmProposalAct extends WmesBaseAct {
	
	@Autowired
	private CrmProposalService crmProposalService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/crm/proposal/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
    	        
    	String keyword = request.getParameter("keyword");
	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = crmProposalService.findAllProposal(jsonPaging, keyword,memberAdmin);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息
     */
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getProposalDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("id");
    	CrmProposal obj = crmProposalService.findProposalById(id);
    	model.put("proposalVO", obj);
    	return "console/crm/proposal/input";
    }
}
