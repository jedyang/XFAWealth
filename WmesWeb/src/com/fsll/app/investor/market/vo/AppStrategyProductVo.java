package com.fsll.app.investor.market.vo;

/**
 * 策略产品实体类VO
 * @author zpzhou
 * @date 2016-9-18
 */
public class AppStrategyProductVo {
	private String fundId;
	private String fundName;
	private String fundCurrency;
	private String fundCurrencyCode;
	private String issueCurrency;
	private String issueCurrencyCode;
	private String fundType;
	private String riskLevel;//风险评级
	private String lastNav;//最新资产净值价格
	private String increase;//回报
	private String productType;//产品类型,fund基金,stock股票,bond债券,futures期货 
	
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
	public String getFundCurrency() {
		return fundCurrency;
	}
	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}
	
	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}

	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}

	public String getIssueCurrency() {
		return issueCurrency;
	}

	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}
	public String getIssueCurrencyCode() {
		return issueCurrencyCode;
	}

	public void setIssueCurrencyCode(String issueCurrencyCode) {
		this.issueCurrencyCode = issueCurrencyCode;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getLastNav() {
		return lastNav;
	}
	public void setLastNav(String lastNav) {
		this.lastNav = lastNav;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
