package com.fsll.wmes.member.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fsll.wmes.entity.MemberIndividual;

/**
 * Individual数据
 * 
 * @author mqzou 2016-06-28
 */
public class IndividualVO {

	private String id;
	private String memberId;
	private String loginCode;
	private String nickName;
	private String email;
	private String password;
	private String repassword;
	private String mobileCode;
	private String mobileNumber;
	private String firstName;
	private String lastName;
	private String country;
	private String genderValue;
	private String nationality;
	private String gender;
	private String nameChn;
	private String certType;
	private String certNo;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date born;
	private String education;
	private String employment;
	private String occupation;
	private String address;
	private String telephone;
	private String isValid;
	private String status;
	private String companyId;
	private String ifaId;
	
	public IndividualVO() {
		
	}
	public IndividualVO(MemberIndividual individual) {
		this.id = individual.getId();
		if(individual.getMember() != null){
			this.memberId = individual.getMember().getId();
			this.loginCode = individual.getMember().getLoginCode();
			this.nickName = individual.getMember().getNickName();
			this.email = individual.getMember().getEmail();
			this.password = individual.getMember().getPassword();
			this.mobileNumber = individual.getMember().getMobileNumber();
			this.isValid=individual.getMember().getIsValid();
			this.mobileCode = individual.getMember().getMobileCode();
			this.status = individual.getMember().getStatus();
		}
		if(individual.getCountry() != null){
			this.country = individual.getCountry().getId();
		}
		if(individual.getNationality() != null){
			this.nationality = individual.getNationality().getId();
		}
		this.firstName = individual.getFirstName();
		this.lastName = individual.getLastName();
		this.genderValue = individual.getGender();
		this.gender = individual.getGender();
		this.nameChn = individual.getNameChn();
		this.certType = individual.getCertType();
		this.certNo = individual.getCertNo();
		this.born = individual.getBorn();
		this.education = individual.getEducation();
		this.employment = individual.getEmployment();
		this.occupation = individual.getOccupation();
		this.address = individual.getAddress();
		this.telephone = individual.getTelephone();
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
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getRepassword() {
		return repassword;
	}
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getIfaId() {
    	return ifaId;
    }
	public void setIfaId(String ifaId) {
    	this.ifaId = ifaId;
    }
	
}
