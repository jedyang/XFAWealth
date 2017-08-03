package com.fsll.wmes.ifa.vo;

import java.util.List;

import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.vo.AccessoryFileVO;

public class IfaCommunicationRecordVO {

	private String id;
	private String ifaId;
	private String memberId;
	private String clientName;
	private String iconUrl;
	private String memoText;
	private String imgText;
	private String date;
	private String time;
	private List<AccessoryFile> fileList;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getMemoText() {
		return memoText;
	}
	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}
	
	public String getImgText() {
		return imgText;
	}
	public void setImgText(String imgText) {
		this.imgText = imgText;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<AccessoryFile> getFileList() {
		return fileList;
	}
	public void setFileList(List<AccessoryFile> fileList) {
		this.fileList = fileList;
	}
	
	
	
	
}
