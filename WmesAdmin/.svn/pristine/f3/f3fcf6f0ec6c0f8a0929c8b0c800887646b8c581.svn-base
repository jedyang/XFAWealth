package com.fsll.wmes.web.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysLog;
import com.fsll.wmes.entity.WebOperLog;
/***
 * 业务接口类：web操作日志管理
 * @author mjhuang
 * @date 2016-6-13
 */
public interface WebOperLogService {
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  WebOperLog saveOrUpdate(WebOperLog log);

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
	public WebOperLog findById(String id);
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,WebOperLog log);
	
}
