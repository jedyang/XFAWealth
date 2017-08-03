/**
 * 
 */
package com.fsll.wmes.member.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;

/**
 * @author michael
 * 成员信息
 */
public class PersonalInfoVO {
	
	public PersonalInfoVO() {
		super();
		baseInfo = new MemberBase();
		individualPerson = new MemberIndividual();
		ifaPerson = new MemberIfa();
		setIfaExtInfo(new IfaExtInfo());
	}

	//基本信息
	private MemberBase baseInfo;
	private String memberId;
	private Integer memberType;//账户类型：0-平台级用户,1-投资人,2-IFA,3-代理商
	private Integer subMemberType;//帐户子类 : 11-独立投资账户; 12-公司投资账户; 13-FI账户; 21-IFA个人,22-ifafirm; 31-distributor

	private String countryId;
	private String countryName;
	private String nationId;
	private String nationName;
	private String certTypeId;
	private String certType;
	private String educationId;
	private String education;
	private String employmentId;
	private String employment;
	private String occupationId;
	private String occupation;
	
	//独立投资人
	private MemberIndividual individualPerson;
	
	//IFA个人
	private MemberIfa ifaPerson;
	private IfaExtInfo ifaExtInfo;
	private String introduction;
	private String highlight;
	
	public MemberBase getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(MemberBase baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public Integer getSubMemberType() {
		return subMemberType;
	}

	public void setSubMemberType(Integer subMemberType) {
		this.subMemberType = subMemberType;
	}

	public MemberIndividual getIndividualPerson() {
		return individualPerson;
	}

	public void setIndividualPerson(MemberIndividual individualPerson) {
		this.individualPerson = individualPerson;
	}

	public MemberIfa getIfaPerson() {
		return ifaPerson;
	}

	public void setIfaPerson(MemberIfa ifaPerson) {
		this.ifaPerson = ifaPerson;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getNationId() {
		return nationId;
	}

	public void setNationId(String nationId) {
		this.nationId = nationId;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getCertTypeId() {
		return certTypeId;
	}

	public void setCertTypeId(String certTypeId) {
		this.certTypeId = certTypeId;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getEducationId() {
		return educationId;
	}

	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEmploymentId() {
		return employmentId;
	}

	public void setEmploymentId(String employmentId) {
		this.employmentId = employmentId;
	}

	public String getEmployment() {
		return employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(String occupationId) {
		this.occupationId = occupationId;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public void setInfos(String lang) {
		this.setMemberId(baseInfo.getId());
		this.setMemberType(baseInfo.getMemberType());
		this.setSubMemberType(baseInfo.getSubMemberType());
		//设置独立投资人信息
		if (individualPerson!=null && (memberType!=null && memberType.intValue()==1 
				&& subMemberType!=null && subMemberType.intValue()==11)){
			if (individualPerson.getCountry()!=null){
				this.setCountryId(individualPerson.getCountry().getId());
				this.setCountryName(individualPerson.getCountry().getCountryName(lang));
			}
			if (individualPerson.getNationality()!=null){
				this.setNationId(individualPerson.getNationality().getId());
				this.setNationName(individualPerson.getNationality().getCountryName(lang));
			}
			this.setCertTypeId(individualPerson.getCertType());
			this.setEducationId(individualPerson.getEducation());
			this.setOccupationId(individualPerson.getOccupation());
			this.setEmploymentId(individualPerson.getEmployment());
			
		}
		//设置IFA个人信息
		if (ifaPerson!=null && (memberType!=null && memberType.intValue()==2
				&& subMemberType!=null && subMemberType.intValue()==21)){
			if (ifaPerson.getCountry()!=null){
				this.setCountryId(ifaPerson.getCountry().getId());
				this.setCountryName(ifaPerson.getCountry().getCountryName(lang));
			}
			if (ifaPerson.getNationality()!=null){
				this.setNationId(ifaPerson.getNationality().getId());
				this.setNationName(ifaPerson.getNationality().getCountryName(lang));
			}
			this.setCertTypeId(ifaPerson.getCertType());
			this.setEducationId(ifaPerson.getEducation());
			this.setOccupationId(ifaPerson.getOccupation());
			this.setEmploymentId(ifaPerson.getEmployment());
		}
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	public void setIfaExtInfo(IfaExtInfo ifaExtInfo) {
		this.ifaExtInfo = ifaExtInfo;
	}

	public IfaExtInfo getIfaExtInfo() {
		return ifaExtInfo;
	}
}
