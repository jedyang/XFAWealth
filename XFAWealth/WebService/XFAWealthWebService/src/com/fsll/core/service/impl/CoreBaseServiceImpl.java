/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.core.service.CoreBaseService;

/**
 * 公共业务方法核心业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("coreBaseService")
public class CoreBaseServiceImpl  extends BaseService implements CoreBaseService{
	
	/**
	 * 创建持久化对象。
	 * @param object  临时状态（transient）的实体对象。
	 * @return 创建后的持久化实例。
	 */
	public Object create(Object object){
		return baseDao.create(object);
	}
	
	/**
	 * 修改持久化对象。
	 * @param object 准备修改的实体对象。
	 * @return 修改后的实体对象。
	 */
	public Object update(Object object){
		return baseDao.update(object);
	}
	
	/**
	 * 删除持久化对象。
	 * @param object 想要删除的实体对象。
	 */
	public void delete(Object object){
		baseDao.delete(object);
	}
	
	/**
	 * 从数据库中读取一个持久化对象。
	 * @param clazz 持久化类
	 * @param id    持久化对象的主键字
	 * @return      读取的一个持久化对象，或者null。
	 */
	public Object get(Class clazz, Serializable id){
		return baseDao.get(clazz, id);
	}
	
	/**
	 * @param hql
	 * @param params
	 * @return
	 */
	public List findHql(final String hql, final Object[] params){
		return baseDao.find(hql, params, false);
	}
	
	/**
	 * 批量更新,直接写hql
	 * @param hql
	 * @param params
	 * @return
	 */
	public int updateHql(final String hql, final Object[] params){
		return baseDao.updateHql(hql, params);
	}

	/**
	 * @param sql  SQL命令，没有参数
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql){
		return springJdbcQueryManager.springJdbcQueryForList(sql);
	}
	
	/**
	 * 执行sql
	 * @param SQL
	 */
	public void updateSql(String sql){
		springJdbcQueryManager.execute(sql);
	}
	
}
