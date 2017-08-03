package com.fsll.wmes.strategy.vo;

import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.StrategyInfo;

public class StrategyDetailVO {

	private String id;
	private String strategyName;
	private String geoAllocation;
	private String sector;
	private String creator;
	private String createTime;
	private String riskLevel;
	private String investmentGoal;
	private String suitability;
	private String reason;
	private String description;
	private String allocationData;
	private Integer productCount;
	// 访问人数量
	private Integer visitCount;
	private Integer click;
	
	public StrategyDetailVO(){
		
	}
	
	public StrategyDetailVO( StrategyInfo strategyInfo ) {
		this.id = strategyInfo.getId();
		this.strategyName = strategyInfo.getStrategyName();
		this.geoAllocation = strategyInfo.getGeoAllocation();
		this.sector = strategyInfo.getSector();
		this.investmentGoal = strategyInfo.getInvestmentGoal();
		this.suitability = strategyInfo.getSuitability();
		this.description = strategyInfo.getDescription();
		this.creator = strategyInfo.getCreator().getNickName();
		this.createTime = null!=strategyInfo.getCreateTime()?DateUtil.dateToDateString(strategyInfo.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.click = strategyInfo.getClick();
		this.productCount = strategyInfo.getProductCount();
		this.riskLevel=strategyInfo.getRiskLevel();
		this.reason=strategyInfo.getReason();
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

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
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

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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

	public String getAllocationData() {
		return allocationData;
	}

	public void setAllocationData(String allocationData) {
		this.allocationData = allocationData;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

}
