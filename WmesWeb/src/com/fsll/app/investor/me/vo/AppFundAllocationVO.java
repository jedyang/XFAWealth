package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 资产分析——基金配置实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppFundAllocationVO {
	private List<AppPortfolioAllocationVO> sectorTypeList;//基金行业配置情况
	
	private List<AppPortfolioAllocationVO> geoAllocationList;//基金区域配置情况
	private List<AppPortfolioProductVo> fundList;//基金类型配置情况
	
	public List<AppPortfolioAllocationVO> getSectorTypeList() {
		return sectorTypeList;
	}
	public void setSectorTypeList(List<AppPortfolioAllocationVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}
	public List<AppPortfolioProductVo> getFundList() {
		return fundList;
	}
	public void setFundList(List<AppPortfolioProductVo> fundList) {
		this.fundList = fundList;
	}
	public List<AppPortfolioAllocationVO> getGeoAllocationList() {
		return geoAllocationList;
	}
	public void setGeoAllocationList(List<AppPortfolioAllocationVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}
		
}
