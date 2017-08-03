package com.fsll.app.common.vo;

/**
 * 待办待阅VO
 * @author zxtan
 * @date 2017-05-02
 */
public class AppTodoVO {
	private String id;				//主键
	private String title;			//标题
	private String type;			//消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	private String moduleType;		//对应模块,如 insight关点,news新闻，openAccount开户
	private String relateId;		//生成的记录来源于哪个表的id
	private String fromMemberId;	//发起人Id
	private String fromMemberName;	//发起人姓名
	private String msgUrl;			//任务url,此字段供web使用
	private String msgParam;		//相关参数key=value格式,多个之间用&分隔
	private String appUrl;			//app中使用的url,要与app端对应
	private String appParam;		//app的参数格式key=value格式,多个之间用&分隔
	private String isApp;			//是否app使用的待办,1是,0否
	private String isRead;			//是否已阅,1已阅,0待阅
	private String readTime;		//待办待阅打开时间
	private String createTime;		//创建时间
	
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
