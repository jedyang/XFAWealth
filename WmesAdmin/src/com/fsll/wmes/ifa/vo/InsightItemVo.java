package com.fsll.wmes.ifa.vo;

/**
 * 观点列表项VO
 * @author zpzhou
 * @date 2016-8-11
 */
public class InsightItemVo {
	private String id;
	private String regionType;
	private String sectionType;
	private String title;
	private Integer upCounter;
	private Integer downCounter;
	private String creatorName;
	private String createTime;
	private String lastUpdate;
	private Integer views;
	private Integer comments;
	private Integer ups;
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
	public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
	public Integer getUps() {
		return ups;
	}
	public void setUps(Integer ups) {
		this.ups = ups;
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
