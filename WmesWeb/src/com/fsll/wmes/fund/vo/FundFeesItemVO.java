package com.fsll.wmes.fund.vo;

/**
 * 基金费用数据
 * 
 * @author tan
 * @date 2016-7-4
 */
public class FundFeesItemVO {
	
    private String id;    
	private String feesItemCode;    
	private String feesItem;    
	private String fees;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeesItemCode() {
		return feesItemCode;
	}

	public void setFeesItemCode(String feesItemCode) {
		this.feesItemCode = feesItemCode;
	}

	public String getFeesItem() {
		return feesItem;
	}

	public void setFeesItem(String feesItem) {
		this.feesItem = feesItem;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}
}
