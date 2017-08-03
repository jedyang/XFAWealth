package com.fsll.wmes.investor.vo;

import java.util.Map;

public class InvestorMyPortfolioVO {
	private String portfolioId;
	private String portfolioName;
	private double totalAssets=0;
	private double totalCash=0;
	private double marketValue=0;
	private double returnRate=0;
	private double returnValue=0;
	private String currency;
	private int sort;
	private String dataList;
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
	public Double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(Double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public Double getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}
	public Double getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}
	public Double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(double returnValue) {
		this.returnValue = returnValue;
	}
	
	public String getDataList() {
		return dataList;
	}
	public void setDataList(String dataList) {
		this.dataList = dataList;
	}
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public void setTotalCash(double totalCash) {
		this.totalCash = totalCash;
	}
	public void setMarketValue(double marketValue) {
		this.marketValue = marketValue;
	}
	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}
	
	
}
