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
@Table(name = "insure_info")
public class InsureInfo implements java.io.Serializable {
	private String id;
	private ProductInfo product;
	private String isinCode;
	private Integer riskLevel;
	private Double lastNav;
	private Date lastNavUpdate;
	private Double dayReturn;
	private Double issuePrice;
	private Date issueDate;
	private Double minInitialAmount;
	private Double minBuyAmount;
	private Double minHoldingAmount;
	private Double minSellAmount;
	private String returnChart;
	private String overhead;
	private Date overheadTime;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;

	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	@Column(name = "isin_code")
	public String getIsinCode() {
		return this.isinCode;
	}

	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}

	@Column(name = "risk_level")
	public Integer getRiskLevel() {
		return this.riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	@Column(name = "last_nav")
	public Double getLastNav() {
		return this.lastNav;
	}

	public void setLastNav(Double lastNav) {
		this.lastNav = lastNav;
	}

	@Column(name = "last_nav_update")
	public Date getLastNavUpdate() {
		return this.lastNavUpdate;
	}

	public void setLastNavUpdate(Date lastNavUpdate) {
		this.lastNavUpdate = lastNavUpdate;
	}

	@Column(name = "day_return")
	public Double getDayReturn() {
		return this.dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}

	@Column(name = "issue_price")
	public Double getIssuePrice() {
		return this.issuePrice;
	}

	public void setIssuePrice(Double issuePrice) {
		this.issuePrice = issuePrice;
	}

	@Column(name = "issue_date")
	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	@Column(name = "min_initial_amount")
	public Double getMinInitialAmount() {
		return this.minInitialAmount;
	}

	public void setMinInitialAmount(Double minInitialAmount) {
		this.minInitialAmount = minInitialAmount;
	}

	@Column(name = "min_buy_amount")
	public Double getMinBuyAmount() {
		return this.minBuyAmount;
	}

	public void setMinBuyAmount(Double minBuyAmount) {
		this.minBuyAmount = minBuyAmount;
	}

	@Column(name = "min_holding_amount")
	public Double getMinHoldingAmount() {
		return this.minHoldingAmount;
	}

	public void setMinHoldingAmount(Double minHoldingAmount) {
		this.minHoldingAmount = minHoldingAmount;
	}

	@Column(name = "min_sell_amount")
	public Double getMinSellAmount() {
		return this.minSellAmount;
	}

	public void setMinSellAmount(Double minSellAmount) {
		this.minSellAmount = minSellAmount;
	}

	@Column(name = "return_chart")
	public String getReturnChart() {
		return this.returnChart;
	}

	public void setReturnChart(String returnChart) {
		this.returnChart = returnChart;
	}

	@Column(name = "overhead")
	public String getOverhead() {
		return this.overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	@Column(name = "overhead_time")
	public Date getOverheadTime() {
		return this.overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
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

	@Column(name = "is_valid", length = 1)
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}