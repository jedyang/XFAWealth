package com.fsll.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sys_lang_config")
public class SysLangConfig {
	
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@Column(name = "key_code")
	private String keyCode;
	
	@Column(name = "value_sc")
	private String valueSc;
	
	@Column(name = "value_tc")
	private String valueTc;
	
	@Column(name = "value_en")
	private String valueEn;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@Transient
	private String nullSc;
	@Transient
	private String nullTc;
	@Transient
	private String nullEn;
	@Transient
	private String createTimeStr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public String getValueSc() {
		return valueSc;
	}
	public void setValueSc(String valueSc) {
		this.valueSc = valueSc;
	}
	public String getValueTc() {
		return valueTc;
	}
	public void setValueTc(String valueTc) {
		this.valueTc = valueTc;
	}
	
	public String getValueEn() {
		return valueEn;
	}
	public void setValueEn(String valueEn) {
		this.valueEn = valueEn;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
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
	public String getNullSc() {
		return nullSc;
	}
	public void setNullSc(String nullSc) {
		this.nullSc = nullSc;
	}
	public String getNullTc() {
		return nullTc;
	}
	public void setNullTc(String nullTc) {
		this.nullTc = nullTc;
	}
	public String getNullEn() {
		return nullEn;
	}
	public void setNullEn(String nullEn) {
		this.nullEn = nullEn;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	

}
