/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.web;

import net.sf.json.JSONObject;

import com.fsll.common.util.JsonPaging;
import com.fsll.dao.entity.WebInterfaceLog;
/***
 * Dao接口日志信息接口 
 * @author tan
 * @date 2016-08-04
 */
public interface WebInterfaceLogService {
	/**
	 * 增加或者修改一条数据
	 * @param obj
	 */
	public  WebInterfaceLog saveOrUpdate(WebInterfaceLog obj);
	
	/**
	 * 通过ID删除记录
	 * @param ids
	 * @return
	 */
	public boolean deleteById(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public WebInterfaceLog findById(String id);
	
	/**
	 * 按条件搜索列表
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findList(JsonPaging jsonPaging,JSONObject q);

}
