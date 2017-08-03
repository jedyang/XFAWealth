package com.fsll.app.investor.me.vo;

/**
 * 投资组合实体类VO
 * @author zpzhou
 * @date 2016-9-14
 */
public class AppPortfolioMessVo {
	private String id;
	private String portfolioName;
	private String investmentGoal;
	private String suitability;
	private String baseCurrency;
	private String riskLevel;
	private String lastUpdate;//持仓表中的更新时间
	private String createTime;
	private String returnRate;
	private String totalReturn;
	private String totalAmount;
	
	private String periodCode;
	private String increase;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getInvestmentGoal() {
		return investmentGoal;
	}
	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}
	public String getSuitability() {
		return suitability;
	}
	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
	public String getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(String totalReturn) {
		this.totalReturn = totalReturn;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
}
