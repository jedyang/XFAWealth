/**
 * 
 */
package com.fsll.app.fund.service;

import java.util.List;

import com.fsll.app.fund.vo.AppFundCompositionDataVO;
import com.fsll.wmes.entity.FundPortfolio;

/**
 * @author scshi
 * 组合内容接口
 *
 */
public interface AppFundPortfolioService {
	
	/***
	 * 基金组合内容列表查询方法
	 * @author scshi
	 * @date 2016-6-17
	 * @param queryPortfolio
	 * @param LANG_CODE 语言
	 * @return list
	 */ 
	public List<AppFundCompositionDataVO> findPortfolioByType(FundPortfolio queryPortfolio,String langCode);
	
	
}
