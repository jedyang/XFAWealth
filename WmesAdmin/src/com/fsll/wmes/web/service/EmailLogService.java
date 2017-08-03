package com.fsll.wmes.web.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.web.vo.EmailLogVO;

public interface EmailLogService {
	/***
	 * 邮件管理列表查询的方法
	 * @author wwluo
	 * @date 2016-06-23
	 * @param jsonpaging
	 * @param emailLog 查询过滤信息
	 * @param langCode 
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, EmailLogVO emailLog, String langCode);
	
	
	/**
	 * 通过ID查找一条数据
	 * @author mqzou
	 * @date 2016-06-23
	 * @param id
	 * @return
	 */
	public WebEmailLog findById(String id);
}
