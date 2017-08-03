package com.fsll.core.entity;

import java.util.HashSet;
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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sys_channel")
public class SysChannel implements java.io.Serializable {
	private String id;
	private SysChannel parent;
	private String channelCode;
	private String name;
	private String height;
	private String url;
	private String isTab;
	private String isDisplayTitle;
	private String remark;
	private String isValid;

	private Set<SysChannel> childSet = new HashSet<SysChannel>(0);
	
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
	@JoinColumn(name = "parent_id")
	public SysChannel getParent() {
		return this.parent;
	}

	public void setParent(SysChannel parent) {
		this.parent = parent;
	}

	@Column(name = "channel_code")
	public String getChannelCode() {
		return this.channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "height")
	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "is_tab")
	public String getIsTab() {
		return this.isTab;
	}

	public void setIsTab(String isTab) {
		this.isTab = isTab;
	}

	@Column(name = "is_display_title")
	public String getIsDisplayTitle() {
		return this.isDisplayTitle;
	}

	public void setIsDisplayTitle(String isDisplayTitle) {
		this.isDisplayTitle = isDisplayTitle;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parent")
	@OrderBy("id asc")
	@JsonIgnore
	public Set<SysChannel> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<SysChannel> childSet) {
		this.childSet = childSet;
	}

}