package com.fsll.wmes.ifafirm.vo;

import java.util.Date;

public class IfafirmFeeSpecVO {
	
	private String id;
	private String ifafirmId;
	private String ifafirmName;
	private String memberId;
	private String memberName;
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
	public String getIfafirmId() {
		return ifafirmId;
	}
	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}
	public String getIfafirmName() {
		return ifafirmName;
	}
	public void setIfafirmName(String ifafirmName) {
		this.ifafirmName = ifafirmName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
