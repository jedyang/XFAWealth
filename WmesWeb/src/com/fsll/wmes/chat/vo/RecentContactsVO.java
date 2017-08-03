package com.fsll.wmes.chat.vo;

public class RecentContactsVO {
	private String id;
	private String name;
	private String sign;
	private String status;
	private String type;
	private String username;
	private String avatar;
	private String fgid;
	private long historyTime;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFgid() {
		return fgid;
	}

	public void setFgid(String fgid) {
		this.fgid = fgid;
	}

	public long getHistoryTime() {
		return historyTime;
	}

	public void setHistoryTime(long historyTime) {
		this.historyTime = historyTime;
	}

}
