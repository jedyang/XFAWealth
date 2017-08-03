package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppMarketIndexVO {
	
	private Boolean ifWatchFund;//是否为自选基金
	private Boolean ifFollowPortfolio;//是否为自选组合
	private Boolean ifFollowStategy;//是否为自选策略
	private List<AppWatchTypeVO> watchTypeList;//分类基金列表
	private List<AppWatchFundItemVO> visitedFundList;//感兴趣基金列表
	private List<AppFollowArenaItemVO> portfolioList;//组合列表
	private List<AppStrategyInfoItemVO> strategyList;//策略列表
	private List<AppHotTopicItemVO> hotTopicList;//热门话题列表
	private List<AppNoticeItemVO> noticeList;//公告列表
	
	
	
	public List<AppWatchTypeVO> getWatchTypeList() {
		return watchTypeList;
	}
	public void setWatchTypeList(List<AppWatchTypeVO> watchTypeList) {
		this.watchTypeList = watchTypeList;
	}	
	public List<AppWatchFundItemVO> getVisitedFundList() {
		return visitedFundList;
	}
	public void setVisitedFundList(List<AppWatchFundItemVO> visitedFundList) {
		this.visitedFundList = visitedFundList;
	}	
	public List<AppFollowArenaItemVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<AppFollowArenaItemVO> portfolioList) {
		this.portfolioList = portfolioList;
	}	
	public List<AppStrategyInfoItemVO> getStrategyList() {
		return strategyList;
	}
	public void setStrategyList(List<AppStrategyInfoItemVO> strategyList) {
		this.strategyList = strategyList;
	}		
	public List<AppHotTopicItemVO> getHotTopicList() {
		return hotTopicList;
	}
	public void setHotTopicList(List<AppHotTopicItemVO> hotTopicList) {
		this.hotTopicList = hotTopicList;
	}		
	public List<AppNoticeItemVO> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(List<AppNoticeItemVO> noticeList) {
		this.noticeList = noticeList;
	}	
	public Boolean getIfWatchFund() {
		return ifWatchFund;
	}
	public void setIfWatchFund(Boolean ifWatchFund){
		this.ifWatchFund = ifWatchFund;
	}
	public Boolean getIfFollowPortfolio() {
		return ifFollowPortfolio;
	}
	public void setIfFollowPortfolio(Boolean ifFollowPortfolio){
		this.ifFollowPortfolio = ifFollowPortfolio;
	}
	public Boolean getIfFollowStategy() {
		return ifFollowStategy;
	}
	public void setIfFollowStategy(Boolean ifFollowStategy){
		this.ifFollowStategy = ifFollowStategy;
	}
}
