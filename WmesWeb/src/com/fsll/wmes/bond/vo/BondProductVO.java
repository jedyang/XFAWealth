package com.fsll.wmes.bond.vo;

import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.ProductInfo;

/**
 * @author Yan
 *	股票基础信息vo
 */
public class BondProductVO {
	
	private String id;
	private String productId;
	private BondInfo bondInfo;
	private ProductInfo productInfo;
	private String name;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BondInfo getBondInfo() {
    	return bondInfo;
    }
	public void setBondInfo(BondInfo bondInfo) {
    	this.bondInfo = bondInfo;
    }
	public ProductInfo getProductInfo() {
    	return productInfo;
    }
	public void setProductInfo(ProductInfo productInfo) {
    	this.productInfo = productInfo;
    }


}
