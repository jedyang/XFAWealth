/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author scshi
 *基金管理费用信息
 */
public class FundChargesDataVO {
	private String fundId;
	private String itemName;
	private String description;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	
	
}
