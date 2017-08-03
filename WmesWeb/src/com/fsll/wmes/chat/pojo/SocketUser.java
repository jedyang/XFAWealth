package com.fsll.wmes.chat.pojo;
import javax.websocket.Session;
/**
 * mjhuang
 */
public class SocketUser {
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    public String getUserId() {
        return userId;
    }
    public boolean isExist(){
    	if(!"0".equals(this.userId)){
    		return true;
    	}else{
    		return false;
    	}
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    private Session session;
    private String userId;

    @Override
    public String toString() {
        return session.getId()+"_"+userId;
    }
}
