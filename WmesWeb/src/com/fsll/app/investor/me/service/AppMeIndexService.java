package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.app.investor.me.vo.AppPortfolioInfoVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.common.util.JsonPaging;

/**
 * 我的——首页服务接口
 * @author zxtan
 * @date 2016-11-29
 */
public interface AppMeIndexService {

	public List<AppPortfolioInfoVO> findVisitedPortfolioList(String memberId,String langCode,String periodCode,int rows);
	
	public AppInsightItemVo findVisitedInsightInfo(String memberId);
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-01-22
	 * @param memberId
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String memberId, String toCurrency,String langCode,String orderType,String keyword);
}
