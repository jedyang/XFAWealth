/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.trade.vo.AipOrderHistoryVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;

/**
 * 交易:交易记录业务接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
public interface TradeRecordService {
	/**
	 * 获取ifa的交易记录
	 * @author wwluo
	 * @date 2016-11-25
	 * @param loginUser
	 * @param filter 列表筛选条件
	 * @return
	 */
	public JsonPaging getTradeRecord(MemberBase loginUser,JsonPaging jsonPaging,TransactionRecordFilterVO filter);
	
	/**
	 * 获取执行中的定投计划
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	public JsonPaging getAipTask(MemberBase loginUser, JsonPaging jsonPaging,TransactionRecordFilterVO filter);
	
	/**
	 * 删除交易计划,逻辑删除
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	public void delOrderPlan(String id);
	
	/**
	 * 获取定投交易记录
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	public List<AipOrderHistoryVO> getAipOrderHistory(String planId,
			String currencyCode, String langCode);
}
