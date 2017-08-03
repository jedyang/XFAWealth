package com.fsll.app.ifa.market.vo;

public class AppAccountRpqInfoVO {
	private String riskLevel;//风险等级
	private String checkDays;//下次评估时间
	private String riskResult;//风险等级
	private String riskRemark;//风险等级
	
	public String getRiskLevel(){
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getCheckDays(){
		return checkDays;
	}
	public void setCheckDays(String checkDays) {
		this.checkDays = checkDays;
	}
	public String getRiskResult() {
		return riskResult;
	}
	public void setRiskResult(String riskResult) {
		this.riskResult = riskResult;
	}
	public String getRiskRemark() {
		return riskRemark;
	}
	public void setRiskRemark(String riskRemark) {
		this.riskRemark = riskRemark;
	}
	
}
