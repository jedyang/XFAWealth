package com.fsll.app.investor.me.vo;

/**
 * 观点VO
 * @author zpzhou
 * @date 2016-8-11
 */
public class AppInsightInfoVo {
	private String id;
	private String regionType;
	private String sectionType;
	private String title;
	private String content;
	private Integer upCounter;
	private Integer downCounter;
	private String creatorName;
	private String createTime;
	private String lastUpdate;
	private Integer views;
	private Integer viewsMonth;
	private Integer viewsWeek;
	private Integer viewsDay;
	private Integer comments;
	private Integer commentsMonth;
	private Integer commentsWeek;
	private Integer commentsDay;
	private Integer ups;
	private Integer upsMonth;
	private Integer upsWeek;
	private Integer upsDay;
	private Integer downs;
	private String followFlag;//是否关注  1是 0否
	private String investorFlag;//是否已访问  1是 0否
	private String recommendedFlag;//是否已推荐  1是 0否
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	public String getSectionType() {
		return sectionType;
	}
	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getUpCounter() {
		return upCounter;
	}
	public void setUpCounter(Integer upCounter) {
		this.upCounter = upCounter;
	}
	public Integer getDownCounter() {
		return downCounter;
	}
	public void setDownCounter(Integer downCounter) {
		this.downCounter = downCounter;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public Integer getViewsMonth() {
		return viewsMonth;
	}
	public void setViewsMonth(Integer viewsMonth) {
		this.viewsMonth = viewsMonth;
	}
	public Integer getViewsWeek() {
		return viewsWeek;
	}
	public void setViewsWeek(Integer viewsWeek) {
		this.viewsWeek = viewsWeek;
	}
	public Integer getViewsDay() {
		return viewsDay;
	}
	public void setViewsDay(Integer viewsDay) {
		this.viewsDay = viewsDay;
	}
	public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
	public Integer getCommentsMonth() {
		return commentsMonth;
	}
	public void setCommentsMonth(Integer commentsMonth) {
		this.commentsMonth = commentsMonth;
	}
	public Integer getCommentsWeek() {
		return commentsWeek;
	}
	public void setCommentsWeek(Integer commentsWeek) {
		this.commentsWeek = commentsWeek;
	}
	public Integer getCommentsDay() {
		return commentsDay;
	}
	public void setCommentsDay(Integer commentsDay) {
		this.commentsDay = commentsDay;
	}
	public Integer getUps() {
		return ups;
	}
	public void setUps(Integer ups) {
		this.ups = ups;
	}
	public Integer getUpsMonth() {
		return upsMonth;
	}
	public void setUpsMonth(Integer upsMonth) {
		this.upsMonth = upsMonth;
	}
	public Integer getUpsWeek() {
		return upsWeek;
	}
	public void setUpsWeek(Integer upsWeek) {
		this.upsWeek = upsWeek;
	}
	public Integer getUpsDay() {
		return upsDay;
	}
	public void setUpsDay(Integer upsDay) {
		this.upsDay = upsDay;
	}
	public Integer getDowns() {
		return downs;
	}
	public void setDowns(Integer downs) {
		this.downs = downs;
	}
	public String getFollowFlag() {
		return followFlag;
	}
	public void setFollowFlag(String followFlag) {
		this.followFlag = followFlag;
	}
	public String getInvestorFlag() {
		return investorFlag;
	}
	public void setInvestorFlag(String investorFlag) {
		this.investorFlag = investorFlag;
	}
	public String getRecommendedFlag() {
		return recommendedFlag;
	}
	public void setRecommendedFlag(String recommendedFlag) {
		this.recommendedFlag = recommendedFlag;
	}
}
