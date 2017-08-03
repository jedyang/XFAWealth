package com.fsll.wmes.chat.util;

import com.fsll.wmes.chat.socket.manager.IUserManager;
import com.fsll.wmes.chat.socket.manager.UserManager;
import com.fsll.wmes.chat.util.serializer.FastJsonSerializer;
import com.fsll.wmes.chat.util.serializer.IJsonSerializer;



/**
 * mjhuang
 */
public class LayIMFactory {
    //创建序列化器
    public static IJsonSerializer createSerializer(){
        return new FastJsonSerializer();
    }
    //创建在线人员管理工具
    public static IUserManager createManager(){
        return UserManager.getInstance();
    }
}
