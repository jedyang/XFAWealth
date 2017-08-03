package com.fsll.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_param_config")
public class SysParamConfig implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id")
	@JsonIgnore
	private SysParamType type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private SysParamConfig parent;
	
	@Column(name = "config_code")
	private String configCode;
	
	@Column(name = "name_sc")
	private String nameSc;
	
	
	@Column(name = "name_tc")
	private String nameTc;
	
	@Column(name = "name_en")
	private String nameEn;
	
	@Column(name = "conf_value_sc")
	private String confValueSc;
	
	@Column(name = "conf_value_tc")
	private String confValueTc;
	
	@Column(name = "conf_value_en")
	private String confValueEn;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Column(name = "is_valid")
	private String isValid;
	
	/****** 以下字段不存储数据库,仅用于页面显示 *******/
	@Transient
	private String parentName;
	@Transient
	private String parentId;
	@Transient
	private String parentCode;
	@Transient
	private String typeName;
	@Transient
	private String typeCode;
	@Transient
	private String xh;
	
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
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
}