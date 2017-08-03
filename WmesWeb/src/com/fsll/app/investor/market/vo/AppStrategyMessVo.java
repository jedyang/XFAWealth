package com.fsll.app.investor.market.vo;

/**
 * 投资策略实体类VO
 * @author zpzhou
 * @date 2016-9-18
 */
public class AppStrategyMessVo {
	private String id;
	private String strategyName;
	private String riskLevel;
	private String investmentGoal;
	private String suitability;
	private String reason;
	private String description;
	private String overhead;//是否置顶
	private String click;
	private String productCount;
	private String createTime;
	private String lastUpdate;//更新时间
	private String creator;
	private String iconUrl;
	private String geoAllocation;
	private String sector;
	private String ifFollow;//是否收藏
	
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
	public String getOverhead() {
		return overhead;
	}
	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
	public String getIfFollow() {
		return ifFollow;
	}
	public void setIfFollow(String ifFollow) {
		this.ifFollow = ifFollow;
	}
}
