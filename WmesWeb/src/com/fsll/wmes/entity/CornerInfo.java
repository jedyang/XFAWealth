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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "corner_info")
public class CornerInfo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	@JsonIgnore
	private MemberBase author;
    
    @Column(name = "emotion_icon")
	private String emotionIcon;
    
    @Column(name = "reply_count")
	private Integer replyCount;
    
    @Column(name = "topic")
	private String topic;
    
    @Column(name = "content")
	private String content;
    
    @Column(name = "is_transfer")
	private String isTransfer;
    
    @Column(name = "icon_url")
	private String iconUrl;
    
    @Column(name = "url")
	private String url;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "last_reply_id")
	@JsonIgnore
	private MemberBase replyer;
	
	@Column(name = "last_replay_time")
	private Date lastReplayTime;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;

	@Column(name = "is_valid")
	private String isValid;
	
	@Transient
	private String createTimeFmt;
	
	@Transient
	private Integer ifaFollows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getAuthor() {
		return author;
	}

	public void setAuthor(MemberBase author) {
		this.author = author;
	}

	public String getEmotionIcon() {
		return emotionIcon;
	}

	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MemberBase getReplyer() {
		return replyer;
	}

	public void setReplyer(MemberBase replyer) {
		this.replyer = replyer;
	}

	public Date getLastReplayTime() {
		return lastReplayTime;
	}

	public void setLastReplayTime(Date lastReplayTime) {
		this.lastReplayTime = lastReplayTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Integer getIfaFollows() {
		return ifaFollows;
	}

	public void setIfaFollows(Integer ifaFollows) {
		this.ifaFollows = ifaFollows;
	}
	
	@Transient
	public String getCreateTimeFmt() {
		return createTimeFmt;
	}

	public void setCreateTimeFmt(String createTimeFmt) {
		this.createTimeFmt = createTimeFmt;
	}
	
	
}
