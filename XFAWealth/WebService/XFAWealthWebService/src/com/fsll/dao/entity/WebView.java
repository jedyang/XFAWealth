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
@Table(name = "web_view")
public class WebView implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "module_type")
	private String moduleType;
    
    @Column(name = "relate_id")
	private String relateId;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_member_id")
	@JsonIgnore
	private MemberBase fromMember;
	
	@Column(name = "scope_flag")
	private String scopeFlag;
	
	@Column(name = "client_flag")
	private String clientFlag;
	
	@Column(name = "prospect_flag")
	private String prospectFlag;
	
	@Column(name = "buddy_flag")
	private String buddyFlag;
	
	@Column(name = "colleague_flag")
	private String colleagueFlag;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public MemberBase getFromMember() {
		return fromMember;
	}

	public void setFromMember(MemberBase fromMember) {
		this.fromMember = fromMember;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getScopeFlag() {
		return scopeFlag;
	}

	public void setScopeFlag(String scopeFlag) {
		this.scopeFlag = scopeFlag;
	}

	public String getClientFlag() {
		return clientFlag;
	}

	public void setClientFlag(String clientFlag) {
		this.clientFlag = clientFlag;
	}

	public String getProspectFlag() {
		return prospectFlag;
	}

	public void setProspectFlag(String prospectFlag) {
		this.prospectFlag = prospectFlag;
	}

	public String getBuddyFlag() {
		return buddyFlag;
	}

	public void setBuddyFlag(String buddyFlag) {
		this.buddyFlag = buddyFlag;
	}

	public String getColleagueFlag() {
		return colleagueFlag;
	}

	public void setColleagueFlag(String colleagueFlag) {
		this.colleagueFlag = colleagueFlag;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


}