package com.fsll.app.ifa.schedule.vo;

/**
 * 日程详情实体类VO
 * @author zxtan
 * @date 2017-03-23
 */
public class AppScheduleVO {
	private String id; //日程id 
	private String customerId; //客户Id
	private String nickName; //客户名称
	private String title; //标题
	private String content; //日程内容
	private String startTime; //开始时间，格式：yyyy-MM-dd HH:mm:ss
	private String endTime; //结束时间，格式：yyyy-MM-dd HH:mm:ss
//	private String color;
//	private String remind;
	private String repeatDay; //如果是定期任务，该值不为空。值为7位1,0组成字符串，分别对应周日至周六，1则表示有值，0表示无
//	private String itemName; 
//	private String remark;
	private String repeatFrom; //用于重复任务，开始重复的时间，格式：yyyy-MM-dd
	private String repeatTo;  //用于重复任务，结束重复的时间，格式：yyyy-MM-dd
	private String eventType; //0：Normal events,1 : Repeat events
	private String remindSetting;//提醒设置:空不提醒。xM代表分钟，xH代表小时，xD代表天，xW代表周，其中x为整数数字
	private String groupId;//分组id
	private String groupName;//分组名称
//	private String creatorId;//creator_id
//	private String creatorName;//creator_id
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
	private String isValid;//数据状态，1有效 0无效
	private String ifImportant;//是否重要日程,1是,0否

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

//	public String getItemName() {
//		return this.itemName;
//	}
//
//	public void setItemName(String itemName) {
//		this.itemName = itemName;
//	}
//
//	public String getRemark() {
//		return this.remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
//	public String getColor() {
//		return color;
//	}
//
//	public void setColor(String color) {
//		this.color = color;
//	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


//	public String getRemind() {
//		return remind;
//	}
//
//	public void setRemind(String remind) {
//		this.remind = remind;
//	}


	public String getRepeatDay() {
		return repeatDay;
	}

	public void setRepeatDay(String repeatDay) {
		this.repeatDay = repeatDay;
	}
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	

	public String getRepeatFrom() {
		return repeatFrom;
	}

	public void setRepeatFrom(String repeatFrom) {
		this.repeatFrom = repeatFrom;
	}


	public String getRepeatTo() {
		return repeatTo;
	}

	public void setRepeatTo(String repeatTo) {
		this.repeatTo = repeatTo;
	}


	public String getRemindSetting() {
		return remindSetting;
	}

	public void setRemindSetting(String remindSetting) {
		this.remindSetting = remindSetting;
	}


	public String getIfImportant() {
		return ifImportant;
	}

	public void setIfImportant(String ifImportant) {
		this.ifImportant = ifImportant;
	}
	
}