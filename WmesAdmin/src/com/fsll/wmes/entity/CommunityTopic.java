package com.fsll.wmes.entity;

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
@Table(name = "community_topic")
public class CommunityTopic implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	private MemberBase creator;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "visitor")
	private String visitor;
	
	@Column(name = "is_insight")
	private Integer isInsight;
	
	@Column(name = "title")
	private String title;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "section_id")
	@JsonIgnore
	private CommunitySection section;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "memo")
	private String memo;
	
	@Column(name = "source_type")
	private String sourceType;
	
	@Column(name = "source_id")
	private String sourceId;

	@Column(name = "comment_count")
	private Integer commentCount;
	
	@Column(name = "read_count")
	private Integer readCount;
	
	@Column(name = "like_count")
	private Integer likeCount;
	
	@Column(name = "unlike_count")
	private Integer unlikeCount;
	
	@Column(name = "tansfer_count")
	private Integer tansferCount;
	
	@Column(name = "favorite_count")
	private Integer favoriteCount;
	
	@Column(name = "is_recommand")
	private Integer isRecommand;
	
	@Column(name = "recommand_reason")
	private Integer recommandReason;
	
	@Column(name = "support_comment")
	private Integer supportComment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}

	public Integer getIsInsight() {
		return isInsight;
	}

	public void setIsInsight(Integer isInsight) {
		this.isInsight = isInsight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
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

	public Integer getTansferCount() {
		return tansferCount;
	}

	public void setTansferCount(Integer tansferCount) {
		this.tansferCount = tansferCount;
	}

	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
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

	public Integer getIsRecommand() {
		return isRecommand;
	}

	public void setIsRecommand(Integer isRecommand) {
		this.isRecommand = isRecommand;
	}

	public Integer getRecommandReason() {
		return recommandReason;
	}

	public void setRecommandReason(Integer recommandReason) {
		this.recommandReason = recommandReason;
	}

	public CommunitySection getSection() {
		return section;
	}

	public void setSection(CommunitySection section) {
		this.section = section;
	}


}