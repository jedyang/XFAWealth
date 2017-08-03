package com.fsll.wmes.crm.vo;

public class ScheduleFilterVO {
	private String groupId;
	private String eventType;
	private String customerId;
	private String clientType;
	private String sort;
	private String order;
	private String ifImportant;
	private String langCode;
	private String teamIfa;
	private String unclassified; // 1：只获取未分组的数据
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getIfImportant() {
		return ifImportant;
	}
	public void setIfImportant(String ifImportant) {
		this.ifImportant = ifImportant;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getTeamIfa() {
		return teamIfa;
	}
	public void setTeamIfa(String teamIfa) {
		this.teamIfa = teamIfa;
	}
	public String getUnclassified() {
		return unclassified;
	}
	public void setUnclassified(String unclassified) {
		this.unclassified = unclassified;
	}
	
	
}
