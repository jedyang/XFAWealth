package com.fsll.wmes.chat.socket.manager;

import com.fsll.wmes.chat.pojo.SocketUser;

/**
 * mjhuang
 */
public interface IUserManager {
	public boolean addUser(SocketUser user);
	public boolean removeUser(SocketUser user);
	public int getOnlineCount();
	public SocketUser getUser(String userId);
}
