package com.fsll.app.investor.discover.service;

import java.util.List;

import com.fsll.app.investor.discover.vo.AppTodoVo;
import com.fsll.common.util.JsonPaging;


/**
 * 待办待阅接口
 * @author zpzhou
 * @date 2016-11-02
 */
public interface AppTodoService {
	
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
	public List<AppTodoVo> getLatestTodoMess(String memberId,String type,String langCode);
	
	/**
	 * 设置待阅为已阅
	 * @param memberId
	 * @param id
	 * @return
	 */
	public boolean updateReadTodo(String memberId,String id);
}
