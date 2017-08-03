package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-18
 */
public class AppHoldReturnVO {
	private String periodCode;
	private String increase;
	private List<AppHoldCumperfVO> cumperfList;//行情数据
	

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
		
	public List<AppHoldCumperfVO> getCumperfList() {
		return cumperfList;
	}
	public void setCumperfList(List<AppHoldCumperfVO> cumperfList) {
		this.cumperfList = cumperfList;
	}
	
	
}
