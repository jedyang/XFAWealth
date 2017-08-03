package com.fsll.app.common.service;


import com.fsll.app.common.vo.AppLatestTodoVO;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
public interface AppWebReadToDoService {
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
	
	
	/**
	 * 根据消息类型得到待办待阅列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param isRead 是否已阅,1已阅,0待阅
	 * @param keyWord 关键词
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return
	 */
	public JsonPaging getTodoList(JsonPaging jsonPaging,String memberId,String type,String isRead,String langCode,String keyWord);
	
	/**
	 * 根据消息类型得到最新待办待阅信息
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return
	 */
	public AppLatestTodoVO getLatestTodoMess(String memberId,String langCode);
	
	/**
	 * 生成WebReadToDo记录
	 * @param todo
	 * @param titleEn
	 * @param titleSc
	 * @param titleTc
	 * @return
	 */
	 public WebReadToDo saveWebReadToDo(WebReadToDo todo, String titleEn, String titleSc, String titleTc);
}

