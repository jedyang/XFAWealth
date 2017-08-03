package com.fsll.dao.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "community_question")
public class CommunityQuestion implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@Column(name = "title")
	private String title;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	private MemberBase creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "answerer_id")
	@JsonIgnore
	private MemberBase answerer;

	@Column(name = "comment_count")
	private Integer commentCount;
	
	@Column(name = "read_count")
	private Integer readCount;
	
	@Column(name = "like_count")
	private Integer likeCount;
	
	@Column(name = "unlike_count")
	private Integer unlikeCount;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "is_answer")
	private Integer isAnswer;
	
	@Column(name = "answer_count")
	private Integer answerCount;

	@Column(name = "memo")
	private String memo;
	
	@Column(name = "is_close")
	private Integer isClose;

	@Column(name = "support_comment")
	private Integer supportComment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public MemberBase getAnswerer() {
		return answerer;
	}

	public void setAnswerer(MemberBase answerer) {
		this.answerer = answerer;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getUnlikeCount() {
		return unlikeCount;
	}

	public void setUnlikeCount(Integer unlikeCount) {
		this.unlikeCount = unlikeCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(Integer isAnswer) {
		this.isAnswer = isAnswer;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getIsClose() {
		return isClose;
	}

	public void setIsClose(Integer isClose) {
		this.isClose = isClose;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getSupportComment() {
		return supportComment;
	}

	public void setSupportComment(Integer supportComment) {
		this.supportComment = supportComment;
	}
}