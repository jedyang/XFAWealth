package com.fsll.wmes.ifafirm.vo;

import java.util.Date;



public class IfafirmCustomerVO {

    private String id;    
	
	private String memberId;

	private String nickname;
	
	private String fullName;
	
	private String clientType;
	
	private String isImportant;
	
	private String ifaName;
    

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}

	public String getIfaName() {
		return ifaName;
	}

	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	
}
