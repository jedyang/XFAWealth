package com.fsll.wmes.rpq.vo;

import java.util.List;

import com.fsll.wmes.entity.RpqPage;

public class RpqPageView {
	
	private String id;
	private String pageName;
	private String remark;
	private RpqPage paper;
	private List<RpqPageQuestView> questList;
	
	public String getPageName() {
		return this.pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public List<RpqPageQuestView> getQuestList() {
		return this.questList;
	}

	public void setQuestList(List<RpqPageQuestView> questList) {
		this.questList = questList;
	}

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getRemark() {
    	return remark;
    }

	public void setRemark(String remark) {
    	this.remark = remark;
    }

	public void setPaper(RpqPage paper) {
	    this.paper = paper;
    }

	public RpqPage getPaper() {
	    return paper;
    }
}
