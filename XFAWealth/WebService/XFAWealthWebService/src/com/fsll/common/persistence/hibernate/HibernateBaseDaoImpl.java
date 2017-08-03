/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.persistence.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.fsll.common.persistence.BaseDao;
import com.fsll.common.util.JsonPaging;

/**
 *  DAO操作接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
@Repository("baseDao")
public class HibernateBaseDaoImpl implements BaseDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	/**
	 * 创建持久化对象。
	 * @param object   临时状态（transient）的实体对象。
	 * @return 创建后的持久化实例。
	 */
	public Object create(Object object) {
		sessionFactory.getCurrentSession().save(object);
		sessionFactory.getCurrentSession().flush();
		return object;
	}

	/**
	 * 修改持久化对象。
	 * @param object 准备修改的实体对象。
	 * @return 修改后的实体对象。
	 */
	public Object update(Object object) {
		sessionFactory.getCurrentSession().update(object);
		sessionFactory.getCurrentSession().flush();
		return object;
	}

	/**
	 * 创建/修改实体对象。Hibernate会根据object的状态，决定是使用save()还是update()。
	 */
	public Object saveOrUpdate(Object object,boolean isAdd) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
		sessionFactory.getCurrentSession().flush();
		return object;
	}

	/**
	 * 创建/修改实体对象。Hibernate会根据object的状态，决定是使用save()还是update()。
	 */
	public Object saveOrUpdate(Object object) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
		sessionFactory.getCurrentSession().flush();
		return object;
	}
	
	/**
	 * 删除持久化对象。
	 * @param object想要删除的实体对象。
	 */
	public void delete(Object object) {
		sessionFactory.getCurrentSession().delete(object);
		sessionFactory.getCurrentSession().flush();
	}
	/**
	 * 从数据库中读取一个持久化对象。
	 * @param clazz 持久化类
	 * @param id 持久化对象的主键字
	 * @return 读取的一个持久化对象，或者null
	 */
	public Object get(Class clazz, Serializable id) {
		Object object = sessionFactory.getCurrentSession().get(clazz, id);
		return object;
	}

	/**
	 * 根据参数查询
	 * @param hql
	 * @param obj
	 * @return
	 */
	public List find(final String hql, final Object[] params,final boolean cacheable) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		if (cacheable) {
			query.setCacheable(true);
		}
		return query.list();
	}
	
	/**
	 * 根据参数查询单个结果
	 * @param HQL
	 * @param obj
	 * @author qgfeng
	 */
	@Override
	public Object getUniqueResult(String hql, Object[] params, boolean cacheable) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		if (cacheable) {
			query.setCacheable(true);
		}
		return query.uniqueResult();
	} 
	
	/**
	 * 获得总记录数
	 * @param hql
	 * @param params
	 * @return
	 */
	public long selectTotalRecords(final String hql, final Object[] params,final boolean cacheable) {
		List list = this.find(hql, params ,cacheable);
		if (list.isEmpty()) {
			return 0;
		} else {
			Object o = list.get(0);
			if (o == null)
				return 0;
			if (o instanceof Integer)
				return ((Integer) o).longValue();
			else
				return ((Long) o).longValue();
		}
	}
	
	/**
	 * 根据参数查询
	 * @param HQL
	 * @param obj
	 * @return
	 */
	public List find(final String hql, final Object[] params,final int start,final int end,final boolean cacheable){
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult(start-1);
		query.setMaxResults(end-start);
		if (cacheable) {
			query.setCacheable(true);
		}
		return query.list();
	}
	
	/**
	 * 批量更新,直接写hql
	 * @param hql
	 * @param params
	 * @return
	 */
	public int updateHql(final String hql, final Object[] params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		int result = query.executeUpdate();
	    return result;
	}
	/**
	 * hql分页面查询
	 * @param hql
	 * @param params
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging selectJsonPaging(final String hql, final Object[] params,JsonPaging jsonPaging,final boolean cacheable) {
		String hql1 = "select count(*) " + hql;
		//modified by michael start
		if (hql.toUpperCase().indexOf("SELECT")>=0) 
			hql1 = "select count(*) " + hql.substring(hql.toUpperCase().indexOf("FROM"));
		//modified by michael end
		
		long totalRecords;
		if (params != null)
			totalRecords = selectTotalRecords(hql1, params,cacheable);
		else
			totalRecords = selectTotalRecords(hql1, null,cacheable);
		jsonPaging.setTotal(Integer.parseInt(Long.toString(totalRecords)));
		
		//modified by michael start
		int curPage = 1;
		if (null!=jsonPaging.getPage())
			curPage = (jsonPaging.getPage()<1)?1:jsonPaging.getPage();
		jsonPaging.setPage(curPage);
		
		int pageSize = 0;
		if (null!=jsonPaging.getRows())
			pageSize = (jsonPaging.getRows()<0)?0:jsonPaging.getRows();
		jsonPaging.setRows(pageSize);
		//modified by michael end
		
		final String order = jsonPaging.getOrderStr();
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql + order);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult((curPage - 1) * pageSize);
		if (pageSize>0)
			query.setMaxResults(pageSize);
		
		if (cacheable) {
			query.setCacheable(true);
		}
		try {
			List list = query.list();
			jsonPaging.setList((ArrayList) list);
		} catch (Exception e) {
			jsonPaging.setList(null);
			e.printStackTrace();
		}
		return jsonPaging;
	} 
	
	/**
	 * hql分页面查询
	 * @param hql
	 * @param params
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging selectJsonPagingNoTotal(final String hql, final Object[] params,JsonPaging jsonPaging,final boolean cacheable) {		
		final int curPage = jsonPaging.getPage();
		final int pageSize = jsonPaging.getRows();
		final String order = jsonPaging.getOrderStr();
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql + order);
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult((curPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		if (cacheable) {
			query.setCacheable(true);
		}
		try {
			List list = query.list();
			jsonPaging.setList((ArrayList) list);
		} catch (Exception e) {
			jsonPaging.setList(null);
			e.printStackTrace();
		}
		return jsonPaging;
	} 
	
	/**
	 * 根据参数查询
	 * @param hql
	 * @param obj
	 * @return
	 */
	public List find(final String hql, final Object[] params,final boolean cacheable,final Integer maxResults) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		if(maxResults != null){
			query.setMaxResults(maxResults);
		}
		for (int i = 0; i < (params != null ? params.length : 0); i++) {
			query.setParameter(i, params[i]);
		}
		if (cacheable) {
			query.setCacheable(true);
		}
		return query.list();
	}
}
