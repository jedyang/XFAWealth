/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.es.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fsll.wmes.es.model.EsDiscoverTopic;

/**
 * 搜索存储类 通过springframework.data.repository实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-12
 */
public interface EsDiscoverTopicRepository extends CrudRepository<EsDiscoverTopic, String> {
	List<EsDiscoverTopic> findByAuthor(String author);
	List<EsDiscoverTopic> findByTopic(String topic);
	List<EsDiscoverTopic> findByAuthorAndTopic(String author,String topic);
	/**
     * 使用Query注解指定查询语句
     * 双引号和不加引号都可,不能是单引号
     * 注意：需要替换的参数?需要加双引号;需要指定参数下标,从0开始
     * @param topic
     * @param content
     * @return
     */
    //@Query("{\"bool\":{\"must\":[{\"term\":{\"content\":\"?1\"}},{\"term\":{\"topic\":\"?0\"}}]}}")
    public EsDiscoverTopic findByTopicAndContent(String topic, String content);
	
}
