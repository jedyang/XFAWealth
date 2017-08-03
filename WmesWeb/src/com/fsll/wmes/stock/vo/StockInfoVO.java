package com.fsll.wmes.stock.vo;

import java.util.Date;

import com.fsll.wmes.entity.StockHouse;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.entity.ProductInfo;

/**
 * @author michael
 *	股票信息vo
 */
public class StockInfoVO {
	
	private String id;
	private String productId;
	private StockInfo bondInfo;
	private StockInfoEn bondInfoEn;
	private StockInfoSc bondInfoSc;
	private StockInfoTc bondInfoTc;
	
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
	private StockHouse bondHouseId;
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


	public StockInfo getStockInfo() {
    	return bondInfo;
    }
	public void setStockInfo(StockInfo bondInfo) {
    	this.bondInfo = bondInfo;
    }
	public StockInfoEn getStockInfoEn() {
    	return bondInfoEn;
    }
	public void setStockInfoEn(StockInfoEn bondInfoEn) {
    	this.bondInfoEn = bondInfoEn;
    }
	public StockInfoSc getStockInfoSc() {
    	return bondInfoSc;
    }
	public void setStockInfoSc(StockInfoSc bondInfoSc) {
    	this.bondInfoSc = bondInfoSc;
    }
	public StockInfoTc getStockInfoTc() {
    	return bondInfoTc;
    }
	public void setStockInfoTc(StockInfoTc bondInfoTc) {
    	this.bondInfoTc = bondInfoTc;
    }

	/**
	 * 按语言设置基金信息
	 * @param langCode
	 */
	public void setDefaultInfoByLang(String langCode) {
		if ("en".equals(langCode)) {// 英文
			if (this.bondInfoEn != null) {
				bondName  = this.bondInfoEn.getStockName();
				bondNamePinyin = this.bondInfoEn.getStockNamePinyin();
				bondHouse = this.bondInfoEn.getStockHouse();
				bondHousePinyin = this.bondInfoEn.getStockHousePinyin();
				lotSize = this.bondInfoEn.getLotSize();
				bondCurrency = this.bondInfoEn.getStockCurrency();
				bondCurrencyCode = this.bondInfoEn.getStockCurrencyCode();
				industryType = this.bondInfoEn.getIndustryType();
				industryTypeCode = this.bondInfoEn.getIndustryTypeCode();
				geoAllocation = this.bondInfoEn.getGeoAllocation();
				geoAllocationCode = this.bondInfoEn.getGeoAllocationCode();
			}
		} else if ("tc".equals(langCode)) {// 繁体
			if (this.bondInfoTc != null) {
				bondName  = this.bondInfoTc.getStockName();
				bondNamePinyin = this.bondInfoTc.getStockNamePinyin();
				bondHouse = this.bondInfoTc.getStockHouse();
				bondHousePinyin = this.bondInfoTc.getStockHousePinyin();
				lotSize = this.bondInfoTc.getLotSize();
				bondCurrency = this.bondInfoTc.getStockCurrency();
				bondCurrencyCode = this.bondInfoTc.getStockCurrencyCode();
				industryType = this.bondInfoTc.getIndustryType();
				industryTypeCode = this.bondInfoTc.getIndustryTypeCode();
				geoAllocation = this.bondInfoTc.getGeoAllocation();
				geoAllocationCode = this.bondInfoTc.getGeoAllocationCode();
			}
		} else {// 简体
			if (this.bondInfoSc != null) {
				bondName  = this.bondInfoSc.getStockName();
				bondNamePinyin = this.bondInfoSc.getStockNamePinyin();
				bondHouse = this.bondInfoSc.getStockHouse();
				bondHousePinyin = this.bondInfoSc.getStockHousePinyin();
				lotSize = this.bondInfoSc.getLotSize();
				bondCurrency = this.bondInfoSc.getStockCurrency();
				bondCurrencyCode = this.bondInfoSc.getStockCurrencyCode();
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
	public String getStockName() {
    	return bondName;
    }
	public void setStockName(String bondName) {
    	this.bondName = bondName;
    }
	public String getStockNamePinyin() {
    	return bondNamePinyin;
    }
	public void setStockNamePinyin(String bondNamePinyin) {
    	this.bondNamePinyin = bondNamePinyin;
    }
	public StockHouse getStockHouseId() {
    	return bondHouseId;
    }
	public void setStockHouseId(StockHouse bondHouseId) {
    	this.bondHouseId = bondHouseId;
    }
	public String getStockHouse() {
    	return bondHouse;
    }
	public void setStockHouse(String bondHouse) {
    	this.bondHouse = bondHouse;
    }
	public String getStockHousePinyin() {
    	return bondHousePinyin;
    }
	public void setStockHousePinyin(String bondHousePinyin) {
    	this.bondHousePinyin = bondHousePinyin;
    }
	public String getLotSize() {
    	return lotSize;
    }
	public void setLotSize(String lotSize) {
    	this.lotSize = lotSize;
    }
	public String getStockCurrencyCode() {
    	return bondCurrencyCode;
    }
	public void setStockCurrencyCode(String bondCurrencyCode) {
    	this.bondCurrencyCode = bondCurrencyCode;
    }
	public String getStockCurrency() {
    	return bondCurrency;
    }
	public void setStockCurrency(String bondCurrency) {
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
