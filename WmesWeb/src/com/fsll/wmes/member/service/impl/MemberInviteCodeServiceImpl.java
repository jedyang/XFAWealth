/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberInviteCode;
import com.fsll.wmes.member.service.MemberInviteCodeService;

/***
 * 业务接口实现类：代理商
 * @author zxtan
 * @date 2016-10-21
 */
@Service("inviteCodeService")
//@Transactional
public class MemberInviteCodeServiceImpl extends BaseService implements MemberInviteCodeService {

	/**
	 * 保存或更新发送邀请码信息
	 */
	@Override
	public MemberInviteCode saveOrUpdate(MemberInviteCode code) {
		return (MemberInviteCode)this.baseDao.saveOrUpdate(code);
	}

	/**
	 * 判断邀请码是否存在
	 * @author Yan
	 * @param inviteCode
	 * @param email
	 * @return
	 */
	@Override
	public MemberInviteCode checkInviteCode(String inviteCode, String email) {
		String hql = " FROM MemberInviteCode c WHERE c.isValid=1 ";
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(inviteCode) && StringUtils.isNotBlank(email)){
			hql += " AND c.inviteCode=? AND c.toEmail=? ORDER BY c.createTime DESC";
			params.add(inviteCode);
			params.add(email);
			List list = this.baseDao.find(hql, params.toArray(), false);
			if(!list.isEmpty()){
				return (MemberInviteCode)list.get(0);
			}
		}
		return null;
	}

	/**
	 * 邀请码列表查询
	 * @author Yan
	 * @param jsonpaging
	 * @param code
	 * @return
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonpaging, MemberInviteCode code) {
		String hql=" FROM MemberInviteCode r WHERE r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		if(null!=code.getMember()&&StringUtils.isNotBlank(code.getMember().getId())){
			hql+=" AND r.member.id = ? ";
			params.add(code.getMember().getId());
		}
		if(StringUtils.isNotBlank(code.getToEmail())||StringUtils.isNotBlank(code.getInviteCode())){
			hql += " AND ( r.toEmail LIKE ? OR r.inviteCode LIKE ? ) ";
			params.add("%"+code.getToEmail()+"%");
			params.add("%"+code.getInviteCode()+"%");
		}
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
    	while(it.hasNext()){
			MemberInviteCode obj = (MemberInviteCode)it.next();
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
}
