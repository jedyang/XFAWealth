/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberInviteCode;
import com.fsll.wmes.entity.WebEmailLog;


/***
 * 业务接口类
 * @author Yan
 * @date 2016-10-21
 */
public interface MemberInviteCodeService {
	
	/**
	 * 保存或更新发送邀请码信息
	 */
	public MemberInviteCode saveOrUpdate(MemberInviteCode code);
	
	/**
	 * 判断邀请码是否存在
	 * @author Yan
	 * @param inviteCode
	 * @param email
	 * @return
	 */
	public MemberInviteCode checkInviteCode(String inviteCode, String email);

	/**
	 * 邀请码列表查询
	 * @author Yan
	 * @param jsonpaging
	 * @param code
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, MemberInviteCode code);
}
