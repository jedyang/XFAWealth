package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "doc_list")
public class DocList implements java.io.Serializable {
	private String id;
	private DocTemplate template;
	private String isNecessary;
	private String isOpen;
	private Integer updateCycle;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date effectDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private Date createTime;
	
	private String isValid;
	
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "template_id")
	@JsonIgnore
	public DocTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DocTemplate template) {
		this.template = template;
	}

	@Column(name = "is_necessary")
	public String getIsNecessary() {
		return this.isNecessary;
	}

	public void setIsNecessary(String isNecessary) {
		this.isNecessary = isNecessary;
	}

	@Column(name = "update_cycle")
	public Integer getUpdateCycle() {
		return this.updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}

	@Column(name = "effect_date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getEffectDate() {
		return this.effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	@Column(name = "create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	@Column(name = "is_open")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

}