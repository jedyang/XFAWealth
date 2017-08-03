/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.es.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.es.model.EsDiscoverTopic;
import com.fsll.wmes.es.repositories.EsDiscoverTopicRepository;
import com.fsll.wmes.es.service.EsDiscoverTopicService;
/**
 * 搜索实现类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-12
 */
@Service("esDiscoverTopicService")
//@Transactional
public class EsDiscoverTopicServiceImpl implements EsDiscoverTopicService{
	
    private static final Logger logger = Logger.getLogger(EsDiscoverTopicServiceImpl.class);

    //@Autowired
    private EsDiscoverTopicRepository esDiscoverTopicRepository;
    
    //@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    
    /**
     * 新增或者修改
     */
    public EsDiscoverTopic saveOrUpdate(EsDiscoverTopic discoverTopic) {
    	if(discoverTopic.getId() == null || "".equals(discoverTopic.getId())){
    		discoverTopic.setId(null);
    	}
        return esDiscoverTopicRepository.save(discoverTopic);
    }
    
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	private void deleteById(String id){
		esDiscoverTopicRepository.delete(id);
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				deleteById(id);
			}
		}
		return true;
	}

    /**
     * 获得一条数据
     *
     * @return
     */
    public EsDiscoverTopic findById(String id) {
    	return esDiscoverTopicRepository.findOne(id);
    }
	
    /**
     * 创建索引
     * @param discoverTopic
     * @return
     */
    public boolean saveIndex(EsDiscoverTopic discoverTopic) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(discoverTopic.getId()).withObject(discoverTopic).build();
            elasticsearchTemplate.index(indexQuery);
            return true;
        } catch (Exception e) {
            logger.error("insert or update news info error.", e);
            return false;
        }
    }
    
    /**
     * 修改索引
     * @param list
     * @return
     */
    public boolean updateIndex(List<EsDiscoverTopic> list) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (EsDiscoverTopic discoverTopic : list) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(discoverTopic.getId()).withObject(discoverTopic).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }

    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param discoverTopic 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,EsDiscoverTopic discoverTopic){
		/********** 搜索参数初始化 start ************/
		String indexName = "discover_topic";
		String type = "discover_topic";
		String[] fields = {"topic","content"};
		String content = discoverTopic.getTopic();
		String hghlightedField = "content";
		String sortField = "lastReplyTime";
		SortOrder order = SortOrder.DESC;
		int from = jsonpaging.getPage();
		int size = jsonpaging.getRows();
		/********** 搜索参数初始化 start ************/
		
		/********** 执行搜索 start ************/
		SearchRequestBuilder reqBuilder = elasticsearchTemplate.getClient().prepareSearch(indexName).setTypes(type).setSearchType(SearchType.QUERY_THEN_FETCH).setExplain(true);
		QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery("\""+ content + "\"");
		for (String k : fields) {
			queryString.field(k);
		}
		queryString.minimumShouldMatch("10");
		
		//should模糊搜索
		reqBuilder.setQuery(QueryBuilders.boolQuery().should(queryString)).setExplain(true);
		if (StringUtils.isNotEmpty(sortField) && order != null) {
			reqBuilder.addSort(sortField, order);
		}
		if (from >= 1 && jsonpaging.getRows() > 0) {
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
		/********** 执行搜索 end ************/
		
		/********** 数据处理 start ************/
		List list = new ArrayList();
		//List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : hits) {
    		EsDiscoverTopic obj = new EsDiscoverTopic();
    		try{
    			BeanUtils.populate(obj,hit.getSource());
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
			
            Map<String,HighlightField> hlFields = hit.highlightFields();
            //从设定的高亮域中取得指定域
            HighlightField hlField = hlFields.get(hghlightedField);
            if (hlField !=null) {
                //取得定义的高亮标签
                Text[] hlTexts = hlField.fragments();
                //为title串值增加自定义的高亮标签
                String hlResult = "";
                for (Text text : hlTexts) {
                	hlResult += text;
                }
                obj.setContent(hlResult);
            }
    		list.add(obj);
		}
		/********** 数据处理 end ************/
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
}
