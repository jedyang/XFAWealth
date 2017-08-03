package com.fsll.wmes.crm.vo;

public class ProposalNumberVO {

	private String memberId;
	private int totalCount;
	private int toConfirmCount;
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
	public int getToConfirmCount() {
		return toConfirmCount;
	}
	public void setToConfirmCount(int toConfirmCount) {
		this.toConfirmCount = toConfirmCount;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
}
