package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppIndexHotService;
import com.fsll.app.investor.market.vo.AppIndexChartVO;
import com.fsll.app.investor.market.vo.AppIndexMarketDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;

/**
 * 首页热门投资接口服务现实
 * @author zpzhou
 * @date 2016-9-26
 */
@Service("appIndexHotService")
//@Transactional
public class AppIndexHotServiceImpl extends BaseService implements AppIndexHotService {

	/**
	 * 得到首页组合中用到最多的基金的行情图表数据
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>FundIndexChartVO	基金图表数据
	 */
	public List<AppIndexChartVO> findFundIndexChart(String period, String addiData, String langCode,int num){
		List<AppIndexChartVO> fundlist=new ArrayList<AppIndexChartVO>();
		int addData = StrUtils.getInt(addiData);
		period = StrUtils.getString(period);
		String startDate = DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr();
		if ("return_period_code_1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("return_period_code_1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("return_period_code_3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("return_period_code_6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("return_period_code_1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("return_period_code_2Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
		if ("return_period_code_3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("return_period_code_5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("return_period_code_10Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
		if ("return_period_code_YTD".equals(period)) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来
		
        startDate = DateUtil.getDateStr(DateUtil.addDate(startDate,Calendar.DATE,-addData));
		//所有数据，不限制日期范围
		if ("All".equals(period)){
			startDate = "";
			endDate = "";
		}
		
		/*String hql = "select f.id,s.fundName,f.riskLevel,r.increase,a.num ";
		hql += " from (select count(*) as num,p.product.id from PortfolioArenaProduct p GROUP BY p.product.id order by p.num desc LIMIT 0, 5 ) as a ";
		hql += " left join FundInfo f on f.product.id=m.product.id ";
		hql += " left join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		List params = new ArrayList();
		params.add(period);
		List list = baseDao.find(hql, params.toArray(), false);*/
		String sql = "select f.id,s.fund_name,f.risk_level,r.increase,a.num ";
		sql += " from (select count(*) as num,p.product_id from portfolio_arena_product p  GROUP BY p.product_id order by num desc LIMIT 0, ? ) as a ";
		sql += " left JOIN fund_info f on f.product_id=a.product_id   ";
		sql += " left join fund_info_sc s on s.id=f.id ";
		sql += " left join fund_return r on r.fund_id=f.id and r.period_Code=? ";
		List params = new ArrayList();
		params.add(num);
		params.add(period);
		List<Map<String, String>> list = springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		for(int i=0;i<list.size();i++){
			Map map = list.get(i);
			AppIndexChartVO  vo = new AppIndexChartVO();
			if(null!=map.get("id"))vo.setId(map.get("id").toString());
			if(null!=map.get("fund_name"))vo.setName(map.get("fund_name").toString());
			if(null!=map.get("risk_level"))vo.setRiskLevel(map.get("risk_level").toString());
			if(null!=map.get("increase"))vo.setIncrease(map.get("increase").toString());
			if(null!=map.get("num"))vo.setNum(map.get("num").toString());
			vo.setStartDate(startDate);
			vo.setEndDate(endDate);
			List<AppIndexMarketDataVO> voList = findFundMarketList(vo.getId(), startDate, endDate);
			vo.setDataList(voList);
			fundlist.add(vo);
		}
		return fundlist;
	}
	
	/**
	 * 某只基金的某段时间的行情数据
	 * @param fundId  基金ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	private List<AppIndexMarketDataVO> findFundMarketList(String fundId, String startDate,String endDate){
		List<AppIndexMarketDataVO> list=new ArrayList<AppIndexMarketDataVO>();
		String hql = "select t.returnRate,t.marketDate from FundMarket t where t.isValid='1'";
		List params = new ArrayList();
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql += "and t.marketDate>=? ";
			params.add(DateUtil.getDate(startDate));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += "and t.marketDate<=? ";
			params.add(DateUtil.getDate(endDate));
		}
		hql += " order by t.fund.id,t.marketDate ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				AppIndexMarketDataVO vo = new AppIndexMarketDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setReturnRate(String.valueOf(objs[0]));
				vo.setMarketDate(String.valueOf(objs[1]));
				list.add(vo);
			}
		}
		return list;
	}
	/**
	 * 得到首页组合中收益最多的行情图表数据
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>FundIndexChartVO	基金图表数据
	 */
	public List<AppIndexChartVO> findPortfolioIndexChart(String period, String addiData, String langCode,int num){
		List<AppIndexChartVO> portfolioList=new ArrayList<AppIndexChartVO>();
		int addData = StrUtils.getInt(addiData);
		period = StrUtils.getString(period);
		String startDate = DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr();
		if ("return_period_code_1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("return_period_code_1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("return_period_code_3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("return_period_code_6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("return_period_code_1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("return_period_code_2Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
		if ("return_period_code_3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("return_period_code_5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("return_period_code_10Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
		if ("return_period_code_YTD".equals(period)) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来
		
        startDate = DateUtil.getDateStr(DateUtil.addDate(startDate,Calendar.DATE,-addData));
		//所有数据，不限制日期范围
		if ("All".equals(period)){
			startDate = "";
			endDate = "";
		}
		
		String hql = "select f.id,f.portfolioName,f.riskLevel,k.cumprefRate";
		hql += " from PortfolioArenaCumperf k left join k.portfolio f";
		hql += " order by k.cumprefRate desc LIMIT 0, ? ";
		List params = new ArrayList();
		params.add(num);
		List list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexChartVO  vo = new AppIndexChartVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());
			vo.setStartDate(startDate);
			vo.setEndDate(endDate);
			List<AppIndexMarketDataVO> voList = findPortfolioMarketList(vo.getId(), startDate, endDate);
			vo.setDataList(voList);
			portfolioList.add(vo);
		}
		return portfolioList;
	}
	
	/**
	 * 某只组合的某段时间的行情数据
	 * @param portfolioId  组合ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	private List<AppIndexMarketDataVO> findPortfolioMarketList(String portfolioId, String startDate,String endDate){
		List<AppIndexMarketDataVO> list=new ArrayList<AppIndexMarketDataVO>();
		String hql = "select t.valuationDate,t.marketDate from PortfolioArenaCumperf t where 1=1 ";
		List params = new ArrayList();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql += "and t.portfolio.id=? ";
			params.add(portfolioId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql += "and t.valuationDate>=? ";
			params.add(DateUtil.getDate(startDate));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += "and t.valuationDate<=? ";
			params.add(DateUtil.getDate(endDate));
		}
		hql += " order by t.portfolio.id,t.valuationDate ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				AppIndexMarketDataVO vo = new AppIndexMarketDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setReturnRate(String.valueOf(objs[0]));
				vo.setMarketDate(String.valueOf(objs[1]));
				list.add(vo);
			}
		}
		return list;
	}

}
