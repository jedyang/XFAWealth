/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.portfolio.vo;

import java.util.Date;

/**
 * 收益的值对象
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-13
 */
public class PortfolioProfitVO {
	private String portfolioId;
	private String marketDate;
	private String totalRate;//回报率,百分之几,保留两位小数
	private double totalPl;//自创建以后的总盈亏金额
	private double dayPl;//每日盈亏金额
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}
	public String getTotalRate() {
		return totalRate;
	}
	public void setTotalRate(String totalRate) {
		this.totalRate = totalRate;
	}
	public double getTotalPl() {
		return totalPl;
	}
	public void setTotalPl(double totalPl) {
		this.totalPl = totalPl;
	}
	public double getDayPl() {
		return dayPl;
	}
	public void setDayPl(double dayPl) {
		this.dayPl = dayPl;
	}
	
}
