/**
 * 
 */
package com.fsll.api.product.vo;

/**
 * 基金基础信息vo
 * @author zpzhou
 * @date 2016-6-20
 */
public class ProductBasicDataVO {
	//共用字段
	private String id;//基金/股票/债券/保险标识
	private String productId;//产品标识
	private String productType;//产品类型
	private String productCode;//产品编码  基金、保险=isinCode 股票、债券=symbolCode
	private String name;
	
	private String currency;
	private String currencyCode;
	private String issueCurrency;
	private String issueCurrencyCode;
	
	private String riskLevel;
	
	private String closingPrice;
	private String priceDate;
	
	private String isinCode;
	private String exchange;
	
	private String followStatus;
	private String langCode;
	
	//特殊字段：1.以下对基金有效
	private String fundHouse;
	private String fundManCo;
	private String fundManager;
	private String fundSize;
	private String toCurrency;
	private String fundType;
	private String mgtFee;
	private String minInitAmount;
	private String minSubscribeAmount;
	private String minHoldingAmount;
	private String minRedemptionAmount;
	private String minRspAmount;
	private String invTarget;
	private String lastFundReturn;//day return
	
	//特殊字段：2.以下对股票有效
	//...
	
	//特殊字段：3.以下对债券有效
	//...
	
	//特殊字段：4.以下对保险有效
	//...
	
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
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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
	public String getClosingPrice() {
		return closingPrice;
	}
	public void setClosingPrice(String closingPrice) {
		this.closingPrice = closingPrice;
	}
	public String getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(String priceDate) {
		this.priceDate = priceDate;
	}
	public String getIsinCode() {
		return isinCode;
	}
	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getFollowStatus() {
		return followStatus;
	}
	public void setFollowStatus(String followStatus) {
		this.followStatus = followStatus;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getFundHouse() {
		return fundHouse;
	}
	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}
	public String getFundManCo() {
		return fundManCo;
	}
	public void setFundManCo(String fundManCo) {
		this.fundManCo = fundManCo;
	}
	public String getFundManager() {
		return fundManager;
	}
	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}
	public String getFundSize() {
		return fundSize;
	}
	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
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
	public String getMgtFee() {
		return mgtFee;
	}
	public void setMgtFee(String mgtFee) {
		this.mgtFee = mgtFee;
	}
	public String getMinInitAmount() {
		return minInitAmount;
	}
	public void setMinInitAmount(String minInitAmount) {
		this.minInitAmount = minInitAmount;
	}
	public String getMinSubscribeAmount() {
		return minSubscribeAmount;
	}
	public void setMinSubscribeAmount(String minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}
	public String getMinHoldingAmount() {
		return minHoldingAmount;
	}
	public void setMinHoldingAmount(String minHoldingAmount) {
		this.minHoldingAmount = minHoldingAmount;
	}
	public String getMinRedemptionAmount() {
		return minRedemptionAmount;
	}
	public void setMinRedemptionAmount(String minRedemptionAmount) {
		this.minRedemptionAmount = minRedemptionAmount;
	}
	public String getMinRspAmount() {
		return minRspAmount;
	}
	public void setMinRspAmount(String minRspAmount) {
		this.minRspAmount = minRspAmount;
	}
	public String getInvTarget() {
		return invTarget;
	}
	public void setInvTarget(String invTarget) {
		this.invTarget = invTarget;
	}
	public String getLastFundReturn() {
		return lastFundReturn;
	}
	public void setLastFundReturn(String lastFundReturn) {
		this.lastFundReturn = lastFundReturn;
	}
}
