package com.fsll.app.ifa.market.vo;

/**
 * 自选策略实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppStrategyInfoItemVO {
	private String strategyId;//策略 Id
	private String strategyName;//策略名称
	private String geoAllocation;//地理区域
	private String sector;//投资领域
	private String riskLevel;//风险等级
	private String investmentGoal;//投资目标
	private String suitability;//投资者适宜性
	private String reason;//推荐原因
	private String creatorId;//创建人 Id
	private String creatorName;//创建人 姓名
	private String creatorIconUrl;//创建人 头像
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
	private String status;//状态，0：草稿；1：已经发布
	private String isValid;//数据是否有效，1是  0否
	private String ifFollow;//是否收藏自选，1是  0否
	
	
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
	public String getIfFollow() {
		return ifFollow;
	}
	public void setIfFollow(String ifFollow) {
		this.ifFollow = ifFollow;
	}
}
