package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-08
 */
public class AppInvestPortfolioForBuyVO {
	private String riskLevel;//组合风险评级
	private List<AppInvestProductForBuyVO> productList;//组合产品
		

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
