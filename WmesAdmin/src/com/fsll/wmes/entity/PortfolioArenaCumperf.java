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
@Table(name = "portfolio_arena_cumperf")
public class PortfolioArenaCumperf implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioArena portfolio;
    
	@Column(name = "valuation_date")
	private Date valuationDate;
	
	@Column(name = "cumpref_rate")
	private Double cumprefRate;
	
	@Column(name = "total_pl")
	private Double totalPl;
	
	@Column(name = "day_pl")
	private Double dayPl;
	
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

	
	public PortfolioArena getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioArena portfolio) {
		this.portfolio = portfolio;
	}

	public Date getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public Double getCumprefRate() {
		return cumprefRate;
	}

	public void setCumprefRate(Double cumprefRate) {
		this.cumprefRate = cumprefRate;
	}

	public Double getTotalPl() {
		return totalPl;
	}

	public void setTotalPl(Double totalPl) {
		this.totalPl = totalPl;
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