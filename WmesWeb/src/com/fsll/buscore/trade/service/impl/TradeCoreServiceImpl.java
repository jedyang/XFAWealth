/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.trade.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.buscore.trade.service.TradeCoreService;
import com.fsll.buscore.trade.vo.TradeIfaVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.BondMarketDay;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.InsureInfo;
import com.fsll.wmes.entity.InsureInfoEn;
import com.fsll.wmes.entity.InsureInfoSc;
import com.fsll.wmes.entity.InsureInfoTc;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.MyAssetHis;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.entity.StockMarketDay;
import com.fsll.wmes.investor.vo.MyAssetsVO;
import com.fsll.wmes.portfolio.vo.AssetsTotalVo;
import com.fsll.wmes.portfolio.vo.HoldProductVO;

/**
 * 交易模块的核心公共类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-3-30
 */
@Service("tradeCoreService")
public class TradeCoreServiceImpl  extends BaseService implements TradeCoreService{
	/**
	 * 获取IFA的所有客户的概况信息
	 * @param ifaId
	 * @return
	 */
	public TradeIfaVO getIfaBasic(String ifaId,String currency){
		
		TradeIfaVO vo=new TradeIfaVO();
		vo.setTotalNum(0);
		vo.setProfitNum(0);
		vo.setLossNum(0);
		vo.setAum(0);
		
		//1.计算客户数、盈利及亏损情况
		StringBuilder hql=new StringBuilder();
		hql.append(" select r.id,sum(h.totalReturnRate) from  CrmCustomer r left join PortfolioHold h on r.member.id=h.member.id ");
		hql.append(" where r.ifa.id=? and r.clientType='1' and exists(select 1 from InvestorAccount i where i.member.id=r.member.id and i.openStatus='3') ");
		hql.append(" group by r.id ");
	    List<Object> params=new ArrayList<Object>();
	    params.add(ifaId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(list!=null && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				if(null!=objects[1]){
					double returnRate=Double.parseDouble(objects[1].toString());
					if(returnRate<0)
						vo.setLossNum(vo.getLossNum()+1);
					else {
						vo.setProfitNum(vo.getProfitNum()+1);
					}
				}
				vo.setTotalNum(vo.getTotalNum()+1);
			}
		}
		
		//2.计算aum
		double total=0;
		hql=new StringBuilder(" select c from CrmCustomer r left join PortfolioHold h on r.ifa.id=h.ifa.id and r.member.id=h.member.id ");
		hql.append(" left join PortfolioHoldAccount c on h.id=c.portfolioHold.id ");
		hql.append(" where r.ifa.id=?  and r.clientType='1' ");
		params=new ArrayList<Object>();
		params.add(ifaId);
		list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()){
				PortfolioHoldAccount account=(PortfolioHoldAccount) it.next();
				if(null==account)continue;
				double sum=account.getTotalAsset()==null?0:account.getTotalAsset();
				if(!currency.equals(account.getBaseCurrency())){
					double rate=getExchangeRate(account.getBaseCurrency(), currency);
					total+=sum*rate;
				}else {
					total+=sum;
				}
			}
		}
		vo.setAum(total);
		
		return vo;
	}
	
	/**
	 * 获取组合持仓的资产统计
	 * @author mqzou 2017-01-10
	 * @param portfolioHoldId
	 * @return
	 */
	public AssetsTotalVo getPortfolioTotalAssets(String holdId,String currency){
		AssetsTotalVo vo = new AssetsTotalVo();
		PortfolioHold hold = (PortfolioHold)this.baseDao.get(PortfolioHold.class, holdId);
		String baseCurrency= hold.getBaseCurrency();
		Double totalReturnRate =hold.getTotalReturnRate();
		Double totalReturnValue = hold.getTotalReturnValue();
		double marketValue = null != hold.getTotalMarketValue() ? hold.getTotalMarketValue() : 0;
		double totalCash = null != hold.getTotalCash() ? hold.getTotalCash() : 0;
		double totalAsset = null != hold.getTotalAsset() ? hold.getTotalAsset() : 0;
		if(null==baseCurrency || "".equals(baseCurrency))baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
		if (currency.equals(baseCurrency)) {
			vo.setTotalMarketValue(marketValue);
			vo.setTotalCash(totalCash);
			vo.setTotalAssets(totalAsset);
			vo.setTotalReturnValue(totalReturnValue);
		} else {
			vo.setTotalMarketValue(marketValue * getExchangeRate(baseCurrency, currency));
			vo.setTotalCash(totalCash * getExchangeRate(baseCurrency, currency));
			vo.setTotalAssets(totalAsset * getExchangeRate(baseCurrency, currency));
			if(null!=totalReturnValue)
			  vo.setTotalReturnValue(totalReturnValue * getExchangeRate(baseCurrency, currency));
		}
		vo.setTotalReturnRate(totalReturnRate);
		return vo;
	}
	
	/**
	 * 获取持仓组合的基金列表
	 * 
	 * @author mqzou 2017-02-22
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	@Override
	public List<HoldProductVO> findHoldFundList(String portfolioHoldId, String currency, String langCode,String memberid) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct p ");
		hql.append(" left join PortfolioHold h on p.portfolioHold.id=h.id");
		hql.append(" left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join FundInfo f on i.id=f.product.id");
		hql.append(" left join " + getLangString("FundInfo", langCode) + " l on f.id=l.id");
		hql.append(" left join PortfolioHoldProductCumperf c on c.portfolioHold.id=h.id and c.product.id=i.id and c.valuationDate=? ");
		hql.append(" where  i.type='fund'");
		List<Object> params = new ArrayList<Object>();
		params.add(DateUtil.getCurDate(Calendar.DATE, -1));
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and h.id=?");
			params.add(portfolioHoldId);
		}
		if(null!=memberid && !"".equals(memberid)){
			hql.append(" and h.member.id=?");
			params.add(memberid);
		}		
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<HoldProductVO> list = new ArrayList<HoldProductVO>();
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) obj[0];
				FundInfo fundInfo = (FundInfo) obj[3];
				PortfolioHoldProductCumperf cumperf=(PortfolioHoldProductCumperf)obj[5];
				
				HoldProductVO vo = new HoldProductVO();
				double holdUnit = null != holdProduct.getHoldingUnit() ? holdProduct.getHoldingUnit() : 0;
				double cost = null != holdProduct.getReferenceCost() ? holdProduct.getReferenceCost() : 0;
				
				String costCurrency=holdProduct.getBaseCurrency();
				if(null==costCurrency || "".equals(costCurrency))costCurrency=CommonConstants.DEF_CURRENCY_HKD;
				if(!costCurrency.equals(currency))cost=getNumByCurrency(cost, costCurrency, currency);
				
				double lastNav = null != fundInfo.getLastNav() ? fundInfo.getLastNav() : 0;
				
				double marketValue = holdUnit * lastNav;
			
				double availableUnit=null!=holdProduct.getAvailableUnit()?holdProduct.getAvailableUnit():0;
				
				String baseCurrency = "";
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					FundInfoEn en = (FundInfoEn) obj[4];
					if (null != en) {
						baseCurrency = en.getFundCurrencyCode();
						vo.setProductName(en.getFundName());
					}
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					FundInfoSc sc = (FundInfoSc) obj[4];
					if (null != sc) {
						baseCurrency = sc.getFundCurrencyCode();
						vo.setProductName(sc.getFundName());
					}
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					FundInfoTc tc = (FundInfoTc) obj[4];
					if (null != tc) {
						baseCurrency = tc.getFundCurrencyCode();
						vo.setProductName(tc.getFundName());
					}
				}
				if(null==baseCurrency || "".equals(baseCurrency))baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
				lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				double pl = holdUnit * (lastNav - cost);
				double plRate =(lastNav - cost)/cost;// marketValue != 0 ? pl / marketValue : 0;
				
				vo.setId(fundInfo.getId());
				vo.setProductId(fundInfo.getProduct().getId());
				
				
				if(!baseCurrency.equals(currency)){
					marketValue=getNumByCurrency(marketValue, baseCurrency, currency);
					//cost=getNumByCurrency(cost, baseCurrency, currency);
					//availableUnit=getNumByCurrency(availableUnit, baseCurrency, currency);
					//pl=getNumByCurrency(pl, baseCurrency, currency);
					//lastNav=getNumByCurrency(lastNav, baseCurrency, currency);					
				}
				vo.setMarketValue(String.valueOf(marketValue));
				vo.setReferenceCost(String.valueOf(cost));
				vo.setAvailableUnit(String.valueOf(availableUnit));
				vo.setPl(String.valueOf(pl));
				vo.setPlRate(String.valueOf(plRate));
				vo.setAccountNo(holdProduct.getAccountNo());
				vo.setHoldUnit(String.valueOf(holdUnit));
				vo.setCurPrice(String.valueOf(lastNav));				
				if(null!=cumperf){
					double dayPl=cumperf.getTotalPl();
					vo.setYesterdayPl(dayPl);					
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取组合的总现金
	 * @author mqzou 2017-02-22
	 * @param portfolioId
	 * @param currency
	 * @return
	 */
	@Override
	public double findPortfolioCash(String portfolioId, String currency,String memberid) {
		double totalCash=0;
		StringBuilder hql=new StringBuilder();
		hql.append(" from PortfolioHoldAccount r where 1=1");
		List<Object> params=new ArrayList<Object>();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql.append(" and r.portfolioHold.id=?");
			params.add(portfolioId);
		}
		if(null!=memberid && !"".equals(memberid)){
			hql.append(" and r.member.id=?");
			params.add(memberid);
		}		
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount account = (PortfolioHoldAccount) it.next();
				double cash=null!=account.getTotalCash()?account.getTotalCash():0;
				String baseCurrency=account.getBaseCurrency();
				if(null==baseCurrency || "".equals(baseCurrency))baseCurrency=CommonConstants.DEF_CURRENCY;
				if(!baseCurrency.equals(currency)){
					cash=getNumByCurrency(cash, baseCurrency, currency);
				}
				totalCash+=cash;
			}
		}
		return totalCash;
	}
	
	/**
	 * 获取持仓组合的股票列表
	 * @author mqzou 2017-02-23
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	@Override
	public List<HoldProductVO> findHoldStockList(String portfolioHoldId, String currency, String langCode,String memberid) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select distinct p,h,i,f,l,c from PortfolioHoldProduct p ");
		hql.append(" left join PortfolioHold h on p.portfolioHold.id=h.id");
		hql.append(" left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join StockInfo f on i.id=f.product.id");
		hql.append(" left join " + getLangString("StockInfo", langCode) + " l on f.id=l.id");
		hql.append(" left join PortfolioHoldProductCumperf c on c.portfolioHold.id=h.id and c.product.id=i.id and c.valuationDate=? ");
		hql.append(" where i.type='stock'");
		List<Object> params = new ArrayList<Object>();
		params.add(DateUtil.getCurDate(Calendar.DATE, -1));
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and h.id=?");
			params.add(portfolioHoldId);
		}
		if(null!=memberid && !"".equals(memberid)){
			hql.append(" and h.member.id=?");
			params.add(memberid);
		}
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<HoldProductVO> list = new ArrayList<HoldProductVO>();

		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) obj[0];
				StockInfo stockInfo = (StockInfo) obj[3];
				if(null==stockInfo)
					continue;
				PortfolioHoldProductCumperf cumperf=(PortfolioHoldProductCumperf)obj[5];
				HoldProductVO vo = new HoldProductVO();
				double holdUnit = null != holdProduct.getHoldingUnit() ? holdProduct.getHoldingUnit() : 0;
				double cost = null != holdProduct.getReferenceCost() ? holdProduct.getReferenceCost() : 0;
				
				String costCurrency=holdProduct.getBaseCurrency();
				if(null==costCurrency || "".equals(costCurrency))
					costCurrency=CommonConstants.DEF_CURRENCY;
				if(!costCurrency.equals(currency))
					cost=getNumByCurrency(cost, costCurrency, currency);
				
				StockMarketDay marketDay=findStockLastNav(stockInfo.getId());
				double  lastNav=null!=marketDay && null!=marketDay.getTradePrice()?marketDay.getTradePrice():0;
				double marketValue = holdUnit * lastNav;
				/*double pl = holdUnit * (lastNav - cost);
				double plRate = (lastNav - cost)/cost;//marketValue != 0 ? pl / marketValue : 0;
*/				double availableUnit=null!=holdProduct.getAvailableUnit()?holdProduct.getAvailableUnit():0;
				
				
				String baseCurrency = "";
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					StockInfoEn en = (StockInfoEn) obj[4];
					if (null != en) {
						baseCurrency = en.getStockCurrencyCode();
						vo.setProductName(en.getStockName());
					}
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					StockInfoSc sc = (StockInfoSc) obj[4];
					if (null != sc) {
						baseCurrency = sc.getStockCurrencyCode();
						vo.setProductName(sc.getStockName());
					}
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					StockInfoTc tc = (StockInfoTc) obj[4];
					if (null != tc) {
						baseCurrency = tc.getStockCurrencyCode();
						vo.setProductName(tc.getStockName());
					}
				}
				if(null==baseCurrency || "".equals(baseCurrency))
					baseCurrency=CommonConstants.DEF_CURRENCY_HKD;

				lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				double pl = holdUnit * (lastNav - cost);
				double plRate =(lastNav - cost)/cost;// marketValue != 0 ? pl / marketValue : 0;
				vo.setId(stockInfo.getId());
								if(!baseCurrency.equals(currency)){
					marketValue=getNumByCurrency(marketValue, baseCurrency, currency);
					//cost=getNumByCurrency(cost, baseCurrency, currency);
					//availableUnit=getNumByCurrency(availableUnit, baseCurrency, currency);
					//pl=getNumByCurrency(pl, baseCurrency, currency);
					//lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				}
				vo.setMarketValue(String.valueOf(marketValue));
				vo.setReferenceCost(String.valueOf(cost));
				vo.setAvailableUnit(String.valueOf(availableUnit));
				vo.setPl(String.valueOf(pl));
				vo.setPlRate(String.valueOf(plRate));
				vo.setAccountNo(holdProduct.getAccountNo());
				vo.setHoldUnit(String.valueOf(holdUnit));
				vo.setCurPrice(String.valueOf(lastNav));
				if(null!=cumperf){
					double dayPl=cumperf.getTotalPl();
					vo.setYesterdayPl(dayPl);
					
				}
				list.add(vo);
				
 
			}
		}
		return list;
	}
	
	/**
	 * 获取股票的最新价格
	 * @author mqzou 2017-02-24
	 * @param stockId
	 * @return
	 */
	private StockMarketDay findStockLastNav(String stockId){
		StringBuilder hql=new StringBuilder();
		hql.append(" from StockMarketDay r where r.stock.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(stockId);
		JsonPaging jsonPaging=new JsonPaging();
		jsonPaging.setOrder(" desc");
		jsonPaging.setSort("r.priceDate");
		jsonPaging.setRows(1);
		jsonPaging.setPage(1);
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		if(null!=list && !list.isEmpty() ){
			StockMarketDay marketDay=(StockMarketDay)list.get(0);
			return marketDay;
			
		}
		return null;
		
	}
	
	/**
	 * 获取持仓组合的债券列表
	 * @author mqzou 2017-02-24
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	@Override
	public List<HoldProductVO> findHoldBond(String portfolioHoldId, String currency, String langCode,String memberid) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct p ");
		hql.append(" left join PortfolioHold h on p.portfolioHold.id=h.id");
		hql.append(" left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join BondInfo f on i.id=f.product.id");
		hql.append(" left join " + getLangString("BondInfo", langCode) + " l on f.id=l.id");
		hql.append(" left join PortfolioHoldProductCumperf c on c.portfolioHold.id=h.id and c.product.id=i.id and c.valuationDate=? ");
		hql.append(" where i.type='bond'");
		List<Object> params = new ArrayList<Object>();
		params.add(DateUtil.getCurDate(Calendar.DATE, -1));
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and h.id=?");
			params.add(portfolioHoldId);
		}
		if(null!=memberid && !"".equals(memberid)){
			hql.append(" and h.member.id=?");
			params.add(memberid);
		}
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<HoldProductVO> list = new ArrayList<HoldProductVO>();

		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) obj[0];
				BondInfo bondInfo = (BondInfo) obj[3];
				if(null==bondInfo)continue;
				PortfolioHoldProductCumperf cumperf=(PortfolioHoldProductCumperf)obj[5];
				HoldProductVO vo = new HoldProductVO();
				double holdUnit = null != holdProduct.getHoldingUnit() ? holdProduct.getHoldingUnit() : 0;
				double cost = null != holdProduct.getReferenceCost() ? holdProduct.getReferenceCost() : 0;
				
				String costCurrency=holdProduct.getBaseCurrency();
				if(null==costCurrency || "".equals(costCurrency))costCurrency=CommonConstants.DEF_CURRENCY_HKD;
				if(!costCurrency.equals(currency))cost=getNumByCurrency(cost, costCurrency, currency);
				
				BondMarketDay marketDay=findBondLastNav(bondInfo.getId());
				double lastNav =null!=marketDay && null!=marketDay.getTradePrice()?marketDay.getTradePrice():0;
				double marketValue = holdUnit * lastNav;
				
				double availableUnit=null!=holdProduct.getAvailableUnit()?holdProduct.getAvailableUnit():0;
				
				String baseCurrency = "";
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					BondInfoEn en = (BondInfoEn) obj[4];
					if (null != en) {
						baseCurrency = en.getBondCurrencyCode();
						vo.setProductName(en.getBondName());
					}
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					BondInfoSc sc = (BondInfoSc) obj[4];
					if (null != sc) {
						baseCurrency = sc.getBondCurrencyCode();
						vo.setProductName(sc.getBondName());
					}
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					BondInfoTc tc = (BondInfoTc) obj[4];
					if (null != tc) {
						baseCurrency = tc.getBondCurrencyCode();
						vo.setProductName(tc.getBondName());
					}
				}
				if(null==baseCurrency || "".equals(baseCurrency))baseCurrency=CommonConstants.DEF_CURRENCY;
				lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				double pl = holdUnit * (lastNav - cost);
				double plRate =(lastNav - cost)/cost;// marketValue != 0 ? pl / marketValue : 0;
				vo.setId(bondInfo.getId());
				
				if(!baseCurrency.equals(currency)){
					marketValue=getNumByCurrency(marketValue, baseCurrency, currency);
					//cost=getNumByCurrency(cost, baseCurrency, currency);
					//availableUnit=getNumByCurrency(availableUnit, baseCurrency, currency);
					//pl=getNumByCurrency(pl, baseCurrency, currency);
					//lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				}
				vo.setMarketValue(String.valueOf(marketValue));
				vo.setReferenceCost(String.valueOf(cost));
				vo.setAvailableUnit(String.valueOf(availableUnit));
				vo.setPl(String.valueOf(pl));
				vo.setPlRate(String.valueOf(plRate));
				vo.setAccountNo(holdProduct.getAccountNo());
				vo.setHoldUnit(String.valueOf(holdUnit));
				vo.setCurPrice(String.valueOf(lastNav));
				if(null!=cumperf){
					double dayPl=cumperf.getTotalPl();
					vo.setYesterdayPl(dayPl);
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取债券的最新价格
	 * @author mqzou 2017-02-24
	 * @param bondId
	 * @return
	 */
	private BondMarketDay findBondLastNav(String bondId){
		StringBuilder hql=new StringBuilder();
		hql.append(" from BondMarketDay r where r.bond.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(bondId);
		JsonPaging jsonPaging=new JsonPaging();
		jsonPaging.setOrder(" desc");
		jsonPaging.setSort("r.priceDate");
		jsonPaging.setRows(1);
		jsonPaging.setPage(1);
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		if(null!=list && !list.isEmpty() ){
			BondMarketDay marketDay=(BondMarketDay)list.get(0);
			return marketDay;
		}
		return null;
	}
	
	/**
	 * 获取持仓组合的保险列表
	 * @author mqzou 2017-02-24
	 * @param portfolioHoldId
	 * @param currency
	 * @return
	 */
	@Override
	public List<HoldProductVO> findHoldInsure(String portfolioHoldId, String currency, String langCode,String memberid) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct p ");
		hql.append(" left join PortfolioHold h on p.portfolioHold.id=h.id");
		hql.append(" left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join InsureInfo f on i.id=f.product.id");
		hql.append(" left join " + getLangString("InsureInfo", langCode) + " l on f.id=l.id");
		hql.append(" left join PortfolioHoldProductCumperf c on c.portfolioHold.id=h.id and c.product.id=i.id and c.valuationDate=? ");
		hql.append(" where i.type='insure'");
		List<Object> params = new ArrayList<Object>();
		params.add(DateUtil.getCurDate(Calendar.DATE, -1));
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and h.id=?");
			params.add(portfolioHoldId);
		}
		if(null!=memberid && !"".equals(memberid)){
			hql.append(" and h.member.id=?");
			params.add(memberid);
		}
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<HoldProductVO> list = new ArrayList<HoldProductVO>();
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) obj[0];
				InsureInfo insureInfo = (InsureInfo) obj[2];
				if(null==insureInfo)continue;
				PortfolioHoldProductCumperf cumperf=(PortfolioHoldProductCumperf)obj[5];
				HoldProductVO vo = new HoldProductVO();
				double holdUnit = null != holdProduct.getHoldingUnit() ? holdProduct.getHoldingUnit() : 0;
				double cost = null != holdProduct.getReferenceCost() ? holdProduct.getReferenceCost() : 0;
				String costCurrency=holdProduct.getBaseCurrency();
				if(null==costCurrency || "".equals(costCurrency))costCurrency=CommonConstants.DEF_CURRENCY_HKD;
				if(!costCurrency.equals(currency))cost=getNumByCurrency(cost, costCurrency, currency);
				double lastNav = null != insureInfo.getLastNav()?insureInfo.getLastNav():0;
				double marketValue = holdUnit * lastNav;
				
				double availableUnit=null!=holdProduct.getAvailableUnit()?holdProduct.getAvailableUnit():0;
				
				/*BondMarketDay marketDay=findBondLastNav(insureInfo.getId());
				lastNav=null!=marketDay && null!=marketDay.getTradePrice()?marketDay.getTradePrice():0;*/
				
				String baseCurrency = "";
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					InsureInfoEn en = (InsureInfoEn) obj[3];
					if (null != en) {
						baseCurrency = en.getInsureCurrencyCode();
						vo.setProductName(en.getInsureName());
					}
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					InsureInfoSc sc = (InsureInfoSc) obj[3];
					if (null != sc) {
						baseCurrency = sc.getInsureCurrencyCode();
						vo.setProductName(sc.getInsureName());
					}
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					InsureInfoTc tc = (InsureInfoTc) obj[3];
					if (null != tc) {
						baseCurrency = tc.getInsureCurrencyCode();
						vo.setProductName(tc.getInsureName());
					}
				}
				if(null==baseCurrency || "".equals(baseCurrency))
					baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
				lastNav=getNumByCurrency(lastNav, baseCurrency, currency);
				double pl = holdUnit * (lastNav - cost);
				double plRate = (lastNav - cost)/cost;//marketValue != 0 ? pl / marketValue : 0;
				vo.setId(insureInfo.getId());
				
				if(!baseCurrency.equals(currency)){
					marketValue=getNumByCurrency(marketValue, baseCurrency, currency);
				//	cost=getNumByCurrency(cost, baseCurrency, currency);
				//	availableUnit=getNumByCurrency(availableUnit, baseCurrency, currency);
					//pl=getNumByCurrency(pl, baseCurrency, currency);
				
				}
				vo.setMarketValue(String.valueOf(marketValue));
				vo.setReferenceCost(String.valueOf(cost));
				vo.setAvailableUnit(String.valueOf(availableUnit));
				vo.setPl(String.valueOf(pl));
				vo.setPlRate(String.valueOf(plRate));
				vo.setAccountNo(holdProduct.getAccountNo());
				vo.setHoldUnit(String.valueOf(holdUnit));
				vo.setCurPrice(String.valueOf(lastNav));
				if(null!=cumperf){
					double dayPl=cumperf.getTotalPl();
					vo.setYesterdayPl(dayPl);
					
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取投资人的总资产数据
	 * @author mqzou 2017-03-24
	 * @param memberId
	 * @param currency
	 * @return
	 */
	@Override
	public MyAssetsVO findInvestorAssets(String memberId, String currency) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from MyAsset r where r.member.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MyAsset myAsset=(MyAsset)list.get(0);
			MyAssetsVO vo=new MyAssetsVO();
			vo.setMemberId(memberId);
			double totalAsset=null!=myAsset.getTotalAsset()?myAsset.getTotalAsset():0;
			double totalMarket=null!=myAsset.getTotalMarket()?myAsset.getTotalMarket():0;
			double totalCash=null!=myAsset.getTotalCash()?myAsset.getTotalCash():0;
			Double totalReturnValue=myAsset.getTotalPl();
			Double accReturn=myAsset.getAccReturn();
			Double dayReturn=myAsset.getDayReturn();
			String baseCurrency=myAsset.getCurrencyType();
			if(null==baseCurrency || "".equals(baseCurrency))baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
			vo.setTotalAssets(getNumByCurrency(totalAsset, baseCurrency, currency));
			vo.setTotalMarketValue(getNumByCurrency(totalMarket, baseCurrency, currency));
			vo.setTotalCashValue(getNumByCurrency(totalCash, baseCurrency, currency));
			if(null!=totalReturnValue)
			   vo.setTotalReturnValue(getNumByCurrency(totalReturnValue, baseCurrency, currency));
			if(null!=accReturn)
			   vo.setTotalReturnRate(accReturn);
			if(null!=dayReturn)
				   vo.setDayReturn(dayReturn);
			hql=new StringBuilder();
			hql.append(" from PortfolioHoldAccount r where r.member.id=?");
			list=baseDao.find(hql.toString(), params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				Iterator it=list.iterator();
				Double totalCashAvailable=null;
				Double totalCashHold=null;
				Double totalCashWithdrawal=null;				
				while (it.hasNext()) {
					PortfolioHoldAccount holdAccount = (PortfolioHoldAccount) it.next();
					Double cashAvailable=holdAccount.getCashAvailable();
					Double cashHold=holdAccount.getCashHold();
					Double cashWithdrawal=holdAccount.getCashWithdrawal();
					baseCurrency=holdAccount.getBaseCurrency();
					if(null!=cashAvailable){
						if(null==totalCashAvailable){
							totalCashAvailable=0.00;
						}
						totalCashAvailable+=getNumByCurrency(cashAvailable, baseCurrency, currency);
					}
					if(null!=cashHold ){
						if(null==totalCashHold){
							totalCashHold=0.00;
						}
						totalCashHold+=getNumByCurrency(cashHold, baseCurrency, currency);
					}
					
					if(null!=cashWithdrawal){
						if( null==totalCashWithdrawal){
							totalCashWithdrawal=0.00;
						}
						totalCashWithdrawal+=getNumByCurrency(cashWithdrawal, baseCurrency, currency);
					}
				}
				vo.setCashAvailable(totalCashAvailable);
				vo.setCashHold(totalCashHold);
				vo.setCashWithdrawal(totalCashWithdrawal);
			}
			
			Date yesterday= DateUtil.getCurDate(Calendar.DATE, -1);
			hql=new StringBuilder();
			hql.append(" from MyAssetHis r where r.member.id=? and r.valuationDate=?");
			params.add(yesterday);
			list=baseDao.find(hql.toString(), params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				MyAssetHis his=(MyAssetHis)list.get(0);
				Double returnRate=his.getAccReturn();
				Double returnValue=his.getTotalPl();
				baseCurrency=his.getCurrencyType();
				vo.setYesterdayReturnRate(returnRate);
				if(null!=returnValue)
				  vo.setYesterdayReturnValue(getNumByCurrency(returnValue, baseCurrency, currency));
			}
		    vo.setCurrency(currency);
			return vo;
		}
		return null;
	}
}
