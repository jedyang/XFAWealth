package com.fsll.app.common.vo;

import java.util.List;

/**
 * 待办待阅VO
 * @author zxtan
 * @date 2017-05-02
 */
public class AppLatestTodoVO {
	private String dealCount;//交易消息数量
	private String noteCount;//一般性通知数量
	private String alertCount;//提醒通知数量
	private List<AppTodoVO> todoList;//最新消息实体列表	
	
	public String getDealCount() {
		return dealCount;
	}
	public void setDealCount(String dealCount) {
		this.dealCount = dealCount;
	}
	public String getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(String noteCount) {
		this.noteCount = noteCount;
	}
	public String getAlertCount() {
		return alertCount;
	}
	public void setAlertCount(String alertCount) {
		this.alertCount = alertCount;
	}
	public List<AppTodoVO> getTodoList() {
		return todoList;
	}
	public void setTodoList(List<AppTodoVO> todoList) {
		this.todoList = todoList;
	}	
}
