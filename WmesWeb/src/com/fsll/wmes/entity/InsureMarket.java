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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "insure_market")
public class InsureMarket implements java.io.Serializable {
	private String id;
	private InsureInfo insure;
	private Date marketDate;
	private Double nav;
	private Double accNav;
	private Double returnRate;
	private String currencyCode;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;

	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "insure_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public InsureInfo getInsure() {
		return insure;
	}

	public void setInsure(InsureInfo insure) {
		this.insure = insure;
	}

	@Column(name = "market_date")
	public Date getMarketDate() {
		return this.marketDate;
	}

	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}

	@Column(name = "nav")
	public Double getNav() {
		return this.nav;
	}

	public void setNav(Double nav) {
		this.nav = nav;
	}

	@Column(name = "acc_nav")
	public Double getAccNav() {
		return this.accNav;
	}

	public void setAccNav(Double accNav) {
		this.accNav = accNav;
	}

	@Column(name = "return_rate")
	public Double getReturnRate() {
		return this.returnRate;
	}

	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
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

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}