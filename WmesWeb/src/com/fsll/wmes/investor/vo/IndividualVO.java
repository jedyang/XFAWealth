package com.fsll.wmes.investor.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberIndividual;

/**
 * Individual数据
 * @author mqzou
 * 2016-06-28
 */
public class IndividualVO {
 
	private String id;
	private String loginCode;
	private String nickName;
	private String email;
	private String firstName;
	private String lastName;
	private String country;
	private String genderValue;
	private String nationality;
	private String password;
	private String mobileNumber;
	private String gender;
	private String nameChn;
	private String certType;
	private String certNo;
	private Date born;
	private String education;
	private String employment;
	private String occupation;
	private String address;
	private String telephone;
	private String memberId;
	
	
	public IndividualVO() {
	    super();
	    // TODO Auto-generated constructor stub
    }
	
	
	public IndividualVO(MemberIndividual vo) {
	    super();
	    this.id = vo.getId();
	    
	    if (null!=vo.getMember()){
	    	this.memberId = vo.getMember().getId();

		    this.loginCode = vo.getMember().getLoginCode();
		    this.nickName = vo.getMember().getNickName();
		    this.email = vo.getMember().getEmail();
//		    this.password = password;
	    
		    this.mobileNumber = vo.getMember().getMobileNumber();
	    }

	    this.firstName = vo.getFirstName();
	    this.lastName = vo.getLastName();
//	    this.country = vo.getCountry().getCountryName(lang);
	    this.genderValue = vo.getGender();
//	    this.nationality = vo.getNationality().getCountryName(lang);
	    this.gender = vo.getGender();

	    this.nameChn = vo.getNameChn();
	    this.certType = vo.getCertType();
	    this.certNo = vo.getCertNo();
	    this.born = vo.getBorn();
	    this.education = vo.getEducation();
	    this.employment = vo.getEmployment();
	    this.occupation = vo.getOccupation();
	    this.address = vo.getAddress();
	    this.telephone = vo.getTelephone();
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getGenderValue() {
		return genderValue;
	}
	public void setGenderValue(String genderValue) {
		this.genderValue = genderValue;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNameChn() {
		return nameChn;
	}
	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	
	public Date getBorn() {
		return born;
	}
	public void setBorn(Date born) {
		this.born = born;
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
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMemberId() {
    	return memberId;
    }
	public void setMemberId(String memberId) {
    	this.memberId = memberId;
    }
	
	
	
	
}
