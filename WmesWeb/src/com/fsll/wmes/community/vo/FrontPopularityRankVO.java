package com.fsll.wmes.community.vo;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsInfo;

/**
 * @author wwlin
 *	首页贴子回复排行前端显示实体
 */
public class FrontPopularityRankVO {
	
	//发布人头像
	private String userHeadUrl;
	//发布人ID
	private String memberId;
	//发布人类型
	private String memberType;
	//发布人昵称
	private String nickName;
	//被关注人数
	private String followersCount;
	//活跃度
	private String activity;
	//发贴子数
	private String topicCount;
	//所属版块
	private String sectionName;
	//评论数
	private String commentCount;
	//标题
	private String topicName;
	//topicid
	private String topicId;
	//浏览量
	private String readCount;
	
	public String getUserHeadUrl() {
		return userHeadUrl;
	}
	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(String followersCount) {
		this.followersCount = followersCount;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTopicCount() {
		return topicCount;
	}
	public void setTopicCount(String topicCount) {
		this.topicCount = topicCount;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getReadCount() {
		return readCount;
	}
	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	

	
	
	
	
	
}


