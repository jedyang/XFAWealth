package com.fsll.app.investor.market.vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * 投资策略配置实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppPortfolioArenaAllocationVO {


	private List<AppPortfolioArenaAllocationDetailVO> sectorTypeList;
	private List<AppPortfolioArenaAllocationDetailVO> fundTypeList;
	private List<AppPortfolioArenaAllocationDetailVO> geoAllocationList;

	
	public List<AppPortfolioArenaAllocationDetailVO> getSectorTypeList() {
		return sectorTypeList;
	}

	public void setSectorTypeList(List<AppPortfolioArenaAllocationDetailVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}
	
	public List<AppPortfolioArenaAllocationDetailVO> getFundTypeList() {
		return fundTypeList;
	}

	public void setFundTypeList(List<AppPortfolioArenaAllocationDetailVO> fundTypeList) {
		this.fundTypeList = fundTypeList;
	}
	
	
	public List<AppPortfolioArenaAllocationDetailVO> getGeoAllocationList() {
		return geoAllocationList;
	}

	public void setGeoAllocationList(List<AppPortfolioArenaAllocationDetailVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}


	
}
