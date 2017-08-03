package com.fsll.wmes.ifa.vo;

import java.util.Date;

/**
 * 显示观点数据，供用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceInsightVO {
	private String insightId;
	private String title;//标题
	private int views;//总访问数
	private String comments;//总评论数
	private Date createTime;//创建时间
	private int insightCount;//该IFA的总观点数
	public String getInsightId() {
		return insightId;
	}
	public void setInsightId(String insightId) {
		this.insightId = insightId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getInsightCount() {
		return insightCount;
	}
	public void setInsightCount(int insightCount) {
		this.insightCount = insightCount;
	}
	
	
}
