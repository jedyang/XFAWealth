package com.fsll.wmes.ifafirm.vo;

import com.fsll.wmes.entity.MemberIfaIfafirm;

public class MemberIfaIfafirmVO extends MemberIfaIfafirm{

	private String filterStatus;
	private String filterKeyWord;
	
	private String memberId;
	private String ifaId;
	private String ifaNameChn; // 中文名
	private String ifaName; // 英文名
	private String name; // 标准名
	private String moblePhone;
	private String email;
	private String loginCode;
	private String registerTimeStr; // 注册时间
	
	
	public String getIfaNameChn() {
		return ifaNameChn;
	}
	public void setIfaNameChn(String ifaNameChn) {
		this.ifaNameChn = ifaNameChn;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getMoblePhone() {
		return moblePhone;
	}
	public void setMoblePhone(String moblePhone) {
		this.moblePhone = moblePhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getFilterKeyWord() {
		return filterKeyWord;
	}
	public void setFilterKeyWord(String filterKeyWord) {
		this.filterKeyWord = filterKeyWord;
	}
	public String getRegisterTimeStr() {
		return registerTimeStr;
	}
	public void setRegisterTimeStr(String registerTimeStr) {
		this.registerTimeStr = registerTimeStr;
	}
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
	public String getFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}
	
	
}
