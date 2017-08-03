package com.fsll.wmes.trade.vo;

import java.util.Date;

public class AipOrderHistoryVO {

	private String fundInfoId;
	private String productId;
	private String fundName;
	private String fundCurrencyCode;
	private String fundCurrencyName;
	private Date orderDate; // 每次定投时间
	private Double orderPrice; // 下单价格
	private Double orderAmount; // 下单金额
	private Double transactionPrice; // 成交价格
	private Double transactionUnit; // 成交份额
	private Double transactionAmount; // 成交金额
	private Date transactionDate; // 实际交易时间
	private Date closeTime; // 成交时间
	private Double fee; // 费用
	private String accountId; 
	private String accountNO;
	private String status;
	private String statusDisplay;
	
	
	public String getFundInfoId() {
		return fundInfoId;
	}
	public void setFundInfoId(String fundInfoId) {
		this.fundInfoId = fundInfoId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}
	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}
	public String getFundCurrencyName() {
		return fundCurrencyName;
	}
	public void setFundCurrencyName(String fundCurrencyName) {
		this.fundCurrencyName = fundCurrencyName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public Double getTransactionUnit() {
		return transactionUnit;
	}
	public void setTransactionUnit(Double transactionUnit) {
		this.transactionUnit = transactionUnit;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNO() {
		return accountNO;
	}
	public void setAccountNO(String accountNO) {
		this.accountNO = accountNO;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	
	
}
