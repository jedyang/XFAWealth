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
@Table(name = "portfolio_hold_cumperf")
public class PortfolioHoldCumperf {

	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hold_id")
	@JsonIgnore
	private PortfolioHold portfolioHold;
	
	@Column(name = "valuation_date")
	private Date valuationDate;
	
	@Column(name = "cumulative_rate")
	private Double cumulativeRate;
	
	@Column(name = "cumulative_pl")
	private Double cumulativePl;
	
	@Column(name = "day_pl")
	private Double dayPl;
	
	@Column(name = "day_return")
	private Double dayReturn;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "total_market_value")
	private Double totalMarketValue;
	
	@Column(name = "total_cash")
	private Double totalCash;
	
	@Column(name = "total_asset")
	private Double totalAsset;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}

	public Date getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public Double getCumulativeRate() {
		return cumulativeRate;
	}

	public void setCumulativeRate(Double cumulativeRate) {
		this.cumulativeRate = cumulativeRate;
	}

	public Double getCumulativePl() {
		return cumulativePl;
	}

	public void setCumulativePl(Double cumulativePl) {
		this.cumulativePl = cumulativePl;
	}

	public Double getDayPl() {
		return dayPl;
	}

	public void setDayPl(Double dayPl) {
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

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Double getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(Double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public void setCumulativeRate(double cumulativeRate) {
		this.cumulativeRate = cumulativeRate;
	}

	public void setCumulativePl(double cumulativePl) {
		this.cumulativePl = cumulativePl;
	}

	public void setDayPl(double dayPl) {
		this.dayPl = dayPl;
	}

	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}

	public Double getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	public Double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}
	
}
