package com.fsll.wmes.crm.action.console;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.CrmProposalVO;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

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
	@Autowired
	private SysParamService paramService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        String langCode = this.getLoginLangFlag(request);
        List<MemberIfafirmVO> ifafirms = ifafirmManageService.getIfaFirms(langCode);
        model.put("ifafirms", ifafirms);
        return "console/crm/proposal/list";//页面摆放路径
    }
    
    /**
     * 详细信息
     */
    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String getProposalDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id = request.getParameter("id");
    	CrmProposal obj = crmProposalService.findProposalById(id);
    	String currCode = obj.getBaseCurrId();
    	String langCode = this.getLoginLangFlag(request);
    	model.put("currCode", StringUtils.isBlank(currCode)?"":paramService.findNameByCode(currCode, langCode));
    	model.put("proposalVO", obj);
    	return "console/crm/proposal/input";
    }
    
    /**
     * 分页获得记录
     * @author wwluo
     * @date 2017/06/06
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public void listJson(CrmProposalVO proposalVO,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	jsonPaging = this.getJsonPaging(request);
        jsonPaging = crmProposalService.findAllProposal(jsonPaging, proposalVO, langCode);
        this.toJsonString(response, jsonPaging);
    }
}
