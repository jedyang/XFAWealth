package com.fsll.wmes.ifa.vo;

/**
 * 首个投资组合，用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpacePortfoliosVO {
	private String id;
	private String portfoliosName;
	private int riskLeve;
	private double returnRate;
	private double returnTotal;
	private double totalInvestAmount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPortfoliosName() {
		return portfoliosName;
	}
	public void setPortfoliosName(String portfoliosName) {
		this.portfoliosName = portfoliosName;
	}
	public int getRiskLeve() {
		return riskLeve;
	}
	public void setRiskLeve(int riskLeve) {
		this.riskLeve = riskLeve;
	}
	public double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}
	public double getReturnTotal() {
		return returnTotal;
	}
	public void setReturnTotal(double returnTotal) {
		this.returnTotal = returnTotal;
	}
	public double getTotalInvestAmount() {
		return totalInvestAmount;
	}
	public void setTotalInvestAmount(double totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}
	
}
