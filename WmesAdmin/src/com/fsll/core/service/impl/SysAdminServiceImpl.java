/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysAdminService;

/**
 * 业务实现:用户管理
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
@Service("SysAdminService")
//@Transactional
public class SysAdminServiceImpl extends BaseService implements SysAdminService {
	
	/**
	 * 增加或者修改一条数据
	 * @param admin 
	 * @return 
	 */
	public  SysAdmin saveOrUpdate(SysAdmin admin,SysAdmin loginUser){
		SysAdmin adminTmp = admin;
		if(null == adminTmp.getId() || "".equals(adminTmp.getId())){
			adminTmp.setId(null);
			adminTmp = (SysAdmin)baseDao.create(adminTmp);
		}else{
			adminTmp = (SysAdmin)baseDao.update(adminTmp);
		}
		return adminTmp;
	}
	
	/**
	 * 修改数据的状态
	 * @param ids
	 * @param status
	 */
	public  boolean saveIsValid(String ids,String isValid){
		if (StringUtils.isNotBlank(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				String hql = "update SysAdmin v set v.isValid = ? where v.id =  ? ";
				List<Object> params = new ArrayList<Object>();
				params.add(isValid);
				params.add(id); 
				this.baseDao.updateHql(hql, params.toArray());
			}
		}
		return true;
	}
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	//private  void deleteRelate(Long id){
		
	//}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			SysAdmin admin = findById(id);
			deleteRelate(id);//先删除关联信息
			baseDao.delete(admin);
			return true;
		}
	}
	
	/**删除关联信息*/
	public void deleteRelate(String id){
		String hql = "delete from SysUsergroupAdmin t where t.admin.id=?";
		this.baseDao.updateHql(hql, new String[]{id});
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (StringUtils.isNotBlank(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				deleteById(id);
			}
		}
		return true;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public SysAdmin findById(String id){
		SysAdmin admin = (SysAdmin) baseDao.get(SysAdmin.class, id);
		return admin;
	}
	
	/**
	 * 查找所有的数据
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging findByPage(JsonPaging jsonPaging,SysAdmin admin,String searchKey){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from SysAdmin r where 1=1 ");
		if(null != admin && StringUtils.isNotBlank(admin.getIsValid())){
			hql.append(" and r.isValid = ? ");
			params.add(admin.getIsValid());
		}
		if(StringUtils.isNotBlank(searchKey)){
			hql.append(" and ( r.loginCode like ? or r.nickName like ? ) ");
			params.add("%"+searchKey+"%");
			params.add("%"+searchKey+"%");
		}
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , true);
		return jsonPaging;
	}
	
	/**
	 * 通过用户帐号查找用户
	 * @param loginCode 用户帐号
	 * @return
	 */
	//@Transactional(readOnly = true)
	public SysAdmin findByCode(String loginCode) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from SysAdmin u where (u.loginCode = ? or u.loginCode = ?) ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		List<SysAdmin> admins = this.baseDao.find(hql, params.toArray(), true);
		if (!admins.isEmpty()) {
			return admins.get(0);
		}
		return null;
	}
	
	/**
	 * 通过用户用户帐号和密码查看用户
	 * @param loginCode 用户帐号
	 * @param password 用户密码
	 * @return
	 */
	//@Transactional(readOnly = true)
	public SysAdmin checkExist(String loginCode, String password) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from SysAdmin u where u.isValid='1' and (u.loginCode = ? or u.loginCode = ?) and u.password = ? ";
		params.add(loginCode.toUpperCase());
		params.add(loginCode.toLowerCase());
		params.add(password);
		List<SysAdmin> admins = this.baseDao.find(hql, params.toArray(), false);
		if (!admins.isEmpty()) {
			return admins.get(0);
		}
		return null;
	}
	
	/**
	 * 通过用户用户帐号和密码查看用户
	 * @param loginCode 用户帐号
	 * @param adminId 用户id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public SysAdmin checkUnique(String loginCode, String adminId) {
		List<Object> params = new ArrayList<Object>();
		String hql = "from SysAdmin u where u.isValid='1' and u.loginCode = ? ";
		params.add(loginCode);
		if(null!=adminId && !"".equals(adminId)){
			hql += "and u.id<>? ";
			params.add(adminId);
		}
		List<SysAdmin> admins = this.baseDao.find(hql, params.toArray(), false);
		if (!admins.isEmpty()) {
			return admins.get(0);
		}
		return null;
	}
	
	/**
	 * 验证密码
	 * */
	@Override
	public boolean checkOraginPsw(String id, String oldPassword) {
		String hql = "from SysAdmin t where t.id=?";
		List<SysAdmin> list = this.baseDao.find(hql, new String[]{id}, false);
		if(list.isEmpty()){
			return false;
		}
		SysAdmin loginUser = list.get(0);
		if(oldPassword.equals(loginUser.getPassword())){
			return true;
		}
		return false;
	}

}
