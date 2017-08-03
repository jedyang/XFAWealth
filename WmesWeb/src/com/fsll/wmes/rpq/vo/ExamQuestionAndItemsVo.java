package com.fsll.wmes.rpq.vo;

import java.util.List;

public class ExamQuestionAndItemsVo {
	
	private String questId;
	private Integer questOrder;
	private String questTitle;
	private String questRemark;
	private List<RpqExamQuestionItemVo> rqpExamItemList;
	
	public String getQuestId() {
		return questId;
	}
	public void setQuestId(String questId) {
		this.questId = questId;
	}
	public Integer getQuestOrder() {
		return questOrder;
	}
	public void setQuestOrder(Integer questOrder) {
		this.questOrder = questOrder;
	}
	public String getQuestTitle() {
		return questTitle;
	}
	public void setQuestTitle(String questTitle) {
		this.questTitle = questTitle;
	}
	public String getQuestRemark() {
		return questRemark;
	}
	public void setQuestRemark(String questRemark) {
		this.questRemark = questRemark;
	}
	public List<RpqExamQuestionItemVo> getRqpExamItemList() {
		return rqpExamItemList;
	}
	public void setRqpExamItemList(List<RpqExamQuestionItemVo> rqpExamItemList) {
		this.rqpExamItemList = rqpExamItemList;
	}
	
	
	
}
