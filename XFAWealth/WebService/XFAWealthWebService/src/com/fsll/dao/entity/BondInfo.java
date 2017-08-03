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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bond_info")
public class BondInfo implements java.io.Serializable {
	private String id;
	private ProductInfo product;
	private String symbolCode;
	private String exchangeCode;
	private Date createTime;
	private Date lastUpdate;
	private String overhead;
	private Date overheadTime;
	private String isValid;
	private Integer riskLevel;
	private String productRating;
	private String listingDate;
	private String maturityDate;
	private String couponType;
	private String couponPaymentFrequency;
	private Double couponRate;
	private Double couponYieldRate;
	private Double minSubscribeAmount;
	private Double minHoldingAmount;
	private Double incrementalAmount;
	private String marketOfIssue;
	private Date previousCouponDate;
	private Date nextCouponDate;
	
	@Transient
	private String bondName;

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

	@Column(name = "symbol_code")
	public String getSymbolCode() {
		return this.symbolCode;
	}

	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}

	@Column(name = "exchange_code")
	public String getExchangeCode() {
		return this.exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
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

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Transient
	public String getBondName() {
		return bondName;
	}

	public void setBondName(String bondName) {
		this.bondName = bondName;
	}
	@Column(name = "risk_level")
	public Integer getRiskLevel() {
		return this.riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	@Column(name = "product_rating")
	public String getProductRating() {
		return this.productRating;
	}

	public void setProductRating(String productRating) {
		this.productRating = productRating;
	}

	@Column(name = "listing_date")
	public String getListingDate() {
		return this.listingDate;
	}

	public void setListingDate(String listingDate) {
		this.listingDate = listingDate;
	}

	@Column(name = "maturity_date")
	public String getMaturityDate() {
		return this.maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	@Column(name = "coupon_type")
	public String getCouponType() {
		return this.couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	@Column(name = "coupon_payment_frequency")
	public String getCouponPaymentFrequency() {
		return this.couponPaymentFrequency;
	}

	public void setCouponPaymentFrequency(String couponPaymentFrequency) {
		this.couponPaymentFrequency = couponPaymentFrequency;
	}

	@Column(name = "coupon_rate")
	public Double getCouponRate() {
		return this.couponRate;
	}

	public void setCouponRate(Double couponRate) {
		this.couponRate = couponRate;
	}

	@Column(name = "coupon_yield_rate")
	public Double getCouponYieldRate() {
		return this.couponYieldRate;
	}

	public void setCouponYieldRate(Double couponYieldRate) {
		this.couponYieldRate = couponYieldRate;
	}
	
	@Column(name = "min_subscribe_amount")
	public Double getMinSubscribeAmount() {
		return minSubscribeAmount;
	}

	public void setMinSubscribeAmount(Double minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}
	
	@Column(name = "min_holding_amount")
	public Double getMinHoldingAmount() {
		return this.minHoldingAmount;
	}

	public void setMinHoldingAmount(Double minHoldingAmount) {
		this.minHoldingAmount = minHoldingAmount;
	}

	@Column(name = "incremental_amount")
	public Double getIncrementalAmount() {
		return this.incrementalAmount;
	}

	public void setIncrementalAmount(Double incrementalAmount) {
		this.incrementalAmount = incrementalAmount;
	}

	@Column(name = "market_of_issue")
	public String getMarketOfIssue() {
		return this.marketOfIssue;
	}

	public void setMarketOfIssue(String marketOfIssue) {
		this.marketOfIssue = marketOfIssue;
	}

	@Column(name = "previous_coupon_date")
	public Date getPreviousCouponDate() {
		return this.previousCouponDate;
	}

	public void setPreviousCouponDate(Date previousCouponDate) {
		this.previousCouponDate = previousCouponDate;
	}

	@Column(name = "next_coupon_date")
	public Date getNextCouponDate() {
		return this.nextCouponDate;
	}

	public void setNextCouponDate(Date nextCouponDate) {
		this.nextCouponDate = nextCouponDate;
	}
	
}