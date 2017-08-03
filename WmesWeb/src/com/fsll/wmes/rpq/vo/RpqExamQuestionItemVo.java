/**
 * 
 */
package com.fsll.wmes.rpq.vo;

import com.fsll.wmes.entity.RpqExamQuest;

/**
 * @author scshi
 *
 */
public class RpqExamQuestionItemVo {
	private String id;
	private RpqExamQuest quest;
	private String type;
	private String title;
	private Integer scoreValue;
	private Integer orderBy;
	private String remark;
	private String checkFlag;//1--已选，0--未选
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public RpqExamQuest getQuest() {
		return quest;
	}
	public void setQuest(RpqExamQuest quest) {
		this.quest = quest;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	
	
}
