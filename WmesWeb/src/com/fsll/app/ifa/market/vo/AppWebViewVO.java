package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 权限控制实体VO
 * @author zxtan
 *
 */
public class AppWebViewVO {

	private String id;//主键Id
	private String moduleType;//对应模块,insight观点,news新闻,strategy策略,portfolio_info组合,portfolio_arena组合,live_info直播
	private String relateId;//关联的记录,与类型一起关联数据记录
	private String scopeFlag;//范围标记,1:Only Me,2:Public,3:Let me specify
	private String clientFlag;//我的客户,1是,0否,-1部分
	private String prospectFlag;//我的潜在客户,1是,0否,-1部分
	private String buddyFlag;//我的合作客户,1是,0否,-1部分
	private String colleagueFlag;//我的同事,1是,0否,-1部分
	private String createTime;//创建时间
	private String lastUpdate;//最近更新时间
	
	private List<AppWebViewDetailVO> clientList;//我的客户
	private List<AppWebViewDetailVO> prospectList;//我的潜在客户
	private List<AppWebViewDetailVO> buddyList;//我的合作客户
	private List<AppWebViewDetailVO> colleagueList;//我的同事


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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	

	public List<AppWebViewDetailVO> getClientList() {
		return clientList;
	}

	public void setClientList(List<AppWebViewDetailVO> clientList) {
		this.clientList = clientList;
	}
	
	public List<AppWebViewDetailVO> getProspectList() {
		return prospectList;
	}

	public void setProspectList(List<AppWebViewDetailVO> prospectList) {
		this.prospectList = prospectList;
	}
	public List<AppWebViewDetailVO> getBuddyList() {
		return buddyList;
	}

	public void setBuddyList(List<AppWebViewDetailVO> buddyList) {
		this.buddyList = buddyList;
	}
	public List<AppWebViewDetailVO> getColleagueList() {
		return colleagueList;
	}

	public void setColleagueList(List<AppWebViewDetailVO> colleagueList) {
		this.colleagueList = colleagueList;
	}
}