package com.fsll.dao.entity;

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
@Table(name = "member_msg_receiver")
public class MemberMsgReceiver implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "m_id")
	@JsonIgnore
	private MemberMsgSender msgSender;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_member_id")
	@JsonIgnore
	private MemberBase toMember;
	
	@Column(name = "is_readed")
	private String isReaded;
	
	@Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberMsgSender getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(MemberMsgSender msgSender) {
		this.msgSender = msgSender;
	}

	public MemberBase getToMember() {
		return toMember;
	}

	public void setToMember(MemberBase toMember) {
		this.toMember = toMember;
	}

	public String getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(String isReaded) {
		this.isReaded = isReaded;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}