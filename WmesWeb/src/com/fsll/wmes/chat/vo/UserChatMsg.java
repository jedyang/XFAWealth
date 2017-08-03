package com.fsll.wmes.chat.vo;

public class UserChatMsg {
    private String userCode;
    private String name;
    private String content;
    
    public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
    public String toString() {
        return "UserChatMsg{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", userCode='" + userCode + '\'' +
                '}';
    }
}
