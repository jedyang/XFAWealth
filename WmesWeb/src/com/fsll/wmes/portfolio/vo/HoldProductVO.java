package com.fsll.wmes.portfolio.vo;

public class HoldProductVO {

	private String id;
	private String productId;
	private String weight;
    private String productName;
    private String holdUnit;
    private String availableUnit;
    private String referenceCost;
    private String curPrice;
    private String pl;
    private String plRate;
    private String marketValue;
    private String accountNo;
    private String displayColor;
    private Double yesterdayPl;//昨日回报
    
    
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getHoldUnit() {
		return holdUnit;
	}
	public void setHoldUnit(String holdUnit) {
		this.holdUnit = holdUnit;
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
	
	public String getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(String curPrice) {
		this.curPrice = curPrice;
	}
	public String getPl() {
		return pl;
	}
	public void setPl(String pl) {
		this.pl = pl;
	}
	public String getPlRate() {
		return plRate;
	}
	public void setPlRate(String plRate) {
		this.plRate = plRate;
	}
	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getDisplayColor() {
		return displayColor;
	}
	public void setDisplayColor(String displayColor) {
		this.displayColor = displayColor;
	}
	public Double getYesterdayPl() {
		return yesterdayPl;
	}
	public void setYesterdayPl(Double yesterdayPl) {
		this.yesterdayPl = yesterdayPl;
	}
    
    
}
