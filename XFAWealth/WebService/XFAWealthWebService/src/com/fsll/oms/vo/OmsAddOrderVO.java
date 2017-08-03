/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.vo;

/**
 * 下订单的vo
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-31
 */
public class OmsAddOrderVO {
	private String creatorNo;//下单人的accountNo 
	private String orderType;//订单类型 1基金(默认) 2股票 3.....
	private String accountNo;//采购人的帐号
	private String symbolCode;//订单关联的产品
	private String currencyCode;//货币符号,默认是HKD,港币
	private Double limitedPrice;//价格   买卖价格          对于申购基金等于申购金额,对于赎回基金等于份额净值
	private Double quantity;//数量   买卖数量   申购基金时等于1 
	private String tranType;//方向 0买 1卖
	private String orderNO;//订单编号    我们系统生成的编号,等于order_history.id
	private String aeCode;//此客户关系的ae code
	private String tradingPassword;//交易密码
	private Double tranFeeRate;//交易费用比率 对应order_history.tran_rate
	private Double tranFeeMini;//最低费用 对应order_history.tran_fee_mini
	private String switchFlag;//基金转换时使用,1表示转换单,0表示正常单(默认) 对应order_history.switch_flag
	private String switchGroup;//基金转换时有效 转换关联的组 对应order_history.switch_group
	private Double switchAllocRate;//基金转换时有效  转换成买入时的比例 对应order_history.switch_alloc_rate
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getSymbolCode() {
		return symbolCode;
	}
	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}
	public Double getLimitedPrice() {
		return limitedPrice;
	}
	public void setLimitedPrice(Double limitedPrice) {
		this.limitedPrice = limitedPrice;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public String getAeCode() {
		return aeCode;
	}
	public void setAeCode(String aeCode) {
		this.aeCode = aeCode;
	}
	public String getTradingPassword() {
		return tradingPassword;
	}
	public void setTradingPassword(String tradingPassword) {
		this.tradingPassword = tradingPassword;
	}
	public String getCreatorNo() {
		return creatorNo;
	}
	public void setCreatorNo(String creatorNo) {
		this.creatorNo = creatorNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Double getTranFeeMini() {
		return tranFeeMini;
	}
	public void setTranFeeMini(Double tranFeeMini) {
		this.tranFeeMini = tranFeeMini;
	}
	
	public Double getTranFeeRate() {
		return tranFeeRate;
	}
	public void setTranFeeRate(Double tranFeeRate) {
		this.tranFeeRate = tranFeeRate;
	}
	public String getSwitchFlag() {
		return switchFlag;
	}
	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}
	public String getSwitchGroup() {
		return switchGroup;
	}
	public void setSwitchGroup(String switchGroup) {
		this.switchGroup = switchGroup;
	}
	public Double getSwitchAllocRate() {
		return switchAllocRate;
	}
	public void setSwitchAllocRate(Double switchAllocRate) {
		this.switchAllocRate = switchAllocRate;
	}
}
