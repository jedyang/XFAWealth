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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.core.entity.SysCountry;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "member_ifa")
public class MemberIfa implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberBase member;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "name_chn")
	private String nameChn;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country")
	@JsonIgnore
	private SysCountry country;
	
	//@Column(name="language_spoken")
	@Transient
	private String languageSpoken;
	
	//@Column(name="live_region")
	@Transient
	private String liveRegion;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "cert_type")
	private String certType;
	
	@Column(name = "cert_no")
	private String certNo;
	
	@Column(name = "born")
	private Date born;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nationality")
	@JsonIgnore
	private SysCountry nationality;
	
	@Column(name = "education")
	private String education;
	
	@Column(name = "employment")
	private String employment;
	
	@Column(name = "occupation")
	private String occupation;
	
	//弃用，改用member_ifa_ifafirm
	@Column(name = "company_type")
	private String companyType;
	
	//弃用，改用member_ifa_ifafirm
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_ifafirm_id")
	@NotFound(action=NotFoundAction.IGNORE)
	private MemberIfafirm ifafirm;
	
	//弃用，改用member_ifa_ifafirm
	@Column(name = "company_ifafirm_status")
	private String ifafirmStatus;
	
	@Column(name = "position")
	private String position;
	
	@Column(name = "ce_number")
	private String ceNumber;
	
	@Column(name = "cfa_number")
	private String cfaNumber;
	
	@Column(name = "cfp_number")
	private String cfpNumber;
	
	@Column(name = "introduction")
	private String introduction;
	
	@Column(name = "appellation")
	private String appellation;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "telephone")
	private String telephone;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "primary_residence")
	@JsonIgnore
	private SysCountry primaryResidence;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "invest_life_begin")
	private Date investLifeBegin;
	
	@Column(name = "popularity_total")
	private Integer popularityTotal;
	
	@Column(name = "is_admin")
	private String isAdmin;
	
	@Column(name = "space_show_aum")
	private String spaceShowAum;
	
	@Column(name = "space_show_performance")
	private String spaceShowPerformance;
	
	@Column(name = "portfolio_critical_value")
	private String portfolioCriticalValue;
	
	@Column(name="portfolio_return_value")
	private String portfolioReturnValue;

	@Column(name = "supervisor")
	private String supervisor;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
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

	public String getNameChn() {
		return nameChn;
	}

	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}

	public SysCountry getCountry() {
		return country;
	}

	public void setCountry(SysCountry country) {
		this.country = country;
	}

	public String getLanguageSpoken() {
		return languageSpoken;
	}

	public void setLanguageSpoken(String languageSpoken) {
		this.languageSpoken = languageSpoken;
	}

	public String getLiveRegion() {
		return liveRegion;
	}

	public void setLiveRegion(String liveRegion) {
		this.liveRegion = liveRegion;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public SysCountry getNationality() {
		return nationality;
	}

	public void setNationality(SysCountry nationality) {
		this.nationality = nationality;
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

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getIfafirmStatus() {
		return ifafirmStatus;
	}

	public void setIfafirmStatus(String ifafirmStatus) {
		this.ifafirmStatus = ifafirmStatus;
	}

	public String getSpaceShowAum() {
		return spaceShowAum;
	}

	public void setSpaceShowAum(String spaceShowAum) {
		this.spaceShowAum = spaceShowAum;
	}

	public String getSpaceShowPerformance() {
		return spaceShowPerformance;
	}

	public void setSpaceShowPerformance(String spaceShowPerformance) {
		this.spaceShowPerformance = spaceShowPerformance;
	}

	public String getPortfolioCriticalValue() {
		return portfolioCriticalValue;
	}

	public void setPortfolioCriticalValue(String portfolioCriticalValue) {
		this.portfolioCriticalValue = portfolioCriticalValue;
	}

	public SysCountry getPrimaryResidence() {
		return primaryResidence;
	}

	public void setPrimaryResidence(SysCountry primaryResidence) {
		this.primaryResidence = primaryResidence;
	}

	public String getPortfolioReturnValue() {
		return portfolioReturnValue;
	}

	public void setPortfolioReturnValue(String portfolioReturnValue) {
		this.portfolioReturnValue = portfolioReturnValue;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	
}