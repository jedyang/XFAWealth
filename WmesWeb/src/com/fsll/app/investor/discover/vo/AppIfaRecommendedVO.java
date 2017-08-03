package com.fsll.app.investor.discover.vo;

import java.util.List;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;


public class AppIfaRecommendedVO {
	private List<AppPortfolioArenaMessVo> portfolioList;
	private List<AppStrategyInfoVO> strategyList;
	private List<AppFundInfoVO> fundList;
	private List<AppInsightInfoVO> insightList;
	private List<AppNewsItemVo> newsList;



	public List<AppPortfolioArenaMessVo> getPortfolioList() {
		return this.portfolioList;
	}

	public void setPortfolioList(List<AppPortfolioArenaMessVo> portfolioList) {
		this.portfolioList = portfolioList;
	}
	
	public List<AppStrategyInfoVO> getStrategyList() {
		return this.strategyList;
	}

	public void setStrategyList(List<AppStrategyInfoVO> strategyList) {
		this.strategyList = strategyList;
	}
	

	public List<AppFundInfoVO> getFundList() {
		return this.fundList;
	}

	public void setFundList(List<AppFundInfoVO> fundList) {
		this.fundList = fundList;
	}
	

	public List<AppInsightInfoVO> getInsightList() {
		return this.insightList;
	}

	public void setInsightList(List<AppInsightInfoVO> insightList) {
		this.insightList = insightList;
	}
	

	public List<AppNewsItemVo> getNewsList() {
		return this.newsList;
	}

	public void setNewsList(List<AppNewsItemVo> newsList) {
		this.newsList = newsList;
	}
}