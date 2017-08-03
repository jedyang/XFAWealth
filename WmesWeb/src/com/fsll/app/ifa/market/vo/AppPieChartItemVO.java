package com.fsll.app.ifa.market.vo;

/**
 * 饼图分析 实体类VO
 * @author zxtan
 * @date 2017-02-26
 */
public class AppPieChartItemVO {
	private String itemId;//ID
	private String itemName;//名称
	private String itemWeight;//分配比率
	private String itemValue;//数量
		
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}	
	public String getItemWeight() {
		return itemWeight;
	}
	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}	
	
}
