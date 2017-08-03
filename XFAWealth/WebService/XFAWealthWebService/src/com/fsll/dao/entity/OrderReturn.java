package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "order_return")
public class OrderReturn implements java.io.Serializable {
	private String id;
	private OrderHistory order;
	private String transactionNo;
	private String description;
	private Double transactionUnit;
	private Double transactionPrice;
	private Double transactionAmount;
	private Date transactionTime;
	private Double transactionFee;
	private String transactionType;
	private String cashFlow;
	private String remark;
	private String currencyCode;
	private String status;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public OrderHistory getOrder() {
		return this.order;
	}

	public void setOrder(OrderHistory order) {
		this.order = order;
	}

	@Column(name = "transaction_no")
	public String getTransactionNo() {
		return this.transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "transaction_unit")
	public Double getTransactionUnit() {
		return this.transactionUnit;
	}

	public void setTransactionUnit(Double transactionUnit) {
		this.transactionUnit = transactionUnit;
	}

	@Column(name = "transaction_price")
	public Double getTransactionPrice() {
		return this.transactionPrice;
	}

	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	@Column(name = "transaction_amount")
	public Double getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "transaction_time")
	public Date getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Column(name = "transaction_fee")
	public Double getTransactionFee() {
		return this.transactionFee;
	}

	public void setTransactionFee(Double transactionFee) {
		this.transactionFee = transactionFee;
	}

	@Column(name = "transaction_type")
	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Column(name = "cash_flow")
	public String getCashFlow() {
		return this.cashFlow;
	}

	public void setCashFlow(String cashFlow) {
		this.cashFlow = cashFlow;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "currency_code")
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}