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
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioInfo portfolio;
	
	@JoinColumn(name = "valuation_date")
	private Date valuationDate;
	
	@JoinColumn(name = "cumulative_rate")
	private double cumulativeRate;
	
	@JoinColumn(name = "cumulative_pl")
	private double cumulativePl;
	
	@JoinColumn(name = "day_pl")
	private double dayPl;
	
	@JoinColumn(name = "create_time")
	private Date createTime;
	
	@JoinColumn(name = "last_update")
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
	
	
}
