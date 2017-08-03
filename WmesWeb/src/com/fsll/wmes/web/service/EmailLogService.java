package com.fsll.wmes.web.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.entity.WebReadToDo;

public interface EmailLogService {
	/***
	 * 邮件管理列表查询的方法
	 * @author mqzou
	 * @date 2016-06-23
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, WebEmailLog emailLog );
	
	/**
	 * 通过ID查找一条数据
	 * @author mqzou
	 * @date 2016-06-23
	 * @param id
	 * @return
	 */
	public WebEmailLog findById(String id);
	
	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-11-14
	 * @param webEmailLog 对象
	 * @return
	 */
	public WebEmailLog saveOrUpdate(WebEmailLog webEmailLog);
	
}
