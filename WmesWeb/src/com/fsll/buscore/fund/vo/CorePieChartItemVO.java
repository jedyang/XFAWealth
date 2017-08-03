package com.fsll.buscore.fund.vo;

/**
 * 饼图分析 实体类VO
 * @author zxtan
 * @date 2017-02-26
 */
public class CorePieChartItemVO {
	private String itemId;//产品ID
	private String itemName;//产品名称
	private String itemWeight;//分配比率
		
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
	
	
}
