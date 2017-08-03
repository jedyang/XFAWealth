package com.fsll.wmes.ifa.service;

import com.fsll.wmes.entity.InsightCount;


/**
 * 观点数据统计接口
 * @author tan
 * @date 2016-8-19
 */
public interface InsightCountService {
	
	
	/**
	 * 获取内容
	 * @param id
	 * @param lang
	 * @return
	 */
	public InsightCount findInsightById(String id);	
	
	
}
