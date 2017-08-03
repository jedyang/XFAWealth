/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 基金行情数据
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public class CorePortfolioVO {
	private String portfolioId;//组合Id
	private Date marketDate;//行情日期
	private String marketDateStr;//行情日期（APP使用）
	private Double returnValue;
	private Double returnRate;//回报率
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
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
	public Double getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Double returnValue) {
		this.returnValue = returnValue;
	}
	public Double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}
	
}
