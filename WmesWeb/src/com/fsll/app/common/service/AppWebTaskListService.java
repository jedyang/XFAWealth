package com.fsll.app.common.service;


import java.util.Date;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebTaskList;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
public interface AppWebTaskListService {
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
	 * 发送TaskList
	 * @author zxtan
	 * @date 2017-06-05
	 * @param taskListId
	 * @return
	 */
	public WebTaskList saveTaskList(WebTaskList taskList);
}

