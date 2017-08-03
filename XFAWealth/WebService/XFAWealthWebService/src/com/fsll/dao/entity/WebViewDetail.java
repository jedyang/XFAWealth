package com.fsll.dao.entity;

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
@Table(name = "web_view_detail")
public class WebViewDetail implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "view_id")
	@JsonIgnore
	private WebView view;
	
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

	public WebView getView() {
		return view;
	}

	public void setView(WebView view) {
		this.view = view;
	}

	public MemberBase getToMember() {
		return toMember;
	}

	public void setToMember(MemberBase toMember) {
		this.toMember = toMember;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



}