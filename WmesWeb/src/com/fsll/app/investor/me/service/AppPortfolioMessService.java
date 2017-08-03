/**
 * 
 */
package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioChartDataVo;
import com.fsll.app.investor.me.vo.AppPortfolioFeeVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldVO;
import com.fsll.app.investor.me.vo.AppPortfolioMarketMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductDetailVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppPortfolioReturnVO;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;



/**
 * 投资组合接口服务接口类
 * @author zpzhou
 * @date 2016-9-14
 */
public interface AppPortfolioMessService {
	
	/**
	 * 得到我的投资组合列表
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param keyWord 搜索关键词
	 * @param toCurrency 货币转换参数
	 * @return
	 */
//	public List<AppPortfolioMessVo> findPortfolioList(String memberId,String ifaId,String keyWord,String toCurrency);

	/**
	 * 得到某个投资组合行情数据
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioMarketMessVo> findPortfolioMarketList(String portfolioId);
	/**
	 * 得到投资组合中某个产品的行情数据
	 * @param portfolioId 组合ID
	 * @param productId 产品ID
	 * @return
	 */
	public List<AppPortfolioMarketMessVo> findPortfolioProductMarketList(String portfolioId,String productId);
	/**
	 * 得到投资组合的产品信息列表
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @param dateType 回报时间类型
	 * @return
	 */
	public List<AppPortfolioProductVo> findPortfolioProductList(String portfolioId,String langCode,String dateType);
	/**
	 * 得到投资组合基本信息
	 * @param portfolioHoldId 组合ID
	 * @return
	 */
	public AppPortfolioHoldVO findPortfolioHold(String portfolioHoldId,String toCurrency,String langCode);
	/**
	 * 得到某个投资组合行情图表数据
	 * @param portfolioId 组合ID
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param langCode 显示语言
	 * @return	PortfolioChartDataVo	投资组合图表数据
	 */
	public AppPortfolioChartDataVo findPortfolioChart(String portfolioId,String period, String addiData, String langCode);
	
	/**
	 * 得到我的投资组合列表
	 * @author zxtan
	 * @date 2016-11-29
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppPortfolioHoldVO> findPortfolioList(String memberId,String toCurrency,String period,String langCode);

	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppPortfolioReturnVO findPortfolioReturn(String portfolioId,String periodCode);
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppPortfolioHoldCumperfVO> findPortfolioCumperfList(String portfolioId,String startDate);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioProductVo> findPortfolioProductFundList(String portfolioHoldId,String langCode,String toCurrency);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	public AppPortfolioProductDetailVO findPortfolioProductDetail(String holdProductId,String langCode,String toCurrency);
	
	/**
	 * 得到投资组合的产品配置信息
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findPortfolioAllocationList(String portfolioHoldId,String langCode,String toCurrency);
	
	/**
	 * 得到投资组合的基金配置信息
	 * @author zxtan
	 * @date 2017-02-17
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPieChartItemVO> findPortfolioFundAllocationList(String portfolioHoldId,String langCode,String toCurrency,String groupType);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @param productId 产品ID
	 * @param toCurrency 显示货币
	 * @return
	 */
	public List<AppPortfolioOrderHistoryVO> findPortfolioOrderHistoryList(String portfolioHoldId,String productId,String toCurrency,String langCode,String keyword);
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	public List<AppPortfolioHoldProductCumperfVO> findPortfolioHoldProductCumperfList(String portfolioHoldId,String productId,String startDate,String toCurrency,String langCode);
	
	/**
	 * 获取组合的费用列表
	 * @author zxtan
	 * @date 2017-01-11
	 * @param holdId
	 * @param langCode
	 * @param toCurrency
	 * @return
	 */
	public List<AppPortfolioFeeVO> findPortfolioFeeList(String holdId,String langCode,String toCurrency);
	
	
	/**
	 * 【我的投资顾问】——得到投资组合列表
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param periodCode 回报周期
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppPortfolioHoldVO> findMyIfaPortfolioList(String memberId,String ifaId,String periodCode,String toCurrency,String langCode);

	/**
	 * 得到我的持仓列表
	 * @author zxtan
	 * @date 2017-02-16
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @param period 周期 
	 * @param status 状态（1：当前持仓，2：进行中）
	 * @return
	 */
	public List<AppPortfolioHoldVO> findPortfolioHoldList(String memberId,String toCurrency,String period,String status,String langCode);

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
	public List<AppHoldProductVO> findHoldProductList(String portfolioHoldId,String productType,String langCode,String toCurrency);
}
