/**
 * 
 */
package com.fsll.app.investor.me.vo;

/**
 * @author zxtan
 *
 */
public class AppRpqExamResultVO {
	private String examId;//问卷Id
	private String expireDate;//下次RPQ时间
	private String riskLevel;//风险评测等级
	private String riskResult;//风险评测结果
	private String riskRemark;//风险评测说明
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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
