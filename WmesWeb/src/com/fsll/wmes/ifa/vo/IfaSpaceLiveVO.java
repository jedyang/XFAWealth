package com.fsll.wmes.ifa.vo;

import com.fsll.wmes.entity.LiveScheduler;

/**
 * 显示直播左侧列表数据，供用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceLiveVO {
	private String lideId;
	private String title;
	private String content;
	private String issuedTime;
	private LiveScheduler liveScheduler;
	
	public String getLideId() {
		return lideId;
	}
	public void setLideId(String lideId) {
		this.lideId = lideId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(String issuedTime) {
		this.issuedTime = issuedTime;
	}
	public LiveScheduler getLiveScheduler() {
		return liveScheduler;
	}
	public void setLiveScheduler(LiveScheduler liveScheduler) {
		this.liveScheduler = liveScheduler;
	}
	
	
	
}

