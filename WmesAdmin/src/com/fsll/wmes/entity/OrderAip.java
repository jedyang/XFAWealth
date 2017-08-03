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
@Table(name = "order_aip")
public class OrderAip implements java.io.Serializable {
	private String id;
	private PortfolioInfo portfolio;
	private String aipState;
	private Date aipInitTime;
	private Date aipNextTime;
	private String aipExecCycle;
	private Integer aipTimeDistance;
	private Double aipAmount;
	private String aipEndFlag;
	private Date aipEndDate;
	private Integer aipEndCount;
	private Double aipEndTotalAmount;
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
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
	public PortfolioInfo getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}

	@Column(name = "aip_state")
	public String getAipState() {
		return this.aipState;
	}

	public void setAipState(String aipState) {
		this.aipState = aipState;
	}

	@Column(name = "aip_init_time")
	public Date getAipInitTime() {
		return this.aipInitTime;
	}

	public void setAipInitTime(Date aipInitTime) {
		this.aipInitTime = aipInitTime;
	}

	@Column(name = "aip_next_time")
	public Date getAipNextTime() {
		return this.aipNextTime;
	}

	public void setAipNextTime(Date aipNextTime) {
		this.aipNextTime = aipNextTime;
	}

	@Column(name = "aip_exec_cycle")
	public String getAipExecCycle() {
		return this.aipExecCycle;
	}

	public void setAipExecCycle(String aipExecCycle) {
		this.aipExecCycle = aipExecCycle;
	}

	@Column(name = "aip_time_distance")
	public Integer getAipTimeDistance() {
		return this.aipTimeDistance;
	}

	public void setAipTimeDistance(Integer aipTimeDistance) {
		this.aipTimeDistance = aipTimeDistance;
	}

	@Column(name = "aip_amount")
	public Double getAipAmount() {
		return this.aipAmount;
	}

	public void setAipAmount(Double aipAmount) {
		this.aipAmount = aipAmount;
	}

	@Column(name = "aip_end_flag")
	public String getAipEndFlag() {
		return this.aipEndFlag;
	}

	public void setAipEndFlag(String aipEndFlag) {
		this.aipEndFlag = aipEndFlag;
	}

	@Column(name = "aip_end_date")
	public Date getAipEndDate() {
		return this.aipEndDate;
	}

	public void setAipEndDate(Date aipEndDate) {
		this.aipEndDate = aipEndDate;
	}

	@Column(name = "aip_end_count")
	public Integer getAipEndCount() {
		return this.aipEndCount;
	}

	public void setAipEndCount(Integer aipEndCount) {
		this.aipEndCount = aipEndCount;
	}

	@Column(name = "aip_end_total_amount")
	public Double getAipEndTotalAmount() {
		return this.aipEndTotalAmount;
	}

	public void setAipEndTotalAmount(Double aipEndTotalAmount) {
		this.aipEndTotalAmount = aipEndTotalAmount;
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

}