/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.trade.service.TradeChartService;

/**
 * 交易:图表分析业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeChartService")
public class TradeChartServiceImpl  extends BaseService implements TradeChartService{
	/**
	 * 获取持仓组合行情记录
	 * Calendar.DATE  5
	 * Calendar.WEEK_OF_YEAR  3
	 * Calendar.MONTH  2
	 * Calendar.YEAR  1
	 * @author wwluo
	 * @data 2016-10-09
	 * @param periodType
	 * @param period
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Date, Object> getHoldCumperf(String holdId,String type,String period) {
		Map<Date, Object> vo = null;
		if(StringUtils.isNotBlank(holdId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldCumperf c" +
					" WHERE" +
					" c.portfolioHold.id=?" +
					" AND" +
					" c.valuationDate" +
					" BETWEEN" +
					" ?" +
					" AND" +
					" CURDATE()" +
					" ORDER BY" +
					" c.valuationDate");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			Calendar calendar = Calendar.getInstance();
			if (StringUtils.isNotBlank(period)) {
				if ("1M".equals(period)){
					calendar.add(Calendar.MONTH, -1);
				}else if ("3M".equals(period)){
					calendar.add(Calendar.MONTH, -3);
				}else if ("6M".equals(period)){
					calendar.add(Calendar.MONTH, -6);
				}else if ("YTD".equals(period)){
					calendar.set(calendar.get(Calendar.YEAR), 0, 2); 
				}else if ("1Y".equals(period)){
					calendar.add(Calendar.YEAR, -1);
				}else if ("2Y".equals(period)){
					calendar.add(Calendar.YEAR, -2);
				}else if ("3Y".equals(period)){
					calendar.add(Calendar.YEAR, -3);
				}else if ("5Y".equals(period)){
					calendar.add(Calendar.YEAR, -5);
				}else if ("10Y".equals(period)){
					calendar.add(Calendar.YEAR, -10);
				}
			}else{
				calendar.add(Calendar.YEAR, -1); // 默认一年数据
			}
			calendar.add(Calendar.DATE,-1);
			params.add(calendar.getTime());
			/*if("W".equals(type)){
				hql.append(" AND WEEKDAY(c.valuation_date)+1=5");
			}else if("M".equals(type)){
				hql.append(" AND WEEKDAY(c.valuation_date)+1=5"); // TODO
			}*/
			List<PortfolioHoldCumperf> holdCumperfs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(holdCumperfs != null && !holdCumperfs.isEmpty()){
				vo = new HashMap<Date, Object>();
				for (PortfolioHoldCumperf portfolioHoldCumperf : holdCumperfs) {
					vo.put(portfolioHoldCumperf.getValuationDate(), portfolioHoldCumperf.getCumulativeRate());
				}
			}
		}
		return vo;
	}
	
	/**
	 * 获取基金收益数据
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	@Override
	public Map<String, Object> getIncomePercentageTotal(Map<Date, Object> fundIncomePercentageTotal, Map<Date, Object> aipIncomePercentageTotal,Map<Date, Object> holdCumperf) {
		Map<String, Object> result = new HashMap<String, Object>();
		Set<Date> dates = new HashSet<Date>();
		if(fundIncomePercentageTotal != null && !fundIncomePercentageTotal.isEmpty()){
			dates.addAll(fundIncomePercentageTotal.keySet());
		}
		if(aipIncomePercentageTotal != null && !aipIncomePercentageTotal.isEmpty()){
			dates.addAll(aipIncomePercentageTotal.keySet());
		}
		if(holdCumperf != null && !holdCumperf.isEmpty()){
			dates.addAll(holdCumperf.keySet());
		}
		List<Object> fundIncomePercentages = new ArrayList<Object>();
		List<Object> aipIncomePercentages = new ArrayList<Object>();
		List<Object> holdIncomePercentages = new ArrayList<Object>();
		List<Date> marketDates = new ArrayList<Date>();
		if(!dates.isEmpty()){
			for (Date date : dates) {
				if(fundIncomePercentageTotal != null && !fundIncomePercentageTotal.containsKey(date)){
					fundIncomePercentageTotal.put(date, "-");
				}
				if(aipIncomePercentageTotal != null && !aipIncomePercentageTotal.containsKey(date)){
					aipIncomePercentageTotal.put(date, "-");
				}
				if(holdCumperf != null && !holdCumperf.containsKey(date)){
					holdCumperf.put(date, "-");
				}
				marketDates.add(date);
			}
			Collections.sort(marketDates, new Comparator<Date>(){
				@Override
				public int compare(Date o1,
						Date o2) {
					if(o1.getTime() > o2.getTime()){
						return 1;
					}else{
						return -1;
					}
				}
			});
			for (Date date : marketDates) {
				if(fundIncomePercentageTotal != null){
					fundIncomePercentages.add(fundIncomePercentageTotal.get(date));
				}
				if(aipIncomePercentageTotal != null){
					aipIncomePercentages.add(aipIncomePercentageTotal.get(date));
				}
				if(holdCumperf != null){
					holdIncomePercentages.add(holdCumperf.get(date));
				}
			}
		}
		result.put("marketDates", marketDates);
		result.put("incomePercentageTotal", fundIncomePercentages);
		result.put("aipIncomePercentageTotal", aipIncomePercentages);
		result.put("holdCumperf", holdIncomePercentages);
		return result;
	}
}
