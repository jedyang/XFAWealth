package com.fsll.buscore.fund.vo;

import java.util.Date;
import java.util.List;

public class CoreFundsReturnForChartsVO {
	
	private String fundId; // 基金ID
	private String fundName; // 基金名称
	private List<Date> marketDates; // 行情日期
	private List<Double> navs; // 净值集合
	private List<Double> returnRates; // 收益集合
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public List<Date> getMarketDates() {
		return marketDates;
	}
	public void setMarketDates(List<Date> marketDates) {
		this.marketDates = marketDates;
	}
	public List<Double> getNavs() {
		return navs;
	}
	public void setNavs(List<Double> navs) {
		this.navs = navs;
	}
	public List<Double> getReturnRates() {
		return returnRates;
	}
	public void setReturnRates(List<Double> returnRates) {
		this.returnRates = returnRates;
	}
	
}
