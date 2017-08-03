package com.fsll.wmes.fund.vo;

import java.util.Date;
import java.util.Map;

public class FundIncomePercentageVO {
	
	//基金收益百分比
	private Double incomePercentage;
	//基金净值
	private Double nav;
	//日期
	private Date marketDate;

	public Double getIncomePercentage() {
		return incomePercentage;
	}

	public void setIncomePercentage(Double incomePercentage) {
		this.incomePercentage = incomePercentage;
	}

	public Date getMarketDate() {
		return marketDate;
	}

	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}

	public Double getNav() {
		return nav;
	}

	public void setNav(Double nav) {
		this.nav = nav;
	}
	
	
}
