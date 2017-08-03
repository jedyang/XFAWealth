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
import com.fsll.wmes.entity.WebBusLog;
import com.fsll.wmes.web.service.WebBusLogService;

/***
 * 业务接口实现类：操作日志管理
 * @author tan
 * @date 2016-8-4
 */
@Service("webBusLogService")
//@Transactional
public class WebBusLogServiceImpl extends BaseService implements WebBusLogService {
	/**
	 * 增加或者修改一条数据
	 * @param log 日志信息
	 * @return 
	 */
	public  WebBusLog saveOrUpdate(WebBusLog log){
		if(null == log.getId() || "".equals(log.getId())){
			log.setId(null);
			log.setCreateTime(new Date());
			log = (WebBusLog)baseDao.create(log);
		}else{
			log = (WebBusLog)baseDao.update(log);
		}
		return log;
	}
	
	
	/**
	 * 获得一条记录的详细信息
	 * @param 
	 * @return
	
	private WebBusLog findDetail(WebBusLog log){
		return log;
	} */
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly=true)
	public WebBusLog findById(String id){
		WebBusLog log = (WebBusLog) baseDao.get(WebBusLog.class, id);
		return log;
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param log 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,WebBusLog log) {
		String hql=" from WebBusLog r where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=log.getLoginCode()&&!"".equals(log.getLoginCode())){
			hql+=" and r.loginCode LIKE ? ";
			params.add("%"+log.getLoginCode()+"%");
		}
		if(null!=log.getModuleType()&&!"".equals(log.getModuleType())){
			hql+=" and r.moduleType LIKE ? ";
			params.add("%"+log.getModuleType()+"%");
		}
		//时间段的查询 开始
		String getStartTime = log.getStartTime();
		String getEndTime = log.getEndTime();

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
