package com.fsll.app.ifa.market.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.app.ifa.market.vo.AppHoldAllocationVO;
import com.fsll.app.ifa.market.vo.AppHoldCountVO;
import com.fsll.app.ifa.market.vo.AppHoldCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldFeeVO;
import com.fsll.app.ifa.market.vo.AppHoldProductCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldProductDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductFundVO;
import com.fsll.app.ifa.market.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductVO;
import com.fsll.app.ifa.market.vo.AppHoldReturnVO;
import com.fsll.app.ifa.market.vo.AppHoldVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.app.ifa.market.vo.AppPieChartItemVO;
import com.fsll.common.util.JsonPaging;

/**
 * IFA-Market投资组合服务
 * @author zxtan
 * @date 2017-03-29
 */
public interface AppPortfolioHoldService {

	/**
	 * 获取IFA客户持仓组合统计（盈利、亏损数）
	 * @author zxtan
	 * @date 2017-04-26
	 */
	public AppHoldCountVO findPortfolioHoldCount(JSONObject jsonObject);
	
	/**
	 * 获取IFA客户详情的组合列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging,JSONObject jsonObject);
	
	/**
	 * 获取IFA客户组合进行中列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findPortfolioOrderPlanList(JsonPaging jsonPaging,JSONObject jsonObject);
	
	/**
	 * 得到投资组合基本信息
	 * @author zxtan
	 * @param portfolioHoldId 组合ID
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldVO findPortfolioHold(String portfolioHoldId,String toCurrency,String langCode);
	
	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppHoldReturnVO findPortfolioHoldReturn(String portfolioHoldId,String periodCode);
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppHoldCumperfVO> findPortfolioHoldCumperfList(String portfolioHoldId,String startDate);
	
	/**
	 * 得到我的组合详情的基金产品信息列表
	 * @author zxtan
	 * @date 2017-03-27
	 * @param portfolioHoldId 组合Id
	 * @param productType 产品类型
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppHoldProductVO> findPortfolioHoldProductList(String portfolioHoldId,String productType,String langCode,String toCurrency);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppHoldAllocationVO> findPortfolioHoldAllocationList(String portfolioHoldId,String langCode,String toCurrency);
		
	/**
	 * 得到投资组合的基金配置情况
	 * @author zxtan
	 * @date 2017-02-17
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPieChartItemVO> findPortfolioFundAllocationList(String portfolioHoldId,String langCode,String toCurrency,String groupType);
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String ifaMemberId,String keyword,String langCode,int num);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioHoldId 组合ID
	 * @param productId 产品ID
	 * @param toCurrency 显示货币
	 * @return
	 */
	public List<AppOrderHistoryVO> findPortfolioOrderHistoryList(String portfolioHoldId,String productId,String toCurrency,String langCode,String keyword);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	public AppHoldProductDetailVO findPortfolioHoldProductDetail(String holdProductId,String langCode,String toCurrency);
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2017-03-29
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	public List<AppHoldProductCumperfVO> findPortfolioHoldProductCumperfList(String portfolioHoldId,String productId,String startDate,String toCurrency);
	
	/**
	 * 获取组合的费用列表
	 * @author zxtan
	 * @date 2017-04-28
	 * @param holdId
	 * @param langCode
	 * @param toCurrency
	 * @return
	 */
	public List<AppHoldFeeVO> findPortfolioFeeList(String holdId,String langCode,String toCurrency);
	
	/**
	 * 得到我的资产的产品回报信息
	 * @author zxtan
	 * @date 2017-03-03
	 * @param holdProductId 持仓产品Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldProductReturnDetailVO findHoldProductReturnDetail(String holdProductId,String langCode,String toCurrency);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppHoldProductFundVO> findPortfolioProductFundList(String portfolioHoldId,String langCode,String toCurrency);
}
