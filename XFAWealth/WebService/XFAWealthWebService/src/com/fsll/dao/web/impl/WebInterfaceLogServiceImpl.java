/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.web.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.dao.entity.WebInterfaceLog;
import com.fsll.dao.web.WebInterfaceLogService;
/***
 * Dao接口日志信息接口实现 
 * @author tan
 * @date 2016-8-4
 */
@Repository("webInterfaceLogService")
public class WebInterfaceLogServiceImpl extends BaseService implements WebInterfaceLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  WebInterfaceLog saveOrUpdate(WebInterfaceLog obj){
		boolean isAdd = false;
		if(null == obj.getId() || "".equals(obj.getId())){
			isAdd = true;
		}
		obj = init(obj);
		if(isAdd){
			obj = (WebInterfaceLog)baseDao.create(obj);
		}else{
			obj = (WebInterfaceLog)baseDao.update(obj);
		}
		return obj;
	}
	
	/**
	 * 初始化方法
	 * @param bondInfo
	 */
	private  WebInterfaceLog init(WebInterfaceLog obj){
		if(null == obj.getId()){
			obj.setId(null);
		}
		return obj;
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
					WebInterfaceLog obj = findById(id);
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
	public WebInterfaceLog findById(String id){
		WebInterfaceLog obj = (WebInterfaceLog) baseDao.get(WebInterfaceLog.class, id);
		return findDetail(obj);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param obj
	 * @return
	 */
	private WebInterfaceLog findDetail(WebInterfaceLog obj){
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
		String invokerType = q.optString("invokerType","");
		String resultFlag = q.optString("resultFlag","");
		String moduleType = q.optString("moduleType","");
		String startTimeTmp = q.optString("startTime","");
		String endTimeTmp = q.optString("endTime","");
		
		//2.组装查询sql
		List params = new ArrayList();
		String hql = "from WebInterfaceLog f  ";
		if(null!=invokerType&&!"".equals(invokerType)){
			hql+=" and r.invokerType = ? ";
			params.add(invokerType);
		}
		if(null!=resultFlag&&!"".equals(resultFlag)){
			hql+=" and r.resultFlag = ? ";
			params.add(resultFlag);
		}
		if(null!=moduleType&&!"".equals(moduleType)){
			hql+=" and r.moduleType = ? ";
			params.add(moduleType);
		}
		
		Date startTime = DateUtil.getDate(startTimeTmp, "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.getDate(endTimeTmp, "yyyy-MM-dd HH:mm:ss");;
		if (startTime!=null && (endTime==null)){
			hql += " and r.createTime >= ? ";
			params.add(startTime);
		}
		if (endTime!=null && (startTime==null)){
			hql += " and r.createTime <= ? ";
			params.add(endTime);
		}
		if(startTime!=null && endTime!=null){				
			hql += " and (r.createTime between ? and ?) ";
			params.add(startTime);
			params.add(endTime);
		}
		
		//3.返回查询结果
		jsonPaging= baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
}
