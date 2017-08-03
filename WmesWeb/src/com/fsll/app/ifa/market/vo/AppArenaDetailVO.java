package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppArenaDetailVO {
	private AppArenaVO portfolioInfo;//基本信息
	private AppArenaAllocationVO allocationList;//配置情况
	private List<AppArenaProductVO> productList;//组合明细
	private AppArenaReturnVO returnInfo;//回报行情数据
	
	
	public AppArenaVO getPortfolioInfo() {
		return portfolioInfo;
	}
	public void setPortfolioInfo(AppArenaVO portfolioInfo) {
		this.portfolioInfo = portfolioInfo;
	}
	public AppArenaAllocationVO getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(AppArenaAllocationVO allocationList) {
		this.allocationList = allocationList;
	}
	
	public List<AppArenaProductVO> getProductList() {
		return productList;
	}
	public void setProductList(List<AppArenaProductVO> productList) {
		this.productList = productList;
	}
	
	public AppArenaReturnVO getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(AppArenaReturnVO returnInfo) {
		this.returnInfo = returnInfo;
	}
	
	
}
