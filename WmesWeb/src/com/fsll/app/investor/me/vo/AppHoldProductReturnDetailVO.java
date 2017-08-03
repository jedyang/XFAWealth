package com.fsll.app.investor.me.vo;

/**
 * 组合产品列表实体类VO
 * @author zxtan
 * @date 2017-03-03
 */
public class AppHoldProductReturnDetailVO {
	private String holdProductId;//持仓产品Id
	private String productId;	//产品Id
	private String productName;//产品名称
	private String productCode;//产品编码
	private String productType;//产品类型
	private String portfolioHoldId;//持仓Id
	private String lastPrice;//最新价格
	private String refCost;//成本价
	private String holdingUnit;//持有数量
	private String availableUnit;//可用数量
	private String marketValue;//参考市值
	private String returnValue;//参考盈亏
	private String returnRate;//盈亏比例	
	private String baseCurrency;//货币
	private String accountNo;//账户编码
	
	
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getRefCost() {
		return refCost;
	}
	public void setRefCost(String refCost) {
		this.refCost = refCost;
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	
}
