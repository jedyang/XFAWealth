/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonUtil;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.trade.service.TradeRecordService;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;

/**
 * 交易:记录部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeRecord")
public class TradeRecordAct extends WmesBaseAct{
	
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private SysParamService sysParamService;
	
	/**
	 * ifa交易记录页面
	 * @author wwluo
	 * @data 2016-11-25
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/orderPlanList")
	public String orderPlanList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.saveLastVisitUrl(request, response);
		MemberBase loginUser = getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String currencyCode = request.getParameter("currencyCode");
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = loginUser.getDefCurrency();
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = CommonConstants.DEF_CURRENCY; 
			}
		}
		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
		model.put("memberType", loginUser.getMemberType());
		model.put("currencyCode", currencyCode);
		model.put("currencyName", currencyName);
		return "trade/record/orderPlanList";
	}
	
	/**
	 * ifa的交易记录
	 * @author wwluo
	 * @data 2016-11-25
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/listForJson")
	public void orderPlanListForJson(HttpServletRequest request, HttpServletResponse response, ModelMap model,TransactionRecordFilterVO filter) {
		String langCode = this.getLoginLangFlag(request);
		filter.setLangCode(langCode);
		String keyWord = this.getDecodeStr(request.getParameter("keyWord"));
		filter.setKeyWord(keyWord);
		MemberBase loginUser = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		if (filter != null && StringUtils.isBlank(filter.getToCurrency())) {
			filter.setToCurrency(loginUser.getDefCurrency());
		}
		jsonPaging = tradeRecordService.getTradeRecord(loginUser,jsonPaging,filter);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取执行中的定投计划
	 * @author wwluo
	 * @data 2017-03-21
	 * @param filter 过滤条件
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/getAipTask")
	public void getAipTask(TransactionRecordFilterVO filter,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		String langCode = this.getLoginLangFlag(request);
		filter.setLangCode(langCode);
		String keyWord = this.getDecodeStr(request.getParameter("keyWord"));
		filter.setKeyWord(keyWord);
		jsonPaging = this.getJsonPaging(request);
		if (filter != null && StringUtils.isBlank(filter.getToCurrency())) {
			filter.setToCurrency(loginUser.getDefCurrency());
		}
		jsonPaging = tradeRecordService.getAipTask(loginUser,jsonPaging,filter);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 删除交易计划草稿
	 * @author wwluo
	 * @data 2017-03-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/delPlanById")
	public void delPlanById(String id,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		boolean flag = false;
		if (StringUtils.isNotBlank(id)) {
			OrderPlan plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,id);
			if(plan != null && ("-1".equals(plan.getFinishStatus()) || "0".equals(plan.getFinishStatus()))){
				PortfolioHold hold = plan.getPortfolioHold();
				if(hold != null){
					tradeRecordService.delOrderPlan(plan.getId());
					flag = true;
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
}
