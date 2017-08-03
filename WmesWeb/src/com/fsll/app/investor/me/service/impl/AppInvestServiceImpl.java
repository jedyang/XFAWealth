package com.fsll.app.investor.me.service.impl;

import java.io.Console;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.me.service.AppInvestService;
import com.fsll.app.investor.me.vo.AppInvestAccountVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForBuyVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForCheckVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForRebalanceVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioReturnVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioVO;
import com.fsll.app.investor.me.vo.AppInvestProductForBuyVO;
import com.fsll.app.investor.me.vo.AppInvestProductForCheckVO;
import com.fsll.app.investor.me.vo.AppInvestProductVO;
import com.fsll.app.investor.me.vo.AppOrderPlanCheckVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductDetailVO;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.app.watch.vo.AppWatchProductVo;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CorePieChartItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondMarketDay;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundPortfolioSc;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnEn;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.InsureInfo;
import com.fsll.wmes.entity.InsureInfoEn;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockMarketDay;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.trade.service.TradeStepService;
import com.mysql.jdbc.log.Log;

/**
 * 我要投资服务接口
 * @author zxtan
 * @date 2017-01-20
 */
@Service("appInvestService")
//@Transactional
public class AppInvestServiceImpl extends BaseService implements AppInvestService {

	@Autowired
	private CorePortfolioService corePortfolioService;
	@Autowired
	private TradeStepService tradeStepService;
	
	/**
	 * 得到我自选的产品列表
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @return List 列表数据
	 */
	public List<AppInvestProductVO> findWatchProductList(String memberId,String langCode){
		List<AppInvestProductVO>  voList = new ArrayList<AppInvestProductVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from WebWatchlist t ");
		hql.append(" inner join WebWatchlistType wt on wt.id=t.webWatchlistType.id ");
		hql.append(" inner join FundInfo f on f.product.id=t.product.id ");
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" where t.member.id=? and t.isValid=? order by wt.name,t.orderBy asc");
		List params = new ArrayList();
		params.add(memberId);
		params.add("1");
		List list= baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppInvestProductVO product = new AppInvestProductVO();
			WebWatchlist watchlist = (WebWatchlist) objs[0];
			WebWatchlistType watchlistType = watchlist.getWebWatchlistType();
			
			FundInfo info = (FundInfo) objs[2];
			FundInfoEn infoLang = new FundInfoEn();
			BeanUtils.copyProperties(objs[3], infoLang);
			
			String productId = info.getProduct().getId();
			product.setProductId(productId);
			product.setFundName(infoLang.getFundName());
			product.setFundType(infoLang.getFundType());
			product.setRiskLevel(String.valueOf(info.getRiskLevel()));
			product.setWatchlistTypeId(watchlistType.getId());
			product.setWatchlistTypeName(watchlistType.getName());
			
			voList.add(product);
		}
		return voList;
	}

	/**
	 * 组合分析——产品列表
	 */
	public AppInvestPortfolioVO findInvestPortfolio(String productIds,String productRates,String langCode, String toCurrency, String periodCode) {
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		AppInvestPortfolioVO vo = new AppInvestPortfolioVO();
		List<AppInvestProductVO> productList = new ArrayList<AppInvestProductVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" left join FundReturn r on r.fund.id=f.id and r.periodCode=? ");
		hql.append(" left join "+ getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" where FIND_IN_SET(f.product.id,?)>0 ");
		hql.append(" order by fl.fundName ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add(productIds);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			String[] arrProduct = productIds.split(",");
			String[] arrRate = productRates.split(",");
//			double avgRate = 1.0/arrProduct.length;
//			avgRate = new BigDecimal(avgRate).setScale(2, BigDecimal.ROUND_UP).doubleValue();
			
			double totalRate = 1.0;
			double perNumber = list.size();
			double portfolioRiskLevel = 0;
			
			for (int i = 0; i < list.size(); i++) {
				AppInvestProductVO product = new AppInvestProductVO();
				Object[] objs = (Object[]) list.get(i);
				FundInfo info = (FundInfo) objs[0];
				FundInfoEn infoLang = new FundInfoEn();
				BeanUtils.copyProperties(objs[1], infoLang);
				product.setFundId(info.getId());
				String productId = info.getProduct().getId();
				product.setProductId(productId);
				product.setFundName(infoLang.getFundName());
				product.setFundType(infoLang.getFundType());
				product.setRiskLevel(String.valueOf(info.getRiskLevel()));
				product.setIssueCurrency(infoLang.getIssueCurrency());
				product.setIssueCurrencyCode(infoLang.getIssueCurrencyCode());
				product.setFundCurrency(infoLang.getFundCurrency());
				product.setFundCurrencyCode(infoLang.getFundCurrencyCode());
				if(null == toCurrency || "".equals(toCurrency)){
					product.setToCurrency(infoLang.getFundCurrency());
					product.setLastNav(getFormatNum(info.getLastNav(),infoLang.getFundCurrencyCode()));
					product.setMinInitialAmount(getFormatNum(info.getMinInitialAmount(),infoLang.getFundCurrencyCode()));
				}else {
					product.setToCurrency(toCurrencyName);
					product.setLastNav(getFormatNumByCurrency(info.getLastNav(), infoLang.getFundCurrencyCode(), toCurrency));
					product.setMinInitialAmount(getFormatNumByCurrency(info.getMinInitialAmount(), infoLang.getFundCurrencyCode(), toCurrency));
				}
				product.setLastNavUpdate(DateUtil.dateToDateString(info.getLastNavUpdate(), "yyyy-MM-dd HH:mm:ss"));
				if(null != objs[2]){
					FundReturn ret = (FundReturn) objs[2];
					product.setIncrease( getFormatNumByPer(ret.getIncrease()));
				}
				if(null != objs[3]){
					FundReturnEn ret = new FundReturnEn();
					BeanUtils.copyProperties(objs[3], ret);
					product.setPeriodName(ret.getPeriodName());
				}
								
				if(null == productRates || "".equals(productRates)){
					//平均分配
					double avgRate = totalRate / perNumber;
					avgRate = new BigDecimal(avgRate).setScale(2, BigDecimal.ROUND_UP).doubleValue();
					product.setAllocationRate(String.valueOf(avgRate));
					portfolioRiskLevel += avgRate * info.getRiskLevel();
					totalRate = totalRate - avgRate;
					perNumber--;
				}else {
					//手工调整过
					for (int j = 0; j < arrProduct.length; j++) {
						if(arrProduct[j].equals(productId)){
							product.setAllocationRate(arrRate[j]);
							double avgRate = Double.parseDouble(arrRate[j]);
							portfolioRiskLevel += avgRate * info.getRiskLevel();
							break;
						}
					}
					
				}
								
				productList.add(product);
			}
			portfolioRiskLevel = Math.ceil(portfolioRiskLevel);
			vo.setRiskLevel(String.valueOf((int) portfolioRiskLevel));			
		}
		
		vo.setProductList(productList);
		List<CorePieChartItemVO> sectorTypeList = corePortfolioService.getFundCompositionData(productIds, productRates, langCode, "sector"); //findPortfolioFundAllocationList(productIds, productRates, langCode, "sector");
		List<CorePieChartItemVO> geoAllocationList = corePortfolioService.getFundCompositionData(productIds, productRates, langCode, "market");
		vo.setSectorTypeList(sectorTypeList);
		vo.setGeoAllocationList(geoAllocationList);
		//return voList;
		return vo;
	}
	
	
/*	*//**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-28
	 * @param productIds 产品Ids
	 * @param productRates 产品占比
	 * @param langCode 语言
	 * @param groupType 统计类型
	 * @return
	 *//*
	private List<AppPieChartItemVO> findPortfolioFundAllocationList(String productIds,String productRates,String langCode,String groupType){
		List<AppPieChartItemVO>  voList = new ArrayList<AppPieChartItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f ");
		//hql.append(" inner JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");	
		hql.append(" inner JOIN FundPortfolio p ON p.fund.id = f.id ");	
		hql.append(" inner JOIN "+this.getLangString("FundPortfolio", langCode)+" l ON l.id = p.id ");	
		hql.append(" where FIND_IN_SET(f.product.id,?)>0 and p.type=? ");
		
		List params = new ArrayList();
		params.add(productIds);
		params.add(groupType);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){			
			Map<String, Double> map = new HashMap<String, Double>();
			String[] arrProduct = productIds.split(",");
			String[] arrRate = productRates.split(",");
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);				
				FundInfo fundInfo = (FundInfo) objs[0];
				FundPortfolio fundPortfolio = (FundPortfolio) objs[1];
				FundPortfolioSc fundPortfolioSc = new FundPortfolioSc();
				BeanUtils.copyProperties(objs[2], fundPortfolioSc);
				String productId = fundInfo.getProduct().getId();
				
				String keyString = fundPortfolioSc.getName();
				double rate = (fundPortfolio.getRate()==null)?0:fundPortfolio.getRate();
								
				for (int j = 0; j < arrProduct.length; j++) {
					if(arrProduct[j].equals(productId)){
						double weight = Double.parseDouble(arrRate[j]);
						if(map.containsKey(keyString)){
							map.put(keyString, map.get(keyString) + weight*rate/100.0);	
						}else {
							map.put(keyString, weight*rate/100.0);
						}
						break;
					}
				}
	

			}
			
			for (Map.Entry<String, Double> entry : map.entrySet()) {
				AppPieChartItemVO vo = new AppPieChartItemVO();
				vo.setItemId(null);
				vo.setItemName(entry.getKey());
				double weight = entry.getValue();
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setItemWeight(String.valueOf(weight));
				voList.add(vo);
			}
		}
		return voList;
	}*/
	
	
	/**
	 * 组合分析——回报图表
	 */
	public List<AppInvestPortfolioReturnVO> findProductReturn(String productIds,String productRates, String periodCode) {
		// TODO Auto-generated method stub
		List<AppInvestPortfolioReturnVO> voList = new ArrayList<AppInvestPortfolioReturnVO>();
		List params = new ArrayList();
		
		String startDate = getStartDate(periodCode);
		StringBuilder hql = new StringBuilder();
		hql.append(" select distinct m.marketDate from FundMarket m ");
		hql.append(" inner join FundInfo f on f.id = m.fund.id ");
		hql.append(" where FIND_IN_SET(f.product.id,?)>0 ");
		params.add(productIds);
		if(null != startDate && !"".equals(startDate)){
			hql.append(" and m.marketDate>= ? ");
			params.add(DateUtil.getDate(startDate));
		}
		hql.append(" order by m.marketDate ");
		
		
		
		List listDate = baseDao.find(hql.toString(), params.toArray(), false);
		
		hql = new StringBuilder();
		hql.append(" from FundMarket m");
		hql.append(" inner join FundInfo f on f.id = m.fund.id ");
		hql.append(" where FIND_IN_SET(f.product.id,?)>0 ");
		params.clear();
		params.add(productIds);
		if(null != startDate && !"".equals(startDate)){
			hql.append(" and m.marketDate>= ? ");
			params.add(DateUtil.getDate(startDate));
		}
		hql.append(" order by m.marketDate ");
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != listDate && !listDate.isEmpty()){
			String[] arrProduct = productIds.split(",");
			String[] arrRate = productRates.split(",");		
//			double firstRate = 0.0;
			double firstNav = 0.0;
			Date firstDate = DateUtil.getDate(listDate.get(0).toString(),"yyyy-MM-dd");
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				FundMarket market = (FundMarket) objs[0];
				Date marketDate =  market.getMarketDate();
				double accNav = market.getNav()==null?0:market.getNav();
				
				int days;
				try {
					days = DateUtil.daysBetween(firstDate, marketDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					days = 0;
				}
				
				if( days == 0 ){
					FundInfo fundInfo = (FundInfo) objs[1];
					String productId = fundInfo.getProduct().getId();
					for (int j = 0; j < arrProduct.length; j++) {
						if(productId.equals(arrProduct[j])){
							double rate = Double.parseDouble(arrRate[j]);
//							firstRate += rate * market.getReturnRate();
							firstNav += rate * accNav;
							break;
						}
					}
				}else {
					break;
				}
			}
			
			//循环每一交易日		
			for (int i = 0; i < listDate.size(); i++) {
				Date rDate = DateUtil.getDate(listDate.get(i).toString(),"yyyy-MM-dd");
				double accNav = 0.0;
				double returnRate = 0.0;
				//查找对应交易日数据
				for (int j = 0; j < list.size(); j++) {
					Object[] objs = (Object[]) list.get(j);
					FundMarket market = (FundMarket) objs[0];
					Date marketDate =  market.getMarketDate();
					
					int days;
					try {
						days = DateUtil.daysBetween(rDate, marketDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						days = 0;
					}
					if(days == 0 ){
						FundInfo fundInfo = (FundInfo) objs[1];
						String productId = fundInfo.getProduct().getId();
						for (int k = 0; k < arrProduct.length; k++) {
							if(productId.equals(arrProduct[k])){
								double rate = Double.parseDouble(arrRate[k]);
								double nav = market.getNav()==null?0:market.getNav();
								accNav += rate * nav;
//								returnRate += rate * market.getReturnRate();
								break;
							}
						}
						
					}					
				}
				//相对第一天的比例
				if(firstNav !=0){
					returnRate = (accNav - firstNav)/firstNav;
				}
				
				returnRate = new BigDecimal(returnRate).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				AppInvestPortfolioReturnVO returnVO = new AppInvestPortfolioReturnVO();
				returnVO.setMarketDate(DateUtil.dateToDateString(rDate, "yyyy-MM-dd"));
				returnVO.setMarketReturn(String.valueOf(returnRate));
				voList.add(returnVO);
			}
		}
		return voList;
	}
	
	/**
	 * 组合购买步骤展示列表
	 */
	public AppInvestPortfolioForBuyVO findInvestPortfolioForBuy(String memberId,String holdId,String productIds,String productRates,String langCode, String toCurrency) {
		AppInvestPortfolioForBuyVO vo = new AppInvestPortfolioForBuyVO();
		List<AppInvestProductForBuyVO> productList = new ArrayList<AppInvestProductForBuyVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" where FIND_IN_SET(f.product.id,?)>0 ");
		hql.append(" order by fl.fundName ");
		List params = new ArrayList();
		params.add(productIds);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			String[] arrProduct = productIds.split(",");
			String[] arrRate = productRates.split(",");
			
			double portfolioRiskLevel = 0;
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
			for (int i = 0; i < list.size(); i++) {
				AppInvestProductForBuyVO product = new AppInvestProductForBuyVO();
				Object[] objs = (Object[]) list.get(i);
				FundInfo info = (FundInfo) objs[0];
				FundInfoEn infoLang = new FundInfoEn();
				BeanUtils.copyProperties(objs[1], infoLang);
				
				product.setFundId(info.getId());
				String productId = info.getProduct().getId();
				product.setProductId(productId);
				product.setFundName(infoLang.getFundName());
				product.setFundCurrency(infoLang.getIssueCurrencyCode());
				
				product.setRiskLevel(String.valueOf(info.getRiskLevel()));
				product.setFundType(infoLang.getFundType());
				if(null == toCurrency || "".equals(toCurrency)){
					product.setToCurrency(infoLang.getFundCurrency());
					product.setMinInitialAmount(getFormatNum(info.getMinInitialAmount(),infoLang.getFundCurrencyCode()));
					product.setLastNav(getFormatNum(info.getLastNav(),infoLang.getFundCurrencyCode()));
				}else {
					product.setToCurrency(toCurrencyName);
					Double rate = getExchangeRate(infoLang.getFundCurrencyCode(), toCurrency);
					if(null != rate){
						product.setMinInitialAmount(getFormatNumByRate(info.getMinInitialAmount(), rate,toCurrency));
						product.setLastNav(getFormatNumByRate(info.getLastNav(), rate,toCurrency));
					}
				}
				
				List<AppInvestAccountVO> accountList = findInvestAccountList(memberId,holdId,productId,info.getRiskLevel(),toCurrency,langCode);
				product.setAccountList(accountList);

				for (int j = 0; j < arrProduct.length; j++) {
					if(arrProduct[j].equals(productId)){
						product.setAllocationRate(arrRate[j]);
						double avgRate = Double.parseDouble(arrRate[j]);
						portfolioRiskLevel += avgRate * info.getRiskLevel();
						break;
					}
				}				
								
				productList.add(product);
			}
			portfolioRiskLevel = Math.ceil(portfolioRiskLevel);
			vo.setRiskLevel(String.valueOf((int) portfolioRiskLevel));
		}
		
		vo.setProductList(productList);
		//return voList;
		return vo;
	}
	
	/**
	 * 获取产品对应可用的账号列表
	 * @param memberId
	 * @param productId
	 * @param productRiskLevel
	 * @param toCurrency
	 * @return
	 */
	private List<AppInvestAccountVO> findInvestAccountList(String memberId,String holdId,String productId,int productRiskLevel,String toCurrency,String langCode) {
		List<AppInvestAccountVO> voList = new ArrayList<AppInvestAccountVO>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldAccount ha ");
		hql.append(" inner join InvestorAccount ia on ia.id=ha.account.id ");
		hql.append(" inner join ProductDistributor d on d.product.id=? and d.distributor.id=ia.distributor.id ");
//		hql.append(" inner join RpqExam r on r.relateId=ia.id ");
		hql.append(" left join PortfolioHold h on h.id=ha.portfolioHold.id ");
		hql.append(" inner join MemberIfa i on i.id=ha.ifa.id ");
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		hql.append(" where ha.member.id=? ");
		if(null == holdId || "".equals(holdId)){
			hql.append(" and ( IFNULL(h.id,'') = '' or ( IFNULL(h.id,'') != '' and not EXISTS ( select hp.id from PortfolioHoldProduct hp where hp.account.id=ia.id ) ) ) ");
		}
		else {
			hql.append(" and ( exists ( select s.id from PortfolioHold s inner join PortfolioHoldProduct p on p.portfolioHold.id=s.id where p.account.id=ia.id and s.id=? )");
			hql.append("    or not exists ( select hp.id from PortfolioHoldProduct hp where hp.account.id=ia.id ) ) ");
			params.add(holdId);
		}
		hql.append(" order by ha.accountNo ");

		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){	
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
			for (int i = 0; i < list.size(); i++) {
				AppInvestAccountVO vo = new AppInvestAccountVO();
				Object[] objs = (Object[]) list.get(i);
				PortfolioHoldAccount holdAccount = (PortfolioHoldAccount) objs[0];
				InvestorAccount investorAccount = (InvestorAccount) objs[1];
				ProductDistributor productDistributor = (ProductDistributor) objs[2];
				MemberIfa ifa = holdAccount.getIfa();
				
				vo.setProductId(productId);
				vo.setAccountId(investorAccount.getId());
				vo.setAccountNo(holdAccount.getAccountNo());
				vo.setTotalFlag(investorAccount.getTotalFlag());
				vo.setIfaId(ifa.getId());
				vo.setIfaMemberId(ifa.getMember().getId());
				
				String ifaName = getCommonMemberName(ifa.getMember().getId(),langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setIfaName(ifaName);
				
				if(null == toCurrency || "".equals(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, holdAccount.getBaseCurrency(), "currency_type"));
					vo.setCashAvailable(getFormatNum(holdAccount.getCashAvailable(),holdAccount.getBaseCurrency()));
					vo.setCashHold(getFormatNum(holdAccount.getCashHold(),holdAccount.getBaseCurrency()));
					vo.setCashWithdrawal(getFormatNum(holdAccount.getCashWithdrawal(),holdAccount.getBaseCurrency()));
					vo.setTransactionFeeRate(String.valueOf(productDistributor.getTransactionFeeRate()));
					vo.setTransactionFeeCur(getParamConfigName(langCode, productDistributor.getTransactionFeeCur(), "currency_type"));
					vo.setTransactionFeeMini(String.valueOf(productDistributor.getTransactionFeeMini()));
				}else {
					String fromCurrency = holdAccount.getBaseCurrency();
					Double rate = getExchangeRate(fromCurrency, toCurrency);
					vo.setBaseCurrency(toCurrencyName);
					if(null != rate){
						vo.setCashAvailable(getFormatNumByRate(holdAccount.getCashAvailable(), rate,toCurrency));
						vo.setCashHold(getFormatNumByRate(holdAccount.getCashHold(), rate,toCurrency));
						vo.setCashWithdrawal(getFormatNumByRate(holdAccount.getCashWithdrawal(), rate,toCurrency));
					}
										
					vo.setTransactionFeeRate(String.valueOf(productDistributor.getTransactionFeeRate()));
					vo.setTransactionFeeCur(toCurrencyName);
					vo.setTransactionFeeMini(getFormatNumByCurrency(productDistributor.getTransactionFeeMini(), productDistributor.getTransactionFeeCur(), toCurrency));
				}
								
//				String distributorId = investorAccount.getDistributor().getId();
				int rpqRiskLevel = investorAccount.getRpqLevel()==null?0:investorAccount.getRpqLevel(); //findAccountRpqRiskLevel(memberId, distributorId);
				vo.setRiskWarning( rpqRiskLevel>=productRiskLevel?"0":"1" );
				
				voList.add(vo);
			}
		}
		
		return voList;		
	}
	
	
	
/*	*//**
	 * 获取账号RPQ风险等级
	 * @param memberId
	 * @param distributorId
	 * @return
	 *//*
	private int findAccountRpqRiskLevel(String memberId,String distributorId) {
		int rpqRiskLevel = 0;
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from RpqExam r ");
		hql.append(" inner join InvestorAccount ia on ia.id=r.relateId ");
		hql.append(" where ia.member.id=? and ia.distributor.id=? and ia.subFlag='0' ");
		
		List params = new ArrayList();
		params.add(memberId);
		params.add(distributorId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[]) list.get(0);
			RpqExam rpq = (RpqExam) objs[0];
			rpqRiskLevel = rpq.getRiskLevel()==null?0:rpq.getRiskLevel();
		}
		
		return rpqRiskLevel;		
	}*/
	

	/**
	 * 组合购买保存草稿
	 */
	public OrderPlan saveInvestPortfolioOrderDraft(String memberId,JSONObject holdObject,JSONObject planObject,JSONArray productArray,String ifaCheck,String ifaMemberId,String langCode){
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
		String planId = planObject.optString("planId", "");
		OrderPlan orderPlan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		
		String holdId = orderPlan == null ? null: orderPlan.getPortfolioHold().getId();
		
		PortfolioHold portfolioHold = savePortfolioHoldDraft(member, holdId, holdObject);
		OrderPlan plan = saveOrderPlanDraft(member, portfolioHold, planObject,ifaCheck);

		boolean flag = saveOrderPlanProductDraft(plan, productArray);
		if("1".equals(ifaCheck)){
//			addOrderPlanCheck(plan, ifaMemberId);
			Set<String> ifaIds = tradeStepService.getOrderIfas(planId); 
			tradeStepService.saveSendToReadForOrderPlan(member, planId, null, langCode, ifaIds);
		}
		return flag==true?plan:null;
	}
	
	/**
	 * 保存组合草稿
	 * @param member
	 * @param holdObject
	 * @return
	 */
	private PortfolioHold savePortfolioHoldDraft(MemberBase member,String holdId,JSONObject holdObject){
		
		String portfolioName = holdObject.optString("portfolioName", "");
		String baseCurrency = holdObject.optString("baseCurrency", "");
		String ifFirst = "1";
		
		PortfolioHold hold = new PortfolioHold();
		if(null != holdId && !"".equals(holdId)){
			hold = (PortfolioHold) baseDao.get(PortfolioHold.class, holdId);
		}else {
			hold.setId(null);
		}		
		hold.setPortfolioName(portfolioName);
		hold.setMember(member);
		hold.setCreateTime(new Date());
		hold.setLastUpdate(new Date());
		hold.setBaseCurrency(baseCurrency);
		hold.setIfFirst(ifFirst);
		hold = (PortfolioHold) baseDao.saveOrUpdate(hold);
		return hold;
	}
	
	/**
	 * 保存组合购买计划的草稿
	 * @param member
	 * @param portfolioHold
	 * @param planObject
	 * @return
	 */
	private OrderPlan saveOrderPlanDraft(MemberBase member,PortfolioHold portfolioHold,JSONObject planObject,String ifaCheck){
		String planId = planObject.optString("planId", "");	
		
		String aipFlag = planObject.optString("aipFlag", "0");
		String baseCurrency = planObject.optString("baseCurrency", portfolioHold.getBaseCurrency());
		double totalBuy = planObject.optDouble("totalBuy", 0.0);
		
		OrderPlan plan = new OrderPlan();
		if(null != planId && !"".equals(planId)){
			plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		}else {
			plan.setId(null);
		}
		plan.setPortfolioHold(portfolioHold);
		plan.setAipFlag(aipFlag);
		if("1".equals(ifaCheck)){
			plan.setFinishStatus("1");
			plan.setCheckStatus("0");
		}else {
			plan.setFinishStatus("0");
		}
		
		plan.setCreator(member);
		plan.setCreateTime(new Date());
		plan.setLastUpdate(new Date());
		plan.setAuthorized("1");
		plan.setTotalBuy(totalBuy);
		plan.setBaseCurrency(baseCurrency);
		plan.setIsValid("1");
		plan = (OrderPlan) baseDao.saveOrUpdate(plan);
		return plan;
	}
	
	/**
	 * 保存组合购买计划的草稿
	 * @param member
	 * @param portfolioHold
	 * @param planObject
	 * @return
	 */
/*	private OrderPlanCheck addOrderPlanCheck(OrderPlan plan, String ifaMemberId){
		MemberBase ifaMember = (MemberBase) baseDao.get(MemberBase.class, ifaMemberId);		
		OrderPlanCheck planCheck = new OrderPlanCheck();
		
		planCheck.setId(null);
		planCheck.setPlan(plan);
		planCheck.setCheck(ifaMember);
		planCheck.setCheckStatus("0");
		
		planCheck = (OrderPlanCheck) baseDao.saveOrUpdate(planCheck);
		return planCheck;
	}*/
	
	/**
	 * 保存计划的审批记录
	 * @param id
	 * @param planId
	 * @param memberId
	 * @param checkStatus
	 * @param checkResult
	 * @param checkIp
	 * @return
	 */
	public boolean updateOrderPlanCheck(String id,String planId, String memberId,String checkStatus,String checkResult,String checkIp){
		boolean flag = false;
		OrderPlanCheck wf = (OrderPlanCheck) baseDao.get(OrderPlanCheck.class, id);
		OrderPlan plan = wf.getPlan();
		if(null != wf && null != plan && planId.equals(plan.getId()) 
				&& null != wf.getCheck() && memberId.equals(wf.getCheck().getId()) ){
			wf.setCheckTime(new Date());
			wf.setCheckStatus(checkStatus);
			wf.setCheckResult(checkResult);
			wf.setCheckIp(checkIp);
			baseDao.update(wf);
			
			String planCheckStatus = plan.getCheckStatus();
			if("2".equals(checkStatus)){
				String hql = "update OrderPlanCheck t set t.checkStatus='3',t.checkTime=now() where t.checkStatus='0' and t.plan.id=? and t.id !=? ";
				baseDao.updateHql(hql, new Object[]{planId,id});
				
				planCheckStatus = planCheckStatus.replace("1", "0");
				plan.setCheckStatus(planCheckStatus);
				plan.setFinishStatus("2");				
				baseDao.update(plan);				
			}else if("1".equals(checkStatus)){
				//int index = planCheckStatus.indexOf("0");
				planCheckStatus = planCheckStatus.replaceFirst("0", "1");
				plan.setCheckStatus(planCheckStatus);
				if(planCheckStatus.indexOf("0")== -1){
					plan.setFinishStatus("3");
				}
				baseDao.update(plan);
			}
			flag = true;
		}
		
		return flag;
	}
	
	
	/**
	 * 删除组合购买计划的草稿
	 * @param planId
	 * @param memberId
	 * @return
	 */
	public boolean deleteOrderPlanDraft(String planId, String memberId){
		boolean flag = false;
		OrderPlan plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		
		if(null != plan ){
			PortfolioHold hold = plan.getPortfolioHold();
			if(memberId.equals(plan.getCreator().getId()) && 
					( "-1".equals(plan.getFinishStatus()) || "0".equals(plan.getFinishStatus()) ) ){
				baseDao.delete(plan);
				flag = true;
			}else {
				flag = false;
			}
			
			if("1".equals(hold.getIfFirst())){
				baseDao.delete(hold);
			}			
		}
		
		return flag;
	}
	
	
	/**
	 * 保存购买产品
	 * @param plan
	 * @param productArray
	 * @return
	 */
	private boolean saveOrderPlanProductDraft(OrderPlan plan,JSONArray productArray){
		try {
			String hql = "delete OrderPlanProduct p where p.plan.id=? ";
			List<Object> params = new ArrayList<Object>();
			params.add(plan.getId());
			baseDao.updateHql(hql, params.toArray());
			
			for (int i = 0; i < productArray.size(); i++) {
				JSONObject productObject = (JSONObject) productArray.get(i);
				String productId = productObject.optString("productId", "");
				ProductInfo productInfo = (ProductInfo) baseDao.get(ProductInfo.class, productId);
				double amount = productObject.optDouble("amount", 0.0);
				double weight = productObject.optDouble("weight", 0.0);
				String tranType = "B";
				String dividend = productObject.optString("dividend", "R");
				String accountId = productObject.optString("accountId", "");
				String accountNo = productObject.optString("accountNo", "");
				InvestorAccount account = (InvestorAccount) baseDao.get(InvestorAccount.class, accountId);
				ProductDistributor pDistributor = findProductDistributor(productId, account.getDistributor().getId());
				
				OrderPlanProduct product = new OrderPlanProduct();
				product.setId(null);
				product.setPlan(plan);
				product.setProduct(productInfo);
				product.setWeight(weight);
				product.setWeightAdjust(weight);
				product.setTranType(tranType);
				product.setDividend(dividend);
				product.setAccount(account);
				product.setAccountNo(accountNo);
				product.setOriginal(0);
				if(null != pDistributor){
					String fromCurrency = plan.getBaseCurrency();
					String toCurrency = pDistributor.getTransactionFeeCur();
					toCurrency = (toCurrency == null || "".equals(toCurrency))? fromCurrency : toCurrency;
					product.setTranFeeCur(toCurrency);
					product.setTranFeeMini(pDistributor.getTransactionFeeMini());
					product.setTranRate(pDistributor.getTransactionFeeRate());
					amount = getNumByCurrency(amount, fromCurrency, toCurrency);	
					double fee = amount*pDistributor.getTransactionFeeRate()/100.0;
					product.setAmount(amount);
					product.setAmountAdjust(amount);
					product.setTranFee(fee);
				}
								
				baseDao.create(product);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取产品代理商列表
	 * @param productId
	 * @param distributorId
	 * @return
	 */
	private ProductDistributor findProductDistributor(String productId,String distributorId) {
				
		StringBuilder hql = new StringBuilder();
		hql.append(" from ProductDistributor d ");
		hql.append(" where d.isPublish='1' and d.product.id=? and d.distributor.id=? ");
		List params = new ArrayList();
		params.add(productId);
		params.add(distributorId);
		List<ProductDistributor> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){						
			return list.get(0);
		}
		
		return null;		
	}
	
	
	/**
	 * 获取交易主表
	 * @author zxtan
	 * @data 2017-02-13
	 * @param id
	 * @return
	 */
	public OrderPlan findOrderPlanById(String id) {
		OrderPlan vo = (OrderPlan)this.baseDao.get(OrderPlan.class, id);
		return vo;
	}	
	
	/**
	 * 获取交易产品
	 * @author zxtan
	 * @data 2017-02-13
	 */
	public List<OrderPlanProduct> findOrderPlanProduct(String planId, String accountNo) {
		List<OrderPlanProduct> orderPlanProductList = null;
		if(StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(accountNo)){
			StringBuilder hql = new StringBuilder();
			hql.append(" From OrderPlanProduct p ");
			hql.append(" WHERE (p.account.accountNo=? OR p.accountNo=?) ");
			hql.append(" AND p.plan.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(accountNo);
			params.add(accountNo);
			params.add(planId);
			orderPlanProductList = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return orderPlanProductList;
	}
	
	/**
	 * 获取 IFA AE账号
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public String findAECode(String ifaId, String distributorId) {
		String aeCode = null;
		if(StringUtils.isNotBlank(ifaId) && StringUtils.isNotBlank(distributorId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" IfaDistributor i" +
					" WHERE" +
					" i.ifa.id=?" +
					" AND" +
					" i.distributor.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(ifaId);
			params.add(distributorId);
			List<IfaDistributor> distributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(distributors!=null && !distributors.isEmpty()){
				IfaDistributor distributor = distributors.get(0);
				aeCode = distributor.getAeCode();
			}
		}
		return aeCode;
	}
	
	/**
	 * 根据产品ID获取基金信息
	 * @author zxtan
	 * @date 2017-02-14
	 * @param productId 产品ID
	 * @return
	 */
	public FundInfo findFundInfoByProduct(String productId){
		FundInfo fund = null;
		if (StringUtils.isNotBlank(productId)) {
			StringBuffer hql = new StringBuffer(" from FundInfo where isValid=1 and product_id=?");
			List params = new ArrayList();
			params.add(productId);
			List<FundInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				fund = list.get(0);
			}
		}
		return fund;
	}
	
	/**
	 * 代理商产品库中产品的编码
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public String findSymbolCode(String productId, String distributorId) {
		String symbolCode = null;
		if(StringUtils.isNotBlank(productId) && StringUtils.isNotBlank(distributorId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" ProductDistributor i" +
					" WHERE" +
					" i.product.id=?" +
					" AND" +
					" i.distributor.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(productId);
			params.add(distributorId);
			List<ProductDistributor> productDistributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(productDistributors!=null && !productDistributors.isEmpty()){
				ProductDistributor productDistributor = productDistributors.get(0);
				symbolCode = productDistributor.getSymbolCode();
			}
		}
		return symbolCode;
	}
	
	/**
	 * 保存交易历史
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public OrderHistory saveOrderHistory(OrderHistory history) {
		return (OrderHistory) this.baseDao.saveOrUpdate(history);
	}
	
	/**
	 * 更新组合持仓帐户绑定表
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public void updatePortfolioHoldAccount(String holdId,String memberId,String accountId,String accountNo) {
		if(StringUtils.isNotBlank(holdId) && StringUtils.isNotBlank(memberId)){
			StringBuffer hql = new StringBuffer("" +
					" UPDATE" +
					" PortfolioHoldAccount a" +
					" SET" +
					" a.portfolioHold.id=?" +
					" WHERE" +
					" a.member.id=?" +
					" ");
			List<Object> params = new ArrayList<Object>();
			if (StringUtils.isNotBlank(accountId)) {
				hql.append(" AND a.account.id=?");
				params.add(accountId);
				this.baseDao.updateHql(hql.toString(), params.toArray());
			}else if (StringUtils.isNotBlank(accountNo)) {
				hql.append(" AND a.accountNo=?");
				params.add(accountNo);
				this.baseDao.updateHql(hql.toString(), params.toArray());
			}
		}
	}
	
	/**
	 * 更新组合交易计划
	 * @author zxtan
	 * @date 2017-02-16
	 */
	public void updateOrderPlan(String planId) {
		if(StringUtils.isNotBlank(planId) ){
			String hql = " UPDATE OrderPlan a SET a.finishStatus='4',a.submitDate=now() WHERE a.id=? ";
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			this.baseDao.updateHql(hql, params.toArray());			
		}
	}
	
	
	/**
	 * 组合调整保存草稿
	 */
	public OrderPlan saveInvestPortfolioOrderDraftForRebalance(String memberId,String portfolioHoldId,String orderPlanId,JSONArray productArray,String ifaCheck,String ifaMemberId,String langCode){
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
		PortfolioHold hold = (PortfolioHold) baseDao.get(PortfolioHold.class, portfolioHoldId);
		
		OrderPlan orderPlan = saveOrderPlanDraftForRebalance(member,hold,orderPlanId,ifaCheck);
		if("1".equals(ifaCheck)){
//			addOrderPlanCheck(orderPlan, ifaMemberId);
			String planId = orderPlan.getId();
			Set<String> ifaIds = tradeStepService.getOrderIfas(planId);
			
			tradeStepService.saveSendToReadForOrderPlan(member, planId, null, langCode, ifaIds);
		}	
		boolean flag = saveOrderPlanProductDraftForRebalance(orderPlan, productArray);
		return flag==true?orderPlan:null;
	}
	
	
	/**
	 * 保存组合购买计划的草稿
	 * @param member
	 * @param portfolioHold
	 * @param planObject
	 * @return
	 */
	private OrderPlan saveOrderPlanDraftForRebalance(MemberBase member,PortfolioHold portfolioHold,String orderPlanId,String ifaCheck){
		OrderPlan plan = new OrderPlan();
		if(null != orderPlanId && !"".equals(orderPlanId)){
			plan = (OrderPlan) baseDao.get(OrderPlan.class, orderPlanId);
		}else {
			plan.setId(null);
			plan.setPortfolioHold(portfolioHold);
			plan.setAipFlag("0");
			if("1".equals(ifaCheck)){
				plan.setFinishStatus("1");
				plan.setCheckStatus("0");
			}else {
				plan.setFinishStatus("0");
			}
//			plan.setFinishStatus("0");
			plan.setCreator(member);
			plan.setCreateTime(new Date());
			plan.setLastUpdate(new Date());
			plan.setAuthorized(null);
			plan.setTotalBuy(null);
			plan.setTotalSell(null);
			plan.setBaseCurrency(portfolioHold.getBaseCurrency());
			plan = (OrderPlan) baseDao.saveOrUpdate(plan);
			
		}

		return plan;
	}
	
	

	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2017-02-24
	 * @param productId
	 * @param unit
	 * @param toCurrency
	 * @return
	 */
	private Double getHoldProductMarketValue(String productId,double unit,String toCurrency){
		ProductInfo productInfo = (ProductInfo) baseDao.get(ProductInfo.class, productId);
		List<Object> params = new ArrayList<Object>();
		double lastPrice = 0.0;
		Double marketValue= 0.0;
		String baseCurrency= toCurrency;
		
		StringBuilder hql = new StringBuilder();
		if("fund".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from FundInfo m inner join FundInfoEn f on f.id=m.id where m.product.id=? ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				FundInfo info = (FundInfo) objs[0];
				FundInfoEn infoEn = (FundInfoEn) objs[1];
				lastPrice = info.getLastNav();
				baseCurrency = infoEn.getFundCurrencyCode();
			}
			
		}else if("insure".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from InsureInfo m inner join InsureInfoEn f on f.id=m.id where m.product.id=? ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				InsureInfo info = (InsureInfo) objs[0];
				InsureInfoEn infoEn = (InsureInfoEn) objs[1];
				lastPrice = info.getLastNav();
				baseCurrency = infoEn.getInsureCurrencyCode();
			}
			
		}else if("bond".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from BondInfo m inner join BondInfoEn f on f.id=m.id ");
			hql.append(" inner join BondMarketDay c on c.stock.id=m.id ");
			hql.append(" where m.product.id=? order by c.priceDate desc ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				BondInfoEn infoEn = (BondInfoEn) objs[1];
				BondMarketDay market = (BondMarketDay) objs[2];
				lastPrice = market.getTradePrice();
				baseCurrency = infoEn.getBondCurrencyCode();
			}
		}else if("stock".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from StockInfo m inner join StockInfoEn f on f.id=m.id ");
			hql.append(" inner join StockMarketDay c on c.stock.id=m.id ");
			hql.append(" where m.product.id=? order by c.priceDate desc ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				StockInfoEn infoEn = (StockInfoEn) objs[1];
				StockMarketDay market = (StockMarketDay) objs[2];
				lastPrice = market.getTradePrice();
				baseCurrency = infoEn.getStockCurrencyCode();
			}
		}
		
		marketValue = getNumByCurrency(lastPrice * unit, baseCurrency, toCurrency);
		
		return marketValue;
	}
	
	
	
	/**
	 * 保存购买产品
	 * @param plan
	 * @param productArray
	 * @return
	 */
	private boolean saveOrderPlanProductDraftForRebalance(OrderPlan plan,JSONArray productArray){
		try {
			StringBuilder hql = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			//清除原有的数据
			hql.append("delete OrderPlanProduct p where p.plan.id=? ");			
			params.add(plan.getId());
			baseDao.updateHql(hql.toString(), params.toArray());
									
			//统计现有基金市值
			hql = new StringBuilder();
			hql.append(" from PortfolioHoldProduct m ");
			hql.append(" inner join FundInfo f on f.product.id=m.product.id ");
			hql.append(" inner join FundInfoEn fl on fl.id=f.id ");
			hql.append(" where m.portfolioHold.id=? ");
			
			params = new ArrayList<Object>();
			params.add(plan.getPortfolioHold().getId());
			
			List list = baseDao.find(hql.toString(), params.toArray(), false);

			double totalMarketValue = 0;
			if(null != list && !list.isEmpty()){
				String holdCurrency = plan.getBaseCurrency();
								
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];
					FundInfo fundInfo = (FundInfo) objs[1];
					FundInfoEn fundInfoEn = new FundInfoEn();
					BeanUtils.copyProperties(objs[2], fundInfoEn);
					
					double holdUnit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
					double lastNav = fundInfo.getLastNav()==null? 0:fundInfo.getLastNav();
					String fundCurrency = StringUtils.isBlank(fundInfoEn.getFundCurrencyCode())?CommonConstants.DEF_CURRENCY:fundInfoEn.getFundCurrencyCode();
					
					totalMarketValue = totalMarketValue + getNumByCurrency(lastNav*holdUnit, fundCurrency, holdCurrency);
				}
			}			

			double totalMarketValueAdjust = totalMarketValue;			
			double totalBuy = 0;
			double totalSell = 0;
			
			for (int i = 0; i < productArray.size(); i++) {				
				JSONObject productObject = (JSONObject) productArray.get(i);
				double amount = productObject.optDouble("amount", 0.0);
				double lastNav = productObject.optDouble("lastNav",0.0);
				double unit = productObject.optDouble("unit",0.0);
				String tranType = productObject.optString("tranType", "B");
				
				if("B".equalsIgnoreCase(tranType)){
					totalMarketValueAdjust = totalMarketValueAdjust + amount;
					totalBuy = totalBuy + amount;
				}else if("S".equalsIgnoreCase(tranType)) {
					amount = lastNav * unit;
					totalMarketValueAdjust = totalMarketValueAdjust - amount;
					totalSell = totalSell + amount;
				}
			}
			
			
			Map<String, String> switchMap = new HashMap<String, String>();
			
			for (int i = 0; i < productArray.size(); i++) {
				JSONObject productObject = (JSONObject) productArray.get(i);
				String productId = productObject.optString("productId", "");
				String fromProductId = productObject.optString("fromProductId", "");
				ProductInfo productInfo = (ProductInfo) baseDao.get(ProductInfo.class, productId);
				
				ProductInfo fromProductInfo = null;
				if(!"".equals(fromProductId)){
					fromProductInfo = (ProductInfo) baseDao.get(ProductInfo.class, fromProductId);
				}

				String holdId = plan.getPortfolioHold().getId();
				PortfolioHoldProduct holdProduct = findPortfolioHoldProduct(holdId, productId);
				
				
				double unit = productObject.optDouble("unit", 0.0);					
				double amount = productObject.optDouble("amount", 0.0);
				double lastNav = productObject.optDouble("lastNav", 0.0);
				double weight = 0.0;
				double unitAdjust = 0.0;
				double amountAdjust = 0.0;
				double weightAdjust = 0.0;

				String tranType = productObject.optString("tranType", "B");
				
				if(null != holdProduct){
					double oriProductMarketValue = holdProduct.getHoldingUnit()*lastNav;// getHoldProductMarketValue(productId, holdProduct.getHoldingUnit(),plan.getBaseCurrency());
					weight = oriProductMarketValue / totalMarketValue;
					//原有产品
					if("B".equalsIgnoreCase(tranType)){
						amountAdjust = oriProductMarketValue + amount;
						weightAdjust = amountAdjust / totalMarketValueAdjust;
					}else if("S".equalsIgnoreCase(tranType)) {
						amount = lastNav * unit;
						unitAdjust = holdProduct.getHoldingUnit() - unit;
						amountAdjust = lastNav * unitAdjust;// oriProductMarketValue - amount;
						weightAdjust = amountAdjust / totalMarketValueAdjust;
					}
					
				}else {
					//新增产品
					amountAdjust = amount;
					weightAdjust = amountAdjust / totalMarketValueAdjust;
					weight = 0.0;
				}
				
				String dividend = productObject.optString("dividend", "R");
				Integer original = 0;
				String switchFlag = productObject.optString("switchFlag", "0");
				String switchGroup = null;
				InvestorAccount account = null;
				ProductDistributor pDistributor = null;
				String accountNo = "";
				PortfolioHoldProduct fromProduct = null;
				if("1".equals(switchFlag)){
					if("S".equalsIgnoreCase(tranType)) {
						switchGroup = StrUtils.getRandomNum();
						switchMap.put(productId, switchGroup);
						boolean hasGroup = false;
						hasGroup = validSwitchGroup(switchGroup);
						while (hasGroup){
							switchGroup = StrUtils.getRandomNum();
							switchMap.put(fromProductId, switchGroup);
							hasGroup = validSwitchGroup(switchGroup);
						}
						fromProduct = findPortfolioHoldProduct(holdId, productId);
					}else {
						switchGroup = switchMap.get(fromProductId);
						fromProduct = findPortfolioHoldProduct(holdId, fromProductId);
					}
					
					account = fromProduct.getAccount();
					accountNo = fromProduct.getAccountNo();
					pDistributor = findProductDistributor(productId, account.getDistributor().getId());
				}else {
					if("S".equalsIgnoreCase(tranType)) {
						account = holdProduct.getAccount();
						accountNo = holdProduct.getAccountNo();
					}else {
						String accountId = productObject.optString("accountId", "");
						accountNo = productObject.optString("accountNo", "");
						account = (InvestorAccount) baseDao.get(InvestorAccount.class, accountId);
					}

					pDistributor = findProductDistributor(productId, account.getDistributor().getId());					
				}
				
				OrderPlanProduct product = new OrderPlanProduct();
				product.setId(null);
				product.setPlan(plan);
				product.setProduct(productInfo);
				product.setWeight(weight);
				product.setWeightAdjust(weightAdjust);
				product.setTranType(tranType);
				product.setDividend(dividend);
				product.setAccount(account);
				product.setAccountNo(accountNo);
				product.setOriginal(original);
				product.setFromProduct(fromProductInfo);
				if(null != pDistributor){
					String fromCurrency = plan.getBaseCurrency();
					String toCurrency = pDistributor.getTransactionFeeCur();
					toCurrency = (toCurrency == null || "".equals(toCurrency))? fromCurrency : toCurrency;
					product.setTranFeeCur(toCurrency);
					product.setTranFeeMini(pDistributor.getTransactionFeeMini());
					product.setTranRate(pDistributor.getTransactionFeeRate());
					amount = getNumByCurrency(amount, fromCurrency, toCurrency);
					amountAdjust = getNumByCurrency( amountAdjust, fromCurrency, toCurrency);
					double fee = amount*pDistributor.getTransactionFeeRate()/100.0;
					if("B".equalsIgnoreCase(tranType)){
						product.setAmount(amount);
						product.setAmountAdjust(amountAdjust);
					}else if("S".equalsIgnoreCase(tranType)) {
						product.setUnit(unit);
						product.setUnitAdjust(unitAdjust);
					}

					product.setTranFee(fee);
				}
								
				baseDao.create(product);
			}
			

			plan.setTotalBuy(totalBuy>0?totalBuy:null);
			plan.setTotalSell(totalSell>0?totalSell:null);
			baseDao.saveOrUpdate(plan);
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	}
	
	
	private boolean validSwitchGroup(String groupStr) {
		boolean flag = false;
		if(StringUtils.isNotBlank(groupStr)){
			StringBuffer hql = new StringBuffer(" FROM OrderPlanProduct WHERE switchGroup=?");
			List<Object> params = new ArrayList<Object>();
			params.add(groupStr);
			List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	private PortfolioHoldProduct findPortfolioHoldProduct(String holdId,String productId){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" where m.portfolioHold.id=? and m.product.id=? ");
		
		List params = new ArrayList();
		params.add(holdId);
		params.add(productId);
		
		List<PortfolioHoldProduct> list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			return list.get(0);			
		}
				
		return null;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	private List<PortfolioHoldProduct> findPortfolioHoldProductList(String holdId){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(holdId);
		
		List<PortfolioHoldProduct> list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			return list;			
		}
				
		return null;
	}
	
	
	/**
	 * 组合产品分配比例
	 * @param holdId
	 * @param planId
	 * @param langCode
	 * @return
	 */
	public AppInvestPortfolioForRebalanceVO findProductAllocationForRebalance(String holdId,String planId,String langCode){
		AppInvestPortfolioForRebalanceVO detailVO = new AppInvestPortfolioForRebalanceVO();
		List<AppPieChartItemVO> voList = new ArrayList<AppPieChartItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(holdId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			String holdCurrency = CommonConstants.DEF_CURRENCY;
			double totalMarketValue = 0;
			double riskLevel = 0;
			
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];
				FundInfo fundInfo = (FundInfo) objs[1];
				FundInfoEn fundInfoEn = new FundInfoEn();
				BeanUtils.copyProperties(objs[2], fundInfoEn);
				PortfolioHold hold = holdProduct.getPortfolioHold();
				
				double holdUnit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
				double lastNav = fundInfo.getLastNav()==null? 0:fundInfo.getLastNav();
				String fundCurrency = StringUtils.isBlank(fundInfoEn.getFundCurrencyCode())?CommonConstants.DEF_CURRENCY:fundInfoEn.getFundCurrencyCode();
				holdCurrency = StringUtils.isBlank(hold.getBaseCurrency())? holdCurrency:hold.getBaseCurrency();
				totalMarketValue = totalMarketValue + getNumByCurrency(lastNav*holdUnit, fundCurrency, holdCurrency);
			}
			
			//包含组合交易计划的
			if(StringUtils.isNotBlank(planId)){
				OrderPlan plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
				if(null != plan){
					double totalBuy = plan.getTotalBuy()==null?0:plan.getTotalBuy();
					double totalSell = plan.getTotalSell()==null?0:plan.getTotalSell();
					totalMarketValue = totalMarketValue + totalBuy - totalSell;
				}
			}
			
			if("".equals(planId)){
				for (int i = 0; i < list.size(); i++) {
					AppPieChartItemVO itemVO = new AppPieChartItemVO();
					Object[] objs = (Object[]) list.get(i);
					PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];
					FundInfo fundInfo = (FundInfo) objs[1];
					FundInfoEn fundInfoEn = new FundInfoEn();
					BeanUtils.copyProperties(objs[2], fundInfoEn);
					
					String productId = holdProduct.getProduct().getId();
//					AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
					
					String productName = fundInfoEn.getFundName();// getHoldProductName(productId, langCode);
					String fundCurrency = StringUtils.isBlank(fundInfoEn.getFundCurrencyCode())?CommonConstants.DEF_CURRENCY:fundInfoEn.getFundCurrencyCode();
					double holdUnit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
					double lastNav = fundInfo.getLastNav()==null? 0:fundInfo.getLastNav();
					Double productMarketValue = getNumByCurrency(lastNav*holdUnit, fundCurrency, holdCurrency);
					productMarketValue = productMarketValue==null?0:productMarketValue;
					Double weight = totalMarketValue==0?0: productMarketValue/totalMarketValue;
					weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 
					Integer productRiskLevel = fundInfo.getRiskLevel();
					if(null != productRiskLevel){
						riskLevel = productRiskLevel * weight;
					}
					itemVO.setItemId(productId);
					itemVO.setItemName(productName);
					itemVO.setItemWeight(String.valueOf(weight));
					voList.add(itemVO);
				}
			}else {
				for (int i = 0; i < list.size(); i++) {
					AppPieChartItemVO itemVO = new AppPieChartItemVO();
					Object[] objs = (Object[]) list.get(i);
					PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];
					FundInfo fundInfo = (FundInfo) objs[1];
					FundInfoEn fundInfoEn = new FundInfoEn();
					BeanUtils.copyProperties(objs[2], fundInfoEn);
					
					String productId = holdProduct.getProduct().getId();
//					AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
					String productName = fundInfoEn.getFundName();
					
					Double weight = 0.0;
					OrderPlanProduct planProduct = findOrderPlanProductByProductId(planId, productId);
					if(null == planProduct){
						String fundCurrency = StringUtils.isBlank(fundInfoEn.getFundCurrencyCode())?CommonConstants.DEF_CURRENCY:fundInfoEn.getFundCurrencyCode();
						double holdUnit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
						double lastNav = fundInfo.getLastNav()==null? 0:fundInfo.getLastNav();
						Double productMarketValue = getNumByCurrency(lastNav*holdUnit, fundCurrency, holdCurrency);
						productMarketValue = getNumByCurrency(productMarketValue, holdProduct.getBaseCurrency(), holdCurrency);
						weight = productMarketValue/totalMarketValue;
						weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();						
					}else {
						weight = planProduct.getWeightAdjust();						
					}
					
					if(null != weight && weight>0){
						Integer productRiskLevel = fundInfo.getRiskLevel();
						if(null != productRiskLevel){
							riskLevel = productRiskLevel * weight;
						}
						itemVO.setItemId(productId);
						itemVO.setItemName(productName);
						itemVO.setItemWeight(String.valueOf(weight));
						voList.add(itemVO);
					}
				}
			}
			detailVO.setProductList(voList);
			int holdRiskLevel = new BigDecimal(riskLevel).setScale(0, BigDecimal.ROUND_UP).intValue();
			detailVO.setRiskLevel(String.valueOf(holdRiskLevel));
		}
						
		return detailVO;
	}
	
	
	/**
	 * 获取审批记录
	 * @author zxtan
	 * @data 2017-03-02
	 */
	public List<AppOrderPlanCheckVO> findOrderPlanCheckList(String planId,String langCode) {
		List<AppOrderPlanCheckVO> voList = new ArrayList<AppOrderPlanCheckVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From OrderPlanCheck m ");
		hql.append(" inner join MemberBase b on b.id=m.check.id ");
//		hql.append(" left join MemberIfa i on i.member.id=b.id ");
//		hql.append(" left join MemberIndividual d on d.member.id=b.id ");
		hql.append(" WHERE m.plan.id=? order by IFNULL(m.checkTime,NOW()) DESC");
		List<Object> params = new ArrayList<Object>();
		params.add(planId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){			
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				AppOrderPlanCheckVO vo = new AppOrderPlanCheckVO();
				OrderPlanCheck check = (OrderPlanCheck) objs[0];
				MemberBase member = (MemberBase) objs[1];
				vo.setId(check.getId());
				vo.setPlanId(planId);
				vo.setCheckId(member.getId());
				String name = getCommonMemberName(member.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setCheckName(name);
				vo.setCheckTime(DateUtil.dateToDateString(check.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCheckStatus(check.getCheckStatus());				
				vo.setCheckResult(check.getCheckResult());
				vo.setCheckIp(check.getCheckIp());
				
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
	/**
	 * 获取交易产品
	 * @author zxtan
	 * @data 2017-02-13
	 */
	private OrderPlanProduct findOrderPlanProductByProductId(String planId,String productId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" From OrderPlanProduct p ");
		hql.append(" WHERE p.plan.id=? and p.product.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(planId);
		params.add(productId);
		List<OrderPlanProduct> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return list.get(0);
		}
		
		return null;
	}
	
	
	/**
	 * 组合确认 步骤展示列表
	 * @author zxtan
	 * @data 2017-03-01
	 */
	public AppInvestPortfolioForCheckVO findInvestPortfolioForCheck(String memberId,String planId,String langCode) {
		AppInvestPortfolioForCheckVO vo = new AppInvestPortfolioForCheckVO();
		
		OrderPlan plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		PortfolioHold hold = plan.getPortfolioHold();
		if(null != hold){
			vo.setPortfolioName(hold.getPortfolioName());
		}
		String toCurrency = plan.getBaseCurrency();
		vo.setBaseCurrency(toCurrency);
		vo.setAipFlag(plan.getAipFlag());
		vo.setTotalBuy(getFormatNum(plan.getTotalBuy(),toCurrency));
		vo.setTotalSell(getFormatNum(plan.getTotalSell(),toCurrency));
		vo.setFinishStatus(plan.getFinishStatus());
		vo.setCreatorId(plan.getCreator().getId());
		
		String finishStatus = plan.getFinishStatus();
		if(null != plan.getCreator() && memberId.equalsIgnoreCase(plan.getCreator().getId())){
			vo.setReadyForOMS("3".equals(finishStatus)?"1":"0");
		}else {
			vo.setReadyForOMS("0");
		}
		
		
		String holdId = hold.getId();

		double totalMarketValue = hold.getTotalMarketValue()==null?0:hold.getTotalMarketValue();
		double totalBuy = plan.getTotalBuy()==null?0:plan.getTotalBuy();
		double totalSell = plan.getTotalSell()==null?0:plan.getTotalSell();
		double totalFee = 0;
		totalMarketValue = totalMarketValue + totalBuy-totalSell;
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(holdId);
		
		List<PortfolioHoldProduct> list = baseDao.find(hql.toString(), params.toArray(), false);
		
		Map<String,Double> riskMap = new HashMap<String, Double>();
		String rebalanceFlag = "0";
		if(null != list && !list.isEmpty()){
			rebalanceFlag = "1";
			String holdCurrency = hold.getBaseCurrency();
			
			for (int i = 0; i < list.size(); i++) {
				PortfolioHoldProduct holdProduct = list.get(i);
				String productId = holdProduct.getProduct().getId();
				AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
				Double weight = 0.0;
				
				Double productMarketValue = productInfoVO.getLastNav()*holdProduct.getHoldingUnit();// getHoldProductMarketValue(productId, holdProduct.getHoldingUnit(), holdCurrency);
				productMarketValue = getNumByCurrency(productMarketValue, productInfoVO.getBaseCurrency(), holdCurrency);
				
				weight = productMarketValue/totalMarketValue;
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();						
				
				
				if(null != weight && weight>0){
					Integer productRiskLevel = productInfoVO.getRiskLevel();
					if(null != productRiskLevel){
						double riskLevel = productRiskLevel * weight;
						riskMap.put(productId, riskLevel);
					}
				}
			}
			
		}
		
		vo.setRebalanceFlag(rebalanceFlag);
		
		hql = new StringBuilder();
		hql.append(" from OrderPlanProduct m ");
		hql.append(" inner join FundInfo f on f.product.id = m.product.id");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" l on l.id = f.id");
		hql.append(" where m.plan.id=? order by m.switchFlag,m.switchGroup,m.tranType desc");
		
		params = new ArrayList();
		params.add(planId);
		
		List planProductList = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppInvestProductForCheckVO> productList = new ArrayList<AppInvestProductForCheckVO>();
		
		if(null != planProductList && !planProductList.isEmpty()){
			Map<String,Double> switchMap = new HashMap<String, Double>();
			for (int i = 0; i < planProductList.size(); i++) {
				AppInvestProductForCheckVO planProductVO = new AppInvestProductForCheckVO();
				Object[] objs = (Object[]) planProductList.get(i);
				OrderPlanProduct planProduct = (OrderPlanProduct) objs[0];
				FundInfo fundInfo = (FundInfo) objs[1];
				FundInfoEn fundInfoEn = new FundInfoEn();
				BeanUtils.copyProperties(objs[2], fundInfoEn);
				//产品风险等级集合
				double riskLevel = 0;				
				
				riskLevel = fundInfo.getRiskLevel()==null?0:fundInfo.getRiskLevel();
				riskLevel = planProduct.getWeightAdjust()==null?0:riskLevel*planProduct.getWeightAdjust();
				
				riskMap.put(fundInfo.getProduct().getId(), riskLevel);				
				planProductVO.setProductId(fundInfo.getProduct().getId());
				planProductVO.setFundId(fundInfo.getId());
				planProductVO.setFundName(fundInfoEn.getFundName());
				planProductVO.setFundType(fundInfoEn.getFundType());
				String fromCurrency = planProduct.getTranFeeCur()==null?fundInfoEn.getFundCurrencyCode():planProduct.getTranFeeCur();
							
				planProductVO.setFundCurrency(fundInfoEn.getFundCurrency());
				planProductVO.setFundCurrencyCode(fundInfoEn.getFundCurrencyCode());
				planProductVO.setIssueCurrency(fundInfoEn.getIssueCurrency());
				planProductVO.setIssueCurrencyCode(fundInfoEn.getIssueCurrencyCode());
				
				planProductVO.setToCurrency(plan.getBaseCurrency());
				planProductVO.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
				planProductVO.setWeight(getFormatNum(planProduct.getWeightAdjust()));
				Double rate = getExchangeRate(fromCurrency, toCurrency);
				String tranType = planProduct.getTranType();
				planProductVO.setTranType(tranType);
				planProductVO.setTranRate(String.valueOf(planProduct.getTranRate()));
				double amount = 0;
				if(null != rate){
					double unit = 0;
					double lastNav = fundInfo.getLastNav();
					
					if ("B".equalsIgnoreCase(tranType)) {
						amount = planProduct.getAmount()==null?0:planProduct.getAmount();
						planProductVO.setAmount(getFormatNumByRate(amount, rate,toCurrency));
						unit = amount/lastNav;
						unit = new BigDecimal(unit).setScale(0, BigDecimal.ROUND_DOWN).intValue();
						planProductVO.setUnit(String.valueOf(unit));
					}else {
						double holdingUnit = planProduct.getUnit()==null?0:planProduct.getUnit();
						holdingUnit = planProduct.getUnitAdjust()==null?holdingUnit:(holdingUnit+planProduct.getUnitAdjust());
						unit = planProduct.getUnit()==null?0:planProduct.getUnit();
						amount = lastNav * unit;
						planProductVO.setAmount(getFormatNumByRate(amount, rate,toCurrency));
						planProductVO.setHoldingUnit(String.valueOf(holdingUnit));
					}
					double tranFee = 0;
					double tranFeeMini = 0;
					if(null != planProduct.getTranRate()){
						tranFee = amount*planProduct.getTranRate()/100.0;
					}
					
					if(null != planProduct.getTranFeeMini()){
						tranFeeMini = planProduct.getTranFeeMini();
					}
					
					
					if(tranFee > tranFeeMini){
						totalFee = totalFee + tranFee*rate;
						planProductVO.setTranFee(getFormatNumByRate(tranFee, rate,toCurrency));
					}else {
						totalFee = totalFee + tranFeeMini*rate;
						planProductVO.setTranFee(getFormatNumByRate(tranFeeMini, rate,toCurrency));
					}
				}
				InvestorAccount investorAccount = planProduct.getAccount();
				if(null != investorAccount){
					planProductVO.setAccountId(investorAccount.getId());
					planProductVO.setAccountNo(planProduct.getAccountNo());
				}
				
				
				planProductVO.setDividend(planProduct.getDividend());
				String switchFlag = planProduct.getSwitchFlag();
				planProductVO.setSwitchFlag(switchFlag);
				if("1".equals(switchFlag)){
					String switchGroup = planProduct.getSwitchGroup();
					planProductVO.setSwitchGroup(switchGroup);
					if(null != planProduct.getFromProduct()){
						planProductVO.setFromProductId(planProduct.getFromProduct().getId());
					}
					if("S".equalsIgnoreCase(tranType)){
						switchMap.put(switchGroup, amount);
					}else {
						double switchAmount = switchMap.get(switchGroup);
						double switchWeight = amount/switchAmount;
						switchWeight = new BigDecimal(switchAmount).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
						planProductVO.setSwitchWeight(String.valueOf(switchWeight));
					}
				}				
				
				productList.add(planProductVO);				
			}
		}
		
		double riskLevel = 0;
		for (Map.Entry<String, Double> entry : riskMap.entrySet()) {
			riskLevel = riskLevel + entry.getValue();
		}
		
		int holdRiskLevel = new BigDecimal(riskLevel).setScale(0, BigDecimal.ROUND_UP).intValue();
		
		vo.setTotalFee(getFormatNum(totalFee,toCurrency));
		vo.setRiskLevel(String.valueOf(holdRiskLevel));
		vo.setProductList(productList);
		
		List<AppOrderPlanCheckVO> checkList = findOrderPlanCheckList(planId, langCode);
		vo.setCheckList(checkList);
		
		return vo;
	}

}
