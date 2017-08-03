package com.fsll.wmes.strategy.service;

import com.fsll.wmes.entity.StrategyCount;


/**
 * 观点数据统计接口
 * @author tan
 * @date 2016-8-19
 */
public interface StrategyCountService {
	
	
	/**
	 * 获取内容
	 * @param id
	 * @param lang
	 * @return
	 */
	public StrategyCount findInsightById(String id);	
	
	
}
