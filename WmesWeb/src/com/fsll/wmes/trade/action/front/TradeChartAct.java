/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonUtil;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.service.FundMarketService;
import com.fsll.wmes.fund.vo.AipVO;
import com.fsll.wmes.fund.vo.FundIncomePercentageVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundMarketDataVO;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.trade.service.TradeChartService;
import com.fsll.wmes.trade.service.TradeRebalanceService;
import com.fsll.wmes.trade.service.TradeStepService;

/**
 * 交易:图表分析部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeChart")
public class TradeChartAct extends WmesBaseAct{

	@Autowired
	private TradeChartService tradeChartService;
	@Autowired
	private TradeStepService tradeStepService;
	
    @Autowired
    private FundInfoService fundInfoService;
    @Autowired
    private FundMarketService fundMarketrService;
	@Autowired
	private CrmProposalService crmProposalService;
	@Autowired
	private PortfolioArenaService portfolioArenaService;
	@Autowired
	private CoreFundService coreFundService;
	@Autowired
	private CorePortfolioService corePortfolioService;
	@Autowired
	private TradeRebalanceService tradeRebalanceService;
	
	 /**
		 * 获取基金价格行情信息列表
		 * @author wwluo
		 * @data 2016-10-19
		 * @param request
		 * @param response
		 * @param model
		 */						
		@RequestMapping(value = "/findFundMarketList")
		public void findFundMarketList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
			Map<String, Object> result = new HashMap<String, Object>();
			String langCode = getLoginLangFlag(request);
			boolean flag = false;
			try {
				String fundIds = request.getParameter("fundIds");
				String period = request.getParameter("period");
				String type = request.getParameter("type");
				List<FundMarketDataVO> fundMarketDataVOs = new ArrayList<FundMarketDataVO>();
				if (StringUtils.isNotBlank(fundIds)) {
					if(fundIds.indexOf(",") > -1){
						//多只基金
						String[] fundId = fundIds.split(",");
						for (int i = 0; i < fundId.length; i++) {
							fundMarketDataVOs = getIncomepercentage(langCode, period,fundMarketDataVOs, fundId[i], type,null,false);
						}
					}else{
						//一只基金
						fundMarketDataVOs = getIncomepercentage(langCode, period,fundMarketDataVOs, fundIds, type,null,false);			
					}
				}
				result.put("fundMarketDataVOs", fundMarketDataVOs);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("flag", flag);
			JsonUtil.toWriter(result, response);
		}
	
	/**
	 * 获取基金总收益数据（orderPlan、rebalance）
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/fundIncomePercentageTotalforOrderPlan")
	public void fundIncomePercentageTotalforOrderPlan(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		try {
			String funds = this.toUTF8String(request.getParameter("funds"));
			String period = request.getParameter("period");
			String type = request.getParameter("type");
			String planId = request.getParameter("planId");
			String holdId = request.getParameter("holdId");
			List<FundMarketDataVO> fundMarketDataVOs = new ArrayList<FundMarketDataVO>();
			Map<Date, Object> fundIncomePercentageTotal = null;
			Map<Date, Object> aipIncomePercentageTotal = null;
			Map<Date, Object> holdCumperf = null;
			if (StringUtils.isNotBlank(funds)) {
				List<Map> fundMap = JsonUtil.toListMap(funds);
				if(fundMap != null && !fundMap.isEmpty()){
					for (Map map : fundMap) {
						String fundId = (String) map.get("fundId");
						String weightStr = (String) map.get("weight");
						Double weight = null;
						if (StringUtils.isBlank(weightStr)) {
							weight = 0.00;
						}else{
							weight = Double.parseDouble(weightStr);
							weight = weight/100;
						}
						FundMarketDataVO vo = new FundMarketDataVO();
						List<FundIncomePercentageVO> fundIncomePercentageVOs = fundMarketrService.getIncomePercentage(fundId, period, type, weight, true);
						vo.setFundIncomePercentageVOs(fundIncomePercentageVOs);
						fundMarketDataVOs.add(vo);
					}
				}
				//取总收益率
				fundIncomePercentageTotal = fundMarketrService.getIncomePercentageTotal(fundMarketDataVOs);
				//取AIP总收益率
				AipVO aip = new AipVO();
				if (StringUtils.isNotBlank(planId)) {
					OrderPlan plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
					if(plan != null && "1".equals(plan.getAipFlag())){
						OrderPlanAip orderPlanAip = tradeStepService.getOrderPlanAipByOrderId(planId);
						if(orderPlanAip != null){
							BeanUtils.copyProperties(orderPlanAip, aip);
							aipIncomePercentageTotal = fundMarketrService.getAipIncomePercentageTotal(aip, fundIncomePercentageTotal);
						}
					}
				}
				if (StringUtils.isNotBlank(holdId)) {
					holdCumperf = tradeChartService.getHoldCumperf(holdId, type, period);
				}
			}
			result = tradeChartService.getIncomePercentageTotal(fundIncomePercentageTotal, aipIncomePercentageTotal, holdCumperf);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取Ecahrt饼图数据
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/getPieData")
	public void getPieData(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		boolean flag = false;
		String funds = this.toUTF8String(request.getParameter("funds"));
		Map<String, Double> fundPortfolioMarket = null;
		Map<String, Double> fundPortfolioSector = null;
		if (StringUtils.isNotBlank(funds)) {
			fundPortfolioMarket = corePortfolioService.getFundCompositionData(funds, "market", langCode);
			fundPortfolioSector = corePortfolioService.getFundCompositionData(funds, "sector", langCode);
			flag = true;
		}
		result.put("flag", flag);
		result.put("market", fundPortfolioMarket);
		result.put("sector", fundPortfolioSector);
		JsonUtil.toWriter(result, response);
	}
	
	 /**
	 * 获取基金收益信息
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */	
	private List<FundMarketDataVO> getIncomepercentage(String langCode, String period,List<FundMarketDataVO> fundMarketDataVOs, String fundId,String type,Double weight,boolean ifWeight) {
		FundMarketDataVO marketDataVO = new FundMarketDataVO();
		if (StringUtils.isNotBlank(fundId)) {
			FundInfoDataVO fundInfoDataVO = fundInfoService.findFundFullInfoById(fundId);
			if(fundInfoDataVO != null){
				if(CommonConstants.LANG_CODE_SC.equals(langCode) && fundInfoDataVO.getFundInfoSc() != null){
					marketDataVO.setFundName(fundInfoDataVO.getFundInfoSc().getFundName());
				}else if(CommonConstants.LANG_CODE_TC.equals(langCode) && fundInfoDataVO.getFundInfoTc() != null){
					marketDataVO.setFundName(fundInfoDataVO.getFundInfoTc().getFundName());
				}else if(CommonConstants.LANG_CODE_EN.equals(langCode) && fundInfoDataVO.getFundInfoEn() != null){
					marketDataVO.setFundName(fundInfoDataVO.getFundInfoEn().getFundName());
				}
			}
			List<FundIncomePercentageVO> fundIncomePercentageVOs = fundMarketrService.getIncomePercentage(fundId, period, type, weight, ifWeight);
			marketDataVO.setFundId(fundId);
			marketDataVO.setFundIncomePercentageVOs(fundIncomePercentageVOs);
			fundMarketDataVOs.add(marketDataVO);
		}
		return fundMarketDataVOs;
	}
	
	/**
	 * 获取基金收益图表数据
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/getSingleFundReturnData")
	public void getSingleFundReturnData(String fundIds,String period,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		Boolean flag = false;
		List<CoreFundsReturnForChartsVO> chartsDatas = new ArrayList<CoreFundsReturnForChartsVO>();
		if (StringUtils.isNotBlank(fundIds) && StringUtils.isNotBlank(period)) {
			chartsDatas = coreFundService.getMulFundReturnRate(fundIds, period, null, langCode);
			flag = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("chartsDatas", chartsDatas);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取多只基金总收益图表数据
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/getFundsReturnData")
	public void getFundsReturnData(String fundIds,String weights,String period,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
 		Map<String, Object> result = new HashMap<String, Object>();
		Boolean flag = false;
		CoreFundsReturnForChartsVO chartsData = null;
		if (StringUtils.isNotBlank(fundIds) && StringUtils.isNotBlank(weights) && StringUtils.isNotBlank(period)) {
			chartsData = coreFundService.getMoreFundReturnRate(fundIds, weights, period, getLoginLangFlag(request));
			flag = true;
		}
		result.put("flag", flag);
		result.put("chartsData", chartsData);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取持仓多只基金总收益图表数据
	 * @author wwluo
	 * @data 2016-10-19
	 * @param request
	 * @param response
	 * @param model
	 */						
	@RequestMapping(value = "/getHoldProductReturnData")
	public void getHoldProductReturnData(String holdId, String fundIds,String weights,String period,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		Boolean flag = false;
		CoreFundsReturnForChartsVO chartsData = null;
		CoreFundsReturnForChartsVO holdChartsData = null;
		if (StringUtils.isNotBlank(fundIds) && StringUtils.isNotBlank(weights) && StringUtils.isNotBlank(period)) {
			chartsData = coreFundService.getMoreFundReturnRate(fundIds, weights, period, getLoginLangFlag(request));
			Map<String, Object> productObj = tradeRebalanceService.getHoldProductInfo(holdId);
			if(productObj != null && !productObj.isEmpty()){
				holdChartsData = coreFundService.getMoreFundReturnRate((String) productObj.get("fundIds"), (String) productObj.get("weights"), period, getLoginLangFlag(request));
			}
			flag = true;
		}
		result.put("flag", flag);
		result.put("chartsData", chartsData);
		result.put("holdChartsData", holdChartsData);
		JsonUtil.toWriter(result, response);
	}
	
	
	
	
	
	
	
	
	
	
}
