/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

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

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.trade.service.TradeCheckService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanAipVO;
import com.fsll.wmes.trade.vo.OrderPlanProductVO;
import com.fsll.wmes.web.service.WebReadToDoService;

/**
 * 交易:审批部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeCheck")
public class TradeCheckAct extends WmesBaseAct{
	@Autowired
	private TradeCheckService tradeCheckService;
	@Autowired
	private TradeStepService tradeStepService;
    @Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	
	
	/**
	 * 交易计划审批
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/approvalPlan")
	public void approvalPlan(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		String langCode = this.getLoginLangFlag(request);
		String planId = request.getParameter("planId");
		String state = request.getParameter("state");
		String opinon = request.getParameter("opinon");
		boolean flag = tradeCheckService.saveApprovalPlan(loginUser, langCode, planId, state, opinon, this.getRemoteHost(request));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 交易操作投资人确认操作（同意、退回） ##
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/investorConfirmOrderPlan")
	public void investorConfirmOrderPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		if(loginUser != null){
			String planId = request.getParameter("planId");
			String finishStatus = request.getParameter("finishStatus");
			flag = tradeCheckService.saveConfirmOrderPlan(loginUser, planId, finishStatus, langCode);
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}

}
