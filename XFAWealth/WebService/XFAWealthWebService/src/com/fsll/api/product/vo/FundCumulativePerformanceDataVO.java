/**
 * 
 */
package com.fsll.api.product.vo;

/**
 * @author scshi
 *	基金累积表现数据
 */
public class FundCumulativePerformanceDataVO {
	private String fundId;
	
	private String period;//时期：1周，1月，3月，6月，1年，2年…
	private String value;//回报值
	private String code;
	
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次 排名
	private String typeTotal;//同类基金总数
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getTypeTotal() {
		return typeTotal;
	}
	public void setTypeTotal(String typeTotal) {
		this.typeTotal = typeTotal;
	}
	

}
