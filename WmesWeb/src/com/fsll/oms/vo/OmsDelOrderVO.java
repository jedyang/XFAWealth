/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.vo;

/**
 * 删除订单的vo
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-31
 */
public class OmsDelOrderVO {
	private String creatorNo;//下单人的accountNo
	private String orderNO;//订单编号    我们系统生成的编号,等于order_history.id
	private String accountNo;//此订单关联的帐号
	private String omsOrderNO;//oms的订单编号
	private String aeCode;//此客户关系的ae code
	private String tradingPassword;//交易密码
	public String getOmsOrderNO() {
		return omsOrderNO;
	}
	public void setOmsOrderNO(String omsOrderNO) {
		this.omsOrderNO = omsOrderNO;
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public String getCreatorNo() {
		return creatorNo;
	}
	public void setCreatorNo(String creatorNo) {
		this.creatorNo = creatorNo;
	}
}
