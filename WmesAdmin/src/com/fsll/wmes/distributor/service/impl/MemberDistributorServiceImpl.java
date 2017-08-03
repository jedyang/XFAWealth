package com.fsll.wmes.distributor.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * 代理商信息实现类
 * @author Yan
 * @date 2016-12-08
 */
@Service("memberDistributorService")
//@Transactional
public class MemberDistributorServiceImpl extends BaseService implements MemberDistributorService {

	/**
	 * 通过ID查询代理公司方法
	 * @return MemberDistributor
	 */
	@Override
	public MemberDistributor findById(String id) {
		MemberDistributor info = (MemberDistributor)this.baseDao.get(MemberDistributor.class, id);
		return info;
	}
	
	/**
	 * 查询所有的代理公司方法
	 * @return list
	 */
	@Override
	public List<MemberDistributor> findAllDistributors() {
		String hql="from MemberDistributor ";
		List<MemberDistributor> list=this.baseDao.find(hql, null, false);
		return list;
	}

}
