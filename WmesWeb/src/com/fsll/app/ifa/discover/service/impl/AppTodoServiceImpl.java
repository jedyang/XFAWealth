package com.fsll.app.ifa.discover.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.app.ifa.discover.service.AppTodoService;
import com.fsll.app.ifa.discover.vo.AppLatestTodoVO;
import com.fsll.app.ifa.discover.vo.AppTodoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoSc;

/**
 * 待办待阅接口现实
 * @author zpzhou
 * @date 2016-11-02
 */
@Service("appIfaTodoService")
//@Transactional
public class AppTodoServiceImpl extends BaseService implements AppTodoService{
	
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
	public JsonPaging getTodoList(JsonPaging jsonPaging,String memberId,String type,String isRead,String langCode,String keyWord){
		List<AppTodoVO> todoList=new ArrayList<AppTodoVO>();
//		String hql="select t.id,s.title,t.type,t.moduleType,t.relateId,t.isRead,t.readTime,t.createTime " +
		String hql=" from WebReadToDo t " +
				" inner join "+this.getLangString("WebReadToDo", langCode)+" s on s.id=t.id "+
				" inner join MemberBase b on b.id=t.fromMember.id "+
//				" left join MemberIfa i on i.member.id=b.id "+
//				" left join MemberIndividual d on d.member.id=b.id "+
				" where t.type=? and t.owner.id=? and t.isValid=? ";
		List params = new ArrayList();
		params.add(type);
		params.add(memberId);
		params.add("1");
		if(null!=isRead && !"".equals(isRead)){
			hql += " and t.isRead=? ";
			params.add(isRead);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and s.title like ? ";
			params.add("%"+keyWord+"%");
		}
		hql+=" order by t.createTime desc ";
		jsonPaging=baseDao.selectJsonPagingNoTotal(hql, params.toArray(),jsonPaging, false);
		List list=jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			WebReadToDo todo = (WebReadToDo) objs[0];
			WebReadToDoSc todoSc = new WebReadToDoSc();
			BeanUtils.copyProperties(objs[1], todoSc);
			MemberBase member = (MemberBase) objs[2];			
			
			AppTodoVO  vo = new AppTodoVO();
			
			vo.setId(todo.getId());
			vo.setTitle(todoSc.getTitle());
			vo.setType(todo.getType());
			vo.setModuleType(todo.getModuleType());
			vo.setRelateId(todo.getRelateId());
			vo.setFromMemberId(member.getId());
			
			String userName = getCommonMemberName(member.getId(),langCode,CommonConstants.MEMBER_NAME_REAL_NAME);

			vo.setFromMemberName(userName);
			
			vo.setMsgUrl(todo.getMsgUrl());
			vo.setMsgParam(todo.getMsgParam());
			vo.setAppUrl(todo.getAppUrl());
			vo.setAppParam(todo.getAppParam());
			vo.setIsApp(todo.getIsApp());
			vo.setIsRead(todo.getIsRead());			
			vo.setReadTime(DateUtil.dateToDateString(todo.getReadTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(todo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						
			todoList.add(vo);
		}
		jsonPaging.setList(todoList);
		return jsonPaging;
	}
	
	/**
	 * 根据消息类型得到最新待办待阅信息
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return
	 */
	public AppLatestTodoVO getLatestTodoMess(String memberId,String langCode){
//		List<AppTodoVO> todoList=new ArrayList<AppTodoVO>();
		AppLatestTodoVO vo = new AppLatestTodoVO();
		vo = getTodoList(vo,memberId, "1", langCode);
		vo = getTodoList(vo,memberId, "2", langCode);
		vo = getTodoList(vo,memberId, "3", langCode);
		
		return vo;
	}
	
	/**
	 * 根据消息类型得到待办待阅列表信息
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return 
	 */
	private AppLatestTodoVO getTodoList(AppLatestTodoVO info,String memberId,String type,String langCode){
		List<AppTodoVO> todoList =  info.getTodoList();
		if(null == todoList){
			todoList = new ArrayList<AppTodoVO>();
		}
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from WebReadToDo t ");
		hql.append(" inner join "+this.getLangString("WebReadToDo", langCode)+" s on s.id=t.id ");
		hql.append(" inner join MemberBase b on b.id=t.fromMember.id ");
		hql.append(" where t.type=? and t.owner.id=? and t.isValid=? ");
		hql.append(" and (t.isRead = '0' or t.isRead is null) ");
		List params = new ArrayList();
		params.add(type);
		params.add(memberId);
		params.add("1");
		hql.append(" order by t.createTime desc ");
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			WebReadToDo todo = (WebReadToDo) objs[0];
			WebReadToDoSc todoSc = new WebReadToDoSc();
			BeanUtils.copyProperties(objs[1], todoSc);
			MemberBase member = (MemberBase) objs[2];			
			
			AppTodoVO  vo = new AppTodoVO();
			
			vo.setId(todo.getId());
			vo.setTitle(todoSc.getTitle());
			vo.setType(todo.getType());
			vo.setModuleType(todo.getModuleType());
			vo.setRelateId(todo.getRelateId());
			vo.setFromMemberId(todo.getFromMember().getId());
			String userName = getCommonMemberName(member.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
			vo.setFromMemberName(userName);
			
			vo.setMsgUrl(todo.getMsgUrl());
			vo.setMsgParam(todo.getMsgParam());
			vo.setAppUrl(todo.getAppUrl());
			vo.setAppParam(todo.getAppParam());
			vo.setIsApp(todo.getIsApp());
			vo.setIsRead(todo.getIsRead());			
			vo.setReadTime(DateUtil.dateToDateString(todo.getReadTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(todo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			
			todoList.add(vo);
			if("1".equals(type)){
				info.setDealCount(String.valueOf(list.size()));
			}else if("2".equals(type)){
				info.setNoteCount(String.valueOf(list.size()));
			}else if("3".equals(type)){
				info.setAlertCount(String.valueOf(list.size()));
			}
			info.setTodoList(todoList);
		}
		return info;
	}
}
