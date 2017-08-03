package com.fsll.app.investor.me.vo;

/**
 * 投资组合用于感兴趣的实体类VO
 * @author zxtan
 * @date 2016-11-28
 */
public class AppPortfolioInfoVO {
	private String id;
	private String portfolioName;//组合名称
	private String investmentGoal;//组合名称
	private String suitability;//投资者适宜性
	private String reason;//推荐理由
	private String riskLevel;//风险级别
	private String totalReturn;//自创建以来的总回报率
	private String description;//投资组合描述
	private String click;
	private String productCount;
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
	private String increase;// 周期回报
	private String creator;//ifa名字
	private String iconUrl;//ifa头像
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
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
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(String totalReturn) {
		this.totalReturn = totalReturn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
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
}
