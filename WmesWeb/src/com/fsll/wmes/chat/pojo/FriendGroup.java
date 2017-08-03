package com.fsll.wmes.chat.pojo;

import java.util.List;

/**
 * Created by mjhuang
 */
public class FriendGroup extends Group {	
    public int getOnline() {
        return online;
    }
    public void setOnline(int online) {
        this.online = online;
    }
    public List<StatusUser> getList() {
        return list;
    }
    public void setList(List<StatusUser> list) {
        this.list = list;
    }
    private int online;
    private List<StatusUser> list;
}
