package com.fsll.wmes.rpq.vo;

import java.util.List;

import com.fsll.wmes.entity.RpqQuest;

public class RpqPageQuestView {
	private String id;
	private String pageId;
	private String titleName;
	private String remark;
	private RpqQuest quest;
	private List<RpqPageQuestItemView> itemList;
	
	public String getTitleName() {
		return this.titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	public List<RpqPageQuestItemView> getItemList() {
		return this.itemList;
	}

	public void setItemList(List<RpqPageQuestItemView> item) {
		this.itemList = item;
	}

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getPageId() {
    	return pageId;
    }

	public void setPageId(String pageId) {
    	this.pageId = pageId;
    }

	public RpqQuest getQuest() {
    	return quest;
    }

	public void setQuest(RpqQuest quest) {
    	this.quest = quest;
    }

	public void setRemark(String remark) {
	    this.remark = remark;
    }

	public String getRemark() {
	    return remark;
    }
}
