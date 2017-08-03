package com.fsll.wmes.web.service;


import java.util.Date;
import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.web.vo.WebTaskListVO;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
public interface WebTaskListService {
	/***
	 * 根据id查询待办对象
	 * @author michael
	 * @date 2016-10-13
	 * @param id
	 * @return
	 */
	public WebTaskList findById(String id);

	
	/***
	 * 待办列表查询的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param jsonPaging
	 * @param memberBase
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberBase memberBase);

	 
	/***
	 * 保存用户对象的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param webTaskList 待办对象
	 * @return
	 */
	public WebTaskList saveOrUpdate(WebTaskList webTaskList);

	/**
	 * 发送待办
	 * @author michael
	 * @date 2016-10-13
	 * @param webTaskList
	 * @return
	 */
	public WebTaskList sendToDo(WebTaskList webTaskList);
	
	/***
	 * 发送待办
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
	public WebTaskList sendToDo(String type, String module, String relateId, String title, Date targetDate, MemberBase fromUser, MemberBase toUser);

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
	public WebTaskList sendToDo(String type, String module, String relateId, String title, String url, String parms, String ifApp, Date targetDate, MemberBase fromUser, MemberBase toUser);
	
	/***
	 * 获取所属的 Task List
	 * @author wwluo
	 * @date 2016-10-21
	 * @param ownerId 当前 memberBaseId
	 * @param langCode 多语言标识
	 * @param recurentFlag 是否重复事件标识
	 * @param IfImportant 是否重要事件
	 * @return
	 */
	public List<WebTaskListVO> getTaskList(String ownerId,String langCode, String recurentFlag,String ifImportant);

	/***
	 * 获取TaskList(含多语言)
	 * @author wwluo
	 * @date 2016-10-21
	 * @param taskListId
	 * @return
	 */
	public WebTaskListVO getTaskListData(String taskListId,String langCode);

	/***
	 * 发送TaskList
	 * @author wwluo
	 * @date 2017-02-16
	 * @param taskListId
	 * @return
	 */
	public WebTaskList saveTaskList(WebTaskList taskList);

}

