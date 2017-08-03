package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.fund.service.FundPortfolioService;
import com.fsll.wmes.fund.vo.FundCompositionDataVO;

/**
 * @author scshi
 * 组合内容接口实现
 *
 */
@Service("fundPortfolioService")
//@Transactional
public class FundPortfolioServiceImpl extends BaseService implements FundPortfolioService {
	
	/***
	 * 基金组合内容列表查询方法
	 * @author scshi
	 * @date 2016-6-17
	 * @param queryPortfolio
	 * @param langCode 语言
	 * @return list
	 */ 
	@Override
	//@Transactional(readOnly=true)
	public List<FundCompositionDataVO> findPortfolioByType(FundPortfolio queryPortfolio,String langCode) {
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
		List<FundCompositionDataVO> list = new ArrayList();
		if(!volist.isEmpty()){
			for(int x=0;x<volist.size();x++){
				FundCompositionDataVO vo = new FundCompositionDataVO();
				Object[] objs = (Object[])volist.get(x);
				vo.setFundId(fundId);
				vo.setCategory(type);
				vo.setItemName(objs[0]==null?"":objs[0].toString());
				vo.setValue(objs[1]==null?"":StrUtils.getNumberString(objs[1].toString()));
				list.add(vo);
			}
		}
		return list;
	}
}
