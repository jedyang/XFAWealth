package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 投资组合实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppHoldDetailVO {
	private AppHoldVO portfolioHold;//基本信息
	private List<AppHoldAllocationVO> allocationList;//产品配置情况
	private List<AppHoldProductVO> fundList;//基金产品列表
	private List<AppHoldProductVO> bondList;//债券产品列表
	private List<AppHoldProductVO> stockList;//股票产品列表
	private List<AppHoldProductVO> insureList;//保险产品列表	
	
	private AppHoldReturnVO returnInfo;//回报行情数据

	private List<AppPieChartItemVO> sectorTypeList;//基金行业配置情况
	private List<AppPieChartItemVO> geoAllocationList;//基金区域配置情况
	
	public AppHoldVO getPortfolioHold() {
		return portfolioHold;
	}
	public void setPortfolioHold(AppHoldVO portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	public List<AppHoldAllocationVO> getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(List<AppHoldAllocationVO> allocationList) {
		this.allocationList = allocationList;
	}
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
	public AppHoldReturnVO getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(AppHoldReturnVO returnInfo) {
		this.returnInfo = returnInfo;
	}
	public List<AppPieChartItemVO> getSectorTypeList() {
		return sectorTypeList;
	}
	public void setSectorTypeList(List<AppPieChartItemVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}
	public List<AppPieChartItemVO> getGeoAllocationList() {
		return geoAllocationList;
	}
	public void setGeoAllocationList(List<AppPieChartItemVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}
	
	
	
	
	
}
