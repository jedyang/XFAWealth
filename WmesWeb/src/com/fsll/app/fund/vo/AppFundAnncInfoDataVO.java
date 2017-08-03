/**
 * 
 */
package com.fsll.app.fund.vo;

import java.util.List;

/**
 * @author scshi
 *基金公告的详细信息
 */
public class AppFundAnncInfoDataVO {
	private String anncId;
	private String anncTitle;
	private String anncDate;
	private String anncContent;
	private List fileList;
	
	public String getAnncTitle() {
		return anncTitle;
	}
	public void setAnncTitle(String anncTitle) {
		this.anncTitle = anncTitle;
	}
	public String getAnncDate() {
		return anncDate;
	}
	public void setAnncDate(String anncDate) {
		this.anncDate = anncDate;
	}
	public String getAnncContent() {
		return anncContent;
	}
	public void setAnncContent(String anncContent) {
		this.anncContent = anncContent;
	}
	public List getFileList() {
		return fileList;
	}
	public void setFileList(List fileList) {
		this.fileList = fileList;
	}
	public String getAnncId() {
		return anncId;
	}
	public void setAnncId(String anncId) {
		this.anncId = anncId;
	}
	
}
