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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_doc_check")
public class InvestorDocCheck implements java.io.Serializable {
	private String id;
	private String docId;
	private MemberBase check;
	private String checkRole;
	private Date checkTime;
	private String checkStatus;
	private String checkResult;
	private String loginCode;
	
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

	@Column(name = "doc_id")
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "check_id")
	@JsonIgnore
	public MemberBase getCheck() {
		return check;
	}

	public void setCheck(MemberBase check) {
		this.check = check;
	}

	@Column(name = "check_role")
	public String getCheckRole() {
		return checkRole;
	}

	public void setCheckRole(String checkRole) {
		this.checkRole = checkRole;
	}

	@Column(name = "check_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "check_status")
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = "check_result")
	public String getCheckResult() {
		return this.checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
    @Transient
	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	
}