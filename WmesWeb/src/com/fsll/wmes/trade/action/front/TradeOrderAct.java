/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

import it.sauronsoftware.base64.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.util.JsonUtil;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.trade.service.TradeOrderService;
import com.fsll.wmes.trade.vo.TransactionAccountVO;

/**
 * 交易:下单与oms交互部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeOrder")
public class TradeOrderAct extends WmesBaseAct{
	@Autowired
	private TradeOrderService tradeOrderService;
	@Autowired
	private ITraderSendService iTraderSendService;
	@Autowired
	private InvestorService investorService;
	
	/**
	 * 获取交易账号，下单
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/getTransactionAccounts")
	public void checkTransactionPassword(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		boolean flag = false;
		List<TransactionAccountVO> accounts = null;
		String orderType = request.getParameter("orderType"); // 订单类型，默认为 1:普通下单（买、卖或转换），0：撤单
		String historyId = request.getParameter("historyId");
		String planId = request.getParameter("planId");
		if("0".equals(orderType) && StringUtils.isNotBlank(historyId)){
		// 撤单
			accounts = tradeOrderService.getHistoryAccountByhistoryId(historyId,loginUser.getMemberType());
			if(accounts != null && !accounts.isEmpty()){
				flag =  true;
			}
		}else if(StringUtils.isNotBlank(planId)){
		// 普通下单
			accounts = tradeOrderService.getOrderPlanAccounts(planId,loginUser.getMemberType(),null);
			if(accounts != null && !accounts.isEmpty()){
				flag =  true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("orderType", orderType);
		result.put("memberType", loginUser.getMemberType());
		result.put("accounts", accounts);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * IFA AE账号验证
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/checkAeCode")
	public void checkAeCode(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		List<String> aeCodes = null;
		if(loginUser != null){
			String checkAccounts = this.toUTF8String(request.getParameter("checkAccounts"));
			if (StringUtils.isNotBlank(checkAccounts)) {
				List<Map> checkAccountMap = JsonUtil.toListMap(checkAccounts);
				if(checkAccountMap != null && !checkAccountMap.isEmpty()){
					aeCodes = new ArrayList<String>();
					for (Object object : checkAccountMap) {
						String account = (String) object;
						InvestorAccount investorAccount = investorService.findInvestorAccountById(account);
						if(investorAccount != null && StringUtils.isNotBlank(investorAccount.getId())){
							String aeCode = tradeOrderService.findAECode(investorAccount.getIfa().getId(),investorAccount.getDistributor().getId());
							if (StringUtils.isNotBlank(aeCode) && !aeCodes.contains(aeCode)) {
								aeCodes.add(aeCode);
							}
						}
					}
					flag = true;
				}
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("aeCodes", aeCodes);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 验证登陆，下单
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/saveLogin")
	public void saveLogin(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		boolean flag = false;
		String pingPos = null;
		String errorCode = null;
		String errorMsg = null;
		String accountNo = request.getParameter("accountNo");
		String accountPwd = request.getParameter("password");
		if(StringUtils.isNotBlank(accountNo) && StringUtils.isNotBlank(accountPwd)){
			accountPwd = Base64.decode(accountPwd, "UTF-8");
			MemberAccountSso sso = null;
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
				sso = iTraderSendService.saveLogin(request, "0", accountNo, accountPwd);
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
				sso = iTraderSendService.saveLogin(request, "1", accountNo, accountPwd);
			}
			if(sso != null){
				if (StringUtils.isNotBlank(sso.getPinPos())) {
					flag = true;
					pingPos = sso.getPinPos();
				}
				errorCode = sso.getErrorCode();
				errorMsg = sso.getErrorMsg();
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("pingPos", pingPos);
		result.put("errorCode", errorCode);
		result.put("errorMsg", errorMsg);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 验证Pin，下单
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/savePin")
	public void savePin(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		boolean flag = false;
		String accountNo = request.getParameter("accountNo");
		String pinCode = request.getParameter("pinCode");
		if(StringUtils.isNotBlank(accountNo) && StringUtils.isNotBlank(pinCode)){
			pinCode = Base64.decode(pinCode, "UTF-8");
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
				flag = iTraderSendService.savePin(request, "0",accountNo, pinCode);
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
				flag = iTraderSendService.savePin(request, "1",accountNo, pinCode);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 交易下单
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/addOrderPlan")
	public void addOrderPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		//MemberSsoVO ssoVo = this.getLoginUserSSO(request);
		String langCode = this.getLoginLangFlag(request);
		String planId = request.getParameter("planId");
		String accountData = this.toUTF8String(request.getParameter("accountData"));
		boolean flag = tradeOrderService.addOrderPlan(request, loginUser, langCode, planId, accountData);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 交易撤单
	 * @author wwluo
	 * @date 2017-01-09
	 */
	@RequestMapping(value = "/deleteOrder")
	public void deleteOrder(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		String historyId = request.getParameter("historyId");
		String password = request.getParameter("password");
		if(StringUtils.isNotBlank(historyId) && StringUtils.isNotBlank(password)){
			flag = tradeOrderService.deleteOrder(request, loginUser, historyId, password);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
}
