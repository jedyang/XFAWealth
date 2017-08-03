package com.fsll.app.ifa.client.vo;

/**
 * IFA客户详情——投资组合列表实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppPortfolioHoldItemVO {
	private String id;//组合Hold ID
	private String portfolioName;//组合名称
	private String baseCurrency;//组合资产货币
	private String totalAsset;//总资产
	private String periodCode;//回报周期
	private String increase;//周期增长率
	private String investDays;//投资天数
	private String alertFlag;//是否超过IFA设置的组合收益率上下限
	private String riskLevel;//组合风险等级
	
	
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

	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}


	public String getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
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
	
	public String getInvestDays() {
		return investDays;
	}
	public void setInvestDays(String investDays) {
		this.investDays = investDays;
	}
	public String getAlertFlag() {
		return alertFlag;
	}
	public void setAlertFlag(String alertFlag) {
		this.alertFlag = alertFlag;
	}

	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}	
}
