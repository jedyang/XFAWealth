package com.fsll.app.investor.me.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fsll.app.investor.me.vo.AppInvestPortfolioForBuyVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForCheckVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioForRebalanceVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioReturnVO;
import com.fsll.app.investor.me.vo.AppInvestPortfolioVO;
import com.fsll.app.investor.me.vo.AppInvestProductVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;

public interface AppInvestService {
	
	/**
	 * 得到我自选的产品列表
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @return List 列表数据
	 */
	public List<AppInvestProductVO> findWatchProductList(String memberId,String langCode);
	
	public AppInvestPortfolioVO findInvestPortfolio(String productIds,String productRates,String langCode,String toCurrency,String periodCode);

	public List<AppInvestPortfolioReturnVO> findProductReturn(String productIds,String productRates,String periodCode);
	
	public AppInvestPortfolioForBuyVO findInvestPortfolioForBuy(String memberId,String holdId,String productIds,String productRates,String langCode,String toCurrency);
	
	public OrderPlan saveInvestPortfolioOrderDraft(String memberId,JSONObject holdObject,JSONObject planObject,JSONArray productArray,String ifaCheck,String ifaMemberId,String langCode);
	
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
	public List<OrderPlanProduct> findOrderPlanProduct(String planId, String accountNo);
	
	/**
	 * 获取 IFA AE账号
	 * @author zxtan
	 * @date 2017-02-14
	 */
	public String findAECode(String ifaId, String distributorId);
	
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
	 * @author zxtan
	 * @date 2017-02-26
	 */
	public OrderPlan saveInvestPortfolioOrderDraftForRebalance(String memberId,String portfolioHoldId,String orderPlanId,JSONArray productArray,String ifaCheck,String ifaMemberId,String langCode);
	
	/**
	 * 组合产品分配比例
	 * @param holdId
	 * @param planId
	 * @param langCode
	 * @return
	 */
	public AppInvestPortfolioForRebalanceVO findProductAllocationForRebalance(String holdId,String planId,String langCode);
	

	/**
	 * 组合确认 步骤展示列表
	 * @author zxtan
	 * @data 2017-03-01
	 */
	public AppInvestPortfolioForCheckVO findInvestPortfolioForCheck(String memberId,String planId,String langCode);
	
	
	/**
	 * 保存计划的审批记录
	 * @author zxtan
	 * @date 2017-03-21
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
	 * 删除组合购买计划的草稿
	 * @author zxtan
	 * @date 2017-03-21
	 * @param planId
	 * @param memberId
	 * @return
	 */
	public boolean deleteOrderPlanDraft(String planId, String memberId);
}
