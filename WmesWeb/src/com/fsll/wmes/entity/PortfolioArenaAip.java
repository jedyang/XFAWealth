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
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_arena_aip")
public class PortfolioArenaAip implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioArena portfolio;
	
	@Column(name = "aip_init_time")
	private Date aipInitTime;

	@Column(name = "aip_exec_cycle")
	private String aipExecCycle;

	@Column(name = "aip_time_distance")
	private Integer aipTimeDistance;

	@Column(name = "aip_amount")
	private Double aipAmount;
	
	@Column(name = "aip_end_type")
	private String aipEndType;
	
	@Column(name = "aip_end_date")
	private Date aipEndDate;
	
	@Column(name = "aip_end_count")
	private Integer aipEndCount;
	
	@Column(name = "aip_end_total_amount")
	private Double aipEndTotalAmount;

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

	public Date getAipInitTime() {
		return aipInitTime;
	}

	public void setAipInitTime(Date aipInitTime) {
		this.aipInitTime = aipInitTime;
	}

	public String getAipExecCycle() {
		return aipExecCycle;
	}

	public void setAipExecCycle(String aipExecCycle) {
		this.aipExecCycle = aipExecCycle;
	}

	public Integer getAipTimeDistance() {
		return aipTimeDistance;
	}

	public void setAipTimeDistance(Integer aipTimeDistance) {
		this.aipTimeDistance = aipTimeDistance;
	}

	public Date getAipEndDate() {
		return aipEndDate;
	}

	public void setAipEndDate(Date aipEndDate) {
		this.aipEndDate = aipEndDate;
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

	public String getAipEndType() {
		return aipEndType;
	}

	public void setAipEndType(String aipEndType) {
		this.aipEndType = aipEndType;
	}

	public Integer getAipEndCount() {
		return aipEndCount;
	}

	public void setAipEndCount(Integer aipEndCount) {
		this.aipEndCount = aipEndCount;
	}

	public Double getAipEndTotalAmount() {
		return aipEndTotalAmount;
	}

	public void setAipEndTotalAmount(Double aipEndTotalAmount) {
		this.aipEndTotalAmount = aipEndTotalAmount;
	}

	public Double getAipAmount() {
		return aipAmount;
	}

	public void setAipAmount(Double aipAmount) {
		this.aipAmount = aipAmount;
	}
	
}
