package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.core.entity.SysCountry;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "member_individual")
public class MemberIndividual implements java.io.Serializable {
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
	@NotFound(action = NotFoundAction.IGNORE)
	private SysCountry country;
	
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
	@NotFound(action = NotFoundAction.IGNORE)
	private SysCountry nationality;
	
	@Column(name = "education")
	private String education;
	
	@Column(name = "employment")
	private String employment;
	
	@Column(name = "occupation")
	private String occupation;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "zipcode")
	private String zipcode;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "home_address")
	private String homeAddress;
	
	@Column(name = "home_zipcode")
	private String homeZipcode;
	
	@Column(name = "home_telephone")
	private String homeTelephone;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getHomeZipcode() {
		return homeZipcode;
	}

	public void setHomeZipcode(String homeZipcode) {
		this.homeZipcode = homeZipcode;
	}

	public String getHomeTelephone() {
		return homeTelephone;
	}

	public void setHomeTelephone(String homeTelephone) {
		this.homeTelephone = homeTelephone;
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
	
}