/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.es.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.es.model.EsDiscoverTopic;

/**
 * 搜索接口类
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-13
 */
public interface EsDiscoverTopicService {
	
    /**
     * 新增或者修改
     * @param discoverTopic
     * @return
     */
    public EsDiscoverTopic saveOrUpdate(EsDiscoverTopic discoverTopic);
    
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
    /**
     * 获得一条数据
     *
     * @return
     */
    public EsDiscoverTopic findById(String id);
	
    /**
     * 新增索引
     * @param book
     * @return
     */
    public boolean saveIndex(EsDiscoverTopic discoverTopic);
    
    /**
     * 修改索引
     * @param list
     * @return
     */
    public boolean updateIndex(List<EsDiscoverTopic> list);
    
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param memberBase 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,EsDiscoverTopic discoverTopic);
	
}
