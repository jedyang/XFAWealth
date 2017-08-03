package com.fsll.wmes.investor.vo;

public class InvestorApprovalVO {

	private String accountId;
	private String accountNo;
	private String memberId;
	private String nickName;
	private String accountCreateTime;
	private String fromType;
	private String accType;
	private String approver;
	private String approverId;
	private String flowState;
	private String flowName;
	private String canApprove;//是否可以审批
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAccountCreateTime() {
		return accountCreateTime;
	}
	public void setAccountCreateTime(String accountCreateTime) {
		this.accountCreateTime = accountCreateTime;
	}
	public String getFromType() {
		return fromType;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getApproverId() {
		return approverId;
	}
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
	public String getFlowState() {
		return flowState;
	}
	public void setFlowState(String flowState) {
		this.flowState = flowState;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getCanApprove() {
		return canApprove;
	}
	public void setCanApprove(String canApprove) {
		this.canApprove = canApprove;
	}
	
	
}
