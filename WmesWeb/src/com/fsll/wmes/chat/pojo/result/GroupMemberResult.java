package com.fsll.wmes.chat.pojo.result;

import java.util.List;

import com.fsll.wmes.chat.pojo.User;



/**
 * Created by mjhuang
 */
public class GroupMemberResult {
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public List<User> getList() {
        return list;
    }
    public void setList(List<User> list) {
        this.list = list;
    }
    public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	private User owner;
    private int members;
    private List<User> list;
}
