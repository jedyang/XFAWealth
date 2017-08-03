package com.fsll.core.vo;

import java.util.Date;

public class SysUserGroupVO {
	private String id;
	private String type;
	private String typeValue;
	private String name;
	private String remark;
	private String createBy;
	private Date createTime;
	private String lastUpdate;
	private String relateId;
	private String relateCompany;
	private String editable;//是否只可编辑
	private String ifafirmId;
	private String distributorId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
	public String getRelateCompany() {
		return relateCompany;
	}
	public void setRelateCompany(String relateCompany) {
		this.relateCompany = relateCompany;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public String getIfafirmId() {
		return ifafirmId;
	}
	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	
	
}
