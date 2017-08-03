package com.fsll.wmes.investor.vo;

public class InvestorHomeTopPerformanceFundsVO {
	private String fundId;
	private String fundName;
	private double increase;
	private String increaseImagePath;
	private String period;
	private int titleNum;
	private String riskLevel;
	private String increasePercentStr;
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
	public double getIncrease() {
		return increase;
	}
	public void setIncrease(double increase) {
		this.increase = increase;
	}
	public String getIncreaseImagePath() {
		return increaseImagePath;
	}
	public void setIncreaseImagePath(String increaseImagePath) {
		this.increaseImagePath = increaseImagePath;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getTitleNum() {
		return titleNum;
	}
	public void setTitleNum(int titleNum) {
		this.titleNum = titleNum;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getIncreasePercentStr() {
		return increasePercentStr;
	}
	public void setIncreasePercentStr(String increasePercentStr) {
		this.increasePercentStr = increasePercentStr;
	}
	
	
}
