/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.web.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysLog;
import com.fsll.wmes.entity.WebInterfaceLog;
/***
 * 业务接口类：日志管理
 * @author tan
 * @date 2016-08-04
 */
public interface WebInterfaceLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  WebInterfaceLog saveOrUpdate(WebInterfaceLog log);

	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public WebInterfaceLog findById(String id);
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,WebInterfaceLog log);

}
