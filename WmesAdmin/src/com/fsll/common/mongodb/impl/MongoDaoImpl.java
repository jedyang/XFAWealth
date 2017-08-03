/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.mongodb.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.fsll.common.mongodb.MongoDao;
import com.fsll.common.util.JsonPaging;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

/**
 * Mongo  数据访问业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-1
 */
@Repository("mongoDao")
public class MongoDaoImpl implements MongoDao {
//	@Resource
	private MongoTemplate mongoTemplate;
	
    /** 
     * 获得mongoTemplate对象 
     * @return 
     */  
    public MongoTemplate getTemplate() {   
        return mongoTemplate;  
    }
    
    /** 
     * 保存一条记录 
     * @param object 
     * @return 
     */  
    public Object insert(Object object) {  
        mongoTemplate.insert(object);  
        return object;  
    }
    
    /** 
     * 保存一个对象到mongodb 
     * @param object 
     * @return 
     */  
    public Object save(Object object) {  
        mongoTemplate.save(object);  
        return object;  
    } 
	
	/**
	 * 删除记录
	 */
	public WriteResult remove(Object object) {
		return mongoTemplate.remove(object);
	}
	
    /** 
     * 通过条件查询更新数据 
     * @param query 
     * @param update 
     * @param object
     * @return 
     */  
    public WriteResult updateFirst(Query query,Update update,Object object) {  
    	return mongoTemplate.updateFirst(query,update,object.getClass());  
    }
	
    /** 
     * 通过条件查询更新数据 
     * @param query 
     * @param update 
     * @param object
     * @return 
     */  
    public WriteResult update(Query query,Update update,Object object) {  
    	return mongoTemplate.upsert(query,update,object.getClass());  
    } 
    
    /** 
     * 通过ID获取记录 
     * @param id 
     * @param object
     * @return 
     */  
    public Object get(String id,Object object) {  
        return mongoTemplate.findById(id,object.getClass());  
    }
    
	 /** 
     * 通过条件查询实体(集合) 
     * @param query 
     * @param object
     * @return 列表
     */  
    public List find(Query query,Object object) {  
        return mongoTemplate.find(query,object.getClass());  
    } 
    
    /** 
     * 通过一定的条件查询一个实体 
     * @param query 
     * @param object
     * @return 查找到的对象
     */  
    public Object findOne(Query query,Object object) {  
        return mongoTemplate.findOne(query,object.getClass());  
    }
    
    /** 
     * 通过条件查询,查询分页结果 
     * @param jsonPaging 
     * @param query 
     * @param object 
     * @return 
     */  
    public JsonPaging selectJsonPaging(JsonPaging jsonPaging,Query query,Object object) {  
        long totalCount = this.mongoTemplate.count(query,object.getClass());
        jsonPaging.setTotal(Integer.parseInt(Long.toString(totalCount)));
		int curPage = 1;
		if (null!=jsonPaging.getPage())
			curPage = jsonPaging.getPage();
		int pageSize = 0;
		if (null!=jsonPaging.getRows())
			pageSize = jsonPaging.getRows();
        query.skip((curPage - 1) * pageSize);// skip相当于从那条记录开始  
        query.limit(pageSize);//从skip开始,取多少条记录  
        List list = mongoTemplate.find(query,object.getClass());
        jsonPaging.setList(list);
        return jsonPaging; 
    } 
    
	/**
	 * 创建表。
	 */
	public DBCollection createCollection(String collectionName) {
		return mongoTemplate.createCollection(collectionName);
	}
	
	/**
	 * 删除表。
	 */
	public void dropCollection(String collectionName) {
		mongoTemplate.dropCollection(collectionName);
	}
	
}
