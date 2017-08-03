/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author scshi
 * 我关注的基金vo
 *
 */
public class FoundFollowVo {
	private String xh;
	private String id;
	private String fundName;
	private String followFlag;
	private String fundCurrency;
	private String fundType;
	private String riskLevel;
	private String lastNav;
	private String lastNavUpdate;
	private String increase;
	private String fundCurrencyCode;
	private String issueDate;
	private String isinCode;
	private String toCurrency;//转换货币
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsinCode() {
		return isinCode;
	}
	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}
	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFollowFlag() {
		return followFlag;
	}
	public void setFollowFlag(String followFlag) {
		this.followFlag = followFlag;
	}
	public String getFundCurrency() {
		return fundCurrency;
	}
	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getLastNav() {
		return lastNav;
	}
	public void setLastNav(String lastNav) {
		this.lastNav = lastNav;
	}
	public String getLastNavUpdate() {
		return lastNavUpdate;
	}
	public void setLastNavUpdate(String lastNavUpdate) {
		this.lastNavUpdate = lastNavUpdate;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	
	
	
}
