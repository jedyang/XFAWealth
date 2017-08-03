package com.fsll.wmes.crm.vo;

import java.util.List;

import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;

public class PortfolioInfoProductVO {
	
	private ProductInfo product;
	//对应权重
	private Double allocationRate;
	private PortfolioInfo portfolio;
	private FundInfo fundInfo;
	private String fundName;
	//备选产品信息
	private List<PortfolioInfoProductDetailVO> productDetails;

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public Double getAllocationRate() {
		return allocationRate;
	}

	public void setAllocationRate(Double allocationRate) {
		this.allocationRate = allocationRate;
	}

	public PortfolioInfo getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
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

	public List<PortfolioInfoProductDetailVO> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<PortfolioInfoProductDetailVO> productDetails) {
		this.productDetails = productDetails;
	}
	
	
	
}
