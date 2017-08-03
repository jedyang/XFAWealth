package com.fsll.wmes.investor.vo;

import java.util.ArrayList;
import java.util.List;

import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.notice.vo.NoticeVO;

public class InvestorHomeVO {
	// My IFA
	private List<IfaVO> ifas = new ArrayList<IfaVO>();
	// My Buddy
	private List<MemberVO> buddys = new ArrayList<MemberVO>();
	
	// Top Performance Funds
	private	List<InvestorHomeTopPerformanceFundsVO> topPerformanceFunds = new ArrayList<InvestorHomeTopPerformanceFundsVO>();
	// Top Performance Portfolio
	private List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolios = new ArrayList<InvestorHomeTopPerformancePortfolioVO>();
	// Most Selected Funds
	private List<InvestorHomeMostSelectedFundsVO> mostSelectedFunds = new ArrayList<InvestorHomeMostSelectedFundsVO>();
	
	// My Favourites Portfolios
	private MyFavoritesPortfolios myFavoritesPortfolios;
	private Integer myFavoritesPortfolioCount;
	// My Favourites Strategies
	private MyFavoritesStrategy myFavoritesStrategies;
	private Integer myFavoritesStrategeCount;
	// My Favourites Watchlists
	private MyFavoritesWatchingTypeVOList myFavoritesWatchLists;
	private Integer myFavoritesWatchListCount;
	// My Favourites News
	private NewsInfoVO news;
	// My Favourites Topic
	private TopicDetailVO topic;
	
	// Recommended Portfolios
	private List<PortfolioArena> arenas = new ArrayList<PortfolioArena>();
	// Recommended Strategies
	private List<NewsInfoVO> strategies = new ArrayList<NewsInfoVO>();
	// Recommended Funds
	private List<FundInfoDataVO> fundInfos = new ArrayList<FundInfoDataVO>();
	// Recommended News
	private List<CommunityNewsListVO> communityNews = new ArrayList<CommunityNewsListVO>();
	// Recommended Insights
	// Recommended Topics
	// Notice
	private List<NoticeVO> notices = new ArrayList<NoticeVO>();
	
	// Common 
	private String currencyCode;
	private String currencyName;
	private String displayColor;
	
	public List<IfaVO> getIfas() {
		return ifas;
	}
	public void setIfas(List<IfaVO> ifas) {
		this.ifas = ifas;
	}
	public List<MemberVO> getBuddys() {
		return buddys;
	}
	public void setBuddys(List<MemberVO> buddys) {
		this.buddys = buddys;
	}
	public List<InvestorHomeTopPerformanceFundsVO> getTopPerformanceFunds() {
		return topPerformanceFunds;
	}
	public void setTopPerformanceFunds(
			List<InvestorHomeTopPerformanceFundsVO> topPerformanceFunds) {
		this.topPerformanceFunds = topPerformanceFunds;
	}
	public List<InvestorHomeTopPerformancePortfolioVO> getTopPerformancePortfolios() {
		return topPerformancePortfolios;
	}
	public void setTopPerformancePortfolios(
			List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolios) {
		this.topPerformancePortfolios = topPerformancePortfolios;
	}
	public List<InvestorHomeMostSelectedFundsVO> getMostSelectedFunds() {
		return mostSelectedFunds;
	}
	public void setMostSelectedFunds(
			List<InvestorHomeMostSelectedFundsVO> mostSelectedFunds) {
		this.mostSelectedFunds = mostSelectedFunds;
	}
	public MyFavoritesPortfolios getMyFavoritesPortfolios() {
		return myFavoritesPortfolios;
	}
	public void setMyFavoritesPortfolios(MyFavoritesPortfolios myFavoritesPortfolios) {
		this.myFavoritesPortfolios = myFavoritesPortfolios;
	}
	public MyFavoritesStrategy getMyFavoritesStrategies() {
		return myFavoritesStrategies;
	}
	public void setMyFavoritesStrategies(MyFavoritesStrategy myFavoritesStrategies) {
		this.myFavoritesStrategies = myFavoritesStrategies;
	}
	public MyFavoritesWatchingTypeVOList getMyFavoritesWatchLists() {
		return myFavoritesWatchLists;
	}
	public void setMyFavoritesWatchLists(
			MyFavoritesWatchingTypeVOList myFavoritesWatchLists) {
		this.myFavoritesWatchLists = myFavoritesWatchLists;
	}
	public NewsInfoVO getNews() {
		return news;
	}
	public void setNews(NewsInfoVO news) {
		this.news = news;
	}
	public TopicDetailVO getTopic() {
		return topic;
	}
	public void setTopic(TopicDetailVO topic) {
		this.topic = topic;
	}
	public List<PortfolioArena> getArenas() {
		return arenas;
	}
	public void setArenas(List<PortfolioArena> arenas) {
		this.arenas = arenas;
	}
	public List<NewsInfoVO> getStrategies() {
		return strategies;
	}
	public void setStrategies(List<NewsInfoVO> strategies) {
		this.strategies = strategies;
	}
	public List<FundInfoDataVO> getFundInfos() {
		return fundInfos;
	}
	public void setFundInfos(List<FundInfoDataVO> fundInfos) {
		this.fundInfos = fundInfos;
	}
	public List<CommunityNewsListVO> getCommunityNews() {
		return communityNews;
	}
	public void setCommunityNews(List<CommunityNewsListVO> communityNews) {
		this.communityNews = communityNews;
	}
	public List<NoticeVO> getNotices() {
		return notices;
	}
	public void setNotices(List<NoticeVO> notices) {
		this.notices = notices;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getDisplayColor() {
		return displayColor;
	}
	public void setDisplayColor(String displayColor) {
		this.displayColor = displayColor;
	}
	public Integer getMyFavoritesPortfolioCount() {
		return myFavoritesPortfolioCount;
	}
	public void setMyFavoritesPortfolioCount(Integer myFavoritesPortfolioCount) {
		this.myFavoritesPortfolioCount = myFavoritesPortfolioCount;
	}
	public Integer getMyFavoritesStrategeCount() {
		return myFavoritesStrategeCount;
	}
	public void setMyFavoritesStrategeCount(Integer myFavoritesStrategeCount) {
		this.myFavoritesStrategeCount = myFavoritesStrategeCount;
	}
	public Integer getMyFavoritesWatchListCount() {
		return myFavoritesWatchListCount;
	}
	public void setMyFavoritesWatchListCount(Integer myFavoritesWatchListCount) {
		this.myFavoritesWatchListCount = myFavoritesWatchListCount;
	}
	
	
}
