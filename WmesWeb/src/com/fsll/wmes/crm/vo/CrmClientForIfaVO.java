package com.fsll.wmes.crm.vo;

import com.fsll.wmes.entity.MemberBase;

public class CrmClientForIfaVO {

	private String id;
	private String memberId;
	private MemberBase member;
	private String iconUrl;
	private String nickName;
	private String contact;
	private String email;
	private String totalAssetStr;
	private double totalAsset;
	private String createDate;
	private String name;
	
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
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTotalAssetStr() {
		return totalAssetStr;
	}
	public void setTotalAssetStr(String totalAssetStr) {
		this.totalAssetStr = totalAssetStr;
	}
	public double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public MemberBase getMember() {
    	return member;
    }
	public void setMember(MemberBase member) {
    	this.member = member;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
