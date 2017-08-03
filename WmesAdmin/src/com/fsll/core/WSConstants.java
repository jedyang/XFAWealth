/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core;
/**
 * wmes接口服务模块常量参数配置类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-6-12
 */
public class WSConstants {
    
	/**************** 服务错误日志  ************/
	public static final String SUCCESS = "1";//成功
	public static final String FAIL = "0";//失败
    
	/**************** 服务错误日志  ************/
	//前台
	public static final String CODE1000 = "1000";
	public static final String MSG1000 = "操作失败";
	
	public static final String CODE1001 = "1001";
	public static final String MSG1001 = "无权限";
	
	public static final String CODE1002 = "1002";
	public static final String MSG1002 = "参数不能为空";
	
	public static final String CODE1003 = "1003";
	public static final String MSG1003 = "参数错误";
	
	public static final String CODE1004 = "1004";
	public static final String MSG1004 = "帐号未激活";
	
	public static final String CODE1005 = "1005";
	public static final String MSG1005 = "用户名或者密码错误";

	public static final String CODE1006 = "1006";
	public static final String MSG1006 = "旧密码错误";

	public static final String CODE1007 = "1007";
	public static final String MSG1007 = "校验密码错误或已过期";

	public static final String CODE1008 = "1008";
	public static final String MSG1008 = "用户不存在";

	public static final String CODE1009 = "1009";
	public static final String MSG1009 = "没有用户验证记录";

	public static final String CODE1010 = "1010";
	public static final String MSG1010 = "唯一性数据已存在";

	//控制台
	public static final String CODE2001 = "2001";
	public static final String MSG2001 = "无数据";
	
	public static final String CODE2002 = "2002";
	public static final String MSG2002 = "数据不存在";
	
	public static final String CODE3001 = "3001";
	public static final String MSG3001 = "其它错误";
	
    /**
     * 构造方法
     */
    private WSConstants() {
    	
    }
    
}
