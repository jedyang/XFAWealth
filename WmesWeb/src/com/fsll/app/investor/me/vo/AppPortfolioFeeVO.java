package com.fsll.app.investor.me.vo;

/**
 * 组合交易费用实体类VO
 * @author zxtan
 * @date 2017-01-11
 */
public class AppPortfolioFeeVO {
	private String holdId;
	private String productId;
	private String fundName;
	private String fee;
	private String currency;
	private String createTime;
	

	public String getHoldId(){
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	public String getProductId(){
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getFundName(){
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFee(){
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getCurrency(){
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCreateTime(){
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
}
