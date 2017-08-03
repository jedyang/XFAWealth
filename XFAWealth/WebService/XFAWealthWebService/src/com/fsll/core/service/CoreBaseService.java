/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service;

import java.io.Serializable;
import java.util.List;

/**
 * 公共业务方法核心接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface CoreBaseService {
	
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
	 * @param hql
	 * @param params
	 * @return
	 */
	public List findHql(final String hql, final Object[] params);
	
	/**
	 * 批量更新,直接写hql
	 * @param hql
	 * @param params
	 * @return
	 */
	public int updateHql(final String hql, final Object[] params);
	
	
	/**
	 * @param sql  SQL命令，没有参数
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql);
	
	
	/**
	 * 执行sql
	 * @param SQL
	 */
	public void updateSql(String sql);
}
