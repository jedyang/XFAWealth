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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "web_push_detail")
public class WebPushDetail implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "push_id")
	@JsonIgnore
	private WebPush push;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to_member_id")
	@JsonIgnore
	private MemberBase toMember;
	
	@JoinColumn(name = "type")
	private String type;
	
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

	public WebPush getPush() {
		return push;
	}

	public void setPush(WebPush push) {
		this.push = push;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}