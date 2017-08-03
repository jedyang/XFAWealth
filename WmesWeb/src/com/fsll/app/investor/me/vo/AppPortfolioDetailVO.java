package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppPortfolioDetailVO {
	private AppPortfolioHoldVO portfolioHold;//基本信息
	private List<AppPortfolioAllocationVO> allocationList;//产品配置情况
//	private List<AppPortfolioProductVo> productList;//组合明细
	
	private List<AppHoldProductVO> fundList;//基金产品列表
	private List<AppHoldProductVO> bondList;//债券产品列表
	private List<AppHoldProductVO> stockList;//股票产品列表
	private List<AppHoldProductVO> insureList;//保险产品列表	
	
	private AppPortfolioReturnVO returnInfo;//回报行情数据

	private List<AppPieChartItemVO> sectorTypeList;//基金行业配置情况
//	private List<AppPortfolioAllocationVO> fundTypeList;//基金类型配置情况
	private List<AppPieChartItemVO> geoAllocationList;//基金区域配置情况
	
	
	public AppPortfolioHoldVO getPortfolioHold() {
		return portfolioHold;
	}
	public void setPortfolioHold(AppPortfolioHoldVO portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	public List<AppPortfolioAllocationVO> getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(List<AppPortfolioAllocationVO> allocationList) {
		this.allocationList = allocationList;
	}
	public List<AppPieChartItemVO> getSectorTypeList() {
		return sectorTypeList;
	}
	public void setSectorTypeList(List<AppPieChartItemVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}
//	public List<AppPortfolioAllocationVO> getFundTypeList() {
//		return fundTypeList;
//	}
//	public void setFundTypeList(List<AppPortfolioAllocationVO> fundTypeList) {
//		this.fundTypeList = fundTypeList;
//	}
	public List<AppPieChartItemVO> getGeoAllocationList() {
		return geoAllocationList;
	}
	public void setGeoAllocationList(List<AppPieChartItemVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}
	
//	public List<AppPortfolioProductVo> getProductList() {
//		return productList;
//	}
//	public void setProductList(List<AppPortfolioProductVo> productList) {
//		this.productList = productList;
//	}
	
	public List<AppHoldProductVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<AppHoldProductVO> fundList) {
		this.fundList = fundList;
	}
	public List<AppHoldProductVO> getBondList() {
		return bondList;
	}
	public void setBondList(List<AppHoldProductVO> bondList) {
		this.bondList = bondList;
	}
	public List<AppHoldProductVO> getStockList() {
		return stockList;
	}
	public void setStockList(List<AppHoldProductVO> stockList) {
		this.stockList = stockList;
	}
	public List<AppHoldProductVO> getInsureList() {
		return insureList;
	}
	public void setInsureList(List<AppHoldProductVO> insureList) {
		this.insureList = insureList;
	}
	
	public AppPortfolioReturnVO getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(AppPortfolioReturnVO returnInfo) {
		this.returnInfo = returnInfo;
	}
	
	
}
