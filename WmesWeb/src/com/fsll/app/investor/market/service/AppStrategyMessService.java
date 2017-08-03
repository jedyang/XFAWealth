/**
 * 
 */
package com.fsll.app.investor.market.service;

import java.util.List;

import com.fsll.app.investor.market.vo.AppStrategyAllocationVO;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.investor.market.vo.AppStrategyProductVo;
import com.fsll.common.util.JsonPaging;



/**
 * 投资策略接口服务接口类
 * @author zpzhou
 * @date 2016-9-18
 */
public interface AppStrategyMessService {
	/**
	 * 得到我有权限查看的投资策略列表
	 * @param memberId 用户ID
	 * @param keyWord 搜索关键词
	 * @return JsonPaging 分页数据
	 */
	public JsonPaging findStrategyList(JsonPaging jsonPaging,String memberId,String keyWord,String langCode);
	/**
	 * 得到投资策略基本信息
	 * @param strategyId 策略ID
	 * @return
	 */
	public AppStrategyMessVo findStrategyInfoMess(String strategyId);
	/**
	 * 得到投资策略的产品信息列表
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
	 * @return
	 */
	public List<AppStrategyProductVo> findStrategyProductList(String strategyId,String langCode,String periodCode,String productType,String toCurrency);
	
	/**
	 * 得到投资策略的产品信息列表
	 * @author zxtan
	 * @date 2016-11-15
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
	 * @param productType 产品类型
	 * @param rows 返回行数
	 * @param toCurrency 货币类型
	 * @return
	 */
	public List<AppStrategyProductVo> findStrategyProductList(String strategyId,String langCode,String periodCode,String productType,int rows,String toCurrency);
	/**
	 * 按分类得到某个策略配置情况
	 * @author zxtan
	 * @date 2016-11-14
	 * @param strategyId
	 * @param type
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyAllocationVO> findStrategyAllocationList(String strategyId,String type,String langCode);
	
	/**
	 * 得到投资策略基本信息
	 * @author zxtan
	 * @date 2016-11-15
	 * @param strategyId 策略ID
	 * @param langCode 语言Code
	 * @return
	 */
	public AppStrategyInfoVO findStrategyInfo(String memberId,String strategyId,String langCode);
}
