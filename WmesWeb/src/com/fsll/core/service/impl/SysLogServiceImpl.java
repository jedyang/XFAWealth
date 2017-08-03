/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysLog;
import com.fsll.core.service.SysLogService;
/***
 * 业务接口实现类：操作日志管理
 * @author mjhuang
 * @date 2016-6-20
 */
@Service("sysLogService")
public class SysLogServiceImpl extends BaseService implements SysLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  SysLog saveOrUpdate(SysLog log){
		//boolean isAdd = false;
		if(null == log.getId() || "".equals(log.getId())){
			log.setId(null);
			log.setCreateTime(new Date());
			log = (SysLog)baseDao.create(log);
			//isAdd = true;
		}else{
			log = (SysLog)baseDao.update(log);
			//isAdd = false;
		}
		//if(isAdd == true){
		//	throw new RuntimeException("主动抛出异常");
		//}
		return log;
	}
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	private  void deleteRelate(String id){

	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			SysLog log = findById(id);
			if(log != null){
				deleteRelate(id);
				baseDao.delete(log);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				boolean result = deleteById(id);
				if(!result){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param CompanyInfo
	 * @return
	 */
	private SysLog findDetail(SysLog log){
		
		return log;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public SysLog findById(String id){
		SysLog log = (SysLog) baseDao.get(SysLog.class, id);
		return findDetail(log);
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonpaging,SysLog log) {
		String hql=" from SysLog r where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=log.getLoginCode()&&!"".equals(log.getLoginCode())){
			hql+=" and ( r.loginCode like ? or  r.nickName like ?  )";
			params.add("%"+log.getLoginCode()+"%");
			params.add("%"+log.getLoginCode()+"%");
		}
		if(null!=log.getModuleType()&&!"".equals(log.getModuleType())){
			hql+=" and r.moduleType = ? ";
			params.add(log.getModuleType());
		}
		//时间段的查询 开始
		String startTimeTmp = log.getStartTime();
		String endTimeTmp = log.getEndTime();
//		if(null!=_startTime && !"".equals(_startTime)){
//			_startTime = _startTime + " 00:00:00";
//		}
//		if(null!=_endTime &&!"".equals(_endTime)){
//			_endTime = _endTime + " 23:59:59";
//		}	
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
		//时间段的查询 结束
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			SysLog obj = (SysLog)it.next();
			obj = findDetail(obj);
			obj.setXh(Integer.toString(indexNumber+index));
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
}
