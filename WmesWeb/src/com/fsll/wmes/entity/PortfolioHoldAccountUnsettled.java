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
@Table(name = "portfolio_hold_account_unsettled")
public class PortfolioHoldAccountUnsettled implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private String accountNo;
	private String prodType;
	private String currency;
	private Double buyAmount;
	private Double sellAmount;
	private Date lastUpdate;

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
	@JoinColumn(name = "account_id")
	@JsonIgnore
	public InvestorAccount getAccount() {
		return account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "account_no")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "prod_type")
	public String getProdType() {
		return this.prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "buy_amount")
	public Double getBuyAmount() {
		return this.buyAmount;
	}

	public void setBuyAmount(Double buyAmount) {
		this.buyAmount = buyAmount;
	}

	@Column(name = "sell_amount")
	public Double getSellAmount() {
		return this.sellAmount;
	}

	public void setSellAmount(Double sellAmount) {
		this.sellAmount = sellAmount;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}