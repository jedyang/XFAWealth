package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-18
 */
public class AppPortfolioReturnVO {
	private String periodCode;
	private String increase;
	private List<AppPortfolioHoldCumperfVO> cumperfList;//行情数据
	

	public String getPeriodCode(){
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getIncrease(){
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
		
	public List<AppPortfolioHoldCumperfVO> getCumperfList() {
		return cumperfList;
	}
	public void setCumperfList(List<AppPortfolioHoldCumperfVO> cumperfList) {
		this.cumperfList = cumperfList;
	}
	
	
}
