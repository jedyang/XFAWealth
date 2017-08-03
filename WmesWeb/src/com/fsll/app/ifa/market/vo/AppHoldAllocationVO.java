package com.fsll.app.ifa.market.vo;

/**
 * 投资组合配置实体类VO
 * @author zxtan
 * @date 2016-12-27
 */
public class AppHoldAllocationVO {

	private String itemName;//名称		
	private String itemRate;//比重
	private String itemValue;//市值
	private String itemIncrease;//回报率
	private String itemCurrency;//货币
	private String itemReturn;//回报值

	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemRate() {
		return itemRate;
	}

	public void setItemRate(String itemRate) {
		this.itemRate = itemRate;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemIncrease() {
		return itemIncrease;
	}

	public void setItemIncrease(String itemIncrease) {
		this.itemIncrease = itemIncrease;
	}

	public String getItemCurrency() {
		return itemCurrency;
	}

	public void setItemCurrency(String itemCurrency) {
		this.itemCurrency = itemCurrency;
	}

	public String getItemReturn() {
		return itemReturn;
	}

	public void setItemReturn(String itemReturn) {
		this.itemReturn = itemReturn;
	}
}
