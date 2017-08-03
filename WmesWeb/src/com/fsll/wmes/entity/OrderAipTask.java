package com.fsll.wmes.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_aip_task")
public class OrderAipTask implements java.io.Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "aip_id")
	@JsonIgnore
    private OrderAip orderAip;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
    
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "dividend")
	private String dividend;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "tran_fee")
	private Double tranFee;
	
	@Column(name = "tran_fee_cur")
	private String tranFeeCur;
	
	@Column(name = "tran_rate")
	private Double tranRate;
	
	@Column(name = "tran_fee_mini")
	private Double tranFeeMini;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderAip getOrderAip() {
		return orderAip;
	}

	public void setOrderAip(OrderAip orderAip) {
		this.orderAip = orderAip;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDividend() {
		return dividend;
	}

	public void setDividend(String dividend) {
		this.dividend = dividend;
	}

	public InvestorAccount getAccount() {
		return account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getTranFee() {
		return tranFee;
	}

	public void setTranFee(Double tranFee) {
		this.tranFee = tranFee;
	}

	public String getTranFeeCur() {
		return tranFeeCur;
	}

	public void setTranFeeCur(String tranFeeCur) {
		this.tranFeeCur = tranFeeCur;
	}

	public Double getTranRate() {
		return tranRate;
	}

	public void setTranRate(Double tranRate) {
		this.tranRate = tranRate;
	}

	public Double getTranFeeMini() {
		return tranFeeMini;
	}

	public void setTranFeeMini(Double tranFeeMini) {
		this.tranFeeMini = tranFeeMini;
	}
}