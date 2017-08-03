package com.fsll.wmes.entity;

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

@Entity
@Table(name = "insure_info_sc")
public class InsureInfoSc implements java.io.Serializable {
	private String id;
	private String insureName;
	private String insureNamePinyin;
	private InsureHouse insureHouseId;
	private String insureHouse;
	private String insureHousePinyin;
	private String lotSize;
	private String insureCurrencyCode;
	private String insureCurrency;
	private String industryTypeCode;
	private String industryType;
	private String geoAllocationCode;
	private String geoAllocation;
	private String invTarget;
	private String keyRisks;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "insure_name")
	public String getInsureName() {
		return this.insureName;
	}

	public void setInsureName(String insureName) {
		this.insureName = insureName;
	}

	@Column(name = "insure_name_pinyin")
	public String getInsureNamePinyin() {
		return this.insureNamePinyin;
	}

	public void setInsureNamePinyin(String insureNamePinyin) {
		this.insureNamePinyin = insureNamePinyin;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "insure_house_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	public InsureHouse getInsureHouseId() {
		return this.insureHouseId;
	}

	public void setInsureHouseId(InsureHouse insureHouseId) {
		this.insureHouseId = insureHouseId;
	}

	@Column(name = "insure_house")
	public String getInsureHouse() {
		return this.insureHouse;
	}

	public void setInsureHouse(String insureHouse) {
		this.insureHouse = insureHouse;
	}

	@Column(name = "insure_house_pinyin")
	public String getInsureHousePinyin() {
		return this.insureHousePinyin;
	}

	public void setInsureHousePinyin(String insureHousePinyin) {
		this.insureHousePinyin = insureHousePinyin;
	}

	@Column(name = "lot_size")
	public String getLotSize() {
		return this.lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	@Column(name = "insure_currency_code")
	public String getInsureCurrencyCode() {
		return this.insureCurrencyCode;
	}

	public void setInsureCurrencyCode(String insureCurrencyCode) {
		this.insureCurrencyCode = insureCurrencyCode;
	}

	@Column(name = "insure_currency")
	public String getInsureCurrency() {
		return this.insureCurrency;
	}

	public void setInsureCurrency(String insureCurrency) {
		this.insureCurrency = insureCurrency;
	}

	@Column(name = "industry_type_code")
	public String getIndustryTypeCode() {
		return this.industryTypeCode;
	}

	public void setIndustryTypeCode(String industryTypeCode) {
		this.industryTypeCode = industryTypeCode;
	}

	@Column(name = "industry_type")
	public String getIndustryType() {
		return this.industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	@Column(name = "geo_allocation_code")
	public String getGeoAllocationCode() {
		return this.geoAllocationCode;
	}

	public void setGeoAllocationCode(String geoAllocationCode) {
		this.geoAllocationCode = geoAllocationCode;
	}

	@Column(name = "geo_allocation")
	public String getGeoAllocation() {
		return this.geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	@Column(name = "inv_target")
	public String getInvTarget() {
		return this.invTarget;
	}

	public void setInvTarget(String invTarget) {
		this.invTarget = invTarget;
	}

	@Column(name = "key_risks")
	public String getKeyRisks() {
		return this.keyRisks;
	}

	public void setKeyRisks(String keyRisks) {
		this.keyRisks = keyRisks;
	}

}