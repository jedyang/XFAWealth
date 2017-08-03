package com.fsll.wmes.stock.vo;

import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;

/**
 * @author Yan
 *	股票基础信息vo
 */
public class StockProductVO {
	
	private String id;
	private String productId;
	private StockInfo stockInfo;
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
	public StockInfo getStockInfo() {
    	return stockInfo;
    }
	public void setStockInfo(StockInfo stockInfo) {
    	this.stockInfo = stockInfo;
    }
	public ProductInfo getProductInfo() {
    	return productInfo;
    }
	public void setProductInfo(ProductInfo productInfo) {
    	this.productInfo = productInfo;
    }

}
