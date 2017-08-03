package com.fsll.app.ifa.schedule.vo;

/**
 * 日程列表实体类VO
 * @author zxtan
 * @date 2017-03-23
 */
public class AppScheduleItemVO {
	private String id; //主键
	private String title;//标题
	private String groupId;//分组Id
	private String groupName;//分组名称
	private String createTime;//创建时间
	private String startTime;//开始时间
	private String endTime;//完成时间
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getGroupId(){
		return groupId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public String getGroupName(){
		return groupName;
	}
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getCreateTime(){
		return createTime;
	}
	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}
	public String getStartTime(){
		return startTime;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	public String getEndTime(){
		return endTime;
	}
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}
}
