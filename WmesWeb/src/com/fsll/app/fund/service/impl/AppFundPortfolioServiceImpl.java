package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundPortfolioService;
import com.fsll.app.fund.vo.AppFundCompositionDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.FundPortfolio;

/**
 * @author scshi
 * 组合内容接口实现
 *
 */
@Service("appFundPortfolioService")
//@Transactional
public class AppFundPortfolioServiceImpl extends BaseService implements AppFundPortfolioService {
	
	/***
	 * 基金组合内容列表查询方法
	 * @author scshi
	 * @date 2016-6-17
	 * @param queryPortfolio
	 * @param LANG_CODE 语言
	 * @return list
	 */ 
	@Override
	//@Transactional(readOnly=true)
	public List<AppFundCompositionDataVO> findPortfolioByType(FundPortfolio queryPortfolio,String langCode) {
		String hql = "select out.name,t.rate from FundPortfolio t ";
		List args = new ArrayList();
		String type = queryPortfolio.getType();
		String fundId = queryPortfolio.getFundId();
		String isValid = queryPortfolio.getIsValid();
		hql += " left join "+this.getLangString("FundPortfolio", langCode);
		hql += " out on t.id=out.id where t.isValid='1'  ";
		
		if(null!= type&& !"".equals(type)){
			hql += " and t.type=? ";
			args.add(type);
		}
		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fund.id=? ";
			args.add(fundId);
		}
		if(null!=isValid && !"".equals(isValid)){
			hql +=" and t.isValid=? ";
			args.add(isValid);
		}else{
			hql += " and t.isValid='1' ";
		}
		hql += "order by t.rate desc";
		
		List volist = this.baseDao.find(hql, args.toArray(), false); 
		List<AppFundCompositionDataVO> list = new ArrayList();
		if(!volist.isEmpty()){
			for(int x=0;x<volist.size();x++){
				AppFundCompositionDataVO vo = new AppFundCompositionDataVO();
				Object[] objs = (Object[])volist.get(x);
				vo.setFundId(fundId);
				vo.setCategory(type);
				vo.setItemName(StrUtils.getString(objs[0].toString()));
				vo.setValue(StrUtils.getString(objs[1].toString()));//increase百分比
				//数据格式处理
				if(null==vo.getValue() || "".equals(vo.getValue())){
					vo.setValue(getFormatNum(null));
				}else{
					vo.setValue(getFormatNum(Double.parseDouble(vo.getValue())));
				}
				list.add(vo);
			}
		}
		return list;
	}
}
