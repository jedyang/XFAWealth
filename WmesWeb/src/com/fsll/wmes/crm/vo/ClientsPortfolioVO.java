package com.fsll.wmes.crm.vo;

public class ClientsPortfolioVO {

	private String id;
	private String portfolioName;
	private String riskLevel;
	private String clientName;
	private String clientId;
	private String totalAssets;
    private String totalReturn;
    private String totalReturnRate;
    private String aipFlag;
    private String lastUpdate;
    private String baseCurrency;
    private String status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
	public String getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(String totalReturn) {
		this.totalReturn = totalReturn;
	}
	
	public String getAipFlag() {
		return aipFlag;
	}
	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getTotalReturnRate() {
		return totalReturnRate;
	}
	public void setTotalReturnRate(String totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
