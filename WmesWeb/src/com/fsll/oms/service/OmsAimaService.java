/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms.service;

import javax.servlet.http.HttpServletRequest;

import com.fsll.oms.vo.OmsAimaLogonVO;

/**
 * oms aima的接口
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-26
 */
public interface OmsAimaService {
	/**
	 * 登录Aima方法
	 * @param code
	 * @param pwd
	 * @return true 成功 false失败
	 */
	public OmsAimaLogonVO saveLogin(HttpServletRequest request,String code,String pwd);
	
	/**
	 * 修改密码
	 * @param 修改人登录oms系统的 code
	 * @param 修改人登录oms系统的 密码
	 * @param 修改目标的用户类型 userType 0=ifa  1=investor
	 * @param 修改目标的code  targetCode
	 * @return 
	 */
	public boolean saveModPwd(HttpServletRequest request,String code,String pwd,String userType,String targetCode);
	
	/**
	 * 修改pin码
	 * @param 修改人登录oms系统的 code
	 * @param 修改人登录oms系统的 密码
	 * @param 修改目标的用户类型 userType 0=ifa  1=investor
	 * @param 修改目标的code  targetCode
	 * @return 
	 */
	public boolean saveModPin(HttpServletRequest request,String code,String pwd,String userType,String targetCode);
	
	/**
	 * 查询多币种帐户资产
	 * @param code ae_code或者account_code
	 * @return 
	 */
	//public List<InvestorAccountCurrency> saveCurrencyAccount(HttpServletRequest request,String code);
	
}
