package com.fsll.wmes.crm.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.RpqExam;


public class CrmCustomerVO {
	private String id;
	private String ifaId;
	private String memberId;
	private String iconUrl;
	private String remark;
	private String mobileCode;
	private String mobileNumber;
	private String email;
	private String nickName;
	private String firstName;
	private String lastName;
	private String nameChn;
	private String gender;
	private String nationality;
	private String nationalityName;
	private String occupation;
	private String occupationName;
	private String employment;
	private String employmentName;
	private String education;
	private String educationName;
	private String certNo;
	private String telephone;
	//@JsonFormat(pattern="yyyy-MM-dd")
	private String born;
	private String clientType;
	private String lastLogin;
	private String clientName;
	
	//code
	private String character;
	private String hobby;
	private String special;
	private String dislike;
	//name
	private String characterName;
	private String hobbyName;
	
	private String webchatCode;
	private String linkedinCode;
	private String facebookCode;
	private String qqCode;
	private String weiboCode;
	private String twitterCode;
		

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getIfaId() {
		return this.ifaId;
	}

	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}


	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getMobileCode() {
		return this.mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}


	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNameChn() {
		return this.nameChn;
	}

	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getNationalityName() {
		return this.nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getOccupationName() {
		return this.occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

	public String getEmployment() {
		return employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getEmploymentName() {
		return employmentName;
	}

	public void setEmploymentName(String employmentName) {
		this.employmentName = employmentName;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}


	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}


	public String getCertNo() {
		return this.certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBorn(){
		return this.born;
	}
	
	public void setBorn(String born){
		this.born = born;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getDislike() {
		return dislike;
	}

	public void setDislike(String dislike) {
		this.dislike = dislike;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getHobbyName() {
		return hobbyName;
	}

	public void setHobbyName(String hobbyName) {
		this.hobbyName = hobbyName;
	}

	public String getWebchatCode() {
		return webchatCode;
	}

	public void setWebchatCode(String webchatCode) {
		this.webchatCode = webchatCode;
	}

	public String getLinkedinCode() {
		return linkedinCode;
	}

	public void setLinkedinCode(String linkedinCode) {
		this.linkedinCode = linkedinCode;
	}

	public String getFacebookCode() {
		return facebookCode;
	}

	public void setFacebookCode(String facebookCode) {
		this.facebookCode = facebookCode;
	}

	public String getQqCode() {
		return qqCode;
	}

	public void setQqCode(String qqCode) {
		this.qqCode = qqCode;
	}

	public String getWeiboCode() {
		return weiboCode;
	}

	public void setWeiboCode(String weiboCode) {
		this.weiboCode = weiboCode;
	}

	public String getTwitterCode() {
		return twitterCode;
	}

	public void setTwitterCode(String twitterCode) {
		this.twitterCode = twitterCode;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
