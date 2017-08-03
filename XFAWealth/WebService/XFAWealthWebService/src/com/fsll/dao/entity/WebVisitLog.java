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
@Table(name = "web_visit_log")
public class WebVisitLog implements java.io.Serializable {
 
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@Column(name = "module_type")
	private String moduleType;
	
	@Column(name = "relate_id")
	private String relateId;
	
	@Column(name = "bus_type")
	private String busType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberBase member;
	
	@Column(name = "visti_time")
	private Date vistiTime;
	
	@Column(name = "visti_ip")
	private String vistiIp;
	
	@Column(name = "browser")
	private String browser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public Date getVistiTime() {
		return vistiTime;
	}

	public void setVistiTime(Date vistiTime) {
		this.vistiTime = vistiTime;
	}

	public String getVistiIp() {
		return vistiIp;
	}

	public void setVistiIp(String vistiIp) {
		this.vistiIp = vistiIp;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	
}
