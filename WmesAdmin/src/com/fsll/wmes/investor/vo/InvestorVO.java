package com.fsll.wmes.investor.vo;

import javax.persistence.Column;

import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;

public class InvestorVO {

	private String id;
	private String memberId;
	private String loginCode;
	private String nickName;
	private String email;
	private String mobileNumber;
	private String memberType;
	private String relateId;
	private String companyName;
	private String name;
	private String ifaId;
	private String ifaName;
	private String ifafirmId;
	private String ifafirmName;
	private String keyWord;
	
	private String nationality;
	private String nameChn;
	private String firstName;
	private String lastName;
	
	private String accountNo;
	private String mobileCode;
    
	
	


	public InvestorVO() {
		super();
	}
	
	public InvestorVO(String id, String memberId, String loginCode,
			String nickName, String email, String mobileNumber,
			String memberType, String relateId, String companyName,
			String name, String ifaId, String ifaName, String ifafirmId,
			String ifafirmName, String keyWord,
			String nameChn, String firstName, String lastName,String accountNo,String mobileCode,MemberIndividual memberIndividual,String langCode) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.loginCode = loginCode;
		this.nickName = nickName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.memberType = memberType;
		this.relateId = relateId;
		this.companyName = companyName;
		this.name = name;
		this.ifaId = ifaId;
		this.ifaName = ifaName;
		this.ifafirmId = ifafirmId;
		this.ifafirmName = ifafirmName;
		this.keyWord = keyWord;
		this.nameChn = nameChn;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountNo = accountNo;
		this.mobileCode = mobileCode;
		
		if(null != memberIndividual.getNationality()){
			this.nationality = memberIndividual.getNationality().getId();
			if(langCode!=null){
				this.nationality = memberIndividual.getNationality().getCountryName(langCode);
			}
		}
		
		if(null !=memberIndividual.getNationality() ){
			this.nationality = memberIndividual.getNationality().getId();
		}
	}


	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getNameChn() {
		return nameChn;
	}

	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getIfafirmId() {
		return ifafirmId;
	}
	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}
	public String getIfafirmName() {
		return ifafirmName;
	}
	public void setIfafirmName(String ifafirmName) {
		this.ifafirmName = ifafirmName;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
}
