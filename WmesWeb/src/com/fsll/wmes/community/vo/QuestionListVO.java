package com.fsll.wmes.community.vo;

import java.util.Date;
import java.util.List;

public class QuestionListVO {

	private String id;
	private String content;
	private String memberId;
	private String memberName;
	private String memberUrl;
	private String memberGender;
	private String memberType;
	private String dateTime;
	private int commentCount;
	private int likeCount;
	private int unlikeCount;
	private int isLike;
	private int isUnlike;
	private int isAnswer;
	private String answererId;
	private String answererName;
	private String answererUrl;
	private String answererGender;
	private String answerMemberType;
	private Date createDate;
	private String createDateStr;
	private List<QuestionAnswerListVO> answerList;

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

	public List<QuestionAnswerListVO> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<QuestionAnswerListVO> answerList) {
		this.answerList = answerList;
	}

	public int getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(int isAnswer) {
		this.isAnswer = isAnswer;
	}

	public String getAnswererId() {
		return answererId;
	}

	public void setAnswererId(String answererId) {
		this.answererId = answererId;
	}

	public String getAnswererName() {
		return answererName;
	}

	public void setAnswererName(String answererName) {
		this.answererName = answererName;
	}

	public String getAnswererUrl() {
		return answererUrl;
	}

	public void setAnswererUrl(String answererUrl) {
		this.answererUrl = answererUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}

	public String getAnswererGender() {
		return answererGender;
	}

	public void setAnswererGender(String answererGender) {
		this.answererGender = answererGender;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getAnswerMemberType() {
		return answerMemberType;
	}

	public void setAnswerMemberType(String answerMemberType) {
		this.answerMemberType = answerMemberType;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

}
