package com.fsll.wmes.strategy.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.StrategyInfo;

public class StrategyListVO {
	private String id;
	private String strategyName;
	private String riskLevel;
	private String geoAllocation;
	private String geoAllocationName;
	private String sector;
	private String sectorName;
	private String investmentGoal;
	private String suitability;
	private String reason;
	private String description;
	private MemberBase creator;
	private String creatorName;
	private Date createTime;
	private Date lastUpdate;
	private String overhead;
	private Date overheadTime;
	private String status;
	private String isValid;
	private String isPublic;

	
	public StrategyListVO(StrategyInfo info){
		this.id=info.getId();
		this.strategyName=info.getStrategyName();
		this.riskLevel=info.getRiskLevel();
		this.geoAllocation=info.getGeoAllocation();
		this.sector=info.getSector();
		this.investmentGoal=info.getInvestmentGoal();
		this.suitability=info.getSuitability();
		this.reason=info.getReason();
		this.description=info.getDescription();
		this.creator=info.getCreator();
		this.createTime=info.getCreateTime();
		this.lastUpdate=info.getLastUpdate();
		this.overhead=info.getOverhead();
		this.overheadTime=info.getOverheadTime();
		this.status=info.getStatus();
		this.isValid=info.getIsValid();
		this.isPublic=info.getIsPublic();
	}
	
	
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

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getOverhead() {
		return overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public Date getOverheadTime() {
		return overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

}
