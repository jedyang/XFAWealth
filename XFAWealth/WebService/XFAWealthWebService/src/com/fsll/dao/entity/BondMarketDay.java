package com.fsll.dao.entity;

import java.sql.Time;
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
@Table(name = "bond_market_day")
public class BondMarketDay implements java.io.Serializable {
	private String id;
	private BondInfo bond;
	private Date priceDate;
	private Double openingPrice;
	private Double closingPrice;
	private Double highestPrice;
	private Double lowestPrice;
	private Double tradePrice;
	private Time tradePriceTime;
	private Long totalVolume;
	private Double totalTurnVolume;
	private String currencyCode;
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
	@JoinColumn(name = "bond_id")
	@JsonIgnore
	public BondInfo getBond() {
		return bond;
	}

	public void setBond(BondInfo bond) {
		this.bond = bond;
	}

	@Column(name = "price_date")
	public Date getPriceDate() {
		return this.priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	@Column(name = "opening_price")
	public Double getOpeningPrice() {
		return this.openingPrice;
	}

	public void setOpeningPrice(Double openingPrice) {
		this.openingPrice = openingPrice;
	}

	@Column(name = "closing_price")
	public Double getClosingPrice() {
		return this.closingPrice;
	}

	public void setClosingPrice(Double closingPrice) {
		this.closingPrice = closingPrice;
	}

	@Column(name = "highest_price")
	public Double getHighestPrice() {
		return this.highestPrice;
	}

	public void setHighestPrice(Double highestPrice) {
		this.highestPrice = highestPrice;
	}

	@Column(name = "lowest_price")
	public Double getLowestPrice() {
		return this.lowestPrice;
	}

	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	@Column(name = "trade_price")
	public Double getTradePrice() {
		return this.tradePrice;
	}

	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}

	@Column(name = "trade_price_time")
	public Time getTradePriceTime() {
		return this.tradePriceTime;
	}

	public void setTradePriceTime(Time tradePriceTime) {
		this.tradePriceTime = tradePriceTime;
	}

	@Column(name = "total_volume")
	public Long getTotalVolume() {
		return this.totalVolume;
	}

	public void setTotalVolume(Long totalVolume) {
		this.totalVolume = totalVolume;
	}

	@Column(name = "total_turn_volume")
	public Double getTotalTurnVolume() {
		return this.totalTurnVolume;
	}

	public void setTotalTurnVolume(Double totalTurnVolume) {
		this.totalTurnVolume = totalTurnVolume;
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

	@Column(name = "currency_code")
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}