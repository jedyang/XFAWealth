/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.MemberAppToken;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.service.MemberAppTokenService;
/***
 * 业务接口实现类：设备令牌保存信息表
 * @author mjhuang
 * @date 2016-6-20
 */
@Service("memberAppTokenService")
//@Transactional
public class MemberAppTokenServiceImpl extends BaseService implements MemberAppTokenService {
	
	/**
	 * @param appType 平台类型 1:android   2:ios 
	 * @param deviceId 设备ID
	 * @param token token 设备token值
	 * @param aliasType 帐号
	 * @param alias 帐号所在组
	 */
	public void saveDeviceToken(String appType,String deviceId,String token,String aliasType,String alias){
		String hql = "from MemberAppToken t where t.token=? ";
		List params = new ArrayList();
		params.add(token);
		List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MemberAppToken obj = list.get(0);
			obj.setLastUpdate(new Date());
			baseDao.saveOrUpdate(obj, false);
		}else{
			MemberAppToken appToken = new MemberAppToken();
			appToken.setAppType(appType);
			appToken.setDeviceId(deviceId);
			appToken.setDeviceToken(token);
			appToken.setAliasType(aliasType);
			appToken.setAlias(alias);
			appToken.setCreateTime(new Date());
			baseDao.create(appToken);
		}
	}
	
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
	public void saveTokenAccount(String memberId,String type,String appType,String deviceId,String token,String aliasType,String alias){
		String hql = "from MemberAppToken t where t.token=? ";
		List params = new ArrayList();
		params.add(token);
		List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		MemberBase member=new MemberBase();
		member.setId(memberId);
		if(null!=list && !list.isEmpty()){
			MemberAppToken obj = list.get(0);
			if("0".equals(type)){
				obj.setMemberId(memberId);
			}else{
				obj.setMemberId(null);
			}
			obj.setLastUpdate(new Date());
			baseDao.saveOrUpdate(obj, false);
		}else{
			if("0".equals(type)){
				MemberAppToken appToken = new MemberAppToken();
				appToken.setAppType(appType);
				appToken.setDeviceId(deviceId);
				appToken.setDeviceToken(token);
				appToken.setAliasType(aliasType);
				appToken.setAlias(alias);
				appToken.setMemberId(memberId);
				appToken.setCreateTime(new Date());
				appToken.setLastUpdate(new Date());
				baseDao.create(appToken);
			}
		}
	}
	
	/**
	 * 查找用户的app设备信息
	 * @author michael
	 * @param memberId
	 * @return
	 */
	public MemberAppToken findDeviceToken(String memberId){
		String hql = "from MemberAppToken t where t.memberId=? order by lastUpdate desc ";
		List params = new ArrayList();
		params.add(memberId);
		List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}
