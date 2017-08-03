/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fsll.common.util.JsonPaging;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

/**
 * MongoDao 数据访问接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-1
 */
public interface MongoDao {
    /** 
     * 获得mongoTemplate对象 
     * @return 
     */  
    public MongoTemplate getTemplate();
    
    /** 
     * 保存一条记录 
     * @param object 
     * @return 
     */  
    public Object insert(Object object);
    
    /** 
     * 保存一个对象到mongodb 
     * @param object 
     * @return 
     */  
    public Object save(Object object);
    
    
	/**
	 * 删除记录
	 */
	public WriteResult remove(Object object);
	
    /** 
     * 通过条件查询更新数据 
     * @param query 
     * @param update 
     * @param object
     * @return 
     */  
    public WriteResult updateFirst(Query query,Update update,Object object);
	
    /** 
     * 通过条件查询更新数据 
     * @param query 
     * @param update 
     * @param object
     * @return 
     */  
    public WriteResult update(Query query,Update update,Object object);
    
    /** 
     * 通过ID获取记录 
     * @param id 
     * @param object
     * @return 
     */  
    public Object get(String id,Object object);
    
	 /** 
     * 通过条件查询实体(集合) 
     * @param query 
     * @param object
     * @return 列表
     */  
    public List find(Query query,Object object);
    
    /** 
     * 通过一定的条件查询一个实体 
     * @param query 
     * @param object
     * @return 查找到的对象
     */  
    public Object findOne(Query query,Object object);
    
    /** 
     * 通过条件查询,查询分页结果 
     * @param jsonPaging 
     * @param query 
     * @param object 
     * @return 
     */  
    public JsonPaging selectJsonPaging(JsonPaging jsonPaging,Query query,Object object);
    
	/**
	 * 创建表。
	 */
	public DBCollection createCollection(String collectionName);
	
	/**
	 * 删除表。
	 */
	public void dropCollection(String collectionName);
    
}
