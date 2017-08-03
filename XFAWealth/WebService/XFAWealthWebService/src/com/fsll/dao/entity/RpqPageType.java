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
@Table(name = "rpq_page_type")
public class RpqPageType implements java.io.Serializable {
	
	private String id;
	private RpqPage rpqPage;
	private MemberDistributor distributor;
	private MemberIfafirm ifafirm;
	private String clientType;
	private String pageType;
	private String isDef;

	private String remark;
	private Date createTime;
	private Date lastUpdate;
	private String orderBy;
	
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "page_id")
	@JsonIgnore
	public RpqPage getRpqPage() {
		return rpqPage;
	}
	public void setRpqPage(RpqPage rpqPage) {
		this.rpqPage = rpqPage;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	public MemberDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}
	
	@Column(name = "client_type")
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	@Column(name = "page_type")
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	@Column(name = "is_def")
	public String getIsDef() {
		return isDef;
	}
	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@Column(name = "order_by")
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	@JsonIgnore
	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

}
