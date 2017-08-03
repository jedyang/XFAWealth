package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-26
 */
public class AppInvestPortfolioForRebalanceVO {
	private String riskLevel;//组合风险评级
	private List<AppPieChartItemVO> productList;//组合产品
		

	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public List<AppPieChartItemVO> getProductList() {
		return productList;
	}
	public void setProductList(List<AppPieChartItemVO> productList) {
		this.productList = productList;
	}		
}
