package com.fsll.app.investor.me.vo;

public class AppPortfolioHoldProductVO {

	private String id;
	private Double holdingUnit;	
	private Double availableUnit;	
	private Double referenceCost;
	private Double totalMarketValue;
	private Double cumperfRate;	
	private String baseCurrency;
	private String productType;
	
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public Double getHoldingUnit() {
		return holdingUnit;
	}

	public void setHoldingUnit(Double holdingUnit) {
		this.holdingUnit = holdingUnit;
	}

	public Double getAvailableUnit() {
		return availableUnit;
	}

	public void setAvailableUnit(Double availableUnit) {
		this.availableUnit = availableUnit;
	}

	public Double getReferenceCost() {
		return referenceCost;
	}

	public void setReferenceCost(Double referenceCost) {
		this.referenceCost = referenceCost;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	

	public Double getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(Double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public Double getCumperfRate() {
		return cumperfRate;
	}

	public void setCumperfRate(Double cumperfRate) {
		this.cumperfRate = cumperfRate;
	}
}