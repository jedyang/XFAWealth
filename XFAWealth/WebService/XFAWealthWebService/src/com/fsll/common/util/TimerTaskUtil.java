package com.fsll.common.util;

import java.util.TimerTask;
import javax.servlet.http.HttpSession;

/**
 * 定时清除指定session值
 * 
 */
public class TimerTaskUtil extends TimerTask{
	
	private HttpSession session;
	private String sessionKey;
	
	public void verifyCodeTimer(HttpSession session,String sessionKey){
		this.session = session;
		this.sessionKey = sessionKey;
    }
	
	@Override
	public void run() {
		session.removeAttribute(sessionKey);
	}

}
