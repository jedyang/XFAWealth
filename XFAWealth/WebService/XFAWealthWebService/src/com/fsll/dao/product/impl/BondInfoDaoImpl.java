/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.dao.CommonDao;
import com.fsll.common.util.JsonPaging;
import com.fsll.dao.entity.BondInfo;
import com.fsll.dao.product.BondInfoDao;

/**
 * Dao债券信息接口实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-19
 */
@Repository("bondInfoDao")
public class BondInfoDaoImpl extends CommonDao implements BondInfoDao {
	
	/**
	 * 增加或者修改一条数据
	 * @param obj
	 */
	public  BondInfo saveOrUpdate(BondInfo obj){
		boolean isAdd = false;
		if(null == obj.getId() || "".equals(obj.getId())){
			isAdd = true;
		}
		obj = init(obj);
		if(isAdd){
			obj = (BondInfo)baseDao.create(obj);
		}else{
			obj = (BondInfo)baseDao.update(obj);
		}
		return obj;
	}
	
	/**
	 * 初始化方法
	 * @param bondInfo
	 */
	private  BondInfo init(BondInfo obj){
		if(null == obj.getId()){
			obj.setId(null);
			obj.setIsValid("1");
		}
		return obj;
	}
	
	/**
	 * 修改数据的状态
	 * @param ids
	 * @param status
	 */
	public  boolean saveStatus(String ids,String status){
		if (ids != null && !ids.equals("")) {
			if(ids.endsWith(","))ids = ids.substring(0,ids.length()-1);
			if (ids != null && !ids.equals("")) {
				String hql = "update BondInfo v set v.isValid = ? where v.id in ("+this.getNewStrId(ids)+") ";
				List<Object> params = new ArrayList<Object>();
				params.add(status);
				this.baseDao.updateHql(hql, params.toArray());
			}
		}
		return true;
	}
	
	/**
	 * 通过ID删除记录
	 * @param ids
	 * @return
	 */
	public boolean deleteById(String ids){
		if (ids != null && !ids.equals("")) {
			if(ids.endsWith(","))ids = ids.substring(0,ids.length()-1);
			String tmpStr = ids;
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				if (!"".equals(id)) {
					BondInfo obj = findById(id);
					if(obj != null){
						deleteRelate(id);//先删除关联信息
						baseDao.delete(obj);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 若是停用或者删除，相关数据也要停用或者删除
	 */
	private  boolean deleteRelate(String id){
		
		return true;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public BondInfo findById(String id){
		BondInfo obj = (BondInfo) baseDao.get(BondInfo.class, id);
		return findDetail(obj);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param obj
	 * @return
	 */
	private BondInfo findDetail(BondInfo obj){
		return obj;
	}
	
	/**
	 * 按条件搜索列表
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findList(JsonPaging jsonPaging,JSONObject q){
		//1.获取查询字段
		String langCode = q.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		//2.组装查询sql
		String hql = "from BondInfo f inner join "+this.getLangString("BondInfo", langCode)+" s on s.id=f.id ";
		hql += " where f.isValid=? order by f.lastUpdate desc ";
		List params = new ArrayList();
		params.add("1");
		
		//3.返回查询结果
		jsonPaging= baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 通过产品ID查找一条数据
	 * @param pruductId
	 * @return
	 */
	public BondInfo findByPruductId(String pruductId){
		String hql = "from BondInfo f where f.product.id = ? ";
		List params = new ArrayList();
		params.add(pruductId);
		List<BondInfo> list = baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			return findDetail(list.get(0));
		}
		return null;
	}
	
}
