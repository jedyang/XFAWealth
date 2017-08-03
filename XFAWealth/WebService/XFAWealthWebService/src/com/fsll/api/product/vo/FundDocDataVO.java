/**
 * 
 */
package com.fsll.api.product.vo;

import java.util.List;

/**
 * @author scshi
 *	基金附件文件及下载链接信息
 */
public class FundDocDataVO {
	private String docName;
	private String lastUdate;
	private List<FundDocDetailVO> fileList;
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
	public List<FundDocDetailVO> getFileList() {
		return fileList;
	}
	public void setFileList(List<FundDocDetailVO> fileList) {
		this.fileList = fileList;
	}
	
}
