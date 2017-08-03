/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebInterfaceLog;
import com.fsll.wmes.web.service.WebInterfaceLogService;
/***
 * 业务接口实现类：操作日志管理
 * @author tan
 * @date 2016-8-4
 */
@Service("webInterfaceLogService")
//@Transactional
public class WebInterfaceLogServiceImpl extends BaseService implements WebInterfaceLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  WebInterfaceLog saveOrUpdate(WebInterfaceLog log){
		if(null == log.getId() || "".equals(log.getId())){
			log.setId(null);
			log.setCreateTime(new Date());
			log = (WebInterfaceLog)baseDao.create(log);
		}else{
			log = (WebInterfaceLog)baseDao.update(log);
		}
		return log;
	}
	
	
	/**
	 * 获得一条记录的详细信息
	 * @param 
	 * @return
	 */
	private WebInterfaceLog findDetail(WebInterfaceLog log){
		
		return log;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly=true)
	public WebInterfaceLog findById(String id){
		WebInterfaceLog log = (WebInterfaceLog) baseDao.get(WebInterfaceLog.class, id);
		return findDetail(log);
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,WebInterfaceLog log) {
		String hql=" from WebInterfaceLog r where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=log.getInvokerType()&&!"".equals(log.getInvokerType())){
			hql+=" and r.invokerType = ? ";
			params.add(log.getInvokerType());
		}
		if(null!=log.getResultFlag()&&!"".equals(log.getResultFlag())){
			hql+=" and r.resultFlag = ? ";
			params.add(log.getResultFlag());
		}
		if(null!=log.getModuleType()&&!"".equals(log.getModuleType())){
			hql+=" and r.moduleType LIKE ? ";
			params.add("%"+log.getModuleType()+"%");
		}
		//时间段的查询 开始
		String getStartTime = log.getStartTime();
		String getEndTime = log.getEndTime();
//		if(null!=_startTime && !"".equals(_startTime)){
//			_startTime = _startTime + " 00:00:00";
//		}
//		if(null!=_endTime &&!"".equals(_endTime)){
//			_endTime = _endTime + " 23:59:59";
//		}	
		Date startTime = DateUtil.getDate(getStartTime, "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.getDate(getEndTime, "yyyy-MM-dd HH:mm:ss");;
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

		return jsonpaging;
	}
}
