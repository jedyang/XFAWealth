package com.fsll.wmes.community.vo;

public class FocusMemberVO {

	private String memberId;
	private int memberType;
	private String nickName;
	private String iconUrl;
	private String gender;
	private int topicCount;
	private int followerCount;
	private int isFocused;//当前用户是否已关注

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public int getTopicCount() {
		return topicCount;
	}

	public void setTopicCount(int topicCount) {
		this.topicCount = topicCount;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public int getIsFocused() {
		return isFocused;
	}

	public void setIsFocused(int isFocused) {
		this.isFocused = isFocused;
	}

}
