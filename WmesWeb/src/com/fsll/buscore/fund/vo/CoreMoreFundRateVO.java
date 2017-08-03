/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.vo;

import java.util.Date;

/**
 * 多个基金组合成的累计行情数据
 * @author 林文伟
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public class CoreMoreFundRateVO {
	private Date marketDate;
	private String marketDateStr;
	private Double returnRate;
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
	public Double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}
	

	
	
}
