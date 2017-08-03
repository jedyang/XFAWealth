package com.fsll.app.investor.discover.vo;

/**
 * 待办待阅VO
 * @author zpzhou
 * @date 2016-11-02
 */
public class AppTodoVo {
	private String id;
	private String title;
	private String type;
	private String moduleType;
	private String relateId;	
	private String fromMemberId;
	private String fromMemberName;	
	private String msgUrl;
	private String msgParam;
	private String appUrl;
	private String appParam;
	private String isApp;	
	private String isRead;
	private String readTime;
	private String createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getFromMemberId() {
		return fromMemberId;
	}
	public void setFromMemberId(String fromMemberId) {
		this.fromMemberId = fromMemberId;
	}
	public String getFromMemberName() {
		return fromMemberName;
	}
	public void setFromMemberName(String fromMemberName) {
		this.fromMemberName = fromMemberName;
	}
	public String getMsgUrl() {
		return msgUrl;
	}
	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}
	public String getMsgParam() {
		return msgParam;
	}
	public void setMsgParam(String msgParam) {
		this.msgParam = msgParam;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getAppParam() {
		return appParam;
	}
	public void setAppParam(String appParam) {
		this.appParam = appParam;
	}
	public String getIsApp() {
		return isApp;
	}
	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}
		
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getReadTime() {
		return readTime;
	}
	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
