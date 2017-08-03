package com.fsll.app.investor.me.vo;

/**
 * 组合产品实体类VO
 * @author zpzhou
 * @date 2016-9-14
 */
public class AppPortfolioProductVo {
	private String id;
	private String productId;
	private String portfolioHoldId;
	private String portfolioId;
	private String fundId;
	private String allocationRate;//分配比率
	private String fundName;
	private String issueCurrency;//基金发行时的货币
	private String issueCurrencyCode;//基金发行时的货币
	private String fundCurrency;//净值货币
	private String fundCurrencyCode;//净值货币
	private String toCurrency;
	private String fundType;
	private String riskLevel;//风险评级
	private String lastNav;//最新资产净值价格
	private String increase;//回报

	private String referenceCost;
	private String holdingUnit;
	private String availableUnit;
	private String cumperfRate;
	private String marketValue;
	private String returnValue;
	
	private String accountId;//投资账户Id
	private String accountNo;//投资账户编码
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPortfolioHoldId() {
		return portfolioHoldId;
	}
	public void setPortfolioHoldId(String portfolioHoldId) {
		this.portfolioHoldId = portfolioHoldId;
	}

	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
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
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
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

	
	public String getReferenceCost() {
		return referenceCost;
	}
	public void setReferenceCost(String referenceCost) {
		this.referenceCost = referenceCost;
	}
	public String getHoldingUnit() {
		return holdingUnit;
	}
	public void setHoldingUnit(String holdingUnit) {
		this.holdingUnit = holdingUnit;
	}
	public String getAvailableUnit() {
		return availableUnit;
	}
	public void setAvailableUnit(String availableUnit) {
		this.availableUnit = availableUnit;
	}
	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getCumperfRate() {
		return cumperfRate;
	}
	public void setCumperfRate(String cumperfRate) {
		this.cumperfRate = cumperfRate;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}
