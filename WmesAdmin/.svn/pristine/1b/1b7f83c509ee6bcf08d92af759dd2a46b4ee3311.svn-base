/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.elasticsearch.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import com.fsll.common.elasticsearch.ElasticsearchService;

/**
 * Elasticsearch 查询的公共实现类
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-14
 */
@Component("elasticsearchService")
public class ElasticsearchServiceImpl implements ElasticsearchService{
	
    private static final Logger logger = Logger.getLogger(ElasticsearchServiceImpl.class);
	
    //@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
	
	/**
	 * 模糊搜索--查询得到结果主键list
	 * @param indexName 索引名称,productindex
	 * @param type      索引类型,用来设定在多个类型中搜索,setTypes("productType1", "productType2","productType3")
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param hghlightedField  高亮显示的字段
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<String> queryForId(String indexName,String type,String[] fields, String content,String hghlightedField,String sortField, SortOrder order, int from, int size) {
		SearchRequestBuilder reqBuilder = elasticsearchTemplate.getClient()
										.prepareSearch(indexName)
										.setTypes(type)
										.setSearchType(SearchType.DEFAULT)
										.setExplain(true);//设置是否按查询匹配度排序
		/*								
		   设置查询类型 0,SearchType.DEFAULT默认    1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN = 扫描查询,无序
		   其值如下所示：
		   QUERY_THEN_FETCH:查询是针对所有的块执行的，但返回的是足够的信息，而不是文档内容（Document）。结果会被排序和分级，基于此，只有相关的块的文档对象会被返回。由于被取到的仅仅是这些，故而返回的hit的大小正好等于指定的size。这对于有许多块的index来说是很便利的（返回结果不会有重复的，因为块被分组了）
		   QUERY_AND_FETCH:最原始（也可能是最快的）实现就是简单的在所有相关的shard上执行检索并返回结果。每个shard返回一定尺寸的结果。由于每个shard已经返回了一定尺寸的hit，这种类型实际上是返回多个shard的一定尺寸的结果给调用者。
		   DFS_QUERY_THEN_FETCH：与QUERY_THEN_FETCH相同，预期一个初始的散射相伴用来为更准确的score计算分配了的term频率。
		   DFS_QUERY_AND_FETCH:与QUERY_AND_FETCH相同，预期一个初始的散射相伴用来为更准确的score计算分配了的term频率。
		   SCAN：在执行了没有进行任何排序的检索时执行浏览。此时将会自动的开始滚动结果集。
		   COUNT：只计算结果的数量，也会执行facet。
		*/
		QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery("\""+ content + "\"");
		for (String k : fields) {
			queryString.field(k);
		}
		queryString.minimumShouldMatch("10");//匹配度
		reqBuilder.setQuery(QueryBuilders.boolQuery().should(queryString)).setExplain(true);
		if (StringUtils.isNotEmpty(sortField) && order != null) {
			reqBuilder.addSort(sortField,order);
		}
		if (from >= 0 && size > 0) {
			reqBuilder.setFrom(from).setSize(size);
		}
        //设置高亮显示
		if(hghlightedField != null && !"".equals(hghlightedField)){
			reqBuilder.addHighlightedField(hghlightedField);
			reqBuilder.setHighlighterPreTags("<span style=\"color:red\">");
			reqBuilder.setHighlighterPostTags("</span>");
		}
		SearchResponse resp = reqBuilder.execute().actionGet();
		SearchHit[] hits = resp.getHits().getHits();
		ArrayList<String> results = new ArrayList<String>();
		for (SearchHit hit : hits) {
			results.add(hit.getId());
            // 获取对应的高亮域
            //Map<String,HighlightField> hlResult = hit.highlightFields();
            //从设定的高亮域中取得指定域
            //HighlightField titleField = hlResult.get(hghlightedField);
            //if (titleField !=null) {
                //取得定义的高亮标签
                //Text[] titleTexts = titleField.fragments();
                //为title串值增加自定义的高亮标签
                //String title = "";
                //for (Text text : titleTexts) {
                //    title += text;
                //}
                //System.out.println(title);
            //}
		}
		return results;
	}

	/**
	 * 模糊搜索--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param hghlightedField  高亮显示的字段
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObject(String indexName,String type,String[] fields, String content,String hghlightedField,String sortField, SortOrder order,int from, int size) {
		SearchRequestBuilder reqBuilder = elasticsearchTemplate.getClient().prepareSearch(indexName).setTypes(type).setSearchType(SearchType.DEFAULT).setExplain(true);
		QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery("\""+ content + "\"");
		for (String k : fields) {
			queryString.field(k);
		}
		queryString.minimumShouldMatch("10");
		//should 模糊搜索
		reqBuilder.setQuery(QueryBuilders.boolQuery().should(queryString)).setExplain(true);
		if (StringUtils.isNotEmpty(sortField) && order != null) {
			reqBuilder.addSort(sortField, order);
		}
		if (from >= 1 && size > 0) {
			reqBuilder.setFrom((from - 1) * size).setSize(size);
		}
        //设置高亮显示
		if(hghlightedField != null && !"".equals(hghlightedField)){
			reqBuilder.addHighlightedField(hghlightedField);
			reqBuilder.setHighlighterPreTags("<span style=\"color:red\">");
			reqBuilder.setHighlighterPostTags("</span>");
		}
		SearchResponse resp = reqBuilder.execute().actionGet();
		SearchHit[] hits = resp.getHits().getHits();
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : hits) {
			results.add(hit.getSource());
			
            // 获取对应的高亮域
            //Map<String,HighlightField> hlResult = hit.highlightFields();
            //从设定的高亮域中取得指定域
            //HighlightField titleField = hlResult.get(hghlightedField);
            //if (titleField !=null) {
                //取得定义的高亮标签
                //Text[] titleTexts = titleField.fragments();
                //为title串值增加自定义的高亮标签
                //String title = "";
                //for (Text text : titleTexts) {
                //    title += text;
                //}
                //System.out.println(title);
            //}
		}
		return results;
	}

	/**
	 * 完全匹配搜索--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param hghlightedField  高亮显示的字段
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObjectEq(String indexName,String type,String[] fields, String content,String hghlightedField, String sortField, SortOrder order,int from, int size) {
		SearchRequestBuilder reqBuilder = elasticsearchTemplate.getClient().prepareSearch(indexName).setTypes(type).setSearchType(SearchType.DEFAULT).setExplain(true);
		QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery("\""+ content + "\"");
		for (String k : fields) {
			queryString.field(k);
		}
		queryString.minimumShouldMatch("10");
		//must 完全匹配搜索
		reqBuilder.setQuery(QueryBuilders.boolQuery().must(queryString)).setExplain(true);
		if (StringUtils.isNotEmpty(sortField) && order != null) {
			reqBuilder.addSort(sortField, order);
		}
		if (from >= 0 && size > 0) {
			reqBuilder.setFrom(from).setSize(size);
		}
        //设置高亮显示
		if(hghlightedField != null && !"".equals(hghlightedField)){
			reqBuilder.addHighlightedField(hghlightedField);
			reqBuilder.setHighlighterPreTags("<span style=\"color:red\">");
			reqBuilder.setHighlighterPostTags("</span>");
		}
		SearchResponse resp = reqBuilder.execute().actionGet();
		SearchHit[] hits = resp.getHits().getHits();

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : hits) {
			results.add(hit.getSource());
            // 获取对应的高亮域
            //Map<String,HighlightField> hlResult = hit.highlightFields();
            //从设定的高亮域中取得指定域
            //HighlightField titleField = hlResult.get(hghlightedField);
            //if (titleField !=null) {
                //取得定义的高亮标签
                //Text[] titleTexts = titleField.fragments();
                //为title串值增加自定义的高亮标签
                //String title = "";
                //for (Text text : titleTexts) {
                //    title += text;
                //}
                //System.out.println(title);
            //}
		}
		return results;
	}

	/**
	 * 多个文字记不清是那些字,然后放进去查询--查询得到结果为Map集合
	 * @param indexName
	 * @param type  表
	 * @param fields   要查询的字段
	 * @param content  查询关键词
	 * @param hghlightedField  高亮显示的字段
	 * @param sortField 排序的字段
	 * @param order 排序的方向
	 * @param from 分页 从0开始
	 * @param size 每页的大小
	 * @return
	 */
	public List<Map<String, Object>> queryForObjectNotEq(String indexName,String type,String field,String hghlightedField, Collection<String> countents, String sortField,SortOrder order, int from, int size) {
		SearchRequestBuilder reqBuilder = elasticsearchTemplate.getClient().prepareSearch(indexName).setTypes(type).setSearchType(SearchType.DEFAULT).setExplain(true);
		List<String> contents = new ArrayList<String>();
		for (String content : countents) {
			contents.add("\"" + content + "\"");
		}
		TermsQueryBuilder inQuery = QueryBuilders.termsQuery(field, contents);
		reqBuilder.setQuery(QueryBuilders.boolQuery().mustNot(inQuery)).setExplain(true);
		if (StringUtils.isNotEmpty(sortField) && order != null) {
			reqBuilder.addSort(sortField, order);
		}
		if (from >= 0 && size > 0) {
			reqBuilder.setFrom(from).setSize(size);
		}
        //设置高亮显示
		if(hghlightedField != null && !"".equals(hghlightedField)){
			reqBuilder.addHighlightedField(hghlightedField);
			reqBuilder.setHighlighterPreTags("<span style=\"color:red\">");
			reqBuilder.setHighlighterPostTags("</span>");
		}
		SearchResponse resp = reqBuilder.execute().actionGet();
		SearchHit[] hits = resp.getHits().getHits();

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : hits) {
			results.add(hit.getSource());
            // 获取对应的高亮域
            //Map<String,HighlightField> hlResult = hit.highlightFields();
            //从设定的高亮域中取得指定域
            //HighlightField titleField = hlResult.get(hghlightedField);
            //if (titleField !=null) {
                //取得定义的高亮标签
                //Text[] titleTexts = titleField.fragments();
                //为title串值增加自定义的高亮标签
                //String title = "";
                //for (Text text : titleTexts) {
                //    title += text;
                //}
                //System.out.println(title);
            //}
		}
		return results;
	}
    
    /**
     * 检查健康状态
    * @return
     */
    public boolean ping() {
    	try {
    		ActionFuture<ClusterHealthResponse> health = elasticsearchTemplate.getClient().admin().cluster().health(new ClusterHealthRequest());
    		ClusterHealthStatus status = health.actionGet().getStatus();
    		if (status.value() == ClusterHealthStatus.RED.value()) {
				throw new RuntimeException("elasticsearch cluster health status is red.");
			}
    		return true;
		} catch (Exception e) {
			logger.error("ping elasticsearch error.", e);
			return false;
		}
    }
}
