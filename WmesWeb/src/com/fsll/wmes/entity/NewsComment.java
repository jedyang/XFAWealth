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
@Table(name = "news_comment")
public class NewsComment implements java.io.Serializable {
	private String id;
	private NewsInfo newsInfo;
	private MemberBase member;
	private String comment;
	private Integer good;
	private Integer bad;
	private Integer totalPost;
	private Date opTime;
	private NewsComment replyComment;
	private MemberBase replyMember;
	private String ip;
	private String FType;
	private Short face;
	private String checked;
	private String status;

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
	@JoinColumn(name = "info_id")
	@JsonIgnore
	public NewsInfo getNewsInfo() {
		return newsInfo;
	}

	public void setNewsInfo(NewsInfo newsInfo) {
		this.newsInfo = newsInfo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	

	@Column(name = "comment")
	public String getComment() {
		return this.comment;
	}
	
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "total_post")
	public Integer getTotalPost() {
		return totalPost;
	}

	public void setTotalPost(Integer totalPost) {
		this.totalPost = totalPost;
	}
	
	@Column(name = "good")
	public Integer getGood() {
		return this.good;
	}

	public void setGood(Integer good) {
		this.good = good;
	}

	@Column(name = "bad")
	public Integer getBad() {
		return this.bad;
	}

	public void setBad(Integer bad) {
		this.bad = bad;
	}

	@Column(name = "op_time")
	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reply_comment_id")
	@JsonIgnore
	public NewsComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(NewsComment replyComment) {
		this.replyComment = replyComment;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reply_member_id")
	@JsonIgnore
	public MemberBase getReplyMember() {
		return replyMember;
	}

	public void setReplyMember(MemberBase replyMember) {
		this.replyMember = replyMember;
	}
	

	@Column(name = "ip")
	public String getIp() {
		return this.ip;
	}

	

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "f_type")
	public String getFType() {
		return this.FType;
	}

	public void setFType(String FType) {
		this.FType = FType;
	}

	@Column(name = "face")
	public Short getFace() {
		return this.face;
	}

	public void setFace(Short face) {
		this.face = face;
	}
	
	@Column(name = "checked")
	public String getChecked() {
		return this.checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}