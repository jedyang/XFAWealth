package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.entity.WebTaskListEn;
import com.fsll.wmes.entity.WebTaskListSc;
import com.fsll.wmes.entity.WebTaskListTc;
import com.fsll.wmes.web.service.WebTaskListService;
import com.fsll.wmes.web.service.WebTaskListService;
import com.fsll.wmes.web.vo.WebTaskListVO;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
@Service("webTaskListService")
//@Transactional
public class WebTaskListServiceImpl extends BaseService implements WebTaskListService {
 
  
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
		return sendToDo(webTaskList.getType(), webTaskList.getModuleType(), webTaskList.getRelateId(), webTaskList.getTitle(), webTaskList.getTaskUrl(), webTaskList.getTaskParam(), webTaskList.getIsApp(), webTaskList.getTargetDate(), webTaskList.getFromMember(), webTaskList.getOwner());
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
	public WebTaskList sendToDo(String type, String module, String relateId, String title, Date targetDate, MemberBase fromUser, MemberBase toUser){
		return sendToDo(type, module, relateId, title, "", "", "", targetDate, fromUser, toUser);
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
	 * @param url 	任务url,此字段供web使用
	 * @param parms 相关参数key=value格式,多个之间用&分隔
	 * @param ifApp 是否app使用的待办,1是,0否
	 * @return
	 */
	public WebTaskList sendToDo(String type, String module, String relateId, String title, String url, String parms, String ifApp, Date targetDate, MemberBase fromUser, MemberBase toUser) {
		WebTaskList webTaskList = new WebTaskList();
		webTaskList.setType(type);
		webTaskList.setModuleType(module);
		webTaskList.setRelateId(relateId);
		webTaskList.setFromMember(fromUser);
		webTaskList.setTaskUrl(url);
		webTaskList.setTaskParam(parms);
		webTaskList.setIsApp(ifApp);
		webTaskList.setHandleStatus("0");//待处理
		webTaskList.setIsValid("1");
		webTaskList.setOwner(toUser);
		webTaskList.setTargetDate(targetDate);
		webTaskList.setCreateDate(new Date());
		
		webTaskList =  (WebTaskList)this.baseDao.saveOrUpdate(webTaskList);
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
		
		//如果app也使用，则发送消息
		if (StringUtils.isNotEmpty(ifApp) && "1".equals(ifApp)){//非null和空串
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
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
	 * 获取所属的 Task List
	 * @author wwluo
	 * @date 2016-10-21
	 * @param ownerId 当前 memberBaseId
	 * @param langCode 多语言标识
	 * @param recurentFlag 是否重复事件标识
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WebTaskListVO> getTaskList(String ownerId,String langCode, String recurentFlag,String ifImportant) {
		List<WebTaskListVO> webTaskListVOs = new ArrayList<WebTaskListVO>();
		if(StringUtils.isNotBlank(ownerId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" WebTaskList" +
					" WHERE" +
					" isValid=1" +
					" AND" +
					" owner_id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(ownerId);
			if (StringUtils.isNotBlank(ifImportant)) {
				hql.append(" AND ifImportant=?");
				params.add(ifImportant);
			}
			hql.append(" ORDER BY createDate DESC");
			List<WebTaskList> webTaskLists = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(webTaskLists != null && !webTaskLists.isEmpty()){
				for (WebTaskList webTaskList : webTaskLists) {
					WebTaskListVO webTaskListVO = getTaskListData(webTaskList.getId(),langCode);
					webTaskListVOs.add(webTaskListVO);
				}
			}
		}
		return webTaskListVOs;
	}

	/***
	 * 获取TaskList(含多语言)
	 * @author wwluo
	 * @date 2016-10-21
	 * @param taskListId
	 * @param langCode
	 * @return
	 */
	@Override
	public WebTaskListVO getTaskListData(String taskListId,String langCode) {
		WebTaskListVO taskListVO = new WebTaskListVO();
		if(StringUtils.isNotBlank(taskListId)){
			WebTaskList taskList = (WebTaskList) this.baseDao.get(WebTaskList.class, taskListId);
			WebTaskListSc taskListSc = (WebTaskListSc) this.baseDao.get(WebTaskListSc.class, taskListId);
			WebTaskListTc taskListTc = (WebTaskListTc) this.baseDao.get(WebTaskListTc.class, taskListId);
			WebTaskListEn taskListEn = (WebTaskListEn) this.baseDao.get(WebTaskListEn.class, taskListId);
			BeanUtils.copyProperties(taskList, taskListVO);
			if(taskListSc != null){
				taskListVO.setTitleSc(taskListSc.getTitle());
			}
			if(taskListTc != null){
				taskListVO.setTitleTc(taskListTc.getTitle());
			}
			if(taskListEn != null){
				taskListVO.setTitleEn(taskListEn.getTitle());
			}
			// 多语言
			if (CommonConstants.LANG_CODE_SC.equals(langCode) && taskListSc != null) {
				taskListVO.setTitle(taskListSc.getTitle());
			} else if (CommonConstants.LANG_CODE_TC.equals(langCode) && taskListTc != null) {
				taskListVO.setTitle(taskListTc.getTitle());
			} else if (CommonConstants.LANG_CODE_EN.equals(langCode) && taskListEn != null) {
				taskListVO.setTitle(taskListEn.getTitle());
			}
			if (taskList != null 
					&& StringUtils.isNotBlank(taskList.getTaskUrl())
						&& StringUtils.isNotBlank(taskList.getTaskParam())) {
				// 完整URL
				taskListVO.setFullUrl(taskList.getTaskUrl() + '?' + taskList.getTaskParam());
				// color
				if(taskList.getGroup() != null){
					taskListVO.setColor(taskList.getGroup().getColor());
				}
			}
		}
		return taskListVO;
	}

	/***
	 * 发送TaskList
	 * @author wwluo
	 * @date 2017-02-16
	 * @param taskListId
	 * @return
	 */
	@Override
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
