package com.fsll.wmes.chat.pojo.message;

/**
 * Created by mjhuang
 */
public class ToServerTextMessage {
    public ToServerMessageMine getMine() {
        return mine;
    }
    public void setMine(ToServerMessageMine mine) {
        this.mine = mine;
    }
    public ToServerMessageTo getTo() {
        return to;
    }
    public void setTo(ToServerMessageTo to) {
        this.to = to;
    }
    private ToServerMessageMine mine;
    private ToServerMessageTo to;
}
