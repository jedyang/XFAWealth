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
import com.fsll.core.entity.SysDesk;

@Entity
@Table(name = "ifa_space")
public class IfaSpace implements java.io.Serializable {
	private String id;
	private MemberIfa ifa;
	private String name;
	private SysDesk desk;
	private String iocnUrl;
	private String bgUrl;
	private Date createTime;
	private Date lastUpdate;
	
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
	@JoinColumn(name = "ifa_id")
	@JsonIgnore
	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desk_id")
	@JsonIgnore
	public SysDesk getDesk() {
		return this.desk;
	}

	public void setDesk(SysDesk desk) {
		this.desk = desk;
	}

	@Column(name = "iocn_url")
	public String getIocnUrl() {
		return this.iocnUrl;
	}

	public void setIocnUrl(String iocnUrl) {
		this.iocnUrl = iocnUrl;
	}

	@Column(name = "bg_url")
	public String getBgUrl() {
		return this.bgUrl;
	}

	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}