package com.fsll.wmes.entity;

import java.util.Date;
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
import com.fsll.core.entity.SysAdmin;

@Entity
@Table(name = "community_section")
public class CommunitySection implements java.io.Serializable {

	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@Column(name = "section_name_sc")
	private String sectionNameSc;
	
	@Column(name = "section_name_en")
	private String sectionNameEn;
	
	@Column(name = "section_name_tc")
	private String sectionNameTc;
	
	@Column(name = "parent_id")
	private String parentId;

	@Column(name = "creator_id")
	private String creator;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Column(name = "icon_url")
	private String iconUrl;
	
	@Column(name = "memo")
	private String memo;
	
	@Column(name = "code")
	private String code;
	
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parentId")
	@OrderBy("orderBy asc")
	@JsonIgnore
	private Set<CommunitySection> childSet = new HashSet<CommunitySection>(0);
	
	
	@Transient
	private List<CommunitySection> childs;
	
	@Transient
	private String createTimeStr;
	
	
	public String getId() {
		return id;
	}
	
 	public void setId(String id) {
		this.id = id;
	}

	public String getSectionNameSc() {
		return sectionNameSc;
	}

	public void setSectionNameSc(String sectionNameSc) {
		this.sectionNameSc = sectionNameSc;
	}

	public String getSectionNameEn() {
		return sectionNameEn;
	}

	public void setSectionNameEn(String sectionNameEn) {
		this.sectionNameEn = sectionNameEn;
	}

	public String getSectionNameTc() {
		return sectionNameTc;
	}

	public void setSectionNameTc(String sectionNameTc) {
		this.sectionNameTc = sectionNameTc;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Set<CommunitySection> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<CommunitySection> childSet) {
		this.childSet = childSet;
	}
	
	public List<CommunitySection> getChilds() {
		return childs;
	}
	public void setChilds(List<CommunitySection> childs) {
		this.childs = childs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
}
