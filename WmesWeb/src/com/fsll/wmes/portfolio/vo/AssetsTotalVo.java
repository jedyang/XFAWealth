package com.fsll.wmes.portfolio.vo;

public class AssetsTotalVo {

	private String portfolioHoldId;
	private double totalAssets=0;
	private double totalMarketValue=0;
	private double totalCash=0;
	private Double totalReturnRate;
	private Double totalReturnValue;
	public String getPortfolioHoldId() {
		return portfolioHoldId;
	}
	public void setPortfolioHoldId(String portfolioHoldId) {
		this.portfolioHoldId = portfolioHoldId;
	}
	public double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public double getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}
	public double getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(double totalCash) {
		this.totalCash = totalCash;
	}
	public Double getTotalReturnRate() {
		return totalReturnRate;
	}
	public void setTotalReturnRate(Double totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}
	public Double getTotalReturnValue() {
		return totalReturnValue;
	}
	public void setTotalReturnValue(Double totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}
	
	
}
