/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.member.service.MemberIfafirmService;

/***
 * 业务接口实现类：代理商
 * @author zxtan
 * @date 2016-10-21
 */
@Service("memberIfafirmService")
//@Transactional
public class MemberIfafirmServiceImpl extends BaseService implements MemberIfafirmService {
	
	/**
	 * 通过ID查询代理公司方法
	 * @return MemberIfafirm
	 */
	@Override
	public MemberIfafirm findById(String id) {
		MemberIfafirm info = (MemberIfafirm)this.baseDao.get(MemberIfafirm.class, id);
		return info;
	}
	
	/**
	 * 获取名称
	 * @param langCode
	 * @return
	 */
	public String getName(String id, String langCode){
		String name = "";
		String hql = " FROM "+this.getLangString("MemberIfafirm", langCode)+" f "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(StringUtils.isNotBlank(id)){
			hql+=" AND f.id=? ";
			params.add(id);
		}
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty() && "sc".equals(langCode)){
			MemberIfafirmSc sc = (MemberIfafirmSc)list.get(0);
			name = sc.getCompanyName();
		} else if(!list.isEmpty() && "tc".equals(langCode)){
			MemberIfafirmTc tc = (MemberIfafirmTc)list.get(0);
			name = tc.getCompanyName();
		} else if(!list.isEmpty() && "en".equals(langCode)){
			MemberIfafirmEn en = (MemberIfafirmEn)list.get(0);
			name = en.getCompanyName();
		}
		return name;
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
