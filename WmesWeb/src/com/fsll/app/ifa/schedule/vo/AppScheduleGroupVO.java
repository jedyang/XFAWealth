package com.fsll.app.ifa.schedule.vo;

/**
 * 日程分组实体类VO
 * @author zxtan
 * @date 2017-03-23
 */
public class AppScheduleGroupVO {
	private String groupId;//分组Id
	private String groupName;//分组名称
	private String color;//颜色
	private String icon;//图标
	
	
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
	public String getColor(){
		return color;
	}
	public void setColor(String color){
		this.color = color;
	}
	public String getIcon(){
		return icon;
	}
	public void setIcon(String icon){
		this.icon = icon;
	}
}
