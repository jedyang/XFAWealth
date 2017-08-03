package com.fsll.wmes.ifa.vo;

import java.util.List;
import java.util.Map;

import com.fsll.wmes.entity.StrategyAllocation;

public class StrategyInfoVO {

	private String id;
	private String strategyName;
	private String geoAllocation;
	private String geoAllocationName;
	private String sector;
	private String sectorName;
	private String investmentGoal;
	private String suitability;
	private String description;
	private String creatorId;
	private String creator;
	private String createTime;
	private String lastUpdate;
	private String overhead;
	private String overheadTime;
	private String status;
	private Integer click;
	private String isPublic;
	private String riskLevel;
	private String reason;
	private List<Map<String,Object>> strategyAllocationsObj; 
	private List<StrategyAllocation> strategyAllocations; 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getGeoAllocation() {
		return geoAllocation;
	}
	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}
	public String getGeoAllocationName() {
		return geoAllocationName;
	}
	public void setGeoAllocationName(String geoAllocationName) {
		this.geoAllocationName = geoAllocationName;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	
	public String getInvestmentGoal() {
		return investmentGoal;
	}
	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}
	public String getSuitability() {
		return suitability;
	}
	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getOverhead() {
		return overhead;
	}
	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}
	public String getOverheadTime() {
		return overheadTime;
	}
	public void setOverheadTime(String overheadTime) {
		this.overheadTime = overheadTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getClick() {
		return click;
	}
	public void setClick(Integer click) {
		this.click = click;
	}
	public String getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}
	public List<StrategyAllocation> getStrategyAllocations() {
		return strategyAllocations;
	}
	public void setStrategyAllocations(List<StrategyAllocation> strategyAllocations) {
		this.strategyAllocations = strategyAllocations;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public List<Map<String, Object>> getStrategyAllocationsObj() {
		return strategyAllocationsObj;
	}
	public void setStrategyAllocationsObj(
			List<Map<String, Object>> strategyAllocationsObj) {
		this.strategyAllocationsObj = strategyAllocationsObj;
	}


	
}
