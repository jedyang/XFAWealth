package com.fsll.wmes.community.vo;

import java.util.List;

public class CommunityCommentVO {

	private String id;
	private String topicId;
	private String content;
	private String memberId;
	private String memberName;
	private String memberUrl;
	private String memberType;
	private String replyId;
	private String replyMemberName;
	private int likeCount;
	private int unlikeCount;
	private int commentCount;
	private int isLike;
	private int isUnlike;
	private String dateTime;
	private List<CommunityCommentVO> childList;
	private int status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberUrl() {
		return memberUrl;
	}

	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}

	public String getReplyId() {
		return replyId;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
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

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getUnlikeCount() {
		return unlikeCount;
	}

	public void setUnlikeCount(int unlikeCount) {
		this.unlikeCount = unlikeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getIsLike() {
		return isLike;
	}

	public void setIsLike(int isLike) {
		this.isLike = isLike;
	}

	public int getIsUnlike() {
		return isUnlike;
	}

	public void setIsUnlike(int isUnlike) {
		this.isUnlike = isUnlike;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public List<CommunityCommentVO> getChildList() {
		return childList;
	}

	public void setChildList(List<CommunityCommentVO> childList) {
		this.childList = childList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
