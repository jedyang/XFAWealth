/**
 * 
 */
package com.fsll.app.fund.vo;

import java.util.List;

import com.fsll.core.vo.AccessoryFileVO;

/**
 * @author scshi
 *	基金附件文件及下载链接信息
 */
public class AppFundDocDataVO {
	private String docName;
	private String lastUdate;
	private List<AppFundDocDetailVO> fileList;
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getLastUdate() {
		return lastUdate;
	}
	public void setLastUdate(String lastUdate) {
		this.lastUdate = lastUdate;
	}
	public List<AppFundDocDetailVO> getFileList() {
		return fileList;
	}
	public void setFileList(List<AppFundDocDetailVO> fileList) {
		this.fileList = fileList;
	}
	
}
