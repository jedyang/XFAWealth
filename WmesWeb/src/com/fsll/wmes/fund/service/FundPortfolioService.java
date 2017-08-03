/**
 * 
 */
package com.fsll.wmes.fund.service;

import java.util.List;

import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.fund.vo.FundCompositionDataVO;

/**
 * @author scshi
 * 组合内容接口
 *
 */
public interface FundPortfolioService {
	
	/***
	 * 基金组合内容列表查询方法
	 * @author scshi
	 * @date 2016-6-17
	 * @param queryPortfolio
	 * @param langCode 语言
	 * @return list
	 */ 
	public List<FundCompositionDataVO> findPortfolioByType(FundPortfolio queryPortfolio,String langCode);
	
	
}
