package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "member_distributor")
public class MemberDistributor implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
    @Column(name = "company_name")
	private String companyName;
    
    @Column(name = "entity_type")
	private String entityType;
    
    @Column(name = "entity_other")
	private String entityOther;
    
    @Column(name = "register_no")
	private String registerNo;
    
    @Column(name = "is_financial")
	private String isFinancial;
    
    @Column(name = "giin")
	private String giin;
    
    @Column(name = "nature_purpose")
	private String naturePurpose;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")  
    @Column(name = "incorporation_date")
	private Date incorporationDate;
    
    @Column(name = "incorporation_place")
	private String incorporationPlace;
    
    @Column(name = "registered_address")
	private String registeredAddress;
    
    @Column(name = "mailing_address")
	private String mailingAddress;
    
    @Column(name = "country")
	private String country;
    
    @Column(name = "website")
	private String website;

    @Column(name = "logofile")
	private String logofile;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setLogofile(String logofile) {
		this.logofile = logofile;
	}

	public String getLogofile() {
		return logofile;
	}

}