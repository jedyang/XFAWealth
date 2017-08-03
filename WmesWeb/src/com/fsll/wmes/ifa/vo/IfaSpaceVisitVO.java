package com.fsll.wmes.ifa.vo;

import java.util.Date;

/**
 * 显示访客列表数据，供用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceVisitVO {
	private String memberId;
	private String name;
	private String visitType;
	private String memberHeadImg;
	private Date visitDateTime;
	private String visitDateTimeStr;
	private String iconUrl;
	private String nickName;
	private String lastName;
	private String firstName;
	private String gender;
	private String nameChn;
	private int memberType;
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public String getMemberHeadImg() {
		return memberHeadImg;
	}
	public void setMemberHeadImg(String memberHeadImg) {
		this.memberHeadImg = memberHeadImg;
	}
	
	public Date getVisitDateTime() {
		return visitDateTime;
	}
	public void setVisitDateTime(Date visitDateTime) {
		this.visitDateTime = visitDateTime;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNameChn() {
		return nameChn;
	}
	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}
	public String getVisitDateTimeStr() {
		return visitDateTimeStr;
	}
	public void setVisitDateTimeStr(String visitDateTimeStr) {
		this.visitDateTimeStr = visitDateTimeStr;
	}
	public int getMemberType() {
		return memberType;
	}
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	
	
	
}
