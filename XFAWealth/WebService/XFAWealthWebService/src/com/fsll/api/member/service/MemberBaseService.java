/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.member.service;

import javax.servlet.http.HttpServletRequest;

import com.fsll.api.member.vo.MemberSsoVO;
import com.fsll.dao.entity.MemberBase;
import com.fsll.dao.entity.MemberCompany;
/***
 * 业务接口类：统一帐号管理
 * @author mjhuang
 * @date 2016-6-20
 */
public interface MemberBaseService {
	
	/***************  登录sso相关方法　begin  **********************/
	/**
	 * sso登录验证
	 * 
	 * @param request 请求
	 * @param loginCode 登录帐号
	 * @param password 密码
	 * @param fromType 来源：web,ios,android
	 * @param appCode app编码
	 * @return
	 */
	public MemberSsoVO saveLoginAuth(HttpServletRequest request,String loginCode,String password,String fromType,String appCode);
	
	/**
	 *校验token
	 *@author mjhuang
	 *@param tokenId 要校验的token
	 *@param fromType 来源：web,ios,android
	 *@return 0 参数错误  1成功 2令牌失效 3令牌不存在
	 */
	public String saveCheckSSOToken(String tokenId,String fromType);
	
	/**
	 *校验当前ip是否存在token
	 *@author mjhuang
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true存在 false不存在
	 */
	public boolean checkSSOExist(String fromIp,String fromType);
	
	/**
	 *校验帐号是否在其他机器有登录
	 *@author mjhuang
	 *@param loginCode 要检查来源的ip
	 *@param fromIp 来源 机器ip
	 *@param fromType 来源：web,ios,android
	 *@return true 在其他机器登录 false没有
	 */
	public boolean checkOtherSSOExist(String loginCode,String fromIp,String fromType);
	
	/**
	 * 检查指定用户loginCode是否存在
	 * @param loginCode
	 * @return
	 */
	public MemberBase checkIfExistsUserByLoginCode(String loginCode);
	
	/**
	 * 检查指定用户loginCode是否存在运营公司
	 * @param loginCode
	 * @return
	 */
	public MemberCompany checkIfValidUserByLoginCode(String loginCode);
	
	/**
	 * app启动时用的方法     获得系统中是否存在指定的token
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备ID
	 * @param token token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveDeviceToken(String appType,String deviceId,String token);
	
	/**
	 *  保存设备的Token与帐号的关联
	 * @param memberId 会员ID
	 * @param type 操作类型 0：保存帐号关联，1：去掉帐号关联
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备编号
	 * @param token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveTokenAccount(String memberId,String type,String appType,String deviceId,String token,String aliasType,String alias);
	
	/***************  登录sso相关方法　end  **********************/
}
