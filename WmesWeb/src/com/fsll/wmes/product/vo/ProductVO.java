package com.fsll.wmes.product.vo;

import java.util.Date;

public class ProductVO {
	
	private String id;//对应的产品的id
	private String productId;//产品id
	//private String symbolCode;//产品标志编号
	private String type;//产品类型：fund、bond、stock
	private Date createTime;
	private Date lastUpdate;
	private String isValid;//状态
	
	private String name;//产品名称
	private String isinCode;
	private String currencyCode;//货币
	private String currency;//货币名称
	
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
	//public String getSymbolCode() {
	//	return symbolCode;
	//}
	//public void setSymbolCode(String symbolCode) {
	//	this.symbolCode = symbolCode;
	//}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsinCode() {
		return isinCode;
	}
	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyCode() {
    	return currencyCode;
    }
	public void setCurrencyCode(String currencyCode) {
    	this.currencyCode = currencyCode;
    }
}
