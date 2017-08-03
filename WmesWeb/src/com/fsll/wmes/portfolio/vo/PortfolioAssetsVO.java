package com.fsll.wmes.portfolio.vo;

public class PortfolioAssetsVO {

	private String portfolioId;
	private String portfolioName;
	private int holdCount;// 产品数量
	private String totalAssets;
	private Double totalReturnValue;
	private Double totalReturnRate;
	private String totalMarketValue;
	private String totalCashValue;
	private String totalReturnValueStr;
	private String totalReturnRateStr;
	private double totalAssetsRate;
	private String baseCurrency;
	private String createTime;
	private double assetRate;

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

	public int getHoldCount() {
		return holdCount;
	}

	public void setHoldCount(int holdCount) {
		this.holdCount = holdCount;
	}

	public String getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}

	public Double getTotalReturnValue() {
		return totalReturnValue;
	}

	public void setTotalReturnValue(Double totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}

	public Double getTotalReturnRate() {
		return totalReturnRate;
	}

	public void setTotalReturnRate(Double totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}

	public String getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(String totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public String getTotalCashValue() {
		return totalCashValue;
	}

	public void setTotalCashValue(String totalCashValue) {
		this.totalCashValue = totalCashValue;
	}

	public String getTotalReturnValueStr() {
		return totalReturnValueStr;
	}

	public void setTotalReturnValueStr(String totalReturnValueStr) {
		this.totalReturnValueStr = totalReturnValueStr;
	}

	public String getTotalReturnRateStr() {
		return totalReturnRateStr;
	}

	public void setTotalReturnRateStr(String totalReturnRateStr) {
		this.totalReturnRateStr = totalReturnRateStr;
	}

	public double getTotalAssetsRate() {
		return totalAssetsRate;
	}

	public void setTotalAssetsRate(double totalAssetsRate) {
		this.totalAssetsRate = totalAssetsRate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public double getAssetRate() {
		return assetRate;
	}

	public void setAssetRate(double assetRate) {
		this.assetRate = assetRate;
	}

}
