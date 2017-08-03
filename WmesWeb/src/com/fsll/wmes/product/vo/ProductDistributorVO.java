package com.fsll.wmes.product.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductInfo;

public class ProductDistributorVO {
	
	private String id;
	private ProductInfo product;
	private MemberDistributor distributor;
	private String isinCode;
	private String symbolCode;
	private String isPublish;
	private Date createTime;
	private String remark;
	private String cies;
	private Integer rpqLevel;
	private Double transactionFeeRate;
	private String transactionFeeCur;
	private Double transactionFeeMini;
	private String productName;
	
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
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getIsinCode() {
    	return isinCode;
    }
	public void setIsinCode(String isinCode) {
    	this.isinCode = isinCode;
    }	
}
