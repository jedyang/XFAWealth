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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_hold")
public class PortfolioHold implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
    private PortfolioInfo portfolio;
    	
	@Column(name = "total_return_rate")
	private Double totalReturnRate;
	
	@Column(name = "total_return_value")
	private Double totalReturnValue;

	@Column(name = "total_market_value")
	private Double totalMarketValue;
		
	@Column(name = "last_update")
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

	


	public Double getTotalReturnRate() {
		return totalReturnRate;
	}

	public void setTotalReturnRate(Double totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}

	public Double getTotalReturnValue() {
		return totalReturnValue;
	}

	public void setTotalReturnValue(Double totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}

	public Double getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(Double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


}