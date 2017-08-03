/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.fsll.common.util.PropertyUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

/**
 * Mongo 操作工具类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-7-21
 */
public class MongoUtilForWrite {
	private static final String MONGOHOST = PropertyUtils.getConfDbPropertyValue("wmongo.host");
	private static final int MONGOPORT = Integer.parseInt(PropertyUtils.getConfDbPropertyValue("wmongo.port"));
	private static final String MONGODB = PropertyUtils.getConfDbPropertyValue("wmongo.db");
	private static final String MONGOUSER = PropertyUtils.getConfDbPropertyValue("wmongo.user");
	private static final String MONGOPWD = PropertyUtils.getConfDbPropertyValue("wmongo.pwd");
    private static final Integer SOTIMEOUT = Integer.parseInt(PropertyUtils.getConfDbPropertyValue("mongo.soTimeOut"));
    private static final Integer CONNECTIONSPERHOST = Integer.parseInt(PropertyUtils.getConfDbPropertyValue("mongo.connectionsPerHost"));
    private static final Integer THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER = Integer.parseInt(PropertyUtils.getConfDbPropertyValue("mongo.threadsAllowedToBlockForConnectionMultiplier"));
    
    private static MongoUtilForWrite MONGOUTIL = null;
    private static MongoClient MONGOCLIENT = null;
    private static MongoDatabase MONGODATABASE = null;
    
	public static MongoClient getClient() {
		return MONGOCLIENT;
	}
    
	public static MongoDatabase getDB() {
		return MONGODATABASE;
	}
    
	private MongoUtilForWrite() {
		
	}

	public static MongoUtilForWrite getInstance() {
		if (MONGOUTIL == null) {
			MONGOUTIL = new MongoUtilForWrite();
		}
		return MONGOUTIL;
	}

	public void init() {
		MONGOUTIL.closeMongoClient();
		MONGOUTIL.getMongoClient();
		MONGOUTIL.getMongoDataBase();
	}

	private void getMongoClient() {
		try {
			//用户名 数据库 密码
			MongoCredential credential = MongoCredential.createCredential(MONGOUSER,MONGODB,MONGOPWD.toCharArray());
        	MongoClientOptions.Builder build = new MongoClientOptions.Builder();
            /*
             * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
             * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
             * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
             */
            build.maxWaitTime(1000 * 60 * 2);
            build.connectTimeout(1000 * 60 * 1);//与数据库建立连接的timeout设置为1分钟
            build.socketTimeout(SOTIMEOUT);// 套接字超时时间，0无限制
            build.connectionsPerHost(CONNECTIONSPERHOST);//连接池设置为500个连接,默认为100
            build.threadsAllowedToBlockForConnectionMultiplier(THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER);//线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
            build.writeConcern(WriteConcern.ACKNOWLEDGED);
            build.socketKeepAlive(true);
            build.cursorFinalizerEnabled(false);
			
            MongoClientOptions myOptions = build.build();
            MONGOCLIENT = new MongoClient(new ServerAddress(MONGOHOST,MONGOPORT),Arrays.asList(credential),myOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMongoDataBase() {
		try {
			if (MONGOCLIENT != null) {
				MONGODATABASE = MONGOCLIENT.getDatabase(MONGODB);// 得到数据库
			} else {
				throw new RuntimeException("MongoClient is empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeMongoClient() {
		if (MONGODATABASE != null) {
			MONGODATABASE = null;
		}
		if (MONGOCLIENT != null) {
			MONGOCLIENT.close();
		}
	}
    
	/**
	 * 将实体类obj的字段信息和内容动态放到mapParams里面
	 * 
	 * @param mapParams
	 * @param obj
	 * @param method
	 */
	public void dymParms(Object mapParams, Object obj, String method) {
		try {
			if (obj != null) {
				Field[] fields = obj.getClass().getDeclaredFields();
				Class<?>[] arrClazz = new Class[2];
				arrClazz[0] = String.class;
				arrClazz[1] = Object.class;
				Method m = mapParams.getClass().getDeclaredMethod(method,arrClazz);
				m.setAccessible(true);
				if (fields != null) {
					for (Field f : fields) {
						f.setAccessible(true);
						Object value = f.get(obj);
						if (null!=value) {
							Class<?> clazz = value.getClass();
							Object[] strs = new Object[2];
							if (clazz == String.class) {
								if ( !"".equals(String.valueOf(value))) {
									strs[0] = f.getName();
									strs[1] = value;
									m.invoke(mapParams, strs);
								}
							} else {
								strs[0] = f.getName();
								strs[1] = value;
								m.invoke(mapParams, strs);
							}
						}
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
