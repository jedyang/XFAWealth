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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "investor_account_currency")
public class InvestorAccountCurrency implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "cash_available")
	private Double cashAvailable;
	
	@Column(name = "cash_hold")
	private Double cashHold;
	
	@Column(name = "cash_withdrawal")
	private Double cashWithdrawal;
	
	@Column(name = "total_cash")
	private Double totalCash;
	
	@Column(name = "buy_hold")
	private Double buyHold;
	
	@Column(name = "sell_hold")
	private Double sellHold;
	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_update")
	private Date lastUpdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getCashAvailable() {
		return cashAvailable;
	}

	public void setCashAvailable(Double cashAvailable) {
		this.cashAvailable = cashAvailable;
	}

	public Double getCashHold() {
		return cashHold;
	}

	public void setCashHold(Double cashHold) {
		this.cashHold = cashHold;
	}

	public Double getCashWithdrawal() {
		return cashWithdrawal;
	}

	public void setCashWithdrawal(Double cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
	}

	public Double getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	public Double getBuyHold() {
		return buyHold;
	}

	public void setBuyHold(Double buyHold) {
		this.buyHold = buyHold;
	}

	public Double getSellHold() {
		return sellHold;
	}

	public void setSellHold(Double sellHold) {
		this.sellHold = sellHold;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
}