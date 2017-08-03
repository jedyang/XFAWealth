package com.fsll.app.investor.me.vo;

import java.util.List;

public class AppRpqExamQuestVO {
	
	private String questId;//问题Id
	private Integer orderBy;//排序
	private String questType;//题目类型
	private String title;//题目
	private String remark;//说明
	private List<AppRpqExamQuestItemVO> questItemList;//题目选项
	
	public String getQuestId() {
		return questId;
	}
	public void setQuestId(String questId) {
		this.questId = questId;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public String getQuestType() {
		return questType;
	}
	public void setQuestType(String questType) {
		this.questType = questType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<AppRpqExamQuestItemVO> getQuestItemList() {
		return questItemList;
	}
	public void setQuestItemList(List<AppRpqExamQuestItemVO> questItemList) {
		this.questItemList = questItemList;
	}
	
	
	
}
