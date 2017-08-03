package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.service.FundReturnService;
import com.fsll.wmes.fund.vo.ChartDataVO;
import com.fsll.wmes.fund.vo.FundCumulativePerformanceDataVO;
import com.fsll.wmes.fund.vo.FundYearPerformanceDataVO;

/**
 * @author scshi
 *组合内容接口实现
 */
@Service("fundReturnService")
//@Transactional
public class FundReturnServiceImpl extends BaseService implements FundReturnService{

	@Autowired
	private FundInfoService fundInfoService;
	
	/**3.1.5	获取基金累积表现图表数据 --- 此方法与需求不服，暂不使用。
	 * @author scshi
	 * @param fundId 资金id
	 * @param cycle 分析周期：D-日；W-周；M-月
	 * @param period 分析时间段，按分析周期设置
	 * @return	<List>ChartDataVO	基金图表数据
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<ChartDataVO> fundChartData(String fundId, String cycle,String period,String langCode) {
		List params = new ArrayList();

		String hql = "select t.fund.id,t.increase,t.typeAverage,t.newRanking,t.lastRanking,t.periodCode,out.periodName from FundReturn t ";
		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.periodCode=out.periodCode where t.isValid = '1' ";

		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		
		if(null!=period && !"".equals(period)){
			hql += "and out.periodCode=? ";
			params.add(period);
		}
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		List<ChartDataVO> list = new ArrayList();
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				ChartDataVO vo = new ChartDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setValue(objs[1]==null?"":objs[1].toString());
				vo.setDate(objs[2]==null?"":objs[2].toString());
				
//				if(null!=cycle && !"".equals(cycle)){
//				
//				}
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**3.1.6	获取基金累积表现信息
	 * @author scshi
	 * @param fundId 基金id
	 * @param period 时期
	 * @param langCode 语言编码
	 * @return	<List>FundCumulativePerformanceDataVO	基金累积表现数据
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<FundCumulativePerformanceDataVO> fundCumulativePerformanceInfo(String fundId, String period, String langCode) {
		String hql = "select t.fund.id,t.increase,t.typeAverage,t.newRanking,t.lastRanking,t.periodCode,out.periodName from FundReturn t ";
		List params = new ArrayList();

		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.periodCode=out.periodCode where t.isValid = '1' and t.type='heap' ";
//		hql += " out on t.id=out.id where t.isValid='1' and t.type='heap' ";

		
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			params.add(fundId);
		}
		if(null!=period && !"".equals(period)){
			hql += "and out.periodCode=? ";
			params.add(period);
		}
		hql += " order by t.periodCode ";
		List preList = this.baseDao.find(hql, params.toArray(), false);
		List<FundCumulativePerformanceDataVO> list = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				FundCumulativePerformanceDataVO vo = new FundCumulativePerformanceDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setValue(StrUtils.getString(objs[1]));//increase
				vo.setTypeAverage(StrUtils.getString(objs[2]));//type average
				vo.setNewRanking(StrUtils.getString(objs[3]));//new ranking
				vo.setLastRanking(StrUtils.getString(objs[4]));//last ranking
				
				vo.setCode(StrUtils.getString(objs[5]));
				vo.setPeriod(StrUtils.getString(objs[6]));
				
				try {
					int fundCnt = fundInfoService.findFundTotalByType(vo.getFundId(),"","");
					vo.setTypeTotal(String.valueOf(fundCnt));
				} catch (Exception e) {
					// TODO: handle exception
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**3.1.7	获取基金年度表现信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param year 获取的年度，如：2010,2012
	 * @param lastYears 获取上x年的数值，不包括今年（正常来说也不会有今年的数值）
	 * @return	<List>FundYearPerformanceDataVO	基金年度表现信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<FundYearPerformanceDataVO> fundYearPerformanceInfo(String fundId, String year, int lastYears, String langCode) {
		String hql = "select t.fund.id,t.increase,t.typeAverage,t.newRanking,t.lastRanking,t.periodCode,out.periodName as year from FundReturn t ";
		List params = new ArrayList();

		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.periodCode=out.periodCode where t.isValid = '1' and t.type='year' ";
		
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			params.add(fundId);
		}
		if(null!=year && !"".equals(year)){
			hql += " and out.periodName IN (?) ";
			year = year.replaceAll("\'", "");
			params.add(StrUtils.seperateWithQuote(year,"'"));
		}
		if(lastYears > 0){
			hql += " and (out.periodName>=YEAR(SYSDATE())-?) ";
			params.add(lastYears);
		}
		hql += " order by t.periodCode ";
		
		List preList = this.baseDao.find(hql, params.toArray(), false);
		List<FundYearPerformanceDataVO> list = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				FundYearPerformanceDataVO vo = new FundYearPerformanceDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setValue(StrUtils.getString(objs[1]));//increase
				vo.setTypeAverage(StrUtils.getString(objs[2]));//type average
				vo.setNewRanking(StrUtils.getString(objs[3]));//new ranking
				vo.setLastRanking(StrUtils.getString(objs[4]));//last ranking
				
				vo.setCode(StrUtils.getString(objs[5]));
				vo.setYear(StrUtils.getString(objs[6]));
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 统计全部基金所有周期的表现
	 * @author zxtan
	 * @date 2016-10-13
	 * @param jsonPaging
	 * @return 周期，最小增长值，最大增长值
	 */
	@Override
	public JsonPaging findPerformanceStatList(JsonPaging jsonPaging){
		String sql = "SELECT period_code,MIN(increase) min_increase,MAX(increase) as max_increase FROM fund_return GROUP BY period_code";
		jsonPaging = springJdbcQueryManager.springJdbcQueryForPaging(sql, null, jsonPaging);
		
		return jsonPaging;
	}

}
