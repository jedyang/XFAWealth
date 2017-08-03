package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-08
 */
public class AppInvestPortfolioForBuyVO {
	private String portfolioName;//组合名称
	private String clientName;//投资客户名称
	private String mobileCode;//投资者电话区号
	private String mobileNumber;//投资者电话号码
	private String totalBuy;//总投资
	private String baseCurrencyName;//货币名称
	private String riskLevel;//组合风险评级
	private List<AppInvestProductForBuyVO> productList;//组合产品
		

	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(String totalBuy) {
		this.totalBuy = totalBuy;
	}
	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}
	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public List<AppInvestProductForBuyVO> getProductList() {
		return productList;
	}
	public void setProductList(List<AppInvestProductForBuyVO> productList) {
		this.productList = productList;
	}		
}
