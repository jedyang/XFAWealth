/**
 * 
 */
package com.fsll.wmes.portfolio.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.PortfolioArena;
//import com.fsll.wmes.entity.PortfolioProduct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArenaAip;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioFundListVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.WebInvestorVisitVO;

/**
 * 投资组合信息查询服务接口
 * @author michael
 * @date 2016-8-17
 */
public interface PortfolioArenaService {

	/**
	 * 获取列表
	 * @param jsonPaging
	 * @param keyword
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging,String keyword);
	
	/**
	 * 获取内容信息
	 * @param id
	 * @return
	 */
	public PortfolioArena findById(String id);
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param jsonPaging
	 * @param keyword
	 * @param sector
	 * @param region
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword, String sector, String region, String fromDate, String toDate);
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param jsonPaging
	 * @param criteria
	 * @return
	 */
	public JsonPaging findByUser(JsonPaging jsonPaging, CriteriaVO criteria,String langCode);
	
	/**
	 * 保存投资策略
	 * @author michael
	 * @param info
	 * @return
	 */
	public PortfolioArena saveOrUpdate(PortfolioArena info);

	/**
	 * 保存投资策略的产品
	 * @author michael
	 * @param strategyArena
	 * @param fundId
	 * @return
	 */
//	public PortfolioProduct saveOrUpdate(PortfolioArena strategyArena, String fundId);
	
	/**
	 * 重置用户所有已置顶的记录
	 * @author michael
	 * @param userId 
	 */
	public void clearOverhead(String userId);

	/**
	 * 删除投资策略
	 */
	public void delete(PortfolioArena info);
	

	/**
	 * 获取投资策略关联的基金产品
	 * @param portfolioId
	 * @return
	 */
	public List<FundInfo> findFundListByPortfolio(String portfolioId);
	
	/**
	 * 通过ID获取查看权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebView findWebViewById(String id);
	
	/**
	 * 获取投资策略关联的查看权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebViewVO findWebViewByPortfolio(String portfolioId);
	
	/**
	 * 通过ID获取推送权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebPush findWebPushById(String id);
	
	/**
	 * 获取投资策略关联的推送权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebPushVO findWebPushByPortfolio(String portfolioId);
	
	/**
	 * 获取查看权限明细
	 * @author michael
	 * @param viewId
	 * @param type
	 * @return
	 */
	public List<WebViewDetail> findWebViewDetailList(String viewId, String type);
	
	/**
	 * 获取推送权限明细
	 * @author michael
	 * @param pushId
	 * @param type
	 * @return
	 */
	public List<WebPushDetail> findWebPushDetailList(String pushId, String type);
	
	/**
	 * 重置查看权限明细记录
	 * @author michael
	 * @param viewId 
	 * @param type
	 */
	public void clearWebViewDetail(String viewId, String type);
	
	/**
	 * 重置推送权限明细记录
	 * @author michael
	 * @param pushId 
	 * @param type
	 */
	public void clearWebPushDetail(String pushId, String type);
	
//	public List<PortfolioProduct> findPortfolioProductByPortfolioId(String id);
//	
//	public List<ProductInfo> findProductArenaList( List<PortfolioProduct> list );
	
	/**
	 * 获取投资组合关联产品比重
	 * @author mqzou 
	 * @date 2016-09-13
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	public double findPortfolioInfoProductRate(String portfolioId,String productId);
	
	/**
	 * 获取投资组合产品比重列表（用于图表显示）
	 * @author mqzou 
	 * @date 2016-09-13
	 * @param type
	 * @param portfolioId
	 * @return
	 */
	public List findPortfolioProductRate(String type,String portfolioId,String langCode);

	/**
	 * 创建投资组合--获取我的投资策略
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public List<StrategyInfo> getMyStrategy(String memberId);

	/**
	 * 根据id获取StrategyInfo实体
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public StrategyInfo getStrategyInfoById(String strategyId);

	/**
	 * 根据id获取PortfolioArena实体
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public PortfolioArena getPortfolioArenaById(String portfolioId);

	/**
	 * 保存组合与产品信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public PortfolioArenaProduct saveOrUpdate(PortfolioArenaProduct product);

	/**
	 * 保存定投信息
	 * @author wwluo
	 * @data 2016-09-29
	 * @return
	 */
	public PortfolioArenaAip saveOrUpdate(PortfolioArenaAip arenaAip);
	
	/**
	 * 获取投资组合的每日盈亏
	 * @author mqzou
	 * @data 2016-10-27
	 * @param portfolioId
	 * @return
	 */
	public List<PortfolioArenaCumperf> findPortfolioArenaCumperfById(String portfolioId);
	
	/**
	 * 获取投资组合的盈亏
	 * @author mqzou
	 * @data 2016-11-15
	 * @param portfolioId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<PortfolioArenaCumperf> findPortfolioArenaCumperf(String portfolioId,String startDate,String endDate);
	
	/**
	 * 获取投资组合最新的一条盈亏记录
	 * @author mqzou
	 * @data 2016-10-27
	 * @param portfolioId
	 * @return
	 */
	public PortfolioArenaCumperf findLastPortfolioArenaCumperf(String portfolioId);

	/**
	 * 获取策略分配比例
	 * @author wwluo
	 * @data 2016-10-25
	 * @return 
	 */
	public List<StrategyAllocation> getStrategyAllocation(String id);

	/**
	 * 投资策略产品
	 * @author wwluo
	 * @data 2016-10-25
	 * @param strategyId 策略Id
	 * @return 
	 */
	public List<StrategyProduct> getStrategyProducts(String strategyId);

	/**
	 * 根据id获取组合定投信息
	 * @author wwluo
	 * @data 2016-10-25
	 * @param id  投资组合Id
 	 * @return 
	 */
	public PortfolioArenaAip getAipByPortfolioId(String id);
	
	/**
	 * 获取我的投资组合（arena）
	 * @author wwluo
	 * @data 2016-11-18
	 * @param keyWord 组合名称关键字
	 * @param isPublic 是否公开 
	 * @param status 是否草稿 
 	 * @return 
	 */
	public JsonPaging getMyArenas(JsonPaging jsonPaging, MemberBase loginUser,
			String keyWord,String isPublic,String status);

	/**
	 * 删除投资组合（物理删除）
	 * @author wwluo
	 * @data 2016-11-18
	 * @param info PortfolioArena 投资组合实体
 	 * @return 
	 */
	public void delPortfolio(PortfolioArena info);
	
	/**
	 * 获取ifa推荐的投资组合列表
	 * @author mqzou
	 * @data 2016-11-30
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param keyWord
	 * @param langCode
	 * @param loginUser
	 * @return
	 */
	public JsonPaging findByIfaRecommend(JsonPaging jsonPaging,String ifaMemberId,String keyWord,String langCode,MemberBase loginUser);

	/**
	 * 获取Memberbase信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public List<MemberBase> getMemberBaseByIds(String ids);

	/**
	 * 删除arena原产品
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public void delArenaProduct(String portfolioId);

	/**
	 * 获取产品权重
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	public List<FundInfoDataVO> getProductWeight(String portfolioId,
			List<FundInfoDataVO> fundInfoList);
	
	/**
	 * 查找策略的产品
	 * @param strategyId
	 * @param type 产品类型:fund基金,stock股票,futures期货
	 * @return
	 */
	public List<ProductInfo> findProductInfoList(String strategyId, String type);
	
	/**
	 * 获取组合的基金列表（用户组合详情显示）
	 * @author mqzou 2014-04-17
	 * @param id
	 * @return
	 */
	public List<PortfolioFundListVO> findPortfolioFundList(String id);

	public String[] getProductWeight(String id);
}
