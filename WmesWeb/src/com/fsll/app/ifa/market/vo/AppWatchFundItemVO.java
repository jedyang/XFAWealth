package com.fsll.app.ifa.market.vo;

/**
 * 自选基金产品实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppWatchFundItemVO {
	private String fundId;//基金 Id
	private String productId;//产品 Id
	private String fundName;//基金名称
	private String issueCurrency;//基金货币名称
	private String issueCurrencyCode;//基金货币编码
	private String riskLevel;//风险等级
	private String fundType;//基金类型
	private String periodName;//周期
	private String increase;//周期回报
	

	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
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
}
