package com.fsll.wmes.fund.service.impl;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.ejb.Local;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.FundMarketMonth;
import com.fsll.wmes.entity.FundMarketWeek;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.service.FundMarketService;
import com.fsll.wmes.fund.service.FundReturnService;
import com.fsll.wmes.fund.vo.AipVO;
import com.fsll.wmes.fund.vo.ChartDataVO;
import com.fsll.wmes.fund.vo.FundCumulativePerformanceDataVO;
import com.fsll.wmes.fund.vo.FundIncomePercentageVO;
import com.fsll.wmes.fund.vo.FundMarketDataVO;

/**
 * 基金净值信息查询服务接口实现
 * @author michael
 * @date 2016-6-20
 */
@Service("fundMarketService")
//@Transactional
public class FundMarketServiceImpl extends BaseService implements FundMarketService {

	@Autowired
	private FundReturnService fundReturnService;
	
	@Autowired
	private FundInfoService fundInfoService;
	
	/**
	 * 获取基金价格行情信息列表
	 * @param fundId 基金id
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param toCurrency 目标货币
	 * @param langCode 语言
	 * @return	GeneralDataVO	价格行情数据
	 */
	//@Transactional(readOnly=true)
	public List<FundMarketDataVO> findFundMarketList(String fundId, String period, String startDate, String endDate, String toCurrency, String langCode) {
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(null);
		jsonPaging.setOrder("asc");
		jsonPaging.setSort("market_date");
		jsonPaging = findFundMarketList(jsonPaging, fundId, period, startDate, endDate, langCode, toCurrency);
		
		return jsonPaging.getList();
	}
	
	/**
	 * 获取基金净值分页
	 * @author Michael
	 * @param jsonPaging 分页对象
	 * @param fundId 基金id
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param langCode 语言
	 * @param toCurrency 目标货币
	 * @return JsonPaging	基金净值分页数据
	 */
	public JsonPaging findFundMarketList(JsonPaging jsonPaging,String fundId, String period, String startDate, String endDate, String langCode, String toCurrency){
		String tableName = "FundMarket";//日行情
		if (null!=period){
			if ("week".equals(period))
				tableName = "FundMarketWeek";//周行情
			else if ("month".equals(period))
				tableName = "FundMarketMonth";//月行情
		}
		
		String hql = "select t.fund.id,i.fundName,t.openPrice,t.lowPrice,t.hightPrice,t.closePrice,t.nav,t.accNav,t.marketDate,t.returnRate,i.fundCurrency,i.fundCurrencyCode from "+tableName+" t ";
		hql += " left join "+this.getLangString("FundInfo", langCode);		
		hql += " i ON i.id=t.fund.id where t.isValid='1' ";
		List params = new ArrayList();
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		
		if(null!=startDate && !"".equals(startDate)){
			hql += "and t.marketDate>=? ";
			params.add(DateUtil.getDate(startDate,CoreConstants.DATE_FORMAT));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += "and t.marketDate<=? ";
			params.add(DateUtil.getDate(endDate,CoreConstants.DATE_FORMAT));
		}

		jsonPaging.setSort("t.marketDate");

		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		
		List<FundMarketDataVO> list = new ArrayList<FundMarketDataVO>();
		List voList = jsonPaging.getList();
		for(int x=0;x<voList.size();x++){
			FundMarketDataVO vo = new FundMarketDataVO();
			Object[] objs = (Object[])voList.get(x);
			vo.setFundId(StrUtils.getString(objs[0]));
			vo.setFundName(StrUtils.getString(objs[1]));
			vo.setOpenPrice(StrUtils.getString(objs[2]));
			vo.setLowPrice(StrUtils.getString(objs[3]));
			vo.setHightPrice(StrUtils.getString(objs[4]));
			vo.setClosePrice(StrUtils.getString(objs[5]));
			vo.setNav(StrUtils.getString(objs[6]));
			vo.setAccNav(StrUtils.getString(objs[7]));
			vo.setMarketDate(StrUtils.getString(objs[8]));
			vo.setReturnRate(StrUtils.getString(objs[9]));
			vo.setOrgCurrency(StrUtils.getString(objs[11]));
			vo.setDestCurrency(toCurrency);
			
			//货币转换
			String fromCurrency = vo.getOrgCurrency();
			if(null!=toCurrency && !"".equals(toCurrency)){
				double rate = 0;
				if (!"".equals(fromCurrency) && !"".equals(toCurrency)){
					try {
						rate = fundInfoService.findExchangeRate(fromCurrency, toCurrency).getRate();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				if (rate>0){
					vo.setOpenPrice(String.valueOf(StrUtils.getDoubleVal(vo.getOpenPrice())*rate));
					vo.setHightPrice(String.valueOf(StrUtils.getDoubleVal(vo.getHightPrice())*rate));
					vo.setLowPrice(String.valueOf(StrUtils.getDoubleVal(vo.getLowPrice())*rate));
					vo.setClosePrice(String.valueOf(StrUtils.getDoubleVal(vo.getClosePrice())*rate));
					vo.setNav(String.valueOf(StrUtils.getDoubleVal(vo.getNav())*rate));
					vo.setAccNav(String.valueOf(StrUtils.getDoubleVal(vo.getAccNav())*rate));
//					vo.setReturnRate(String.valueOf(StrUtils.getDoubleVal(vo.getReturnRate())*rate));//比率不用转换
				}else vo.setDestCurrency(fromCurrency);//获取兑换率失败，不转换货币
			}
			
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取基金净值图表数据
	 * @author scshi
	 * @param fundId 资金id
	 * @param dataType 数据类型
	 * @param cycle 分析周期：D-日；W-周；M-月
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param toCurrency 转换目标货币
	 * @param langCode 显示语言
	 * @return	<List>ChartDataVO	基金图表数据
	 */
	public ChartDataVO fundChartData(String fundId, String dataType, String cycle, String period, String addiData, String toCurrency,String langCode) {
		int addData = StrUtils.getInt(addiData);
		period = StrUtils.getString(period);
		String startDate = DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr();
		if ("1WK".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("1Mth".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("3Mth".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("6Mth".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("1Yr".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("2Yr".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
		if ("3Yr".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("5Yr".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("10Yr".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
		if ("YTD".equals(period)) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来

		cycle = StrUtils.getString(cycle);
		if ("W".equals(cycle)){ 
			cycle = "week";//周行情
			startDate = DateUtil.getDateStr(DateUtil.addDate(startDate,Calendar.DATE,-addData*7));
		}
		else if ("M".equals(cycle)){ 
			cycle = "month";//月行情
			startDate = DateUtil.getDateStr(DateUtil.addDate(startDate,Calendar.MONTH,-addData));
		}

		else{ 
			cycle = "day";
			startDate = DateUtil.getDateStr(DateUtil.addDate(startDate,Calendar.DATE,-addData));
		}
		//所有数据，不限制日期范围
		if ("All".equals(period)){
			startDate = "";
			endDate = "";
		}
		
		List<FundMarketDataVO> voList = findFundMarketList(fundId, cycle, startDate, endDate, toCurrency, langCode);
		ChartDataVO result = new ChartDataVO();
		
		result.setStartDate(startDate);
		result.setEndDate(endDate);
		result.setPeriodCode(period);
		result.setFundId(fundId);
		result.setDataList(voList);
		
		//补充最新基金回报信息
		String periodCode = getPeriodCode(period);
		List<FundCumulativePerformanceDataVO> pers = fundReturnService.fundCumulativePerformanceInfo(fundId, periodCode, langCode);
		if (null!=pers && !pers.isEmpty()){
			FundCumulativePerformanceDataVO vo = pers.get(0);
			result.setLastRanking(vo.getLastRanking());
			result.setNewRanking(vo.getNewRanking());
			result.setTypeAverage(vo.getTypeAverage());
			result.setIncrease(vo.getValue());
			result.setTypeTotal(vo.getTypeTotal());
		}
		return result;
	}
	
	/**
	 * 得到基金回报时期编码
	 * @param dateType
	 * @return
	 */
	private String getPeriodCode(String dateType) {
		String dateTypeStr = "";
		if ("1WK".equals(dateType))
			dateTypeStr = "fund_return_period_code_1W";
		if ("1Mth".equals(dateType))
			dateTypeStr = "fund_return_period_code_1M";
		if ("3Mth".equals(dateType))
			dateTypeStr = "fund_return_period_code_3M";
		if ("6Mth".equals(dateType))
			dateTypeStr = "fund_return_period_code_6M";
		
		if ("YTD".equals(dateType))
			dateTypeStr = "fund_return_period_code_YTD";
		if ("1Yr".equals(dateType))
			dateTypeStr = "fund_return_period_code_1Y";
		if ("2Yr".equals(dateType))
			dateTypeStr = "fund_return_period_code_2Y";
		if ("3Yr".equals(dateType))
			dateTypeStr = "fund_return_period_code_3Y";
		if ("5Yr".equals(dateType))
			dateTypeStr = "fund_return_period_code_5Y";
		if ("10Yr".equals(dateType))
			dateTypeStr = "fund_return_period_code_10Y";

		return dateTypeStr;
	}

	/**
	 * 获取基金收益百分比
	 * @author wwluo
	 * @param period 分析时间段编码(默认1Yr)：1Mth：一个月,3Mth,6Mth,1Yr：一年 ,3Yr,5Yr,YTD
	 * @param type W：FundMarketWeek,M：FundMarketMonth,默认取 FundMarket
	 * @return	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public List<FundIncomePercentageVO> getIncomePercentage(String fundId,
			String period,String type,Double weight,boolean ifWeight) {
		List<FundIncomePercentageVO> fundIncomePercentageVOs = new ArrayList<FundIncomePercentageVO>();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(fundId)) {
			StringBuffer hql = new StringBuffer(" from");
			if("W".equals(type)){
				hql.append(" FundMarketWeek");
			}else if("M".equals(type)){
				hql.append(" FundMarketMonth");
			}else{
				hql.append(" FundMarket");
			}
			hql.append(" m where m.isValid=1 and m.fund.id=?");
			List params = new ArrayList();
			params.add(fundId);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date()); 
			if (StringUtils.isNotBlank(period)) {
				if ("1Mth".equals(period)){
					calendar.add(Calendar.MONTH, -1);
				}else if ("3Mth".equals(period)){
					calendar.add(Calendar.MONTH, -3);
				}else if ("6Mth".equals(period)){
					calendar.add(Calendar.MONTH, -6);
				}else if ("YTD".equals(period)){
					calendar.set(calendar.get(Calendar.YEAR), 0, 2); 
				}else if ("1Yr".equals(period)){
					calendar.add(Calendar.YEAR, -1);
				}else if ("2Yr".equals(period)){
					calendar.add(Calendar.YEAR, -2);
				}else if ("3Yr".equals(period)){
					calendar.add(Calendar.YEAR, -3);
				}else if ("5Yr".equals(period)){
					calendar.add(Calendar.YEAR, -5);
				}else if ("10Yr".equals(period)){
					calendar.add(Calendar.YEAR, -10);
				}
			}else{
				calendar.add(Calendar.YEAR, -1);
			}
			calendar.add(Calendar.DATE,-1);
			Date startDate = calendar.getTime();
			Date curDate = new Date();
			hql.append(" and m.marketDate between ? and ?");
			params.add(startDate);
			params.add(curDate);
			hql.append(" order by m.marketDate");
			List<Object> fundMarkets = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(fundMarkets != null && !fundMarkets.isEmpty()){
				if(fundMarkets.get(0) instanceof FundMarket){
					FundMarket startFundMarket = (FundMarket) fundMarkets.get(0);
					Double startNav = startFundMarket.getNav();
					for (Object object : fundMarkets) {
						FundIncomePercentageVO fundIncomePercentageVO = new FundIncomePercentageVO();
						Double tempNav = null; 
						FundMarket fundMarket = (FundMarket) object;
						BeanUtils.copyProperties(fundMarket, fundIncomePercentageVO);
						tempNav = fundMarket.getNav();
						Double incomePercentage = (tempNav - startNav)/startNav*100;
						if(ifWeight){
							incomePercentage = incomePercentage*weight;
						}
						if(incomePercentage.isNaN()){
							incomePercentage = 0.00;
						}else{
							//保留两位小数，四舍五入
							incomePercentage = new BigDecimal(incomePercentage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						}
						fundIncomePercentageVO.setIncomePercentage(incomePercentage);
						fundIncomePercentageVOs.add(fundIncomePercentageVO);
					}
				}else if(fundMarkets.get(0) instanceof FundMarketWeek){
					FundMarketWeek startFundMarketWeek = (FundMarketWeek) fundMarkets.get(0);
					Double startNav = startFundMarketWeek.getNav();
					for (Object object : fundMarkets) {
						FundIncomePercentageVO fundIncomePercentageVO = new FundIncomePercentageVO();
						Double tempNav = null; 
						FundMarketWeek fundMarketWeek = (FundMarketWeek) object;
						BeanUtils.copyProperties(fundMarketWeek, fundIncomePercentageVO);
						//格式化时间
						tempNav = fundMarketWeek.getNav();
						Double incomePercentage = (tempNav - startNav)/startNav*100;
						if(ifWeight){
							incomePercentage = incomePercentage*weight;
						}
						//保留两位小数，四舍五入
						incomePercentage = new BigDecimal(incomePercentage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						fundIncomePercentageVO.setIncomePercentage(incomePercentage);
						fundIncomePercentageVOs.add(fundIncomePercentageVO);
					}
				}else if(fundMarkets.get(0) instanceof FundMarketMonth){
					FundMarketMonth startFundMarketMonth = (FundMarketMonth) fundMarkets.get(0);
					Double startNav = startFundMarketMonth.getNav();
					for (Object object : fundMarkets) {
						FundIncomePercentageVO fundIncomePercentageVO = new FundIncomePercentageVO();
						Double tempNav = null; 
						FundMarketMonth fundMarketMonth = (FundMarketMonth) object;
						BeanUtils.copyProperties(fundMarketMonth, fundIncomePercentageVO);
						//格式化时间
						tempNav = fundMarketMonth.getNav();
						Double incomePercentage = (tempNav - startNav)/startNav*100;
						if(ifWeight){
							incomePercentage = incomePercentage*weight;
						}
						//保留两位小数，四舍五入
						incomePercentage = new BigDecimal(incomePercentage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						fundIncomePercentageVO.setIncomePercentage(incomePercentage);
						fundIncomePercentageVOs.add(fundIncomePercentageVO);
					}
				}
			}
		}
		return fundIncomePercentageVOs;
	}

	/**
	 * 获取多只基金总收益百分比
	 * @author wwluo
	 * @param fundMarketDataVOs
	 * @return	
	 */
	@Override
	public Map<Date, Object> getIncomePercentageTotal(
			List<FundMarketDataVO> fundMarketDataVOs) {
		Map<Date, Object> vo = new HashMap<Date, Object>();
		if(fundMarketDataVOs != null && !fundMarketDataVOs.isEmpty()){
			List<FundIncomePercentageVO> fundIncomePercentageVOs = new ArrayList<FundIncomePercentageVO>();
			List<FundIncomePercentageVO> incomePercentageVOs = new  ArrayList<FundIncomePercentageVO>();
			//参照物下标
			Integer index = 0;
			//查找一个不为空的集合为参照物
			for (int i = 0; i < fundMarketDataVOs.size(); i++) {
				if(fundMarketDataVOs.get(i).getFundIncomePercentageVOs() != null &&
						!fundMarketDataVOs.get(i).getFundIncomePercentageVOs().isEmpty()){
					incomePercentageVOs = fundMarketDataVOs.get(i).getFundIncomePercentageVOs();
					index = i;
					break;
				}
			}
			//合并除参照物外的其余集合，大集合
			for (int i = 0; i < fundMarketDataVOs.size(); i++) {
				if(i!=index){
					List<FundIncomePercentageVO> vos = fundMarketDataVOs.get(i).getFundIncomePercentageVOs();
					fundIncomePercentageVOs.addAll(vos);
				}
			}
			//总收益集合
			List<FundIncomePercentageVO> incomePercentageTotal = new ArrayList<FundIncomePercentageVO>();
			//遍历参照物
			for (FundIncomePercentageVO fundIncomePercentageVO : incomePercentageVOs) {
				FundIncomePercentageVO percentageVO = new FundIncomePercentageVO();
				Date source = fundIncomePercentageVO.getMarketDate();
				Double navSource = fundIncomePercentageVO.getNav();
				//目标集合
				List<FundIncomePercentageVO> targetVO = new ArrayList<FundIncomePercentageVO>();
				//遍历大集合，存入目标集合
				for (int i = 0; i < fundIncomePercentageVOs.size(); i++) {
					Date target = fundIncomePercentageVOs.get(i).getMarketDate();
					if(source.equals(target)){
						targetVO.add(fundIncomePercentageVOs.get(i));
					}
				}
				Double navTotal = navSource;
				//遍历目标集合，计算总净值
				for (FundIncomePercentageVO percentage : targetVO) {
					Double navTarget = percentage.getIncomePercentage();
					if(navSource != null && navTarget != null){
						navTotal = navSource + navTarget;
					}else if(navSource == null){
						navTotal = navTarget;
					}
	  			}	
				percentageVO.setNav(navTotal);
				percentageVO.setMarketDate(source);
				incomePercentageTotal.add(percentageVO);
			}
			//处理总收益集合，求出总收益值
			if(incomePercentageTotal !=null && !incomePercentageTotal.isEmpty()){
				//总净值参照物
				Double startNav = null;
				for (Integer i = 0; i < incomePercentageTotal.size(); i++) {
					if(incomePercentageTotal.get(i).getNav()!=null){
						startNav = incomePercentageTotal.get(i).getNav();
						break;
					}
				}
				//计算总收益值
				for (FundIncomePercentageVO fundIncomePercentageVO : incomePercentageTotal) {
					Double tempNav = fundIncomePercentageVO.getNav();
					Double incomePercentage = null;
					if(tempNav != null){
						incomePercentage = (tempNav - startNav)/startNav*100;
						//保留两位小数，四舍五入
						incomePercentage = new BigDecimal(incomePercentage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					vo.put(fundIncomePercentageVO.getMarketDate(), incomePercentage);
				}
			}
		}
		return vo;
	}
	
	/**
	 * 定投基金总收益率
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	@Override
	public Map<Date, Object>  getAipIncomePercentageTotal(AipVO aip, Map<Date, Object> incomePercentages) {
		List<Map<Date, Object>> vos = new ArrayList<Map<Date, Object>>();
		List<String> dates = this.caluAipTime(aip);
		for (String dateStr : dates) {
			Date date = DateUtil.StringToDate(dateStr, CommonConstants.FORMAT_DATE);
			if(incomePercentages.containsKey(date)){
				Map<Date, Object> map = new HashMap<Date, Object>();
				map.put(date, incomePercentages.get(date));
				vos.add(map);
				break;
			}
		}
		List<Map<Date, Object>> temps = new ArrayList<Map<Date, Object>>();
		if(!vos.isEmpty()){
			for (Date marketDate : incomePercentages.keySet()) {
				Map<Date, Object> temp = new HashMap<Date, Object>();
				temp.put(marketDate, incomePercentages.get(marketDate));
				temps.add(temp);
			}
			Collections.sort(temps, new Comparator<Map<Date, Object>>(){
				@Override
				public int compare(Map<Date, Object> o1,
						Map<Date, Object> o2) {
					Date date1 = null;
					for (Date date : o1.keySet()) {
						date1 = date;
					}
					Date date2 = null;
					for (Date date : o2.keySet()) {
						date2 = date;
					}
					if(date1.getTime() > date2.getTime()){
						return 1;
					}else{
						return -1;
					}
				}
			});
			Map<Date, Object> lastObj = temps.get(temps.size()-1);
			for (int i = 0; i < vos.size(); i++) {
				Double incomePercentage = null;
				for (Date marketDate : lastObj.keySet()) {
					incomePercentage = (Double) lastObj.get(marketDate);
				}
				if(incomePercentage != null){
					incomePercentage = incomePercentage/100;
				}else{
					incomePercentage = 0.00;
				}
				if(i > 0){
					Double tempNav = incomePercentage;
					for (int j = 1; j <= i; j++) {
						for (Date marketDate : vos.get(i-j).keySet()) {
							tempNav = (Double) vos.get(i-j).get(marketDate) + tempNav;
						}
					}
					tempNav = tempNav/(i+1);
					tempNav = new BigDecimal(tempNav).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					for (Date marketDate : vos.get(i).keySet()) {
						vos.get(i).put(marketDate, tempNav);
					}
				}else{
					incomePercentage = new BigDecimal(incomePercentage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					for (Date marketDate : vos.get(i).keySet()) {
						vos.get(i).put(marketDate, incomePercentage);
					}
				}
			}
		}
		for (Map<Date, Object> incomePercentage : temps) {
			for (Map<Date, Object> aipIncomePercentage : vos) {
				Date marketDate = null;
				for (Date obj : aipIncomePercentage.keySet()) {
					marketDate = obj;
				}
				if(!incomePercentage.containsKey(marketDate)){
					vos.add(incomePercentage);
					break;
				}
			}
		}
		Map<Date, Object> result = new HashMap<Date, Object>();
		for (Map<Date, Object> map : vos) {
			result.putAll(map);
		}
		return result;
	}
	
	/**
	 * 计算周期内每次定投时间
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	@Override
	public List<String> caluAipTime(AipVO aip) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(CommonConstants.FORMAT_DATE);
		List<String> dates = new ArrayList<String>();
		// 周期
		String execCycle = aip.getAipExecCycle();
		// 间隔
		Integer timeDistance = aip.getAipTimeDistance();
		Date startDate = aip.getCreateTime();
		Calendar startCalendar = Calendar.getInstance();
		if(startDate!=null){
			startCalendar.setTime(startDate);
		}
		startCalendar.add(Calendar.YEAR, -1);
		startDate = startCalendar.getTime();
		// 按时间结束的定投
		if(aip != null && aip.getAipEndDate() != null){
			Long endTime = aip.getAipEndDate().getTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			if(CommonConstants.AIP_EXEC_CYCLE_W.equals(execCycle)){
				while (calendar.getTime().getTime() < endTime) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 7);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_B.equals(execCycle)){
				while (calendar.getTime().getTime() < endTime) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 14);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_M.equals(execCycle)){
				while (calendar.getTime().getTime() < endTime) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_MONTH);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.MONTH, 1);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}
		// 按次数结束的定投
		}else if(aip != null && aip.getAipEndCount() != null){
			Integer endCount = aip.getAipEndCount();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			if(CommonConstants.AIP_EXEC_CYCLE_W.equals(execCycle)){
				for (int i = 0; i < endCount; i++) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 7);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_B.equals(execCycle)){
				for (int i = 0; i < endCount; i++) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 14);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_M.equals(execCycle)){
				for (int i = 0; i < endCount; i++) {
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_MONTH);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.MONTH, 1);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}
		// 按总金额结束的定投	
		}else if(aip != null && aip.getAipEndTotalAmount() != null){
			Double aipAmount = aip.getAipAmount();
			Double endTotalAmount = aip.getAipEndTotalAmount();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			if(CommonConstants.AIP_EXEC_CYCLE_W.equals(execCycle)){
				Integer index = 1;
				while (aipAmount < endTotalAmount) {
					aipAmount = aip.getAipAmount();
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 7);
					dates.add(dateFormat.format(calendar.getTime()));
					aipAmount = aipAmount*index;
					index ++;
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_B.equals(execCycle)){
				while (aipAmount < endTotalAmount)  {
					aipAmount = aip.getAipAmount();
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK+1);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.DATE, 14);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}else if(CommonConstants.AIP_EXEC_CYCLE_M.equals(execCycle)){
				while (aipAmount < endTotalAmount)  {
					aipAmount = aip.getAipAmount();
					calendar.add(Calendar.DATE, -Calendar.DAY_OF_MONTH);
					calendar.add(Calendar.DATE, timeDistance);
					calendar.add(Calendar.MONTH, 1);
					dates.add(dateFormat.format(calendar.getTime()));
				}
			}
		}
		return dates;
	}
	
	/**
	 * 根据传入基金获取资产类别
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	@Override
	public List<Map<String,Object>> getAssetClassByfunds(String funds,String assetClass,String langCode) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> result = null;
		if (StringUtils.isNotBlank(funds) 
				&& StringUtils.isNotBlank(langCode) 
					&& StringUtils.isNotBlank(assetClass)) {
			langCode = langCode.substring(0,1).toUpperCase() + langCode.substring(1);
			List<String> sectorTypes = new ArrayList<String>();
			if(funds.indexOf(",") > -1){
				String[] fund = funds.split(",");
				for (int i = 0; i < fund.length; i++) {
					StringBuffer hql = new StringBuffer("" +
							" SELECT " + assetClass +
							" FROM" +
							" FundInfo"+langCode+"" +
							" WHERE id=?");
					List<Object> params = new ArrayList<Object>();
					params.add(fund[i]);
					List<String> object = this.baseDao.find(hql.toString(), params.toArray(), false);
					if(object != null && !object.isEmpty()){
						for (String objs : object) {
							sectorTypes.add((String) objs);
						}
					}
				}
			}else{
				StringBuffer hql = new StringBuffer("" +
						" SELECT " + assetClass +
						" FROM" +
						" FundInfo"+langCode+"" +
						" WHERE id=?");
				List<Object> params = new ArrayList<Object>();
				params.add(funds);
				List<String> object = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(object != null && !object.isEmpty()){
					for (String objs : object) {
						sectorTypes.add(objs);
					}
				}
			}
			if(!sectorTypes.isEmpty()){
				result = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < sectorTypes.size(); i++) {
					List<String> list = new ArrayList<String>();
					list.add(sectorTypes.get(i));
					for (String geoAllocation : sectorTypes) {
						boolean flag = list.contains(geoAllocation);
						if(flag){
							list.add(geoAllocation);
						}
					}
					map.put(sectorTypes.get(i), list.size()-1);
				}
				result.add(map);
			}
		}
		return result;
	}

}
