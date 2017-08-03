package com.fsll.wmes.crm.vo;

public class CrmPortfolioHoldVO {

	private String portfolioHoldId;  
	private String portfolioId;  
	private String portfolioName;   
	private String totalReturnRate;	
	private String totalReturnValue;
	private String totalMarketValue;		
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
	
	public String getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
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

	public String getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(String totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
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