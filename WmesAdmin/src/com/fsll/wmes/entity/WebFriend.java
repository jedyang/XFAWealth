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
@Table(name = "web_friend")
public class WebFriend implements java.io.Serializable {
	private String id;
	private MemberBase fromMember;
	private MemberBase toMember;
	private String relationships;
	private String applyMsg;
	private String checkResult;
	private String checkRemark;
	private Date checkTime;
	private Date createTime;
	private Date lastUpdate;
	
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
	@JoinColumn(name = "from_member_id")
	@JsonIgnore
	public MemberBase getFromMember() {
		return fromMember;
	}

	public void setFromMember(MemberBase fromMember) {
		this.fromMember = fromMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_member_id")
	@JsonIgnore
	public MemberBase getToMember() {
		return toMember;
	}

	public void setToMember(MemberBase toMember) {
		this.toMember = toMember;
	}

	@Column(name = "relationships")
	public String getRelationships() {
		return this.relationships;
	}

	public void setRelationships(String relationships) {
		this.relationships = relationships;
	}

	@Column(name = "apply_msg")
	public String getApplyMsg() {
		return this.applyMsg;
	}

	public void setApplyMsg(String applyMsg) {
		this.applyMsg = applyMsg;
	}

	@Column(name = "check_result")
	public String getCheckResult() {
		return this.checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	@Column(name = "check_remark")
	public String getCheckRemark() {
		return this.checkRemark;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}

	@Column(name = "check_time")
	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
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

}