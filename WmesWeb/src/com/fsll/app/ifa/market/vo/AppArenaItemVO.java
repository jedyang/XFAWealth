package com.fsll.app.ifa.market.vo;

/**
 * 自选组合实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppArenaItemVO {
	private String arenaId;//组合 Id
	private String portfolioName;//组合名称
	private String riskLevel;//风险等级
	private String investmentGoal;//投资目标
	private String suitability;//投资者适宜性
	private String reason;//推荐原因
	private String periodName;//周期
	private String increase;//周期回报
	private String investDays;//投资天数
	private String status;//状态，0：草稿；1：已经发布
	private String isValid;//是否有效，1是，0否

	private String creatorId;//创建人 Id
	private String creatorName;//创建人 姓名
	private String creatorIconUrl;//创建人 头像
	private String ifFollow;//是否收藏自选，1是  0否
	
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
	public String getInvestDays() {
		return investDays;
	}
	public void setInvestDays(String investDays) {
		this.investDays = investDays;
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
	public String getIfFollow() {
		return ifFollow;
	}
	public void setIfFollow(String ifFollow) {
		this.ifFollow = ifFollow;
	}
}
