package com.fsll.app.investor.me.vo;

/**
 * 组合表现数据VO
 * @author zpzhou
 * @date 2016-9-21
 */
public class AppPortfolioPerformanceVo {
	private String portfolioId;
	private String type;
	private String period;//时期：1周，1月，3月，6月，1年，2年…
	private String periodCode;
	private String increase;//增长数（百分比）%
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次 排名
	private String typeTotal;//同类组合总数
	
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getTypeAverage() {
		return typeAverage;
	}
	public void setTypeAverage(String typeAverage) {
		this.typeAverage = typeAverage;
	}
	public String getNewRanking() {
		return newRanking;
	}
	public void setNewRanking(String newRanking) {
		this.newRanking = newRanking;
	}
	public String getLastRanking() {
		return lastRanking;
	}
	public void setLastRanking(String lastRanking) {
		this.lastRanking = lastRanking;
	}
	public String getTypeTotal() {
		return typeTotal;
	}
	public void setTypeTotal(String typeTotal) {
		this.typeTotal = typeTotal;
	}
}
