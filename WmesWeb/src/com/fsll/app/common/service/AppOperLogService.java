package com.fsll.app.common.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.OperLog;
/***
 * 业务接口类：web操作日志管理
 * @author mjhuang
 * @date 2016-6-13
 */
public interface AppOperLogService {
	
	/**
	 * 增加一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  OperLog create(OperLog log);

	/**
	 * 修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  OperLog update(String id,Update update);
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public OperLog findById(String id);
	
	/**
	 * 查找记录
	 */
	public List<OperLog> findAll();
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,OperLog log);
	
}
