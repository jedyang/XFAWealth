package com.fsll.wmes.crm.vo;

import java.util.Date;

public class ProposalEmailRecordsVO {

	private String id;
	private String toMemberId;
	private String toMemberName;
	private String toEmailAddr;
	private String toEmailTitle;
	private String toEmailContent;
	private String sendFlag;
	private Date sendedTime;
	private String sendedTimeStr;
	private Integer sendCount;
	private String moduleType;
	private String creatorId;
	private String creator;
	
	private String relateId; // 投资方案ID
	private String proposalName;
	private String proposalCreateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToMemberId() {
		return toMemberId;
	}
	public void setToMemberId(String toMemberId) {
		this.toMemberId = toMemberId;
	}
	public String getToMemberName() {
		return toMemberName;
	}
	public void setToMemberName(String toMemberName) {
		this.toMemberName = toMemberName;
	}
	public String getToEmailAddr() {
		return toEmailAddr;
	}
	public void setToEmailAddr(String toEmailAddr) {
		this.toEmailAddr = toEmailAddr;
	}
	public String getToEmailTitle() {
		return toEmailTitle;
	}
	public void setToEmailTitle(String toEmailTitle) {
		this.toEmailTitle = toEmailTitle;
	}
	public String getToEmailContent() {
		return toEmailContent;
	}
	public void setToEmailContent(String toEmailContent) {
		this.toEmailContent = toEmailContent;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public Date getSendedTime() {
		return sendedTime;
	}
	public void setSendedTime(Date sendedTime) {
		this.sendedTime = sendedTime;
	}
	public String getSendedTimeStr() {
		return sendedTimeStr;
	}
	public void setSendedTimeStr(String sendedTimeStr) {
		this.sendedTimeStr = sendedTimeStr;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getProposalCreateTime() {
		return proposalCreateTime;
	}
	public void setProposalCreateTime(String proposalCreateTime) {
		this.proposalCreateTime = proposalCreateTime;
	}
	
	
	
}
