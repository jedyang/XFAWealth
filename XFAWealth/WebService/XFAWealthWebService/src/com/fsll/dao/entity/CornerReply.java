package com.fsll.dao.entity;

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
@Table(name = "corner_reply")
public class CornerReply implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	@JsonIgnore
	private CornerInfo corner;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reply_user_id")
	@JsonIgnore
	private MemberBase replyUser;
    
    @Column(name = "replay_time")
	private Date replayTime;
    
    @Column(name = "emotion_icon")
	private String emotionIcon;
    
    @Column(name = "content")
	private String content;
    
	@Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CornerInfo getCorner() {
		return corner;
	}

	public void setCorner(CornerInfo corner) {
		this.corner = corner;
	}

	public MemberBase getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(MemberBase replyUser) {
		this.replyUser = replyUser;
	}

	public Date getReplayTime() {
		return replayTime;
	}

	public void setReplayTime(Date replayTime) {
		this.replayTime = replayTime;
	}

	public String getEmotionIcon() {
		return emotionIcon;
	}

	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	
	
}

