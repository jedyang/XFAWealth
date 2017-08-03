package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 组合产品实体类VO
 * @author zxtan
 * @date 2017-4-28
 */
public class AppHoldProductDetailVO {
	private String id;
	private String productId;
	private String portfolioHoldId;
	private String portfolioId;
	private String portfolioName;
	private String accountId;
	private String fundId;
	private String fundName;
	private String fundCurrency;
	private String fundCurrencyCode;
	private String issueCurrency;
	private String issueCurrencyCode;
	private String toCurrency;
	private String fundType;
	private String riskLevel;//风险评级
	private String lastNav;//最新资产净值价格
	private String lastNavUpdate;//最新资产净值价格
	private String dayReturn;//回报

	private String referenceCost;
	private String referenceUnitPrice;
	private String holdingUnit;
	private String availableUnit;
	private String cumperfRate;
	private String marketValue;
	private String returnValue;	
	private String valuationDate;
	
	private String dividend;//账户分红方式
	private String allocationRate;//分配比率
	
	private List<AppHoldProductCumperfVO> cumperfList;
	private List<AppOrderHistoryVO> orderHistoryList;
	
	
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
	

	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
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
	public String getLastNavUpdate() {
		return lastNavUpdate;
	}
	public void setLastNavUpdate(String lastNavUpdate) {
		this.lastNavUpdate = lastNavUpdate;
	}
	public String getDayReturn() {
		return dayReturn;
	}
	public void setDayReturn(String dayReturn) {
		this.dayReturn = dayReturn;
	}
	
	
	public String getReferenceCost() {
		return referenceCost;
	}
	public void setReferenceCost(String referenceCost) {
		this.referenceCost = referenceCost;
	}
	public String getReferenceUnitPrice() {
		return referenceUnitPrice;
	}
	public void setReferenceUnitPrice(String referenceUnitPrice) {
		this.referenceUnitPrice = referenceUnitPrice;
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
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
	
	public String getCumperfRate() {
		return cumperfRate;
	}
	public void setCumperfRate(String cumperfRate) {
		this.cumperfRate = cumperfRate;
	}
	
	public String getDividend() {
		return dividend;
	}
	public void setDividend(String dividend) {
		this.dividend = dividend;
	}
	public String getAllocationRate() {
		return allocationRate;
	}
	public void setAllocationRate(String allocationRate) {
		this.allocationRate = allocationRate;
	}
	public List<AppHoldProductCumperfVO> getCumperfList(){
		return cumperfList;
	}
	
	public void setCumperfList(List<AppHoldProductCumperfVO> cumperfList){
		this.cumperfList = cumperfList;
	}
	
	public List<AppOrderHistoryVO> getOrderHistoryList(){
		return orderHistoryList;
	}
	
	public void setOrderHistoryList(List<AppOrderHistoryVO> orderHistoryList){
		this.orderHistoryList = orderHistoryList;
	}
}
