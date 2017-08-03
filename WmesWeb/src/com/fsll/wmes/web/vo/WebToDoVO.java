package com.fsll.wmes.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class WebToDoVO {

	private String id;
	private String toDoCode;
	private String toDoId;
	private String fromMemberId;
	private String fromMemberName;
	private String ownerId;
	private String ownerName;
	private String title;
	private String url;

	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	private Date createTime;
	private String toDoType;

	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	private Date receiveTime;
	private String status;
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToDoCode() {
		return toDoCode;
	}

	public void setToDoCode(String toDoCode) {
		this.toDoCode = toDoCode;
	}

	public String getToDoId() {
		return toDoId;
	}

	public void setToDoId(String toDoId) {
		this.toDoId = toDoId;
	}

	public String getFromMemberId() {
		return fromMemberId;
	}

	public void setFromMemberId(String fromMemberId) {
		this.fromMemberId = fromMemberId;
	}
	
	public String getFromMemberName() {
		return fromMemberName;
	}

	public void setFromMemberName(String fromMemberName) {
		this.fromMemberName = fromMemberName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getToDoType() {
		return toDoType;
	}

	public void setToDoType(String toDoType) {
		this.toDoType = toDoType;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}


}