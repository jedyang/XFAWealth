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
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.web.service.WebBusLogService;
import com.fsll.wmes.web.service.WebReadService;

/***
 * 业务接口实现类：待办待阅管理
 * @author Yan
 * @date 2016-11-23
 */
@Service("webReadServiceImpl")
//@Transactional
public class WebReadServiceImpl extends BaseService implements WebReadService {
	/**
	 * 增加或者修改一条数据
	 * @param info 信息
	 * @return 
	 */
	public  WebReadToDo saveOrUpdate(WebReadToDo log){
		if(null == log.getId() || "".equals(log.getId())){
			log.setId(null);
			log.setCreateTime(new Date());
			log = (WebReadToDo)baseDao.create(log);
		}else{
			log = (WebReadToDo)baseDao.update(log);
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
	public WebReadToDo findById(String id){
		WebReadToDo info = (WebReadToDo) baseDao.get(WebReadToDo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly=true)
	public List<Object> findTitleById(String id,String langCode){
		String hql = " FROM "+this.getLangString("WebReadToDo", langCode) + " r ";
			hql += " WHERE r.id=? ";
		Object[] objs = new Object[1];
		objs[0] = id;
		List<Object> info = this.baseDao.find(hql, objs, false);
		return info;
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param info 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,WebReadToDo info,String nickName, String langCode) {
		String hql=" FROM WebReadToDo r ";
			hql += " INNER JOIN "+this.getLangString("WebReadToDo", langCode);
			hql += " l ON r.id=l.id ";
			hql += " INNER JOIN MemberBase m ON m.id=r.owner.id";
			hql += " WHERE 1=1 ";
			hql += " AND r.isValid=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=info.getType() && !"".equals(info.getType())){
			hql+=" AND r.type = ? ";
			params.add(info.getType());
		}
		if(null!=info.getModuleType() && !"".equals(info.getModuleType())){
			hql+=" AND r.moduleType LIKE ? ";
			params.add("%"+info.getModuleType()+"%");
		}
		if(null!=info.getTitle() && !"".equals(info.getTitle())){
			hql+=" AND l.title LIKE ? ";
			params.add("%"+info.getTitle()+"%");
		}
		if(null!=info.getIsRead() && !"".equals(info.getIsRead())){
			hql+=" AND r.isRead = ? ";
			params.add(info.getIsRead());
		}
		if(null!=nickName && !"".equals(nickName)){
			hql+=" AND m.nickName LIKE ? ";
			params.add("%"+nickName+"%");
			
		}
		//时间段的查询 开始
//		String getStartTime = info.getStartTime();
//		String getEndTime = info.getEndTime();
//
//		Date startTime = DateUtil.getDate(getStartTime, "yyyy-MM-dd HH:mm:ss");
//		Date endTime = DateUtil.getDate(getEndTime, "yyyy-MM-dd HH:mm:ss");;
//		if (startTime!=null && (endTime==null)){
//			hql += " and r.createTime >= ? ";
//			params.add(startTime);
//		}
//		if (endTime!=null && (startTime==null)){
//			hql += " and r.createTime <= ? ";
//			params.add(endTime);
//		}
//		if(startTime!=null && endTime!=null){				
//			hql += " and (r.createTime between ? and ?) ";
//			params.add(startTime);
//			params.add(endTime);
//		}
		//时间段的查询 结束
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);

		return jsonpaging;
	}
}
