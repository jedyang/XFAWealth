package com.fsll.wmes.distributor.service;

import java.util.List;

import com.fsll.wmes.entity.MemberDistributor;

/**
 * 代理商信息接口
 * @author Yan
 * @date 2016-12-08
 */
public interface MemberDistributorService {
	
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
}
