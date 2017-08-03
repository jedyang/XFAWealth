package com.fsll.app.investor.me.vo;


/**
 * 我的历史资产信息实体类VO
 * @author zpzhou
 * @date 2016-9-13
 */
public class AppMyAssetsHisMessVo {
	private String totalAmount;
	private String accReturn;
	private String cashAmount;
	private String currencyType;
	private String valuationDate;
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getAccReturn() {
		return accReturn;
	}
	public void setAccReturn(String accReturn) {
		this.accReturn = accReturn;
	}
	public String getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
}
