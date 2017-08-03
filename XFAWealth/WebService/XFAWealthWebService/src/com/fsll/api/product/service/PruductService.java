/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.product.service;

import net.sf.json.JSONObject;

import com.fsll.api.product.vo.ProductDetailVo;
import com.fsll.common.util.JsonPaging;

/**
 * 产品信息业务接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-19
 */
public interface PruductService {
	/**
	 * 按条件搜索列表
	 * @param jsonPaging 分页信息
	 * @param q 查询条件josn对象
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findAll(JsonPaging jsonPaging,JSONObject q);
	
	/**
	 * 增加或者修改一条数据
	 * @param q 参数对象
	 * @return 特定对象
	 */
	public  Object saveOrUpdate(JSONObject q);
	
	/**
	 * 修改数据的状态
	 * @param q 参数对象
	 * @return
	 */
	public  boolean saveStatus(JSONObject q);
	
	/**
	 * 通过ID删除记录
	 * @param q 参数对象
	 * @return
	 */
	public boolean delete(JSONObject q);
	
	/**
	 * 通过ID查找一条数据
	 * @param q 参数对象
	 * @return
	 */
	public ProductDetailVo findById(JSONObject q);
}
