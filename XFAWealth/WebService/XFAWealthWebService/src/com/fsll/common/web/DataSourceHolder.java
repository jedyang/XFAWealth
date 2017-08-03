/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.web;

/**
 * 数据源操作
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-8-26
 */
public class DataSourceHolder {
	
	//线程本地环境
	private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static final String DB_TYPE_W = "wDataSource";
	public static final String DB_TYPE_R = "rDataSource";
	
	// 设置数据源
    public static void setDataSource(String dbKey) {
    	clearDataSource();
    	contextHolder.set(dbKey);
    }

	// 获取数据源
    public static String getDataSource() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_TYPE_W;//默认是写库
        }
        return db;
    }

	// 清除数据源
	public static void clearDataSource() {
		contextHolder.remove();
	}
}
