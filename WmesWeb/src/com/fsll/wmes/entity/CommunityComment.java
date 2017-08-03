package com.fsll.wmes.entity;

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
@Table(name = "community_comment")
public class CommunityComment implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@Column(name = "target")
	private String target;
	
	@Column(name = "target_id")
	private String targetId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	private MemberBase creator;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "comment_type")
	private String commentType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_comment_id")
	@JsonIgnore
	private CommunityComment parent;
	
	@Column(name = "like_count")
	private Integer likeCount;
	
	@Column(name = "unlike_count")
	private Integer unlikeCount;
	
	@Column(name="comment_count")
	private Integer commentCount;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "memo")
	private String memo;

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

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}


	public CommunityComment getParent() {
		return parent;
	}

	public void setParent(CommunityComment parent) {
		this.parent = parent;
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

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	

}