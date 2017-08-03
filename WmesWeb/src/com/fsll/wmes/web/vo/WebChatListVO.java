package com.fsll.wmes.web.vo;

import java.util.List;

public class WebChatListVO {

	private String type;
	private List<WebChatMemberVO> memberList;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<WebChatMemberVO> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<WebChatMemberVO> memberList) {
		this.memberList = memberList;
	}
	
	
	
	
}
