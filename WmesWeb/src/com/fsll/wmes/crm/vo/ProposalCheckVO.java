package com.fsll.wmes.crm.vo;

import java.util.Date;

public class ProposalCheckVO {

	private String id;
	private String proposalId;
	private String checkId; // 审核人 memberId
	private String approval; // 审核人 nickName
	private Date checkTime; // 审核时间
	private String checkStatus; // 审核状态
	private String checkStatusDisplay;
	private String checkResult; // 审核意见
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProposalId() {
		return proposalId;
	}
	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckStatusDisplay() {
		return checkStatusDisplay;
	}
	public void setCheckStatusDisplay(String checkStatusDisplay) {
		this.checkStatusDisplay = checkStatusDisplay;
	}
}
