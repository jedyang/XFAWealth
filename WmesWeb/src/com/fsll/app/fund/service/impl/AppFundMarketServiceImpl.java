package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundMarketService;
import com.fsll.app.fund.service.AppFundReturnService;
import com.fsll.app.fund.vo.AppChartDataVO;
import com.fsll.app.fund.vo.AppFundCumulativePerformanceDataVO;
import com.fsll.app.fund.vo.AppFundMarketDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;

/**
 * 基金净值信息查询服务接口实现
 * @author zpzhou
 * @date 2016-6-20
 */
@Service("appFundMarketService")
//@Transactional
public class AppFundMarketServiceImpl extends BaseService implements AppFundMarketService {

	@Autowired
	private AppFundReturnService fundReturnService;
	
	/**
	 * 获取基金净值分页
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
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		String tableName = "FundMarket";//日行情
		if (null!=period){
			if ("week".equals(period))
				tableName = "FundMarketWeek";//周行情
			else if ("month".equals(period))
				tableName = "FundMarketMonth";//月行情
		}
		
		String hql = "select t.fund.id,i.fundName,t.nav,t.accNav,DATE_FORMAT(t.marketDate,'%Y-%m-%d'),t.returnRate,i.fundCurrency,i.fundCurrencyCode from "+tableName+" t ";
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
		List<AppFundMarketDataVO> list = new ArrayList<AppFundMarketDataVO>();
		List voList = jsonPaging.getList();
		for(int x=0;x<voList.size();x++){
			AppFundMarketDataVO vo = new AppFundMarketDataVO();
			Object[] objs = (Object[])voList.get(x);
			vo.setFundId(StrUtils.getString(objs[0]));
			vo.setFundName(StrUtils.getString(objs[1]));
			vo.setNav(StrUtils.getString(objs[2]));
			vo.setAccNav(StrUtils.getString(objs[3]));
			vo.setMarketDate(StrUtils.getString(objs[4]));
			vo.setReturnRate(StrUtils.getString(objs[5]));
			vo.setOrgCurrency(StrUtils.getString(objs[6]));
			
			String fromCurrency=StrUtils.getString(objs[7]);
			//数据格式处理
			if(null==vo.getReturnRate() || "".equals(vo.getReturnRate())){
				vo.setReturnRate(getFormatNumByPer(null));
			}else{
				vo.setReturnRate(getFormatNumByPer(Double.parseDouble(vo.getReturnRate())));
			}
			
			if(StringUtils.isNotBlank(toCurrency)){
				//货币转换
				vo.setDestCurrency(toCurrencyName);
				Double rate = getExchangeRate(fromCurrency, toCurrency);
				if(null==vo.getNav() || "".equals(vo.getNav())){
					vo.setNav(getFormatNumByRate(null,rate,toCurrency));
				}else{
					vo.setNav(getFormatNumByRate(Double.parseDouble(vo.getNav()),rate,toCurrency));
				}
				if(null==vo.getAccNav() || "".equals(vo.getAccNav())){
					vo.setAccNav(getFormatNumByRate(null,rate,toCurrency));
				}else{
					vo.setAccNav(getFormatNumByRate(Double.parseDouble(vo.getAccNav()),rate,toCurrency));
				}
			}else {
				vo.setDestCurrency(StrUtils.getString(objs[6]));
				if(null==vo.getNav() || "".equals(vo.getNav())){
					vo.setNav(getFormatNum(null));
				}else{
					vo.setNav(getFormatNum(Double.parseDouble(vo.getNav()),fromCurrency));
				}
				if(null==vo.getAccNav() || "".equals(vo.getAccNav())){
					vo.setAccNav(getFormatNum(null));
				}else{
					vo.setAccNav(getFormatNum(Double.parseDouble(vo.getAccNav()),fromCurrency));
				}
			}
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取基金净值图表数据
	 * @param fundId 资金id
	 * @param dataType 数据类型
	 * @param cycle 分析周期：D-日；W-周；M-月
	 * @param period 分析时间段编码：return_period_code_1W：一周，return_period_code_1M：一个月...，return_period_code_1Y：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param toCurrency 转换目标货币
	 * @param langCode 显示语言
	 * 
	 */
	public AppChartDataVO fundChartData(String fundId, String dataType, String cycle, String periodCode, String addiData, String toCurrency,String langCode) {
		int addData = StrUtils.getInt(addiData);
//		periodCode = StrUtils.getString(periodCode);
		String startDate = DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr();
		
		startDate = getStartDate(periodCode);
		
		cycle = StrUtils.getString(cycle);
		if("".equals(startDate)){
			startDate = "";
			endDate = "";
		}else{
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
		}
		
		//所有数据，不限制日期范围
//		if ("All".equals(periodCode)){
//			startDate = "";
//			endDate = "";
//		}
		List<AppFundMarketDataVO> voList = findFundMarketList(fundId, cycle, startDate, endDate, toCurrency, langCode);
		AppChartDataVO result = new AppChartDataVO();
		
		result.setStartDate(startDate);
		result.setEndDate(endDate);
		result.setPeriodCode(periodCode);
		result.setFundId(fundId);
		result.setDataList(voList);
		
		//补充最新基金回报信息
//		String periodCode = getPeriodCode(period);
		List<AppFundCumulativePerformanceDataVO> pers = fundReturnService.fundCumulativePerformanceInfo(fundId, periodCode, langCode);
		if (null!=pers && !pers.isEmpty()){
			AppFundCumulativePerformanceDataVO vo = pers.get(0);
			result.setLastRanking(vo.getLastRanking());
			result.setNewRanking(vo.getNewRanking());
			result.setTypeAverage(vo.getTypeAverage());
			result.setIncrease(vo.getValue());//百分比
			result.setTypeTotal(vo.getTypeTotal());
		}
		return result;
	}
	
	/**
	 * 获取基金价格行情信息列表
	 * @param fundId 基金id
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param toCurrency 目标货币
	 * @param langCode 语言
	 * 
	 */
	private List<AppFundMarketDataVO> findFundMarketList(String fundId, String period, String startDate, String endDate, String toCurrency, String langCode) {
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		String tableName = "FundMarket";//日行情
		if (null!=period){
			if ("week".equals(period))
				tableName = "FundMarketWeek";//周行情
			else if ("month".equals(period))
				tableName = "FundMarketMonth";//月行情
		}
		
		String hql = "select t.fund.id,i.fundName,t.nav,t.accNav,date_format(t.marketDate,'%Y-%m-%d'),t.returnRate,i.fundCurrency,i.fundCurrencyCode from "+tableName+" t ";
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
		hql += " order by t.marketDate";

		List voList=baseDao.find(hql, params.toArray(), false);
		List<AppFundMarketDataVO> list = new ArrayList<AppFundMarketDataVO>();
		for(int x=0;x<voList.size();x++){
			AppFundMarketDataVO vo = new AppFundMarketDataVO();
			Object[] objs = (Object[])voList.get(x);
			vo.setFundId(StrUtils.getString(objs[0]));
			vo.setFundName(StrUtils.getString(objs[1]));
			vo.setNav(StrUtils.getString(objs[2]));
			vo.setAccNav(StrUtils.getString(objs[3]));
			vo.setMarketDate(StrUtils.getString(objs[4]));
			vo.setReturnRate(StrUtils.getString(objs[5]));
			vo.setOrgCurrency(StrUtils.getString(objs[6]));
			String fromCurrency=StrUtils.getString(objs[7]);
			
			//数据格式处理
/*			if(null==vo.getReturnRate() || "".equals(vo.getReturnRate())){
				vo.setReturnRate(getFormatNumByPer(null));
			}else{
				vo.setReturnRate(getFormatNumByPer(Double.parseDouble(vo.getReturnRate())));
			}*/
			//货币转换
			if(StringUtils.isBlank(toCurrency)){
				vo.setDestCurrency(StrUtils.getString(objs[6]));
				if(StringUtils.isNotBlank(vo.getNav())){
					vo.setNav(getFormatNum(Double.parseDouble(vo.getNav()),fromCurrency));
				}
			}else {
				vo.setDestCurrency(toCurrencyName);
				Double rate = getExchangeRate(fromCurrency, toCurrency);
				if(null==vo.getNav() || "".equals(vo.getNav())){
					vo.setNav(getFormatNumByRate(null,rate,toCurrency));
				}else{
					vo.setNav(getFormatNumByRate(Double.parseDouble(vo.getNav()),rate,toCurrency));
				}
				if(null==vo.getAccNav() || "".equals(vo.getAccNav())){
					vo.setAccNav(getFormatNumByRate(null,rate,toCurrency));
				}else{
					vo.setAccNav(getFormatNumByRate(Double.parseDouble(vo.getAccNav()),rate,toCurrency));
				}
			}

			list.add(vo);
		}
		return list;
	}
	
}
