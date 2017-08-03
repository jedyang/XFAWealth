/**
 * 
 */
package com.fsll.app.investor.market.service;


import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.common.util.JsonPaging;

/**
 * 首页不容错过接口类
 * @author zpzhou
 * @date 2016-9-26
 */
public interface AppIndexNoMissService {
	
	/**
	 * 得到首页不容错过信息
	 * @param langCode 语言
	 * @return
	 */
	public List<AppIndexNoMissVo> findIndexNoMiss(String memberId,String langCode,String period);


	/**
	 * 得到首页-不容错过-更多投资策略列表信息
	 * @return
	 */
	public JsonPaging findNoMissStrategyMore(JsonPaging jsonPaging);
	/**
	 * 得到首页-不容错过-更多投资组合列表信息
	 * @return
	 */
	public JsonPaging findNoMissPortfolioMore(JsonPaging jsonPaging);
	/**
	 * 得到首页-不容错过-更多基金列表信息
	 * @return
	 */
	public JsonPaging findNoMissFundMore(JsonPaging jsonPaging,String langCode,String period);
}
