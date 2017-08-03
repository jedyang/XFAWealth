package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "news_category")
public class NewsCategory implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private NewsCategory parent;

    @Column(name = "name_sc")
	private String nameSc;
    
    @Column(name = "name_tc")
	private String nameTc;
    
    @Column(name = "name_en")
	private String nameEn;
    
    @Column(name = "code")
	private String code;
    
    @Column(name = "icon_url")
	private String iconUrl;
    
    @Column(name = "order_by")
	private String orderBy;
    
    @Column(name = "xfa_type_id")
    private String xfaTypeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public NewsCategory getParent() {
		return parent;
	}

	public void setParent(NewsCategory parent) {
		this.parent = parent;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getXfaTypeId() {
		return this.xfaTypeId;
	}

	public void setXfaTypeId(String xfaTypeId) {
		this.xfaTypeId = xfaTypeId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}