package com.fsll.wmes.entity;

import java.util.Date;
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
@Table(name = "bbs_type")
public class BbsType implements java.io.Serializable {
	private String id;
	private BbsType parent;
	private String moduleType;
	private String relateId;
	private String name;
	private MemberBase master;
	private String remark;
	private Long topicCount;
	private Long replyCount;
	private Long clickCoount;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	private Set<BbsType> childSet = new HashSet<BbsType>(0);

	
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parent")
	@OrderBy("id asc")
	@JsonIgnore
	public Set<BbsType> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<BbsType> childSet) {
		this.childSet = childSet;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	public BbsType getParent() {
		return parent;
	}
	
	public void setParent(BbsType parent) {
		this.parent = parent;
	}

	@Column(name = "module_type")
	public String getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@Column(name = "relate_id")
	public String getRelateId() {
		return this.relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "master_id")
	@JsonIgnore
	public MemberBase getMaster() {
		return master;
	}

	public void setMaster(MemberBase master) {
		this.master = master;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "topic_count")
	public Long getTopicCount() {
		return this.topicCount;
	}

	public void setTopicCount(Long topicCount) {
		this.topicCount = topicCount;
	}

	@Column(name = "reply_count")
	public Long getReplyCount() {
		return this.replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}

	@Column(name = "click_coount")
	public Long getClickCoount() {
		return this.clickCoount;
	}

	public void setClickCoount(Long clickCoount) {
		this.clickCoount = clickCoount;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}