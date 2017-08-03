
package com.fsll.app.investor.market.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页VO
 * @author zpzhou
 * @date 2016-10-31
 */
public class AppIndexVo {
	private List<AppIndexHotVO> fundList=new ArrayList<AppIndexHotVO>();
	private List<AppIndexHotVO> portfolioList=new ArrayList<AppIndexHotVO>();
	private List<AppIndexNoMissVo> noMissList=new ArrayList<AppIndexNoMissVo>();
	private List<AppIndexTopCategoryVO> topList=new ArrayList<AppIndexTopCategoryVO>();
	private List<AppIndexBbsTopicVO> topicList=new ArrayList<AppIndexBbsTopicVO>();
	private List<AppNoticeItemVO> noticeList = new ArrayList<AppNoticeItemVO>();
	
	public List<AppIndexHotVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<AppIndexHotVO> fundList) {
		this.fundList = fundList;
	}
	public List<AppIndexHotVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<AppIndexHotVO> portfolioList) {
		this.portfolioList = portfolioList;
	}
	public List<AppIndexNoMissVo> getNoMissList() {
		return noMissList;
	}
	public void setNoMissList(List<AppIndexNoMissVo> noMissList) {
		this.noMissList = noMissList;
	}
	public List<AppIndexTopCategoryVO> getTopList() {
		return topList;
	}
	public void setTopList(List<AppIndexTopCategoryVO> topList) {
		this.topList = topList;
	}
	public List<AppIndexBbsTopicVO> getTopicList() {
		return topicList;
	}
	public void setTopicList(List<AppIndexBbsTopicVO> topicList) {
		this.topicList = topicList;
	}
	
	public List<AppNoticeItemVO> getNoticeList(){
		return noticeList;
	}
	public void setNoticeList(List<AppNoticeItemVO> noticeList){
		this.noticeList = noticeList;
	}
}
