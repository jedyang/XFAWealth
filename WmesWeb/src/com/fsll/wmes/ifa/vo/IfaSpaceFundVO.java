package com.fsll.wmes.ifa.vo;
/**
 * 显示基金数据，供用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceFundVO {
	private String fundId;
	private String fundName;
	private double increaseRate;
	private String periodName;
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
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
	public double getIncreaseRate() {
		return increaseRate;
	}
	public void setIncreaseRate(double increaseRate) {
		this.increaseRate = increaseRate;
	}
	
	
}
