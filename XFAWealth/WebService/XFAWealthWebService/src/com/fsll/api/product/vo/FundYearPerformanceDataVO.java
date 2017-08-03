/**
 * 
 */
package com.fsll.api.product.vo;

/**
 * @author scshi
 *基金年度表现数据
 */
public class FundYearPerformanceDataVO {
	private String fundId;
	
	private String year;
	private String code;
	private String value;
	
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次排名
	private String typeTotal;//同类基金总数
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public String getTypeTotal() {
		return typeTotal;
	}
	public void setTypeTotal(String typeTotal) {
		this.typeTotal = typeTotal;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

}
