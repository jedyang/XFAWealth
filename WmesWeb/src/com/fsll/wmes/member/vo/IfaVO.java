package com.fsll.wmes.member.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MemberIfa;

public class IfaVO {
	private String id;
	private String memberId;
	private String loginCode;
	private String nickName;
	private String email;
	private String password;
	private String repassword;
	private String mobileNumber;
	private String mobileCode;
	private String firstName;
	private String lastName;
	private String country;
	private String genderValue;
	private String nationality;
	private String gender;
	private String nameChn;
	private String certType;
	private String certNo;
	private String born;
	private String education;
	private String employment;
	private String occupation;
	private String address;
	private String telephone;
	private String companyType;
	private String companyTypeValue;
	private String companyIfafirmId;
	private String companyIfafirm;
	private String position;
	private String ceNumber;
	private String cfaNumber;
	private String cfpNumber;
	private String introduction;
	private String appellation;
	private String primaryResidence;
	private String serviceRegion;
	private String expertiseType;
	private Date investLifeBegin;
	private Integer popularityTotal;
	private String investStyle;
	private String languageDesc;
	private String isAdmin;
	private String checkStatus;
	private String iconUrl;
	private String keyword;
	private String isValid;
	private String ifafirmId;
	private String status;
	private String name;
	private String teams;
	private List teamList;
	
	public IfaVO() {
		
	}
	
	public IfaVO(MemberIfa ifa,String langCode) {
		this.id = ifa.getId();
		if(ifa.getMember()!=null){
			this.memberId = ifa.getMember().getId();
			this.loginCode = ifa.getMember().getLoginCode();
			this.nickName = ifa.getMember().getNickName();
			this.email = ifa.getMember().getEmail();
			this.password = ifa.getMember().getPassword();
			this.mobileNumber = ifa.getMember().getMobileNumber();
			this.isValid = ifa.getMember().getIsValid();
			this.mobileCode = ifa.getMember().getMobileCode();
			this.iconUrl = ifa.getMember().getIconUrl();
			this.status = ifa.getMember().getStatus();
		}
		if(ifa.getNationality()!=null){
			this.nationality = ifa.getNationality().getId();
			if(langCode!=null){
				this.nationality = ifa.getNationality().getCountryName(langCode);
			}
		}
		if(ifa.getCountry()!=null){
			this.country = ifa.getCountry().getId();
			if(langCode!=null){
				this.country = ifa.getCountry().getCountryName(langCode);
			}
		}
		if(ifa.getPrimaryResidence()!=null){
			this.primaryResidence = ifa.getPrimaryResidence().getId();
		}
		if(ifa.getIfafirm()!=null){
			this.companyIfafirmId = ifa.getIfafirm().getId();
		}
		if(ifa.getNationality()!=null){
			this.nationality = ifa.getNationality().getId();
		}
		if(ifa.getPrimaryResidence()!=null){
			this.primaryResidence = ifa.getPrimaryResidence().getId();
		}
		this.firstName = ifa.getFirstName();
		this.lastName = ifa.getLastName();
		this.genderValue = ifa.getGender();
		this.gender = ifa.getGender();
		this.nameChn = ifa.getNameChn();
		this.certType = ifa.getCertType();
		this.certNo = ifa.getCertNo();
		this.born = DateUtil.getDateStr(ifa.getBorn());
		this.education = ifa.getEducation();
		this.employment = ifa.getEmployment();
		this.occupation = ifa.getOccupation();
		this.address = ifa.getAddress();
		this.telephone = ifa.getTelephone();
		this.companyType = ifa.getCompanyType();
		this.companyTypeValue = ifa.getCompanyType();
		this.position = ifa.getPosition();
		this.ceNumber = ifa.getCeNumber();
		this.cfaNumber = ifa.getCfaNumber();
		this.cfpNumber = ifa.getCfpNumber();
		this.introduction = ifa.getIntroduction();
		this.appellation = ifa.getAppellation();
		this.investLifeBegin = ifa.getInvestLifeBegin();
		this.popularityTotal = ifa.getPopularityTotal();
		this.isAdmin = ifa.getIsAdmin();
//		this.checkStatus = ifa.getIfafirmStatus();
		
    	if(CommonConstants.LANG_CODE_EN.equals(langCode)){
    		this.name = ifa.getLastName() + " " + ifa.getFirstName();
    	}else {
    		this.name = ifa.getNameChn();
		}
    	if (StringUtils.isBlank(this.name)) {
    		this.name = ifa.getMember().getNickName();
    		if (StringUtils.isBlank(this.name)) {
        		this.name = ifa.getMember().getLoginCode();
        	}
		}
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

	public String getBorn() {
		return born;
	}

	public void setBorn(String born) {
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

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyTypeValue() {
		return companyTypeValue;
	}

	public void setCompanyTypeValue(String companyTypeValue) {
		this.companyTypeValue = companyTypeValue;
	}

	public String getCompanyIfafirmId() {
		return companyIfafirmId;
	}

	public void setCompanyIfafirmId(String companyIfafirmId) {
		this.companyIfafirmId = companyIfafirmId;
	}

	public String getCompanyIfafirm() {
		return companyIfafirm;
	}

	public void setCompanyIfafirm(String companyIfafirm) {
		this.companyIfafirm = companyIfafirm;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCeNumber() {
		return ceNumber;
	}

	public void setCeNumber(String ceNumber) {
		this.ceNumber = ceNumber;
	}

	public String getCfaNumber() {
		return cfaNumber;
	}

	public void setCfaNumber(String cfaNumber) {
		this.cfaNumber = cfaNumber;
	}

	public String getCfpNumber() {
		return cfpNumber;
	}

	public void setCfpNumber(String cfpNumber) {
		this.cfpNumber = cfpNumber;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getAppellation() {
		return appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	public String getServiceRegion() {
		return serviceRegion;
	}

	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}

	public String getExpertiseType() {
		return expertiseType;
	}

	public void setExpertiseType(String expertiseType) {
		this.expertiseType = expertiseType;
	}

	public Date getInvestLifeBegin() {
		return investLifeBegin;
	}

	public void setInvestLifeBegin(Date investLifeBegin) {
		this.investLifeBegin = investLifeBegin;
	}

	public Integer getPopularityTotal() {
		return popularityTotal;
	}

	public void setPopularityTotal(Integer popularityTotal) {
		this.popularityTotal = popularityTotal;
	}

	public String getInvestStyle() {
		return investStyle;
	}

	public void setInvestStyle(String investStyle) {
		this.investStyle = investStyle;
	}

	public String getLanguageDesc() {
		return languageDesc;
	}

	public void setLanguageDesc(String languageDesc) {
		this.languageDesc = languageDesc;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getPrimaryResidence() {
		return primaryResidence;
	}

	public void setPrimaryResidence(String primaryResidence) {
		this.primaryResidence = primaryResidence;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getIfafirmId() {
		return ifafirmId;
	}

	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public String getCheckStatus() {
    	return checkStatus;
    }

	public void setCheckStatus(String checkStatus) {
    	this.checkStatus = checkStatus;
    }

	public String getTeams() {
    	return teams;
    }

	public void setTeams(String teams) {
    	this.teams = teams;
    }

	public List getTeamList() {
    	return teamList;
    }

	public void setTeamList(List teamList) {
    	this.teamList = teamList;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }
}
