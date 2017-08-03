/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundBonus;
import com.fsll.wmes.fund.service.FundBonusService;
import com.fsll.wmes.fund.vo.FundDividendDataVO;
import com.fsll.wmes.fund.vo.FundYearPerformanceDataVO;

/**
 * @author scshi
 *	基金分红派息资料接口实现
 */
@Service("fundAnnoService")
//@Transactional
public class FundBonusServiceImpl extends BaseService implements FundBonusService {
	/**3.1.10	获取基金分红派息资料
	 * @author scshi
	 * @param fundId 资金id
	 * @return	<List>FundDividendDataVO	基金历史分红派息数据
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<FundDividendDataVO> fundDividendInfo(String fundId) {
		String hql = "from FundBonus t where t.isValid='1' ";
		List params = new ArrayList();
		if(null!=fundId && !"".equals(fundId)){
			hql +=" and t.fund.id=? ";
			params.add(fundId);
		}
		hql +=" order by t.lastUpdate desc";
		List<FundBonus> preList = this.baseDao.find(hql, params.toArray(), false);
		List<FundDividendDataVO> voList = new ArrayList();
		if(!preList.isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			for(FundBonus preVo:preList){
				FundDividendDataVO vo = new FundDividendDataVO();
				//除息日期
				String exDividendDate = preVo.getExDividendDate()==null?"":sdf.format(preVo.getExDividendDate());
				//年化股息率
				String annualDividendYield = preVo.getAnnualDividendYield()==null?"":preVo.getAnnualDividendYield().toString();
				//每单位股息
				String dividenPerUnit = preVo.getDividendPerUnit()==null?"":preVo.getDividendPerUnit().toString();
				
				vo.setAnnualDividend(annualDividendYield);
				vo.setDividenPerUnit(dividenPerUnit);
				vo.setExDividendDate(exDividendDate);
				voList.add(vo);
			}
		}
		return voList;
	}

	/**3.1.10	获取基金分红派息信息
	 * @author scshi
	 * @param jsonPaging 分页信息
	 * @param fundId 基金id
	 * @return	jsonPaging	基金公告列表数据
	 */
	//@Transactional(readOnly=true)
	public JsonPaging fundDividendInfoList(JsonPaging jsonPaging ,String fundId) {
//		String hql = "from FundBonus t where t.isValid='1' ";
		String hql = "select t.fund.id, t.annualDividendYield, t.dividenPerUnit,date_format(t.exDividendDate,'%Y-%m-%d') as exDividendDate,c.fundCurrency from FundBonus t " +
				"left join FundInfoEn c on t.fund.id=c.id where t.isValid='1' ";
		List params = new ArrayList();
		
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			params.add(fundId);
		}

		//hql += "order by t.exDividendDate desc";
		List<FundDividendDataVO> voList = new ArrayList();
//		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		jsonPaging=this.baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
//		List<FundBonus> preList = jsonPaging.getList();
//		if(!preList.isEmpty()){
//			for(FundBonus preVo: preList){
//				FundDividendDataVO vo = new FundDividendDataVO();
//				//除息日期
//				String exDividendDate = DateUtil.dateToDateString(preVo.getExDividendDate(), CoreConstants.DATE_FORMAT);
//				//年化股息率
//				String annualDividendYield = preVo.getAnnualDividendYield()==null?"":preVo.getAnnualDividendYield().toString();
//				//每单位股息
//				String dividenPerUnit = preVo.getDividenPerUnit()==null?"":preVo.getDividenPerUnit().toString();
//				
//				vo.setAnnualDividend(annualDividendYield);
//				vo.setDividenPerUnit(dividenPerUnit);
//				vo.setExDividendDate(exDividendDate);
//				voList.add(vo);
//			}
//		}
		List preList = jsonPaging.getList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				FundDividendDataVO vo = new FundDividendDataVO();
				Object[] objs = (Object[])preList.get(x);
				
				//除息日期
				String exDividendDate = StrUtils.getString(objs[3]);
				//年化股息率
				String annualDividendYield = StrUtils.getString(objs[1]);
				//每单位股息
				String dividenPerUnit = StrUtils.getString(objs[2])+StrUtils.getString(objs[4]);
				
				vo.setAnnualDividend(annualDividendYield);
				vo.setDividenPerUnit(dividenPerUnit);
				vo.setExDividendDate(exDividendDate);
				
				voList.add(vo);
			}
		}
		jsonPaging.setTotal(voList.size());
		jsonPaging.setList(voList);
		
		return jsonPaging;
	}
}
