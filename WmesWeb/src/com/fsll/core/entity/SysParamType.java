package com.fsll.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_param_type")
public class SysParamType implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "type_code")
	private String typeCode;
	
	@Column(name = "is_valid")
	private String isValid;

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "type")
	////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
	@OrderBy("orderBy asc")
	@JsonIgnore
	private Set<SysParamConfig> sysParamConfigSet = new HashSet<SysParamConfig>(0);
	
	/****** 以下字段不存储数据库,仅用于页面显示 *******/
	@Transient
	private String xh;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Set<SysParamConfig> getSysParamConfigSet() {
		return sysParamConfigSet;
	}

	public void setSysParamConfigSet(Set<SysParamConfig> sysParamConfigSet) {
		this.sysParamConfigSet = sysParamConfigSet;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}
}