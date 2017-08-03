/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.service;

import java.util.List;
import java.util.Map;

import com.fsll.buscore.fund.vo.CorePieChartItemVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;

/**
 * 组合计算相关的核心公共类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public interface CorePortfolioService {
	/**
	 * 获取指定时间范围内的组合累计收益数据
	 * @param portfolioId 组合id
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CorePortfolioVO> getPortfolioReturnRate(String portfolioId,String frequencyType,String currency);
	
	/**
     * 汇总基金组合（Ecahrt饼图）
     * @date 2017/2/23
     * @param fundDatas  json格式基金数据 [{"fundId":"0691edfe9e374821bf1b141d6f105fb6","weight":"74.0"},{"fundId":"0691edfe9e374821bf1b141d6f105fb6","weight":"74.0"}]
     * @param type  fund_portfolio.type
     * @param langCode
     * @return Map<name, value> 
     */
	public Map<String, Double> getFundCompositionData(String fundDatas,String type,String langCode);
	
	
	/**
	 * 获取指定时间范围内的组合累计收益数据
	 * @param portfolioId 组合id
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CorePortfolioVO> findArenaReturnRate(String portfolioId,String frequencyType,String langCode);
	
	
	/**
     * 汇总基金组合（APP饼图）
     * @date 2017/6/7
     * @param productIds  产品id,多个以号码隔开
     * @param productWeights  产品占比重（小数），多个以逗号隔开
     * @param langCode 语言
     * @param groupType 类型：market/sector
     * @return 
     */
	public List<CorePieChartItemVO> getFundCompositionData(String productIds,String productWeights,String langCode,String groupType);
}
