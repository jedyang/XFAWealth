/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.vo;

import java.util.Date;

/**
 * 基金行情数据
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public class CoreFundNavVO {
	private String fundId;
	private Date marketDate;
	private String marketDateStr; //App使用
	private Double nav;
	private Double rate;
	private String rateStr;
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public Date getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}
	public String getMarketDateStr() {
		return marketDateStr;
	}
	public void setMarketDateStr(String marketDateStr) {
		this.marketDateStr = marketDateStr;
	}
	public Double getNav() {
		return nav;
	}
	public void setNav(Double nav) {
		this.nav = nav;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getRateStr() {
		return rateStr;
	}
	public void setRateStr(String rateStr) {
		this.rateStr = rateStr;
	}
	
}
