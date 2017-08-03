package com.fsll.wmes.portfolio.vo;

public class PortfolioProductListVO {

	private String id;
	private String productId;
	private String productName;
	private String holdingUnit;
	private String availableUnit;
	private String referenceCost;
	private String pl;
	private double plValue;
	private String marketValue;
	private String accountNo;
	private String accountId;
	private String totalRate;
	private String riskLevel;
	private String currency;
	private double allocationRate;
	private String allocationRateString;
	
	
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getReferenceCost() {
		return referenceCost;
	}
	public void setReferenceCost(String referenceCost) {
		this.referenceCost = referenceCost;
	}
	public String getPl() {
		return pl;
	}
	public void setPl(String pl) {
		this.pl = pl;
	}
	
	public double getPlValue() {
		return plValue;
	}

	public void setPlValue(double plValue) {
		this.plValue = plValue;
	}

	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
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
	public String getTotalRate() {
		return totalRate;
	}
	public void setTotalRate(String totalRate) {
		this.totalRate = totalRate;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAllocationRate() {
		return allocationRate;
	}

	public void setAllocationRate(double allocationRate) {
		this.allocationRate = allocationRate;
	}

	public String getAllocationRateString() {
		return allocationRateString;
	}

	public void setAllocationRateString(String allocationRateString) {
		this.allocationRateString = allocationRateString;
	}
	
	
}
