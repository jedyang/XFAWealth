package com.fsll.app.investor.discover.vo;

import java.util.List;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;


public class AppIfaPerformanceVO {
	private String aum;
	private List<AppPortfolioArenaMessVo> portfolioList;

 
	public String getAum() {
		return this.aum;
	}

	public void setAum(String aum) {
		this.aum = aum;
	}

	public List<AppPortfolioArenaMessVo> getPortfolioList() {
		return this.portfolioList;
	}

	public void setPortfolioList(List<AppPortfolioArenaMessVo> portfolioList) {
		this.portfolioList = portfolioList;
	}
}