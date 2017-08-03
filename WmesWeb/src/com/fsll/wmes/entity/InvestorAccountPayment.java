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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_account_payment")
public class InvestorAccountPayment implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private String transactionFirst;
	private String transactionSecond;
	private String advisoryFirst;
	private String advisorySecond;
	private String portfolioFundsFirst;
	private String portfolioFundsSecond;
	private String portfolioEtfFirst;
	private String portfolioEtfSecond;
	private String portfolioBondsFirst;
	private String portfolioBondsSecond;
	private String portfolioAdvisoryFirst;
	private String portfolioAdvisorySecond;
	private Date createTime;
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
		return this.account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "transaction_first")
	public String getTransactionFirst() {
		return this.transactionFirst;
	}

	public void setTransactionFirst(String transactionFirst) {
		this.transactionFirst = transactionFirst;
	}

	@Column(name = "transaction_second")
	public String getTransactionSecond() {
		return this.transactionSecond;
	}

	public void setTransactionSecond(String transactionSecond) {
		this.transactionSecond = transactionSecond;
	}

	@Column(name = "advisory_first")
	public String getAdvisoryFirst() {
		return this.advisoryFirst;
	}

	public void setAdvisoryFirst(String advisoryFirst) {
		this.advisoryFirst = advisoryFirst;
	}

	@Column(name = "advisory_second")
	public String getAdvisorySecond() {
		return this.advisorySecond;
	}

	public void setAdvisorySecond(String advisorySecond) {
		this.advisorySecond = advisorySecond;
	}

	@Column(name = "portfolio_funds_first")
	public String getPortfolioFundsFirst() {
		return this.portfolioFundsFirst;
	}

	public void setPortfolioFundsFirst(String portfolioFundsFirst) {
		this.portfolioFundsFirst = portfolioFundsFirst;
	}

	@Column(name = "portfolio_funds_second")
	public String getPortfolioFundsSecond() {
		return this.portfolioFundsSecond;
	}

	public void setPortfolioFundsSecond(String portfolioFundsSecond) {
		this.portfolioFundsSecond = portfolioFundsSecond;
	}

	@Column(name = "portfolio_etf_first")
	public String getPortfolioEtfFirst() {
		return this.portfolioEtfFirst;
	}

	public void setPortfolioEtfFirst(String portfolioEtfFirst) {
		this.portfolioEtfFirst = portfolioEtfFirst;
	}

	@Column(name = "portfolio_etf_second")
	public String getPortfolioEtfSecond() {
		return this.portfolioEtfSecond;
	}

	public void setPortfolioEtfSecond(String portfolioEtfSecond) {
		this.portfolioEtfSecond = portfolioEtfSecond;
	}

	@Column(name = "portfolio_bonds_first")
	public String getPortfolioBondsFirst() {
		return this.portfolioBondsFirst;
	}

	public void setPortfolioBondsFirst(String portfolioBondsFirst) {
		this.portfolioBondsFirst = portfolioBondsFirst;
	}

	@Column(name = "portfolio_bonds_second")
	public String getPortfolioBondsSecond() {
		return this.portfolioBondsSecond;
	}

	public void setPortfolioBondsSecond(String portfolioBondsSecond) {
		this.portfolioBondsSecond = portfolioBondsSecond;
	}

	@Column(name = "portfolio_advisory_first")
	public String getPortfolioAdvisoryFirst() {
		return this.portfolioAdvisoryFirst;
	}

	public void setPortfolioAdvisoryFirst(String portfolioAdvisoryFirst) {
		this.portfolioAdvisoryFirst = portfolioAdvisoryFirst;
	}

	@Column(name = "portfolio_advisory_second")
	public String getPortfolioAdvisorySecond() {
		return this.portfolioAdvisorySecond;
	}

	public void setPortfolioAdvisorySecond(String portfolioAdvisorySecond) {
		this.portfolioAdvisorySecond = portfolioAdvisorySecond;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}