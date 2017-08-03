package com.fsll.wmes.chat.pojo.result;

/**
 * Created by mjhuang
 */
public class ToClientMessageResult {
    public ToClientMessageType getType() {
        return type;
    }
    public void setType(ToClientMessageType type) {
        this.type = type;
    }
    public Object getMsg() {
        return msg;
    }
    public void setMsg(Object msg) {
        this.msg = msg;
    }
    private ToClientMessageType type;
    private Object msg;
}
