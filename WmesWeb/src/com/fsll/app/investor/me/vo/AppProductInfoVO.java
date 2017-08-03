package com.fsll.app.investor.me.vo;

/**
 * 组合产品实体类VO
 * @author zxtan
 * @date 2017-02-27
 */
public class AppProductInfoVO {
	private String productId;//产品ID
	private String productName;//产品名称
	private String productCode;//产品名称
	private Integer riskLevel;//风险评级
	private Double lastNav;//最新产品价格
	private String baseCurrency;//产品资金货币
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public Integer getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Double getLastNav() {
		return lastNav;
	}
	public void setLastNav(Double lastNav) {
		this.lastNav = lastNav;
	}
		
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
		
}
