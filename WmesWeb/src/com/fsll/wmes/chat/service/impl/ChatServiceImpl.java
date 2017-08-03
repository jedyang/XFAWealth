package com.fsll.wmes.chat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.pojo.User;
import com.fsll.wmes.chat.pojo.message.ToDBMessage;
import com.fsll.wmes.chat.service.ChatService;
import com.fsll.wmes.chat.vo.ChatHistoryVO;
import com.fsll.wmes.chat.vo.GroupFriendListVO;
import com.fsll.wmes.chat.vo.MessageListVO;
import com.fsll.wmes.chat.vo.RecentContactsForAppVO;
import com.fsll.wmes.chat.vo.RecentContactsVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.LayimGroup;
import com.fsll.wmes.entity.LayimGroupDetail;
import com.fsll.wmes.entity.LayimMsgHistory;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoEn;
import com.fsll.wmes.entity.WebReadToDoSc;
import com.fsll.wmes.entity.WebReadToDoTc;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

/**
 * 聊天接口实现类
 * @author mqzou
 * @date 2017-05-08
 */
@Service("chatService")
public class ChatServiceImpl extends BaseService implements ChatService {

	/**
	 * 查询组的owner
	 * @author mqzou 2017-05-09
	 */
	@Override
	public User queryOwner(String gId) {
		LayimGroup group=(LayimGroup)baseDao.get(LayimGroup.class, gId);
		if(null!=group && null!=group.getMember()){
			User owner = new User();
			owner.setId(group.getMember().getId());
			owner.setAvatar(PageHelper.getUserHeadUrl(group.getMember().getIconUrl(), ""));
			owner.setSign(group.getMember().getHighlight());
			owner.setUsername(group.getMember().getNickName());
			return owner;
		}
		return null;
	}

	/**
	 * 查询组成员
	 * @author mqzou 2017-05-09
	 */
	@Override
	public List<User> queryMember(String gId) {
		//String hql=" from LayimGroupDetail r where r.group.id=?";
		String hql=" from WebFriend r where r.group.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(gId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<User> list=new ArrayList<User>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				WebFriend detail = (WebFriend) it.next();
				User user=new User();
				user.setAvatar(PageHelper.getUserHeadUrl(detail.getToMember().getIconUrl(), ""));
				user.setId(detail.getToMember().getId());
				user.setSign(detail.getToMember().getHighlight());
				user.setUsername(detail.getToMember().getNickName());
				list.add(user);
			}
		}
		return list;
	}

	/**
	 * 查询Mine结果集
	 * @author mqzou 2017-05-09
	 */
	@Override
	public StatusUser queryMine(String userId,String langCode) {
		MemberBase member=(MemberBase)baseDao.get(MemberBase.class, userId);
		if(null!=member){
			StatusUser user=new StatusUser();
			user.setId(userId);
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
				String nickName=getCommonMemberName(member.getId(), langCode, "2");
				user.setUsername(nickName);
			}else {
				 user.setUsername(member.getNickName());
			}
           
            user.setSign(member.getHighlight());
            user.setAvatar(PageHelper.getUserHeadUrlNoLogin(member.getIconUrl()));
            user.setStatus(member.getImStatus());
            return user;
		}
		return null;
	}

	/**
	 * 查询朋友分组集
	 * @author mqzou 2017-05-09
	 */
	@Override
	public List<FriendGroup> queryFriendGroup(String userId) {
		String hql=" from LayimGroup r where r.member.id=? and r.groupType='f'";
		List<Object> params=new ArrayList<Object>();
		params.add(userId);
		List<FriendGroup> list=new ArrayList<FriendGroup>();
		List resultList=baseDao.find(hql,params.toArray() , false);
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				LayimGroup group = (LayimGroup) it.next();
				FriendGroup vo=new FriendGroup();
				vo.setId(group.getId());
				vo.setGroupname(group.getGroupName());
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 查询朋友分组集
	 * @author mqzou 2017-05-09
	 */
	@Override
	public List<StatusUser> queryFriends(String userId,String langCode) {
		String hql=" FROM WebFriend r where  r.fromMember.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(userId);
		//params.add(userId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<StatusUser> list=new ArrayList<StatusUser>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				WebFriend detail = (WebFriend) it.next();
				StatusUser user=new StatusUser();
				user.setId(detail.getToMember().getId());
				user.setFgid(null!=detail.getGroup()?detail.getGroup().getId():"");
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==detail.getToMember().getSubMemberType()){
					String nickName=getCommonMemberName(detail.getToMember().getId(), langCode, "2");
					user.setUsername(nickName);
				}else {
					user.setUsername(detail.getToMember().getNickName());
				}
				
				user.setSign(detail.getToMember().getHighlight());
				user.setAvatar(PageHelper.getUserHeadUrlNoLogin(detail.getToMember().getIconUrl()));
				user.setStatus(detail.getToMember().getImStatus());
				user.setNewMsgCount(findNewMsgCount(user.getId(), userId));
				list.add(user);
			}
		}
		return list;
	}
	
	/**
	 * 获取用户的未读消息
	 * @author mqzou  2017-05-25
	 * @param fromMemberId
	 * @param memberId
	 * @return
	 */
	private int findNewMsgCount(String fromMemberId,String memberId){
		StringBuilder hql=new StringBuilder();
		hql.append(" select count(0)  from LayimMsgHistory r where r.toUser.id=? and r.isRead='0'");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		if(null!=fromMemberId && !"".equals(fromMemberId)){
			hql.append(" and r.fromUser.id=?");
			params.add(fromMemberId);
		}
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
				return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}

	/**
	 * 查询大分组集
	 * @author mqzou 2017-05-09
	 */
	@Override
	public List<BigGroup> queryGroup(String userId) {
		String hql=" from LayimGroup r where r.member.id=? and r.groupType='g'";
		List<Object> params=new ArrayList<Object>();
		params.add(userId);
		List<BigGroup> list=new ArrayList<BigGroup>();
		List resultList=baseDao.find(hql, params.toArray(), false);
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				LayimGroup vo = (LayimGroup) it.next();
				 BigGroup group = new BigGroup();
                 group.setId(vo.getId());
                 group.setGroupname(vo.getGroupName());
                 group.setAvatar(vo.getAvatar());
                 list.add(group);
			}
		}
		return list;
	}

	/**
	 * 根据群id获取所有群员的id
	 * @author mqzou 2017-05-09
	 */
	@Override
	public List<String> getMemberListOnlyIds(String groupId) {
		String hql=" from WebFriend r where r.group.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(groupId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<String>  list=new ArrayList<String>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				WebFriend detail = (WebFriend) it.next();
				list.add(detail.getToMember().getId());
			}
		}
		return null;
	}

	/**
	 * 添加聊天记录
	 */
	@Override
	public boolean addMsgRecord(LayimMsgHistory message) {
		try {
			message=(LayimMsgHistory)baseDao.saveOrUpdate(message);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return true;
	}
	
	/**
	 * 获取分组信息
	 * @author mqzou 2017-05-09
	 * @param id
	 * @return
	 */
	@Override
	public LayimGroup findGroupById(String id) {
		LayimGroup group=(LayimGroup)baseDao.get(LayimGroup.class, id);
		return group;
	}

	/**
	 * 获取聊天记录
	 * @author mqzou 2017-05-17
	 * @param fromMemberId
	 * @param toMemberId
	 * @return
	 */
	@Override
	public JsonPaging getChatHistory(JsonPaging jsonPaging,String fromMemberId, String toMemberId,String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from LayimMsgHistory r where (r.fromUser.id=? and r.toUser.id=?) or (r.fromUser.id=? and r.toUser.id=?)");
		List<Object> params=new ArrayList<Object>();
		params.add(fromMemberId);
		params.add(toMemberId);
		params.add(toMemberId);
		params.add(fromMemberId);
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<ChatHistoryVO> list=new ArrayList<ChatHistoryVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				LayimMsgHistory object = (LayimMsgHistory) it.next();
				ChatHistoryVO vo=new ChatHistoryVO();
				vo.setId(object.getFromUser().getId());
				vo.setContent(object.getMsg());
				vo.setAvatar(PageHelper.getUserHeadUrlNoLogin(object.getFromUser().getIconUrl()));
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==object.getFromUser().getSubMemberType()){
					String nickName=getCommonMemberName(object.getFromUser().getId(), langCode, "2");
					vo.setUsername(nickName);
				}else {
					vo.setUsername(object.getFromUser().getNickName());
				}
				vo.setTimestamp(object.getTimestamp());
				if(object.getFromUser().getId().equals(fromMemberId)){
					vo.setMine(true);
				}else {
					vo.setMine(false);
				}
				vo.setType("friend");
				vo.setDateTime(DateUtil.dateToDateString(object.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取用户的好友列表（移动端）
	 * @author mqzou 2017-05-17
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public List<GroupFriendListVO> findFriendList(String memberId, String langCode) {
		String hql=" from LayimGroup r where r.member.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List groupList=baseDao.find(hql, params.toArray(), false);
		List<GroupFriendListVO> list=new ArrayList<GroupFriendListVO>();
		if(null!=groupList && !groupList.isEmpty()){
			Iterator it=groupList.iterator();
			while (it.hasNext()) {
				LayimGroup group = (LayimGroup) it.next();
				GroupFriendListVO vo=new GroupFriendListVO();
				vo.setId(group.getId());
				vo.setGroupname(group.getGroupName());
				vo=findFriendList(memberId,vo, langCode);
				list.add(vo);
			}
		}
		GroupFriendListVO vo=findFriendList(memberId,null, langCode);
		if(null!=vo){//
			vo.setGroupname(PropertyUtils.getPropertyValue(langCode, "member.front.MyBuddy", null));
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 根据分组获取好友列表
	 * @author mqzou 2017-05-17
	 * @param vo
	 * @param langCode
	 * @return
	 */
	private GroupFriendListVO findFriendList(String memberId,GroupFriendListVO vo,String langCode){
		int onLine=0;
	    String	hql="";
	    if(null!=vo){
	    	hql=" from WebFriend r where r.group.id='"+vo.getId()+"'";
	    }else {
	    	hql=" from WebFriend r where r.group is null and r.fromMember.id='"+memberId+"'";
			
		}
		List friendList=baseDao.find(hql, null, false);
		List<User> userList=new ArrayList<User>();
		if(null!=friendList && !friendList.isEmpty()){
			if(null==vo){
				vo=new GroupFriendListVO();
			}
			Iterator iterator=friendList.iterator();
			while (iterator.hasNext()) {
				WebFriend friend = (WebFriend) iterator.next();
				User user=new User();
				MemberBase member=friend.getToMember();
				user.setId(member.getId());
				user.setAvatar(PageHelper.getUserHeadUrlNoLogin(member.getIconUrl()));
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
					String nickName=getCommonMemberName(member.getId(), langCode, "2");
					user.setUsername(nickName);
				}else {
					user.setUsername(member.getNickName());
				}
			    if("online".equals(member.getImStatus())){
			    	onLine++;
			    }
				userList.add(user);
			}
			vo.setList(userList);
			vo.setOnline(onLine);
		}
		
		return vo;
	}

	/**
	 * 获取用户的最近联系人
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public List<RecentContactsVO> findRecentContacts(String memberId, String langCode) {
		String sql="CALL pro_getrecentcontacts(?,?);";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(100);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<RecentContactsVO> list=new ArrayList<RecentContactsVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator iterator=resultList.iterator();
			JsonPaging jsonPaging=new JsonPaging();
			jsonPaging.setOrder("desc");
			jsonPaging.setSort("r.createTime");
			jsonPaging.setPage(1);
			jsonPaging.setRows(1);
			
			while (iterator.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) iterator.next();
				String id=getMapValue(map, "id");
				if(null!=id && !"".equals(id)){
					User user=queryMine(id,langCode);
					RecentContactsVO vo=new RecentContactsVO();
					vo.setId(id);
					vo.setAvatar(user.getAvatar());
					vo.setFgid(user.getFgid());
					vo.setName(user.getUsername());
					vo.setUsername(user.getUsername());
					vo.setType("friend");
					vo.setSign(user.getSign());
					
					jsonPaging=getChatHistory(jsonPaging, memberId, id, langCode);
					if(null!=jsonPaging && null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
						ChatHistoryVO historyVO=(ChatHistoryVO)jsonPaging.getList().get(0);
						vo.setHistoryTime(historyVO.getTimestamp());
					}
					list.add(vo);
				}
				
			}
		}
		return list;
	}

	/**
	 * 获取用户的最近联系人(移动端)
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public List<RecentContactsForAppVO> findContactsForApp(String memberId, String langCode,int rows) {
		String sql="CALL pro_getrecentcontacts(?,?);";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(rows);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<RecentContactsForAppVO> list=new ArrayList<RecentContactsForAppVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator iterator=resultList.iterator();
			JsonPaging jsonPaging=new JsonPaging();
			jsonPaging.setOrder("desc");
			jsonPaging.setSort("r.createTime");
			jsonPaging.setPage(1);
			jsonPaging.setRows(1);
			
			while (iterator.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) iterator.next();
				String id=getMapValue(map, "id");
				if(null!=id && !"".equals(id)){
					User user=queryMine(id,langCode);
					RecentContactsForAppVO vo=new RecentContactsForAppVO();
					vo.setId(id);
					vo.setAvatar(user.getAvatar());
					vo.setName(user.getUsername());
					vo.setUsername(user.getUsername());
					vo.setType("friend");
					
					jsonPaging=getChatHistory(jsonPaging, memberId, id, langCode);
					if(null!=jsonPaging && null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
						ChatHistoryVO historyVO=(ChatHistoryVO)jsonPaging.getList().get(0);
						vo.setHistoryTime(historyVO.getTimestamp());
						vo.setSign(historyVO.getContent());
						vo.setContent(historyVO.getContent());
						vo.setTimestamp(historyVO.getTimestamp());
						vo.setDateTime(historyVO.getDateTime());
					}
					
					list.add(vo);
				}
				
			}
		}
		return list;
	}
	
	/**
	 * 获取消息列表
	 * @author mqzou 2017-05-18
	 * @param memberId
	 * @param langCode
	 * @param dateFormat
	 * @return
	 */
	@Override
	public JsonPaging getMessageList(JsonPaging jsonPaging,MemberBase member) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebFriend f inner join WebReadToDo r on f.id=r.relateId ");
		hql.append(" inner join "+getLangString("WebReadToDo", member.getLangCode())+" a on r.id=a.id");
		hql.append(" where r.owner.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(member.getId());
		hql.append(" order by f.checkResult asc ");
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<MessageListVO> list=new ArrayList<MessageListVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				WebFriend friend=(WebFriend)objects[0];
				WebReadToDo todo=(WebReadToDo)objects[1];
				MessageListVO vo=new MessageListVO();
				if(member.getLangCode().equals(CommonConstants.LANG_CODE_EN)){
					WebReadToDoEn en=(WebReadToDoEn)objects[2];
					vo.setContent(en.getTitle());
				}else if (member.getLangCode().equals(CommonConstants.LANG_CODE_SC)) {
					WebReadToDoSc sc=(WebReadToDoSc)objects[2];
					vo.setContent(sc.getTitle());
				}else if (member.getLangCode().equals(CommonConstants.LANG_CODE_TC)) {
					WebReadToDoTc tc=(WebReadToDoTc)objects[2];
					vo.setContent(tc.getTitle());
				}
				/*String nickName="";
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==friend.getFromMember().getSubMemberType()){
					 nickName=getCommonMemberName(friend.getFromMember().getId(), member.getLangCode(), "2");
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==friend.getFromMember().getSubMemberType()) {
					nickName=getCommonMemberName(friend.getFromMember().getId(), member.getLangCode(), "1");
				}*/
				vo.setFrom(friend.getFromMember().getId());
				vo.setHref("");
				if("0".equals(friend.getCheckResult())){
					vo.setRead(0);
				}else {
					vo.setRead(1);
				}
				vo.setRemark(friend.getApplyMsg());
				vo.setTime(DateUtil.getDateTimeGoneFormatStr(friend.getCreateTime(), member.getLangCode(), member.getDateFormat()));
				vo.setType(1);
				vo.setUid(todo.getId());
				vo.setId(friend.getId());
				User user=queryMine(friend.getFromMember().getId(), member.getLangCode());
				vo.setUser(user);
			    vo.setFromGroup(null!=friend.getGroup()?friend.getGroup().getId():"");
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 设置消息已读
	 * @author mqzou 2017-05-24
	 * @param fromMemberId
	 * @param toMemberId
	 */
	@Override
	public void setMessageRead(String fromMemberId, String toMemberId) {
		String hql="update LayimMsgHistory r set r.isRead='1' where r.fromUser.id=? and r.toUser.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(fromMemberId);
		params.add(toMemberId);
		baseDao.updateHql(hql, params.toArray());
	}
}
