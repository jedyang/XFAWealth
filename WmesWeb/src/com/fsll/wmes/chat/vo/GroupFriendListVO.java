package com.fsll.wmes.chat.vo;

import java.util.List;

import com.fsll.wmes.chat.pojo.User;

public class GroupFriendListVO {

	private String id;
	private String groupname;
	private int online;
	private List<User> list;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
		this.list = list;
	}

}
