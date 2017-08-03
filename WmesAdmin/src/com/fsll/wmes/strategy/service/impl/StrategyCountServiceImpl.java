package com.fsll.wmes.strategy.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.strategy.service.StrategyCountService;

/**
 * 观点数据统计接口实现
 * @author tan
 * @date 2016-8-11
 */
@Service("strategyCountService")
//@Transactional
public class StrategyCountServiceImpl extends BaseService implements
		StrategyCountService {

	@Override
	public StrategyCount findInsightById(String id) {
		// TODO Auto-generated method stub
		StrategyCount obj = (StrategyCount) this.baseDao.get(StrategyCount.class, id);
		return obj;
	}

}
