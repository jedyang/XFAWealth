/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.List;

import com.fsll.core.vo.AccessoryFileVO;

/**
 * @author scshi
 *	基金附件文件及下载链接信息
 */
public class FundDocDataVO {
	private String docName;
	private String lastUdate;
	private List<AccessoryFileVO> fileList;
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
	public List<AccessoryFileVO> getFileList() {
		return fileList;
	}
	public void setFileList(List<AccessoryFileVO> fileList) {
		this.fileList = fileList;
	}
	
}
