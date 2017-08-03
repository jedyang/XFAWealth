/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.base;

import java.net.URLDecoder;

import com.fsll.core.base.CoreBaseAct;

/**
 * 控制器基类：核对模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class WmesBaseAct extends CoreBaseAct{
    public String toUTF8String(String val){
    	try {
        	if (null!=val && !"".equals(val)){
        		val = URLDecoder.decode(val,"UTF-8");
        		//val = new String(val.getBytes("ISO-8859-1"),"UTF-8");
        		return val;
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
    }
}
