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
@Table(name = "web_chat")
public class WebChat implements java.io.Serializable {
	private String id;
	private MemberBase send;
	private MemberBase receive;
	private String emotionIcon;
	private String content;
	private String isRead;
	private Date createTime;
	private String isValid;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "send_id")
	@JsonIgnore
	public MemberBase getSend() {
		return this.send;
	}

	public void setSend(MemberBase send) {
		this.send = send;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receive_id")
	@JsonIgnore
	public MemberBase getReceive() {
		return this.receive;
	}

	public void setReceive(MemberBase receive) {
		this.receive = receive;
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

	@Column(name = "is_read")
	public String getIsRead() {
		return this.isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}