package com.fsll.wmes.portfolio.vo;

public class PortfolioProductPLVO {

	private String valuationDate;
	private String productName;
	private double totalPlValue;
	private double dayPlValue;
	
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getTotalPlValue() {
		return totalPlValue;
	}
	public void setTotalPlValue(double totalPlValue) {
		this.totalPlValue = totalPlValue;
	}
	public double getDayPlValue() {
		return dayPlValue;
	}
	public void setDayPlValue(double dayPlValue) {
		this.dayPlValue = dayPlValue;
	}
	
	
	
}
