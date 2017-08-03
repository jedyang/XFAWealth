/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.web.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebBusLog;
import com.fsll.wmes.entity.WebReadToDo;

/***
 * 业务接口类：业务日志管理
 * @author Yan
 * @date 2016-11-11
 */
public interface WebReadService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public WebReadToDo saveOrUpdate(WebReadToDo info);

	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public WebReadToDo findById(String id);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public List<Object> findTitleById(String id,String langCode);
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,WebReadToDo info,String nickName, String langCode);

}
