package com.fsll.wmes.member.vo;

import java.util.Date;
import java.util.List;

import com.fsll.core.entity.AccessoryFile;

public class DocListVO {
	private String id;
	private String templateId;
	private String docName;
	private String desc;
	private String isNecessary;
	private Integer updateCycle;
	private Date effectDate;//expireDate
	private Date createTime;//submitDate
	private String isValid;
	private String accessoryFileId;
	private String accessoryFileName;
	private Date accessoryFileCreateTime;
	private List<AccessoryFile> fileList;
	private String checkStatus;
	private String expireStatus;//是否过期，1=过期，0=未过期
	private int effectDateBetween;
	private String docListId;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIsNecessary() {
		return isNecessary;
	}
	public void setIsNecessary(String isNecessary) {
		this.isNecessary = isNecessary;
	}
	public Integer getUpdateCycle() {
		return updateCycle;
	}
	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}
	public Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getAccessoryFileName() {
		return accessoryFileName;
	}
	public void setAccessoryFileName(String accessoryFile) {
		this.accessoryFileName = accessoryFile;
	}
	public Date getAccessoryFileCreateTime() {
		return accessoryFileCreateTime;
	}
	public void setAccessoryFileCreateTime(Date accessoryFileCreateTime) {
		this.accessoryFileCreateTime = accessoryFileCreateTime;
	}
	public String getAccessoryFileId() {
		return accessoryFileId;
	}
	public void setAccessoryFileId(String accessoryFileId) {
		this.accessoryFileId = accessoryFileId;
	}
	public List<AccessoryFile> getFileList() {
		return fileList;
	}
	public void setFileList(List<AccessoryFile> fileList) {
		this.fileList = fileList;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getExpireStatus() {
		return expireStatus;
	}
	public void setExpireStatus(String expireStatus) {
		this.expireStatus = expireStatus;
	}
	public int getEffectDateBetween() {
		return effectDateBetween;
	}
	public void setEffectDateBetween(int effectDateBetween) {
		this.effectDateBetween = effectDateBetween;
	}
	public String getDocListId() {
		return docListId;
	}
	public void setDocListId(String docListId) {
		this.docListId = docListId;
	}
	
	
	
}
