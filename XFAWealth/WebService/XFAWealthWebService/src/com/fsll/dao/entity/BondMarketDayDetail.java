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
@Table(name = "bond_market_day_detail")
public class BondMarketDayDetail implements java.io.Serializable {
	private String id;
	private BondInfo bond;
	private Date priceDate;
	private Time priceTime;
	private Double openingPrice;
	private Double closingPrice;
	private Double highestPrice;
	private Double lowestPrice;
	private String bidPrice;
	private String bidPriceTime;
	private String askPrice;
	private String askPriceTime;
	private String tradePrice;
	private String tradePriceTime;
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

	@Column(name = "price_time")
	public Time getPriceTime() {
		return this.priceTime;
	}

	public void setPriceTime(Time priceTime) {
		this.priceTime = priceTime;
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

	@Column(name = "bid_price")
	public String getBidPrice() {
		return this.bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	@Column(name = "bid_price_time")
	public String getBidPriceTime() {
		return this.bidPriceTime;
	}

	public void setBidPriceTime(String bidPriceTime) {
		this.bidPriceTime = bidPriceTime;
	}

	@Column(name = "ask_price")
	public String getAskPrice() {
		return this.askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	@Column(name = "ask_price_time")
	public String getAskPriceTime() {
		return this.askPriceTime;
	}

	public void setAskPriceTime(String askPriceTime) {
		this.askPriceTime = askPriceTime;
	}

	@Column(name = "trade_price")
	public String getTradePrice() {
		return this.tradePrice;
	}

	public void setTradePrice(String tradePrice) {
		this.tradePrice = tradePrice;
	}

	@Column(name = "trade_price_time")
	public String getTradePriceTime() {
		return this.tradePriceTime;
	}

	public void setTradePriceTime(String tradePriceTime) {
		this.tradePriceTime = tradePriceTime;
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