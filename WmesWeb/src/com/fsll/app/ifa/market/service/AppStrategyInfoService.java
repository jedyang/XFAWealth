package com.fsll.app.ifa.market.service;


import java.util.List;

import com.fsll.app.ifa.market.vo.AppStrategyAllocationVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoVO;
import com.fsll.app.ifa.market.vo.AppStrategyProductVo;
import com.fsll.common.util.JsonPaging;

/**
 * IFA 策略管理服务
 * @author zxtan
 * @date 2017-03-23
 */
public interface AppStrategyInfoService {

	/**
	 * 获取我的策略
	 * @author zxtan
	 * @date 2017-03-23
	 * @param jsonPaging
	 * @param memberId
	 * @param status
	 * @param isValid
	 * @param langCode
	 * @return
	 */
	public JsonPaging findMyStrategyInfoList(JsonPaging jsonPaging,String memberId,String keyword,String status,String isValid,String langCode);
	
	/**
	 * IFA策略列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findAllStrategyInfoList(JsonPaging jsonPaging,String memberId, String keyword, String langCode);
	
	/**
	 * 得到投资策略基本信息
	 * @author zxtan
	 * @date 2016-11-15
	 * @param strategyId 策略ID
	 * @param langCode 语言Code
	 * @return
	 */
	public AppStrategyInfoVO findStrategyInfo(String memberId,String strategyId,String langCode);
	
	/**
	 * 得到投资策略的产品信息列表
	 * @param strategyId 策略ID
	 * @param langCode 语言
	 * @param periodCode 回报时间类型
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
	 * 更新或删除策略
	 * @author zxtan
	 * @date 2017-03-23
	 * @param id 策略Id
	 * @param isValid 是否有效（update时用到）
	 * @return
	 */
	public boolean updateStrategyInfo(String id,String isValid);
	
	/**
	 * 更新或删除策略
	 * @author zxtan
	 * @date 2017-03-23
	 * @param id 策略Id
	 * @param isValid 是否有效（update时用到）
	 * @return
	 */
	public boolean deleteStrategyInfo(String id);
}
