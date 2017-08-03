/**
 * 
 */
package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebExchangeRate;

/**
 * @author scshi_u330p
 *货币转换接口
 *@date 20161101
 */
public interface SysCurrencyExchangeService {

	/**
	 * 管理列表数据
	 * @author scshi_u330p
	 * @date 20161101
	 * @param keyword
	 * 
	 * modify by rqwang 20170512
	 * */
	public JsonPaging findAll(JsonPaging jsonpaging,String keyWord,String langCode) throws Exception ;
	
	/**
	 * 根据ID获取货币汇率实体
	 * @author scshi_u330p
	 * @date 20161102
	 * */
	public WebExchangeRate findById(String id);

	/**
	 * 根据输入code获取已存在的汇率关系
	 * @author scshi_u330p
	 * @date 20161104
	 * @param currencyCode 
	 * @param currencyType 1-- 查找原始货币，2--查找目标货币
	 * */
	public List<WebExchangeRate> findExistExchange(String currencyCode,String currencyType);
	
	/**
	 * 保存或更新
	 * @param info
	 * @return
	 */
	public WebExchangeRate saveOrUpdate(WebExchangeRate info);
	

}
