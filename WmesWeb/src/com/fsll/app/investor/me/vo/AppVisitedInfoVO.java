package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 我的-感兴趣的实体类
 * 
 * @author zxtan
 * @date 2016-11-29
 */
public class AppVisitedInfoVO {

	private List<AppPortfolioInfoVO> portfolioList;
	private AppInsightItemVo insightInfo;
	
	public List<AppPortfolioInfoVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<AppPortfolioInfoVO> portfolioList) {
		this.portfolioList = portfolioList;
	}
	
	public AppInsightItemVo getInsightInfo() {
		return insightInfo;
	}
	public void setInsightInfo(AppInsightItemVo insightInfo) {
		this.insightInfo = insightInfo;
	}
}
