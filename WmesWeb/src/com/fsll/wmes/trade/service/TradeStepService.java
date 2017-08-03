/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderAip;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.ifafirm.vo.IfaMyTeamListVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.product.vo.ProductVO;
import com.fsll.wmes.trade.vo.OrderPlanProductVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;
import com.fsll.wmes.trade.vo.TransactionVO;

/**
 * 交易:分步管理业务接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeStepService {
	
	/**
	 * 获取 portfolio 产品持仓数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param
	 * @return
	 */
	public List<PortfolioHoldProduct> getPortfolioHoldProducts(String portfolioId);
	
	/**
	 * 获取组合可用的账号
	 * @author wwluo
	 * @date 2016-01-07
	 * @param 
	 * @return
	 */
	public List<InvestorAccountVO> findPortfolioHoldAccount(MemberBase member,MemberIfa memberIfa,String langCode,String toCurrency,String holdId);
	
	/**
	 * 获取开户RPQ风险等级
	 * @author wwluo
	 * @param relateId accountId
	 * @date 2017-02-07
	 */
	//public Integer getRPQRiskLevel(String relateId);
	
	/**
	 * 获取可用现金总金额
	 * @author wwluo
	 * @date 2017-02-07
	 */
	public Double getCashAvailableTotal(String memberId,String ifaId, String defCurrency,String holdId);
	
	/**
	 * 获取交易产品
	 * @author wwluo
	 * @date 2017-02-07
	 */
	public List<PlanProductVO> getOrderPlanProducts(String planId,String currencyCode, String langCode);
	
	/**
	 * 获取交易计划的产品列表
	 * @author mqzou
	 * @data 2016-10-14
	 * @param planId
	 * @return
	 */
	public List<OrderPlanProductVO> getOrderPlanProducts(String planId,String langCode);
	
	/**
	 * 保存交易订单
	 * @author wwluo
	 * @data 2016-10-17
	 * @param planId
	 * @param holdId
	 * @param currencyCode
	 * @param portfolioName
	 * @param orderPlanProducts
	 * @return 非空 成功,空 失败
	 */
	public String saveOrderPlan(MemberBase memberBase,String planId, String holdId, String currencyCode,String portfolioName,String orderPlanProducts);
	
	/**
	 * 保存交易计划股权选项信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	public void updateOrderPlanProduct(String planId,String subscriptionDatas,String currencyCode);
	
	/**
	 * 交易金额汇总（计算 order_plan.total_buy、order_plan.total_sell）
	 * @author wwluo
	 * @data 2017-02-27
	 * @param 
	 * @return
	 */
	public OrderPlan saveTotalAmountSummary(String planId);
	
	/**
	 * 下单成功后发送待办待阅
	 * @param memberBase
	 * @param planId
	 * @param supervisorId
	 * @param langCode
	 * @param ifaIds
	 */
	public void saveSendToReadForOrderPlan(MemberBase memberBase,String planId,String supervisorId,String langCode,Set<String> ifaIds);
	
	/**
	 * 获取定投计划（执行中）
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	public OrderAip getOrderAipByPlan(String planId);
	
	/**
	 * 获取定投信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	public OrderPlanAip getOrderPlanAipByOrderId(String planId);
	
	/**
	 * 保存定投信息
	 * @param request
	 * @param memberBase
	 * @return
	 */
	public boolean saveAip(HttpServletRequest request,MemberBase memberBase);
	
	/**
	 * 获取我的团队上司
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	public List<IfaMyTeamListVO> getMySupervisorByMemberId(String memberId,String langCode);
	
	/**
	 * 封装交易页面IFA展示信息
	 * @author wwluo
	 * @data 2017-05-11
	 * @param currencyCode
	 * @param langCode
	 * @param loginUser
	 * @param orderPlan
	 * @return
	 */	
	public TransactionVO getTransactionVOForIfa(String currencyCode, String langCode,
			MemberBase loginUser, OrderPlan orderPlan);

	/**
	 * 封装交易页面Investor展示信息
	 * @author wwluo
	 * @data 2017-05-11
	 * @param currencyCode
	 * @param langCode
	 * @param loginUser
	 * @param orderPlan
	 * @return
	 */
	public TransactionVO getTransactionVOForInvestor(String currencyCode, String langCode,
			MemberBase loginUser, OrderPlan orderPlan);

	/**
	 * 根据ids获取产品VO
	 * @author wwluo
	 * @data 2017-05-11
	 * @param ids 基金ID
	 * @param memberId 当前登录member ID
	 * @param currencyCode 货币编码
	 * @param langCode 多语言编码
	 * @return
	 */
	public List<PlanProductVO> getPlanProductVOByIds(String ids,String memberId, String currencyCode, String langCode);

	/**
	 * 根据ids获取产品VO
	 * @author wwluo
	 * @data 2017-05-11
	 * @param loginUser
	 * @param planId 
	 * @param currencyCode 货币编码
	 * @param portfolioName 组合名称
	 * @param productData 产品信息
	 * @return planId
	 */
	public String saveOrderPlan(MemberBase loginUser, String planId,
			String currencyCode, Integer riskLevel, String portfolioName, String productData);

	/**
	 * 获取交易账号关联的IFA ID
	 * @author wwluo
	 * @data 2017-05-11
	 * @param planId 
	 * @return 
	 */
	public Set<String> getOrderIfas(String planId);
	
	/**
	 * 往OrderPlan插入基金
	 * @author wwluo
	 * @date 2017-02-20
	 * @param planId
	 * @param fundIds
	 */
	public void addPlanProduct(String palnId, String fundIds);
	
	/**
	 * 获取产品代理商关系表
	 * @author wwluo
	 * @date 2017-02-20
	 * @param memberId
	 * @param productId
	 */
	public ProductDistributor getProductDistributorByMemberIdAndProductId(String memberId,String productId);
	
}
