package com.fsll.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sys_desk_channel")
public class SysDeskChannel implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private SysDeskChannel parent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desk_id")
	private SysDesk desk;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "channel_id")
	private SysChannel channel;
	
	@Column(name = "order_by")
	private Short orderBy;
	
	@Column(name = "serial_no")
	private String serialNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysDeskChannel getParent() {
		return parent;
	}

	public void setParent(SysDeskChannel parent) {
		this.parent = parent;
	}

	public SysDesk getDesk() {
		return desk;
	}

	public void setDesk(SysDesk desk) {
		this.desk = desk;
	}

	public SysChannel getChannel() {
		return channel;
	}

	public void setChannel(SysChannel channel) {
		this.channel = channel;
	}

	public Short getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}