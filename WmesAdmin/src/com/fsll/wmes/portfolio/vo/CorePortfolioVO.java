package com.fsll.wmes.portfolio.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CorePortfolioVO {
	private String portfolioId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date marketDate;
	private Double returnValue;
	private Double returnRate;

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
