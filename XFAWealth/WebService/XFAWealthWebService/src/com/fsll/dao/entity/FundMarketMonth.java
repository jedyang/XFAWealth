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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "fund_market_month")
public class FundMarketMonth implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fund_id")
	@JsonIgnore
	private FundInfo fund;
	
	@Column(name = "market_date")
	private Date marketDate;
	
	@Column(name = "nav")
	private Double nav;
	
	@Column(name = "acc_nav")
	private Double accNav;
	
	@Column(name = "return_rate")
	private Double returnRate;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
    @Column(name = "create_time")
	private Date createTime;
    
    @Column(name = "last_update")
	private Date lastUpdate;
    
    @Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FundInfo getFund() {
		return fund;
	}

	public void setFund(FundInfo fund) {
		this.fund = fund;
	}

	public Date getMarketDate() {
		return marketDate;
	}

	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}

	public Double getNav() {
		return nav;
	}

	public void setNav(Double nav) {
		this.nav = nav;
	}

	public Double getAccNav() {
		return accNav;
	}

	public void setAccNav(Double accNav) {
		this.accNav = accNav;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}