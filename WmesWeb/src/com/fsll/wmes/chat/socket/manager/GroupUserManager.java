package com.fsll.wmes.chat.socket.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fsll.wmes.chat.dao.LayIMDao;
import com.fsll.wmes.chat.service.ChatService;
import com.fsll.wmes.chat.util.cache.LayIMCache;



/**
 * mjhuang
 */
@Component
public class GroupUserManager {
	@Autowired
	private ChatService chatService;
	
    private static final String CACHENAME = "LAYIM_GROUP";
    private static final String CACHEKEY = "GM_";
    //每个组存一个
    private String getCacheKey(String groupId){
        return CACHEKEY + groupId;
    }
    //将某个组的用户id存入缓存  key=》list
    public boolean saveGroupMemeberIds(String groupId, List<String> userIds) {
        String key = getCacheKey(groupId);
        LayIMCache.getInstance().setListCache(CACHENAME,key,userIds);
        return true;
    }
    public List<String> getGroupMembers(String groupId){
        String key = getCacheKey(groupId);
        List<String> list = LayIMCache.getInstance().getListCache(CACHENAME,key);
        if (list == null || list.isEmpty()) {
            //缓存中没有数据,需要从数据库读取
            List<String> memebers = chatService.getMemberListOnlyIds(groupId);
            saveGroupMemeberIds(groupId, memebers);
            return memebers;
        }
        //直接从缓存中读取出来
        return list;
    }
}
