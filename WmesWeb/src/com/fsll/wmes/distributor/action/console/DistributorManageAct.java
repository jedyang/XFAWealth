package com.fsll.wmes.distributor.action.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.member.service.MemberBaseService;

/**
 * @author mqzou
 * Distributor操作类
 */

@Controller
@RequestMapping("/console/distributor/info")
public class DistributorManageAct extends WmesBaseAct{
	@Autowired
	private InvestorService investorService;
	@Autowired
	private DistributorService distributorService;
	

	
	/**
     * Distributor 的客户账户列表
     * @author mqzou
     * @date 2016-09-19
     */
	@RequestMapping(value = "/clientManagement", method = RequestMethod.GET)
	public String ifaAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
        String langCode=getLoginLangFlag(request);
		if(null!=memberAdmin){
			String inUse=request.getParameter("in_use");//账户状态
			String inApproval=request.getParameter("inApproval");
			String cancellation=request.getParameter("cancellation");
			String currency=request.getParameter("cur");//基本货币
			
			String status="";
			if(null!=inUse && "1".equals(inUse)){
				status+="'1',";
			}
			if(null!=inApproval && "1".equals(inApproval)){
				status+="-1";
			}
			if(status.endsWith(",")){
				status=status.substring(0,status.length()-1);
			}
			
			String isValid="";
			if(null!=cancellation && "1".equals(cancellation)){
				isValid="0";
				
			}
			MemberDistributor distributor=memberAdmin.getDistributor();
			InvestorAccount account=new InvestorAccount();
			account.setDistributor(distributor);
			//account.setMember(loginUser);
			account.setOpenStatus(status);
			account.setBaseCurrency(currency);
			account.setIsValid(isValid);
			/*List<InvestorAccount> list=investorService.findAllAccountList(account);
			List accountList=new ArrayList();
			Iterator<InvestorAccount> it=list.iterator();
			int approvalCount=0;
			while (it.hasNext()) {
				InvestorAccount investorAccount = (InvestorAccount) it.next();
				AccountVO vo=new AccountVO();
				vo.setId(investorAccount.getId());
				vo.setAccountNo(investorAccount.getAccountNo());
				vo.setAccType("I".equals(investorAccount.getAccType())?"Indiviual":"Joint");
				vo.setBaseCurrency(null!=investorAccount.getBaseCurrency()?investorAccount.getBaseCurrency():"");
				vo.setCies(null!=investorAccount.getCies() && "1".equals(investorAccount.getCies())?"CIES":"");
				vo.setDistributorId(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getId():"");
				vo.setFromType(investorAccount.getFromType());
				vo.setMemberId(investorAccount.getMember().getId());
				vo.setOpenStatus(investorAccount.getOpenStatus());
				if("-1".equals(vo.getOpenStatus())){
					approvalCount++;
				}
				vo.setTotalAssest("100,340.00");
				vo.setCash("20,068");
				vo.setProductValue("80,272");
				vo.setCashPercent("20%");
				vo.setProductValuePercent("80%");
				vo.setNextDCDate("30");
				vo.setNextRPQDate("40");
				vo.setFlowStatus(investorAccount.getFlowStatus());
				vo.setFaca(null!=investorAccount.getFaca() && "1".equals(investorAccount.getFaca())?"FACA":"");
				vo.setSubFlag(null!=investorAccount.getSubFlag()?investorAccount.getSubFlag():"");
				vo.setLoginCode(investorAccount.getMember().getLoginCode());
				accountList.add(vo);
			}
			model.put("accountList", accountList);*/
			List ifafirmList=investorService.findAccountIfafirm(account,langCode);
			model.put("ifafirmList", ifafirmList);
			model.put("in_use", inUse);
			model.put("inApproval", inApproval);
			model.put("cancellation", cancellation);
			model.put("cur", currency);
			//model.put("approvalCount", approvalCount);
			return "console/distributor/clientManagement";
		}else {
			return "redirect:"+this.getFullPath(request)+"console/viewLogin.do";
		}
		
	}
	
	/**
     * Distributor 的客户账户列表
     * @author mqzou
     * @date 2016-09-19
     */
	@RequestMapping(value = "/accountList", method = RequestMethod.POST)
	public void getAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode=getLoginLangFlag(request);
		if(null!=memberAdmin){
			String inUse=request.getParameter("in_use");//账户状态
			String inApproval=request.getParameter("inApproval");
			String cancellation=request.getParameter("cancellation");
			String currency=request.getParameter("cur");//基本货币
			String ifafirmId=request.getParameter("ifafirmId");
			String loginCode=request.getParameter("clientName");
			String status="";
			if(null!=inUse && "1".equals(inUse)){
				status+="'1',";
			}
			if(null!=inApproval && "1".equals(inApproval)){
				status+="-1";
			}
			if(status.endsWith(",")){
				status=status.substring(0,status.length()-1);
			}
			
			String isValid="";
			if(null!=cancellation && "1".equals(cancellation)){
				isValid="0";
				
			}
			
		
			InvestorAccount account=new InvestorAccount();
			if(null!=ifafirmId && !"".equals(ifafirmId)){
				MemberIfa ifa=new MemberIfa();
				MemberIfafirm ifafirm=new MemberIfafirm();
				ifafirm.setId(ifafirmId);
				ifa.setIfafirm(ifafirm);
				account.setIfa(ifa);
			}
			
			
			MemberDistributor distributor=memberAdmin.getDistributor();
			account.setDistributor(distributor);
			
			account.setOpenStatus(status);
			account.setBaseCurrency(currency);
			account.setIsValid(isValid);
			account.setAccountNo(loginCode);
			AccountVO accountVO=new AccountVO();
			accountVO.setOpenStatus(status);
			accountVO.setBaseCurrency(currency);
			accountVO.setIsValid(isValid);
			accountVO.setAccountNo(loginCode);
			accountVO.setDistributorId(distributor.getId());
			jsonPaging=getJsonPaging(request);
			jsonPaging=investorService.findAccountList(jsonPaging, accountVO,langCode);
			List accountList=new ArrayList();
			Iterator it=jsonPaging.getList().iterator();
			int approvalCount=0;
			while (it.hasNext()) {
				AccountVO vo=(AccountVO)it.next();
				
				if("-1".equals(vo.getOpenStatus())){
					approvalCount++;
				}
				
			}
			
			result.put("accountList", accountList);
			List distributorList=investorService.findInvestorDistributor(account);
			result.put("distributorList", distributorList);
			result.put("approvalCount", approvalCount);
			
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 代理商数据查询的方法（下拉填充数据）
	 * @author qgfeng
	 * @date 2016-12-5
	 */
	@RequestMapping(value = "/listJsonByIfafirm", method = RequestMethod.POST)
	public void listJsonByIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model, String ifafirmId) {
		List<MemberDistributor> list = distributorService.findDistributorByIfafirm(ifafirmId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("ifafirmJson", list);
		JsonUtil.toWriter(obj, response);
	}
}
