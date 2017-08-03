package com.fsll.app.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.common.service.AppWebTaskListService;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.entity.WebTaskListEn;
import com.fsll.wmes.entity.WebTaskListSc;
import com.fsll.wmes.entity.WebTaskListTc;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
@Service("appWebTaskListService")
//@Transactional
public class AppWebTaskListServiceImpl extends BaseService implements AppWebTaskListService {
 
  
	/***
	 * 根据id查询对象
	 * @author michael
	 * @date 2016-10-13
	 * @param id
	 * @return
	 */
	@Override
	public WebTaskList findById(String id) {
		String hql="from WebTaskList where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<WebTaskList> list=this.baseDao.find(hql, params.toArray(), false);
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	/***
	 * 列表查询的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param memberBase
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberBase memberBase) {
		String hql=" from WebTaskList where isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		
		if(null!=memberBase && null != memberBase.getId() && !"".equals( memberBase.getId())){
			hql += "and owner.id=? ";
			params.add( memberBase.getId());
		}
		hql += " order by  ";

		
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}
	
	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param webTaskList 对象
	 * @return
	 */
	@Override
	public WebTaskList saveOrUpdate(WebTaskList webTaskList) {
		return (WebTaskList)baseDao.saveOrUpdate(webTaskList);
	}

	/**
	 * 发送任务
	 * @author michael
	 * @date 2016-10-13
	 * @param webTaskList
	 * @return
	 */
	public WebTaskList sendToDo(WebTaskList webTaskList){
		return sendToDo(webTaskList.getType(), webTaskList.getModuleType(), webTaskList.getRelateId(), webTaskList.getTitle(), webTaskList.getTargetDate(), webTaskList.getFromMember(), webTaskList.getOwner());
	}
	
	/***
	 * 发送任务
	 * @author michael
	 * @date 2016-10-13
	 * @param type 任务分类,来源于基础数据
	 * @param module 对应模块,K:KYC,P:Proposal,A:Appointment,M:Meeting
	 * @param relateId 模块对应的对象id
	 * @param title 标题
	 * @param targetDate 目标日期
	 * @param fromUser 发送人
	 * @param toUser 接收人
	 * @return
	 */
	public WebTaskList sendToDo(String type, String module, String relateId, String title, Date targetDate, MemberBase fromUser, MemberBase toUser) {
		WebTaskList webTaskList = new WebTaskList();
		webTaskList.setType(type);
		webTaskList.setModuleType(module);
		webTaskList.setRelateId(relateId);
		webTaskList.setFromMember(fromUser);
		webTaskList.setHandleStatus("0");//待处理
		webTaskList.setIsValid("1");
		webTaskList.setOwner(toUser);
		webTaskList.setTargetDate(targetDate);
		webTaskList.setCreateDate(new Date());
		
		webTaskList =  (WebTaskList)baseDao.saveOrUpdate(webTaskList);
		if (null!=webTaskList && null!=webTaskList.getId()){
			WebTaskListEn en = new WebTaskListEn();
			en.setId(webTaskList.getId());
			en.setTitle(title);
			createTaskListEn(en);
			
			WebTaskListSc sc = new WebTaskListSc();
			sc.setId(webTaskList.getId());
			sc.setTitle(title);
			createTaskListSc(sc);

			WebTaskListTc tc = new WebTaskListTc();
			tc.setId(webTaskList.getId());
			tc.setTitle(title);
			createTaskListTc(tc);
		}
		return webTaskList;
	}
    
	/**
	 * 创建英文附加信息
	 * @param en
	 */
	private void createTaskListEn(WebTaskListEn en){
		String sql = " insert into web_task_list_en (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( en.getId());
		params.add( en.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/**
	 * 创建简体附加信息
	 * @param en
	 */
	private void createTaskListSc(WebTaskListSc sc){
		String sql = " insert into web_task_list_sc (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( sc.getId());
		params.add( sc.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/**
	 * 创建繁体附加信息
	 * @param en
	 */
	private void createTaskListTc(WebTaskListTc tc){
		String sql = " insert into web_task_list_tc (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( tc.getId());
		params.add( tc.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/***
	 * 发送TaskList
	 * @author zxtan
	 * @date 2017-06-05
	 * @param taskListId
	 * @return
	 */
	public WebTaskList saveTaskList(WebTaskList taskList) {
		String titleSc = taskList.getTitleSc();
		String titleTc = taskList.getTitleTc();
		String titleEn = taskList.getTitleEn();
		taskList = (WebTaskList) this.baseDao.create(taskList);
		if(taskList != null){
			WebTaskListEn en = new WebTaskListEn();
			en.setId(taskList.getId());
			en.setTitle(titleEn);
			createTaskListEn(en);
			WebTaskListSc sc = new WebTaskListSc();
			sc.setId(taskList.getId());
			sc.setTitle(titleSc);
			createTaskListSc(sc);
			WebTaskListTc tc = new WebTaskListTc();
			tc.setId(taskList.getId());
			tc.setTitle(titleTc);
			createTaskListTc(tc);
		}
		return taskList;
	}

}
