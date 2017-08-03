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
@Table(name = "product_distributor")
public class ProductDistributor {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	private MemberDistributor distributor;
	
	@Column(name = "symbol_code")
	private String symbolCode;
	
	@Column(name = "is_publish")
	private String isPublish;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "cies")
	private String cies;
	
	@Column(name = "rpq_level")
	private Integer rpqLevel;
	
	@Column(name = "transaction_fee_rate")
	private Double transactionFeeRate;
	
	@Column(name = "transaction_fee_cur")
	private String transactionFeeCur;
	
	@Column(name = "transaction_fee_mini")
	private Double transactionFeeMini;
	
	@Column(name = "tradable")
	private String tradable;
	
	@Column(name = "status")
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	public String getSymbolCode() {
		return symbolCode;
	}

	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCies() {
		return cies;
	}

	public void setCies(String cies) {
		this.cies = cies;
	}

	public Integer getRpqLevel() {
		return rpqLevel;
	}

	public void setRpqLevel(Integer rpqLevel) {
		this.rpqLevel = rpqLevel;
	}

	public Double getTransactionFeeRate() {
		return transactionFeeRate;
	}

	public void setTransactionFeeRate(Double transactionFeeRate) {
		this.transactionFeeRate = transactionFeeRate;
	}

	public String getTransactionFeeCur() {
		return transactionFeeCur;
	}

	public void setTransactionFeeCur(String transactionFeeCur) {
		this.transactionFeeCur = transactionFeeCur;
	}

	public Double getTransactionFeeMini() {
		return transactionFeeMini;
	}

	public void setTransactionFeeMini(Double transactionFeeMini) {
		this.transactionFeeMini = transactionFeeMini;
	}

	public String getTradable() {
		return tradable;
	}

	public void setTradable(String tradable) {
		this.tradable = tradable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
