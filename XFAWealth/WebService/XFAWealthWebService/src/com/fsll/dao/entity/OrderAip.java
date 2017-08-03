package com.fsll.dao.entity;

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
    private PortfolioHold portfolioHold;
	private OrderPlan plan;
	private String aipState;
	private Date aipInitTime;
	private Date aipNextTime;
	private String aipExecCycle;
	private Integer aipTimeDistance;
	private Double aipAmount;
	private String aipEndType;
	private Date aipEndDate;
	private Integer aipEndCount;
	private Double aipEndTotalAmount;
	private Integer aipCount;
	private Double aipTotalAmount;
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
	@JoinColumn(name = "hold_id")
	@JsonIgnore
	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plan_id")
	@JsonIgnore
	public OrderPlan getPlan() {
		return plan;
	}

	public void setPlan(OrderPlan plan) {
		this.plan = plan;
	}
	
	@Column(name = "aip_end_type")
	public String getAipEndType() {
		return aipEndType;
	}

	public void setAipEndType(String aipEndType) {
		this.aipEndType = aipEndType;
	}

	@Column(name = "aip_end_total_amount")
	public Double getAipEndTotalAmount() {
		return aipEndTotalAmount;
	}

	public void setAipEndTotalAmount(Double aipEndTotalAmount) {
		this.aipEndTotalAmount = aipEndTotalAmount;
	}

	@Column(name = "aip_count")
	public Integer getAipCount() {
		return aipCount;
	}

	public void setAipCount(Integer aipCount) {
		this.aipCount = aipCount;
	}

	@Column(name = "aip_total_amount")
	public Double getAipTotalAmount() {
		return aipTotalAmount;
	}

	public void setAipTotalAmount(Double aipTotalAmount) {
		this.aipTotalAmount = aipTotalAmount;
	}

}