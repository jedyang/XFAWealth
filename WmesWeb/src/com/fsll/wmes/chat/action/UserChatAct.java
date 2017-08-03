/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.chat.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.chat.dao.LayIMDao;
import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.JsonResultType;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.pojo.User;
import com.fsll.wmes.chat.pojo.result.BaseDataResult;
import com.fsll.wmes.chat.pojo.result.GroupMemberResult;
import com.fsll.wmes.chat.pojo.result.JsonResult;
import com.fsll.wmes.chat.pojo.result.JsonResultHelper;
import com.fsll.wmes.chat.service.ChatService;
import com.fsll.wmes.chat.util.LimitQueue;
import com.fsll.wmes.chat.vo.ChatUserVO;
import com.fsll.wmes.chat.vo.GreetingMsg;
import com.fsll.wmes.chat.vo.GroupFriendListVO;
import com.fsll.wmes.chat.vo.MessageListVO;
import com.fsll.wmes.chat.vo.RecentContactsForAppVO;
import com.fsll.wmes.chat.vo.RecentContactsVO;
import com.fsll.wmes.chat.vo.UserChatMsg;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.LayimGroup;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.ifa.vo.MyBuddyVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 聊天室管理
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-9-19
 */
@Controller
@RequestMapping(value = "/front/chat")
public class UserChatAct extends WmesBaseAct {

	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private WebReadToDoService webReadToDoService;

	//LayIMDao dao = new LayIMDao();

	@RequestMapping(value = "/send/userLogin")
	public void addUser(HttpServletRequest request, HttpServletResponse response, Model model) {
		/********** test start ***********/
		// 添加一个在线用户进来
		MemberSsoVO ssoAccount = memberBaseService.saveLoginAuth(request, "7979565656", "ac72e698171f4a400afe7a422a2e9132", "web");
		request.getSession().setAttribute("U", ssoAccount);
		/********** test end ***********/
		Map map = new HashMap();
		map.put("flag", "1");
		JsonUtil.toWriter(map, response);
	}

	public SimpMessagingTemplate template;// 用于转发数据(sendTo)
	private Map<Integer, Object[]> msgCache = new HashMap<Integer, Object[]>();// 消息队列

	@Autowired
	public UserChatAct(SimpMessagingTemplate template) {
		this.template = template;
	}

	/**
	 * 广播信息
	 * 
	 * @param message
	 * @throws Exception
	 */
	@MessageMapping("/topicChat/all")
	public void greeting(GreetingMsg message) throws Exception {
		String dest = "/topicChat/all"; // 找到需要发送的地址
		this.template.convertAndSend(dest, message);// 发送用户的聊天记录
	}

	/**
	 * 一对一发送,发送特定的客户端 WebSocket聊天的相应接收方法和转发方法
	 * 
	 * @param userChat
	 *            关于用户聊天的各个信息
	 */
	@MessageMapping("/userChat/{userCode}")
	public void userChat(@PathVariable String userCode, UserChatMsg userChat) {
		String dest = "/userChat/" + userChat.getUserCode(); // 找到需要发送的地址
		this.template.convertAndSend(dest, userChat);// 发送用户的聊天记录
		// 获取缓存，并将用户最新的聊天记录存储到缓存中
		Object[] cache = msgCache.get(Integer.parseInt(userChat.getUserCode()));
		try {
			userChat.setName(URLDecoder.decode(userChat.getName(), "utf-8"));
			userChat.setContent(URLDecoder.decode(userChat.getContent(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		((LimitQueue<UserChatMsg>) cache[1]).offer(userChat);
	}

	// private int MAX_CHAT_HISTORY;
	// public void setMAX_CHAT_HISTORY(int MAX_CHAT_HISTORY) {
	// this.MAX_CHAT_HISTORY = MAX_CHAT_HISTORY;
	// }
	@RequestMapping(value = "/chatLog")
	public String chatLog(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "chat/chatLog";
	}

	@RequestMapping(value = "/msgbox")
	public String msgBox(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		//model.put("applyList", newList);
		return "chat/msgbox";
	}
	
	
	
	/**
	 * 获取分组中的用户信息
	 * 
	 * @author mqzou 2017-05-09
	 */
	@RequestMapping(value = "/getMember")
	public void getMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		// MemberBase loginUser=getLoginUser(request);
		String groupId = request.getParameter("");
		GroupMemberResult groupMemberResult = new GroupMemberResult();
		List<User> users = chatService.queryMember(groupId);
		User owner = chatService.queryOwner(groupId);
		groupMemberResult.setOwner(owner);
		groupMemberResult.setMembers(users.size());
		groupMemberResult.setList(users);
		JsonResult result = JsonResultHelper.createSuccessResult("", groupMemberResult);
		// JsonResult result=dao.getMemberList(loginUser.getId());
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 获取聊天的基本信息
	 * 
	 * @author mqzou 2017-05-09
	 */
	@RequestMapping(value = "/getBaseList")
	public void getBaseMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		MemberBase loginUser = getLoginUser(request);
		JsonResult jsonResult = new JsonResult();
		BaseDataResult result = new BaseDataResult();
		String langCode=getLoginLangFlag(request);
		// 查询mine
		StatusUser mine = chatService.queryMine(loginUser.getId(),langCode);

		result.setMine(mine);
		List<FriendGroup> friendGroups = chatService.queryFriendGroup(loginUser.getId());// friend
		//if(null==friendGroups || friendGroups.isEmpty()){
			FriendGroup group=new FriendGroup();
			group.setId("");
			group.setGroupname(PropertyUtils.getPropertyValue(langCode, "friend.group.default", null));
			friendGroups.add(group);
		//}
		List<StatusUser> users = chatService.queryFriends(loginUser.getId(),langCode);// 查询用户
		List<BigGroup> groups = chatService.queryGroup(loginUser.getId()); // group
		BigGroup bigGroup=new BigGroup();
		bigGroup.setId("");
		bigGroup.setGroupname(PropertyUtils.getPropertyValue(langCode,  "friend.group.default", null));
		groups.add(bigGroup);
		List<RecentContactsVO> history=chatService.findRecentContacts(loginUser.getId(), langCode);

		friendGroups = getFriendGroupList(friendGroups, users);
		result.setFriend(friendGroups);
		result.setGroup(groups);
		result.setHistory(history);
		
		jsonResult.setCode(JsonResultType.TYPESUCCESS);
		jsonResult.setMsg("");
		jsonResult.setData(result);
		JsonUtil.toWriter(jsonResult, response);
	}

	// 处理分组和好友间的关系
	private List<FriendGroup> getFriendGroupList(List<FriendGroup> friendGroup, List<StatusUser> friends) {
		List<FriendGroup> list = new ArrayList<FriendGroup>();
		for (FriendGroup fg : friendGroup) {
			List<StatusUser> users = new ArrayList<StatusUser>();
			int online = 0;
			for (StatusUser u : friends) {
				if (fg.getId().equals(u.getFgid())) {
					if ("online".equals(u.getStatus())) {
						online++;
					}
					users.add(u);
				}
			}
			fg.setOnline(online);
			fg.setList(users);
			list.add(fg);
		}
		return list;
	}
	/**
	 * 获取聊天记录
	 * @author mqzou 2017-05-17
	 */
	@RequestMapping(value="/getChatHistory")
	public void getChatHistory(HttpServletRequest request, HttpServletResponse response, Model model){
		//MemberBase loginUser=getLoginUser(request);
		String fromMemberId=request.getParameter("fromMemberId");
		String langCode=request.getParameter("langCode");//getLoginLangFlag(request);
		String toMemberId=request.getParameter("toMemberId");//当前登录人
		jsonPaging=getJsonPaging(request);
		jsonPaging=chatService.getChatHistory(jsonPaging, fromMemberId, toMemberId, langCode);
		JsonUtil.toWriter(jsonPaging, response);
	}
	/**
	 * 移动端聊天
	 * @author mqzou  2017-05-17
	 */
	@RequestMapping(value="/mobileChat")
	public String chatForMobile(HttpServletRequest request, HttpServletResponse response, Model model){
		return "chat/mobile";
	}
	
	/**
	 * 获取当前用户的好友列表（移动端聊天）
	 * @author mqzou  2017-05-17
	 */
	@RequestMapping(value="/appInitChat")
	public void findGroupFriends(HttpServletRequest request, HttpServletResponse response, Model model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		String memberId=request.getParameter("memberId");
		String langCode=request.getParameter("lang");
		
		List<GroupFriendListVO> list=chatService.findFriendList(memberId, langCode);
		result.put("friendList", list);
		User curUser=chatService.queryMine(memberId,langCode);
		result.put("member", curUser);
		List<RecentContactsForAppVO> history=chatService.findContactsForApp(memberId, langCode,100);
		result.put("history", history);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取消息列表
	 * @author mqzou  2017-05-22
	 */
	@RequestMapping(value="/getMessage")
	public void getMessage(HttpServletRequest request, HttpServletResponse response, Model model){
		String memberId=request.getParameter("id");
		String langCode=request.getParameter("langCode");
		MemberBase member=memberBaseService.findById(memberId);

		if(null==langCode || "".equals(langCode)){
			langCode=CommonConstants.DEF_LANG_CODE;
		}
        String dateFormat=member.getDateFormat();
        if(null==dateFormat || "".equals(dateFormat)){
        	dateFormat=CommonConstants.FORMAT_DATE;
        }
        member.setLangCode(langCode);
        member.setDateFormat(dateFormat);
        jsonPaging=new JsonPaging();
        jsonPaging.setPage(1);
        jsonPaging.setRows(20);
        jsonPaging=chatService.getMessageList(jsonPaging, member);
        JsonUtil.toWriter(jsonPaging, response);
     
	}
	
	/**
	 * @author mqzou
	 * @tips：myBuddy处理（接受，拒绝）
	 */
	@RequestMapping(value="/dealWithFriend")
	public void dealWithFriend(HttpServletRequest request, HttpServletResponse response, Model model){
		String id = request.getParameter("id");
		String status = request.getParameter("status");//0 拒绝 1同意
		String groupId=request.getParameter("group");
		String langCode=request.getParameter("lang");
		
		WebFriend webFriend = webFriendService.findWebFriendById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != webFriend) {
			webFriend.setCheckResult(status);
			webFriend.setLastUpdate(new Date());
			WebFriend vo = webFriendService.updateWebFriend(webFriend);
			if (null != vo) {
				
				//更新所有的相关待办
				webReadToDoService.updateReadTodoReaded(CommonConstantsWeb.WEB_READ_MODULE_FRIENDS, vo.getFromMember().getId(), vo.getToMember().getId());
				
				String titlePro="";
				
				if("1".equals(status)){
					WebFriend friend=new WebFriend();
					friend.setFromMember(vo.getToMember());
					friend.setToMember(vo.getFromMember());
					friend.setCheckResult(vo.getCheckResult());
					friend.setCreateTime(DateUtil.getCurDate());
					friend.setRelationships(vo.getRelationships());
					friend.setCheckTime(DateUtil.getCurDate());
					LayimGroup group=chatService.findGroupById(groupId);
					friend.setGroup(group);
					vo=webFriendService.updateWebFriend(friend);
					titlePro="ifalist.list.applyfriend.agree";
					
				}else {
					titlePro="ifalist.list.applyfriend.reject";
				}
				
				WebReadToDo webReadToDo = new WebReadToDo();
				webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
				webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_FRIENDS);
				webReadToDo.setRelateId(id);
				webReadToDo.setFromMember(webFriend.getToMember());
				webReadToDo.setIsRead("0");//待阅
				webReadToDo.setIsValid("1");
				
				webReadToDo.setOwner(webFriend.getFromMember());
				
				webReadToDo.setCreateTime(new Date());
				String nickName="";
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==webFriend.getFromMember().getSubMemberType()){
					nickName=getCommonMemberName(webFriend.getFromMember().getId(), langCode, "2");
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==webFriend.getFromMember().getSubMemberType()) {
					nickName=getCommonMemberName(webFriend.getFromMember().getId(), langCode, "1");
				}
				
				webReadToDoService.saveWebReadToDo(webReadToDo, nickName+" " + PropertyUtils.getPropertyValue("en", titlePro, null), nickName+" " + PropertyUtils.getPropertyValue("sc", titlePro, null), nickName+" " + PropertyUtils.getPropertyValue("tc", titlePro, null));
				obj.put("result", true);
			} else {
				obj.put("result", true);
			}
		} else {
			obj.put("result", true);
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * @author mqzou
	 * @tips：设置消息已读
	 */
	@RequestMapping(value="/setMessageRead")
	public void setMessageRead(HttpServletRequest request, HttpServletResponse response, Model model){
		String fromMemberId=request.getParameter("fromId");
		String toMemberId=request.getParameter("toId");
		chatService.setMessageRead(fromMemberId, toMemberId);
	}
	
	/**
	 * @author mqzou 2017-05-25
	 * @tips：获取用户信息
	 */
	@RequestMapping(value="/getMemberInfo")
	public void getMemberInfo(HttpServletRequest request, HttpServletResponse response, Model model){
		String langCode=request.getParameter("lang");
		String memberId=request.getParameter("id");
		User user=chatService.queryMine(memberId, langCode);
		ChatUserVO vo=new ChatUserVO();
		vo.setId(user.getId());
		vo.setAvatar(user.getAvatar());
		vo.setName(user.getUsername());
		vo.setType("friend");
		JsonUtil.toWriter(vo, response);
		
	}
	
}
