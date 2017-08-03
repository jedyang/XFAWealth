/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import java.util.List;
import java.util.Map;

import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.trade.vo.PlanProductVO;

/**
 * 交易:持仓调整接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeRebalanceService {
	
	/**
	 * 获取该组合的全部交易计划
	 * @author wwluo
	 * @date 2017-02-15
	 */
	public List<OrderPlan> getOrderPlansByHold(String holdId, String status);
	
	/**
	 * 封装持仓产品到PlanProductVO
	 * @author wwluo
	 * @date 2017-02-20
	 */
	public List<PlanProductVO> getPortfolioHoldProducts(String id,String currencyCode, String langCode);
	
	/**
	 * 获取 portfolio 产品持仓数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param
	 * @return
	 */
	public List<PortfolioHoldProduct> getPortfolioHoldProducts(String holdId,String productType);
	
	/**
	 * 获取 portfolio各产品持仓   PortfolioHoldProduct表
	 * @author wwluo
	 * @data 2016-10-10
	 * @param holdId
	 * @param productId
	 * @return
	 */
	public PortfolioHoldProduct getPortfolioHoldProduct(String holdId,String productId);

	/**
	 * 获取持仓产品信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	public Map<String, Object> getHoldProductInfo(String holdId);
	
}
