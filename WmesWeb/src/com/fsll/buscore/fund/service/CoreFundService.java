/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.service;

import java.util.List;

import com.fsll.buscore.fund.vo.CoreFundNavAlignVO;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.buscore.fund.vo.CoreMoreFundRateVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;

/**
 * 基金计算相关的核心公共类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public interface CoreFundService {
	/**
	 * 获取指定时间范围内的基金净值数据
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavVO> getFundNav(String fundId,String frequencyType,String currency);
	
	/**
	 * 获取指定时间范围内的基金累计收益数据
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public CoreFundsReturnForChartsVO getFundReturnRate(String fundId,String frequencyType,String currency);
	
	/**
	 * 获取指定时间范围内的多个净值集合对齐数据
	 * @param fundIds 基金id,多个之间用,分隔
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavAlignVO> getFundNavAlign(String fundIds,String frequencyType,String currency);
	
	/**
	 * 获取指定时间范围内的多个基金累计收益数据2
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @return
	 */
	public CoreFundsReturnForChartsVO getMoreFundReturnRate(String fundIds,String allocationRateList,String frequencyType,String langCode);
	
	/**
	 * 获取指定时间范围内的多个基金累计收益数据-用于多个基金多个拆线图
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @return
	 */
	public List<CoreFundsReturnForChartsVO> getMulFundReturnRate(String fundIds,String frequencyType,String currency,String langCode);
	
	/**
	 * 获取指定时间范围内的多个基金的累计收益数据(合并一条线,供APP使用)
	 * 
	 * @param productIds
	 * @param frequencyType
	 *            频率 1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavVO> getMoreFundReturnRateForAPP(String productIds,String allocationRateList,String frequencyType,String langCode) ;
	
}
