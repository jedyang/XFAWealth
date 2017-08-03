/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysLog;
/***
 * 业务接口类：日志管理
 * @author tan
 * @date 2016-06-17
 */
public interface SysLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  SysLog saveOrUpdate(SysLog log);

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
	public SysLog findById(String id);
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,SysLog log);

}
