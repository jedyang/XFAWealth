package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.core.entity.SysCountry;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_account_contact")
public class InvestorAccountContact implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private String contactType;
	private String appellation;
	private String lastName;
	private String firstName;
	private String nickName;
	private String nameChn;
	private SysCountry country;
	private Date born;
	private String gender;
	private String certType;
	private String certNo;
	private SysCountry nationality;
	private SysCountry primaryResidence;
	private String relateTypeDesc;
	private String levelOfEducation;
	private String employmentDetail;
	private String employerName;
	private String employerBus;
	private String occupation;
	private String licenseFlag;
	private String aimFlag;
	private String aimName;
	private String fundsSource;
	private String fundsSourceDesc;
	private String annualIncome;
	private String netWorth;
	private String liquidAsset;
	private String riskProfile;
	private String investObj;
	private String investObjDesc;
	private String investExperience;
	private String investHorizon;
	private String usFaxFlag;
	private String closeRelateFlag;
	private String closeRelateDesc;
	private String residentialCountryCode;
	private String residentialPhoneNumber;
	private String mobileCountryCode;
	private String mobilePhoneNumber;
	private String officeCountryCode;
	private String officePhoneNumber;
	private String faxCountryCode;
	private String faxPhoneNumber;
	private String email;
	private Date createTime;
	private Date lastUpdate;
	
	private String countryName;
	private String nationalName;
	private String primaryCountryName;
	private String sexName;
	private String eduLevelName;
	private String occupationName;
	private String investHname;
	
	
	

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	public InvestorAccount getAccount() {
		return this.account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "contact_type")
	public String getContactType() {
		return this.contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	@Column(name = "appellation")
	public String getAppellation() {
		return this.appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "nick_name")
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name = "name_chn")
	public String getNameChn() {
		return this.nameChn;
	}

	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country")
	@JsonIgnore
	public SysCountry getCountry() {
		return this.country;
	}

	public void setCountry(SysCountry country) {
		this.country = country;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "born", length = 10)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getBorn() {
		return this.born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	@Column(name = "gender")
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "cert_type")
	public String getCertType() {
		return this.certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	@Column(name = "cert_no")
	public String getCertNo() {
		return this.certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nationality")
	@JsonIgnore
	public SysCountry getNationality() {
		return this.nationality;
	}

	public void setNationality(SysCountry nationality) {
		this.nationality = nationality;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "primary_residence")
	@JsonIgnore
	public SysCountry getPrimaryResidence() {
		return primaryResidence;
	}

	public void setPrimaryResidence(SysCountry primaryResidence) {
		this.primaryResidence = primaryResidence;
	}

	@Column(name = "relate_type_desc")
	public String getRelateTypeDesc() {
		return this.relateTypeDesc;
	}

	public void setRelateTypeDesc(String relateTypeDesc) {
		this.relateTypeDesc = relateTypeDesc;
	}

	@Column(name = "level_of_education")
	public String getLevelOfEducation() {
		return this.levelOfEducation;
	}

	public void setLevelOfEducation(String levelOfEducation) {
		this.levelOfEducation = levelOfEducation;
	}

	@Column(name = "employment_detail")
	public String getEmploymentDetail() {
		return this.employmentDetail;
	}

	public void setEmploymentDetail(String employmentDetail) {
		this.employmentDetail = employmentDetail;
	}

	@Column(name = "employer_name")
	public String getEmployerName() {
		return this.employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	@Column(name = "employer_bus")
	public String getEmployerBus() {
		return this.employerBus;
	}

	public void setEmployerBus(String employerBus) {
		this.employerBus = employerBus;
	}

	@Column(name = "occupation")
	public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	@Column(name = "license_flag")
	public String getLicenseFlag() {
		return this.licenseFlag;
	}

	public void setLicenseFlag(String licenseFlag) {
		this.licenseFlag = licenseFlag;
	}

	@Column(name = "aim_flag")
	public String getAimFlag() {
		return this.aimFlag;
	}

	public void setAimFlag(String aimFlag) {
		this.aimFlag = aimFlag;
	}

	@Column(name = "aim_name")
	public String getAimName() {
		return this.aimName;
	}

	public void setAimName(String aimName) {
		this.aimName = aimName;
	}

	@Column(name = "funds_source")
	public String getFundsSource() {
		return this.fundsSource;
	}

	public void setFundsSource(String fundsSource) {
		this.fundsSource = fundsSource;
	}

	@Column(name = "funds_source_desc")
	public String getFundsSourceDesc() {
		return this.fundsSourceDesc;
	}

	public void setFundsSourceDesc(String fundsSourceDesc) {
		this.fundsSourceDesc = fundsSourceDesc;
	}

	@Column(name = "annual_income")
	public String getAnnualIncome() {
		return this.annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	@Column(name = "net_worth")
	public String getNetWorth() {
		return this.netWorth;
	}

	public void setNetWorth(String netWorth) {
		this.netWorth = netWorth;
	}

	@Column(name = "liquid_asset")
	public String getLiquidAsset() {
		return this.liquidAsset;
	}

	public void setLiquidAsset(String liquidAsset) {
		this.liquidAsset = liquidAsset;
	}

	@Column(name = "risk_profile")
	public String getRiskProfile() {
		return this.riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
	}

	@Column(name = "invest_obj")
	public String getInvestObj() {
		return this.investObj;
	}

	public void setInvestObj(String investObj) {
		this.investObj = investObj;
	}

	@Column(name = "invest_obj_desc")
	public String getInvestObjDesc() {
		return this.investObjDesc;
	}

	public void setInvestObjDesc(String investObjDesc) {
		this.investObjDesc = investObjDesc;
	}

	@Column(name = "invest_experience")
	public String getInvestExperience() {
		return this.investExperience;
	}

	public void setInvestExperience(String investExperience) {
		this.investExperience = investExperience;
	}

	@Column(name = "invest_horizon")
	public String getInvestHorizon() {
		return this.investHorizon;
	}

	public void setInvestHorizon(String investHorizon) {
		this.investHorizon = investHorizon;
	}

	@Column(name = "us_fax_flag")
	public String getUsFaxFlag() {
		return this.usFaxFlag;
	}

	public void setUsFaxFlag(String usFaxFlag) {
		this.usFaxFlag = usFaxFlag;
	}

	@Column(name = "close_relate_flag")
	public String getCloseRelateFlag() {
		return this.closeRelateFlag;
	}

	public void setCloseRelateFlag(String closeRelateFlag) {
		this.closeRelateFlag = closeRelateFlag;
	}

	@Column(name = "close_relate_desc")
	public String getCloseRelateDesc() {
		return this.closeRelateDesc;
	}

	public void setCloseRelateDesc(String closeRelateDesc) {
		this.closeRelateDesc = closeRelateDesc;
	}

	@Column(name = "residential_country_code")
	public String getResidentialCountryCode() {
		return this.residentialCountryCode;
	}

	public void setResidentialCountryCode(String residentialCountryCode) {
		this.residentialCountryCode = residentialCountryCode;
	}

	@Column(name = "residential_phone_number")
	public String getResidentialPhoneNumber() {
		return this.residentialPhoneNumber;
	}

	public void setResidentialPhoneNumber(String residentialPhoneNumber) {
		this.residentialPhoneNumber = residentialPhoneNumber;
	}

	@Column(name = "mobile_country_code")
	public String getMobileCountryCode() {
		return this.mobileCountryCode;
	}

	public void setMobileCountryCode(String mobileCountryCode) {
		this.mobileCountryCode = mobileCountryCode;
	}

	@Column(name = "mobile_phone_number")
	public String getMobilePhoneNumber() {
		return this.mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Column(name = "office_country_code")
	public String getOfficeCountryCode() {
		return this.officeCountryCode;
	}

	public void setOfficeCountryCode(String officeCountryCode) {
		this.officeCountryCode = officeCountryCode;
	}

	@Column(name = "office_phone_number")
	public String getOfficePhoneNumber() {
		return this.officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	@Column(name = "fax_country_code")
	public String getFaxCountryCode() {
		return this.faxCountryCode;
	}

	public void setFaxCountryCode(String faxCountryCode) {
		this.faxCountryCode = faxCountryCode;
	}

	@Column(name = "fax_phone_number")
	public String getFaxPhoneNumber() {
		return this.faxPhoneNumber;
	}

	public void setFaxPhoneNumber(String faxPhoneNumber) {
		this.faxPhoneNumber = faxPhoneNumber;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/********************用于页面显示************************/
	@Transient
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Transient
	public String getNationalName() {
		return nationalName;
	}

	public void setNationalName(String nationalName) {
		this.nationalName = nationalName;
	}
	@Transient
	public String getPrimaryCountryName() {
		return primaryCountryName;
	}

	public void setPrimaryCountryName(String primaryCountryName) {
		this.primaryCountryName = primaryCountryName;
	}
	@Transient
	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	@Transient
	public String getEduLevelName() {
		return eduLevelName;
	}

	public void setEduLevelName(String eduLevelName) {
		this.eduLevelName = eduLevelName;
	}
	@Transient
	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	@Transient
	public String getInvestHname() {
		return investHname;
	}

	public void setInvestHname(String investHname) {
		this.investHname = investHname;
	}

	
}