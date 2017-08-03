package com.fsll.wmes.web.service;


import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
public interface WebReadToDoService {
	/***
	 * 根据id查询对象
	 * @author michael
	 * @date 2016-10-13
	 * @param id
	 * @return
	 */
	public WebReadToDo findById(String id);

	
	/***
	 * 列表查询的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param memberBase
	 * @param jsonpaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberBase memberBase);

	 
	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param webReadToDo 对象
	 * @return
	 */
	public WebReadToDo saveOrUpdate(WebReadToDo webReadToDo);

	/**
	 * 发送消息
	 * @author michael
	 * @date 2016-10-13
	 * @param webReadToDo
	 * @return
	 */
	public WebReadToDo sendToRead(WebReadToDo webReadToDo);
	
	/***
	 * 发送消息
	 * @author michael
	 * @date 2016-10-13
	 * @param type 消息类型  1：交易相关消息，2：一般性通知，3：告警、提醒消息（CommonConstantsWeb已定义）
	 * @param module 模块：insight关点,news新闻 （CommonConstantsWeb已定义）
	 * @param relateId 模块对应的对象id
	 * @param title 标题
	 * @param fromUser 发送人
	 * @param toUser 接收人
	 * @return
	 */
	public WebReadToDo sendToRead(String type, String module, String relateId, String title, MemberBase fromUser, MemberBase toUser);
	
	/***
	 * 发送消息
	 * @author michael
	 * @date 2016-10-13
	 * @param type 消息类型  1：交易相关消息，2：一般性通知，3：告警、提醒消息（CommonConstantsWeb已定义）
	 * @param module 模块：insight关点,news新闻 （CommonConstantsWeb已定义）
	 * @param relateId 模块对应的对象id
	 * @param title 标题
	 * @param fromUser 发送人
	 * @param toUser 接收人
	 * @param url 	任务url,此字段供web使用
	 * @param parms 相关参数key=value格式,多个之间用&分隔
	 * @param ifApp 是否app使用的待办,1是,0否
	 * @return
	 */
	public WebReadToDo sendToRead(String type, String module, String relateId, String title, String url, String parms, String ifApp, MemberBase fromUser, MemberBase toUser);
	
	/**
	 * 生成todo记录
	 * 
	 * @param todo
	 * @return
	 */
	public WebReadToDo saveWebReadToDo(WebReadToDo todo);
	
	/**
	 * 生成WebReadToDo记录
	 * 
	 * @param todo
	 * @param titleEn
	 * @param titleSc
	 * @param titleTc
	 * @return
	 */
	public WebReadToDo saveWebReadToDo(WebReadToDo todo, String titleEn, String titleSc, String titleTc);
	 
	/**
	 * 更新待办为已阅
	 * @param en
	 */
	public void updateReadToDoReaded(String id);
	 
	/***
	 * 列表查询的方法
	 * @author michael
	 * @date 2017-02-16
	 * @param owner
	 * @param relateId
	 * @return
	 */
	public List findByOwner(MemberBase owner, String relateId);
	
	/**
	 * 更新待办为已阅
	 * @author michael
	 * @date 2017-02-16
	 * @param owner
	 * @param relateId
	 */
	public void updateReadToDoReaded(MemberBase owner, String relateId);
	
	/**
	 * 更新待办已阅（处理好友 申请消息后将所有相关的待办都设置为已阅）
	 * @author mqzou 2017-05-11
	 * @param type
	 * @param fromMemberId
	 * @param owner
	 */
	public void updateReadTodoReaded(String type,String fromMemberId,String owner);
}

