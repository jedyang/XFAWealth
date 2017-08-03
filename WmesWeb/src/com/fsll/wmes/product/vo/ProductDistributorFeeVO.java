package com.fsll.wmes.product.vo;

import java.util.Date;

public class ProductDistributorFeeVO {
	
	private String id;
	private String distributorId;
	private String distributorName;
	private String productId;
	private String productName;
	private String productType;
	private String feeType;
	private String feeTypeName;
	private Double feeDefValue;
	private Double feeMin;
	private Double feeMax;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeTypeName() {
		return feeTypeName;
	}
	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
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
