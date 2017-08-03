/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.product;

import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.common.util.JsonPaging;

/**
 * Dao复合或者报表查询接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-23
 */
public interface ProductReportDao {
	/**
	 * 按条件多表查询-有分页且有总记录数
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findXXX(JsonPaging jsonPaging,JSONObject q);
	
	/**
	 * 按条件多表查询-有分页无总记录数
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging queryXXX(JsonPaging jsonPaging,JSONObject q);
	
	/**
	 * 按条件多表查询-返回列表
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public List getXXX(JSONObject q);
	
}
