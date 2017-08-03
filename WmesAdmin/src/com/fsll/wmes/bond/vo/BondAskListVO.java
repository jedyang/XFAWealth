package com.fsll.wmes.bond.vo;

import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.BondAsk;

public class BondAskListVO {

	private String id;
	private String bondName;
	private String orderNo;
	private String clientName;
	private String orderType;
	private double commissionAmount;
	private String orderDate;
	private String orderStatus;
	private String accountNo;
	private String currency;
	private String answerDate;
	private String answerName;
	private double answerPrice;

	public BondAskListVO(){
		
	}
	
	public BondAskListVO(BondAsk ask){
		this.id=ask.getId();
		this.orderNo=ask.getOrderNo();
		this.orderType=ask.getOrderType();
		this.commissionAmount=ask.getCommissionAmount();
		this.orderDate=null!=ask.getOrderDate()?DateUtil.dateToDateString(ask.getOrderDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.orderStatus=ask.getOrderStatus();
		this.accountNo=ask.getAccountNo();
		this.currency=ask.getBaseCurrency();
		this.answerDate=null!=ask.getAnswerDate()?DateUtil.dateToDateString(ask.getAnswerDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.answerPrice=null!=ask.getAnswerPrice()?ask.getAnswerPrice():0;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBondName() {
		return bondName;
	}

	public void setBondName(String bondName) {
		this.bondName = bondName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAnswerDate() {
		return answerDate;
	}

	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public double getAnswerPrice() {
		return answerPrice;
	}

	public void setAnswerPrice(double answerPrice) {
		this.answerPrice = answerPrice;
	}

}
