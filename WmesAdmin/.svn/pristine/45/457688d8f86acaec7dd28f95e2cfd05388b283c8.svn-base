package com.fsll.core.vo;

import java.util.Date;

import com.fsll.core.entity.SysRole;

public class SysRoleVO {

	private String id;
	private String code;
	private String type;
	private String typeValue;
	private String name;
	private String remark;
	private String createBy;
	private Date createTime;
	private Date lastUpdate;
	private String relateId;
	private String companyName;
	private String ifafirmId;
	private String distributorId;
	private String isValid;
	private String keyWord;
	
	public SysRoleVO() {
	}
	
	public SysRoleVO(SysRole role) {
		this.id = role.getId();
		this.code = role.getCode();
		this.type = role.getType();
		this.name = role.getName();
		this.remark = role.getRemark();
		this.createTime = role.getCreateTime();
		this.lastUpdate = role.getLastUpdate();
		this.isValid = role.getIsValid();
		if(role.getCreateBy()!=null){
			this.relateId = role.getCreateBy().getId();
			this.createBy = role.getCreateBy().getNickName();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
}
