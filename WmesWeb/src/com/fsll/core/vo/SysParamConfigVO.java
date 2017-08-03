/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.vo;
/**
 * 基础数据的页面处理的封装类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public class SysParamConfigVO{
    private String id;
	private String parentId;
	private String parentCode;
	private String typeCode;
	private String typeName;
	private String configCode;
	private String nameSc;
	private String nameTc;
	private String nameEn;
	private String confValueSc;
	private String confValueTc;
	private String confValueEn;
	private Integer orderBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
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
}