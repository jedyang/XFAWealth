package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundBonusService;
import com.fsll.app.fund.vo.AppFundDividendDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;

/**
 * @author scshi
 *	基金分红派息资料接口实现
 */
@Service("appFundBonusService")
//@Transactional
public class AppFundBonusServiceImpl extends BaseService implements AppFundBonusService {
	
	/**3.1.10	获取基金分红派息信息
	 * @author scshi
	 * @param jsonPaging 分页信息
	 * @param fundId 基金id
	 * @return	jsonPaging	基金公告列表数据
	 */
	//@Transactional(readOnly=true)
	public JsonPaging fundDividendInfoList(JsonPaging jsonPaging ,String fundId) {
		String hql = "select t.fund.id, t.annualDividendYield, t.dividendPerUnit,date_format(t.exDividendDate,'%Y-%m-%d') as exDividendDate,c.fundCurrency from FundBonus t " +
				"left join FundInfoEn c on t.fund.id=c.id where t.isValid='1' ";
		List params = new ArrayList();
		
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			params.add(fundId);
		}
		List<AppFundDividendDataVO> voList = new ArrayList();
		jsonPaging=this.baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List preList = jsonPaging.getList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				AppFundDividendDataVO vo = new AppFundDividendDataVO();
				Object[] objs = (Object[])preList.get(x);
				//除息日期
				String exDividendDate = StrUtils.getString(objs[3]);
				//年化股息率
				String annualDividendYield =getFormatNumByPer(Double.parseDouble(objs[1].toString()));
				//每单位股息
				String dividenPerUnit = String.valueOf(objs[2])+StrUtils.getString(objs[4]);
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
