package com.fsll.wmes.chat.socket.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fsll.wmes.chat.util.cache.LayIMCache;



/**
 * mjhuang
 */
public class OnLineUserManager {
	
    static final String CACHENAME = "LAYIM";
    static final String CACHEKEY = "ONLINE_USER";

    public void addUser(String userId){
        Map map = LayIMCache.getInstance().get(CACHENAME,CACHEKEY);
        if(map == null){
            map = new ConcurrentHashMap();
        }
        map.put(userId,"online");
        LayIMCache.getInstance().set(CACHENAME,CACHEKEY,map);
    }

    public void removeUser(String userId){
        Map map = LayIMCache.getInstance().get(CACHENAME,CACHEKEY);
        if (map == null){ return; }
        map.remove(userId);
        LayIMCache.getInstance().set(CACHENAME,CACHEKEY,map);
    }

    public Map getOnLineUsers(){
        Map map = LayIMCache.getInstance().get(CACHENAME,CACHEKEY);
        return map;
    }
    
}
