/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.product.vo;
/**
 * P(页面)使用的产品列表虚拟对象
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-20
 */
public class ProductListVo {
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
	
	private String followFlag;
	
	//特殊字段：1.以下对基金有效
	private String fundType;
	private String increase;
	
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
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getFollowFlag() {
		return followFlag;
	}
	public void setFollowFlag(String followFlag) {
		this.followFlag = followFlag;
	}
}
