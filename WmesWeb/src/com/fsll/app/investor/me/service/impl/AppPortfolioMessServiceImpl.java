/**
 * 
 */
package com.fsll.app.investor.me.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.statistics.extended.RateImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaCumperfVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaReturnVO;
import com.fsll.app.investor.me.service.AppPortfolioMessService;
import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioChartDataVo;
import com.fsll.app.investor.me.vo.AppPortfolioFeeVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldVO;
import com.fsll.app.investor.me.vo.AppPortfolioMarketMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppPortfolioPerformanceVo;
import com.fsll.app.investor.me.vo.AppPortfolioProductDetailVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppPortfolioReturnVO;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundPortfolioSc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.ProductInfo;

/**
 * 投资组合接口服务接口类现实
 * @author zpzhou
 * @date 2016-9-14
 */
@Service("appPortfolioMessService")
//@Transactional
public class AppPortfolioMessServiceImpl extends BaseService implements AppPortfolioMessService {

/*	*//**
	 * 得到我的投资组合列表
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param keyWord 搜索关键词
	 * @param toCurrency 货币转换参数
	 * @return
	 *//*
	public List<AppPortfolioMessVo> findPortfolioList(String memberId,String ifaId,String keyWord,String toCurrency){
		List<AppPortfolioMessVo>  messList = new ArrayList<AppPortfolioMessVo>();
		String hql = " from PortfolioInfo p left join PortfolioHold h on p.id=h.portfolio.id where p.member.id=? and p.isValid=? ";
		List params = new ArrayList();
		params.add(memberId);
		params.add("1");
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and p.portfolioName like ? ";
			params.add("%"+keyWord+"%");
		}
		if(null!=ifaId && !"".equals(ifaId)){
			hql += " and p.memberIfa.id= ? ";
			params.add(ifaId);
		}
		List list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioInfo info=(PortfolioInfo)objs[0];
			PortfolioHold hold=(PortfolioHold)objs[1];
			AppPortfolioMessVo vo =new AppPortfolioMessVo();
			vo.setId(info.getId());
			vo.setBaseCurrency(info.getBaseCurrency());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setPortfolioName(info.getPortfolioName());
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			if(null!=hold){
				vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			    vo.setReturnRate(getFormatNumByPer(hold.getTotalReturnRate()));
			    Double rate=getExchangeRate(info.getBaseCurrency(),toCurrency);
			    vo.setTotalAmount(getFormatNumByRate(hold.getTotalMarketValue(),rate,toCurrency));
			    vo.setTotalReturn(getFormatNumByRate(hold.getTotalReturnValue(),rate,toCurrency));
			}
			messList.add(vo);
		}
		return messList;
	}*/
	/**
	 * 得到某个投资组合行情数据
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioMarketMessVo> findPortfolioMarketList(String portfolioId){
		List<AppPortfolioMarketMessVo>  messList = new ArrayList<AppPortfolioMarketMessVo>();
		String hql = " from PortfolioHoldCumperf m where m.portfolio.id=? ";
		List params = new ArrayList();
		params.add(portfolioId);
		List<PortfolioHoldCumperf> list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			PortfolioHoldCumperf market =list.get(i);
			AppPortfolioMarketMessVo vo =new AppPortfolioMarketMessVo();
			vo.setId(market.getId());
			vo.setTotalPl(String.valueOf(market.getCumulativePl()));
			vo.setDayPl(String.valueOf(market.getDayPl()));
			vo.setValuationDate(DateUtil.dateToDateString(market.getValuationDate(),"yyyy-MM-dd"));
			vo.setCreateTime(DateUtil.dateToDateString(market.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(market.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 得到投资组合中某个产品的行情数据
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioMarketMessVo> findPortfolioProductMarketList(String portfolioId,String productId){
		List<AppPortfolioMarketMessVo>  messList = new ArrayList<AppPortfolioMarketMessVo>();
		String hql = " from PortfolioHoldProductCumperf m where m.portfolio.id=? and m.product.id=?";
		List params = new ArrayList();
		params.add(portfolioId);
		params.add(productId);
		List<PortfolioHoldProductCumperf> list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			PortfolioHoldProductCumperf market =list.get(i);
			AppPortfolioMarketMessVo vo =new AppPortfolioMarketMessVo();
			vo.setId(market.getId());
			vo.setTotalPl(String.valueOf(market.getTotalPl()));
			vo.setDayPl(String.valueOf(market.getDayPl()));
			vo.setValuationDate(DateUtil.dateToDateString(market.getValuationDate(),"yyyy-MM-dd"));
			vo.setCreateTime(DateUtil.dateToDateString(market.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(market.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			messList.add(vo);
		}
		return messList;
	}
	

	
	/**
	 * 得到投资组合的产品信息列表
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioProductVo> findPortfolioProductList(String portfolioId,String langCode,String dateType){
		List<AppPortfolioProductVo>  messList = new ArrayList<AppPortfolioProductVo>();
		String hql = "select f.id,m.allocationRate,s.fundName,s.fundCurrency,s.fundType,f.riskLevel,f.lastNav,r.increase ";
		hql += " from PortfolioInfoProduct m ";
		hql += " left join FundInfo f on f.product.id=m.product.id ";
		
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=f.id ";

		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where m.portfolio.id=?  ";
		List params = new ArrayList();
		params.add(dateType);
		params.add(portfolioId);
		
		List list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppPortfolioProductVo  vo = new AppPortfolioProductVo();
			vo.setFundId(objs[0]==null?"":objs[0].toString());
			vo.setAllocationRate(objs[1]==null?"":objs[1].toString());
			vo.setFundName(objs[2]==null?"":objs[2].toString());
			vo.setFundCurrency(objs[3]==null?"":objs[3].toString());
			vo.setFundType(objs[4]==null?"":objs[4].toString());
			vo.setRiskLevel(objs[5]==null?"":objs[5].toString());
			vo.setLastNav(objs[6]==null?"":objs[6].toString());
			vo.setIncrease(objs[7]==null?"":objs[7].toString());
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 得到投资组合基本信息
	 * @author zxtan
	 * @param portfolioHoldId 组合ID
	 * @param toCurrency 货币
	 * @return
	 */
	public AppPortfolioHoldVO findPortfolioHold(String portfolioHoldId,String toCurrency,String langCode){
		AppPortfolioHoldVO vo = new AppPortfolioHoldVO();
		String hql = " from PortfolioHold h where h.id=? ";
		List params = new ArrayList();
		params.add(portfolioHoldId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioHold hold = (PortfolioHold)list.get(0);
			vo.setId(hold.getId());
			
			vo.setCreateTime(DateUtil.dateToDateString(hold.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setPortfolioName(hold.getPortfolioName());
			//vo.setRiskLevel(String.valueOf(info.getRiskLevel()));			
			vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
		    vo.setTotalReturnRate( getFormatNumByPer(hold.getTotalReturnRate()));
		    if("".equals(toCurrency)){
		    	vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), "currency_type"));
		    	vo.setBaseCurrencyCode(hold.getBaseCurrency());
			    vo.setTotalMarketValue(getFormatNum(hold.getTotalMarketValue(),hold.getBaseCurrency()));
			    vo.setTotalReturnValue(getFormatNum(hold.getTotalReturnValue(),hold.getBaseCurrency()));
			    vo.setTotalCash(getFormatNum(hold.getTotalCash(),hold.getBaseCurrency()));
			    vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
		    }else {
		    	vo.setBaseCurrency(getParamConfigName(langCode, toCurrency, "currency_type"));
		    	vo.setBaseCurrencyCode(toCurrency);
		    	Double rate = getExchangeRate(hold.getBaseCurrency(),toCurrency);
		    	if(rate != null){
		    		vo.setTotalMarketValue( getFormatNumByRate(hold.getTotalMarketValue(), rate,toCurrency));
		    		vo.setTotalReturnValue( getFormatNumByRate(hold.getTotalReturnValue(), rate,toCurrency));
		    		vo.setTotalCash( getFormatNumByRate(hold.getTotalCash(), rate,toCurrency));
		    		vo.setTotalAsset( getFormatNumByRate(hold.getTotalAsset(), rate,toCurrency));
		    	}else {
		    		vo.setTotalMarketValue( getFormatNum(null));
				    vo.setTotalReturnValue( getFormatNum(null));
				    vo.setTotalCash(getFormatNum(null));
				    vo.setTotalAsset(getFormatNum(null));
				}			    
			}
		    
		    MemberIfa ifa = hold.getIfa();
		    vo.setIfaId(ifa.getId());
		    vo.setIfaMemberId(ifa.getMember().getId());
			
		    String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
			vo.setIfaName(ifaName);	
			
			String hasOrderPlan = checkHoldOrderPlan(portfolioHoldId);
			vo.setHasOrderPlan(hasOrderPlan);
			
		}
		return vo;
	}
	
	/**
	 * 是否有进行中的计划
	 * @param holdId
	 * @return
	 */
	private String checkHoldOrderPlan(String holdId){
		String hql = "from OrderPlan t where t.portfolioHold.id=? and t.finishStatus in ('1','2','3') ";
		List<Object> params = new ArrayList<Object>();
		params.add(holdId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return String.valueOf(list.size());
		}else {
			return "0";
		}
	}
	
	/**
	 * 得到某个投资组合行情图表数据
	 * @param portfolioId 组合ID
	 * @param dataType 数据类型
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param toCurrency 转换目标货币
	 * @param langCode 显示语言
	 * @return	<List>PortfolioChartDataVo	基金图表数据
	 */
	public AppPortfolioChartDataVo findPortfolioChart(String portfolioId, String period, String addiData,String langCode){
		AppPortfolioChartDataVo result=new AppPortfolioChartDataVo();
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
		List<AppPortfolioMarketMessVo>  messList = new ArrayList<AppPortfolioMarketMessVo>();
		String hql = " from PortfolioHoldCumperf m where m.portfolio.id=? ";
		List params = new ArrayList();
		params.add(portfolioId);
		if(null!=startDate && !"".equals(startDate)){
			hql += " and m.marketDate>=? ";
			params.add(DateUtil.getDate(startDate,CoreConstants.DATE_FORMAT));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += " and m.marketDate<=? ";
			params.add(DateUtil.getDate(endDate,CoreConstants.DATE_FORMAT));
		}
		hql +=" order by m.marketDate asc";
		List<PortfolioHoldCumperf> list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			PortfolioHoldCumperf market =list.get(i);
			AppPortfolioMarketMessVo vo =new AppPortfolioMarketMessVo();
			vo.setId(market.getId());
			vo.setDayPl(String.valueOf(market.getDayPl()));
			vo.setTotalPl(String.valueOf(market.getCumulativePl()));
			vo.setValuationDate(DateUtil.dateToDateString(market.getValuationDate(),"yyyy-MM-dd"));
			vo.setCreateTime(DateUtil.dateToDateString(market.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(market.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			messList.add(vo);
		}		
		result.setDataList(messList);
		//补充最新基金回报信息
		List<AppPortfolioPerformanceVo> pers = cumulativePerformanceInfo(portfolioId, period,"heap");
		if (null!=pers && !pers.isEmpty()){
			AppPortfolioPerformanceVo vo = pers.get(0);
			result.setLastRanking(vo.getLastRanking());
			result.setNewRanking(vo.getNewRanking());
			result.setTypeAverage(vo.getTypeAverage());
			result.setIncrease(vo.getIncrease());
			result.setTypeTotal(vo.getTypeTotal());
		}
		return result;
	}
	
	/**获取组合累积表现信息
	 * @param portfolioId 组合id
	 * @param period 时期
	 * @param type 类型  year/heap
	 * @return	<List>PortfolioPerformanceVo	基金累积表现数据
	 */
	private List<AppPortfolioPerformanceVo> cumulativePerformanceInfo(String portfolioId, String period,String type) {
		List<AppPortfolioPerformanceVo> pList=new ArrayList<AppPortfolioPerformanceVo>();
		String hql = " from PortfolioReturn t where t.isValid = '1' ";
		List params = new ArrayList();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql += " and t.portfolio.id=? ";
			params.add(portfolioId);
		}
		if(null!=period && !"".equals(period)){
			hql += "and t.periodCode=? ";
			params.add(period);
		}
		if(null!=type && !"".equals(type)){
			hql += "and t.type=? ";
			params.add(type);
		}
		hql += " order by t.periodCode asc";
		List<PortfolioHoldReturn> preList = this.baseDao.find(hql, params.toArray(), false);
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				AppPortfolioPerformanceVo vo = new AppPortfolioPerformanceVo();
				PortfolioHoldReturn portfolioReturn = preList.get(x);
				vo.setIncrease(String.valueOf(portfolioReturn.getIncrease()));//increase
				vo.setTypeAverage(String.valueOf(portfolioReturn.getTypeAverage()));//type average
				vo.setNewRanking(String.valueOf(portfolioReturn.getNewRanking()));//new ranking
				vo.setLastRanking(String.valueOf(portfolioReturn.getLastRanking()));//last ranking
				vo.setPeriodCode(portfolioReturn.getPeriodCode());
				//vo.setPortfolioId(portfolioReturn.getPortfolio().getId());
				long fundCnt = findPortfolioTotal(portfolioId);
				vo.setTypeTotal(String.valueOf(fundCnt));
				pList.add(vo);
			}
		}
		return pList;
	}
	
	/**
	 * 组合统计总数
	 * @param portfolioId 组合ID
	 * @return
	 */
	private long findPortfolioTotal(String portfolioId){
		String hql = "from PortfolioInfo p where p.id=? and p.isValid=?";
		List params = new ArrayList();
		params.add(portfolioId);
		params.add("1");
		long countStr =baseDao.selectTotalRecords(hql, params.toArray(), false);
		return countStr;
	}
	
	/**
	 * （资产分析）得到我的投资组合列表
	 * @author zxtan
	 * @date 2016-11-29
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppPortfolioHoldVO> findPortfolioList(String memberId,String toCurrency,String periodCode,String langCode){
		List<AppPortfolioHoldVO>  messList = new ArrayList<AppPortfolioHoldVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" left join PortfolioHoldReturn r on h.id=r.portfolioHold.id and r.periodCode=? ");
		hql.append(" where h.member.id=? ");
		hql.append(" and exists ( select d.id from PortfolioHoldProduct d where d.portfolioHold.id=h.id ) ");
		hql.append(" order by h.lastUpdate desc ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHold hold = (PortfolioHold)objs[0];
			PortfolioHoldReturn ret = (PortfolioHoldReturn)objs[1];
			
			AppPortfolioHoldVO vo = new AppPortfolioHoldVO();
			vo.setId(hold.getId());
			vo.setPortfolioName(hold.getPortfolioName());
			
			vo.setCreateTime(DateUtil.dateToDateString(hold.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));

			vo.setTotalReturnRate(getFormatNumByPer(hold.getTotalReturnRate()));
			
			if( null==toCurrency || "".equals(toCurrency) ){
				vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), "currency_type"));
			    vo.setTotalMarketValue(getFormatNum(hold.getTotalMarketValue(),hold.getBaseCurrency()));
			    vo.setTotalReturnValue(getFormatNum(hold.getTotalReturnValue(),hold.getBaseCurrency()));
			    vo.setTotalCash(getFormatNum(hold.getTotalCash(),hold.getBaseCurrency()));
			    vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
			}else {
				vo.setBaseCurrency(toCurrencyName);
			    Double rate=getExchangeRate(hold.getBaseCurrency(),toCurrency);
			    vo.setTotalMarketValue(getFormatNumByRate(hold.getTotalMarketValue(),rate,toCurrency));
			    vo.setTotalReturnValue(getFormatNumByRate(hold.getTotalReturnValue(),rate,toCurrency));
			    vo.setTotalCash(getFormatNumByRate(hold.getTotalCash(),rate,toCurrency));
			    vo.setTotalAsset(getFormatNumByRate(hold.getTotalAsset(),rate,toCurrency));
			}
			
			if( null != ret ){
				vo.setPeriodCode(periodCode);
				Double increase = ret.getIncrease();
				vo.setIncrease(getFormatNumByPer(increase));
			}
			
			messList.add(vo);
		}
		return messList;
	}
	
	
	/**
	 * 得到我的持仓列表
	 * @author zxtan
	 * @date 2017-02-16
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @param period 周期 
	 * @param status 状态（1：当前持仓，2：进行中）
	 * @return
	 */
	public List<AppPortfolioHoldVO> findPortfolioHoldList(String memberId,String toCurrency,String periodCode,String status,String langCode){
		List<AppPortfolioHoldVO>  messList = new ArrayList<AppPortfolioHoldVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" left join PortfolioHoldReturn r on h.id=r.portfolioHold.id and r.periodCode=? ");
		hql.append(" where h.member.id=? ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add(memberId);
		if("1".equals(status)){
			hql.append(" and exists (select p.id from PortfolioHoldProduct p where p.portfolioHold.id=h.id ) ");
		}else {
//			hql.append(" and exists (select p.id from OrderPlan p where p.portfolioHold.id=h.id and p.finishStatus!='5' ) ");
			hql.append(" and exists (select p.id from OrderPlan p where p.portfolioHold.id=h.id and ( FIND_IN_SET(p.finishStatus,'1,2,3,4')>0 or (FIND_IN_SET(p.finishStatus,'-1,0')>0 and p.creator.id=? ) ) ) ");
			params.add(memberId);
		}
		hql.append(" order by h.lastUpdate desc ");		
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioHold hold = (PortfolioHold)objs[0];
				PortfolioHoldReturn ret = (PortfolioHoldReturn)objs[1];
				
				AppPortfolioHoldVO vo = new AppPortfolioHoldVO();
				vo.setId(hold.getId());
				vo.setPortfolioName(hold.getPortfolioName());
				vo.setIfFirst(hold.getIfFirst());
				vo.setCreateTime(DateUtil.dateToDateString(hold.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
	
				vo.setTotalReturnRate(getFormatNumByPer(hold.getTotalReturnRate()));
				
				if( null==toCurrency || "".equals(toCurrency) ){
					vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), "currency_type"));
				    vo.setTotalMarketValue(getFormatNum(hold.getTotalMarketValue(),hold.getBaseCurrency()));
				    vo.setTotalReturnValue(getFormatNum(hold.getTotalReturnValue(),hold.getBaseCurrency()));
				    vo.setTotalCash(getFormatNum(hold.getTotalCash(),hold.getBaseCurrency()));
				    vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
				}else {
					vo.setBaseCurrency(toCurrencyName);
				    Double rate=getExchangeRate(hold.getBaseCurrency(),toCurrency);
				    vo.setTotalMarketValue(getFormatNumByRate(hold.getTotalMarketValue(),rate,toCurrency));
				    vo.setTotalReturnValue(getFormatNumByRate(hold.getTotalReturnValue(),rate,toCurrency));
				    vo.setTotalCash(getFormatNumByRate(hold.getTotalCash(),rate,toCurrency));
				    vo.setTotalAsset(getFormatNumByRate(hold.getTotalAsset(),rate,toCurrency));
				}
				
				if( null != ret ){
					vo.setPeriodCode(periodCode);
					Double increase = ret.getIncrease();
					vo.setIncrease(getFormatNumByPer(increase));
				}
				if("2".equals(status)){
					vo = findOrderPlanCount(vo,memberId, toCurrency);
				}
				messList.add(vo);
			}
		}
		return messList;
	}
	
	
	/**
	 * （我的）得到我的持仓——投资组合列表
	 * @author zxtan
	 * @date 2017-02-16
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public AppPortfolioHoldVO findOrderPlanCount(AppPortfolioHoldVO holdVO,String memberId,String toCurrency){
		
		String sql = "CALL pro_getOrderPlanCount(?)";
		List params = new ArrayList();
		params.add(holdVO.getId());
		
		List list = this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
		Iterator it = (Iterator) list.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
			
	    	String planId = getMapObject(map,"id","");
	    	holdVO.setOrderPlanId(planId);
			holdVO.setOrderPlanCreatorId(getMapObject(map,"creator_id",""));
			holdVO.setOrderPlanCreateTime(getMapObject(map,"create_time",""));
			String finishStatus = getMapObject(map,"finish_status","");
			String totalBuy = getMapObject(map,"total_buy","0");
			holdVO.setFinishStatus(finishStatus);			
			if("1".equals(holdVO.getIfFirst())){
				//申购中
				String baseCurrency = getMapObject(map,"base_currency","");
				if(null != toCurrency && !"".equals(toCurrency)){
					Double rate = getExchangeRate(baseCurrency, toCurrency);
					if(null != rate){
						try {
							double buy = Double.parseDouble(totalBuy);
							holdVO.setTotalBuy(getFormatNumByRate(buy, rate,toCurrency));
						} catch (Exception e) {
							// TODO: handle exception
						}						
					}
				}else {
					double buy = Double.parseDouble(totalBuy);
					holdVO.setTotalBuy(getFormatNum(buy,baseCurrency));					
				}
				
				int productCount,successCount,pendCount,failCount;

				successCount = Integer.parseInt(getMapObject(map,"success_count","0"));
				pendCount = Integer.parseInt(getMapObject(map,"pend_count","0"));
				failCount = Integer.parseInt(getMapObject(map,"fail_count","0"));
				productCount = successCount+pendCount+failCount;
				
				holdVO.setProductCount(String.valueOf(productCount));
				holdVO.setSuccessCount(String.valueOf(successCount));
				holdVO.setPendCount(String.valueOf(pendCount));
				holdVO.setFailCount(String.valueOf(failCount));
				
			}else {
				//调整中
				int buyCount,sellCount;//,switchCount;

				buyCount = Integer.parseInt(getMapObject(map,"buy_count","0"));
				sellCount = Integer.parseInt(getMapObject(map,"sell_count","0"));
//				switchCount = Integer.parseInt(getMapObject(map,"switch_count","0"));
				
				holdVO.setBuyCount(String.valueOf(buyCount));
				holdVO.setSellCount(String.valueOf(sellCount));
//				holdVO.setSwitchCount(String.valueOf(switchCount));
			}	
			
			String hql = "from OrderPlanCheck m where m.checkStatus='0' and m.plan.id=? and m.check.id=? ";
			List<OrderPlanCheck> checkList = baseDao.find(hql, new Object[]{planId,memberId}, false);
			if(null != checkList && !checkList.isEmpty()){
				holdVO.setIfCheck("1");
			}else {
				holdVO.setIfCheck("0");
			}
			
		}
		
		return holdVO;
	}
	
	private String getMapObject(Map map, String key,String defaultValue) {
		return map.get(key) == null ? defaultValue : map.get(key).toString();
	}
	
	
	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppPortfolioReturnVO findPortfolioReturn(String portfolioHoldId,String periodCode){
		AppPortfolioReturnVO vo =new AppPortfolioReturnVO();
		String hql = " from PortfolioHoldReturn p where p.portfolioHold.id=? and p.periodCode=? ";
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(periodCode);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioHoldReturn info=(PortfolioHoldReturn)list.get(0);
			vo.setPeriodCode(info.getPeriodCode());
			//数据格式处理
			if(null==info.getIncrease() || "".equals(info.getIncrease().toString())){
				vo.setIncrease(getFormatNum(null));
			}else{
				vo.setIncrease(getFormatNumByPer(info.getIncrease()));
			}	
		}		
		
		return vo;
	}
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppPortfolioHoldCumperfVO> findPortfolioCumperfList(String portfolioHoldId,String startDate){
		List<AppPortfolioHoldCumperfVO> cumperfList = new ArrayList<AppPortfolioHoldCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldCumperf t inner join PortfolioHold h on t.portfolioHold.id=h.id where 1=1 ");
		List params = new ArrayList();
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and t.portfolioHold.id=? ");
			params.add(portfolioHoldId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql.append( "and t.valuationDate>=? ");
			params.add(DateUtil.getDate(startDate));
		}
		
		hql.append(" order by t.valuationDate ");

		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[]) list.get(i);
			PortfolioHoldCumperf cumperf = (PortfolioHoldCumperf) objs[0];
			PortfolioHold hold = (PortfolioHold) objs[1];
			AppPortfolioHoldCumperfVO vo = new AppPortfolioHoldCumperfVO();
			
			vo.setId(cumperf.getId());
			vo.setPortfolioId(hold.getId());
						
			vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
			if(null==cumperf.getCumulativeRate() || "".equals(cumperf.getCumulativeRate().toString())){
				vo.setCumulativeRate(getFormatNum(null));
			}else{
				vo.setCumulativeRate(getFormatNum(cumperf.getCumulativeRate()));
			}
			if(null==cumperf.getCumulativePl() || "".equals(cumperf.getCumulativePl().toString())){
				vo.setCumulativePl(getFormatNum(null));
			}else{
				vo.setCumulativePl(getFormatNum(cumperf.getCumulativePl(),cumperf.getBaseCurrency()));
			}
			if(null==cumperf.getDayPl() || "".equals(cumperf.getDayPl().toString())){
				vo.setDayPl(getFormatNum(null));
			}else{
				vo.setDayPl(getFormatNum(cumperf.getDayPl(),cumperf.getBaseCurrency()));
			}
			vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			cumperfList.add(vo);
		}
		return cumperfList;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioProductVo> findPortfolioProductFundList(String portfolioHoldId,String langCode,String toCurrency){
		List<AppPortfolioProductVo>  messList = new ArrayList<AppPortfolioProductVo>();
		StringBuilder hql = new StringBuilder();
//		hql.append("select m.id,m.holdingUnit,s.fundName,m.baseCurrency,s.fundType,f.riskLevel,f.lastNav,m.referenceCost,m.product.id,m.availableUnit ");
//		hql.append(" from PortfolioHoldProduct m ");
//		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
//		hql.append(" left join "+this.getLangString("FundInfo", langCode));
//		hql.append(" s on s.id=f.id ");
//		hql.append(" where m.portfolioHold.id=? ");
		
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		double totalAmount = 0;
		if(null != list && !list.isEmpty()){
			String toCurrencyName = "";
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppPortfolioProductVo  vo = new AppPortfolioProductVo();
				PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
				FundInfo fundInfo = (FundInfo) objs[1];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[2], fundInfoSc);
				
				vo.setId(product.getId());			
				vo.setFundId(fundInfo.getId());
				//vo.setAllocationRate(objs[1]==null?"":objs[1].toString());
				vo.setFundName(fundInfoSc.getFundName());
//				String fromCurrency = product.getBaseCurrency();
				
				if(null == toCurrency || "".equals(toCurrency)){
					toCurrency = product.getPortfolioHold().getBaseCurrency();
					toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
				}
				
				vo.setFundCurrency(fundInfoSc.getFundCurrency());
				vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
				vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
				vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());				
				vo.setFundType(fundInfoSc.getFundType());
				vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));

				double lastNav = fundInfo.getLastNav()==null?0:fundInfo.getLastNav();
				double refCost = product.getReferenceCost()==null?0:product.getReferenceCost();
				if(!"".equals(toCurrency)){
					vo.setToCurrency(toCurrencyName);
					lastNav = getNumByCurrency(lastNav, fundInfoSc.getFundCurrencyCode(), toCurrency);
					refCost = getNumByCurrency(refCost, product.getBaseCurrency(), toCurrency);
					vo.setLastNav(getFormatNum(lastNav,toCurrency));
				}else {
					vo.setToCurrency(getParamConfigName(langCode, fundInfoSc.getFundCurrencyCode(), "currency_type"));
					vo.setLastNav(getFormatNum(fundInfo.getLastNav(),fundInfoSc.getFundCurrencyCode()));					
				}
				
							
				String productId = product.getProduct() == null? "": product.getProduct().getId();
				double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
				vo.setHoldingUnit(getFormatNum(product.getHoldingUnit()));
				vo.setAvailableUnit(getFormatNum(product.getAvailableUnit()));
				vo.setProductId(productId);
				vo.setPortfolioHoldId(portfolioHoldId);
				
				

				Double marketValue = lastNav*holdingUnit;					
				Double totalPl = (lastNav-refCost)*holdingUnit;				
				Double cumperfRate = (lastNav-refCost)/refCost;
				
				totalAmount += marketValue;
				
				vo.setMarketValue(getFormatNum(marketValue,toCurrency));
				vo.setReturnValue(getFormatNum(totalPl,toCurrency));
				vo.setCumperfRate(getFormatNumByPer(cumperfRate));	
				
				vo.setAccountId(product.getAccount()==null?null:product.getAccount().getId());
				vo.setAccountNo(product.getAccountNo());
							
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
	}
	
	
	/**
	 * 得到我的组合详情的基金产品信息列表
	 * @author zxtan
	 * @date 2017-03-27
	 * @param portfolioHoldId 组合Id
	 * @param productType 产品类型
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppHoldProductVO> findHoldProductList(String portfolioHoldId,String productType,String langCode,String toCurrency){
		List<AppHoldProductVO>  voList = new ArrayList<AppHoldProductVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" Inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" Inner join ProductInfo p on p.id=m.product.id ");
		hql.append(" where h.id=? and p.type=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(productType);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			double totalAmount = 0;	
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
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
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	public AppPortfolioProductDetailVO findPortfolioProductDetail(String holdProductId,String langCode,String toCurrency){

		AppPortfolioProductDetailVO  vo = new AppPortfolioProductDetailVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");	
		hql.append(" where m.id=? ");
		
		List params = new ArrayList();
		params.add(holdProductId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
			PortfolioHold hold = (PortfolioHold) objs[1];
			FundInfo fundInfo = (FundInfo) objs[2];
			FundInfoSc fundInfoSc = new FundInfoSc();
			if(null != objs[3]){
				BeanUtils.copyProperties(objs[3], fundInfoSc);
				vo.setFundId(fundInfoSc.getId());
				vo.setFundName(fundInfoSc.getFundName());
				vo.setFundType(fundInfoSc.getFundType());
			}
			
			vo.setId(product.getId());
			String productId = product.getProduct().getId();
			String portfolioHoldId = hold.getId();
			vo.setProductId(productId);
			vo.setPortfolioHoldId(portfolioHoldId);
			vo.setPortfolioName(hold.getPortfolioName());
			if(null != fundInfo.getRiskLevel()){
				vo.setRiskLevel(fundInfo.getRiskLevel().toString());
			}
			vo.setFundCurrency(fundInfoSc.getFundCurrency());
			vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
			vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
			vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
			vo.setToCurrency(getParamConfigName(langCode, toCurrency, "currency_type"));
			
			double lastNav = getNumByCurrency(fundInfo.getLastNav(), fundInfoSc.getFundCurrencyCode(), toCurrency);
			vo.setLastNav(getFormatNum(lastNav));
			vo.setLastNavUpdate(DateUtil.dateToDateString(fundInfo.getLastNavUpdate(), "yyyy-MM-dd"));
			vo.setDayReturn(getFormatNumByPer(fundInfo.getDayReturn()));
			Double cost = 0.0;
			if(null != product.getReferenceCost()){
				cost = getNumByCurrency(product.getReferenceCost(), product.getBaseCurrency(), toCurrency);
				vo.setReferenceCost( getFormatNum( cost,toCurrency ));
			}
			Double unit = 0.0;
			if(null != product.getHoldingUnit()){
				unit = product.getHoldingUnit();
				//Double unitPrice = cost / unit;
				vo.setHoldingUnit(String.valueOf(unit));
				//vo.setReferenceUnitPrice(getFormatNum(unitPrice));
			}
			if(null != product.getAvailableUnit()){
				vo.setAvailableUnit(product.getAvailableUnit().toString());
			}
			
			double totalMarketValue = lastNav*unit ;
			double totalPl = (lastNav-cost)*unit;
			Double cumperfRate = cost==0?null:1.0*(lastNav-cost)/cost;
							
			vo.setMarketValue(getFormatNum(totalMarketValue,toCurrency));
			vo.setReturnValue(getFormatNum(totalPl,toCurrency));
			vo.setCumperfRate(getFormatNumByPer(cumperfRate));	
			vo.setValuationDate(DateUtil.dateToDateString(fundInfo.getLastNavUpdate(), "yyyy-MM-dd"));
			
			double totalAmount = findPortfolioFundTotalMarketValue(portfolioHoldId,toCurrency);
			if(totalAmount > 0){
				double rate = totalMarketValue/totalAmount;
				vo.setAllocationRate(getFormatNumByPer(rate));
			}
			
			
			if(null != objs[4]){
				InvestorAccount account = (InvestorAccount) objs[4];
				vo.setDividend(account.getDividend());
				vo.setAccountId(account.getId());
			}
			
			
		}
				
		return vo;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	private Double findPortfolioFundTotalMarketValue(String portfolioId,String toCurrency){
//		List<AppPortfolioProductVo>  messList = new ArrayList<AppPortfolioProductVo>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join FundInfoEn s on s.id=f.id ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		double totalAmount = 0.0;
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];			
//			String productId = product.getProduct().getId();
			FundInfo fundInfo = (FundInfo) objs[1];
			FundInfoEn fundInfoEn = (FundInfoEn) objs[2];
			double unit = product.getHoldingUnit()==null?0.0:product.getHoldingUnit();
			double lastNav = fundInfo.getLastNav()==null?0.0:fundInfo.getLastNav();
			Double marketValue = getNumByCurrency(lastNav*unit, fundInfoEn.getFundCurrencyCode(), toCurrency);
//			marketValue = marketValue==null?0.0:marketValue;
			if(null != marketValue){
				totalAmount += marketValue;		
			}
		}
				
		return totalAmount;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioHoldId 组合ID
	 * @param productId 产品ID
	 * @param toCurrency 显示货币
	 * @return
	 */
	public List<AppPortfolioOrderHistoryVO> findPortfolioOrderHistoryList(String portfolioHoldId,String productId,String toCurrency,String langCode,String keyword){
		List<AppPortfolioOrderHistoryVO>  messList = new ArrayList<AppPortfolioOrderHistoryVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory m ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" where 1=1 ");

		List params = new ArrayList();
				
		if(!"".equalsIgnoreCase(portfolioHoldId)){
			hql.append(" and m.portfolioHold.id=? ");
			params.add(portfolioHoldId);
		}
		
		if(!"".equalsIgnoreCase(productId)){
			hql.append(" and m.product.id=? ");
			params.add(productId);
		}
		
		if(!"".equalsIgnoreCase(keyword)){
			hql.append(" and ( s.fundName like ? or p.portfolioName like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		hql.append(" order by m.updateTime desc ");		
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			OrderHistory history = (OrderHistory) objs[0];
			InvestorAccount account = (InvestorAccount) objs[1];
			AppPortfolioOrderHistoryVO vo = new AppPortfolioOrderHistoryVO();
			FundInfoSc fundInfo = new FundInfoSc();
			
			if(null != objs[3]){
				BeanUtils.copyProperties(objs[3], fundInfo);
				vo.setFundName(fundInfo.getFundName());
			}
			if(null != objs[4]){
				PortfolioHold portfolioHold = (PortfolioHold) objs[4];
				vo.setPortfolioName(portfolioHold.getPortfolioName());
			}
			
			vo.setId(history.getId());
			//vo.setOrderNo(history.getOrderNo());
			vo.setAccountNo(account.getAccountNo());
			String currencyCode = history.getCurrencyCode();
			
			if("".equals(toCurrency)){
				vo.setCurrencyCode(getParamConfigName(langCode, currencyCode, "currency_type"));
			}else {
				vo.setCurrencyCode(toCurrencyName);
			}
			
			if(null != history.getCommissionUnit()){
				vo.setCommissionUnit(history.getCommissionUnit().toString());
			}
			
			if(null != history.getCommissionPrice()){
				vo.setCommissionPrice(getFormatNumByCurrency(history.getCommissionPrice(), currencyCode, toCurrency));
			}
			if(null != history.getCommissionAmount()){
				vo.setCommissionAmount(getFormatNumByCurrency(history.getCommissionAmount(),currencyCode,toCurrency));
			}
			
			if(null != history.getTransactionUnit()){
				vo.setTransactionUnit(history.getTransactionUnit().toString());
			}			
			if(null != history.getTransactionAmount()){
				vo.setTransactionAmount(getFormatNumByCurrency(history.getTransactionAmount(),currencyCode,toCurrency));
			}
			if(null != history.getFee()){
				vo.setFee(getFormatNumByCurrency(history.getFee(),currencyCode,toCurrency));
			}	
			vo.setOrderType(history.getOrderType());
			vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setStatus(history.getStatus());
			vo.setIfAip(history.getIfAip());
			vo.setSwitchFlag(history.getSwitchFlag());
			
			messList.add(vo);
		}
				
		return messList;
	}
	
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findPortfolioAllocationList(String portfolioHoldId,String langCode,String toCurrency){
		List<AppPortfolioAllocationVO>  voList = new ArrayList<AppPortfolioAllocationVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join ProductInfo f on f.id=m.product.id ");		
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppPortfolioHoldProductVO> productVOList = new ArrayList<AppPortfolioHoldProductVO>();
		double fundTotal = 0;
		double bondTotal = 0;
		double stockTotal = 0;
		double insureTotal = 0;
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
				AppPortfolioHoldProductVO  vo = new AppPortfolioHoldProductVO();
				vo.setId(product.getId());
				double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
				double availableUnit = product.getAvailableUnit()==null?0:product.getAvailableUnit();
				
				vo.setHoldingUnit(holdingUnit);			
				vo.setAvailableUnit(availableUnit);
				
	
				ProductInfo info = (ProductInfo) objs[1];
				String productType = info.getType();
				vo.setProductType(productType);	
	
				String productId = info.getId();
	
				AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
				String fromCurrency = productInfoVO.getBaseCurrency();
				
				Double referenceCost;
				Double lastPrice;
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setBaseCurrency(toCurrency);
					referenceCost = getNumByCurrency(product.getReferenceCost(), product.getBaseCurrency(), toCurrency);
					lastPrice = getNumByCurrency(productInfoVO.getLastNav(), fromCurrency, toCurrency);				
				}else {
					vo.setBaseCurrency(product.getBaseCurrency());
					referenceCost = product.getReferenceCost();
					lastPrice = getNumByCurrency(productInfoVO.getLastNav(), fromCurrency, product.getBaseCurrency());
				}
				lastPrice = lastPrice==null?0.0:lastPrice;
				vo.setReferenceCost(referenceCost);
				
				Double totalMarketValue = lastPrice*holdingUnit;
				Double cumperfRate = (referenceCost == null || referenceCost == 0)?0:(lastPrice-referenceCost)/referenceCost;
				vo.setTotalMarketValue(totalMarketValue);
				vo.setCumperfRate(cumperfRate);
				
				if("fund".equalsIgnoreCase(productType)){
					fundTotal += totalMarketValue;
				}else if ("bond".equalsIgnoreCase(productType)) {
					bondTotal += totalMarketValue;
				}else if ("stock".equalsIgnoreCase(productType)) {
					stockTotal += totalMarketValue;
				}else if ("insure".equalsIgnoreCase(productType)) {
					insureTotal += totalMarketValue;
				}
							
				productVOList.add(vo);	
			}
			
		}
		
		
		double fundRate = 0;
		double bondRate = 0;
		double stockRate = 0;
		double insureRate = 0;
		for (int i = 0; i < productVOList.size(); i++) {
			AppPortfolioHoldProductVO  vo = productVOList.get(i);
			double marketValue = vo.getTotalMarketValue()==null?0:vo.getTotalMarketValue();
			double cumperfRate = vo.getCumperfRate()==null?0:vo.getCumperfRate();
			String productType = vo.getProductType();
			
			if("fund".equalsIgnoreCase(productType) && fundTotal != 0){
				
					fundRate += marketValue * cumperfRate / fundTotal;
				
			}else if ("bond".equalsIgnoreCase(productType) && bondTotal != 0) {
				
					bondRate += marketValue * cumperfRate / bondTotal;
				
			}else if ("stock".equalsIgnoreCase(productType) && stockTotal != 0) {
				
				stockRate += marketValue * cumperfRate / stockTotal;
				
			}else if ("insure".equalsIgnoreCase(productType) && insureTotal != 0) {				
				insureRate += marketValue * cumperfRate / insureTotal;				
			}
		}
		
		double cashTotal = findPortfolioHoldCash(portfolioHoldId, toCurrency);
		double total = fundTotal+bondTotal+stockTotal+insureTotal+cashTotal;
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		
		if(fundTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("fund", fundTotal, fundRate, total,toCurrencyName);
			voList.add(vo);
		}
		
		if(bondTotal>0){		
			AppPortfolioAllocationVO vo = genPortfolioAllocation("bond", bondTotal, bondRate, total,toCurrencyName);
			voList.add(vo);
		}
		
		if(stockTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("stock", stockTotal, stockRate, total,toCurrencyName);
			voList.add(vo);
		}

		if(insureTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("insure", insureTotal, insureRate, total,toCurrencyName);
			voList.add(vo);
		}
		
		if(cashTotal>0){
			AppPortfolioAllocationVO vo = genPortfolioAllocation("cash", cashTotal, 0, total,toCurrencyName);
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
	private AppPortfolioAllocationVO genPortfolioAllocation(String itemName,double itemValue,double itemIncrease,double total,String toCurrency)
	{
		AppPortfolioAllocationVO vo = new AppPortfolioAllocationVO();
		vo.setItemName(itemName);
		vo.setItemValue(getFormatNum(itemValue));
		vo.setItemIncrease(getFormatNumByPer(itemIncrease));
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
	private double findPortfolioHoldCash(String portfolioHoldId,String toCurrency){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldAccount m ");		
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		double totalCash = 0;
		List<PortfolioHoldAccount> list = baseDao.find(hql.toString(), params.toArray(),false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				PortfolioHoldAccount holdAccount = list.get(i);
				double cash = holdAccount.getTotalCash()==null?0:holdAccount.getTotalCash();
				Double rate = getExchangeRate(holdAccount.getBaseCurrency(), toCurrency);
				if(null != rate){
					totalCash += cash*rate;
				}
			}
		}
		return totalCash;
	}
	
	
//	/**
//	 * 获取产品最新一条回报数据
//	 * @author zxtan
//	 * @date 2016-12-27
//	 * @param portfolioId
//	 * @param productId
//	 * @return
//	 */
//	private PortfolioHoldProductCumperf findPortfolioHoldProductCumperf(String portfolioHoldId,String productId){
////		List<AppPortfolioAllocationVO>  voList = new ArrayList<AppPortfolioAllocationVO>();
//		StringBuilder hql = new StringBuilder();
//		hql.append(" from PortfolioHoldProductCumperf m ");		
//		hql.append(" where m.portfolioHold.id=?  and m.product is not null and m.product.id=? ");
//		hql.append(" order by m.valuationDate desc ");
//		
//		List params = new ArrayList();
//		params.add(portfolioHoldId);
//		params.add(productId);
//		
//		List<PortfolioHoldProductCumperf> list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
//		if(null != list && !list.isEmpty())
//			return list.get(0);
//		else {
//			return null;
//		}
//	}
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	public List<AppPortfolioHoldProductCumperfVO> findPortfolioHoldProductCumperfList(String portfolioHoldId,String productId,String startDate,String toCurrency,String langCode){
		List<AppPortfolioHoldProductCumperfVO>  voList = new ArrayList<AppPortfolioHoldProductCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProductCumperf m ");		
		hql.append(" where m.portfolioHold.id=? and m.product.id=? ");
		hql.append(" and m.valuationDate > ? ");
		hql.append(" order by m.valuationDate ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(productId);
		params.add(DateUtil.getDate(startDate));
		
		List<PortfolioHoldProductCumperf> list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				PortfolioHoldProductCumperf cumperf = list.get(i);
				AppPortfolioHoldProductCumperfVO vo = new AppPortfolioHoldProductCumperfVO();
				
				vo.setId(cumperf.getId());
				vo.setCumperfRate(getFormatNum(cumperf.getCumperfRate()));
				if(null == toCurrency || "".equals(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, cumperf.getBaseCurrency(), "currency_type"));
					vo.setTotalPl(getFormatNum(cumperf.getTotalPl(),cumperf.getBaseCurrency()));
					vo.setDayPl(getFormatNum(cumperf.getDayPl(),cumperf.getBaseCurrency()));
					vo.setTotalMarketValue(getFormatNum(cumperf.getTotalMarketValue(),cumperf.getBaseCurrency()));
				}else {
					vo.setBaseCurrency(toCurrencyName);
					Double rate=getExchangeRate(cumperf.getBaseCurrency(),toCurrency);
					vo.setTotalPl(getFormatNumByRate(cumperf.getTotalPl(), rate,toCurrency));
					vo.setDayPl(getFormatNumByRate(cumperf.getDayPl(), rate,toCurrency));
					vo.setTotalMarketValue(getFormatNumByRate(cumperf.getTotalMarketValue(), rate,toCurrency));
				}

				vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
				vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd"));
				vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd"));
				
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取组合的费用列表
	 * @author zxtan
	 * @date 2017-02-07
	 * @param holdId
	 * @param langCode
	 * @param toCurrency
	 * @return
	 */
	public List<AppPortfolioFeeVO> findPortfolioFeeList(String holdId,String langCode,String toCurrency){
		List<AppPortfolioFeeVO> voList = new ArrayList<AppPortfolioFeeVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory o ");
		hql.append(" inner join FundInfo f on f.product.id=o.product.id ");
		hql.append(" inner join "+getLangString("FundInfo",langCode)+" l on l.id=f.id ");
		hql.append(" inner join PortfolioHold h on h.id=o.portfolioHold.id ");		
		hql.append(" where o.status in ('2','3') and h.id=? ");
		hql.append(" order by o.updateTime desc ");
		List<String> params = new ArrayList<String>();
		params.add(holdId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppPortfolioFeeVO vo = new AppPortfolioFeeVO();
				Object[] objs = (Object[]) list.get(i);
				OrderHistory orderHistory = (OrderHistory) objs[0];
				FundInfoSc fundInfo = new FundInfoSc();
				BeanUtils.copyProperties(objs[2], fundInfo);
				
				vo.setProductId(orderHistory.getProduct().getId());
				vo.setHoldId(holdId);
				vo.setFundName(fundInfo.getFundName());
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setCurrency(toCurrencyName);
					vo.setFee(getFormatNumByCurrency(orderHistory.getFee(), orderHistory.getCurrencyCode(), toCurrency));
				}else {
					vo.setCurrency(getParamConfigName(langCode, orderHistory.getCurrencyCode(), "currency_type"));
					vo.setFee(getFormatNum(orderHistory.getFee(),orderHistory.getCurrencyCode()));
				}
				vo.setCreateTime(DateUtil.dateToDateString(orderHistory.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				voList.add(vo);
			}
		}
				
		return voList;
	}
	
	/**
	 * 得到我的投资顾问创建的投资组合列表
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param periodCode 回报周期
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppPortfolioHoldVO> findMyIfaPortfolioList(String memberId,String ifaId,String periodCode,String toCurrency,String langCode){
		List<AppPortfolioHoldVO>  messList = new ArrayList<AppPortfolioHoldVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id and r.periodCode=? ");
		hql.append(" where h.member.id=? and h.ifa.id=? ");
		hql.append(" order by h.lastUpdate desc ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add(memberId);
		params.add(ifaId);
				
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHold hold=(PortfolioHold)objs[0];
			AppPortfolioHoldVO vo =new AppPortfolioHoldVO();
			vo.setId(hold.getId());
			vo.setPortfolioName(hold.getPortfolioName());
			vo.setBaseCurrency(hold.getBaseCurrency());
			vo.setCreateTime(DateUtil.dateToDateString(hold.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
		    
			vo.setTotalReturnRate(getFormatNumByPer(hold.getTotalReturnRate()));
			
			if( null==toCurrency || "".equals(toCurrency) ){
				vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), "currency_type"));
			    vo.setTotalMarketValue(getFormatNum(hold.getTotalMarketValue(),hold.getBaseCurrency()));
			    vo.setTotalReturnValue(getFormatNum(hold.getTotalReturnValue(),hold.getBaseCurrency()));
			    vo.setTotalCash(getFormatNum(hold.getTotalCash(),hold.getBaseCurrency()));
			    vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
			}else {
				vo.setBaseCurrency(toCurrencyName);
				String fromCurrency = hold.getBaseCurrency();
				if(StringUtils.isBlank(fromCurrency)) fromCurrency = CommonConstants.DEF_CURRENCY_HKD;
			    Double rate=getExchangeRate(fromCurrency,toCurrency);
			    vo.setTotalMarketValue(getFormatNumByRate(hold.getTotalMarketValue(),rate,toCurrency));
			    vo.setTotalReturnValue(getFormatNumByRate(hold.getTotalReturnValue(),rate,toCurrency));
			    vo.setTotalCash(getFormatNumByRate(hold.getTotalCash(),rate,toCurrency));
			    vo.setTotalAsset(getFormatNumByRate(hold.getTotalAsset(),rate,toCurrency));
			}
					    
		    if(null != objs[1]){
		    	PortfolioHoldReturn holdReturn = (PortfolioHoldReturn) objs[1];
		    	vo.setPeriodCode(periodCode);
		    	vo.setIncrease(getFormatNumByPer(holdReturn.getIncrease()));
		    }
		    			
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-17
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPieChartItemVO> findPortfolioFundAllocationList(String portfolioHoldId,String langCode,String toCurrency,String groupType){

		List<AppPieChartItemVO>  voList = new ArrayList<AppPieChartItemVO>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" INNER JOIN PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" LEFT JOIN ProductInfo p on p.id=m.product.id ");	
		hql.append(" LEFT JOIN FundInfo f ON f.product.id = m.product.id ");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");	
		hql.append(" where p.type='fund' and m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		double total = 0;
		Map<String, Double> map = new HashMap<String, Double>();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
				PortfolioHold hold = (PortfolioHold) objs[1];
				FundInfo fundInfo = (FundInfo) objs[3];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[4], fundInfoSc);
				
				String keyString = fundInfo.getId();
							
				toCurrency = "".equals(toCurrency)? hold.getBaseCurrency():toCurrency;
				String fromCurrency = fundInfoSc.getFundCurrencyCode()==null?product.getBaseCurrency():fundInfoSc.getFundCurrencyCode();
				
				double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit(); 
				double lastNav = fundInfo.getLastNav()==null?0:fundInfo.getLastNav();
				Double totalMarketValue = 0.0;
				totalMarketValue = lastNav * holdingUnit;
				totalMarketValue = getNumByCurrency(totalMarketValue, fromCurrency, toCurrency);
				totalMarketValue = totalMarketValue==null?0:totalMarketValue;
				total += totalMarketValue;
				if(map.containsKey(keyString)){
					map.put(keyString, map.get(keyString) + totalMarketValue);	
				}else {
					map.put(keyString, totalMarketValue);
				}
			}
		}

		Map<String, Double> mapFP = new HashMap<String, Double>();
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			
			String fundId = entry.getKey();
			double weight = entry.getValue()/total;			
			
			hql = new StringBuilder();
			hql.append(" from FundInfo f ");	
			hql.append(" inner JOIN FundPortfolio p ON p.fund.id = f.id ");	
			hql.append(" inner JOIN "+this.getLangString("FundPortfolio", langCode)+" l ON l.id = p.id ");	
			hql.append(" where f.id=? and p.type=? ");
			
			params = new ArrayList();
			params.add(fundId);
			params.add(groupType);
			
			List listFP = baseDao.find(hql.toString(), params.toArray(), false);
			
			if(null != listFP && !listFP.isEmpty()){			

				for(int i=0;i<listFP.size();i++){
					Object[] objs = (Object[])listFP.get(i);				
//					FundInfo fundInfo = (FundInfo) objs[0];
					FundPortfolio fundPortfolio = (FundPortfolio) objs[1];
					FundPortfolioSc fundPortfolioSc = new FundPortfolioSc();
					BeanUtils.copyProperties(objs[2], fundPortfolioSc);
//					String productId = fundInfo.getProduct().getId();
					
					String keyString = fundPortfolioSc.getName();
					double rate = (fundPortfolio.getRate()==null)?0:fundPortfolio.getRate();
					
					if(mapFP.containsKey(keyString)){
						mapFP.put(keyString, mapFP.get(keyString) + weight*rate/100.0);	
					}else {
						mapFP.put(keyString, weight*rate/100.0);
					}
				}
			}
		}	
		
		for (Map.Entry<String, Double> entry : mapFP.entrySet()) {
			AppPieChartItemVO vo = new AppPieChartItemVO();
			vo.setItemId(null);
			vo.setItemName(entry.getKey());
			double weight = entry.getValue()==null?0:entry.getValue();
			weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			vo.setItemWeight(String.valueOf(weight));
			voList.add(vo);
		}
		
		return voList;
	}
}
