/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import java.util.Date;
import java.util.Map;


/**
 * 交易:图表分析业务接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeChartService {
	/**
	 * 获取持仓组合行情记录
	 * @author wwluo
	 * @data 2016-10-09
	 * @param 
	 * @return
	 */
	public Map<Date, Object> getHoldCumperf(String planId, String type, String period);
	
	/**
	 * 获取基金收益数据
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	public Map<String, Object> getIncomePercentageTotal(Map<Date, Object> fundIncomePercentageTotal, Map<Date, Object> aipIncomePercentageTotal,Map<Date, Object> holdCumperf);
}
