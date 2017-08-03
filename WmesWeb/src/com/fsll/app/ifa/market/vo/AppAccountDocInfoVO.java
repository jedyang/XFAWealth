package com.fsll.app.ifa.market.vo;

import java.util.List;

import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.vo.AccessoryFileVO;

public class AppAccountDocInfoVO {
	private String docName;//风险等级
	private String checkDays;//下次评估时间
	private String isNecessary;//是否必要
	private String checkStatus;//最后的审核状态,0待审核,1审核通过,2审核退回
	private List<AccessoryFileVO> fileList;//文件列表
	
	public String getDocName(){
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getCheckDays(){
		return checkDays;
	}
	public void setCheckDays(String checkDays) {
		this.checkDays = checkDays;
	}
	public String getIsNecessary(){
		return isNecessary;
	}
	public void setIsNecessary(String isNecessary) {
		this.isNecessary = isNecessary;
	}	
	public String getCheckStatus(){
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public List<AccessoryFileVO> getFileList(){
		return fileList;
	}
	public void setFileList(List<AccessoryFileVO> fileList) {
		this.fileList = fileList;
	}	
	
}
