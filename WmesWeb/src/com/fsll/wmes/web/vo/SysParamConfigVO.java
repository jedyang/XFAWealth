package com.fsll.wmes.web.vo;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysParamType;

public class SysParamConfigVO {
	private String id;
	private SysParamType type;
	private SysParamConfig parent;
	private String configCode;
	private String currName;
	private String nameSc;
	private String nameTc;
	private String nameEn;
	private String confValueSc;
	private String confValueTc;
	private String confValueEn;
	private Integer orderBy;
	private String isValid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SysParamType getType() {
		return type;
	}
	public void setType(SysParamType type) {
		this.type = type;
	}
	public SysParamConfig getParent() {
		return parent;
	}
	public void setParent(SysParamConfig parent) {
		this.parent = parent;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public String getCurrName() {
		return currName;
	}
	public void setCurrName(String currName) {
		this.currName = currName;
	}
	public String getNameSc() {
		return nameSc;
	}
	public void setNameSc(String nameSc) {
		this.nameSc = nameSc;
	}
	public String getNameTc() {
		return nameTc;
	}
	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getConfValueSc() {
		return confValueSc;
	}
	public void setConfValueSc(String confValueSc) {
		this.confValueSc = confValueSc;
	}
	public String getConfValueTc() {
		return confValueTc;
	}
	public void setConfValueTc(String confValueTc) {
		this.confValueTc = confValueTc;
	}
	public String getConfValueEn() {
		return confValueEn;
	}
	public void setConfValueEn(String confValueEn) {
		this.confValueEn = confValueEn;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	
}
