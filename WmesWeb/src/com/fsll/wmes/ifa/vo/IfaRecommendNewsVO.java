package com.fsll.wmes.ifa.vo;

/**
 * 新闻推荐实体类
 * 
 * @author mqzou
 * @date 2016-09-30
 */
public class IfaRecommendNewsVO {

	private String id;//recommendId
	private String newsId;
	private String title;
	private String iconUrl;
	private String createTime;//新闻创建时间
	private String recommendTime;//推荐时间
	private String overhead;
	private String reason;
	private String views;
	private String ups;
	private String downs;
	private String visitCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRecommendTime() {
		return recommendTime;
	}
	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}
	public String getOverhead() {
		return overhead;
	}
	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getUps() {
		return ups;
	}
	public void setUps(String ups) {
		this.ups = ups;
	}
	public String getDowns() {
		return downs;
	}
	public void setDowns(String downs) {
		this.downs = downs;
	}
	public String getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(String visitCount) {
		this.visitCount = visitCount;
	}
	
	
}
