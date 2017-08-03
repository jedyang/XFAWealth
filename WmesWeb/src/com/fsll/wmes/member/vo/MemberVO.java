package com.fsll.wmes.member.vo;

public class MemberVO {
	private String id;
	private String name;
	private String iconUrl;
	
	private Integer followers;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Integer getFollowers() {
		return followers;
	}
	public void setFollowers(Integer followers) {
		this.followers = followers;
	}
	
	
}
