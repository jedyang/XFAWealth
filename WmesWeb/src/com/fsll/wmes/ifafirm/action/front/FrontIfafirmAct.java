package com.fsll.wmes.ifafirm.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;

/**
 * @author wwlin
 * 
 */
@Controller
@RequestMapping("/front/ifafirm/info")
public class FrontIfafirmAct extends WmesBaseAct {
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private IfafirmManageService iFAFrimService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysCountryService sysCountryService;
	/**
	 * 
	 * @author wwlin
	 * @date 2016-08-15 
	 */
	@RequestMapping(value = "/myifateamlist", method = RequestMethod.GET)
	public String IfaTeamList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return "ifaFirm/myIfaTeam";
	}
	
	/**
	 * 分页获得记录，供前台调用
	 * 
	 * @author 林文伟
	 * 
	 */
	@RequestMapping(value = "/getifateamlistJson", method = RequestMethod.POST)
	public void getIfaTeamListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		MemberIndividual investor = null;
		if(null!=loginUser){
			int memberType = loginUser.getMemberType();
			if(memberType==2){
				String memberId = loginUser.getId();
				MemberIfa ifa =  memberBaseService.findIfaMember(memberId);
				if(null!=ifa&&""!=ifa.getId()){
					String ifaId = ifa.getId();
					IfaListfiltersVO filtersVO = new IfaListfiltersVO();
					String lang = this.getLoginLangFlag(request);
					String keyword = toUTF8String(request.getParameter("keyword"));

					jsonPaging = this.getJsonPaging(request);
					jsonPaging = ifaManageService.loadIFAMyTeamList(jsonPaging,ifaId, keyword);
					// this.toJsonString(response, jsonPaging);
					model.put("jsonPaging", jsonPaging);

					this.toJsonString(response, jsonPaging);
					// ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
				}
			}	
		}
		
	}
}
