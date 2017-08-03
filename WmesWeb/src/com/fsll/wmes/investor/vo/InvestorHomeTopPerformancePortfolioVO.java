package com.fsll.wmes.investor.vo;

public class InvestorHomeTopPerformancePortfolioVO {
	private String portfolioId;
	private String portfolioName;
	private Double increasePercent;
	private String increaseImagePath;
	private String period;
	private Integer titleNum;
	private String riskLevel;
	private String increasePercentStr;
	
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
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
	public Double getIncreasePercent() {
		return increasePercent;
	}
	public void setIncreasePercent(Double increasePercent) {
		this.increasePercent = increasePercent;
	}
	public Integer getTitleNum() {
		return titleNum;
	}
	public void setTitleNum(Integer titleNum) {
		this.titleNum = titleNum;
	}
	
	
}
