package com.fsll.wmes.portfolio.vo;

public class AssetsVO {

	private String memberId;
	private Double totalReturnRate;
	private Double totalReturnValue;
	private double totalMarketValue;
	private double totalCashValue;
	private double totalAssets;
	private String totalReturnRateStr;
	private String totalReturnValueStr;
	private String totalMarketValueStr;
	private String totalCashValueStr;
	private String totalAssetsStr;
	private String baseCurrency;
	private String currencyName;
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public double getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}
	public String getTotalReturnRateStr() {
		return totalReturnRateStr;
	}
	public void setTotalReturnRateStr(String totalReturnRateStr) {
		this.totalReturnRateStr = totalReturnRateStr;
	}
	public String getTotalReturnValueStr() {
		return totalReturnValueStr;
	}
	public void setTotalReturnValueStr(String totalReturnValueStr) {
		this.totalReturnValueStr = totalReturnValueStr;
	}
	public String getTotalMarketValueStr() {
		return totalMarketValueStr;
	}
	public void setTotalMarketValueStr(String totalMarketValueStr) {
		this.totalMarketValueStr = totalMarketValueStr;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	public double getTotalCashValue() {
		return totalCashValue;
	}
	public void setTotalCashValue(double totalCashValue) {
		this.totalCashValue = totalCashValue;
	}
	public String getTotalCashValueStr() {
		return totalCashValueStr;
	}
	public void setTotalCashValueStr(String totalCashValueStr) {
		this.totalCashValueStr = totalCashValueStr;
	}
	public double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public String getTotalAssetsStr() {
		return totalAssetsStr;
	}
	public void setTotalAssetsStr(String totalAssetsStr) {
		this.totalAssetsStr = totalAssetsStr;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
}
