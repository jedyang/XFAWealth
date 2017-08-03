package com.fsll.app.discover.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.app.discover.service.AppCommunityService;
import com.fsll.app.discover.vo.AppCommunityCommentVO;
import com.fsll.app.discover.vo.AppCommunityQuestionVO;
import com.fsll.app.discover.vo.AppCommunitySectionVO;
import com.fsll.app.discover.vo.AppCommunityTopicVO;
import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.app.investor.discover.vo.AppNewsCommentVO;
import com.fsll.app.investor.discover.vo.AppNewsInfoItemVO;
import com.fsll.app.investor.me.vo.AppCommunityTopicItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.FrontSearchUserVO;
import com.fsll.wmes.entity.CommunityBehavior;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.CommunityContent;
import com.fsll.wmes.entity.CommunityFocus;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;

/**
 * 社区接口实体
 * @author zxtan
 * @date 2017-05-17
 */
@Service("appCommunityService")
public class AppCommunityServiceImpl extends BaseService implements
		AppCommunityService {

	@Autowired
	private CommunitySpaceService communitySpaceService;
	@Autowired
	private AppNewsInfoService newsInfoService;

	/**
	 * 获取社区栏目列表
	 * @author zxtan
	 * @date 2017-05-17 
	 */
	public List<AppCommunitySectionVO> findSectionList(String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunitySection r where  r.status='1' order by r.orderBy asc ");
		List<CommunitySection> list = baseDao.find(hql.toString(), null, false);
		List<AppCommunitySectionVO> voList=new ArrayList<AppCommunitySectionVO>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CommunitySection section = (CommunitySection) list.get(i);
				AppCommunitySectionVO vo = new AppCommunitySectionVO();
				vo.setSectionId(section.getId());
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					vo.setSectionName(section.getSectionNameEn());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setSectionName(section.getSectionNameSc());
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setSectionName(section.getSectionNameTc());
				}
				voList.add(vo);
			}
		}
		
		return voList;
	}
	

	/**
	 * 获取最新动态（关注）的帖子
	 * @author zxtan
	 * @date 2017-05-18
	 */
	public JsonPaging findDynamicTopicList(JsonPaging jsonPaging,String memberId, String langCode) {
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class,memberId);
				
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunityContent c on c.id=t.id ");
		hql.append(" inner join CommunitySection s on t.section.id=s.id ");
		hql.append(" left join CommunityBehavior b on b.target='topic' and b.targetId=t.id and b.behaviorType in ('like','unlike') and b.creator.id=? ");
		hql.append(" where t.status='1' ");
		hql.append("  and ( t.creator.id=? or ( exists ( select f.id from CommunityFocus f where f.focus.id=t.creator.id and f.creator.id=? )  ");
		hql.append("   and ( t.visitor = 'all' or ( t.visitor = 'friend' and exists( select f.id from WebFriend f where f.checkResult='1' and f.toMember.id=t.creator.id and f.fromMember.id=? ))) ");

		List params = new ArrayList();
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		
		if(null != memberBase && memberBase.getMemberType()==1){
			//当前登录人为投资者
			hql.append(" or ( t.visitor = 'client' and exists( select c.id from CrmCustomer c inner join MemberIfa i on i.id=c.ifa.id where i.member.id=t.creator.id and c.member.id=? )) ");
			params.add(memberId);
		}
		hql.append(" )) ");
		hql.append(" order by t.createTime desc ");
				
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppCommunityTopicVO> voList = new ArrayList<AppCommunityTopicVO>();
			for(int i=0;i<list.size(); i++){
				Object[] objs = (Object[])list.get(i);
				CommunityTopic info = (CommunityTopic)objs[0];
				CommunityContent content = (CommunityContent)objs[1];
				CommunitySection section = (CommunitySection)objs[2];
				
				AppCommunityTopicVO vo = new AppCommunityTopicVO();
				vo.setTopicId(info.getId());
				vo.setTopicTitle(info.getTitle());
				vo.setSectionId(section.getId());
				String sectionName = section.getSectionNameSc();
				if("en".equalsIgnoreCase(langCode)){
					sectionName = section.getSectionNameEn();
				}else if("tc".equalsIgnoreCase(langCode)){
					sectionName = section.getSectionNameTc();
				}
				vo.setSectionName(sectionName);
				if(null != info.getCreator()){
					vo.setMemberId(info.getCreator().getId());
					vo.setMemberType(String.valueOf(info.getCreator().getMemberType()));
					if(info.getCreator().getMemberType()==2){
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
					}else {
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
					}
					vo.setMemberIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				}
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setSourceId(info.getSourceId());
				vo.setSourceType(info.getSourceType());
				vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
				vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
				vo.setCommentCount(String.valueOf(info.getCommentCount()));
				vo.setReadCount(String.valueOf(info.getReadCount()));
				vo.setLikeCount(String.valueOf(info.getLikeCount()));
				vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
				vo.setTransferCount(String.valueOf(info.getTansferCount()));
				vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
				vo.setSuportComment(String.valueOf(info.getSupportComment()));
				if(null != objs[3]){
					CommunityBehavior behavior = (CommunityBehavior) objs[3];
					vo.setBehaviorType(behavior.getBehaviorType());
				}
				vo.setContent(content.getContent());
				voList.add(vo);
	    	}
			jsonPaging.setList(voList);
		}
				
		return jsonPaging;
	}


	/**
	 * 获取栏目的帖子
	 * @author zxtan
	 * @date 2017-05-18
	 */
	public JsonPaging findTopicListBySection(JsonPaging jsonPaging,String memberId,String sectionId, String langCode) {
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class,memberId);
				
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunityContent c on c.id=t.id ");
		hql.append(" inner join CommunitySection s on t.section.id=s.id ");
		hql.append(" left join CommunityBehavior b on b.target='topic' and b.targetId=t.id and b.behaviorType in ('like','unlike') and b.creator.id=? ");
		hql.append(" where t.status='1' and s.id=? ");
		hql.append("  and ( t.creator.id=? or ( t.visitor = 'all' ");
		hql.append(" 	or ( t.visitor = 'friend' and exists( select f.id from WebFriend f where f.checkResult='1' and f.toMember.id=t.creator.id and f.fromMember.id=? )) ");

		List params = new ArrayList();
		params.add(memberId);
		params.add(sectionId);
		params.add(memberId);
		params.add(memberId);
		
		if(null != memberBase && memberBase.getMemberType()==1){
			//当前登录人为投资者
			hql.append(" or ( t.visitor = 'client' and exists( select c.id from CrmCustomer c inner join MemberIfa i on i.id=c.ifa.id where i.member.id=t.creator.id and c.member.id=? )) ");
			params.add(memberId);
		}
		hql.append(" )) ");
		hql.append(" order by t.createTime desc ");
				
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppCommunityTopicVO> voList = new ArrayList<AppCommunityTopicVO>();
			for(int i=0;i<list.size(); i++){
				Object[] objs = (Object[])list.get(i);
				CommunityTopic info = (CommunityTopic)objs[0];
				CommunityContent content = (CommunityContent)objs[1];
				CommunitySection section = (CommunitySection)objs[2];
				
				AppCommunityTopicVO vo = new AppCommunityTopicVO();
				vo.setTopicId(info.getId());
				vo.setTopicTitle(info.getTitle());
				vo.setSectionId(section.getId());
				String sectionName = section.getSectionNameSc();
				if("en".equalsIgnoreCase(langCode)){
					sectionName = section.getSectionNameEn();
				}else if("tc".equalsIgnoreCase(langCode)){
					sectionName = section.getSectionNameTc();
				}
				vo.setSectionName(sectionName);
				if(null != info.getCreator()){
					vo.setMemberId(info.getCreator().getId());
					vo.setMemberType(String.valueOf(info.getCreator().getMemberType()));
					if(info.getCreator().getMemberType()==2){
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
					}else {
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
					}
					vo.setMemberIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				}
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setSourceId(info.getSourceId());
				vo.setSourceType(info.getSourceType());
				vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
				vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
				vo.setCommentCount(String.valueOf(info.getCommentCount()));
				vo.setReadCount(String.valueOf(info.getReadCount()));
				vo.setLikeCount(String.valueOf(info.getLikeCount()));
				vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
				vo.setTransferCount(String.valueOf(info.getTansferCount()));
				vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
				vo.setSuportComment(String.valueOf(info.getSupportComment()));
				if(null != objs[3]){
					CommunityBehavior behavior = (CommunityBehavior) objs[3];
					vo.setBehaviorType(behavior.getBehaviorType());
				}
				vo.setContent(content.getContent());
				voList.add(vo);
	    	}
			jsonPaging.setList(voList);
		}
				
		return jsonPaging;
	}
	
	
	/**
	 * 获取帖子
	 * @author zxtan
	 * @date 2017-05-18
	 */
	public JsonPaging findSearchTopicList(JsonPaging jsonPaging,String memberId,String keyword, String langCode) {
		MemberBase memberBase = (MemberBase) baseDao.get(MemberBase.class,memberId);
				
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunityContent c on c.id=t.id ");
		hql.append(" left join CommunitySection s on t.section.id=s.id ");
		hql.append(" left join CommunityBehavior b on b.target='topic' and b.targetId=t.id and b.behaviorType in ('like','unlike') and b.creator.id=? ");
		hql.append(" where t.status='1' and t.title like ? ");
		hql.append("  and ( t.creator.id=? or ( t.visitor = 'all' ");
		hql.append(" 	or ( t.visitor = 'friend' and exists( select f.id from WebFriend f where f.checkResult='1' and f.toMember.id=t.creator.id and f.fromMember.id=? )) ");

		List params = new ArrayList();
		params.add(memberId);
		params.add("%"+keyword+"%");
		params.add(memberId);
		params.add(memberId);
		
		if(null != memberBase && memberBase.getMemberType()==1){
			//当前登录人为投资者
			hql.append(" or ( t.visitor = 'client' and exists( select c.id from CrmCustomer c inner join MemberIfa i on i.id=c.ifa.id where i.member.id=t.creator.id and c.member.id=? )) ");
			params.add(memberId);
		}
		hql.append(" )) ");
		hql.append(" order by t.createTime desc ");
				
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppCommunityTopicVO> voList = new ArrayList<AppCommunityTopicVO>();
			for(int i=0;i<list.size(); i++){
				Object[] objs = (Object[])list.get(i);
				CommunityTopic info = (CommunityTopic)objs[0];
				CommunityContent content = (CommunityContent)objs[1];
				CommunitySection section = (CommunitySection)objs[2];
				
				AppCommunityTopicVO vo = new AppCommunityTopicVO();
				vo.setTopicId(info.getId());
				vo.setTopicTitle(info.getTitle());
				if(null != section){
					vo.setSectionId(section.getId());
					String sectionName = section.getSectionNameSc();
					if("en".equalsIgnoreCase(langCode)){
						sectionName = section.getSectionNameEn();
					}else if("tc".equalsIgnoreCase(langCode)){
						sectionName = section.getSectionNameTc();
					}
					vo.setSectionName(sectionName);
				}
				if(null != info.getCreator()){
					vo.setMemberId(info.getCreator().getId());
					vo.setMemberType(String.valueOf(info.getCreator().getMemberType()));
					if(info.getCreator().getMemberType()==2){
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
					}else {
						vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
					}
					vo.setMemberIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
				}
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setSourceId(info.getSourceId());
				vo.setSourceType(info.getSourceType());
				vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
				vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
				vo.setCommentCount(String.valueOf(info.getCommentCount()));
				vo.setReadCount(String.valueOf(info.getReadCount()));
				vo.setLikeCount(String.valueOf(info.getLikeCount()));
				vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
				vo.setTransferCount(String.valueOf(info.getTansferCount()));
				vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
				vo.setSuportComment(String.valueOf(info.getSupportComment()));
				if(null != objs[3]){
					CommunityBehavior behavior = (CommunityBehavior) objs[3];
					vo.setBehaviorType(behavior.getBehaviorType());
				}
				vo.setContent(content.getContent());
				voList.add(vo);
	    	}
			jsonPaging.setList(voList);
		}
				
		return jsonPaging;
	}
	
	/**
	 * 搜索页面-获取user列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging findSearchUserList(JsonPaging jsonPaging,String memberId,String keywords,String lang) {
		List params = new ArrayList();
		String hql = "select m.id,m.nickName,m.memberType from MemberBase m where m.memberType in('1','2') and m.status='1' ";

		if(StringUtils.isNotBlank(keywords)){
			hql += " and ( m.nickName like ?  ) ";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY m.createTime DESC ";

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontSearchUserVO> voList = new ArrayList<FrontSearchUserVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontSearchUserVO vo = new FrontSearchUserVO();//每个前端实体
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = objs[0]==null?"":objs[0].toString();
		//	String nickName = objs[1]==null?"":objs[1].toString();
			String memberType = objs[2]==null?"":objs[2].toString();


			vo.setMemberId(id);
			//vo.setNickName(nickName);
			vo.setMemberType(memberType);
			
			//发布人的头像与名称
			if(!"".equals(id)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, id);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				
				vo.setIconUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
			}
			vo.setFollowerCount(communitySpaceService.getFollowCount(id));
			vo.setTopicCount(communitySpaceService.getTopciCount(id));
			//
			if(StringUtils.isNotBlank(memberId)){
				CommunityFocus focus = communitySpaceService.findCommunityFocus(memberId,id);
				if(null!=focus){
					vo.setIsFocused(1);
			    }else {
			    	vo.setIsFocused(0);
				}
			}
			
			voList.add(vo);
		}
		jsonPaging.getList().clear();
		jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		// modify end
		
		return jsonPaging;
	}
	
	
	/**
	 * 得到帖子信息
	 * @author zxtan
	 * @date 2017-05-18
	 * @return
	 */
	public AppCommunityTopicVO findTopicInfo(String memberId,String topicId,String langCode){
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunityContent c on c.id=t.id ");
		hql.append(" inner join CommunitySection s on s.id=t.section.id ");
		hql.append(" left join CommunityBehavior b on b.target='topic' and b.targetId=t.id and b.behaviorType in ('like','unlike') and b.creator.id=? ");
		hql.append(" where t.status='1' and t.id=? ");

		List params = new ArrayList();
		params.add(memberId);
		params.add(topicId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);

		AppCommunityTopicVO vo = new AppCommunityTopicVO();
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			CommunityTopic info = (CommunityTopic)objs[0];
			CommunityContent content = (CommunityContent)objs[1];
			CommunitySection section = (CommunitySection)objs[2];
			
			vo.setTopicId(info.getId());
			vo.setTopicTitle(info.getTitle());
			vo.setSectionId(section.getId());
			String sectionName = section.getSectionNameSc();
			if("en".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameEn();
			}else if("tc".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameTc();
			}
			vo.setSectionName(sectionName);
			if(null != info.getCreator()){
				vo.setMemberId(info.getCreator().getId());
				vo.setMemberType(String.valueOf(info.getCreator().getMemberType()));
				if(info.getCreator().getMemberType()==2){
					vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
				}
				vo.setMemberIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
			}
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
			vo.setSourceId(info.getSourceId());
			vo.setSourceType(info.getSourceType());
			if(StringUtils.isNotBlank(info.getSourceId()) && StringUtils.isNotBlank(info.getSourceType())){
				if("topic".equalsIgnoreCase(info.getSourceType())){
					AppCommunityTopicVO sourceTopic = findTopicInfo(memberId, info.getSourceId(), langCode);
					vo.setSourceObj(sourceTopic);
				}else {
					AppNewsInfoItemVO newsInfo = newsInfoService.findNewsInfo(memberId, info.getSourceId());
					vo.setSourceObj(newsInfo);
				}
			}
			vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
			vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
			vo.setCommentCount(String.valueOf(info.getCommentCount()));
			vo.setReadCount(String.valueOf(info.getReadCount()));
			vo.setLikeCount(String.valueOf(info.getLikeCount()));
			vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
			vo.setTransferCount(String.valueOf(info.getTansferCount()));
			vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
			vo.setSuportComment(String.valueOf(info.getSupportComment()));
			if(null != objs[3]){
				CommunityBehavior behavior = (CommunityBehavior) objs[3];
				vo.setBehaviorType(behavior.getBehaviorType());
			}
			vo.setContent(content.getContent());
    	}
		
		return vo;
	}
	
	/**
	 * 添加帖子
	 * @author zxtan
	 * @date 2017-03-10
	 * @param jsonObject
	 * @return
	 */
	public boolean saveTopic(JSONObject jsonObject){
		String memberId = jsonObject.optString("memberId", "");
		String title = jsonObject.optString("title", "");
		int isInsight = jsonObject.optInt("isInsight", 0);
		String visitor = jsonObject.optString("visitor", "all");
		String sectionId = jsonObject.optString("sectionId", "");
		String content = jsonObject.optString("content", "");
		String sourceType = jsonObject.optString("sourceType", "");
		String sourceId = jsonObject.optString("sourceId", "");
		
		MemberBase creator = (MemberBase) baseDao.get(MemberBase.class, memberId);
		CommunitySection section = (CommunitySection) baseDao.get(CommunitySection.class, sectionId);
		if(null == creator || null == section) return false;
		
		CommunityTopic topic = new CommunityTopic();
		topic.setId(null);
		topic.setCreator(creator);
		topic.setCreateTime(new Date());
		topic.setIsInsight(isInsight);
		topic.setVisitor(visitor);
		topic.setTitle(title);
		topic.setSection(section);
		if(StringUtils.isNotBlank(sourceType) && StringUtils.isNotBlank(sourceId) ){
			topic.setSourceType(sourceType);
			topic.setSourceId(sourceId);
			
			CommunityTopic sourceTopic = (CommunityTopic) baseDao.get(CommunityTopic.class, sourceId);
			
			while (null != sourceTopic && null != sourceTopic.getId()) {
				int count = sourceTopic.getTansferCount()==null?1:sourceTopic.getTansferCount()+1;
				sourceTopic.setTansferCount(count);
				baseDao.saveOrUpdate(sourceTopic);
				if(null != sourceTopic.getSourceId()){
					sourceTopic = (CommunityTopic) baseDao.get(CommunityTopic.class, sourceId);
				}
			}
		}
		topic.setStatus(1);
		topic.setSupportComment(1);
		
		topic = (CommunityTopic) baseDao.saveOrUpdate(topic);
		
		CommunityContent detail = new CommunityContent();
		detail.setId(topic.getId());
		detail.setContent(content);
		detail = (CommunityContent) baseDao.create(detail);
		
		return true;
	}
	
	
	/**
	 * 得到回复列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @return
	 */
	public JsonPaging findCommentList(JsonPaging jsonPaging,String memberId,String target,String targetId,String commentType,String langCode){
		StringBuilder hql = new StringBuilder();
		hql.append(" from CommunityComment c ");
		hql.append(" inner join MemberBase m on m.id=c.creator.id ");
		hql.append(" left join CommunityBehavior b on b.targetId=c.id and b.target='comment' and FIND_IN_SET(b.behaviorType,'like,unlike')>0 and b.creator.id=? ");
		hql.append(" where c.status='1' and c.targetId=? and c.commentType=? ");
		hql.append(" order by c.createTime desc ");
		List params = new ArrayList();
		params.add(memberId);
		params.add(targetId);
		params.add(commentType);
				
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list=jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			List<AppCommunityCommentVO> voList=new ArrayList<AppCommunityCommentVO>();
			for(int i=0;i<list.size(); i++){
				AppCommunityCommentVO vo=new AppCommunityCommentVO();
				Object[] objs = (Object[])list.get(i);
				CommunityComment info = (CommunityComment)objs[0];
				MemberBase member = (MemberBase) objs[1]; 	
				
				String id = info.getId();			
				vo.setId(id);
				vo.setTarget(info.getTarget());
				vo.setTargetId(info.getTargetId());
				vo.setMemberId(member.getId());
				vo.setMemberType(String.valueOf(member.getMemberType()));
				String memberName = member.getNickName();
				if(member.getMemberType()==2){
					memberName = getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME);
				}
				vo.setMemberName(memberName);
				String iconUrl = PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null);
				vo.setMemberIconUrl(iconUrl);
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setCommentType(info.getCommentType());
				if(null != info.getParent()){
					vo.setReplyId(info.getParent().getId());
					CommunityComment parent = info.getParent();
					String replyMemberName = parent.getCreator().getNickName();
					if(parent.getCreator().getMemberType()==2){
						replyMemberName = getCommonMemberName(parent.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME);
					}										
					vo.setReplyMemberName(replyMemberName);
				}
				vo.setLikeCount(String.valueOf(info.getLikeCount()));
				vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
				vo.setCommentCount(String.valueOf(info.getCommentCount()));
				vo.setContent(info.getContent());
				if(null != objs[2]){
					CommunityBehavior behavior = (CommunityBehavior) objs[2];
					vo.setBehaviorType(behavior.getBehaviorType());
				}
//				List<AppCommunityCommentVO> replyList = findReplyList(memberId,id,langCode);
//				vo.setReplyList(replyList);
	
				voList.add(vo);
	    	}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	/**
	 * 获取某条评论的回复
	 * @author zxtan
	 * @date 2017-03-10
	 * @param commentId
	 * @return
	 */
	private List<AppCommunityCommentVO> findReplyList(String memberId,String commentId,String langCode){
		StringBuilder hql = new StringBuilder();
		hql.append(" from CommunityComment c ");
		hql.append(" inner join MemberBase m on m.id=c.creator.id ");
		hql.append(" left join CommunityBehavior b on b.targetId=c.id and b.target='comment' and FIND_IN_SET(b.behaviorType,'like,unlike')>0 and b.creator.id=? ");
		hql.append(" where c.status='1' and c.parent.id=? ");
		hql.append(" order by c.createTime desc ");
		List params = new ArrayList();
		params.add(memberId);
		params.add(commentId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppCommunityCommentVO> voList = new ArrayList<AppCommunityCommentVO>();
		if(null != list && !list.isEmpty()){
			
			for(int i=0;i<list.size(); i++){
				AppCommunityCommentVO vo = new AppCommunityCommentVO();
				Object[] objs = (Object[])list.get(i);
				CommunityComment info = (CommunityComment)objs[0];
				MemberBase member = (MemberBase) objs[1]; 
				
				String id = info.getId();			
				vo.setId(id);
				vo.setTarget(info.getTarget());
				vo.setTargetId(info.getTargetId());
				vo.setMemberId(member.getId());
				vo.setMemberType(String.valueOf(member.getMemberType()));
				String memberName = member.getNickName();
				if(member.getMemberType()==2){
					memberName = getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME);
				}
				vo.setMemberName(memberName);
				String iconUrl = PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null);
				vo.setMemberIconUrl(iconUrl);
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setCommentType(info.getCommentType());
				vo.setLikeCount(String.valueOf(info.getLikeCount()));
				vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
				vo.setCommentCount(String.valueOf(info.getCommentCount()));
				vo.setContent(info.getContent());
				if(null != objs[2]){
					NewsBehavior behavior = (NewsBehavior) objs[2];
					vo.setBehaviorType(behavior.getBehaviorType());
				}
				List<AppCommunityCommentVO> replyList = findReplyList(memberId,id,langCode);
				vo.setReplyList(replyList);
					
				voList.add(vo);
	    	}
		}
		
		return voList;
	}
	
	
	
	/**
	 * 帖子点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean saveBehavior(String memberId,String target,String targetId,String behaviorType){
		CommunityBehavior behavior = null;
		CommunityBehavior oriBehavior = null;
		String oriBehaviorType = null;
		StringBuilder hql = new StringBuilder();
		hql.append(" from CommunityBehavior t ");
		hql.append(" where t.creator.id=? and t.target=? and t.targetId=? ");
		if("favorite".equalsIgnoreCase(behaviorType)){
			hql.append(" and t.behaviorType='favorite' ");
		}else if("like".equalsIgnoreCase(behaviorType) || "unlike".equalsIgnoreCase(behaviorType)) {
			hql.append(" and FIND_IN_SET(t.behaviorType,'like,unlike')>0 ");
		}else {
			return false;
		}
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(target);
		params.add(targetId);
		
		List<CommunityBehavior> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			behavior = list.get(0);
//			oriBehavior = list.get(0);
			oriBehaviorType = behavior.getBehaviorType();
			if(behavior.getBehaviorType().equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				baseDao.delete(behavior);				
			}else {
				behavior.setBehaviorType(behaviorType);
				behavior.setCreateTime(new Date());
				baseDao.saveOrUpdate(behavior);				
			}
		}else {
			behavior = new CommunityBehavior();
			behavior.setId(null);
			behavior.setTarget(target);
			behavior.setTargetId(targetId);
			behavior.setBehaviorType(behaviorType);
			MemberBase creator = (MemberBase) baseDao.get(MemberBase.class, memberId);
			behavior.setCreator(creator);
			behavior.setCreateTime(new Date());
			baseDao.saveOrUpdate(behavior);			
		}
		
		if("topic".equalsIgnoreCase(target)){
			CommunityTopic topic = (CommunityTopic) baseDao.get(CommunityTopic.class, targetId);				
			if(null == topic) return false;
			saveTopicBehavior(topic, oriBehaviorType, behaviorType);
		}else if("comment".equalsIgnoreCase(target)){
			CommunityComment comment = (CommunityComment) baseDao.get(CommunityComment.class, targetId);
			if(null == comment) return false;
			saveCommentBehavior(comment, oriBehaviorType, behaviorType);
		}else if("question".equalsIgnoreCase(target)){
			CommunityQuestion question = (CommunityQuestion) baseDao.get(CommunityQuestion.class, targetId);
			if(null == question) return false;
			saveQuestionBehavior(question, oriBehaviorType, behaviorType);
		}
		
		return true;
	}
	
	/**
	 * 更新帖子点赞或踩数量
	 * @param info
	 * @param oriBehavior
	 * @param behaviorType
	 */
	private void saveTopicBehavior(CommunityTopic info,String oriBehaviorType,String behaviorType){
		if(null == oriBehaviorType){
			if("like".equalsIgnoreCase(behaviorType)){
				int likeCount = info.getLikeCount()==null?1:info.getLikeCount()+1;
				info.setLikeCount(likeCount);
			}else if("unlike".equalsIgnoreCase(behaviorType)) {
				int unlikeCount = info.getUnlikeCount()==null?1:info.getUnlikeCount()+1;
				info.setUnlikeCount(unlikeCount);
			}else if("favorite".equalsIgnoreCase(behaviorType)) {
				int favoriteCount = info.getFavoriteCount()==null?1:info.getFavoriteCount()+1;
				info.setFavoriteCount(favoriteCount);
			}
		}else {
			if(oriBehaviorType.equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				if("like".equalsIgnoreCase(behaviorType)){
					int likeCount = info.getLikeCount()==null?0:info.getLikeCount()-1;
					if(likeCount <0) likeCount = 0;
					info.setLikeCount(likeCount);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount()-1;
					if(unlikeCount <0) unlikeCount = 0;
					info.setUnlikeCount(unlikeCount);
				}else if("favorite".equalsIgnoreCase(behaviorType)) {
					int favoriteCount = info.getFavoriteCount()==null?0:info.getFavoriteCount()-1;
					info.setFavoriteCount(favoriteCount);
				}
			}else {				
				int likeCount = info.getLikeCount()==null?0:info.getLikeCount();
				int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount();
				
				if("like".equalsIgnoreCase(behaviorType)){
					info.setLikeCount(likeCount+1);
					info.setUnlikeCount(unlikeCount-1);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					info.setLikeCount(likeCount-1);
					info.setUnlikeCount(unlikeCount+1);
				}
			}
		}

		baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 更新帖子回复的点赞或踩数量
	 * @param info
	 * @param oriBehavior
	 * @param behaviorType
	 */
	private void saveCommentBehavior(CommunityComment info,String oriBehaviorType,String behaviorType){
		if(null == oriBehaviorType){
			if("like".equalsIgnoreCase(behaviorType)){
				int likeCount = info.getLikeCount()==null?1:info.getLikeCount()+1;
				info.setLikeCount(likeCount);
			}else if("unlike".equalsIgnoreCase(behaviorType)) {
				int unlikeCount = info.getUnlikeCount()==null?1:info.getUnlikeCount()+1;
				info.setUnlikeCount(unlikeCount);
			}
		}else {
			if(oriBehaviorType.equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				if("like".equalsIgnoreCase(behaviorType)){
					int likeCount = info.getLikeCount()==null?0:info.getLikeCount()-1;
					if(likeCount<0) likeCount = 0;
					info.setLikeCount(likeCount);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount()-1;
					if(unlikeCount<0) unlikeCount = 0;
					info.setUnlikeCount(unlikeCount);
				}
			}else {				
				int likeCount = info.getLikeCount()==null?0:info.getLikeCount();
				int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount();
				
				if("like".equalsIgnoreCase(behaviorType)){
					info.setLikeCount(likeCount+1);
					info.setUnlikeCount(unlikeCount-1);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					info.setLikeCount(likeCount-1);
					info.setUnlikeCount(unlikeCount+1);
				}
			}
		}

		baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 更新问题点赞或踩数量
	 * @param info
	 * @param oriBehavior
	 * @param behaviorType
	 */
	private void saveQuestionBehavior(CommunityQuestion info,String oriBehaviorType,String behaviorType){
		if(null == oriBehaviorType){
			if("like".equalsIgnoreCase(behaviorType)){
				int likeCount = info.getLikeCount()==null?1:info.getLikeCount()+1;
				info.setLikeCount(likeCount);
			}else if("unlike".equalsIgnoreCase(behaviorType)) {
				int unlikeCount = info.getUnlikeCount()==null?1:info.getUnlikeCount()+1;
				info.setUnlikeCount(unlikeCount);
			}
		}else {
			if(oriBehaviorType.equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				if("like".equalsIgnoreCase(behaviorType)){
					int likeCount = info.getLikeCount()==null?0:info.getLikeCount()-1;
					if(likeCount<0) likeCount = 0;
					info.setLikeCount(likeCount);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount()-1;
					if(unlikeCount<0) unlikeCount = 0;
					info.setUnlikeCount(unlikeCount);
				}
			}else {				
				int likeCount = info.getLikeCount()==null?0:info.getLikeCount();
				int unlikeCount = info.getUnlikeCount()==null?0:info.getUnlikeCount();
				
				if("like".equalsIgnoreCase(behaviorType)){
					info.setLikeCount(likeCount+1);
					info.setUnlikeCount(unlikeCount-1);
				}else if("unlike".equalsIgnoreCase(behaviorType)) {
					info.setLikeCount(likeCount-1);
					info.setUnlikeCount(unlikeCount+1);
				}
			}
		}

		baseDao.saveOrUpdate(info);
	}
	
	
	/**
	 * 添加评论
	 * @author zxtan
	 * @date 2017-05-03
	 */
	public boolean addComment(String memberId,String target,String targetId, String parentId,String commentType,String content){
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
		if(null == member) return false;
		
		CommunityComment info = new CommunityComment();
		info.setId(null);
		info.setTarget(target);
		info.setTargetId(targetId);
		info.setCreator(member);
		info.setCreateTime(new Date());
		info.setCommentType(commentType);
		if(null != parentId && !"".equals(parentId)){
			CommunityComment parent = (CommunityComment) baseDao.get(CommunityComment.class, parentId);
			info.setParent(parent);
			
			while (null != parent && null != parent.getId()) {
				int commentCount = parent.getCommentCount()==null?1:parent.getCommentCount()+1;
				parent.setCommentCount(commentCount);
				baseDao.saveOrUpdate(parent);
				
				parent = parent.getParent();
			}			
		}
		
		info.setContent(content);
		info.setStatus(1);
		baseDao.saveOrUpdate(info);
				
		if("topic".equalsIgnoreCase(target)){
			CommunityTopic topic = (CommunityTopic) baseDao.get(CommunityTopic.class, targetId);
			int commentCount = topic.getCommentCount()==null?1:topic.getCommentCount()+1;
			topic.setCommentCount(commentCount);
			baseDao.saveOrUpdate(topic);
		}else if("question".equalsIgnoreCase(target)){
			CommunityQuestion question = (CommunityQuestion) baseDao.get(CommunityQuestion.class, targetId);
			if("comment".equalsIgnoreCase(commentType)){
				int commentCount = question.getCommentCount()==null?1:question.getCommentCount()+1;
				question.setCommentCount(commentCount);
				baseDao.saveOrUpdate(question);
			}else if("answer".equalsIgnoreCase(commentType)){
				int answerCount = question.getAnswerCount()==null?1:question.getAnswerCount()+1;
				question.setAnswerCount(answerCount);
				question.setIsAnswer(1);
				baseDao.saveOrUpdate(question);
			}
		}
		
		return true;
	}
	
	
	/**
	 * 得到帖子信息
	 * @author zxtan
	 * @date 2017-05-18
	 * @return
	 */
	public AppCommunityQuestionVO findQuestionInfo(String memberId,String questionId,String langCode){
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityQuestion t ");
		hql.append(" left join CommunityBehavior b on b.target='question' and b.targetId=t.id and b.behaviorType in ('like','unlike') and b.creator.id=? ");
		hql.append(" where t.status='1' and t.id=? ");

		List params = new ArrayList();
		params.add(memberId);
		params.add(questionId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);

		AppCommunityQuestionVO vo = new AppCommunityQuestionVO();
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			CommunityQuestion info = (CommunityQuestion)objs[0];
			
			vo.setQuestionId(info.getId());
			vo.setQuestionTitle(info.getTitle());
			
			if(null != info.getCreator()){
				vo.setMemberId(info.getCreator().getId());
				vo.setMemberType(String.valueOf(info.getCreator().getMemberType()));
				if(info.getCreator().getMemberType()==2){
					vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					vo.setMemberName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
				}
				vo.setMemberIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(), null));
			}
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
			
			vo.setCommentCount(String.valueOf(info.getCommentCount()));
			vo.setReadCount(String.valueOf(info.getReadCount()));
			vo.setLikeCount(String.valueOf(info.getLikeCount()));
			vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
			vo.setAnswerCount(String.valueOf(info.getAnswerCount()));
			vo.setIsAnswer(String.valueOf(info.getIsAnswer()));
			vo.setIsClose(String.valueOf(info.getIsClose()));
			vo.setSuportComment(String.valueOf(info.getSupportComment()));
			if(null != objs[1]){
				CommunityBehavior behavior = (CommunityBehavior) objs[1];
				vo.setBehaviorType(behavior.getBehaviorType());
			}
    	}
		
		return vo;
	}

}
