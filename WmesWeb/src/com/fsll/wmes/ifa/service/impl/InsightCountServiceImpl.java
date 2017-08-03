package com.fsll.wmes.ifa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.ifa.service.InsightCountService;

/**
 * 观点数据统计接口实现
 * @author tan
 * @date 2016-8-11
 */
@Service("insightCountService")
//@Transactional
public class InsightCountServiceImpl extends BaseService implements
		InsightCountService {

	@Override
	public InsightCount findInsightById(String id) {
		// TODO Auto-generated method stub
		InsightCount obj = (InsightCount) this.baseDao.get(InsightCount.class, id);
		return obj;
	}

}
