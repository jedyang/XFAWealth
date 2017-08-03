package com.fsll.wmes.rpq.vo;

import com.fsll.wmes.entity.RpqQuestItem;



public class RpqPageQuestItemView {
	private String id;
	private String questId;
	private String pageId;
	
	private String type;
	private String title;
	private String remark;
	private Integer scoreValue;
	private RpqQuestItem item;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getScoreValue() {
		return this.scoreValue;
	}

	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getQuestId() {
    	return questId;
    }

	public void setQuestId(String questId) {
    	this.questId = questId;
    }

	public String getPageId() {
    	return pageId;
    }

	public void setPageId(String pageId) {
    	this.pageId = pageId;
    }

	public RpqQuestItem getItem() {
    	return item;
    }

	public void setItem(RpqQuestItem item) {
    	this.item = item;
    }

	public String getRemark() {
    	return remark;
    }

	public void setRemark(String remark) {
    	this.remark = remark;
    }
}
