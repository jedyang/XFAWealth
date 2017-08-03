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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "layim_msg_history")
public class LayimMsgHistory {

	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_user")
	@JsonIgnore
	private MemberBase fromUser;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_user")
	@JsonIgnore
	private MemberBase toUser;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	private LayimGroup group;
	
	@Column(name = "msg")
	private String msg;
	
	@Column(name = "chat_type")
	private int chatType;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "msg_type")
	private int msgType;
	
	@Column(name = "timestamp")
	private long timestamp;
	
	@Column(name = "is_read")
	private String isRead;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getFromUser() {
		return fromUser;
	}

	public void setFromUser(MemberBase fromUser) {
		this.fromUser = fromUser;
	}

	public MemberBase getToUser() {
		return toUser;
	}

	public void setToUser(MemberBase toUser) {
		this.toUser = toUser;
	}

	public LayimGroup getGroup() {
		return group;
	}

	public void setGroup(LayimGroup group) {
		this.group = group;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
	
}
