package com.fsll.wmes.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "my_asset_his")
public class MyAssetHis implements java.io.Serializable {
	private String id;
	private MemberBase member;
	private Date valuationDate;
	private Double totalAsset;
	private Double totalMarket;
	private Double totalCash;
	private String currencyType;
	private Double accReturn;
	private Double dayReturn;
	private Double totalPl;
	private Double dayPl;
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
	@JoinColumn(name = "member_id")
	@JsonIgnore
	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "valuation_date")
	public Date getValuationDate() {
		return this.valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	@Column(name = "total_asset")
	public Double getTotalAsset() {
		return this.totalAsset;
	}

	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}

	@Column(name = "total_market")
	public Double getTotalMarket() {
		return this.totalMarket;
	}

	public void setTotalMarket(Double totalMarket) {
		this.totalMarket = totalMarket;
	}

	@Column(name = "total_cash")
	public Double getTotalCash() {
		return this.totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	@Column(name = "currency_type")
	public String getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "acc_return")
	public Double getAccReturn() {
		return this.accReturn;
	}

	public void setAccReturn(Double accReturn) {
		this.accReturn = accReturn;
	}

	@Column(name = "total_pl")
	public Double getTotalPl() {
		return this.totalPl;
	}

	public void setTotalPl(Double totalPl) {
		this.totalPl = totalPl;
	}

	@Column(name = "day_pl")
	public Double getDayPl() {
		return this.dayPl;
	}

	public void setDayPl(Double dayPl) {
		this.dayPl = dayPl;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@Column(name = "day_return")
	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}
}