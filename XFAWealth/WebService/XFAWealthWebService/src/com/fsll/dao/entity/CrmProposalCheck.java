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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "crm_proposal_check")
public class CrmProposalCheck implements java.io.Serializable {
	private String id;
	private CrmProposal proposal;
	private MemberBase check;
	private Date checkTime;
	private String checkStatus;
	private String checkResult;
	private String checkIp;
	
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
	@JoinColumn(name = "check_id")
	@JsonIgnore
	public MemberBase getCheck() {
		return check;
	}

	public void setCheck(MemberBase check) {
		this.check = check;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "proposal_id")
	@JsonIgnore
	public CrmProposal getProposal() {
		return proposal;
	}

	public void setProposal(CrmProposal proposal) {
		this.proposal = proposal;
	}
	

	@Column(name = "check_ip")
	public String getCheckIp() {
		return checkIp;
	}

	public void setCheckIp(String checkIp) {
		this.checkIp = checkIp;
	}
	
}