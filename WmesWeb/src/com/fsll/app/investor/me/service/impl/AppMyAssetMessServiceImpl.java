package com.fsll.app.investor.me.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.me.service.AppMyAssetMessService;
import com.fsll.app.investor.me.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppMyAssetsHisMessVo;
import com.fsll.app.investor.me.vo.AppMyAssetsMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondMarketDay;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.InsureInfo;
import com.fsll.wmes.entity.InsureInfoEn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.MyAssetHis;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockMarketDay;

/**
 * 我的资产信息接口服务接口类现实
 * @author zpzhou
 * @date 2016-9-13
 */
@Service("appMyAssetMessService")
//@Transactional
public class AppMyAssetMessServiceImpl extends BaseService implements AppMyAssetMessService {

	/**
	 * 得到我的资产信息
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public AppMyAssetsMessVo findMyAssetMess(String memberId,String toCurrency,String langCode){
		AppMyAssetsMessVo  vo = new AppMyAssetsMessVo();
		String hql = " from MyAsset m where m.member.id=? ";
		List params = new ArrayList();
		params.add(memberId);
		List<MyAsset> list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MyAsset assets= (MyAsset)list.get(0);
			MemberBase member = assets.getMember();
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(member.getIconUrl(),""));
			String fromCurrency=String.valueOf(assets.getCurrencyType());
			if(null != toCurrency && !"".equals(toCurrency)){
				Double rate=getExchangeRate(fromCurrency, toCurrency);
				vo.setTotalAsset(getFormatNumByRate(assets.getTotalAsset(),rate,toCurrency));
				vo.setTotalMarket(getFormatNumByRate(assets.getTotalMarket(),rate,toCurrency));
				vo.setTotalCash(getFormatNumByRate(assets.getTotalCash(),rate,toCurrency));
				vo.setTotalPl(getFormatNumByRate(assets.getTotalPl(),rate,toCurrency));
				vo.setDayPl(getFormatNumByRate(assets.getDayPl(),rate,toCurrency));
				vo.setCurrencyType(getParamConfigName(langCode, toCurrency, "currency_type"));
			}else {
				vo.setTotalAsset(getFormatNum(assets.getTotalAsset(),fromCurrency));
				vo.setTotalMarket(getFormatNum(assets.getTotalMarket(),fromCurrency));
				vo.setTotalCash(getFormatNum(assets.getTotalCash(),fromCurrency));
				vo.setTotalPl(getFormatNum(assets.getTotalPl(),fromCurrency));
				vo.setDayPl(getFormatNum(assets.getDayPl(),fromCurrency));
				vo.setCurrencyType(getParamConfigName(langCode, fromCurrency, "currency_type"));
			}
			vo.setAccReturn(getFormatNumByPer(assets.getAccReturn()));
			vo.setLastUpdate(DateUtil.dateToDateString(assets.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
		}else {
			MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
			if(null != member){
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(member.getIconUrl(),""));
			}
		}
		return vo;
	}
	
//	/**
//	 * 得到我的历史资产信息列表
//	 * @param memberId 用户ID
//	 * @param toCurrency 货币转换参数
//	 * @return
//	 */
//	public List<AppMyAssetsHisMessVo> findMyAssetHisMess(String memberId,String toCurrency){
//		List<AppMyAssetsHisMessVo>  assetsList = new ArrayList<AppMyAssetsHisMessVo>();
//		String hql = " from MyAssetHis m where m.member.id=? order by m.valuationDate ";
//		List params = new ArrayList();
//		params.add(memberId);
//		List<MyAssetHis> list = baseDao.find(hql, params.toArray(), false);
//		for(int i=0;i<list.size();i++){
//			AppMyAssetsHisMessVo vo =new AppMyAssetsHisMessVo();
//			MyAssetHis assets= list.get(i);
//			String fromCurrency=String.valueOf(assets.getCurrencyType());
//			Double rate=getExchangeRate(fromCurrency, toCurrency);
//			vo.setAccReturn(getFormatNumByRate(assets.getAccReturn(),rate));
//			vo.setCashAmount(getFormatNumByRate(assets.getCashAmount(),rate));
//			vo.setTotalAmount(getFormatNumByRate(assets.getTotalAmount(),rate));
//			vo.setCurrencyType(fromCurrency);
//			vo.setValuationDate(DateUtil.dateToDateString(assets.getValuationDate(),"yyyy-MM-dd"));
//			assetsList.add(vo);
//		}
//		return assetsList;
//	}
	
	/**
	 * 得到我的历史资产信息列表
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @param startDate 开始时间
	 * @return
	 */
	public List<AppMyAssetsHisMessVo> findMyAssetHisMess(String memberId,String toCurrency,String startDate){
		List<AppMyAssetsHisMessVo>  assetsList = new ArrayList<AppMyAssetsHisMessVo>();
		String hql = " from MyAssetHis m where m.member.id=? and m.valuationDate >= ? order by m.valuationDate ";
		List params = new ArrayList();
		params.add(memberId);
		params.add( DateUtil.getDate(startDate));
		List<MyAssetHis> list = baseDao.find(hql, params.toArray(), false);
		
		for(int i=0;i<list.size();i++){
			AppMyAssetsHisMessVo vo =new AppMyAssetsHisMessVo();
			MyAssetHis assets= list.get(i);
			String fromCurrency=String.valueOf(assets.getCurrencyType());
			Double rate=getExchangeRate(fromCurrency, toCurrency);
			vo.setAccReturn(getFormatNumByRate(assets.getAccReturn(),rate,toCurrency));
			vo.setCashAmount(getFormatNumByRate(assets.getTotalCash(),rate,toCurrency));
			vo.setTotalAmount(getFormatNumByRate(assets.getTotalAsset(),rate,toCurrency));
			vo.setCurrencyType(fromCurrency);
			vo.setValuationDate(DateUtil.dateToDateString(assets.getValuationDate(),"yyyy-MM-dd"));
			assetsList.add(vo);
		}
		return assetsList;
	}
	
	/**
	 * 得到资产分析的产品配置信息
	 * @author zxtan
	 * @date 2017-02-17
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findProductAllocationList(String memberId,String langCode,String toCurrency){
		List<AppPortfolioAllocationVO>  voList = new ArrayList<AppPortfolioAllocationVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" left join ProductInfo f on f.id=m.product.id ");		
		hql.append(" where h.member.id=? ");
		
		List params = new ArrayList();
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppPortfolioHoldProductVO> productVOList = new ArrayList<AppPortfolioHoldProductVO>();
		
		//市值总额
		double fundTotal = 0;
		double bondTotal = 0;
		double stockTotal = 0;
		double insureTotal = 0;
		double fundTotalReturn = 0;
		double bondTotalReturn = 0;
		double stockTotalReturn = 0;
		double insureTotalReturn = 0;
		
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
//			PortfolioHold hold = (PortfolioHold) objs[1];
			AppPortfolioHoldProductVO  vo = new AppPortfolioHoldProductVO();
			vo.setId(product.getId());
			vo.setHoldingUnit(product.getHoldingUnit());
			
			vo.setAvailableUnit(product.getAvailableUnit());
			Double referenceCost;
			if(null != toCurrency && !"".equals(toCurrency)){
				vo.setBaseCurrency(toCurrency);
				referenceCost = getNumByCurrency(product.getReferenceCost(), product.getBaseCurrency(), toCurrency);
			}else {
				vo.setBaseCurrency(product.getBaseCurrency());
				referenceCost = product.getReferenceCost();
			}
			
			vo.setReferenceCost(referenceCost);
			String productType = "";
			if(null != objs[2]){
				ProductInfo info = (ProductInfo) objs[2];
				productType = info.getType();
				vo.setProductType(productType);				
			}
			
			String productId = product.getProduct().getId();
						
			AppProductInfoVO productInfoVO = findProductInfo(productId, "en");
			Double rate = getExchangeRate(product.getBaseCurrency(), vo.getBaseCurrency());
			if(null != rate){
				double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
				double unit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
				double refCost = product.getReferenceCost()==null?lastNav:product.getHoldingUnit();
				double totalMarketValue = lastNav* unit * rate; 
				
				double returnValue = (lastNav-refCost)* unit * rate; 
				
				vo.setTotalMarketValue(totalMarketValue);
				
				if("fund".equalsIgnoreCase(productType)){
					fundTotal += totalMarketValue;
					fundTotalReturn += returnValue;
				}else if ("bond".equalsIgnoreCase(productType)) {
					bondTotal += totalMarketValue;
					bondTotalReturn += returnValue;
				}else if ("stock".equalsIgnoreCase(productType)) {
					stockTotal += totalMarketValue;
					stockTotalReturn += returnValue;
				}else if ("insure".equalsIgnoreCase(productType)) {
					insureTotal += totalMarketValue;
					insureTotalReturn += returnValue;
				}
			}
						
			productVOList.add(vo);	
		}
		

		
		double cash = 0.0;
		String totalCash;
		AppMyAssetsMessVo myAsset = findMyAssetMess(memberId, toCurrency,langCode);
		if(null != myAsset){
			totalCash = myAsset.getTotalCash()==null? "0":myAsset.getTotalCash().replace(",", "").replace("-", "");			
		}else {
			totalCash = "0";
		}
		
		cash = Double.parseDouble(totalCash);
		
		double total = fundTotal+bondTotal+stockTotal+insureTotal+cash;
		
		if(fundTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("fund", fundTotal, fundTotalReturn, total,toCurrencyName);
			voList.add(vo);
		}
		
		if(bondTotal>0){		
			AppPortfolioAllocationVO vo = genPortfolioAllocation("bond", bondTotal, bondTotalReturn, total,toCurrencyName);
			voList.add(vo);
		}
		
		if(stockTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("stock", stockTotal, stockTotalReturn, total,toCurrencyName);
			voList.add(vo);
		}

		if(insureTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("insure", insureTotal, insureTotalReturn, total,toCurrencyName);
			voList.add(vo);
		}

		if(cash>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("cash", cash, 0, total,toCurrencyName);
			voList.add(vo);
		}
		
		return voList;
	}
	
	/**
	 * 生成组合配置情况
	 * @author zxtan
	 * @date 2016-12-27
	 * @param itemName
	 * @param itemValue
	 * @param itemIncrease
	 * @param total
	 * @param toCurrency
	 * @return
	 */
	private AppPortfolioAllocationVO genPortfolioAllocation(String itemName,double itemValue,double itemReturn,double total,String toCurrency)
	{
		AppPortfolioAllocationVO vo = new AppPortfolioAllocationVO();
		vo.setItemName(itemName);
		vo.setItemValue(getFormatNum(itemValue));
		vo.setItemReturn(getFormatNumByPer(itemReturn));
		double rate = itemValue/total;
		vo.setItemRate(getFormatNum(rate));
		vo.setItemCurrency(toCurrency);
		return vo;
	}
	
	
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	private PortfolioHoldProductCumperf findPortfolioHoldProductCumperf(String portfolioHoldId,String productId){
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProductCumperf m ");		
		hql.append(" where m.portfolioHold.id=? and m.product.id=? ");
		hql.append(" order by m.valuationDate desc ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(productId);
		
		List<PortfolioHoldProductCumperf> list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		if(null != list && !list.isEmpty())
			return list.get(0);
		else {
			return null;
		}
	}
	
	
	/**
	 * 得到资产分析的基金配置信息
	 * @author zxtan
	 * @date 2017-02-17
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @param groupType 配置分组类型
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findProductFundAllocationList(String memberId,String langCode,String toCurrency,String groupType){
		List<AppPortfolioAllocationVO>  voList = new ArrayList<AppPortfolioAllocationVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" INNER JOIN PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" LEFT JOIN ProductInfo p on p.id=m.product.id ");	
		hql.append(" LEFT JOIN FundInfo f ON f.product.id = m.product.id ");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");	
		hql.append(" where p.type='fund' and h.member.id=? ");
		
		List params = new ArrayList();
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		double total = 0;
		Map<String, Double> map = new HashMap<String, Double>();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
			PortfolioHold hold = (PortfolioHold) objs[1];
			FundInfo fundInfo = (FundInfo) objs[3];
			FundInfoSc fundInfoSc = new FundInfoSc();
			BeanUtils.copyProperties(objs[4], fundInfoSc);
			
			String keyString = "";
			if("fundType".equalsIgnoreCase(groupType)){
				keyString = fundInfoSc.getFundType();
			}else if("sectorType".equalsIgnoreCase(groupType)){
				keyString = fundInfoSc.getSectorType();
			}else if("geoAllocation".equalsIgnoreCase(groupType)){
				keyString = fundInfoSc.getGeoAllocation();
			}
			
			toCurrency = "".equals(toCurrency)? hold.getBaseCurrency():toCurrency;

			
			String productId = product.getProduct().getId();
			String holdId = hold.getId();
			PortfolioHoldProductCumperf cumperf = findPortfolioHoldProductCumperf(holdId, productId);
			Double totalMarketValue = 0.0;
			if(null != cumperf){
				totalMarketValue = getNumByCurrency(cumperf.getTotalMarketValue(), cumperf.getBaseCurrency(), toCurrency) ;								
			}else {
				double holdingUnit = product.getHoldingUnit(); 
				double lastNav = fundInfo.getLastNav();
				totalMarketValue = lastNav * holdingUnit;
				totalMarketValue = getNumByCurrency(totalMarketValue, product.getBaseCurrency(), toCurrency);
			}

			total += totalMarketValue;
			if(map.containsKey(keyString)){
				map.put(keyString, map.get(keyString) + totalMarketValue);	
			}else {
				map.put(keyString, totalMarketValue);
			}
		}
		
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			AppPortfolioAllocationVO vo = new AppPortfolioAllocationVO();
			vo.setItemCurrency(toCurrency);
			vo.setItemName(entry.getKey());
			vo.setItemValue(getFormatNum(entry.getValue()));
			double value = entry.getValue();
			double rate = value/total;
			rate = new BigDecimal(rate).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			vo.setItemRate(String.valueOf(rate));
			voList.add(vo);
		}		
		
		return voList;
	}
	
	/**
	 * 得到我的资产的基金产品信息列表
	 * @author zxtan
	 * @date 2017-03-03
	 * @param memberId 会员Id
	 * @param productType 产品类型
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppHoldProductVO> findHoldProductList(String memberId,String productType,String langCode,String toCurrency){
		List<AppHoldProductVO>  voList = new ArrayList<AppHoldProductVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" Inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" Inner join ProductInfo p on p.id=m.product.id ");
		hql.append(" where h.member.id=? and p.type=? ");
		
		List params = new ArrayList();
		params.add(memberId);
		params.add(productType);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			double totalAmount = 0;	
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppHoldProductVO  vo = new AppHoldProductVO();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];

				String productId = holdProduct.getProduct().getId();
				AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
				
				
				vo.setHoldProductId(holdProduct.getId());
				vo.setPortfolioHoldId(holdProduct.getPortfolioHold().getId());
				vo.setProductId(productId);
				vo.setProductName(productInfoVO.getProductName());	
				vo.setProductType(productType);
				vo.setBaseCurrency(toCurrencyName);
									
				double marketValue = 0;
				double refCost= 0;
				double returnValue = 0;
				double returnRate = 0;

				double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
				double unit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
				refCost = holdProduct.getReferenceCost()==null?lastNav:holdProduct.getReferenceCost();
				
				Double rate = getExchangeRate(holdProduct.getBaseCurrency(), toCurrency);
				if(null != rate){				
					marketValue = lastNav * unit * rate;					
					returnValue = (lastNav - refCost)* unit * rate;
					returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
				}
							
				totalAmount += marketValue;
				
				vo.setMarketValue(getFormatNum(marketValue,toCurrency));
				vo.setReturnValue(getFormatNum(returnValue,toCurrency));
				vo.setReturnRate(getFormatNumByPer(returnRate));				
				
							
				voList.add(vo);
			}
			
			for (int i = 0; i < voList.size(); i++) {
				AppHoldProductVO vo = voList.get(i);
				double marketValue = 0;
				if(null != vo.getMarketValue()){
					marketValue = Double.parseDouble(vo.getMarketValue().replace(",", ""));			
				}
				double weight = 0;
				if (totalAmount != 0){
					weight = marketValue / totalAmount;
				}
					
				vo.setWeight(getFormatNumByPer(weight));
				voList.set(i, vo);
			}
		}
		
		return voList;
	}
	
	
	/**
	 * 得到我的资产的产品回报信息
	 * @author zxtan
	 * @date 2017-03-03
	 * @param holdProductId 持仓产品Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldProductReturnDetailVO findHoldProductReturnDetail(String holdProductId,String langCode,String toCurrency){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" Inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" Inner join ProductInfo p on p.id=m.product.id ");
		hql.append(" where m.id=? ");
		
		List params = new ArrayList();
		params.add(holdProductId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);

		AppHoldProductReturnDetailVO  vo = new AppHoldProductReturnDetailVO();
		if(null != list && !list.isEmpty()){
			
			Object[] objs = (Object[])list.get(0);
			PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];

			String productId = holdProduct.getProduct().getId();
			AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
			
			
			vo.setHoldProductId(holdProduct.getId());
			vo.setPortfolioHoldId(holdProduct.getPortfolioHold().getId());
			vo.setProductId(productId);
			vo.setProductName(productInfoVO.getProductName());	
			vo.setProductCode(productInfoVO.getProductCode());
			vo.setProductType(holdProduct.getProduct().getType());
								
			double marketValue = 0;
			double refCost= 0;
			double returnValue = 0;
			double returnRate = 0;

			double availableUnit = holdProduct.getAvailableUnit()==null?0:holdProduct.getAvailableUnit();
			double unit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
			double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
			refCost = holdProduct.getReferenceCost()==null?lastNav:holdProduct.getReferenceCost();
			
			vo.setHoldingUnit(getFormatNum(unit));
			vo.setAvailableUnit(getFormatNum(availableUnit));
			
			if("".equals(toCurrency)){	
				vo.setLastPrice(getFormatNum(lastNav,holdProduct.getBaseCurrency()));
				vo.setRefCost(getFormatNum(refCost,holdProduct.getBaseCurrency()));
				vo.setBaseCurrency(holdProduct.getBaseCurrency());
				marketValue = lastNav * unit;					
				returnValue = (lastNav - refCost)* unit;
				returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
			}else {
				vo.setBaseCurrency(toCurrency);
				Double rate = getExchangeRate(holdProduct.getBaseCurrency(), toCurrency);
				if(null != rate){	
					vo.setLastPrice(getFormatNum(lastNav*rate,toCurrency));
					vo.setRefCost(getFormatNum(refCost*rate,toCurrency));
					marketValue = lastNav * unit * rate;					
					returnValue = (lastNav - refCost)* unit * rate;
					returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
				}
			}
						
			vo.setMarketValue(getFormatNum(marketValue,vo.getBaseCurrency()));
			vo.setReturnValue(getFormatNum(returnValue,vo.getBaseCurrency()));
			vo.setReturnRate(getFormatNumByPer(returnRate));				
			vo.setAccountNo(holdProduct.getAccountNo());
		}
		
		return vo;
	}
	
/*	*//**
	 * 得到我的资产的基金产品信息列表
	 * @author zxtan
	 * @date 2017-02-22
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 *//*
	public List<AppPortfolioProductVo> findProductFundList(String memberId,String langCode,String toCurrency){
		List<AppPortfolioProductVo>  messList = new ArrayList<AppPortfolioProductVo>();
		StringBuilder hql = new StringBuilder();
		hql.append("select m.id,m.holdingUnit,s.fundName,m.baseCurrency,s.fundType,f.riskLevel,f.lastNav,m.referenceCost,m.product.id,h.id ");
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" INNER JOIN PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" where h.member.id=? ");
		
		List params = new ArrayList();
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		double totalAmount = 0;
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppPortfolioProductVo  vo = new AppPortfolioProductVo();
				vo.setId(objs[0]==null?"":objs[0].toString());			
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				//vo.setAllocationRate(objs[1]==null?"":objs[1].toString());
				vo.setFundName(objs[2]==null?"":objs[2].toString());
				//String fromCurrency = objs[3]==null?"":objs[3].toString();
				vo.setFundCurrency(toCurrency);
				vo.setFundType(objs[4]==null?"":objs[4].toString());
				vo.setRiskLevel(objs[5]==null?"":objs[5].toString());
				//String lastNav = objs[6]==null?"0":objs[6].toString();
				//vo.setLastNav(objs[6]==null?"":objs[6].toString());
				//vo.setIncrease(objs[7]==null?"":objs[7].toString());
				//String holdingUnit = objs[1]==null?"0":objs[1].toString();
				String productId = objs[8]==null?"":objs[8].toString();
				String portfolioHoldId = objs[9]==null?"":objs[9].toString();
				vo.setProductId(productId);
				vo.setPortfolioHoldId(portfolioHoldId);
				
				PortfolioHoldProductCumperf cumperf = findPortfolioHoldProductCumperf(portfolioHoldId, productId);
				if(null != cumperf){					
					Double totalMarketValue;
					String totalPl;
					if(null != toCurrency && !"".equals(toCurrency) ){
						totalMarketValue = getNumByCurrency(cumperf.getTotalMarketValue(), cumperf.getBaseCurrency(), toCurrency) ;
						totalPl = getFormatNumByCurrency(cumperf.getTotalPl(), cumperf.getBaseCurrency(), toCurrency) ;
					}else {
						totalMarketValue = cumperf.getTotalMarketValue() ;
						totalPl = getFormatNum(cumperf.getTotalPl()) ;
					}
					
					Double cumperfRate = cumperf.getCumperfRate();
					
					totalAmount += totalMarketValue;
					
					vo.setMarketValue(getFormatNum(totalMarketValue));
					vo.setReturnValue(totalPl);
					vo.setCumperfRate(getFormatNumByPer(cumperfRate));				
				}
							
				messList.add(vo);
			}
			
			for (int i = 0; i < messList.size(); i++) {
				AppPortfolioProductVo  vo = messList.get(i);
				double marketValue = 0;
				if(null != vo.getMarketValue()){
					marketValue = Double.parseDouble(vo.getMarketValue().replace(",", ""));			
				}
				double rate = 0;
				if (totalAmount != 0)
				{
					rate = marketValue / totalAmount;
				}
					
				vo.setAllocationRate(getFormatNumByPer(rate));
				messList.set(i, vo);
			}
		}
		
		return messList;
	}	*/

}
