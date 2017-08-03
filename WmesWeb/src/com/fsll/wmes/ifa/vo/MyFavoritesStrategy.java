package com.fsll.wmes.ifa.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 显示我收藏的策略数据列表
 * @author 林文伟
 * @date 2016-9-28
 */
public class MyFavoritesStrategy {
	private String favoritesId;
	private String strategyId;
	private String strategyName;
	private String strategyCreateName;
	private String strategyCreateMemberId;
	private Date createTime;
	private Date favoritesTime;
	private String favoritesTimeStr;
	private Integer fundsCount;
	
	private String riskLevel;
	private String geoAllocationName;
	private String sectorName;
	private String strategyAllocationsObj; 
	private String createrHeadUrl;
	
	public String getFavoritesId() {
		return favoritesId;
	}
	public void setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
	}
	public String getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getStrategyCreateName() {
		return strategyCreateName;
	}
	public void setStrategyCreateName(String strategyCreateName) {
		this.strategyCreateName = strategyCreateName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getFavoritesTime() {
		return favoritesTime;
	}
	public void setFavoritesTime(Date favoritesTime) {
		this.favoritesTime = favoritesTime;
	}
	public Integer getFundsCount() {
		return fundsCount;
	}
	public void setFundsCount(Integer fundsCount) {
		this.fundsCount = fundsCount;
	}
	public String getFavoritesTimeStr() {
		return favoritesTimeStr;
	}
	public void setFavoritesTimeStr(String favoritesTimeStr) {
		this.favoritesTimeStr = favoritesTimeStr;
	}
	public String getGeoAllocationName() {
		return geoAllocationName;
	}
	public void setGeoAllocationName(String geoAllocationName) {
		this.geoAllocationName = geoAllocationName;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public String getStrategyAllocationsObj() {
		return strategyAllocationsObj;
	}
	public void setStrategyAllocationsObj(String strategyAllocationsObj) {
		this.strategyAllocationsObj = strategyAllocationsObj;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getCreaterHeadUrl() {
		return createrHeadUrl;
	}
	public void setCreaterHeadUrl(String createrHeadUrl) {
		this.createrHeadUrl = createrHeadUrl;
	}
	public String getStrategyCreateMemberId() {
		return strategyCreateMemberId;
	}
	public void setStrategyCreateMemberId(String strategyCreateMemberId) {
		this.strategyCreateMemberId = strategyCreateMemberId;
	}
	
	
	
}

