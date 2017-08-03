/**
 * 
 */
package com.fsll.app.fund.vo;

/**
 * @author michael
 *	基金表现数据
 */
public class AppFundReturnDataVO {
	private String fundId;
	private String type;//year, heap
	private String period;//时段名称：累积 -- 1月，1年，3年... ; 年 -- 2012,2013...
	private String increase;
	
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次 排名
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
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
	
	
	

}
