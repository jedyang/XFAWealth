package com.fsll.wmes.bond.vo;

import java.util.Date;

import com.fsll.wmes.entity.BondHouse;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.ProductInfo;

/**
 * @author michael
 *	债劵信息vo
 */
public class BondInfoVO {
	
	private String id;
	private String productId;
	private BondInfo bondInfo;
	private BondInfoEn bondInfoEn;
	private BondInfoSc bondInfoSc;
	private BondInfoTc bondInfoTc;
	
	//基本信息
	private ProductInfo product;
	private String symbolCode;
	private String exchangeCode;
	private Date createTime;
	private Date lastUpdate;
	private String overhead;
	private Date overheadTime;
	private String isValid;
	
	//多语言信息
	private String bondName;
	private String bondNamePinyin;
	private BondHouse bondHouseId;
	private String bondHouse;
	private String bondHousePinyin;
	private String lotSize;
	private String bondCurrencyCode;
	private String bondCurrency;
	private String industryTypeCode;
	private String industryType;
	private String geoAllocationCode;
	private String geoAllocation;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}


	public BondInfo getBondInfo() {
    	return bondInfo;
    }
	public void setBondInfo(BondInfo bondInfo) {
    	this.bondInfo = bondInfo;
    }
	public BondInfoEn getBondInfoEn() {
    	return bondInfoEn;
    }
	public void setBondInfoEn(BondInfoEn bondInfoEn) {
    	this.bondInfoEn = bondInfoEn;
    }
	public BondInfoSc getBondInfoSc() {
    	return bondInfoSc;
    }
	public void setBondInfoSc(BondInfoSc bondInfoSc) {
    	this.bondInfoSc = bondInfoSc;
    }
	public BondInfoTc getBondInfoTc() {
    	return bondInfoTc;
    }
	public void setBondInfoTc(BondInfoTc bondInfoTc) {
    	this.bondInfoTc = bondInfoTc;
    }

	/**
	 * 按语言设置基金信息
	 * @param langCode
	 */
	public void setDefaultInfoByLang(String langCode) {
		if ("en".equals(langCode)) {// 英文
			if (this.bondInfoEn != null) {
				bondName  = this.bondInfoEn.getBondName();
				bondNamePinyin = this.bondInfoEn.getBondNamePinyin();
				bondHouse = this.bondInfoEn.getBondHouse();
				bondHousePinyin = this.bondInfoEn.getBondHousePinyin();
				lotSize = this.bondInfoEn.getLotSize();
				bondCurrency = this.bondInfoEn.getBondCurrency();
				bondCurrencyCode = this.bondInfoEn.getBondCurrencyCode();
				industryType = this.bondInfoEn.getIndustryType();
				industryTypeCode = this.bondInfoEn.getIndustryTypeCode();
				geoAllocation = this.bondInfoEn.getGeoAllocation();
				geoAllocationCode = this.bondInfoEn.getGeoAllocationCode();
			}
		} else if ("tc".equals(langCode)) {// 繁体
			if (this.bondInfoTc != null) {
				bondName  = this.bondInfoTc.getBondName();
				bondNamePinyin = this.bondInfoTc.getBondNamePinyin();
				bondHouse = this.bondInfoTc.getBondHouse();
				bondHousePinyin = this.bondInfoTc.getBondHousePinyin();
				lotSize = this.bondInfoTc.getLotSize();
				bondCurrency = this.bondInfoTc.getBondCurrency();
				bondCurrencyCode = this.bondInfoTc.getBondCurrencyCode();
				industryType = this.bondInfoTc.getIndustryType();
				industryTypeCode = this.bondInfoTc.getIndustryTypeCode();
				geoAllocation = this.bondInfoTc.getGeoAllocation();
				geoAllocationCode = this.bondInfoTc.getGeoAllocationCode();
			}
		} else {// 简体
			if (this.bondInfoSc != null) {
				bondName  = this.bondInfoSc.getBondName();
				bondNamePinyin = this.bondInfoSc.getBondNamePinyin();
				bondHouse = this.bondInfoSc.getBondHouse();
				bondHousePinyin = this.bondInfoSc.getBondHousePinyin();
				lotSize = this.bondInfoSc.getLotSize();
				bondCurrency = this.bondInfoSc.getBondCurrency();
				bondCurrencyCode = this.bondInfoSc.getBondCurrencyCode();
				industryType = this.bondInfoSc.getIndustryType();
				industryTypeCode = this.bondInfoSc.getIndustryTypeCode();
				geoAllocation = this.bondInfoSc.getGeoAllocation();
				geoAllocationCode = this.bondInfoSc.getGeoAllocationCode();
			}
		}
	}
	public ProductInfo getProduct() {
    	return product;
    }
	public void setProduct(ProductInfo product) {
    	this.product = product;
    }
	public String getSymbolCode() {
    	return symbolCode;
    }
	public void setSymbolCode(String symbolCode) {
    	this.symbolCode = symbolCode;
    }
	public String getExchangeCode() {
    	return exchangeCode;
    }
	public void setExchangeCode(String exchangeCode) {
    	this.exchangeCode = exchangeCode;
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
	public String getOverhead() {
    	return overhead;
    }
	public void setOverhead(String overhead) {
    	this.overhead = overhead;
    }
	public Date getOverheadTime() {
    	return overheadTime;
    }
	public void setOverheadTime(Date overheadTime) {
    	this.overheadTime = overheadTime;
    }
	public String getIsValid() {
    	return isValid;
    }
	public void setIsValid(String isValid) {
    	this.isValid = isValid;
    }
	public String getBondName() {
    	return bondName;
    }
	public void setBondName(String bondName) {
    	this.bondName = bondName;
    }
	public String getBondNamePinyin() {
    	return bondNamePinyin;
    }
	public void setBondNamePinyin(String bondNamePinyin) {
    	this.bondNamePinyin = bondNamePinyin;
    }
	public BondHouse getBondHouseId() {
    	return bondHouseId;
    }
	public void setBondHouseId(BondHouse bondHouseId) {
    	this.bondHouseId = bondHouseId;
    }
	public String getBondHouse() {
    	return bondHouse;
    }
	public void setBondHouse(String bondHouse) {
    	this.bondHouse = bondHouse;
    }
	public String getBondHousePinyin() {
    	return bondHousePinyin;
    }
	public void setBondHousePinyin(String bondHousePinyin) {
    	this.bondHousePinyin = bondHousePinyin;
    }
	public String getLotSize() {
    	return lotSize;
    }
	public void setLotSize(String lotSize) {
    	this.lotSize = lotSize;
    }
	public String getBondCurrencyCode() {
    	return bondCurrencyCode;
    }
	public void setBondCurrencyCode(String bondCurrencyCode) {
    	this.bondCurrencyCode = bondCurrencyCode;
    }
	public String getBondCurrency() {
    	return bondCurrency;
    }
	public void setBondCurrency(String bondCurrency) {
    	this.bondCurrency = bondCurrency;
    }
	public String getIndustryTypeCode() {
    	return industryTypeCode;
    }
	public void setIndustryTypeCode(String industryTypeCode) {
    	this.industryTypeCode = industryTypeCode;
    }
	public String getIndustryType() {
    	return industryType;
    }
	public void setIndustryType(String industryType) {
    	this.industryType = industryType;
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
}
