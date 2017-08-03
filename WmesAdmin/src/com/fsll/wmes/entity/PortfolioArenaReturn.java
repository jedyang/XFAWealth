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
@Table(name = "portfolio_arena_return")
public class PortfolioArenaReturn implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioArena portfolio;
    
	@Column(name = "type")
	private String type;
	
	@Column(name = "period_code")
	private String periodCode;
	
	@Column(name = "increase")
	private Double increase;
	
	@Column(name = "type_average")
	private Double typeAverage;
	
	@Column(name = "new_ranking")
	private Integer newRanking;
	
	@Column(name = "last_ranking")
	private Integer lastRanking;
	
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

	public PortfolioArena getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioArena portfolio) {
		this.portfolio = portfolio;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public Double getIncrease() {
		return increase;
	}

	public void setIncrease(Double increase) {
		this.increase = increase;
	}

	public Double getTypeAverage() {
		return typeAverage;
	}

	public void setTypeAverage(Double typeAverage) {
		this.typeAverage = typeAverage;
	}

	public Integer getNewRanking() {
		return newRanking;
	}

	public void setNewRanking(Integer newRanking) {
		this.newRanking = newRanking;
	}

	public Integer getLastRanking() {
		return lastRanking;
	}

	public void setLastRanking(Integer lastRanking) {
		this.lastRanking = lastRanking;
	}

	
	
}