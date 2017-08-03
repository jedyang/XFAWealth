package com.fsll.wmes.chat.socket.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fsll.wmes.chat.pojo.SocketUser;



/**
 * mjhuang
 */
public class UserManager implements IUserManager {
    private static Map<String,SocketUser> socketUserMap;
    private OnLineUserManager onLineUserManager;
    private UserManager(){
        socketUserMap = new ConcurrentHashMap<String,SocketUser>();
        onLineUserManager = new OnLineUserManager();
    }
    private static UserManager manager = new UserManager();
    public static IUserManager getInstance(){
        return manager;
    }
    public boolean addUser(SocketUser user) {
        String sessionUserId = user.getUserId();
        removeUser(sessionUserId);
        socketUserMap.put(sessionUserId, user);
        onLineUserManager.addUser(sessionUserId); //加入在线列表缓存
        return true;
    }
    public boolean removeUser(SocketUser user) {
        String sessionUserId =  user.getUserId();
        onLineUserManager.removeUser(sessionUserId);
        return removeUser(sessionUserId);
    }
    public int getOnlineCount() {
        return socketUserMap.size();
    }
    public SocketUser getUser(String userId){
        String key = userId;
        if(socketUserMap.containsKey(key)){
            return socketUserMap.get(key);
        }
        return new SocketUser();
    }
    private boolean removeUser(String sessionUserId) {
        socketUserMap.remove(sessionUserId);
        return true;
    }
}
