package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppIndexTopicService;
import com.fsll.app.investor.market.vo.AppIndexBbsReplyVO;
import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexBbsTypeVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.BbsReply;
import com.fsll.wmes.entity.BbsTopic;
import com.fsll.wmes.entity.BbsTopicCount;
import com.fsll.wmes.entity.BbsType;
import com.fsll.wmes.entity.MemberBase;

/**
 * 首页热门话题接口服务现实
 * @author zpzhou
 * @date 2016-10-09
 */
@Service("appIndexTopicService")
//@Transactional
public class AppIndexTopicServiceImpl extends BaseService implements AppIndexTopicService {

	/**
	 * 热门话题模块列表信息
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexBbsTypeVO> findIndexBbsType(String langCode){
		List<AppIndexBbsTypeVO> topList=new ArrayList<AppIndexBbsTypeVO>();
		String hql = "select t.id,t.name from BbsType t where t.isValid=? order by t.createTime desc";
		List params = new ArrayList();
		params.add("1");
		List list = baseDao.find(hql, params.toArray(),false);
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexBbsTypeVO  vo = new AppIndexBbsTypeVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			topList.add(vo);
    	}
		return topList;
	}
	/**
	 * 得到热门话题列表信息
	 * @param jsonPaging 分页参数
	 * @param typeId 类型ID  0:最热门  1：推荐  2:模块
	 * @param moduleId 模块ID  当typeId=2时不能为空
	 * @return
	 */
	public JsonPaging findIndexBbsTopicList(JsonPaging jsonPaging,String typeId,String moduleId){
		List params = new ArrayList();
		String hql = " from BbsTopic b ";
		hql += " left join b.type t  ";
		hql += " left join b.author m  ";
		hql += " left join BbsTopicCount c on c.infoId=b.id  ";
		if("0".equals(typeId)){//最热门
			hql += " where b.isValid=? ";
			hql += " order by b.replyCount desc ";
			params.add("1");
		}else if("1".equals(typeId)){//推荐
			hql += " where b.isValid=? and b.isRecommend=?";
			hql += " order by b.orderBy desc";
			params.add("1");
			params.add("1");
		}else{//按模块
			hql += " where b.isValid=? and b.type.id=?";
			hql += " order by b.orderBy desc";
			params.add("1");
			params.add(moduleId);
		}
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List<AppIndexBbsTopicVO> iList=new ArrayList<AppIndexBbsTopicVO>();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			BbsTopic info=(BbsTopic)objs[0];
			BbsType type=(BbsType)objs[1];
			MemberBase author=(MemberBase)objs[2];
			BbsTopicCount count=(BbsTopicCount)objs[3];
			AppIndexBbsTopicVO vo=new AppIndexBbsTopicVO();
			vo.setId(info.getId());
			vo.setContent(info.getContent());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setIsRecommend(info.getIsRecommend());
			vo.setIsTop(info.getIsTop());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setReplyCount(info.getReplyCount());
			vo.setTopic(info.getTopic());
			if(null!=type)vo.setTypeName(type.getName());
			if(null!=author)vo.setAuthorName(author.getNickName());
			if(null!=count)vo.setViews(count.getViews());
			iList.add(vo);
    	}
		jsonPaging.setList(iList);
		return jsonPaging;
	}
	/**
	 * 得到某个热门话题详细信息
	 * @param topicId 话题ID
	 * @return
	 */
	public AppIndexBbsTopicVO findIndexBbsTopicMess(String topicId){
		AppIndexBbsTopicVO vo =new AppIndexBbsTopicVO();
		List params = new ArrayList();
		String hql = " from BbsTopic b ";
		hql += " left join b.type t  ";
		hql += " left join b.author m  ";
		hql += " left join BbsTopicCount c on c.infoId=b.id  ";
		hql += " where b.id=? and b.isValid=? ";
		params.add(topicId);
		params.add("1");
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			BbsTopic info=(BbsTopic)objs[0];
			BbsType type=(BbsType)objs[1];
			MemberBase author=(MemberBase)objs[2];
			BbsTopicCount count=(BbsTopicCount)objs[3];
			vo.setId(info.getId());
			vo.setContent(info.getContent());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setIsRecommend(info.getIsRecommend());
			vo.setIsTop(info.getIsTop());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setReplyCount(info.getReplyCount());
			vo.setTopic(info.getTopic());
			if(null!=type)vo.setTypeName(type.getName());
			if(null!=author)vo.setAuthorName(author.getNickName());
			if(null!=count)vo.setViews(count.getViews());
    	}
		return vo;
	}
	/**
	 * 得到某个热门话题的回复列表信息
	 * @param jsonPaging 分页参数
	 * @param topicId 话题ID
	 * @return
	 */
	public JsonPaging findIndexBbsReplyList(JsonPaging jsonPaging,String topicId){
		List params = new ArrayList();
		String hql = " from BbsReply b ";
		hql += " left join b.type t  ";
		hql += " left join b.replyUser m  ";
		hql += " where b.topic.id=? and b.isValid=? ";
		hql += " order by b.replayTime desc";
		params.add(topicId);
		params.add("1");
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List<AppIndexBbsReplyVO> iList=new ArrayList<AppIndexBbsReplyVO>();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			BbsReply info=(BbsReply)objs[0];
			BbsType type=(BbsType)objs[1];
			MemberBase replyUser=(MemberBase)objs[2];
			AppIndexBbsReplyVO vo=new AppIndexBbsReplyVO();
			vo.setId(info.getId());
			vo.setEmotionIcon(info.getEmotionIcon());
			vo.setContent(info.getContent());
			vo.setReplayTime(DateUtil.dateToDateString(info.getReplayTime(), "yyyy-MM-dd HH:mm:ss"));
			if(null!=type)vo.setTypeName(type.getName());
			if(null!=replyUser)vo.setReplyUserName(replyUser.getNickName());
			iList.add(vo);
    	}
		jsonPaging.setList(iList);
		return jsonPaging;
	}
}
