package com.fsll.wmes.entity;

import java.util.Date;

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
@Table(name = "community_focus")
public class CommunityFocus implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	private MemberBase creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "focus_id")
	@JsonIgnore
	private MemberBase focus;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	private CommunityFocusGroup group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public MemberBase getFocus() {
		return focus;
	}

	public void setFocus(MemberBase focus) {
		this.focus = focus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public CommunityFocusGroup getGroup() {
		return group;
	}

	public void setGroup(CommunityFocusGroup group) {
		this.group = group;
	}

	
}