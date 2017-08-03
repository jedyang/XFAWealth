package com.fsll.wmes.ifa.vo;

public class MyBuddyVO {

	private String id;
	private String memberId;
	private String state;
	private String loginCode;
	private String mobileNumber;
	private String email;
	private String iconUrl;
	private String gender;
	private String nickName;
	private String msg;
	private String imUserId;//im
	private String fromMemberType;
	private String toMemberType;
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
	/**
	 * 0 客户或ifa ,1 其他好友
	 * @param state
	 */
	public String getState() {
		return state;
	}
	/**
	 * 0 客户或ifa ,1 其他好友
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getImUserId() {
		return imUserId;
	}
	public void setImUserId(String imUserId) {
		this.imUserId = imUserId;
	}
	public String getFromMemberType() {
		return fromMemberType;
	}
	public void setFromMemberType(String fromMemberType) {
		this.fromMemberType = fromMemberType;
	}
	public String getToMemberType() {
		return toMemberType;
	}
	public void setToMemberType(String toMemberType) {
		this.toMemberType = toMemberType;
	}
	
	
	
}
