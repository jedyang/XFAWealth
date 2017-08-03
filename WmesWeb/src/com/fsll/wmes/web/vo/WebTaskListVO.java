package com.fsll.wmes.web.vo;

import com.fsll.wmes.entity.WebTaskList;

public class WebTaskListVO extends WebTaskList{
	
	private String title;
	private String titleSc;
	private String titleTc;
	private String titleEn;
	private String fullUrl;
	private String color;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleSc() {
		return titleSc;
	}
	public void setTitleSc(String titleSc) {
		this.titleSc = titleSc;
	}
	public String getTitleTc() {
		return titleTc;
	}
	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}
	public String getTitleEn() {
		return titleEn;
	}
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
