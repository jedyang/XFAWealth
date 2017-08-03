/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.service;
import javax.servlet.http.HttpServletRequest;

import com.fsll.dao.entity.MemberAccountSso;
import com.fsll.oms.vo.OmsAddOrderVO;
import com.fsll.oms.vo.OmsDelOrderVO;
/**
 * itrader消息发送方法
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-26
 */
public interface ITraderSendService {
	
	/**
	 * 登录指令方法
	 * @param type 0是AE或者ifa登录 1是investor登录
	 * @param code
	 * @param pwd
	 * @return true 成功 false失败
	 */
	public MemberAccountSso saveLogin(HttpServletRequest request,String type,String code,String pwd);
	
	/**
	 * 检验Pin码的方法
	 * @param type 0是AE或者ifa登录 1是investor登录
	 * @param code ae_code或者account_code
	 * @param pinValue 要校验的pin码值,多个之间用,分隔
	 * @return true 成功 false失败
	 */
	public boolean savePin(HttpServletRequest request,String type,String code,String pinValue);
	
	/**
	 * 登出指令方法
	 * @param type
	 * @param code
	 * @return true 成功 false失败
	 */
	public boolean saveLogout(HttpServletRequest request,String type,String code);
	
	/**
	 * 新增订单
	 * @param orderVO 订单
	 * @return true 成功 false失败
	 */
	public boolean addOrder(HttpServletRequest request,OmsAddOrderVO orderVO);
	
	/**
	 * 取消订单
	 * @param orderVO
	 * @return true 成功 false失败
	 */
	public boolean deleteOrder(HttpServletRequest request,OmsDelOrderVO orderVO);
	
}
