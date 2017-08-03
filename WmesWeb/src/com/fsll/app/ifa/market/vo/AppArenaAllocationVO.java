package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 投资策略配置实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppArenaAllocationVO {


	private List<AppArenaAllocationDetailVO> sectorTypeList;
	private List<AppArenaAllocationDetailVO> fundTypeList;
	private List<AppArenaAllocationDetailVO> geoAllocationList;

	
	public List<AppArenaAllocationDetailVO> getSectorTypeList() {
		return sectorTypeList;
	}

	public void setSectorTypeList(List<AppArenaAllocationDetailVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}
	
	public List<AppArenaAllocationDetailVO> getFundTypeList() {
		return fundTypeList;
	}

	public void setFundTypeList(List<AppArenaAllocationDetailVO> fundTypeList) {
		this.fundTypeList = fundTypeList;
	}
	
	
	public List<AppArenaAllocationDetailVO> getGeoAllocationList() {
		return geoAllocationList;
	}

	public void setGeoAllocationList(List<AppArenaAllocationDetailVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}


	
}
