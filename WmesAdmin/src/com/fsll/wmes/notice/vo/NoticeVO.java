package com.fsll.wmes.notice.vo;

import com.fsll.wmes.entity.Notice;

public class NoticeVO extends Notice{
	
	private String filterTitle;
	private String filterContent;
	private String filterLevel;
	private String filterStartTime;
	private String filterEndTime;
	
	private String releaseDateStr;
	private String releaseName; // 发布者
	private String sourceName; // 发布机构
	
	public String getFilterTitle() {
		return filterTitle;
	}
	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	public String getFilterContent() {
		return filterContent;
	}
	public void setFilterContent(String filterContent) {
		this.filterContent = filterContent;
	}
	public String getFilterStartTime() {
		return filterStartTime;
	}
	public void setFilterStartTime(String filterStartTime) {
		this.filterStartTime = filterStartTime;
	}
	public String getFilterEndTime() {
		return filterEndTime;
	}
	public void setFilterEndTime(String filterEndTime) {
		this.filterEndTime = filterEndTime;
	}
	public String getReleaseDateStr() {
		return releaseDateStr;
	}
	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}
	public String getFilterLevel() {
		return filterLevel;
	}
	public void setFilterLevel(String filterLevel) {
		this.filterLevel = filterLevel;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	
	
}
