package com.fsll.app.watch.vo;

/**
 * 自选基金产品实体类VO
 * @author zxtan
 * @date 2017-02-20
 */
public class AppWatchFundVO {
	private String id;//基金 Id
	private String productId;//产品 Id
	private String fundName;//基金名称
	private String toCurrency;//货币转换
	private String issueCurrency;//基金发行货币名称
	private String issueCurrencyCode;//基金发行货币编码
	private String fundCurrency;//基金净值报价货币名称
	private String fundCurrencyCode;//基金净值报价货币编码
	private String riskLevel;//风险等级
	private String fundType;//基金类型
	private String lastNav;//最新净值
	private String lastNavUpdate;//最新净值时间
	private String dayReturn;//日回报
	
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
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	public String getFundCurrency() {
		return fundCurrency;
	}
	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
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
	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}
	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}
	public String getDayReturn() {
		return dayReturn;
	}
	public void setDayReturn(String dayReturn) {
		this.dayReturn = dayReturn;
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
