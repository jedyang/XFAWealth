package com.fsll.wmes.notice.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.entity.Notice;

public class NoticeVO extends Notice{

	private String releaseByName;
	private String releaseDateStr;
	private String createTimeStr;
	private String sourceName; // 发布机构
	private String targetDisplay;
	private String levelDisplay; 
	
	private String isNew;
	
	public String getReleaseByName() {
		return releaseByName;
	}
	public void setReleaseByName(String releaseByName) {
		this.releaseByName = releaseByName;
	}
	public String getReleaseDateStr() {
		return releaseDateStr;
	}
	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getTargetDisplay() {
		return targetDisplay;
	}
	public void setTargetDisplay(String targetDisplay) {
		this.targetDisplay = targetDisplay;
	}
	public String getLevelDisplay() {
		return levelDisplay;
	}
	public void setLevelDisplay(String levelDisplay) {
		this.levelDisplay = levelDisplay;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	
	
}
