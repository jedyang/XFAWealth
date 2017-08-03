package com.fsll.app.ifa.market.vo;

/**
 * 组合产品列表实体类VO
 * @author zxtan
 * @date 2017-03-03
 */
public class AppHoldProductVO {
	private String holdProductId;//持仓产品Id
	private String productId;	//产品Id
	private String productName;//产品名称
	private String productType;//产品类型
	private String portfolioHoldId;//持仓Id
	private String weight;//占比
	private String marketValue;//参考市值
	private String returnValue;//参考盈亏
	private String returnRate;//盈亏比例	
	private String baseCurrency;//货币
	
	
	public String getHoldProductId() {
		return holdProductId;
	}
	public void setHoldProductId(String holdProductId) {
		this.holdProductId = holdProductId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPortfolioHoldId() {
		return portfolioHoldId;
	}
	public void setPortfolioHoldId(String portfolioHoldId) {
		this.portfolioHoldId = portfolioHoldId;
	}

	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
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

	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	
}
