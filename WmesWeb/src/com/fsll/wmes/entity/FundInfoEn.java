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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "fund_info_en")
public class FundInfoEn implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;
    
    @Column(name = "fund_name")
	private String fundName;
    
    @Column(name = "fund_name_pinyin")
	private String fundNamePinyin;
    
	@Column(name = "domicile_code")
	private String domicileCode;
    
    @Column(name = "domicile")
	private String domicile;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fund_house_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	private FundHouse fundHouseId;
	
	@Column(name = "fund_house")
	private String fundHouse;
	
	@Column(name = "fund_house_pinyin")
	private String fundHousePinyin;
	
	@Column(name = "fund_man_co")
	private String fundManCo;
	
	@Column(name = "fund_manager")
	private String fundManager;
	
	@Column(name = "fund_size")
	private String fundSize;
	
	@Column(name = "fund_currency_code")
	private String fundCurrencyCode;
	
	@Column(name = "fund_currency")
	private String fundCurrency;
	
	@Column(name = "issue_currency_code")
	private String issueCurrencyCode;
	
	@Column(name = "issue_currency")
	private String issueCurrency;
	
	@Column(name = "fund_type_code")
	private String fundTypeCode;
	
	@Column(name = "fund_type")
	private String fundType;
	
	@Column(name = "sector_type_code")
	private String sectorTypeCode;
	
	@Column(name = "sector_type")
	private String sectorType;
	
	@Column(name = "geo_allocation_code")
	private String geoAllocationCode;
	
	@Column(name = "geo_allocation")
	private String geoAllocation;
	
	@Column(name = "inv_target")
	private String invTarget;
	
	@Column(name = "key_risks")
	private String keyRisks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getDomicileCode() {
		return domicileCode;
	}

	public void setDomicileCode(String domicileCode) {
		this.domicileCode = domicileCode;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public FundHouse getFundHouseId() {
		return fundHouseId;
	}

	public void setFundHouseId(FundHouse fundHouseId) {
		this.fundHouseId = fundHouseId;
	}

	public String getFundHouse() {
		return fundHouse;
	}

	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}

	public String getFundManCo() {
		return fundManCo;
	}

	public void setFundManCo(String fundManCo) {
		this.fundManCo = fundManCo;
	}

	public String getFundManager() {
		return fundManager;
	}

	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}

	public String getFundSize() {
		return fundSize;
	}

	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}

	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}

	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}

	public String getFundCurrency() {
		return fundCurrency;
	}

	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}

	public String getFundTypeCode() {
		return fundTypeCode;
	}

	public void setFundTypeCode(String fundTypeCode) {
		this.fundTypeCode = fundTypeCode;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getSectorTypeCode() {
		return sectorTypeCode;
	}

	public void setSectorTypeCode(String sectorTypeCode) {
		this.sectorTypeCode = sectorTypeCode;
	}

	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	public String getGeoAllocationCode() {
		return geoAllocationCode;
	}

	public void setGeoAllocationCode(String geoAllocationCode) {
		this.geoAllocationCode = geoAllocationCode;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getInvTarget() {
		return invTarget;
	}

	public void setInvTarget(String invTarget) {
		this.invTarget = invTarget;
	}

	public String getKeyRisks() {
		return keyRisks;
	}

	public void setKeyRisks(String keyRisks) {
		this.keyRisks = keyRisks;
	}
	
	public String getFundNamePinyin() {
		return fundNamePinyin;
	}

	public void setFundNamePinyin(String fundNamePinyin) {
		this.fundNamePinyin = fundNamePinyin;
	}

	public String getFundHousePinyin() {
		return fundHousePinyin;
	}

	public void setFundHousePinyin(String fundHousePinyin) {
		this.fundHousePinyin = fundHousePinyin;
	}
	
	public String getIssueCurrencyCode() {
		return issueCurrencyCode;
	}

	public void setIssueCurrencyCode(String issueCurrencyCode) {
		this.issueCurrencyCode = issueCurrencyCode;
	}

	public String getIssueCurrency() {
		return issueCurrency;
	}

	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}
}