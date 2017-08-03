package com.fsll.wmes.community.vo;

public class QuestionAnswerListVO {

	private String id;
	private String content;
	private String dateTime;
	private int commentCount;
	private int likeCount;
	private int unlikeCount;
	private int isLike;
	private int isUnlike;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
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

}
