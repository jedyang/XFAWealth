/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.persistence;

import java.io.Serializable;
import java.util.List;

import com.fsll.common.util.JsonPaging;

/**
 * 
 * DAO操作接口。
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public interface BaseDao {
	
	/**
	 * 创建持久化对象。
	 * @param object  临时状态（transient）的实体对象。
	 * @return 创建后的持久化实例。
	 */
	public Object create(Object object);
	
	/**
	 * 修改持久化对象。
	 * @param object 准备修改的实体对象。
	 * @return 修改后的实体对象。
	 */
	public Object update(Object object);
	
	/**
	 * 创建/修改实体对象。Hibernate会根据object的状态，决定是使用save()还是update()。
	 */
	public Object saveOrUpdate(Object object,boolean isAdd);
	
	/**
	 * 创建/修改实体对象。Hibernate会根据object的状态，决定是使用save()还是update()。
	 */
	public Object saveOrUpdate(Object object);
	
	/**
	 * 删除持久化对象。
	 * @param object 想要删除的实体对象。
	 */
	public void delete(Object object);
	
	/**
	 * 从数据库中读取一个持久化对象。
	 * @param clazz 持久化类
	 * @param id    持久化对象的主键字
	 * @return      读取的一个持久化对象，或者null。
	 */
	public Object get(Class clazz, Serializable id);
	
	/**
	 * 根据参数查询
	 * @param HQL
	 * @param obj
	 * @return
	 */
	public List find(final String hql, final Object[] params,final boolean cacheable);
	
	/**
	 * 根据参数查询单个结果
	 * @param HQL
	 * @param obj
	 * @author qgfeng
	 * @date 2016-11-15
	 */
	public Object getUniqueResult(final String hql, final Object[] params,final boolean cacheable);
	
	
	/**
	 * 获得总记录数
	 * @param hql
	 * @param params
	 * @return
	 */
	public long selectTotalRecords(final String hql, final Object[] params,final boolean cacheable);
	
	/**
	 * 根据参数查询
	 * @param hql
	 * @param params
	 * @param start
	 * @param end
	 * @return
	 */
	public List find(final String hql, final Object[] params,final int start,final int end,final boolean cacheable);
	
	/**
	 * 批量更新,直接写hql
	 * @param hql
	 * @param params
	 * @return
	 */
	public int updateHql(final String hql, final Object[] params);
	
	/**
	 * hql分页面查询
	 * @param hql
	 * @param params
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging selectJsonPaging(final String hql, final Object[] params,JsonPaging jsonPaging,final boolean cacheable);
	/**
	 * hql分页面查询(不查询总记录数)
	 * @param hql
	 * @param params
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging selectJsonPagingNoTotal(final String hql, final Object[] params,JsonPaging jsonPaging,final boolean cacheable);

	/**
	 * 根据参数查询
	 * @param HQL
	 * @param obj
	 * @param maxResults
	 * @return
	 */
	public List find(String string, Object[] array, boolean b,
			Integer maxResults);
}