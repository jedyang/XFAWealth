package com.fsll.wmes.member.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;

public class WebToDoToReadVO {
	private String id;
	private String title;
	private String toDoToReadCode;
	private String toDoToReadId;
	private MemberBase fromMember;
	private MemberBase owner;
	private String url;
	private Date createTime;
	private String toDoToReadType;
	private Date receiveTime;
	private String status;
	private String isValid;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToDoToReadCode() {
		return toDoToReadCode;
	}
	public void setToDoToReadCode(String toDoToReadCode) {
		this.toDoToReadCode = toDoToReadCode;
	}
	public String getToDoToReadId() {
		return toDoToReadId;
	}
	public void setToDoToReadId(String toDoToReadId) {
		this.toDoToReadId = toDoToReadId;
	}
	public MemberBase getFromMember() {
		return fromMember;
	}
	public void setFromMember(MemberBase fromMember) {
		this.fromMember = fromMember;
	}
	public MemberBase getOwner() {
		return owner;
	}
	public void setOwner(MemberBase owner) {
		this.owner = owner;
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
	public String getToDoToReadType() {
		return toDoToReadType;
	}
	public void setToDoToReadType(String toDoToReadType) {
		this.toDoToReadType = toDoToReadType;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
