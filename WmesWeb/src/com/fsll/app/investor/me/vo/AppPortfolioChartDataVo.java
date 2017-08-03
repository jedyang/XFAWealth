package com.fsll.app.investor.me.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合图表数据实体类VO
 * @author zpzhou
 * @date 2016-9-21
 */
public class AppPortfolioChartDataVo {
	private String periodCode;
	private String periodName;
	private String startDate;
	private String endDate;
	private String increase;//回报（按period来获取）
	private String typeAverage;//同类平均
	private String newRanking;//最新排名
	private String lastRanking;//上次排名
	private String typeTotal;//同类组合总数
	
	private List<AppPortfolioMarketMessVo> dataList=new ArrayList<AppPortfolioMarketMessVo>();

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

	public List<AppPortfolioMarketMessVo> getDataList() {
		return dataList;
	}

	public void setDataList(List<AppPortfolioMarketMessVo> dataList) {
		this.dataList = dataList;
	}
}
