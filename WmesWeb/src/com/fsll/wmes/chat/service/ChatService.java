package com.fsll.wmes.chat.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.pojo.User;
import com.fsll.wmes.chat.pojo.message.ToDBMessage;
import com.fsll.wmes.chat.vo.GroupFriendListVO;
import com.fsll.wmes.chat.vo.RecentContactsForAppVO;
import com.fsll.wmes.chat.vo.RecentContactsVO;
import com.fsll.wmes.entity.LayimGroup;
import com.fsll.wmes.entity.LayimMsgHistory;
import com.fsll.wmes.entity.MemberBase;

public interface ChatService {

	/*
	 * 查询组的owner
	 */
	public User queryOwner(String gId);

	/*
	 * 查询组成员
	 */
	public List<User> queryMember(String gId);

	/*
	 * 查询Mine结果集
	 */
	public StatusUser queryMine(String userId,String langCode);

	/*
	 * 查询朋友分组集
	 */
	public List<FriendGroup> queryFriendGroup(String userId);

	/*
	 * 查询朋友分组集
	 */
	public List<StatusUser> queryFriends(String userId,String langCode);

	/*
	 * 查询大分组集
	 */
	public List<BigGroup> queryGroup(String userId);
	
	/**
	 * 根据群id获取所有群员的id
	 * @param groupId
	 * @return
	 */
	public List<String> getMemberListOnlyIds(String groupId);
	/**
	 * 添加聊天记录
	 * @param message
	 * @return
	 */
	public boolean addMsgRecord(LayimMsgHistory message);
	
	/**
	 * 获取分组信息
	 * @param id
	 * @return
	 */
	public LayimGroup findGroupById(String id);
	
	/**
	 * 获取聊天记录
	 * @author mqzou 2017-05-17
	 * @param fromMemberId
	 * @param toMemberId
	 * @return
	 */
	public JsonPaging getChatHistory(JsonPaging jsonPaging, String fromMemberId,String toMemberId,String langCode);
	
	/**
	 * 获取用户的好友列表（移动端）
	 * @author mqzou 2017-05-17
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<GroupFriendListVO> findFriendList(String memberId,String langCode);
	
	/**
	 * 获取用户的最近联系人
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<RecentContactsVO> findRecentContacts(String memberId,String langCode);
	
	/**
	 * 获取用户的最近联系人(移动端)
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<RecentContactsForAppVO> findContactsForApp(String memberId,String langCode,int rows);
	
	/**
	 * 获取消息列表
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @param dateFormat
	 * @return
	 */
	public JsonPaging getMessageList(JsonPaging jsonPaging,MemberBase member);
	
	/**
	 * 设置消息已读
	 * @author mqzou 2017-05-24
	 * @param fromMemberId
	 * @param toMemberId
	 */
	public void setMessageRead(String fromMemberId,String toMemberId);
}
