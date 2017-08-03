package com.fsll.app.ifa.market.vo;

/**
 * 组合分析 产品实体类VO
 * @author zxtan
 * @date 2017-01-20
 */
public class AppInvestProductVO {
	private String productId;//产品ID
	private String fundId;//基金ID
	private String fundName;//基金名称
	private String fundType;//类型
	private String watchlistTypeId;//自选类型ID
	private String watchlistTypeName;//自选类型名称	
	private String riskLevel;//风险评级
	private String issueCurrency;//基金发行时的货币
	private String issueCurrencyCode;//基金发行时的货币
	private String fundCurrency;//净值货币
	private String fundCurrencyCode;//净值货币
	private String lastNav;//最新资产净值价格
	private String lastNavUpdate;//最新资产净值价格
	private String periodName;//周期
	private String increase;//回报
	private String minInitialAmount;//最少认购额
	private String allocationRate;//分配比率
	private String toCurrency;
	
	

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
	public String getWatchlistTypeId() {
		return watchlistTypeId;
	}
	public void setWatchlistTypeId(String watchlistTypeId) {
		this.watchlistTypeId = watchlistTypeId;
	}
	public String getWatchlistTypeName() {
		return watchlistTypeName;
	}
	public void setWatchlistTypeName(String watchlistTypeName) {
		this.watchlistTypeName = watchlistTypeName;
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
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getMinInitialAmount() {
		return minInitialAmount;
	}
	public void setMinInitialAmount(String minInitialAmount) {
		this.minInitialAmount = minInitialAmount;
	}
	
}
