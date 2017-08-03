/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavAlignVO;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.buscore.fund.vo.CoreMoreFundRateVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.persistence.SpringJdbcQueryManager;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.NewsInfoDoc;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.news.vo.NewsSimpleInfoVO;

/**
 * 组合计算相关的核心公共类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
@Service("coreFundService")
public class CoreFundServiceImpl extends BaseService implements CoreFundService{
	/**
	 * 获取指定时间范围内的基金净值数据
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavVO> getFundNav(String fundId,String frequencyType,String currency){
		List<CoreFundNavVO> returnList = new ArrayList<CoreFundNavVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from FundMarket r ");
		hql.append("  where r.fund.id=? ");
		params.add(fundId);
		//计算日期范围
		if (StringUtils.isNotBlank(frequencyType)) {
			Calendar calendarc = Calendar.getInstance();
			//获取最新一条的基金净值
			Date tmpLatestDate = calendarc.getTime();
			FundMarket latestFundMarket =  getLatestFundMarketInfo(fundId);
			if(null!=latestFundMarket){
				tmpLatestDate = latestFundMarket.getMarketDate();
			}
			if ("1W".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.DATE, -7);
			}
			else if("2W".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.DATE, -14);
			}
			else if("1M".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.MONTH, -1);
			}
			else if("3M".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.MONTH, -3);
			}
			else if("6M".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.MONTH, -6);
			}
			else if("1Y".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.YEAR, -1);
			}
			else if("3Y".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.YEAR, -3);
			}
			else if("5Y".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				calendarc.add(Calendar.YEAR, -5);
			}
			else if("YTD".equals(frequencyType)){
				calendarc.setTime(tmpLatestDate);
				Date yearFirstDate = DateUtil.getCurrYearFirst();
				int diffDay = (int)DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(yearFirstDate),DateUtil.getDateStr(tmpLatestDate));
				calendarc.add(Calendar.DATE, -diffDay);
			}
			//hql.append(" and r.marketDate between ? and CURDATE()");
			//params.add(calendarc.getTime());
			Date ttt = calendarc.getTime();
			params.add(calendarc.getTime());
			params.add(tmpLatestDate);
			
			//获取范围起始的那一天是否有净值，没有就拿前面的补上
			//FundMarket beginFundMarket =  this.getFundMarketInfo(fundId, calendarc.getTime());
			
		}
		hql.append(" and r.marketDate between ? and ?" );
		hql.append("  order by r.marketDate asc");

		//jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<FundMarket> list = this.baseDao.find(hql.toString(),  params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			for(FundMarket each : list){
				Date marketDate = each.getMarketDate();
				Double nav = each.getNav();
				Double returnRate = each.getReturnRate();
				String currencyCode = each.getCurrencyCode();
				if(StringUtils.isNotBlank(currency) && !currency.equals(currencyCode)){
					nav = getNumByCurrency(nav,currencyCode,currency);
				}
				//组装返回的实体
				//nav = Double.parseDouble(new DecimalFormat("#.00").format(nav));
				//returnRate = Double.parseDouble(new DecimalFormat("#.0000").format(returnRate));
				CoreFundNavVO fundNavVo = new CoreFundNavVO();
				fundNavVo.setFundId(fundId);
				fundNavVo.setMarketDate(marketDate);
				fundNavVo.setMarketDateStr(DateUtil.dateToDateString(marketDate, CommonConstants.FORMAT_DATE));
				fundNavVo.setNav(nav);
				fundNavVo.setRate(returnRate);
				returnList.add(fundNavVo);
			}
		}


		return returnList;
	}
		
	/**
	 * 获取指定时间范围内的基金累计收益数据
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public CoreFundsReturnForChartsVO getFundReturnRate(String fundId,String frequencyType,String currency){
		List<CoreFundNavVO> list = new ArrayList<CoreFundNavVO>();
		CoreFundsReturnForChartsVO vo = new CoreFundsReturnForChartsVO();
		StringBuilder hql=new StringBuilder();
		hql.append(" from FundMarket r ");
		hql.append("  where r.fund.id=? ");
		//计算日期范围
		if (StringUtils.isNotBlank(frequencyType)) {
			//先获取最新的一条净值的时间，以它做为时间基准
			FundMarket fm = this.getLatestFundMarketInfo(fundId);
			if(null!=fm&&""!=fm.getId()){
				Date latestDate = fm.getMarketDate();//以该时间做为基准，设置时间范围
				Calendar calendar = Calendar.getInstance();
	        	calendar.setTime(latestDate);//给日历设定它的基准时间 ，比如：2017/4/27
	        	long diffDay = 0;
	        	
	        	if ("1W".equals(frequencyType)){
					calendar.add(Calendar.DATE, -7);//一周的第一天
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("2W".equals(frequencyType)){
					calendar.add(Calendar.DATE, -14);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("1M".equals(frequencyType)){
					calendar.add(Calendar.MONTH, -1);
					//diffDay = 30;
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("3M".equals(frequencyType)){
					calendar.add(Calendar.MONTH, -3);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("6M".equals(frequencyType)){
					calendar.add(Calendar.MONTH, -6);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("YTD".equals(frequencyType)){
					//获取当年第一天
					Date firstDate = DateUtil.getCurrYearFirst();
					try {
						diffDay = DateUtil.daysBetween(firstDate,calendar.getTime());
						calendar.add(Calendar.DATE, -(int)diffDay);
						//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
						vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
						} catch (ParseException e) {
							e.printStackTrace();
						}
				}
				else if ("1Y".equals(frequencyType)){
					calendar.add(Calendar.YEAR, -1);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("3Y".equals(frequencyType)){
					calendar.add(Calendar.YEAR, -3);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
				else if ("5Y".equals(frequencyType)){
					calendar.add(Calendar.DATE, -5);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
					vo = this.genSigleFundsReturnRate((int)diffDay, fundId,calendar , latestDate,"");
				}
			}
			//获取相关日期数
			//long diffDay = 0;
			//Calendar calendar = Calendar.getInstance();
			
		}
		return vo;
	}
	
	public List<CoreFundNavVO> genFundReturnRate(int diffDay,String fundId,Calendar calendar,String currency){
		List<CoreFundNavVO> list = new ArrayList<CoreFundNavVO>();
		//先日期为基准，每个日期循环里面多个基金
		Double prevNav = 0.00;//存放前一天的净值数据
		for(int i=0;i<diffDay;i++){//判断每一天是否有数据
//			if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)){
//				calendar.add(Calendar.DATE, -2);
//			}else if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
//				calendar.add(Calendar.DATE, -1);
//			}
					CoreFundNavVO tempVo = new CoreFundNavVO();
					//组合该日期，获取该日期的基金净值
					//String marketDateStr = DateUtil.getDateStr(calendar.getTime());
					Date marketDate = calendar.getTime();
					FundMarket vo = getFundMarketInfo(fundId,marketDate);
					if(i==0){ //第一笔数据是0%;
						if(null!=vo){//该天有数据
							tempVo.setFundId(fundId);
							tempVo.setMarketDate(marketDate);
							Double nav = vo.getNav();
							String currencyCode = vo.getCurrencyCode();
							Double newNav = traCurrency(nav,currencyCode,currency);
							tempVo.setNav(newNav);
							tempVo.setRate(0.00);
							prevNav = vo.getNav();
						}else{//该天无数据，取最近一条补齐
							vo = getPrevFundMarketInfo(fundId,marketDate);
							if(null!=vo){//该天有数据
								tempVo.setFundId(fundId);
								tempVo.setMarketDate(marketDate);
								Double nav = vo.getNav();
								String currencyCode = vo.getCurrencyCode();
								Double newNav = traCurrency(nav,currencyCode,currency);
								tempVo.setNav(newNav);
								tempVo.setRate(0.00);
								prevNav = vo.getNav();
							} 
						}
					} else{ //不是第一天，则今天-昨天/昨天
						if(null!=vo){//该天有数据
							tempVo.setFundId(fundId);
							tempVo.setMarketDate(marketDate);
							Double nav = vo.getNav();
							String currencyCode = vo.getCurrencyCode();
							Double newNav = traCurrency(nav,currencyCode,currency);
							tempVo.setNav(newNav);
							Double rate = (vo.getNav()-prevNav)/prevNav;
							rate = Double.parseDouble(new DecimalFormat("#.0000").format(rate));
							tempVo.setRate(rate);
							prevNav = vo.getNav();
						}else{//该天无数据，取最近一条补齐
							vo = getPrevFundMarketInfo(fundId,marketDate);
							if(null!=vo){//该天有数据
								tempVo.setFundId(fundId);
								tempVo.setMarketDate(marketDate);
								Double nav = vo.getNav();
								String currencyCode = vo.getCurrencyCode();
								Double newNav = traCurrency(nav,currencyCode,currency);
								tempVo.setNav(newNav);
								Double rate = (vo.getNav()-prevNav)/prevNav;
								rate = Double.parseDouble(new DecimalFormat("#.0000").format(rate));
								tempVo.setRate(rate);
								prevNav = vo.getNav();
							} 
						}
					}
					if(null!=tempVo)list.add(tempVo);
			//日期递增
			calendar.add(calendar.DATE, 1);//1表示1天，7表示一周
		}
		
		return list;
	}
	
	/**
	 * 获取指定时间范围内的多个净值集合对齐数据
	 * @param fundIds 基金id,多个之间用,分隔
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavAlignVO> getFundNavAlign(String fundIds,String frequencyType,String currency){
		
		List<CoreFundNavAlignVO> list = new ArrayList<CoreFundNavAlignVO>();
		list = genFundNavAlign(fundIds, frequencyType, currency);
		return list;
	}
	
	public List<CoreFundNavAlignVO> genFundNavAlign(String fundIds,String frequencyType,String currency){
		List<CoreFundNavAlignVO> superVoList = new ArrayList<CoreFundNavAlignVO>();
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		//List<CoreFundNavVO> returnList = new ArrayList<CoreFundNavVO>();
		//List<Object> params = new ArrayList<Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from FundMarket r ");
		hql.append("  where r.fund.id=? ");
		//计算日期范围
		if (StringUtils.isNotBlank(frequencyType)) {
			//获取相关日期数
			long diffDay = 0;
			Calendar calendar = Calendar.getInstance();
			if ("1W".equals(frequencyType)){
				calendar.add(Calendar.DATE, -7);
				diffDay = 7; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("2W".equals(frequencyType)){
				calendar.add(Calendar.DATE, -14);
				diffDay = 14; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("1M".equals(frequencyType)){
				calendar.add(Calendar.DATE, -30);
				diffDay = 30; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("3M".equals(frequencyType)){
				calendar.add(Calendar.DATE, -90);
				diffDay = 90; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("6M".equals(frequencyType)){
				calendar.add(Calendar.DATE, -180);
				diffDay = 180; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("YTD".equals(frequencyType)){
				//获取当年第一天
				Date firstDate = DateUtil.getCurrYearFirst();
				try {
					diffDay = DateUtil.daysBetween(firstDate,calendar.getTime());
					calendar.add(Calendar.DATE, -(int)diffDay);
					superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
					} catch (ParseException e) {
						e.printStackTrace();
				}
			}
			else if ("1Y".equals(frequencyType)){
				calendar.add(Calendar.DATE, -365);
				diffDay = 365; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,1,currency);
			}
			else if ("3Y".equals(frequencyType)){
				calendar.add(Calendar.DATE, -3*365);
				diffDay = 156; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,7,currency);
			}
			else if ("5Y".equals(frequencyType)){
				calendar.add(Calendar.DATE, -5*365);
				diffDay = 260; 
				superVoList = getCoreFundNavAlignVO(diffDay,fundIds,calendar,7,currency);
			}
		}


		return superVoList;
	}
	
	/**
	 * 循环组装数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private List<CoreFundNavAlignVO> getCoreFundNavAlignVO(long diffDay,String fundIds ,Calendar calendar,int diffType,String currency){
		List<CoreFundNavAlignVO> superVoList = new ArrayList<CoreFundNavAlignVO>();
		//先日期为基准，每个日期循环里面多个基金
		for(int i=0;i<diffDay;i++){//判断每一天是否有数据
			String[] arrayFunds=fundIds.split(",");
			List<CoreFundNavVO> dataList = new ArrayList<CoreFundNavVO>();
			if(null!=arrayFunds && arrayFunds.length>0){
				for (String fundId : arrayFunds) {
					CoreFundNavVO tempVo = new CoreFundNavVO();
					//组合该日期，获取该日期的基金净值
					//String marketDateStr = DateUtil.getDateStr(calendar.getTime());
					Date marketDate = calendar.getTime();
					FundMarket vo = getFundMarketInfo(fundId,marketDate);
					if(null!=vo){//该天有数据
						tempVo.setFundId(fundId);
						tempVo.setMarketDate(marketDate);
						tempVo.setNav(this.traCurrency(vo.getNav(),vo.getCurrencyCode(),currency));
						tempVo.setRate(vo.getReturnRate());
					}else{//该天无数据，取最近一条补齐
						vo = getPrevFundMarketInfo(fundId,marketDate);
						if(null!=vo){//该天有数据
							tempVo.setFundId(fundId);
							tempVo.setMarketDate(marketDate);
							tempVo.setNav(this.traCurrency(vo.getNav(),vo.getCurrencyCode(),currency));
							tempVo.setRate(vo.getReturnRate());
						} else{//也没有数据的话，给个默认的
							tempVo.setFundId(fundId);
							tempVo.setMarketDate(marketDate);
							tempVo.setNav(null);
							tempVo.setRate(null);
						}
					}
					if(null!=tempVo)dataList.add(tempVo);
				}
			}
			CoreFundNavAlignVO superVo = new CoreFundNavAlignVO();
			superVo.setDataDate(calendar.getTime());
			superVo.setDataDateStr(DateUtil.getDateStr(calendar.getTime()));
			superVo.setDataIndex(i+1);
			superVo.setDataList(dataList);
			superVoList.add(superVo);
			//日期递增
			calendar.add(calendar.DATE, diffType);//1表示1天，7表示一周
		}
		
		return superVoList;
	}
	
	/**
	 * 获取指定时间范围内的多个基金的累计收益数据(合并一条线)
	 * 
	 * @param portfolioId
	 *            组合id
	 * @param frequencyType
	 *            频率 1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundNavVO> getMoreFundReturnRateForAPP(String productIds,String allocationRateList,String frequencyType,String langCode) {
		List<CoreFundNavVO> voList = new ArrayList<CoreFundNavVO>();
		List<CoreFundsReturnForChartsVO> list = new ArrayList<CoreFundsReturnForChartsVO>();
		if (StringUtils.isNotBlank(frequencyType)) {
			//获取基准日期为最新的日期，然后向前推
			HashMap<Date, Double> marketMp = new HashMap<Date,Double>() ;
			String[] fundsArray = productIds.split(",");
			String[] allocationRateArray = allocationRateList.split(",");
			HashMap<String, Double> fundInfoMp = new HashMap<String,Double>() ;
			String fundIds = "";
			for (int k = 0; k < fundsArray.length; k++) {
				String tmpProductId = fundsArray[k];
				if(tmpProductId.startsWith("FUND") == false||StringUtils.isNotBlank(tmpProductId)==false){
					continue;
				}
				FundInfo fund = getFundInfoByProductId(tmpProductId);
				String fundId = fund.getId();
				fundIds += fundId + ",";
				Double allocationRate = StrUtils.getDouble(allocationRateArray[k]);
				fundInfoMp.put(fundId, allocationRate);
			}
			
			fundIds = fundIds.substring(0, fundIds.length()-1);
			
			list =  this.getMulFundReturnRate(fundIds, frequencyType, "", langCode);
			if(null!=list&&!list.isEmpty()){
				for(int i=0;i<list.size();i++){
					String tmpFundId = list.get(i).getFundId();
					Double allocationRate = fundInfoMp.get(tmpFundId)*100;
					List<Date> marketDates = list.get(i).getMarketDates();
					List<Double> returnRates = list.get(i).getReturnRates();
					for(int k=0;k<marketDates.size();k++){
						Date md = marketDates.get(k);
						Double rate = returnRates.get(k);
						Double newRate = rate*allocationRate;
						if(marketMp.containsKey(md)){
							Double tmpRate = marketMp.get(md);
							tmpRate += newRate;
							marketMp.put(md, tmpRate);
						}else{
							marketMp.put(md, newRate);
						}
					}
				}
			}
			
			Collection<Date> keyset= marketMp.keySet();  
		     List<Date> list2 = new ArrayList<Date>(keyset);  
		       
		     //对key键值按字典升序排序  
		     Collections.sort(list2);  
			
			List<Date> marketDates = new ArrayList<Date>();
			List<Double> marketRate = new ArrayList<Double>();
			
			for (int i = 0; i < list2.size(); i++) {  
		        Date key = list2.get(i);
				Double val = marketMp.get(list2.get(i));
				marketDates.add(key);
				val = Double.parseDouble(new DecimalFormat("#.0000").format(val/100.0));
				marketRate.add(val);
				CoreFundNavVO vo = new CoreFundNavVO();
				String marketDateStr = DateUtil.getDateStr(key);
				vo.setMarketDateStr(marketDateStr);
				vo.setMarketDate(key);
				vo.setRateStr(val.toString());
				vo.setRate(val);
				voList.add(vo);
		     }  
			
		}

		return voList;
	}
	
	/**
	 * 获取指定时间范围内的多个基金的累计收益数据(合并一条线)
	 * 
	 * @param portfolioId
	 *            组合id
	 * @param frequencyType
	 *            频率 1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public CoreFundsReturnForChartsVO getMoreFundReturnRate(String fundIds,String allocationRateList,String frequencyType,String langCode) {
		CoreFundsReturnForChartsVO vo = new CoreFundsReturnForChartsVO();
		List<CoreFundsReturnForChartsVO> list = new ArrayList<CoreFundsReturnForChartsVO>();
		if (StringUtils.isNotBlank(frequencyType)) {
			//获取基准日期为最新的日期，然后向前推
			HashMap<Date, Double> marketMp = new HashMap<Date,Double>() ;
			String[] fundsArray = fundIds.split(",");
			String[] allocationRateArray = allocationRateList.split(",");
			HashMap<String, Double> fundInfoMp = new HashMap<String,Double>() ;
			for (int k = 0; k < fundsArray.length; k++) {
				String fundId = fundsArray[k];
				Double allocationRate = StrUtils.getDouble(allocationRateArray[k]);
				fundInfoMp.put(fundId, allocationRate);
			}
			
			list =  this.getMulFundReturnRate(fundIds, frequencyType, "", langCode);
			if(null!=list&&!list.isEmpty()){
				for(int i=0;i<list.size();i++){//每个基金
					String tmpFundId = list.get(i).getFundId();
					Double allocationRate = fundInfoMp.get(tmpFundId);
					List<Date> marketDates = list.get(i).getMarketDates();
					List<Double> returnRates = list.get(i).getReturnRates();
					for(int k=0;k<marketDates.size();k++){
						Date md = marketDates.get(k);
						Double rate = returnRates.get(k);
						Double newRate = rate*allocationRate;
						if(marketMp.containsKey(md)){
							Double tmpRate = marketMp.get(md);
							tmpRate += newRate;
							marketMp.put(md, tmpRate);
						}else{
							marketMp.put(md, newRate);
						}
					}
				}
			}
			
			Collection<Date> keyset= marketMp.keySet();  
		     List<Date> list2 = new ArrayList<Date>(keyset);  
		       
		     //对key键值按字典升序排序  
		     Collections.sort(list2);  
			
			List<Date> marketDates = new ArrayList<Date>();
			List<Double> marketRate = new ArrayList<Double>();
			
			for (int i = 0; i < list2.size(); i++) {  
		         Date key = list2.get(i);
					Double val = marketMp.get(list2.get(i));
					marketDates.add(key);
					val = Double.parseDouble(new DecimalFormat("#.0000").format(val/100.0));
					marketRate.add(val);
		     }  
			
			vo.setMarketDates(marketDates);
			vo.setReturnRates(marketRate);
		}

		return vo;
	}
	
	// 获取基金日期范围内累计收益
	public CoreFundsReturnForChartsVO genFundsReturnRate(int diffDay,Date startDt,String fundIds,String allocationRateList, Calendar calendar, int diffType,String langCode) {
		CoreFundsReturnForChartsVO  newList = new CoreFundsReturnForChartsVO();
		List<CoreMoreFundRateVO> list = new ArrayList<CoreMoreFundRateVO>();
			String[] fundsArray = fundIds.split(",");
			String[] allocationRateArray = allocationRateList.split(",");
			//几个基金中最开始的一条基金的日期
			Date firstFundMarketDate = null;
			//循环基金，把每个基金的最初一条行情存进来
			HashMap<String, FundMarket> firstMap = new HashMap<String,FundMarket>() ;
			//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
			HashMap<String, FundMarket> beginMap = new HashMap<String,FundMarket>() ;
			
			HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
			HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
			for (int k = 0; k < fundsArray.length; k++) {
				String fundId = fundsArray[k];
				//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
				FundMarket geginFundMarket = getFundMarketInfo(fundId,startDt);
				if(null==geginFundMarket){
					 geginFundMarket = getPrevFundMarketInfo(fundId,startDt);
					 if(null!=geginFundMarket){
						 beginMap.put(fundId, geginFundMarket);
					 }
				} else beginMap.put(fundId, geginFundMarket);
				// 获取该基金最开始第一条的行情数据
				FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
				firstMap.put(fundId, firstFundMarket);
				if(firstFundMarketDate!=null){
					Date tmpDate = firstFundMarket.getMarketDate();
					long difftime=(tmpDate.getTime()-firstFundMarketDate.getTime());
					if(difftime<0)firstFundMarketDate = tmpDate;
				} else firstFundMarketDate = firstFundMarket.getMarketDate();
				//每只基金通过时间范围把数据筛选出来
				Calendar nowCalendar = Calendar.getInstance();
				nowCalendar.add(Calendar.DATE, -1);
				Date endDt = nowCalendar.getTime();
				List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,startDt,endDt);
				HashMap<String,Double> templist =  new HashMap<String,Double>() ;
				for(FundMarket each :fundMarketList){
					String dateStr = DateUtil.getDateStr(each.getMarketDate());
					templist.put(dateStr, each.getNav());
				}
				allFundMarketMap.put(fundId, templist);
				//存放该基金所有行情数据
				List<FundMarket> allfundMarketList = this.getAlFundMarket(fundId);
				allFundMarketMapList.put(fundId, allfundMarketList);
			}
			//如果只有一只基金，获取其名称
			if(fundsArray.length==1){
				if("en".equals(langCode)){
					FundInfoEn fundInfo = findFundInfoEnById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("sc".equals(langCode)){
					FundInfoSc fundInfo = findFundInfoScById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("tc".equals(langCode)){
					FundInfoTc fundInfo = findFundInfoTcById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
			}
			
			List<Date> marketDates = new ArrayList<Date>();
			List<Double> returnRates = new ArrayList<Double>();
			for (int i = 0; i < diffDay; i++) {// 判断每一天是否有数据
				//判断日期是否小于最小的基金日期，如果是，则不用分析，重新循环
				long superDiffTime=(calendar.getTime().getTime()-firstFundMarketDate.getTime());
				if(superDiffTime<0){
					calendar.add(calendar.DATE, 1);
					continue;
				}
				if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)||Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
					calendar.add(calendar.DATE, 1);
					continue;
				}
//				if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
//					calendar.add(calendar.DATE, 2);
//					i++;
//					continue;
//				}
				marketDates.add(calendar.getTime());
				Double totalRate = 0.00;
				//Double totalDayPl = 0.00;//每日盈亏
				for (int k = 0; k < fundsArray.length; k++) {// 所包含的基金，分析该基金
					// 获取该基金在指定的范围内的累计收益
					// 计算日期范围
					String fundId = fundsArray[k];
					Double allocationRate = StrUtils.getDouble(allocationRateArray[k]);
					// 先日期为基准，每个日期循环里面多个基金
					Double navNow = 0.00;
					Double navFirst = 0.00;
					Double cumprefRate = 0.00;
					//String rateStr = "0.00";
					// 组合该日期，获取该日期的基金净值
					Date marketDate = calendar.getTime();
					// 获取该日期的基金数据，如果没有，则取最新的前面一条
					HashMap<String,Double> templist = allFundMarketMap.get(fundId);
					navNow =  templist.get(DateUtil.getDateStr(marketDate));
					//FundMarket vo = getFundMarketInfo(fundId, marketDate);// 73.9048
					if(navNow==null){ 
						//循环分析
						List<FundMarket> allFundMarket = allFundMarketMapList.get(fundId);
						//System.out.println(allFundMarket.size());
						for(int p=0;p<allFundMarket.size();p++){
							FundMarket vo = allFundMarket.get(p);
							if (null != vo) {
								Date vomarketDate = vo.getMarketDate();
								long difftime=(marketDate.getTime()-vomarketDate.getTime());
								if(difftime>0){
									navNow = vo.getNav();
								}
								 
							}
						}

					}
					// 获取该基金最开始第一条的行情数据
					FundMarket firstFundMarket = beginMap.get(fundId);//firstMap
//					FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
					if (null != firstFundMarket) {
						navFirst = (Double) firstFundMarket.getNav();// 基金净值,最开始的净值
					}
					if (navNow!=null && navFirst > 0) {
						Double tempCumprefRate = (navNow - navFirst) / navFirst;
						tempCumprefRate = (tempCumprefRate*allocationRate)/100;
						BigDecimal bg = new BigDecimal(tempCumprefRate);
						cumprefRate = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					// 该组合的累计收益率
					totalRate += cumprefRate;
					//cumprefRate = Double.parseDouble(new DecimalFormat("#.0000").format(cumprefRate));
					
				}
				totalRate = Double.parseDouble(new DecimalFormat("#.0000").format(totalRate));
				CoreMoreFundRateVO pVo = new CoreMoreFundRateVO();//用于存放基金最终 生成的单个实体数据
				pVo.setMarketDate(calendar.getTime());
				if (i == 0) {
					//pVo.setReturnRate(0.00);
					totalRate = 0.00;
				}
				else {
					pVo.setReturnRate(totalRate);
				}
				if (null != pVo) list.add(pVo);
				// 日期递增
				if(returnRates.size()==0)totalRate=0.00;
				returnRates.add(totalRate);
				calendar.add(calendar.DATE, diffType);// 1表示1天，7表示一周
			}
	
			newList.setMarketDates(marketDates);
			newList.setReturnRates(returnRates);
		return newList;
	}
	
	// 获取单个基金日期范围内累计收益
	public CoreFundsReturnForChartsVO genSigleFundsReturnRate(int diffDay,String fundIds, Calendar calendar,Date latestDate,String langCode) {
		Date startDt = calendar.getTime();
		CoreFundsReturnForChartsVO  newList = new CoreFundsReturnForChartsVO();
		List<CoreMoreFundRateVO> list = new ArrayList<CoreMoreFundRateVO>();
			String[] fundsArray = fundIds.split(",");
			//几个基金中最开始的一条基金的日期
			Date firstFundMarketDate = null;
			//循环基金，把每个基金的最初一条行情存进来
			HashMap<String, FundMarket> firstMap = new HashMap<String,FundMarket>() ;
			//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
			HashMap<String, FundMarket> beginMap = new HashMap<String,FundMarket>() ;
			
			HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
			HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
			for (int k = 0; k < fundsArray.length; k++) {
				String fundId = fundsArray[k];
				
				
				
				
				
				// 获取该基金最开始第一条的行情数据
				FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
				firstMap.put(fundId, firstFundMarket);
				if(firstFundMarketDate!=null){
					Date tmpDate = firstFundMarket.getMarketDate();
					long difftime=(tmpDate.getTime()-firstFundMarketDate.getTime());
					if(difftime<0)firstFundMarketDate = tmpDate;
				} else firstFundMarketDate = firstFundMarket.getMarketDate();
				
				//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
				FundMarket geginFundMarket = getFundMarketInfo(fundId,startDt);
				if(null==geginFundMarket){
					 geginFundMarket = getPrevFundMarketInfo(fundId,startDt);
					 if(null!=geginFundMarket){
						 beginMap.put(fundId, geginFundMarket);
					 }
				} else beginMap.put(fundId, geginFundMarket);
				
				if((calendar.getTime()).getTime()<firstFundMarket.getMarketDate().getTime()){
					String firstDate = DateUtil.getDateStr(calendar.getTime());
					geginFundMarket = firstFundMarket;
					beginMap.put(fundId, geginFundMarket);
				}
				
				
				
				//每只基金通过时间范围把数据筛选出来
				//Calendar nowCalendar = Calendar.getInstance();
				//nowCalendar.add(Calendar.DATE, -1);
				//Date endDt = nowCalendar.getTime();
				List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,startDt,latestDate);
				HashMap<String,Double> templist =  new HashMap<String,Double>() ;
				for(FundMarket each :fundMarketList){
					String dateStr = DateUtil.getDateStr(each.getMarketDate());
					templist.put(dateStr, each.getNav());
				}
				allFundMarketMap.put(fundId, templist);
				//存放该基金所有行情数据
				List<FundMarket> allfundMarketList = this.getAlFundMarket(fundId);
				allFundMarketMapList.put(fundId, allfundMarketList);
			}
			//如果只有一只基金，获取其名称
			if(fundsArray.length==1){
				if("en".equals(langCode)){
					FundInfoEn fundInfo = findFundInfoEnById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("sc".equals(langCode)){
					FundInfoSc fundInfo = findFundInfoScById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("tc".equals(langCode)){
					FundInfoTc fundInfo = findFundInfoTcById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
			}
			
			List<Date> marketDates = new ArrayList<Date>();
			List<Double> returnRates = new ArrayList<Double>();
			for (int i = 0; i <= diffDay; i++) {// 判断每一天是否有数据
				//判断日期是否小于最小的基金日期，如果是，则不用分析，重新循环
				long superDiffTime=(calendar.getTime().getTime()-firstFundMarketDate.getTime());
				if(superDiffTime<0){
					calendar.add(calendar.DATE, 1);
					continue;
				}
				if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)||Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
					calendar.add(calendar.DATE, 1);
					continue;
				}
//				if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
//					calendar.add(calendar.DATE, 2);
//					i++;
//					continue;
//				}
				marketDates.add(calendar.getTime());
				Double totalRate = 0.00;
				//Double totalDayPl = 0.00;//每日盈亏
				for (int k = 0; k < fundsArray.length; k++) {// 所包含的基金，分析该基金
					// 获取该基金在指定的范围内的累计收益
					// 计算日期范围
					String fundId = fundsArray[k];
					
					// 先日期为基准，每个日期循环里面多个基金
					Double navNow = 0.00;
					Double navFirst = 0.00;
					Double cumprefRate = 0.00;
					//String rateStr = "0.00";
					// 组合该日期，获取该日期的基金净值
					Date marketDate = calendar.getTime();
					// 获取该日期的基金数据，如果没有，则取最新的前面一条
					HashMap<String,Double> templist = allFundMarketMap.get(fundId);
					navNow =  templist.get(DateUtil.getDateStr(marketDate));
					//FundMarket vo = getFundMarketInfo(fundId, marketDate);// 73.9048
					if(navNow==null){ 
						//循环分析
						List<FundMarket> allFundMarket = allFundMarketMapList.get(fundId);
						//System.out.println(allFundMarket.size());
						for(int p=0;p<allFundMarket.size();p++){
							FundMarket vo = allFundMarket.get(p);
							if (null != vo) {
								Date vomarketDate = vo.getMarketDate();
								long difftime=(marketDate.getTime()-vomarketDate.getTime());
								if(difftime>0){
									navNow = vo.getNav();
								}
								 
							}
						}

					}
					// 获取该基金最开始第一条的行情数据
					FundMarket firstFundMarket = beginMap.get(fundId);//firstMap
//					FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
					if (null != firstFundMarket) {
						navFirst = (Double) firstFundMarket.getNav();// 基金净值,最开始的净值
					}
					if (navNow!=null && navFirst > 0) {
						Double tempCumprefRate = (navNow - navFirst) / navFirst;
						//tempCumprefRate = (tempCumprefRate*allocationRate)/100;
						BigDecimal bg = new BigDecimal(tempCumprefRate);
						cumprefRate = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					// 该组合的累计收益率
					totalRate += cumprefRate;
					//cumprefRate = Double.parseDouble(new DecimalFormat("#.0000").format(cumprefRate));
					
				}
				totalRate = Double.parseDouble(new DecimalFormat("#.0000").format(totalRate));
				CoreMoreFundRateVO pVo = new CoreMoreFundRateVO();//用于存放基金最终 生成的单个实体数据
				pVo.setMarketDate(calendar.getTime());
				if (i == 0) {
					//pVo.setReturnRate(0.00);
					totalRate = 0.00;
				}
				else {
					pVo.setReturnRate(totalRate);
				}
				if (null != pVo) list.add(pVo);
				// 日期递增
				if(returnRates.size()==0)totalRate=0.00;
				returnRates.add(totalRate);
				calendar.add(calendar.DATE, 1);// 1表示1天，7表示一周
			}
	
			newList.setMarketDates(marketDates);
			newList.setReturnRates(returnRates);
		return newList;
	}
	
	
	
	
	
	
	/**
	 * 获取指定时间范围内的基金累计收益数据-多个拆线
	 * @param fundId 基金
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CoreFundsReturnForChartsVO> getMulFundReturnRate(String fundIds,String frequencyType,String currency,String langCode){
		List<CoreFundsReturnForChartsVO> list = new ArrayList<CoreFundsReturnForChartsVO>();
		CoreFundsReturnForChartsVO vo = new CoreFundsReturnForChartsVO();
		StringBuilder hql=new StringBuilder();
		hql.append(" from FundMarket r ");
		hql.append("  where r.fund.id=? ");
		//计算日期范围
		if (StringUtils.isNotBlank(frequencyType)) {
			//先获取最新的一条净值的时间，以它做为时间基准，由于几个基金，取最大的
			String latestFundId = "";
			Date latestDate = null;
			Date earliestDate = null;
			String[] fundsArray = fundIds.split(",");
			for (int k = 0; k < fundsArray.length; k++) {
				String fundId = fundsArray[k];
				FundMarket fm = this.getLatestFundMarketInfo(fundId);
				Date tempDate = fm.getMarketDate();
				if(latestDate == null){
					latestDate = tempDate;
					latestFundId = fm.getFund().getId();
				}
				else{
					if(latestDate.getTime()<tempDate.getTime()){//如果大于原有的，则用新的代替原有的
						latestDate = tempDate;
						latestFundId = fm.getFund().getId();
					}
				}
				
				
				
				
				fm = this.getFundFirstMarketInfo(fundId);
				Date tempDate2 = fm.getMarketDate();
				if(earliestDate == null){
					earliestDate = tempDate2;
				} else{
					if(earliestDate.getTime()>tempDate2.getTime())earliestDate=tempDate2;
				}
				
			}
//			if(!"".equals(latestFundId)){
//				FundMarket fm = this.getFundFirstMarketInfo(latestFundId);
//				if(null!=fm){
//					earliestDate = fm.getMarketDate();
//				} else{
//					if(earliestDate.getTime()<tempDate.getTime()){
//						
//					}
//				}
//			}
			
			
				//Date latestDate = fm.getMarketDate();//以该时间做为基准，设置时间范围
				Calendar calendar = Calendar.getInstance();
	        	calendar.setTime(latestDate);//给日历设定它的基准时间 ，比如：2017/4/27
	        	long diffDay = 0;
	        	
	        	if ("1W".equals(frequencyType)){
	        		calendar.setTime(latestDate);
					calendar.add(Calendar.DATE, -7);//一周的第一天
					Date stopDate = calendar.getTime();
					
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.DATE, -7);//一周的第一天
						stopDate = calendar.getTime();
						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
				}
//				else if ("2W".equals(frequencyType)){
//					for (int k = 0; k < fundsArray.length; k++) {
//						calendar.setTime(latestDate);
//						calendar.add(Calendar.DATE, -14);
//						Date stopDate = calendar.getTime();
//						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
//						String fundId = fundsArray[k];
//						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,null);
//						list.add(vo);
//					}
//					
//				}
				else if ("1M".equals(frequencyType)){
					calendar.setTime(latestDate);
					calendar.add(Calendar.MONTH, -1);
					Date stopDate = calendar.getTime();
					if(earliestDate.getTime()>stopDate.getTime()){
						stopDate=earliestDate;
					}
					
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.MONTH, -1);
						 stopDate = calendar.getTime();
						if(earliestDate.getTime()>stopDate.getTime()){
							stopDate=earliestDate;
						}
						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
					//
					for(int i=0;i<=diffDay;i++){
						
					}
				}
				else if ("3M".equals(frequencyType)){
					
					calendar.setTime(latestDate);
					calendar.add(Calendar.MONTH, -3);
					Date stopDate = calendar.getTime();
					if(earliestDate.getTime()>stopDate.getTime()){
						stopDate=earliestDate;
					}
					
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.MONTH, -3);
						stopDate = calendar.getTime();
						if(earliestDate.getTime()>stopDate.getTime()){
							stopDate=earliestDate;
						}
						
						
						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
				}
				else if ("6M".equals(frequencyType)){
					
					calendar.setTime(latestDate);
					calendar.add(Calendar.MONTH, -6);
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.MONTH, -6);
						//Date stopDate = earliestDate;
						calendar.setTime(stopDate);
						
						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
					

				}
				else if ("YTD".equals(frequencyType)){
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					//获取当年第一天
					Date firstDate = DateUtil.getCurrYearFirst();
					
						
						//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
						for (int k = 0; k < fundsArray.length; k++) {
							
							
							//calendar.add(Calendar.DATE, -(int)diffDay);

							 stopDate = earliestDate;
							calendar.setTime(stopDate);
							//diffDay = DateUtil.daysBetween(firstDate,calendar.getTime());
							diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
							String fundId = fundsArray[k];
							vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
							list.add(vo);
						}
						
				}
				else if ("1Y".equals(frequencyType)){
					calendar.setTime(latestDate);
					calendar.add(Calendar.YEAR, -1);
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.YEAR, -1);
						stopDate = earliestDate;
						calendar.setTime(stopDate);
//						if(earliestDate.getTime()<stopDate.getTime()){
//							stopDate=earliestDate;
//						}
						
						

						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(stopDate),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
				}
				else if ("3Y".equals(frequencyType)){
					calendar.setTime(latestDate);
					calendar.add(Calendar.YEAR, -3);
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.YEAR, -3);
						 stopDate = earliestDate;
						calendar.setTime(stopDate);
//						if(earliestDate.getTime()<stopDate.getTime()){
//							stopDate=earliestDate;
//						}
						
						

						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(stopDate),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
				}
				else if ("5Y".equals(frequencyType)){
					calendar.setTime(latestDate);
					calendar.add(Calendar.YEAR, -5);
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					for (int k = 0; k < fundsArray.length; k++) {
						calendar.setTime(latestDate);
						calendar.add(Calendar.YEAR, -5);
						 stopDate = earliestDate;
						calendar.setTime(stopDate);
//						if(earliestDate.getTime()<stopDate.getTime()){
//							stopDate=earliestDate;
//						}
						
						

						diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(stopDate),DateUtil.getDateStr(latestDate));
						String fundId = fundsArray[k];
						vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
						list.add(vo);
					}
				}
				else if ("LAUNCH".equals(frequencyType)){//return_period_code_LAUNCH
					Date stopDate = earliestDate;
					calendar.setTime(stopDate);
					diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
					
					HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
					HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
					List<Date> noDataList = new ArrayList<Date>();
					for (int k = 0; k < fundsArray.length; k++) {
						String fundId = fundsArray[k];

						List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,earliestDate,latestDate);
						HashMap<String,Double> templist =  new HashMap<String,Double>() ;
						for(FundMarket each :fundMarketList){
							String dateStr = DateUtil.getDateStr(each.getMarketDate());
							templist.put(dateStr, each.getNav());
						}
						allFundMarketMap.put(fundId, templist);
					}

					for (int i = 0; i <= diffDay; i++) {
						boolean havedData = false;
						for (int k = 0; k < fundsArray.length; k++) {
								String fundId = fundsArray[k];
								HashMap<String,Double> temp = allFundMarketMap.get(fundId);
								
								Iterator iter = temp.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String key = (String)entry.getKey();
									if(DateUtil.getDateStr(calendar.getTime()).equals(key)){
										havedData = true;
										break;
									}
								}	
						}
						if(havedData == false){
							noDataList.add(calendar.getTime());
						}
						
						calendar.add(calendar.DATE, 1);
					}
					//获取他创建的日期
					Date firstDate = DateUtil.getCurrYearFirst();
					
						
						//list = this.genFundReturnRate((int)diffDay,fundId,calendar,currency);
						for (int k = 0; k < fundsArray.length; k++) {
							
							
							//calendar.add(Calendar.DATE, -(int)diffDay);

							 stopDate = earliestDate;
							calendar.setTime(stopDate);
							//diffDay = DateUtil.daysBetween(firstDate,calendar.getTime());
							diffDay = DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(calendar.getTime()),DateUtil.getDateStr(latestDate));
							String fundId = fundsArray[k];
							vo = this.genMulFundsReturnRate((int)diffDay, fundId,calendar , latestDate,stopDate,langCode,noDataList);
							list.add(vo);
						}
						
				}
			
			//获取相关日期数
	        	for(CoreFundsReturnForChartsVO each : list){
	        		List<Date> marketdates =  each.getMarketDates();
	        	}
	        	
	        	
			
		}
		return list;
	}
	// 获取多个基金日期范围内累计收益，需要补齐
	public CoreFundsReturnForChartsVO genMulFundsReturnRate(int diffDay,String fundIds, Calendar calendar,Date latestDate,Date stopDate,String langCode,List<Date> noDataList) {
		Date startDt = calendar.getTime();
		CoreFundsReturnForChartsVO  newList = new CoreFundsReturnForChartsVO();
		List<CoreMoreFundRateVO> list = new ArrayList<CoreMoreFundRateVO>();
			String[] fundsArray = fundIds.split(",");
			//几个基金中最开始的一条基金的日期
			Date firstFundMarketDate = null;
			//循环基金，把每个基金的最初一条行情存进来
			HashMap<String, FundMarket> firstMap = new HashMap<String,FundMarket>() ;
			//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
			HashMap<String, FundMarket> beginMap = new HashMap<String,FundMarket>() ;
			
			HashMap<String, HashMap<String,Double>> allFundMarketMap = new HashMap<String,HashMap<String,Double>>() ;
			HashMap<String, List<FundMarket>> allFundMarketMapList = new HashMap<String,List<FundMarket>>() ;
			for (int k = 0; k < fundsArray.length; k++) {
				String fundId = fundsArray[k];
				newList.setFundId(fundId);
				
				
				
				
				// 获取该基金最开始第一条的行情数据
				FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
				firstMap.put(fundId, firstFundMarket);
				if(firstFundMarketDate!=null){
					Date tmpDate = firstFundMarket.getMarketDate();
					long difftime=(tmpDate.getTime()-firstFundMarketDate.getTime());
					if(difftime<0)firstFundMarketDate = tmpDate;
				} else firstFundMarketDate = firstFundMarket.getMarketDate();
				
				//获取范围起始日期内的第一条基金，如果没有，则向前拿最近的一条
				FundMarket geginFundMarket = getFundMarketInfo(fundId,startDt);
				if(null==geginFundMarket){
					 geginFundMarket = getPrevFundMarketInfo(fundId,startDt);
					 if(null!=geginFundMarket){
						 beginMap.put(fundId, geginFundMarket);
					 }
				} else beginMap.put(fundId, geginFundMarket);
				
				if((calendar.getTime()).getTime()<firstFundMarket.getMarketDate().getTime()){
					String firstDate = DateUtil.getDateStr(calendar.getTime());
					geginFundMarket = firstFundMarket;
					beginMap.put(fundId, geginFundMarket);
				}
				
				
				
				//每只基金通过时间范围把数据筛选出来
				//Calendar nowCalendar = Calendar.getInstance();
				//nowCalendar.add(Calendar.DATE, -1);
				//Date endDt = nowCalendar.getTime();
				List<FundMarket> fundMarketList = this.getFundMarketByTimeScrope(fundId,startDt,latestDate);
				HashMap<String,Double> templist =  new HashMap<String,Double>() ;
				for(FundMarket each :fundMarketList){
					String dateStr = DateUtil.getDateStr(each.getMarketDate());
					templist.put(dateStr, each.getNav());
				}
				allFundMarketMap.put(fundId, templist);
				//存放该基金所有行情数据
				List<FundMarket> allfundMarketList = this.getAlFundMarket(fundId);
				allFundMarketMapList.put(fundId, allfundMarketList);
			}
			//如果只有一只基金，获取其名称
			if(fundsArray.length==1){
				if("en".equals(langCode)){
					FundInfoEn fundInfo = findFundInfoEnById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("sc".equals(langCode)){
					FundInfoSc fundInfo = findFundInfoScById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
				else if("tc".equals(langCode)){
					FundInfoTc fundInfo = findFundInfoTcById(fundsArray[0]);
					newList.setFundName(fundInfo.getFundName());
					newList.setFundId(fundsArray[0]);
				}
			}
			
			List<Date> marketDates = new ArrayList<Date>();
			List<Double> returnRates = new ArrayList<Double>();
			for (int i = 0; i <= diffDay; i++) {// 判断每一天是否有数据
				//判断日期是否小于最小的基金日期，如果是，则不用分析，重新循环 2017-05-08 2017-05-05 
//				if(calendar.getTime().getTime()-stopDate.getTime()<0){ 
//					calendar.add(calendar.DATE, 1);
//					continue;
//				}
//				long superDiffTime=(calendar.getTime().getTime()-firstFundMarketDate.getTime());
//				if(superDiffTime<0){
//					if(calendar.getTime().getTime()-stopDate.getTime()==0){
//						
//					} else{
//						calendar.add(calendar.DATE, 1);
//						continue;
//					}
//					
//				}
				
//				if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)||Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
//					calendar.add(calendar.DATE, 1);
//					continue;
//				}
				
				if(noDataList.contains(calendar.getTime())){
					calendar.add(calendar.DATE, 1);
					continue;
				}
				
				marketDates.add(calendar.getTime());
				Double totalRate = 0.00;
				//Double totalDayPl = 0.00;//每日盈亏
				for (int k = 0; k < fundsArray.length; k++) {// 所包含的基金，分析该基金
					// 获取该基金在指定的范围内的累计收益
					// 计算日期范围
					String fundId = fundsArray[k];
					
					// 先日期为基准，每个日期循环里面多个基金
					Double navNow = 0.00;
					Double navFirst = 0.00;
					Double cumprefRate = 0.00;
					//String rateStr = "0.00";
					// 组合该日期，获取该日期的基金净值
					Date marketDate = calendar.getTime();
					// 获取该日期的基金数据，如果没有，则取最新的前面一条
					HashMap<String,Double> templist = allFundMarketMap.get(fundId);
					navNow =  templist.get(DateUtil.getDateStr(marketDate));
					//FundMarket vo = getFundMarketInfo(fundId, marketDate);// 73.9048
					if(navNow==null){ 
						//循环分析
						List<FundMarket> allFundMarket = allFundMarketMapList.get(fundId);
						//System.out.println(allFundMarket.size());
						for(int p=0;p<allFundMarket.size();p++){
							FundMarket vo = allFundMarket.get(p);
							if (null != vo) {
								Date vomarketDate = vo.getMarketDate();
								long difftime=(marketDate.getTime()-vomarketDate.getTime());
								if(difftime>0){
									navNow = vo.getNav();
								}
								 
							}
						}

					}
					// 获取该基金范围开始的行情数据
					FundMarket firstFundMarket = beginMap.get(fundId);//firstMap
//					FundMarket firstFundMarket = getFundFirstMarketInfo(fundId);
					if (null != firstFundMarket) {
						navFirst = (Double) firstFundMarket.getNav();// 基金净值,最开始的净值
					}
					if (navNow!=null && navFirst > 0) {
						//Double tempCumprefRate = (navNow - navFirst) / navFirst;
						//tempCumprefRate = (tempCumprefRate*allocationRate)/100;
						//BigDecimal bg = new BigDecimal(tempCumprefRate);
						//cumprefRate = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
						cumprefRate = new BigDecimal(((navNow - navFirst) / navFirst)*100.0).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					// 该组合的累计收益率
					totalRate += cumprefRate;
					//cumprefRate = Double.parseDouble(new DecimalFormat("#.0000").format(cumprefRate));
					
				}
				totalRate = Double.parseDouble(new DecimalFormat("#.0000").format(totalRate));
				CoreMoreFundRateVO pVo = new CoreMoreFundRateVO();//用于存放基金最终 生成的单个实体数据
				pVo.setMarketDate(calendar.getTime());
				if (i == 0) {
					//pVo.setReturnRate(0.00);
					totalRate = 0.00;
				}
				else {
					pVo.setReturnRate(totalRate);
				}
				if (null != pVo) list.add(pVo);
				// 日期递增
				if(returnRates.size()==0)totalRate=0.00;
				returnRates.add(totalRate);
				calendar.add(calendar.DATE, 1);// 1表示1天，7表示一周
			}
	
			newList.setMarketDates(marketDates);
			newList.setReturnRates(returnRates);
		return newList;
	}
	
	/**
	 * 转换净值的货币
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	public Double traCurrency(Double nav,String from,String to ){
		if(from.equals(to)&&StringUtils.isNotBlank(from)&&StringUtils.isNotBlank(to))
			return this.getNumByCurrency(nav, from,to);
		else
			return nav;
	}
	
	/**
	 * 通过FundID,日期获取基金的行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getFundMarketInfo(String productId,Date date) {
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
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过FundID,时间范围获取基金的范围内的行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private List<FundMarket> getFundMarketByTimeScrope(String fundId,Date startDt,Date endDt) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate BETWEEN ? AND ? order by r.marketDate asc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		params.add(startDt);
		params.add(endDt);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 通过FundID,获取所有行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private List<FundMarket> getAlFundMarket(String fundId) {
		String sql = "from FundMarket r where r.fund.id=?  order by r.marketDate asc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 通过FundID,日期获取基金的行情数据，如果该日期无数据，则通过获取比它最早的有数据的那条记录
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getPrevFundMarketInfo(String productId,Date date) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate<? order by r.marketDate desc";
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
	 * 通过ID查找一条基金附加简体中文信息
	 * @param id
	 * @return
	 */
	public FundInfoSc findFundInfoScById(String id){
		FundInfoSc info = (FundInfoSc) baseDao.get(FundInfoSc.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条基金附加繁体中文信息
	 * @param id
	 * @return
	 */
	public FundInfoTc findFundInfoTcById(String id){
		FundInfoTc info = (FundInfoTc) baseDao.get(FundInfoTc.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条基金附加英文信息
	 * @param id
	 * @return
	 */
	public FundInfoEn findFundInfoEnById(String id){
		FundInfoEn info = (FundInfoEn) baseDao.get(FundInfoEn.class, id);
		return info;
	}
	
	/**
	 * 根据产品ID获取基金信息
	 */
	public FundInfo getFundInfoByProductId(String productId){
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
}
