/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.elasticsearch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;

/**
 * Elasticsearch 查询的公共接口类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-14
 */
public interface ElasticsearchService {
	/**
	 * 模糊搜索--查询得到结果主键list
	 * @param indexName 索引名称,productindex
	 * @param type      索引类型,用来设定在多个类型中搜索,setTypes("productType1", "productType2","productType3")
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<String> queryForId(String indexName,String type,String[] fields, String content,String hghlightedField,String sortField, SortOrder order, int from, int size);
	
	/**
	 * 模糊搜索--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObject(String indexName,String type,String[] fields, String content,String hghlightedField, String sortField, SortOrder order,int from, int size);
	
	
	/**
	 * 完全匹配搜索--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObjectEq(String indexName,String type,String[] fields, String content,String hghlightedField,String sortField, SortOrder order,int from, int size);
	
	/**
	 * 多个文字记不清是那些字,然后放进去查询--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObjectNotEq(String indexName,String type,String field,String hghlightedField,Collection<String> countents, String sortField,SortOrder order, int from, int size);
	
    /**
     * 检查健康状态
    * @return
     */
    public boolean ping();
}
