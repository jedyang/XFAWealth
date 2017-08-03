package com.fsll.wmes.ifa.vo;

import java.util.Date;

/**
 * 显示新闻数据，供用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceNewsVO {
	private String newsId;
	private String title;//标题
	private String ifaRecommendedReason;//ifa推荐时，填写的理由
	private String url;//访问地址
	private String thumbnailImagePath;//缩略图
	private Date createTime;//创建时间
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
	public String getIfaRecommendedReason() {
		return ifaRecommendedReason;
	}
	public void setIfaRecommendedReason(String ifaRecommendedReason) {
		this.ifaRecommendedReason = ifaRecommendedReason;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThumbnailImagePath() {
		return thumbnailImagePath;
	}
	public void setThumbnailImagePath(String thumbnailImagePath) {
		this.thumbnailImagePath = thumbnailImagePath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
