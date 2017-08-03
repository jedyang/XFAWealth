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
import com.fsll.core.entity.SysChannel;

@Entity
@Table(name = "ifa_space_channel")
public class IfaSpaceChannel implements java.io.Serializable {
	private String id;
	private IfaSpace space;
	private SysChannel channel;
	private Integer orderBy;
	private String isDisplay;
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
	@JoinColumn(name = "space_id")
	@JsonIgnore
	public IfaSpace getSpace() {
		return space;
	}

	public void setSpace(IfaSpace space) {
		this.space = space;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "channel_id")
	@JsonIgnore
	public SysChannel getChannel() {
		return channel;
	}

	public void setChannel(SysChannel channel) {
		this.channel = channel;
	}

	@Column(name = "order_by")
	public Integer getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "is_display")
	public String getIsDisplay() {
		return this.isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}