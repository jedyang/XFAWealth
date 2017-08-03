package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundInfoService;
import com.fsll.app.fund.service.AppFundReturnService;
import com.fsll.app.fund.vo.AppFundCumulativePerformanceDataVO;
import com.fsll.app.fund.vo.AppFundYearPerformanceDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.StrUtils;

/**
 * @author scshi
 *组合内容接口实现
 */
@Service("appFundReturnService")
//@Transactional
public class AppFundReturnServiceImpl extends BaseService implements AppFundReturnService{

	@Autowired
	private AppFundInfoService fundInfoService;
	
	/**获取基金累积表现信息
	 * @author scshi
	 * @param fundId 基金id
	 * @param period 时期
	 * @param langCode 语言编码
	 * @return	<List>FundCumulativePerformanceDataVO	基金累积表现数据
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<AppFundCumulativePerformanceDataVO> fundCumulativePerformanceInfo(String fundId, String period, String langCode) {
		String hql = "select t.fund.id,t.increase,t.typeAverage,t.newRanking,t.lastRanking,t.periodCode,out.periodName from FundReturn t ";
		List params = new ArrayList();

		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.periodCode=out.periodCode where t.isValid = '1' and t.type='heap' ";

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
		List<AppFundCumulativePerformanceDataVO> list = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				AppFundCumulativePerformanceDataVO vo = new AppFundCumulativePerformanceDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				if(null!=objs[1])vo.setValue(StrUtils.getString(objs[1].toString()));//increase百分比
				if(null!=objs[2])vo.setTypeAverage(StrUtils.getString(objs[2]));//type average
				if(null!=objs[3])vo.setNewRanking(StrUtils.getString(objs[3]));//new ranking
				if(null!=objs[4])vo.setLastRanking(StrUtils.getString(objs[4]));//last ranking
				if(null!=objs[5])vo.setCode(StrUtils.getString(objs[5]));
				if(null!=objs[6])vo.setPeriod(StrUtils.getString(objs[6]));
				int fundCnt = fundInfoService.findFundTotalByType(vo.getFundId(),"","");
				vo.setTypeTotal(String.valueOf(fundCnt));
				//数据格式处理
				if(null==vo.getValue() || "".equals(vo.getValue())){
					vo.setValue(getFormatNumByPer(null));
				}else{
					vo.setValue(getFormatNumByPer(Double.parseDouble(vo.getValue())));
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**获取基金年度表现信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param year 获取的年度，如：2010,2012
	 * @param lastYears 获取上x年的数值，不包括今年（正常来说也不会有今年的数值）
	 * @return	<List>FundYearPerformanceDataVO	基金年度表现信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<AppFundYearPerformanceDataVO> fundYearPerformanceInfo(String fundId, String year, int lastYears, String langCode) {
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
		List<AppFundYearPerformanceDataVO> list = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				AppFundYearPerformanceDataVO vo = new AppFundYearPerformanceDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setValue(StrUtils.getString(objs[1].toString()));//increase百分比
				vo.setTypeAverage(StrUtils.getString(objs[2]));//type average
				vo.setNewRanking(StrUtils.getString(objs[3]));//new ranking
				vo.setLastRanking(StrUtils.getString(objs[4]));//last ranking
				vo.setCode(StrUtils.getString(objs[5]));
				vo.setYear(StrUtils.getString(objs[6]));
				//数据格式处理
				if(null==vo.getValue() || "".equals(vo.getValue())){
					vo.setValue(getFormatNumByPer(null));
				}else{
					vo.setValue(getFormatNumByPer(Double.parseDouble(vo.getValue())));
				}
				list.add(vo);
			}
		}
		return list;
	}

}
