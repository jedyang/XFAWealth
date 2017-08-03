package com.fsll.wmes.chat.pojo;
/**
 * 
 * Created by mjhuang
 * "username": "纸飞机"
 ,"id": "100000"
 ,"status": "online"
 ,"sign": "在深邃的编码世界,做一枚轻盈的纸飞机"
 ,"avatar": "http://cdn.firstlinkapp.com/upload/2016_6/1465575923433_33812.jpg"
 */
public class User {
    private String id;
    public String getFgid() {
        return fgid;
    }
    public void setFgid(String fgid) {
        this.fgid = fgid;
    }
    private String fgid;
    private String sign;
    private String avatar;
    private String username;
    private int newMsgCount;
    public User(){

    }
    public User(String id,String username,String sign,String avatar){
        this.id = id;
        this.username = username;
        this.sign = sign;
        this.avatar = avatar;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
	public int getNewMsgCount() {
		return newMsgCount;
	}
	public void setNewMsgCount(int newMsgCount) {
		this.newMsgCount = newMsgCount;
	}
    
}
