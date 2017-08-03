package com.fsll.app.investor.market.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页基金图表数据VO
 * @author zpzhou
 * @date 2016-9-26
 */
public class AppIndexChartVO {
	private String startDate;
	private String endDate;
	private String id;
	private String name;//名称
	private String riskLevel;//风险等级
	private String increase;//回报
	private String num;//被用于组合的数量
	private List<AppIndexMarketDataVO> dataList=new ArrayList<AppIndexMarketDataVO>();
	
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public List<AppIndexMarketDataVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<AppIndexMarketDataVO> dataList) {
		this.dataList = dataList;
	}

}
