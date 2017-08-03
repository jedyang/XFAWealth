package com.fsll.app.investor.market.vo;

/**
 * 组合用于展示的产品实体类VO
 * @author zpzhou
 * @date 2016-9-18
 */
public class AppPortfolioArenaProductVo {
	private String id;
	private String portfolioId;
	private String productId;
	private String allocationRate;//分配比率
	private String fundId;
	private String fundName;
	private String fundType;
	private String riskLevel;//风险评级
	private String fundCurrency;
	private String fundCurrencyCode;
	private String issueCurrency;
	private String issueCurrencyCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getAllocationRate() {
		return allocationRate;
	}
	public void setAllocationRate(String allocationRate) {
		this.allocationRate = allocationRate;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
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
	
}
