/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysAdmin;

/**
 * 业务接口:用户管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public interface SysAdminService {
	
	/**
	 * 增加或者修改一条数据
	 * @param account 
	 * @return 
	 */
	public  SysAdmin saveOrUpdate(SysAdmin account,SysAdmin loginUser);
	
	/**
	 * 修改数据的状态
	 * @param ids
	 * @param status
	 */
	public  boolean saveIsValid(String ids,String isValid);
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public SysAdmin findById(String id);
	
	/**
	 * 查找所有的数据
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging findByPage(JsonPaging jsonPaging,SysAdmin admin,String searchKey);
	
	/**
	 * 通过用户帐号查找用户
	 * @param loginCode 用户帐号
	 * @return
	 */
	public SysAdmin findByCode(String loginCode);
	/**
	 * 通过用户用户帐号和密码查看用户
	 * @param loginCode 用户帐号
	 * @param password 用户密码
	 * @return
	 */
	public SysAdmin checkExist(String loginCode, String password);
	
	/**
	 * 通过用户用户帐号和密码查看用户
	 * @param loginCode 用户帐号
	 * @param accountId 用户id
	 * @return
	 */
	public SysAdmin checkUnique(String loginCode, String accountId);
	
	/**
	 * 验证密码
	 * */
	public boolean checkOraginPsw(String id, String oldPassword);


	
}
