/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.trade.service.TradeRebalanceService;
import com.fsll.wmes.trade.vo.PlanProductVO;

/**
 * 交易:持仓调整业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeRebalanceService")
public class TradeRebalanceServiceImpl  extends BaseService implements TradeRebalanceService{
	
	/**
	 * 获取该组合的全部交易计划
	 * @author wwluo
	 * @date 2017-02-15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderPlan> getOrderPlansByHold(String holdId, String status) {
		List<OrderPlan> orderPlans = null;
		if(StringUtils.isNotBlank(holdId)){
			StringBuffer hql = new StringBuffer(" FROM OrderPlan p WHERE p.portfolioHold.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			if (StringUtils.isNotBlank(status)) {
				hql.append(" AND p.finishStatus=?");
				params.add(status);
			}
			orderPlans = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return orderPlans;
	}
	
	/**
	 * 封装持仓产品到PlanProductVO
	 * @author wwluo
	 * @date 2017-02-20
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanProductVO> getPortfolioHoldProducts(String holdId,String currencyCode, String langCode) {
		List<PlanProductVO> vos = null;
		if(StringUtils.isNotBlank(holdId) && StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(langCode)){
			PortfolioHold hold = (PortfolioHold) this.baseDao.get(PortfolioHold.class, holdId);
			if(hold != null){
				StringBuffer hql = new StringBuffer(""
						+ " SELECT"
						+ " p,i.fundName,f.riskLevel,i.fundType,i.fundCurrencyCode,f.lastNav,f.id,p.account.id,p.accountNo"
						+ " FROM"
						+ " PortfolioHoldProduct p"
						+ " LEFT JOIN"
						+ " FundInfo f"
						+ " ON"
						+ " p.product.id=f.product.id"
						+ " LEFT JOIN"
						+ " FundInfo" + this.getLangFirstCharUpper(langCode) + " i"
						+ " ON"
						+ " f.id=i.id"
						+ " LEFT JOIN"
						+ " ProductInfo d"
						+ " ON"
						+ " d.id=f.product.id"
						+ " WHERE"
						+ " p.portfolioHold.id=?"
						+ " AND" 
						+ " d.type='fund'"
						+ "");
				List<Object> params = new ArrayList<Object>();
				params.add(hold.getId());
				List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(objs != null && !objs.isEmpty()){
					vos = new ArrayList<PlanProductVO>();
					for (Object[] objects : objs) {
						PlanProductVO vo = new PlanProductVO();
						PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objects[0];
						String fundName = (String) objects[1];
						Integer riskLevel = (Integer) objects[2];
						String fundType = (String) objects[3];
						String fundCurrencyCode = (String) objects[4];
						Double lastNav = (Double) objects[5];
						String fundId = (String) objects[6];
						vo.setFundInfoId(fundId);
						vo.setHoldProductId(holdProduct.getId());
						vo.setFundName(fundName);
						vo.setRiskLevel(riskLevel);
						vo.setFundType(fundType);
						if(holdProduct.getAccount() != null){
							vo.setAccountId(holdProduct.getAccount().getId());
						}
						vo.setAccountNo(holdProduct.getAccountNo());
						vo.setFundCurrencyCode(fundCurrencyCode);
						String fundCurrencyName =
							this.getParamConfigName(langCode, fundCurrencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY);
						vo.setFundCurrencyName(fundCurrencyName);
						Double fundCurrencyRate = null;  // 与exchangeRate不同
						if(fundCurrencyCode != null){
							fundCurrencyRate = this.getExchangeRate(fundCurrencyCode, currencyCode);
						}
						if(fundCurrencyRate == null){
							fundCurrencyRate = 1.00;
						}
						if(lastNav != null){
							vo.setLastNav(lastNav*fundCurrencyRate);
						}
						vo.setFundInfoId(fundId);
						Double exchangeRate = null;
						if (StringUtils.isNotBlank(holdProduct.getBaseCurrency()) && StringUtils.isNotBlank(currencyCode)) {
							exchangeRate = this.getExchangeRate(holdProduct.getBaseCurrency(), currencyCode);
						}
						if(exchangeRate == null){
							exchangeRate = 1.00;
						}
						if(holdProduct.getHoldingUnit() != null && lastNav != null){
							vo.setReferenceCost(holdProduct.getHoldingUnit()*lastNav*fundCurrencyRate);
						}
						vo.setAvailableUnit(holdProduct.getAvailableUnit());
						vo.setHoldingUnit(holdProduct.getHoldingUnit());
						if(holdProduct.getProduct() != null){
							vo.setProductId(holdProduct.getProduct().getId());
						}
						vo.setRiskLevel(riskLevel);
						vos.add(vo);
					}
				}
			}
		}
		return vos;
	}
	
	/**
	 * 获取 portfolio 产品持仓数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PortfolioHoldProduct> getPortfolioHoldProducts(String holdId,String productType) {
		List<PortfolioHoldProduct> holdProducts = null;
		if(StringUtils.isNotBlank(holdId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=? ");
			List params = new ArrayList();
			params.add(holdId);
			if(StringUtils.isNotBlank(productType)){
				hql.append(" AND p.product.type=?");
				params.add(productType);
			}
			holdProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return holdProducts;
	}
	
	/**
	 * 获取 portfolio各产品持仓   PortfolioHoldProduct表
	 * @author wwluo
	 * @data 2016-10-10
	 * @param holdId
	 * @param productId
	 * @return
	 */
	@Override
	public PortfolioHoldProduct getPortfolioHoldProduct(String holdId,String productId) {
		PortfolioHoldProduct  pd = null;
		if(StringUtils.isNotBlank(holdId) && StringUtils.isNotBlank(productId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldProduct a" +
					" WHERE" +
					" a.portfolioHold.id = ? and a.product.id=?   " +
					" and " +
					" a.lastUpdate" +
					" Desc" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			params.add(productId);
			List<PortfolioHoldProduct> hps = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(hps != null && !hps.isEmpty()){
				pd = hps.get(0);
			}
		}
		return pd;
	}

	/**
	 * 获取持仓产品信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> getHoldProductInfo(String holdId) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(holdId)){
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " i.id,i.lastNav,e.fundCurrencyCode,a.holdingUnit"
					+ " FROM"
					+ " PortfolioHoldProduct a"
					+ " LEFT JOIN"
					+ " ProductInfo p"
					+ " ON"
					+ " p.id=a.product.id"
					+ " LEFT JOIN"
					+ " FundInfo i"
					+ " ON"
					+ " i.product.id=p.id"
					+ " LEFT JOIN"
					+ " FundInfoEn e"
					+ " ON"
					+ " i.id=e.id"
					+ " WHERE"
					+ " a.portfolioHold.id=?"
					+ " AND"
					+ " p.type=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			params.add(CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
			List<Object[]> objects = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objects != null && !objects.isEmpty()){
				String fundIds = "";
				String weights = "";
				List<Map<String, String>> funds = new ArrayList<Map<String, String>>();
				Double totalAmout = 0d;
				for (Object[] objs : objects) {
					Double lastNav = (Double) objs[1];
					String fundCurrencyCode = (String) objs[2];
					Double holdingUnit = (Double) objs[3];
					Double rate = null;
					if(!CommonConstants.DEF_CURRENCY.equals(fundCurrencyCode)){
						rate = this.getExchangeRate(fundCurrencyCode, CommonConstants.DEF_CURRENCY);
					}
					if(rate == null){
						rate = 1d;
					}
					totalAmout += lastNav * holdingUnit * rate;
				}
				for (Object[] objs : objects) {
					String fundId = (String) objs[0];
					Double lastNav = (Double) objs[1];
					String fundCurrencyCode = (String) objs[2];
					Double holdingUnit = (Double) objs[3];
					Double rate = null;
					if(!CommonConstants.DEF_CURRENCY.equals(fundCurrencyCode)){
						rate = this.getExchangeRate(fundCurrencyCode, CommonConstants.DEF_CURRENCY);
					}
					if(rate == null){
						rate = 1d;
					}
					fundIds += fundId + ",";
					Double weight = (lastNav * holdingUnit * rate)/totalAmout * 100;
					weight = new BigDecimal(weight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					weights += weight + ",";
					Map<String, String> fundObj = new HashMap<String, String>();
					fundObj.put("fundId", fundId);
					fundObj.put("weight", weight.toString());
					funds.add(fundObj);
				}
				fundIds = fundIds.substring(0, fundIds.length() - 1);
				weights = weights.substring(0, weights.length() - 1);
				result.put("fundIds", fundIds);
				result.put("weights", weights);
				result.put("funds", funds);
			}
		}
		return result;
	}
	
}
