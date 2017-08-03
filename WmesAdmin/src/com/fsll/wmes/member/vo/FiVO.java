package com.fsll.wmes.member.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberFi;

public class FiVO {

	private String id;
	private String memberId;
	private String loginCode;
	private String nickName;
	private String email;
	private String password;
	private String mobileNumber;
	private String companyName;
	private String entityType;
	private String entityOther;
	private String registerNo;
	private String isFinancial;
	private String giin;
	private String naturePurpose;
	private Date incorporationDate;
	private String incorporationPlace;
	private String registeredAddress;
	private String mailingAddress;
	private String country;
	private String website;
	private String isValid;
	
	public FiVO() {
		
	}

	public FiVO(MemberFi memeberFi) {
		this.id = memeberFi.getId();
		if(null != memeberFi.getMember()){
			this.memberId = memeberFi.getMember().getId();
			this.loginCode = memeberFi.getMember().getLoginCode();
			this.nickName = memeberFi.getMember().getNickName();
			this.email = memeberFi.getMember().getEmail();
			this.password = memeberFi.getMember().getEmail();
			this.mobileNumber = memeberFi.getMember().getMobileNumber();
			this.isValid = memeberFi.getMember().getIsValid();
		}
		if(null != memeberFi.getIncorporationPlace()){
			this.incorporationPlace = memeberFi.getIncorporationPlace().getId();
		}
		if(null != memeberFi.getCountry()){
			this.country = memeberFi.getCountry().getId();
		}
		this.companyName = memeberFi.getCompanyName();
		this.entityType = memeberFi.getEntityType();
		this.entityOther = memeberFi.getEntityOther();
		this.registerNo = memeberFi.getRegisterNo();
		this.isFinancial = memeberFi.getIsFinancial();
		this.giin = memeberFi.getGiin();
		this.naturePurpose = memeberFi.getNaturePurpose();
		this.incorporationDate = memeberFi.getIncorporationDate();
		this.registeredAddress = memeberFi.getRegisteredAddress();
		this.mailingAddress = memeberFi.getMailingAddress();
		this.website = memeberFi.getWebsite();
	}



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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityOther() {
		return entityOther;
	}

	public void setEntityOther(String entityOther) {
		this.entityOther = entityOther;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}

	public String getGiin() {
		return giin;
	}

	public void setGiin(String giin) {
		this.giin = giin;
	}

	public String getNaturePurpose() {
		return naturePurpose;
	}

	public void setNaturePurpose(String naturePurpose) {
		this.naturePurpose = naturePurpose;
	}

	public Date getIncorporationDate() {
		return incorporationDate;
	}

	public void setIncorporationDate(Date incorporationDate) {
		this.incorporationDate = incorporationDate;
	}

	public String getIncorporationPlace() {
		return incorporationPlace;
	}

	public void setIncorporationPlace(String incorporationPlace) {
		this.incorporationPlace = incorporationPlace;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	

}
