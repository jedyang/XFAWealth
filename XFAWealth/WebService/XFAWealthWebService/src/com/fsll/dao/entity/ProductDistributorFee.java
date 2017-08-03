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
@Table(name = "product_distributor_fee")
public class ProductDistributorFee {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	private MemberDistributor distributor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
	
	@Column(name = "fee_type")
	private String feeType;
	
	@Column(name = "fee_def_value")
	private Double feeDefValue;
	
	@Column(name = "fee_min")
	private Double feeMin;
	
	@Column(name = "fee_max")
	private Double feeMax;
	
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

	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getFeeDefValue() {
		return feeDefValue;
	}

	public void setFeeDefValue(Double feeDefValue) {
		this.feeDefValue = feeDefValue;
	}

	public Double getFeeMin() {
		return feeMin;
	}

	public void setFeeMin(Double feeMin) {
		this.feeMin = feeMin;
	}

	public Double getFeeMax() {
		return feeMax;
	}

	public void setFeeMax(Double feeMax) {
		this.feeMax = feeMax;
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
}
