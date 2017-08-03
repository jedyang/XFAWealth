/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.member.service.MemberIfafirmService;

/**
 * 业务接口实现类
 * @author Yan
 * @date 2016-12-06
 */
@Service("memberIfafirmService")
//@Transactional
public class MemberIfafirmServiceImpl extends BaseService implements MemberIfafirmService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	@Override
	public MemberIfafirm findById(String id) {
		MemberIfafirm info = (MemberIfafirm) baseDao.get(MemberIfafirm.class, id);
		return info;
	}
	
	/**
	 * 查找所有
	 * @return
	 */
	@Override
	public List<MemberIfafirm> findAllMemberIfafirm(String langCode) {
		String hql="FROM MemberIfafirm r ";
		hql += " INNER JOIN " + this.getLangString("MemberIfafirm",langCode);
		hql += " l ON r.id=l.id";
		List<MemberIfafirm> list=this.baseDao.find(hql, null, false);
		return list;
	}

}
