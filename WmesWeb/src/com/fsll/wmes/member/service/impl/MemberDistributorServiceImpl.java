/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;


import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.member.service.MemberDistributorService;

/***
 * 业务接口实现类：代理商
 * @author zxtan
 * @date 2016-10-21
 */
@Service("memberDistributorService")
//@Transactional
public class MemberDistributorServiceImpl extends BaseService implements MemberDistributorService {

	@Override
	public String getNameList(String idList) {
		// TODO Auto-generated method stub
		String nameList = "";
		String idString = StrUtils.seperateWithQuote(idList,"'");
		String hql = " from MemberDistributor l where l.id in ("+idString+") ";
		List<MemberDistributor> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			for (MemberDistributor item : list) {
				if("".equals(nameList) ){
					nameList = item.getCompanyName();
				}else {
					nameList += "," + item.getCompanyName();
				}				
			}
		}
				
		return nameList;
	}
	
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
