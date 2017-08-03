package com.fsll.app.discover.vo;

/**
 * 帖子观点实体类VO
 * @author zxtan
 * @date 2017-05-17
 */
public class AppCommunityQuestionVO {
	private String questionId;//话题 Id
	private String questionTitle;//话题标题		
	private String memberId;//发布人ID	
	private String memberType;//发布人类型
	private String memberIconUrl;//发布人头像	
	private String memberName;//发布人昵称
	private String createTime;//创建时间
	private String commentCount;//评论数
	private String readCount;//阅读数
	private String likeCount;//点赞数
	private String unlikeCount;//踩的数
	private String isAnswer;//是否已回答，1是 0否
	private String answerCount;//回答次数
	private String isClose;//是否已结束问题，1是 0否
	private String suportComment;//是否支持评论，1是，0否
	private String behaviorType;//赞踩行为，like unlike
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
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
	public String getMemberIconUrl() {
		return memberIconUrl;
	}
	public void setMemberIconUrl(String memberIconUrl) {
		this.memberIconUrl = memberIconUrl;
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
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getReadCount() {
		return readCount;
	}
	public void setReadCount(String readCount) {
		this.readCount = readCount;
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
	public String getIsAnswer() {
		return isAnswer;
	}
	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}
	public String getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(String answerCount) {
		this.answerCount = answerCount;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	public String getSuportComment() {
		return suportComment;
	}
	public void setSuportComment(String suportComment) {
		this.suportComment = suportComment;
	}
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}		
}
