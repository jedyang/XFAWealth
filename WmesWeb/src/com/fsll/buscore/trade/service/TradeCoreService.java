/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.trade.service;

import java.util.List;

import com.fsll.buscore.trade.vo.TradeIfaVO;
import com.fsll.wmes.investor.vo.MyAssetsVO;
import com.fsll.wmes.portfolio.vo.AssetsTotalVo;
import com.fsll.wmes.portfolio.vo.HoldProductVO;

/**
 * 交易模块的核心公共类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-3-30
 */
public interface TradeCoreService {
	/**
	 * 获取IFA的所有客户的概况信息
	 * @param ifaId
	 * @return
	 */
	public TradeIfaVO getIfaBasic(String ifaId,String currency);
	
	/**
	 * 获取组合持仓的资产统计
	 * @author mqzou 2017-01-10
	 * @param portfolioHoldId
	 * @return
	 */
	public AssetsTotalVo getPortfolioTotalAssets(String holdId,String currency);
	
	/**
	 * 获取持仓组合的基金列表
	 * @author mqzou 2017-02-22
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	public List<HoldProductVO> findHoldFundList(String portfolioHoldId,String currency,String langCode,String memberid);
	
	/**
	 * 获取组合的总现金
	 * @author mqzou 2017-02-22
	 * @param portfolioId
	 * @param currency
	 * @return
	 */
	public double findPortfolioCash(String portfolioId,String currency,String memberid);
	
	/**
	 * 获取持仓组合的股票列表
	 * @author mqzou 2017-02-23
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	public List<HoldProductVO> findHoldStockList(String portfolioHoldId,String currency,String langCode,String memberid);
	
	/**
	 * 获取持仓组合的债券列表
	 * @author mqzou 2017-02-24
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	public List<HoldProductVO> findHoldBond(String portfolioHoldId,String currency,String langCode,String memberid);
	
	/**
	 * 获取持仓组合的保险列表
	 * @author mqzou 2017-02-24
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	public List<HoldProductVO> findHoldInsure(String portfolioHoldId,String currency,String langCode,String memberid);
	
	
	/**
	 * 获取投资人的总资产数据
	 * @author mqzou 2017-03-24
	 * @param memberId
	 * @param currency
	 * @return
	 */
	public MyAssetsVO findInvestorAssets(String memberId,String currency);

}
