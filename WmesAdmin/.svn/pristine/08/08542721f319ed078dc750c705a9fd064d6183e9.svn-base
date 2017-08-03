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
@Table(name = "stock_info_en")
public class StockInfoEn implements java.io.Serializable {
	private String id;
	private String stockName;
	private String stockNamePinyin;
	private StockHouse stockHouseId;
	private String stockHouse;
	private String stockHousePinyin;
	private String lotSize;
	private String stockCurrencyCode;
	private String stockCurrency;
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

	@Column(name = "stock_name")
	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Column(name = "stock_name_pinyin")
	public String getStockNamePinyin() {
		return this.stockNamePinyin;
	}

	public void setStockNamePinyin(String stockNamePinyin) {
		this.stockNamePinyin = stockNamePinyin;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_house_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	public StockHouse getStockHouseId() {
		return this.stockHouseId;
	}

	public void setStockHouseId(StockHouse stockHouseId) {
		this.stockHouseId = stockHouseId;
	}

	@Column(name = "stock_house")
	public String getStockHouse() {
		return this.stockHouse;
	}

	public void setStockHouse(String stockHouse) {
		this.stockHouse = stockHouse;
	}

	@Column(name = "stock_house_pinyin")
	public String getStockHousePinyin() {
		return this.stockHousePinyin;
	}

	public void setStockHousePinyin(String stockHousePinyin) {
		this.stockHousePinyin = stockHousePinyin;
	}

	@Column(name = "lot_size")
	public String getLotSize() {
		return this.lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	@Column(name = "stock_currency_code")
	public String getStockCurrencyCode() {
		return this.stockCurrencyCode;
	}

	public void setStockCurrencyCode(String stockCurrencyCode) {
		this.stockCurrencyCode = stockCurrencyCode;
	}

	@Column(name = "stock_currency")
	public String getStockCurrency() {
		return this.stockCurrency;
	}

	public void setStockCurrency(String stockCurrency) {
		this.stockCurrency = stockCurrency;
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