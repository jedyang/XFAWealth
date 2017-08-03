package com.fsll.wmes.chat.pojo;

/**
 * Created by mjhuang
 */
public enum JsonResultType {
    TYPESUCCESS("成功",0), TYPEFAILED("失败",1),TYPEEXCEPTION("异常" ,2);
    JsonResultType(String s, int i) {
    	
    }
}
