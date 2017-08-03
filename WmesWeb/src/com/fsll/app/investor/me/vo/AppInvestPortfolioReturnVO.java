package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-08
 */
public class AppInvestPortfolioReturnVO {
	private String marketDate;//组合回报日期
	private String marketReturn;//组合回报率
		

	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}	
	public String getMarketReturn() {
		return marketReturn;
	}
	public void setMarketReturn(String marketReturn) {
		this.marketReturn = marketReturn;
	}	
}
