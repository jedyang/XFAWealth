/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.trade.vo.OrderPlanDetailVO;

/**
 * 交易:审批信息接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeCheckService {
	
	
	/**
	 * 交易操作投资人确认操作（同意、退回）
	 * @param loginUser
	 * @param planId
	 * @param finishStatus
	 * @return
	 */
	public boolean saveConfirmOrderPlan(MemberBase loginUser, String planId, String finishStatus, String langCode);
	
	/**
	 * 获取交易计划详情（提交OMS前）
	 * @param loginUser
	 * @param plan
	 * @param langCode
	 * @param currencyCode
	 * @return
	 */
	public OrderPlanDetailVO getOrderPlanProducts(MemberBase loginUser,OrderPlan plan, String langCode, String currencyCode);
	
	/**
	 * 交易计划审批
	 * @param memberBase
	 * @param langCode
	 * @param planId
	 * @param state
	 * @param opinon
	 * @param checkIp
	 * @return
	 */
	public boolean saveApprovalPlan(MemberBase memberBase,String langCode,String planId,String state,String opinon,String checkIp);
}
