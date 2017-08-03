/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import java.util.List;

import com.fsll.wmes.entity.MemberDistributor;


/***
 * 业务接口类：代理商
 * @author zxtan
 * @date 2016-10-21
 */
public interface MemberDistributorService {
	
	/**
	 * 根据Ids获取NameList
	 * @param idList id串，以逗号分隔
	 * @return 
	 */
	public String getNameList(String idList);
	
	/**
	 * 通过ID查询代理公司方法
	 * @return MemberDistributor
	 */
	public MemberDistributor findById(String id);
	
	/**
	 * 查询所有的代理公司方法
	 * @return list
	 */
	public List<MemberDistributor> findAllDistributors();
	
	/***/
}
