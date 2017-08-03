package com.fsll.core.entity;

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

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_server_log")
public class SysServerLog implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id", nullable = true)
	private SysServerInfo info;
	
	@Column(name = "moudle_name")
	private String moudleName;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysServerInfo getInfo() {
		return info;
	}

	public void setInfo(SysServerInfo info) {
		this.info = info;
	}

	public String getMoudleName() {
		return moudleName;
	}

	public void setMoudleName(String moudleName) {
		this.moudleName = moudleName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}