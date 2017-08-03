package com.fsll.wmes.investor.vo;

public class InvestorAccountHoldVO {
	private String memberId;
	private String accountId;
	private String accountNo;
	private String productId;
	private String productInformation;
	private String baseCurrency;
	private double latestMarketPrice;
	private double holdingUnit;
	private double availableUnit;
	private double referencecost=0;
	private int number;
	private String productType;

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductInformation() {
		return productInformation;
	}
	public void setProductInformation(String productInformation) {
		this.productInformation = productInformation;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public double getLatestMarketPrice() {
		return latestMarketPrice;
	}
	public void setLatestMarketPrice(double latestMarketPrice) {
		this.latestMarketPrice = latestMarketPrice;
	}
	public double getHoldingUnit() {
		return holdingUnit;
	}
	public void setHoldingUnit(double holdingUnit) {
		this.holdingUnit = holdingUnit;
	}
	public double getAvailableUnit() {
		return availableUnit;
	}
	public void setAvailableUnit(double availableUnit) {
		this.availableUnit = availableUnit;
	}
	public double getReferencecost() {
		return referencecost;
	}
	public void setReferencecost(double referencecost) {
		this.referencecost = referencecost;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
