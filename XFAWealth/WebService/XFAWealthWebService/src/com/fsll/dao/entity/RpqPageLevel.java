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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "rpq_page_level")
public class RpqPageLevel implements java.io.Serializable {
	private String id;
	private MemberBase member;
	private RpqPage page;
	private Integer beginScore;
	private Integer endScore;
	private Integer riskLevel;
	@Transient
	private String result;
	@Transient
	private String remark;
	private Date createTime;
	private Date lastUpdate;
	private String status;
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
	@JoinColumn(name = "member_id")
	@JsonIgnore
	public MemberBase getMember() {
		return this.member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "page_id")
	@JsonIgnore
	public RpqPage getPage() {
		return this.page;
	}

	public void setPage(RpqPage page) {
		this.page = page;
	}

	@Column(name = "begin_score")
	public Integer getBeginScore() {
		return this.beginScore;
	}

	public void setBeginScore(Integer beginScore) {
		this.beginScore = beginScore;
	}

	@Column(name = "end_score")
	public Integer getEndScore() {
		return this.endScore;
	}

	public void setEndScore(Integer endScore) {
		this.endScore = endScore;
	}

	@Column(name = "risk_level")
	public Integer getRiskLevel() {
		return this.riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}
	
	@Transient
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Transient
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}