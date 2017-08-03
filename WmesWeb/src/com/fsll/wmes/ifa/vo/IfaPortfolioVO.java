package com.fsll.wmes.ifa.vo;

public class IfaPortfolioVO {

	private String id;
	private String portfolioName;
	private String nickName;
	private String riskLevel;
	private double totalAsset;
	private String totalAssetStr;
	private double totalReturn;
	private String totalReturnStr;
	private String lastUpdate;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}
	public String getTotalAssetStr() {
		return totalAssetStr;
	}
	public void setTotalAssetStr(String totalAssetStr) {
		this.totalAssetStr = totalAssetStr;
	}
	public double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(double totalReturn) {
		this.totalReturn = totalReturn;
	}
	public String getTotalReturnStr() {
		return totalReturnStr;
	}
	public void setTotalReturnStr(String totalReturnStr) {
		this.totalReturnStr = totalReturnStr;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	 
}
