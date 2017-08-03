package com.fsll.wmes.ifa.vo;

import java.util.Date;

/**
 * Investor浏览ifa信息VO
 * @author Yan
 */
public class IfaMessageVO {

	//member_base
	private String memberId;	
	private String nickName;	
	private String email;	
	private String mobileCode;	
	private String mobileNumber;	
	private String iconUrl;
	//member_ifa
	private String ifaId;	
	private String gender;
	private Integer popularityTotal;	
	private Date investLifeBegin;	
	private Integer experience;		//new Date - investLifeBegin
	//member_ifafirm
	private String firmLogo;	
	//member_ifafirm_sc
	private String companyName;		
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getPopularityTotal() {
		return popularityTotal;
	}
	public void setPopularityTotal(Integer popularityTotal) {
		this.popularityTotal = popularityTotal;
	}
	public Date getInvestLifeBegin() {
		return investLifeBegin;
	}
	public void setInvestLifeBegin(Date investLifeBegin) {
		this.investLifeBegin = investLifeBegin;
	}
	public Integer getExperience() {
		return experience;
	}
	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	public String getFirmLogo() {
		return firmLogo;
	}
	public void setFirmLogo(String firmLogo) {
		this.firmLogo = firmLogo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
