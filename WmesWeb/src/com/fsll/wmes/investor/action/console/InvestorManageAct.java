package com.fsll.wmes.investor.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.MemberSsoVO;

/*****
 * Investor开户
 * 
 * @author mqzou 2016-06-28
 */
@Controller
public class InvestorManageAct extends CoreBaseAct {

	@Autowired
	private InvestorServiceForConsole investorService;
	@Autowired
	private MemberManageServiceForConsole memberManageServiceForConsole;
	@Autowired
	private MemberBaseService memberBaseService;

	
	/**
	 * 待审批的投资人开户列表分页列表
	 * 
	 * @author mqzou
	 * @date  2016-08-03
	 */
	@RequestMapping(value = "/console/investor/approvalList")
	public String approvalList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		this.isMobileDevice(request, model);
		return "console/investor/approvallist";
	}
	
	/**
	 * 获取待审批的投资人开户列表
	 * 
	 * @author mqzou
	 * @date 2016-08-03
	 */
	@RequestMapping(value = "/console/investor/approvalJsonList")
	public void findInvestorAccountForApprove(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		MemberBase curMember=getLoginUser(request);
		String keyWord = request.getParameter("");
		if (null != keyWord && !"".equals(keyWord)) {
			try {
				keyWord = URLDecoder.decode(keyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(curMember.getId());
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR==curMember.getMemberType()){
			InvestorAccount account = new InvestorAccount();
			MemberDistributor distributor = admin.getDistributor();
			

			account.setDistributor(distributor);

			jsonPaging = this.getJsonPaging(request);
			jsonPaging = investorService.findInvestorAccountForApproval(jsonPaging, account);
		}else {
			jsonPaging=new JsonPaging();
		}
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取投资人账户列表
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	@RequestMapping(value = "/console/investor/JsonList")
	public void findInvestorAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);

		String memberId=request.getParameter("memberId");
		String langCode=getLoginLangFlag(request);
		InvestorAccount account =new InvestorAccount();
		if(null!=memberId && !"".equals(memberId)){
		    MemberBase memberBase=memberBaseService.findById(memberId);
		    account.setMember(memberBase);
		}
		
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==curMember.getMemberType()){
			MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(curMember.getId());
			MemberIfa ifa=new MemberIfa();
			ifa.setIfafirm(admin.getIfafirm());
			account.setIfa(ifa);
		}
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=investorService.findInvestorAccountList(jsonPaging, account, langCode);

		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 投资人开户列表分页列表
	 * 
	 * @author mqzou
	 * @date  2016-08-23
	 */
	@RequestMapping(value = "/console/investor/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String id=request.getParameter("id");
		model.put("id",	 id);
		return "console/investor/list";
	}
	
	/**
	 * 投资人账户详细信息
	 * 
	 * @author mqzou
	 * @date  2016-08-23
	 */
	@RequestMapping(value = "/console/investor/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		this.isMobileDevice(request, model);
		String id=request.getParameter("id");
		InvestorAccount account=investorService.findById(id);
		
	    model.put("account", account);
		return "console/investor/input";
	}
	

}
