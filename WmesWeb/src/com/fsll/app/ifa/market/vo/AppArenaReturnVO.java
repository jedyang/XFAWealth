package com.fsll.app.ifa.market.vo;

import java.util.List;

import com.fsll.buscore.fund.vo.CorePortfolioVO;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-18
 */
public class AppArenaReturnVO {
	private String periodCode;
	private String increase;
	private List<CorePortfolioVO> cumperfList;//行情数据
	

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
		
	public List<CorePortfolioVO> getCumperfList() {
		return cumperfList;
	}
	public void setCumperfList(List<CorePortfolioVO> cumperfList) {
		this.cumperfList = cumperfList;
	}
	
	
}
