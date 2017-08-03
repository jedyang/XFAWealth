package com.fsll.app.discover.vo;

import java.util.List;

/**
 * 评论VO
 * @author zxtan
 * @date 2017-05-18
 */
public class AppCommunityCommentVO {
	private String id;//评论Id
	private String target;//topic 帖子 question 问题
	private String targetId;//帖子或问题Id
	private String memberId;//评论人Id
	private String memberIconUrl;//评论人头像
	private String memberType;//评论人类型	
	private String memberName;//评论人名称
	private String createTime;//评论时间
	private String replyId; //被回复人
	private String replyMemberName;//被回复人名称
	private String commentType;//操作类型：comment评论/回复；answer 回答（是只有question时才有）
	private String likeCount;//点赞数量
	private String unlikeCount;//踩的数量
	private String commentCount;//针对评论的回复数
	private String content;//评论内容
	private String behaviorType;//当前人行为，赞或踩
	private List<AppCommunityCommentVO> replyList;//针对评论回复列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberIconUrl() {
		return memberIconUrl;
	}
	public void setMemberIconUrl(String memberIconUrl) {
		this.memberIconUrl = memberIconUrl;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getReplyMemberName() {
		return replyMemberName;
	}
	public void setReplyMemberName(String replyMemberName) {
		this.replyMemberName = replyMemberName;
	}
	public String getCommentType() {
		return commentType;
	}
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getUnlikeCount() {
		return unlikeCount;
	}
	public void setUnlikeCount(String unlikeCount) {
		this.unlikeCount = unlikeCount;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}
	public List<AppCommunityCommentVO> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<AppCommunityCommentVO> replyList) {
		this.replyList = replyList;
	}
	
	
}


