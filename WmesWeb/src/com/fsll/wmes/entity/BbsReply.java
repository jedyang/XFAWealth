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
@Table(name = "bbs_reply")
public class BbsReply implements java.io.Serializable {

	private String id;
	private BbsType type;
	private BbsTopic topic;
	private MemberBase replyUser;
	private Date replayTime;
	private String emotionIcon;
	private String content;
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
	@JoinColumn(name = "topic_id")
	@JsonIgnore
	public BbsTopic getTopic() {
		return topic;
	}

	public void setTopic(BbsTopic topic) {
		this.topic = topic;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reply_user_id")
	@JsonIgnore
	public MemberBase getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(MemberBase replyUser) {
		this.replyUser = replyUser;
	}

	@Column(name = "replay_time")
	public Date getReplayTime() {
		return this.replayTime;
	}

	public void setReplayTime(Date replayTime) {
		this.replayTime = replayTime;
	}

	@Column(name = "emotion_icon")
	public String getEmotionIcon() {
		return this.emotionIcon;
	}

	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}