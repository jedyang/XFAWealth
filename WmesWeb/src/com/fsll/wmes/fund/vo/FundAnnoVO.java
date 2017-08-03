package com.fsll.wmes.fund.vo;

import java.util.Date;

import org.aspectj.weaver.loadtime.IWeavingContext;

public class FundAnnoVO {

	private String id;
	private String annoName;
	private Date annoDate;
	private String annoDateFormat;
	private String annoContent;
	private String langCode;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnnoName() {
		return annoName;
	}
	public void setAnnoName(String annoName) {
		this.annoName = annoName;
	}
	public Date getAnnoDate() {
		return annoDate;
	}
	public void setAnnoDate(Date annoDate) {
		this.annoDate = annoDate;
	}
	public String getAnnoContent() {
		return annoContent;
	}
	public void setAnnoContent(String annoContent) {
		this.annoContent = annoContent;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getAnnoDateFormat() {
		return annoDateFormat;
	}
	public void setAnnoDateFormat(String annoDateFormat) {
		this.annoDateFormat = annoDateFormat;
	}
	 
	 
}
