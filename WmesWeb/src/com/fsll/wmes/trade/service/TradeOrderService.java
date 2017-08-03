/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderReturn;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionAccountVO;

/**
 * 交易:下单信息接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeOrderService {
	
	
	/**
	 * 获取history交易账号,根据memberType返回investor账号或者ifa AE账号
	 * @author wwluo
	 * @date 2016-11-30
	 */
	public List<TransactionAccountVO> getHistoryAccountByhistoryId(String historyId, Integer memberType);
	
	/**
	 * 获取交易账号,根据memberType返回investor账号或者ifa AE账号
	 * @author wwluo
	 * @date 2016-11-30
	 */
	public List<TransactionAccountVO> getOrderPlanAccounts(String planId, Integer memberType,String planProductId);
	
	/**
	 * 发送oms下单
	 * @param loginUser
	 * @param ssoVo
	 * @param langCode
	 * @param planId
	 * @param accountData
	 */
	public boolean addOrderPlan(HttpServletRequest request,MemberBase loginUser,String langCode,String planId,String accountData);
	
	/**
	 * 撤单操作
	 * @param request
	 * @param loginUser
	 * @param historyId
	 * @param password
	 * @return
	 */
	public boolean deleteOrder(HttpServletRequest request,MemberBase memberBase,String historyId,String password);
	
	/**
	 * 获取 IFA AE账号
	 * @author wwluo
	 * @date 2016-12-26
	 */
	public String findAECode(String ifaId, String distributorId);
	
	/**
	 * 获取一条交易历史记录
	 * @author wwluo
	 * @date 2017-02-07
	 * @param planId orderPlan id
	 * @param productId 产品id
	 * @param tranType 交易类型
	 */
	public OrderHistory getOrderHistory(String planId, String productId, String tranType);

	/**
	 * 获取交易详情产品(提交OMS后)
	 * @author wwluo
	 * @date 2017-02-15
	 */
	public List<PlanProductVO> getHistoryProducts(String planId, String currencyCode,String langCode);
	
	/**
	 * 获取一条OrderReturn信息
	 * @author wwluo
	 * @date 2017-02-15
	 */
	public OrderReturn getOrderReturnByHistoryId(String historyId);
	
}
