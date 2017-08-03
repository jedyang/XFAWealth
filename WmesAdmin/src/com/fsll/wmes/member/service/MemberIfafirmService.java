/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.service;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfafirm;

/**
 * 业务接口类
 * @author Yan
 * @date 2016-12-06
 */
public interface MemberIfafirmService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberIfafirm findById(String id);
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<MemberIfafirm> findAllMemberIfafirm(String langCode);
}
