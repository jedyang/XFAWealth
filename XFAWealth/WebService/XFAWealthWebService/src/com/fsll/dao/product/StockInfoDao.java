/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.product;

import net.sf.json.JSONObject;

import com.fsll.common.util.JsonPaging;
import com.fsll.dao.entity.StockInfo;

/**
 * Dao股票信息接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-19
 */
public interface StockInfoDao {
	/**
	 * 增加或者修改一条数据
	 * @param obj
	 */
	public  StockInfo saveOrUpdate(StockInfo obj);
	
	/**
	 * 修改数据的状态
	 * @param ids
	 * @param status
	 */
	public  boolean saveStatus(String ids,String status);
	
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
	public StockInfo findById(String id);
	
	/**
	 * 按条件搜索列表
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findList(JsonPaging jsonPaging,JSONObject q);
	
	/**
	 * 通过产品ID查找一条数据
	 * @param pruductId
	 * @return
	 */
	public StockInfo findByPruductId(String pruductId);
}
