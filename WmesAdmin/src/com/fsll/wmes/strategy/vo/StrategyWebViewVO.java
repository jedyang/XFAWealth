package com.fsll.wmes.strategy.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebViewDetail;

public class StrategyWebViewVO {
	private String id;
	private String moduleType;
	private String relateId;
	private MemberBase fromMember;
	private String scopeFlag;
	private String clientFlag;
	private String prospectFlag;
	private String buddyFlag;
	private String colleagueFlag;
	private Date createTime;
	private Date lastUpdate;
	private List<WebViewDetail> details;
	
	private String clients;
	private String buddies;
	private String prospects;
	private String colleagues;
	
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
	public MemberBase getFromMember() {
		return fromMember;
	}
	public void setFromMember(MemberBase fromMember) {
		this.fromMember = fromMember;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public List<WebViewDetail> getDetails() {
		return details;
	}
	public void setDetails(List<WebViewDetail> details) {
		this.details = details;
	}
	public String getClients() {
		return clients;
	}
	public void setClients(String clients) {
		this.clients = clients;
	}
	public String getBuddies() {
		return buddies;
	}
	public void setBuddies(String buddies) {
		this.buddies = buddies;
	}
	public String getProspects() {
		return prospects;
	}
	public void setProspects(String prospects) {
		this.prospects = prospects;
	}
	public String getColleagues() {
		return colleagues;
	}
	public void setColleagues(String colleagues) {
		this.colleagues = colleagues;
	}
	
}
