package com.fsll.wmes.ifafirm.vo;

import com.fsll.wmes.entity.IfafirmTeamIfa;

public class IfafirmTeamIfaVO extends IfafirmTeamIfa{

	private String teamId;
	
	private String memberId;
	private String ifaId;
	private String ifaNameChn;
	private String ifaName;
	private String moblePhone;
	private String email;
	private String loginCode;
	
	
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	
}
