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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "web_email_log")
public class WebEmailLog implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_member_id")
	@JsonIgnore
	private MemberBase toMember;
	
	@Column(name = "to_email_addr")
	private String toEmailAddr;
	
	@Column(name = "to_email_title")
	private String toEmailTitle;
	
	@Column(name = "to_email_content")
	private String toEmailContent;
	
	@Column(name = "send_flag")
	private String sendFlag;
	
	@Column(name = "sended_time")
	private Date sendedTime;
	
	@Column(name = "send_count")
	private Integer sendCount;
	
	@Column(name = "module_type")
	private String moduleType;
	
	@Column(name = "relate_id")
	private String relateId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	private MemberBase creator;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getToMember() {
		return toMember;
	}

	public void setToMember(MemberBase toMember) {
		this.toMember = toMember;
	}

	public String getToEmailTitle() {
		return toEmailTitle;
	}

	public void setToEmailTitle(String toEmailTitle) {
		this.toEmailTitle = toEmailTitle;
	}

	public String getToEmailContent() {
		return toEmailContent;
	}

	public void setToEmailContent(String toEmailContent) {
		this.toEmailContent = toEmailContent;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Date getSendedTime() {
		return sendedTime;
	}

	public void setSendedTime(Date sendedTime) {
		this.sendedTime = sendedTime;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getToEmailAddr() {
		return toEmailAddr;
	}

	public void setToEmailAddr(String toEmailAddr) {
		this.toEmailAddr = toEmailAddr;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	
}