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
@Table(name = "portfolio_hold_product_cumperf")
public class PortfolioHoldProductCumperf {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioInfo portfolio;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name="account_no")
	private String accountNo;
	
	@Column(name="valuation_date")
	private Date valuationDate;
	
	@Column(name="cumperf_rate")
	private double cumperfRate;
	
	@Column(name="total_pl")
	private double totalPl;
	
	@Column(name="day_pl")
	private double dayPl;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="last_update")
	private Date lastUpdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PortfolioInfo getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}

	

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
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

	public Date getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public double getCumperfRate() {
		return cumperfRate;
	}

	public void setCumperfRate(double cumperfRate) {
		this.cumperfRate = cumperfRate;
	}

	public double getTotalPl() {
		return totalPl;
	}

	public void setTotalPl(double totalPl) {
		this.totalPl = totalPl;
	}

	public double getDayPl() {
		return dayPl;
	}

	public void setDayPl(double dayPl) {
		this.dayPl = dayPl;
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
