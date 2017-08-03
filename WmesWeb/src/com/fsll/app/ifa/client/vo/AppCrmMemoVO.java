package com.fsll.app.ifa.client.vo;

import java.util.List;

import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.vo.AccessoryFileVO;

/**
 * IFA客户详情——销售记录列表实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppCrmMemoVO {
	private String id;//主键 ID
	private String memoText;//备忘记录
	private String createTime;//创建日期
	private String lastModify;//最后更新时间
	private List<AccessoryFileVO> fileList;//文件列表
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMemoText() {
		return memoText;
	}
	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public List<AccessoryFileVO> getFileList(){
		return fileList;
	}
	public void setFileList(List<AccessoryFileVO> fileList) {
		this.fileList = fileList;
	}	
}
