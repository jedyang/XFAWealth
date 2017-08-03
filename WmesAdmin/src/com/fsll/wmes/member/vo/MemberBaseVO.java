package com.fsll.wmes.member.vo;

import com.fsll.wmes.entity.MemberBase;

public class MemberBaseVO {
	private String id;
	private String loginCode;
	private String nickName;
	private String email;
	private String isValid;
	private String consoleRoleMemberId;
	private Integer memberType;
	private String keyWord;
	
	
	public MemberBaseVO() {
	}
	
	public MemberBaseVO(MemberBase base) {
		if(base!=null){
			this.id = base.getId();
			this.loginCode = base.getLoginCode();
			this.email = base.getEmail();
			this.isValid = base.getIsValid();
			this.memberType = base.getMemberType();
			this.nickName = base.getNickName();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getConsoleRoleMemberId() {
		return consoleRoleMemberId;
	}
	public void setConsoleRoleMemberId(String consoleRoleMemberId) {
		this.consoleRoleMemberId = consoleRoleMemberId;
	}
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
