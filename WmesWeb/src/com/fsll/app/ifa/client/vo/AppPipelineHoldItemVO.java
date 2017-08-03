package com.fsll.app.ifa.client.vo;

public class AppPipelineHoldItemVO {

	private String portfolioHoldId;  
	private String portfolioName;   
	private String totalReturnRate;	
	private String totalReturnValue;
	private String totalAsset;		
	private String lastUpdate;
	private String ascAlert;
	private String descAlert;
	private String ifSummary;
	private String baseCurrency;
	
	public String getPortfolioHoldId() {
		return portfolioHoldId;
	}

	public void setPortfolioHoldId(String portfolioHoldId) {
		this.portfolioHoldId = portfolioHoldId;
	}
		
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getTotalReturnRate() {
		return totalReturnRate;
	}

	public void setTotalReturnRate(String totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}

	public String getTotalReturnValue() {
		return totalReturnValue;
	}

	public void setTotalReturnValue(String totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}

	public String getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getAscAlert() {
		return ascAlert;
	}

	public void setAscAlert(String ascAlert) {
		this.ascAlert = ascAlert;
	}

	public String getDescAlert() {
		return descAlert;
	}

	public void setDescAlert(String descAlert) {
		this.descAlert = descAlert;
	}
	
	public String getIfSummary(){
		return ifSummary;
	}

	public void setIfSummary(String ifSummary) {
		this.ifSummary = ifSummary;
	}

	public String getBaseCurrency(){
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
}