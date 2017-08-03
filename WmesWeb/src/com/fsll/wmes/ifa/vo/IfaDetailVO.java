package com.fsll.wmes.ifa.vo;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;

public class IfaDetailVO {

	private MemberBase member;
	private MemberIfa ifaInfo;
	private String nationality;
	private String occupation;
	private String education;
	private String employment;
	private String investLife;
	private String certType;
	public MemberBase getMember() {
		return member;
	}
	public void setMember(MemberBase member) {
		this.member = member;
	}
	public MemberIfa getIfaInfo() {
		return ifaInfo;
	}
	public void setIfaInfo(MemberIfa ifaInfo) {
		this.ifaInfo = ifaInfo;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}
	public String getInvestLife() {
		return investLife;
	}
	public void setInvestLife(String investLife) {
		this.investLife = investLife;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	
	
}
