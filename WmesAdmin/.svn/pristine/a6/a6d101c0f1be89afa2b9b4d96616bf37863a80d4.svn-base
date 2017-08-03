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
@Table(name = "bond_info_en")
public class BondInfoEn implements java.io.Serializable {
	private String id;
	private String bondName;
	private String bondNamePinyin;
	private BondHouse bondHouseId;
	private String bondHouse;
	private String bondHousePinyin;
	private String lotSize;
	private String bondCurrencyCode;
	private String bondCurrency;
	private String issueCurrencyCode;
	private String issueCurrency;
	private String industryTypeCode;
	private String industryType;
	private String geoAllocationCode;
	private String geoAllocation;

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

	@Column(name = "bond_name")
	public String getBondName() {
		return this.bondName;
	}

	public void setBondName(String bondName) {
		this.bondName = bondName;
	}

	@Column(name = "bond_name_pinyin")
	public String getBondNamePinyin() {
		return this.bondNamePinyin;
	}

	public void setBondNamePinyin(String bondNamePinyin) {
		this.bondNamePinyin = bondNamePinyin;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bond_house_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	public BondHouse getBondHouseId() {
		return bondHouseId;
	}
	
	public void setBondHouseId(BondHouse bondHouseId) {
		this.bondHouseId = bondHouseId;
	}

	@Column(name = "bond_house")
	public String getBondHouse() {
		return this.bondHouse;
	}

	public void setBondHouse(String bondHouse) {
		this.bondHouse = bondHouse;
	}

	@Column(name = "bond_house_pinyin")
	public String getBondHousePinyin() {
		return this.bondHousePinyin;
	}

	public void setBondHousePinyin(String bondHousePinyin) {
		this.bondHousePinyin = bondHousePinyin;
	}

	@Column(name = "lot_size")
	public String getLotSize() {
		return this.lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	@Column(name = "bond_currency_code")
	public String getBondCurrencyCode() {
		return this.bondCurrencyCode;
	}

	public void setBondCurrencyCode(String bondCurrencyCode) {
		this.bondCurrencyCode = bondCurrencyCode;
	}

	@Column(name = "bond_currency")
	public String getBondCurrency() {
		return this.bondCurrency;
	}

	public void setBondCurrency(String bondCurrency) {
		this.bondCurrency = bondCurrency;
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

	@Column(name = "issue_currency_code")
	public String getIssueCurrencyCode() {
		return issueCurrencyCode;
	}

	public void setIssueCurrencyCode(String issueCurrencyCode) {
		this.issueCurrencyCode = issueCurrencyCode;
	}

	@Column(name = "issue_currency")
	public String getIssueCurrency() {
		return issueCurrency;
	}

	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}

}