package com.fsll.wmes.ifafirm.vo;

import java.util.Date;



public class IfafirmAccountVO {

    private String id;    
	
	private String memberId;

	private String nickname;
	
	private String fullName;
		
	private String createTime;
	
	private String ifaName;

	private String accountNo;	

	private String flowStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIfaName() {
		return ifaName;
	}

	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
}
