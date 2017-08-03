package com.fsll.app.investor.market.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppPortfolioArenaDetailVO {
	private AppPortfolioArenaMessVo portfolioInfo;//基本信息
	private AppPortfolioArenaAllocationVO allocationList;//配置情况
	private List<AppPortfolioArenaProductVo> productList;//组合明细
	private AppPortfolioArenaReturnVO returnInfo;//回报行情数据
	
	
	public AppPortfolioArenaMessVo getPortfolioInfo() {
		return portfolioInfo;
	}
	public void setPortfolioInfo(AppPortfolioArenaMessVo portfolioInfo) {
		this.portfolioInfo = portfolioInfo;
	}
	public AppPortfolioArenaAllocationVO getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(AppPortfolioArenaAllocationVO allocationList) {
		this.allocationList = allocationList;
	}
	
	public List<AppPortfolioArenaProductVo> getProductList() {
		return productList;
	}
	public void setProductList(List<AppPortfolioArenaProductVo> productList) {
		this.productList = productList;
	}
	
	public AppPortfolioArenaReturnVO getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(AppPortfolioArenaReturnVO returnInfo) {
		this.returnInfo = returnInfo;
	}
	
	
}
