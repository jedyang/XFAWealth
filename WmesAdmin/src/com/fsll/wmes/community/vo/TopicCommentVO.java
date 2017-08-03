package com.fsll.wmes.community.vo;

import com.fsll.wmes.entity.CommunityComment;

public class TopicCommentVO extends CommunityComment{

	private String targetId; // community_topic.id 或者 community_question.id ,根据 target 而定
	private String targetTitle; // community_topic.title 或者 community_question.title ,根据 target 而定
	private String memberName; // 评论人姓名
	private String createTimeStr; // 创建时间
	
	// 查询过滤部分
	private String filterTitle;
	private String filterComment;
	private String filterMemberName;
	private String filterStartTime;
	private String filterEndTime;
	private String filterStatus;
	
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetTitle() {
		return targetTitle;
	}
	public void setTargetTitle(String targetTitle) {
		this.targetTitle = targetTitle;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getFilterTitle() {
		return filterTitle;
	}
	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	public String getFilterComment() {
		return filterComment;
	}
	public void setFilterComment(String filterComment) {
		this.filterComment = filterComment;
	}
	public String getFilterMemberName() {
		return filterMemberName;
	}
	public void setFilterMemberName(String filterMemberName) {
		this.filterMemberName = filterMemberName;
	}
	public String getFilterStartTime() {
		return filterStartTime;
	}
	public void setFilterStartTime(String filterStartTime) {
		this.filterStartTime = filterStartTime;
	}
	public String getFilterEndTime() {
		return filterEndTime;
	}
	public void setFilterEndTime(String filterEndTime) {
		this.filterEndTime = filterEndTime;
	}
	public String getFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}
	
}
