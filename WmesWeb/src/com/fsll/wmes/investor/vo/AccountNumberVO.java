package com.fsll.wmes.investor.vo;

public class AccountNumberVO {

	private String memberId;
	private int totalCount;
	private int  auditedCount;
	private String tips;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getAuditedCount() {
		return auditedCount;
	}
	public void setAuditedCount(int auditedCount) {
		this.auditedCount = auditedCount;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
	
}
