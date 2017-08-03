/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.buscore.fund.vo.CorePieChartItemVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundPortfolioSc;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.vo.PortfolioFundListVO;

/**
 * 基金计算相关的核心公共类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2017-5-9
 */
@Service("corePortfolioService")
public class CorePortfolioServiceImpl extends BaseService implements
		CorePortfolioService {
	@Resource
	private PortfolioArenaService portfolioArenaService;
	@Autowired
	private CoreFundService coreFundService;
	/**
	 * 获取指定时间范围内的组合累计收益数据
	 * 
	 * @param portfolioId
	 *            组合id
	 * @param frequencyType
	 *            频率 1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CorePortfolioVO> getPortfolioReturnRate(String portfolioId,String frequencyType, String currency) {
		List<CorePortfolioVO> list = new ArrayList<CorePortfolioVO>();
		list = findArenaReturnRate(portfolioId, frequencyType, currency);
		return list;
	}

	// 获取基金日期范围内累计收益
	public List<CorePortfolioVO> findArenaReturnRate(String portfolioId,String frequencyType, String currency) {
		//通过组合获取所包含的多只基金
		String fundIds = "";
		String allocationRates = "";
		List<PortfolioFundListVO> fundList = findPortfolioFundList(portfolioId);
		if(null!=fundList&&!fundList.isEmpty()){
			for(PortfolioFundListVO each : fundList){
				String fundId = each.getFundId();
				String allocationRate = StrUtils.getString(each.getWeight());
				if(StringUtils.isNotBlank(fundId)){
					fundIds += fundId + ",";
					allocationRates += allocationRate + ",";
				}
			}
		}
		if(StringUtils.isNotBlank(fundIds)){
			fundIds = fundIds.substring(0, fundIds.length()-1);
			allocationRates = allocationRates.substring(0, allocationRates.length()-1);
		}
		List<CorePortfolioVO> newList = new ArrayList<CorePortfolioVO>();
		List<CoreFundNavVO> list = coreFundService.getMoreFundReturnRateForAPP(fundIds,allocationRates, frequencyType,"");
		if(null!=list&&!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				CorePortfolioVO vo = new CorePortfolioVO();
				vo.setMarketDate(list.get(i).getMarketDate());
				vo.setReturnRate(list.get(i).getRate());
				vo.setMarketDateStr( DateUtil.getDateStr(list.get(i).getMarketDate()) );
				vo.setReturnRate(list.get(i).getRate());
				newList.add(vo);
			}
		}
		
		return newList;
	}
	// 获取组合基金日期范围内累计收益
	private List<CorePortfolioVO> findArenaReturnRate_BAK(String portfolioId,String frequencyType, String currency) {
		List<CorePortfolioVO> voList = new ArrayList<CorePortfolioVO>();
						
		Map<Date, Date> marketDateMap = new TreeMap<Date, Date>(); //日期
		Map<String,Map<Date, CoreFundNavVO>> dataMap = new HashMap<String, Map<Date,CoreFundNavVO>>();//基金回报
		Date firstDate = null;
		Map<String,Double> firstDayNavMap = new HashMap<String, Double>();//基金回报
		Map<String,Double> fundWeightMap = new HashMap<String, Double>();//基金权重
		
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		Date beginDate;
		Date endDate;
		Date createDate;
		
		hql.append(" from FundMarket t ");
		hql.append(" inner join FundInfo f on f.id=t.fund.id ");
		hql.append(" inner join PortfolioArenaProduct p on p.product.id=f.product.id ");
		hql.append(" where t.marketDate is not null and p.portfolio.id=? ");
		hql.append(" order by t.marketDate desc ");
		params = new ArrayList<Object>();
		params.add(portfolioId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false); 
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[]) list.get(0);
			FundMarket  market = (FundMarket) objs[0];
			PortfolioArenaProduct product = (PortfolioArenaProduct) objs[2];
			
			endDate = market.getMarketDate();
			createDate = product.getPortfolio().getCreateTime();
		}else {
			endDate = new Date();
			createDate = null;
		}
		
		beginDate = getBeginDate(endDate, frequencyType);
		if(null != createDate && beginDate.before(createDate)){			
			beginDate = createDate;			
		}
		
		hql = new StringBuilder();
		hql.append(" from FundMarket t ");
		hql.append(" inner join FundInfo f on f.id=t.fund.id ");
		hql.append(" inner join PortfolioArenaProduct p on p.product.id=f.product.id ");
		hql.append(" where t.marketDate >? and p.portfolio.id=? ");
		hql.append(" order by t.marketDate ");
		params = new ArrayList<Object>();
		params.add(beginDate);
		params.add(portfolioId);
		List marketDataList = baseDao.find(hql.toString(), params.toArray(), false); 
		if(null != marketDataList && !marketDataList.isEmpty()){
			
			for (int i = 0; i < marketDataList.size(); i++) {
				Object[] objs = (Object[]) marketDataList.get(i);
				FundMarket  market = (FundMarket) objs[0];
				FundInfo fundInfo = market.getFund();
				PortfolioArenaProduct product = (PortfolioArenaProduct) objs[2];
				
				//日期
				marketDateMap.put(market.getMarketDate(), market.getMarketDate());
				if(null == firstDate){
					firstDate = market.getMarketDate();
				}
				
				String fundId = fundInfo.getId();
				double nav = market.getNav()==null?0:market.getNav();
				double rate = product.getAllocationRate()==null?0:product.getAllocationRate();
				
				CoreFundNavVO fundNavVO = new CoreFundNavVO();
				fundNavVO.setFundId(fundId);
				fundNavVO.setMarketDate(market.getMarketDate());
				fundNavVO.setNav(nav);
				fundNavVO.setRate(rate);
				
				fundWeightMap.put(fundId, rate);
				
				Map<Date, CoreFundNavVO> subMap = new HashMap<Date, CoreFundNavVO>();
				if(dataMap.containsKey(fundId)){
					subMap = dataMap.get(fundId);						
				}
				subMap.put(market.getMarketDate(), fundNavVO);
				dataMap.put(fundId, subMap);
			}
			
			firstDate = findFundFirstNavDate(dataMap, firstDate);
			List<Date> removeList = new ArrayList<Date>();
			
			for (Map.Entry<Date, Date> entry : marketDateMap.entrySet()) {
				if(entry.getKey().before(firstDate)){
					removeList.add(entry.getKey());					
				}
			}
			
			for (Date date : removeList) {
				marketDateMap.remove(date);
			}
			
			firstDayNavMap = findFundFirstNavMap(dataMap,firstDate);
						
			for (Map.Entry<Date, Date> entry : marketDateMap.entrySet()) {
				CorePortfolioVO vo = new CorePortfolioVO();
				//循环每一天
				Date marketDate = entry.getValue();
				double returnRate = 0; 
				for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
					//循环每一产品
					String fundId = data.getKey();
					Map<Date, CoreFundNavVO> fundDataList = data.getValue();
					double fundFirstNav = firstDayNavMap.get(fundId);
					//double weight = fundWeightMap.get(fundId)/100.0;
					double weight = fundWeightMap.get(fundId);
					double nav = 0;
					
					if(fundDataList.containsKey(marketDate)){
						//当天有数据
						nav = fundDataList.get(marketDate).getNav()==null?0:fundDataList.get(marketDate).getNav();
					}else {
						//当天没有数据，要补数据
						FundMarket market = getPrevFundMarketInfo(fundId,marketDate);
						if(null != market){
							nav = market.getNav()==null?0:market.getNav();
						}
					}
					
					double fundReturnRate = fundFirstNav>0? weight*(nav-fundFirstNav)/fundFirstNav:0;
					returnRate += fundReturnRate;
				}
				
				returnRate = new BigDecimal(returnRate).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setPortfolioId(portfolioId);
				vo.setMarketDate(marketDate);
				vo.setMarketDateStr(DateUtil.dateToDateString(marketDate, CommonConstants.FORMAT_DATE));
				vo.setReturnRate(returnRate);
				voList.add(vo);
			}
					
		}

		return voList;
	}
	
	private Date getBeginDate(Date date, String frequencyType) {
		Date beginDate;
		if (StringUtils.isNotBlank(frequencyType)) {
			// 获取相关日期数
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if ("1W".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -7);				
			} else if ("2W".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -14);				
			} else if ("1M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -30);
			} else if ("3M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -90);
			} else if ("6M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -180);				
			} else if ("YTD".equals(frequencyType)) {
				// 获取当年第一天
				Date firstDate = DateUtil.getCurrYearFirst();	
				calendar.setTime(firstDate);
			} else if ("1Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -365);
				
			} else if ("3Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -3 * 365);
				
			} else if ("5Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -5 * 365);				
			}
			beginDate = calendar.getTime();
		}else {
			beginDate = new Date();
		}
		return beginDate;
	}
	
	private Date findFundFirstNavDate(Map<String,Map<Date, CoreFundNavVO>> dataMap,Date firstDate){
		Date result = firstDate;
		for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
			String fundId = data.getKey();
			Map<Date, CoreFundNavVO> fundDataList = data.getValue();
			Date marketDate = null;
			if( fundDataList.containsKey(firstDate)){
				marketDate = firstDate;
			}else {
				FundMarket market = getFundFirstMarketInfo(fundId,firstDate);
				if(null != market){
					marketDate = market.getMarketDate();
				}else {					
					marketDate = firstDate;
				}
			}
			if(marketDate.after(result)){
				result = marketDate;
			}
			
		}		
					
		return result;		
	}
	
	private Map<String, Double> findFundFirstNavMap(Map<String,Map<Date, CoreFundNavVO>> dataMap,Date firstDate){
		Map<String, Double> result = new HashMap<String, Double>();
		for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
			String fundId = data.getKey();
			Map<Date, CoreFundNavVO> fundDataList = data.getValue();
			double firstNav = 0;
			if( fundDataList.containsKey(firstDate)){
				firstNav = fundDataList.get(firstDate).getNav();
			}else {
				FundMarket market = getFundFirstMarketInfo(fundId,firstDate);
				if(null != market){
					firstNav = market.getNav()== null?1:market.getNav();
				}else {					
					firstNav = 1;
				}
			}
			result.put(fundId, firstNav);
		}		
					
		return result;		
	}
	
	private double findFundFirstNav(String fundId, Map<Date, CoreFundNavVO> dataMap,Date firstDate){
		double firstNav = 0;
		if( dataMap.containsKey(firstDate)){
			firstNav = dataMap.get(firstDate).getNav();
		}else {
			FundMarket market = getLatestFundMarketInfo(fundId);
			firstNav = market.getNav();
		}				
		return firstNav;		
	}

	/**
	 * 转换净值的货币
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	public Double traCurrency(Double nav, String from, String to) {
		if (from.equals(to) && StringUtils.isNotBlank(from)
				&& StringUtils.isNotBlank(to))
			return this.getNumByCurrency(nav, from, to);
		else
			return nav;
	}

	/**
	 * 通过FundID,日期获取基金的行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getFundMarketInfo(String productId, Date date) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate=?";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,获取基金的最新的一条行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getLatestFundMarketInfo(String fundId) {
		String sql = "from FundMarket r where r.fund.id=? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(),0,2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,获取基金的第一条行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getFundFirstMarketInfo(String fundId) {
		String sql = "from FundMarket r where r.fund.id=? order by r.marketDate asc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,日期获取基金的行情数据，如果该日期无数据，则通过获取比它最早的有数据的那条记录
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getPrevFundMarketInfo(String fundId, Date date) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate<? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(),0,2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过FundID,日期获取基金的行情数据，如果该日期无数据，则通过获取比它最早的有数据的那条记录
	 * 
	 * @author zxtan 2017-05-26
	 * @return
	 */
	private FundMarket getFundFirstMarketInfo(String fundId, Date date) {
		String sql = "from FundMarket r where r.nav>0 and r.fund.id=? and r.marketDate<? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(),0,2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			sql = "from FundMarket r where r.nav>0 and r.fund.id=? order by r.marketDate";
			params = new ArrayList<Object>();
			params.add(fundId);
			list = this.baseDao.find(sql, params.toArray(),0,2, false);
			if (null != list && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}			
		}
	}

	/**
	 * 通过ProductId获取FundID
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private String getFundInfoByProductId(String productId) {
		String sql = "from FundInfo r where r.product.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		List<FundInfo> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0).getId();
		} else {
			return "";
		}
	}

	/**
     * 汇总基金组合（Ecahrt饼图）
     * @date 2017/2/23
     * @param fundDatas  json格式基金数据 [{"fundId":"0691edfe9e374821bf1b141d6f105fb6","weight":"74.0"}]
     * @param type  fund_portfolio.type
     * @param langCode
     * @return Map<name, value> 
     */
	@Override
	public Map<String, Double> getFundCompositionData(String funds,
			String type, String langCode) {
		Map<String, Double> result = new TreeMap<String, Double>();
		if (StringUtils.isNotBlank(funds)) {
			List<Map> fundMaps = JsonUtil.toListMap(funds);
			if(fundMaps != null && !fundMaps.isEmpty()){
				Double total = 0.00;
				Map<String, Double> map = new TreeMap<String, Double>();
				for (Map fundMap : fundMaps) {
					String fundId = (String) fundMap.get("fundId");
					String weightStr = (String) fundMap.get("weight");
					Double weight = null;
					if (StringUtils.isNotBlank(weightStr)) {
						weight = Double.parseDouble(weightStr)/100;
					}
					if(weight == null){
						weight = 0.00;
					}
					if(StringUtils.isNotBlank(fundId)){
						StringBuffer hql = new StringBuffer("" +
								" SELECT l.name,p.rate FROM" +
								" FundPortfolio p" +
								" LEFT JOIN" +
								" FundPortfolio" + this.getLangFirstCharUpper(langCode) + " l" +
								" ON" +
								" l.id=p.id" +
								" WHERE" +
								" p.fund.id=?" +
								" AND" +
								" p.type=?" +
								" AND" +
								" p.isValid=1");
						List<Object> params = new ArrayList<Object>();
						params.add(fundId);
						params.add(type);
						List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
						if(objs != null && !objs.isEmpty()){
							for (Object[] objects : objs) {
								String name = (String) objects[0];
								Double rate = (Double) objects[1];
								total = total + rate * weight;
								if(name != null){
									if(map.get(name) == null){
										rate = rate * weight;
										map.put(name, rate);
									}else{
										Double value = map.get(name);
										value = value + rate * weight;
										map.put(name, value);
									}
								}
							}
							for (String key : map.keySet()) {
								Double rate = map.get(key);
								if(total > 0){
									result.put(key, rate/total*100);
								}
							}
						}
					}
				}
			}
		}
		if(result != null && result.isEmpty()){
			result = null;
		}
		return result;
	}
	
	/**
     * 汇总基金组合（APP饼图）
     * @date 2017/6/7
     * @param productIds  产品id,多个以号码隔开
     * @param productWeights  产品占比重（小数），多个以逗号隔开
     * @param langCode 语言
     * @param groupType 类型：market/sector
     * @return 
     */
	public List<CorePieChartItemVO> getFundCompositionData(String productIds,String productWeights,String langCode,String groupType){
		List<CorePieChartItemVO>  voList = new ArrayList<CorePieChartItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f ");
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
			String[] arrRate = productWeights.split(",");
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
				CorePieChartItemVO vo = new CorePieChartItemVO();
				vo.setItemId(null);
				vo.setItemName(entry.getKey());
				double weight = entry.getValue();
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setItemWeight(String.valueOf(weight));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 获取组合的基金列表（用户组合详情显示）
	 * @author mqzou 2014-04-17
	 * @param id
	 * @return
	 */
	public List<PortfolioFundListVO> findPortfolioFundList(String id) {
		List<String> params = new ArrayList<String>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from PortfolioArenaProduct p left join ProductInfo i on p.product.id=i.id ");
		hql.append(" inner join FundInfo f on f.product.id=i.id ");
		hql.append(" where i.type='fund' and p.portfolio.id=? and f is not null");
		//String hql = " from FundInfo t where t.product.id in (select i.id from PortfolioArenaProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.portfolio.id=?) ";
		params.add(id);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<PortfolioFundListVO> list=new ArrayList<PortfolioFundListVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				PortfolioArenaProduct product=(PortfolioArenaProduct)objects[0];
				FundInfo fundInfo=(FundInfo)objects[2];
				if(null==fundInfo)
					continue;
				PortfolioFundListVO vo=new PortfolioFundListVO();
				vo.setFundId(fundInfo.getProduct().getId());
				vo.setWeight(product.getAllocationRate());
				list.add(vo);
			}
		}
		return list;
	}


}
