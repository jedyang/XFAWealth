package com.fsll.app.investor.market.vo;


/**
 * 首页最佳基金VO
 * @author zpzhou
 * @date 2016-9-26
 */
public class AppIndexFundTopVO {
	private String fundId;
	private String fundName;//基金名称
	private String fundType;//基金类型
	private String riskLevel;//风险等级
	private String increase;//回报
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
}
