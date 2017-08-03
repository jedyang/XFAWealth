package com.fsll.core.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_menu")
public class SysMenu implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private SysMenu parent;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "name_sc")
	private String nameSc;
	
	@Column(name = "name_tc")
	private String nameTc;
	
	@Column(name = "name_en")
	private String nameEn;
	
	@Column(name = "web_url")
	private String webUrl;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "order_by")
	private Short orderBy;
	
	@Column(name = "if_inner")
	private Short ifInner;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parent")
	////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("orderBy asc")
	@JsonIgnore
	private Set<SysMenu> childSet = new HashSet<SysMenu>(0);
	
	@Transient
	private List<SysMenu> childs;
	
	@Transient
	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysMenu getParent() {
		return parent;
	}

	public void setParent(SysMenu parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Short getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Set<SysMenu> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<SysMenu> childSet) {
		this.childSet = childSet;
	}

	public Short getIfInner() {
		return ifInner;
	}

	public void setIfInner(Short ifInner) {
		this.ifInner = ifInner;
	}

	public List<SysMenu> getChilds() {
		return childs;
	}

	public void setChilds(List<SysMenu> childs) {
		this.childs = childs;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}