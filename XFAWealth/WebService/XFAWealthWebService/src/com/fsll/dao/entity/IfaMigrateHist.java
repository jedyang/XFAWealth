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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ifa_migrate_hist")
public class IfaMigrateHist implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_member")
//	@JsonIgnore
	private MemberBase fromMember;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_member")
//	@JsonIgnore
	private MemberBase toMember;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cus_member")
//	@JsonIgnore
	private MemberBase cusMember;

	@Column(name = "data_type")
	private String dataType;
	
	@Column(name = "status")
	private String status;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
//	@JsonIgnore
	private MemberBase createBy;

	
	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public MemberBase getFromMember() {
    	return fromMember;
    }

	public void setFromMember(MemberBase fromMember) {
    	this.fromMember = fromMember;
    }

	public MemberBase getToMember() {
    	return toMember;
    }

	public void setToMember(MemberBase toMember) {
    	this.toMember = toMember;
    }

	public MemberBase getCusMember() {
    	return cusMember;
    }

	public void setCusMember(MemberBase cusMember) {
    	this.cusMember = cusMember;
    }

	public String getDataType() {
    	return dataType;
    }

	public void setDataType(String dataType) {
    	this.dataType = dataType;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getRemark() {
    	return remark;
    }

	public void setRemark(String remark) {
    	this.remark = remark;
    }

	public Date getCreateTime() {
    	return createTime;
    }

	public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }

	public MemberBase getCreateBy() {
    	return createBy;
    }

	public void setCreateBy(MemberBase createBy) {
    	this.createBy = createBy;
    }
	

}