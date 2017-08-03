package com.fsll.wmes.investor.vo;

public class MyAssetsVO {

	private String memberId;
	private double totalAssets;
	private double totalMarketValue;
	private double totalCashValue;
	private Double totalReturnRate;
	private Double totalReturnValue;
	private Double dayReturn;
	private Double yesterdayReturnRate;
	private Double yesterdayReturnValue;
	private Double cashAvailable;
	private Double cashWithdrawal;
	private Double cashHold;
	private String currency;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public double getTotalCashValue() {
		return totalCashValue;
	}

	public void setTotalCashValue(double totalCashValue) {
		this.totalCashValue = totalCashValue;
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

	public Double getYesterdayReturnRate() {
		return yesterdayReturnRate;
	}

	public void setYesterdayReturnRate(Double yesterdayReturnRate) {
		this.yesterdayReturnRate = yesterdayReturnRate;
	}

	public Double getYesterdayReturnValue() {
		return yesterdayReturnValue;
	}

	public void setYesterdayReturnValue(Double yesterdayReturnValue) {
		this.yesterdayReturnValue = yesterdayReturnValue;
	}

	public Double getCashAvailable() {
		return cashAvailable;
	}

	public void setCashAvailable(Double cashAvailable) {
		this.cashAvailable = cashAvailable;
	}

	public Double getCashWithdrawal() {
		return cashWithdrawal;
	}

	public void setCashWithdrawal(Double cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
	}

	public Double getCashHold() {
		return cashHold;
	}

	public void setCashHold(Double cashHold) {
		this.cashHold = cashHold;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}

}
