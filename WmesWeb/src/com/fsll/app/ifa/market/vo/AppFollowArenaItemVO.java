package com.fsll.app.ifa.market.vo;

/**
 * 自选组合实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppFollowArenaItemVO {
	private String arenaId;//组合 Id
	private String portfolioName;//组合名称
//	private String geoAllocation;//地理区域
//	private String sector;//投资领域
	private String riskLevel;//风险等级
	private String periodName;//周期
	private String increase;//周期回报
	private String creatorId;//创建人 Id
	private String creatorName;//创建人 姓名
	private String creatorIconUrl;//创建人 头像
	
	
	public String getArenaId() {
		return arenaId;
	}
	public void setArenaId(String arenaId) {
		this.arenaId = arenaId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
//	public String getGeoAllocation() {
//		return geoAllocation;
//	}
//	public void setGeoAllocation(String geoAllocation) {
//		this.geoAllocation = geoAllocation;
//	}
//	public String getSector() {
//		return sector;
//	}
//	public void setSector(String sector) {
//		this.sector = sector;
//	}	
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}	
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}	
	public String getCreatorIconUrl() {
		return creatorIconUrl;
	}
	public void setCreatorIconUrl(String creatorIconUrl) {
		this.creatorIconUrl = creatorIconUrl;
	}
}
