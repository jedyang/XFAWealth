package com.fsll.wmes.crm.vo;

import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;

/**
 * 
 * 备选产品VO
 *
 */
public class PortfolioInfoProductDetailVO {

	private PortfolioInfo portfolio;
	private ProductInfo product;
	private FundInfo fundInfo;
	private String fundName;
	//备选产品
	private ProductInfo spareProduct;
	//备选基金
	private FundInfo spareFundInfo;
	//备选基金名称
	private String spareFundName;
	//是否默认
	private String isDef;
	public PortfolioInfo getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}
	public ProductInfo getProduct() {
		return product;
	}
	public void setProduct(ProductInfo product) {
		this.product = product;
	}
	public FundInfo getFundInfo() {
		return fundInfo;
	}
	public void setFundInfo(FundInfo fundInfo) {
		this.fundInfo = fundInfo;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public ProductInfo getSpareProduct() {
		return spareProduct;
	}
	public void setSpareProduct(ProductInfo spareProduct) {
		this.spareProduct = spareProduct;
	}
	public FundInfo getSpareFundInfo() {
		return spareFundInfo;
	}
	public void setSpareFundInfo(FundInfo spareFundInfo) {
		this.spareFundInfo = spareFundInfo;
	}
	public String getSpareFundName() {
		return spareFundName;
	}
	public void setSpareFundName(String spareFundName) {
		this.spareFundName = spareFundName;
	}
	public String getIsDef() {
		return isDef;
	}
	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}
	
	
}
