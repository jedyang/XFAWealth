package com.fsll.wmes.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class WebToReadVO {

	private String id;
	private String toReadCode;
	private String toReadId;
	private String fromMemberId;
	private String fromMemberName;
	private String ownerId;
	private String ownerName;
	private String title;
	private String url;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	private Date createTime;
	private String toReadType;

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

	public String getToReadCode() {
		return toReadCode;
	}

	public void setToReadCode(String toReadCode) {
		this.toReadCode = toReadCode;
	}

	public String getToReadId() {
		return toReadId;
	}

	public void setToReadId(String toReadId) {
		this.toReadId = toReadId;
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

	public String getToReadType() {
		return toReadType;
	}

	public void setToReadType(String toReadType) {
		this.toReadType = toReadType;
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