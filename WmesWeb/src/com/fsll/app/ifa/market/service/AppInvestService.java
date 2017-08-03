package com.fsll.app.ifa.market.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fsll.app.ifa.market.vo.AppIfaSupervisorVO;
import com.fsll.app.ifa.market.vo.AppInvestPortfolioForBuyVO;
import com.fsll.app.ifa.market.vo.AppInvestPortfolioForCheckVO;
import com.fsll.app.ifa.market.vo.AppInvestPortfolioForRebalanceVO;
import com.fsll.app.ifa.market.vo.AppInvestPortfolioReturnVO;
import com.fsll.app.ifa.market.vo.AppInvestPortfolioVO;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanProduct;

public interface AppInvestService {

	/**
	 * 组合分析——产品列表（调整比例）
	 * @author zxtan
	 * @date 2017-04-26
	 */
	public AppInvestPortfolioVO editInvestPortfolio(String planId,String langCode, String periodCode, String adjustProductIds,String adjustProductWeights);

	/**
	 * 组合分析——回报图表
	 */
	public List<AppInvestPortfolioReturnVO> findProductReturn(String planId, String periodCode);
	
	/**
	 * 组合购买显示（填写金额，选择投资账户）
	 * @author zxtan
	 * @date 2017-04-26
	 */
	public AppInvestPortfolioForBuyVO findInvestPortfolioForBuy(String planId,String langCode);
	
	/**
	 * 获取我的团队上司
	 * @author zxtan
	 * @data 2017-04-26
	 * @param 
	 * @return
	 */
	public List<AppIfaSupervisorVO> findMySupervisorList(String memberId,String langCode);
	
	/**
	 * 组合购买保存草稿
	 */
	public OrderPlan saveInvestPortfolioOrderDraft(JSONObject planObject,JSONObject holdObject,JSONArray productArray,String ifCheck,String supervisorId);
	
	/**
	 * 组合确认 步骤展示列表
	 * @author zxtan
	 * @data 2017-03-01
	 */
	public AppInvestPortfolioForCheckVO findInvestPortfolioForCheck(String memberId,String planId,String langCode);
	
	/**
	 * 删除组合购买计划的草稿
	 * @param planId
	 * @param memberId
	 * @return
	 */
	public boolean deleteOrderPlanDraft(String planId, String memberId);
	
	/**
	 * 保存计划的审批记录
	 * @param id
	 * @param planId
	 * @param memberId
	 * @param checkStatus
	 * @param checkResult
	 * @param checkIp
	 * @return
	 */
	public boolean updateOrderPlanCheck(String id,String planId, String memberId,String checkStatus,String checkResult,String checkIp);
	
	/**
	 * 获取交易主表
	 * @author zxtan
	 * @data 2017-02-13
	 * @param id
	 * @return
	 */
	public OrderPlan findOrderPlanById(String id);
	
	/**
	 * 获取交易产品
	 * @author zxtan
	 * @data 2017-02-13
	 */
	public List<OrderPlanProduct> findOrderPlanProduct(String planId, String aeCode);
	
	/**
	 * 根据产品ID获取基金信息
	 * @author zxtan
	 * @date 2017-02-14
	 * @param productId 产品ID
	 * @return
	 */
	public FundInfo findFundInfoByProduct(String productId);
	
	/**
	 * 代理商产品库中产品的编码
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public String findSymbolCode(String productId, String distributorId);
	
	/**
	 * 保存交易历史
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public OrderHistory saveOrderHistory(OrderHistory history);
	
	/**
	 * 更新组合持仓帐户绑定表
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public void updatePortfolioHoldAccount(String holdId,String memberId,String accountId,String accountNo);
	
	/**
	 * 更新组合交易计划
	 * @author zxtan
	 * @date 2017-02-16
	 */
	public void updateOrderPlan(String planId);
	
	
	/**
	 * 组合调整保存草稿
	 */
	public OrderPlan saveInvestPortfolioOrderDraftForRebalance(String portfolioHoldId,String orderPlanId,JSONArray productArray,String ifCheck,String supervisorId);
	
	/**
	 * 组合产品分配比例
	 * @param holdId
	 * @param planId
	 * @param langCode
	 * @return
	 */
	public AppInvestPortfolioForRebalanceVO findProductAllocationForRebalance(String holdId,String planId,String langCode);
	
	/**
	 * 组合分析——回报图表
	 */
	public List<AppInvestPortfolioReturnVO> findProductReturn(String productIds,String productRates, String periodCode);
	
	/**
	 * 组合购买显示（填写金额，选择投资账户）
	 * @author zxtan
	 * @date 2017-04-26
	 */
	public AppInvestPortfolioForBuyVO findInvestPortfolioForRebalanceBuy(String holdId,String productIds,String langCode);
}
