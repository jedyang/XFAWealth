package com.fsll.wmes.chat.pojo.message;

/**
 * Created by mjhuang
 */
public class ToDBMessage {
    public String getSendUserId() {
        return sendUserId;
    }
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }
    public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getChatType() {
        return chatType;
    }
    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
    public long getAddtime() {
        return addTime;
    }
    public void setAddtime(long addtime) {
        this.addTime = addtime;
    }
    public int getMsgType() {
        return msgType;
    }
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    private String sendUserId;
    private String toUserId;
    private String groupId;
    private String msg;
    private int chatType;
    private long addTime;
    private int msgType;
}

