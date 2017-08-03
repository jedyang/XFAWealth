/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import com.fsll.wmes.entity.MemberAppToken;


/***
 * 业务接口类：设备令牌保存信息表
 * @author mjhuang
 * @date 2016-6-20
 */
public interface MemberAppTokenService {
	/**
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备ID
	 * @param token token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveDeviceToken(String appType,String deviceId,String token,String aliasType,String alias);
	
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
	
	/**
	 * 查找用户的app设备信息
	 * @author michael
	 * @param memberId
	 * @return
	 */
	public MemberAppToken findDeviceToken(String memberId);
}
