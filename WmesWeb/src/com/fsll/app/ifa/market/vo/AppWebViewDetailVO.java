package com.fsll.app.ifa.market.vo;


public class AppWebViewDetailVO {

	private String id; //主键ID
//	private String webViewId; //关联的权限表
	private String toMemberId;//接收人member id
	private String toMemberName;//接收人member name
	private String type;//类型,Client Prospect Buddy Team

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public String getWebViewId() {
//		return webViewId;
//	}
//
//	public void setWebViewId(String webViewId) {
//		this.webViewId = webViewId;
//	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



}