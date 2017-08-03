package com.fsll.wmes.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class EmailLogVO {

	private String id;
	private String toMemberId;
	private String toMemberName;
	private String toEmailAddr;
	private String toEmailTitle;
	private String toEmailContent;
	private String sendFlag;
	private String sendFlagName;
	@JsonFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date sendedTime;
	private Integer sendCount;
	private String moduleType;
	private String creatorId;
	private String creatorName;
	
	private String startTimeStr;
	private String endTimeStr;
	
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
	public String getSendFlagName() {
		return sendFlagName;
	}
	public void setSendFlagName(String sendFlagName) {
		this.sendFlagName = sendFlagName;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	
	
}
