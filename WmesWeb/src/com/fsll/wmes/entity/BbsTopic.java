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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bbs_topic")
public class BbsTopic implements java.io.Serializable {
	private String id;
	private BbsType type;
	private MemberBase author;
	private String emotionIcon;
	private Long replyCount;
	private String isRecommend;
	private String isTop;
	private Long orderBy;
	private String topic;
	private String content;
	private String isTransfer;
	private String iconUrl;
	private String url;
	private MemberBase lastReply;
	private Date lastReplayTime;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	@Column(name = "emotion_icon")
	public String getEmotionIcon() {
		return this.emotionIcon;
	}

	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}

	@Column(name = "reply_count")
	public Long getReplyCount() {
		return this.replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}

	@Column(name = "is_recommend")
	public String getIsRecommend() {
		return this.isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	@Column(name = "is_top")
	public String getIsTop() {
		return this.isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	@Column(name = "order_by")
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "topic")
	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "is_transfer")
	public String getIsTransfer() {
		return this.isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	@Column(name = "icon_url")
	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	@Column(name = "last_replay_time")
	public Date getLastReplayTime() {
		return this.lastReplayTime;
	}

	public void setLastReplayTime(Date lastReplayTime) {
		this.lastReplayTime = lastReplayTime;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id")
	@JsonIgnore
	public BbsType getType() {
		return type;
	}

	public void setType(BbsType type) {
		this.type = type;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public MemberBase getAuthor() {
		return author;
	}

	public void setAuthor(MemberBase author) {
		this.author = author;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "last_reply_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public MemberBase getLastReply() {
		return lastReply;
	}

	public void setLastReply(MemberBase lastReply) {
		this.lastReply = lastReply;
	}

}