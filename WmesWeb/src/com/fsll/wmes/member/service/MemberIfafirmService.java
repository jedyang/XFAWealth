/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import java.util.List;

import com.fsll.wmes.entity.MemberIfafirm;


/***
 * 业务接口类
 * @author Yan
 * @date 2016-10-21
 */
public interface MemberIfafirmService {
	
	/**
	 * 通过ID查询代理公司方法
	 * @return MemberIfafirm
	 */
	public MemberIfafirm findById(String id);

	/**
	 * 获取名称
	 * @param langCode
	 * @return
	 */
	public String getName(String id,String langCode);
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<MemberIfafirm> findAllMemberIfafirm(String langCode);
}
