package com.fsll.wmes.group.vo;

import com.fsll.wmes.entity.MemberGroup;

public class MemberGroupVO extends MemberGroup{

	private String statusDisplay;
	private String createTimeStr;
	private String creator; // 创建人姓名
	private Integer memberCount; // 含会员数量
	
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	
}
