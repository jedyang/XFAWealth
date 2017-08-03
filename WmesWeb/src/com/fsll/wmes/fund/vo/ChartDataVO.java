/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.List;

/**
 * @author scshi
 *	基金图表数据
 */
public class ChartDataVO {
	private String fundId;
	private String date;
	private String value;
	private String name;
	private String type;
	
	//以下用于app基金净值图表
	private String periodCode;
	private String periodName;
	private String startDate;
	private String endDate;
	private String increase;//回报（按period来获取）
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次排名
	private String typeTotal;//同类基金总数
	
	private List<FundMarketDataVO> dataList;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public List<FundMarketDataVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<FundMarketDataVO> dataList) {
		this.dataList = dataList;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
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
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}

}
