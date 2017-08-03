package com.fsll.wmes.community.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.exception.commonException;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.CommunityCommentVO;
import com.fsll.wmes.community.vo.CommunitySectionVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.community.vo.FrontPopularityRankVO;
import com.fsll.wmes.community.vo.FrontSearchUserVO;
import com.fsll.wmes.community.vo.QuestionDetailVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.community.vo.TopicShareParamtersVO;
import com.fsll.wmes.entity.CommunityBehavior;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.CommunityContent;
import com.fsll.wmes.entity.CommunityFocus;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.SysSearchHistory;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.ifa.vo.InsightVistorVo;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.news.service.NewsInfoService;
import com.fsll.wmes.news.vo.NewsCommentVO;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;


/**
 * 社区服务接口实现类
 * @author wwlin
 * @date 2017-03-12
 */
@Service("communityService")
public class CommunityServiceImpl extends BaseService implements CommunityService {
	
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private CommunitySpaceService communitySpaceService;
	@Autowired
	private NewsInfoService newsInfoService;
	/**
	 * 分享新闻、贴子、组合、策略等到贴子
	 * @return
	 */
	public CommunityTopic saveTopicFromShare(TopicShareParamtersVO paramVO){
		//添加贴子信息
		CommunityTopic topic = new CommunityTopic();
		topic.setCreateTime(new Date());
		if(paramVO.getCreator()!=null&&StringUtils.isNotBlank(paramVO.getCreator().getId()))topic.setCreator(paramVO.getCreator());
		if(StringUtils.isNotBlank(paramVO.getSectionId())){
			CommunitySection section = new CommunitySection();
			section.setId(paramVO.getSectionId());
			topic.setSection(section);
		}
		topic.setSourceId(paramVO.getSourceId());
		topic.setSourceType(paramVO.getSourceType());
		topic.setTitle(paramVO.getTitle());
		topic.setVisitor(paramVO.getVisitor());
		topic.setStatus(1);
		topic = (CommunityTopic)this.baseDao.create(topic);
		//添加贴子内容
		if(StringUtils.isNotBlank(topic.getId())){
			CommunityContent content = new CommunityContent();
			content.setId(topic.getId());
			content.setContent(paramVO.getContent());
			content = (CommunityContent)this.baseDao.create(content);
		}
		
		return topic;
		
	}
	
	/**
	 * 获取我的Dynamic贴子数据（我发的贴子跟我关注的人发的贴子）
	 * @return
	 */
	public void getMyDynamicTopic(String memberId){
		
		
	}
	
	/**
	 * 获取我的Dynamic贴子数据分页数据（我发的贴子跟我关注的人发的贴子）
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getMyDynamicTopicList(JsonPaging jsonPaging,MemberBase member,List<String>focusList,String keywords,String lang) {
		List params = new ArrayList();
		/*String hql = " SELECT r.id,r.title,r.createTime,r.creator.id,c.content,r.commentCount,r.readCount,r.likeCount,r.unlikeCount,r.section.id,r.sourceId,r.sourceType ";
		hql+="FROM CommunityTopic r,CommunityContent c WHERE r.id=c.id and ( r.creator.id IN(SELECT f.focus.id FROM CommunityFocus f WHERE f.creator.id='"+memberId+"') or r.creator.id='"+memberId+"' ) ";
		hql+="and CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.`creator_id`)";
		hql+=" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=t.`creator_id`) ELSE 1=1 END";
		params.add(memberId);
		params.add(memberId);
		
		//hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		//hql += " group by t.member.id ";
		if(StringUtils.isNotBlank(keywords)){
			hql += " and r.title like ? ";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY  CASE WHEN r.creator.id='"+memberId+"' THEN 1 ELSE 2 END ASC,r.createTime DESC ";*/
		
		StringBuilder sql=new StringBuilder();
		sql.append(" select t.*,m.`content` from community_topic t left join community_content m on t.id=m.id ");
		sql.append(" where t.creator_id=? or (t.creator_id in (select f.focus_id from community_focus f where f.creator_id=? ) and CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.`creator_id`)");
		sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=t.`creator_id`) ELSE 1=1 END)");
		params.add(member.getId());
		params.add(member.getId());
		params.add(member.getId());
		params.add(member.getId());
		if(StringUtils.isNotBlank(keywords)){
			sql.append( " and t.title like ? ");
			params.add("%"+keywords+"%");
		}
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" t.create_time");
         jsonPaging=this.springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		//jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontCommunityTopicVO> voList = new ArrayList<FrontCommunityTopicVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontCommunityTopicVO vo = new FrontCommunityTopicVO();//每个前端实体
			HashMap map=(HashMap)jsonPaging.getList().get(z);
			//Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = getMapValue(map, "id");//objs[0]==null?"":objs[0].toString();
			String title =getMapValue(map, "title");// objs[1]==null?"":objs[1].toString();
			String createTime =getMapValue(map, "create_time");// objs[2]==null?"":objs[2].toString();
			String creatorId =getMapValue(map, "creator_id");// objs[3]==null?"":objs[3].toString();
			String content = getMapValue(map, "content");//objs[4]==null?"":objs[4].toString();
			String commentCount =getMapValue(map, "comment_count");// objs[5]==null?"0":objs[5].toString();
			String readCount =getMapValue(map, "read_count");// objs[6]==null?"0":objs[6].toString();
			String likeCount =getMapValue(map, "like_count");// objs[7]==null?"0":objs[7].toString();
			String unlikeCount =getMapValue(map, "unlike_count");// objs[8]==null?"0":objs[8].toString();
			String sectionId =getMapValue(map, "section_id");// objs[9]==null?"":objs[9].toString();
			String sourceId=getMapValue(map, "source_id");//objs[10]==null?"":objs[10].toString();
			String sourceType=getMapValue(map, "source_type");//objs[11]==null?"":objs[11].toString();

			vo.setId(id);
			vo.setTitle(StringEscapeUtils.unescapeHtml(title));
			vo.setContent(content);
			vo.setCommentCount(commentCount);
			vo.setReadCount(readCount);
			vo.setLikeCount(likeCount);
			vo.setUnlikeCount(unlikeCount);
			
			if(null!=sourceId && !"".equals(sourceId)){
				vo.setIsShare("1");
			}else {
				vo.setIsShare("0");
			}
			vo.setSourceType(sourceType);
			//格式化时间格式
			if(!"".equals(createTime)){
				Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,member.getDateFormat()));
			}
			//发布人的头像与名称
			if(!"".equals(creatorId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(creatorId);
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				
				vo.setMemberType(user.getMemberType().toString());
			}
			//获取版块名称
			if(StringUtils.isNotBlank(sectionId)){
				CommunitySection section = getCommunitySectionInfo(sectionId);
				if(null!=section&&StringUtils.isNotBlank(section.getId())){
					vo.setSectionId(section.getId());
					if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
					else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
					else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
				}
			}
			//查看是否点赞或彩
			CommunityBehavior behavior=findBehavior(id, member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			if(null!=behavior){
				vo.setIsLike(1);
			}
			 behavior=findBehavior(id, member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			 if(null!=behavior){
					vo.setIsUnlike(1);
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
	 * 获取各个版块的贴子列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getSetionTypeTopicListJson(JsonPaging jsonPaging,MemberBase member,String keywords,String lang,String sectionId) {
		List params = new ArrayList();
		String hql = " SELECT r.id,r.title,r.createTime,r.creator.id,c.content,r.commentCount,r.readCount,r.likeCount,r.unlikeCount,r.section.id,r.sourceId,r.sourceType";
		hql+=" FROM CommunityTopic r,CommunityContent c WHERE r.id=c.id and r.section.id=? ";
		//hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		//hql += " group by t.member.id ";
		params.add(sectionId);
		if(StringUtils.isNotBlank(keywords)){
			hql += " and r.title like ?";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY r.createTime DESC ";

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontCommunityTopicVO> voList = new ArrayList<FrontCommunityTopicVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontCommunityTopicVO vo = new FrontCommunityTopicVO();//每个前端实体
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = objs[0]==null?"":objs[0].toString();
			String title = objs[1]==null?"":objs[1].toString();
			String createTime = objs[2]==null?"":objs[2].toString();
			String creatorId = objs[3]==null?"":objs[3].toString();
			String content = objs[4]==null?"":objs[4].toString();
			String commentCount = objs[5]==null?"0":objs[5].toString();
			String readCount = objs[6]==null?"0":objs[6].toString();
			String likeCount = objs[7]==null?"0":objs[7].toString();
			String unlikeCount = objs[8]==null?"0":objs[8].toString();
			String sourceId= objs[9]==null?"0":objs[9].toString();
			String sourceType=objs[10]==null?"0":objs[10].toString();

			vo.setId(id);
			vo.setTitle(StringEscapeUtils.unescapeHtml(title));
			vo.setContent(content);
			vo.setCommentCount(commentCount);
			vo.setReadCount(readCount);
			vo.setLikeCount(likeCount);
			vo.setUnlikeCount(unlikeCount);
			
			if(null!=sourceId && !"".equals(sourceId)){
				vo.setIsShare("1");
			}else {
				vo.setIsShare("0");
			}
			vo.setSourceType(sourceType);
			
			//格式化时间格式
			if(!"".equals(createTime)){
				Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,member.getDateFormat()));
			}
			//发布人的头像与名称
			if(!"".equals(creatorId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(creatorId);
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());
			}
			//获取版块名称
			if(StringUtils.isNotBlank(sectionId)){
				CommunitySection section = getCommunitySectionInfo(sectionId);
				if(null!=section&&StringUtils.isNotBlank(section.getId())){
					vo.setSectionId(section.getId());
					if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
					else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
					else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
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
	 * 获取各个版块的贴子列表
	 * @author wwlin 2017-03-17
	 * @param jsonPaging
	 * @param loginMemberId 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging getSetionTypeTopicListJson2(JsonPaging jsonPaging, MemberBase member,String lang,String keyWords,String sectionId) {
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.*,e.content FROM `community_topic` t left join `community_content` e on e.id=t.id   WHERE  t.status=1 and t.section_id=? ");
		sql.append("AND (t.creator_id=? or ( CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.`creator_id`)");
		sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=t.`creator_id`) ELSE 1=1 END))");
		List<Object> params=new ArrayList<Object>();
		params.add(sectionId);
		params.add(member.getId());
		params.add(member.getId());
		params.add(member.getId());
		if(StringUtils.isNotBlank(keyWords)){
			sql.append(" and t.title like ?");
			params.add("%"+keyWords+"%");
		}
		sql.append(" ORDER BY t.create_time DESC ");//System.out.println(sql.toString());
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String creatorId=getMapValue(map, "creator_id");
				String likeCount=getMapValue(map, "like_count");
				String unlikeCount=getMapValue(map, "unlike_count");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				String sourceId= getMapValue(map, "source_id");
				String sourceType=getMapValue(map, "source_type");
				
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  this.getCommunitySectionInfo(sectionId); 
				if(null!=section) vo.setSectionName(section.getSectionNameEn());
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(readCount);
				vo.setLikeCount(StringUtils.isNotBlank(likeCount)==true?likeCount:"0");
				vo.setUnlikeCount(StringUtils.isNotBlank(unlikeCount)==true?unlikeCount:"0");
				
				
				if(null!=sourceId && !"".equals(sourceId)){
					vo.setIsShare("1");
				}else {
					vo.setIsShare("0");
				}
				vo.setSourceType(sourceType);
				
				String newContent = delHTMLTag(content);
				if(newContent.length()>150)newContent = newContent.substring(0, 150);
				vo.setContent(content);
				
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
				}
				
				//发布人的头像与名称
				if(!"".equals(creatorId)){
					MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
					String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
					vo.setMemberId(creatorId);
					vo.setUserHeadUrl(userHeadUrl);
					if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
						String nickName=getCommonMemberName(user.getId(), lang, "2");
						vo.setNickName(nickName);
					}else {
						vo.setNickName(user.getNickName());
					}
					//vo.setNickName(user.getNickName());
					vo.setMemberType(user.getMemberType().toString());
				}
				
				//获取版块名称
				//if(StringUtils.isNotBlank(sectionId)){
					if(StringUtils.isNotBlank(sectionId) && null!=section&&StringUtils.isNotBlank(section.getId())){
						vo.setSectionId(section.getId());
						if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
						else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
						else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
					}
				//}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		
		return jsonPaging;
	}
	
	/**
	 * 获取贴子分页列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getTopicListJson(JsonPaging jsonPaging,String memberId,String keywords,String lang,Boolean isInsight) {
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.*,e.content FROM `community_topic` t,`community_content` e  WHERE e.id=t.id and t.status=1 ");
		sql.append("AND (t.creator_id =? or  CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.`creator_id`)");
		sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=t.`creator_id`) ELSE 1=1 END)");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		if(isInsight==true)sql.append(" and t.is_insight = '1' ");
		if(StringUtils.isNotBlank(keywords)){
			sql.append(" and t.title like ?");
			params.add("%"+keywords+"%");
		}
		sql.append(" ORDER BY t.create_time DESC ");
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String sectionId=getMapValue(map, "section_id");
				String creatorId=getMapValue(map, "creator_id");
				String likeCount=getMapValue(map, "like_count");
				String unlikeCount=getMapValue(map, "unlike_count");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  this.getCommunitySectionInfo(sectionId); 
				if(null!=section) vo.setSectionName(section.getSectionNameEn());
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(readCount);
				vo.setLikeCount(StringUtils.isNotBlank(likeCount)==true?likeCount:"0");
				vo.setUnlikeCount(StringUtils.isNotBlank(unlikeCount)==true?unlikeCount:"0");
				
				String newContent = delHTMLTag(content);
				if(newContent.length()>150)newContent = newContent.substring(0, 150);
				vo.setContent(newContent);
				
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
				}
				
				//发布人的头像与名称
				if(!"".equals(creatorId)){
					MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
					String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
					vo.setMemberId(creatorId);
					vo.setUserHeadUrl(userHeadUrl);
					if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
						String nickName=getCommonMemberName(user.getId(), lang, "2");
						vo.setNickName(nickName);
					}else {
						vo.setNickName(user.getNickName());
					}
					//vo.setNickName(user.getNickName());
					vo.setMemberType(user.getMemberType().toString());
				}
				
				//获取版块名称
				if(StringUtils.isNotBlank(sectionId)&&null!=section&&StringUtils.isNotBlank(section.getId())){
					//if(null!=section&&StringUtils.isNotBlank(section.getId())){
						vo.setSectionId(section.getId());
						if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
						else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
						else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
					//}
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		
		return jsonPaging;
	}
	
	/**
	 * 获取管理员推荐的贴子列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getRecommandTopicListJson(JsonPaging jsonPaging,MemberBase member,String keywords,String lang) {
		List params = new ArrayList();
		String hql = " SELECT r.id,r.title,r.createTime,r.creator.id,c.content,r.commentCount,r.readCount,r.likeCount,r.unlikeCount,r.section.id FROM CommunityTopic r,CommunityContent c WHERE r.id=c.id and r.isRecommand=? ";
		//hql +=" from WebInvestorVisit t where t.relateId=? and t.moduleType=? ";
		//hql += " group by t.member.id ";
		params.add(1);
		if(StringUtils.isNotBlank(keywords)){
			hql += " and r.title like ?";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY r.createTime DESC ";

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontCommunityTopicVO> voList = new ArrayList<FrontCommunityTopicVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontCommunityTopicVO vo = new FrontCommunityTopicVO();//每个前端实体
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = objs[0]==null?"":objs[0].toString();
			String title = objs[1]==null?"":objs[1].toString();
			String createTime = objs[2]==null?"":objs[2].toString();
			String creatorId = objs[3]==null?"":objs[3].toString();
			String content = objs[4]==null?"":objs[4].toString();
			String commentCount = objs[5]==null?"0":objs[5].toString();
			String readCount = objs[6]==null?"0":objs[6].toString();
			String likeCount = objs[7]==null?"0":objs[7].toString();
			String unlikeCount = objs[8]==null?"0":objs[8].toString();
			String sectionId = objs[9]==null?"":objs[9].toString();

			vo.setId(id);
			vo.setTitle(StringEscapeUtils.unescapeHtml(title));
			vo.setContent(content);
			vo.setCommentCount(commentCount);
			vo.setReadCount(readCount);
			vo.setLikeCount(likeCount);
			vo.setUnlikeCount(unlikeCount);
			//格式化时间格式
			if(!"".equals(createTime)){
				Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,member.getDateFormat()));
			}
			//发布人的头像与名称
			if(!"".equals(creatorId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(creatorId);
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());
			}
			//获取版块名称
			if(StringUtils.isNotBlank(sectionId)){
				CommunitySection section = getCommunitySectionInfo(sectionId);
				if(null!=section&&StringUtils.isNotBlank(section.getId())){
					vo.setSectionId(section.getId());
					if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
					else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
					else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
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
	 * 获取首页昨天排行回复贴子最高的前面5位
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getYesterdayTopCommentTopicCreator(String loginMemberId,String langCode) {
		List params = new ArrayList();
		/*String hql = " SELECT COUNT(*) , b.creator.id  FROM CommunityComment a,CommunityTopic b  "
					+" WHERE a.targetId=b.id AND TO_DAYS( NOW( ) )-TO_DAYS( a.createTime)= 1 AND a.target='topic'  "
					+" GROUP BY b.creator.id "
					+" ORDER BY COUNT(*) DESC";
		JsonPaging page = new JsonPaging();
		page.setPage(1);
		page.setRows(5);
		page.setTotal(5);
		page = this.baseDao.selectJsonPaging(hql, params.toArray(), page, false);*/
		//page = springJdbcQueryManager.springJdbcQueryForPaging(hql, null, page);
		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT t.creator_id,COUNT(*)  FROM community_topic t ");
		sql.append(" LEFT JOIN  community_comment c  ON t.id=c.target_id ");
		sql.append("  WHERE TO_DAYS(NOW())-TO_DAYS(c.`create_time`)<=1  AND  c.target='topic' ");
		sql.append(" AND ( t.creator_id=? or CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.creator_id) WHEN 'client' THEN ? IN (SELECT c.");
		sql.append(" `member_id` FROM crm_customer c LEFT JOIN member_ifa i ON c.ifa_id=i.id WHERE i.`member_id`=t.creator_id) ELSE 1=1 END )  GROUP BY t.`creator_id` ORDER BY COUNT(*) DESC");
		sql.append(" limit 5");
		params.add(loginMemberId);
		params.add(loginMemberId);
		params.add(loginMemberId);
		//params.add(loginMemberId);
		
		
		List list=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		
		//显示的前端实体列表
		List<FrontPopularityRankVO> voList = new ArrayList<FrontPopularityRankVO>();
		if(null==list || list.isEmpty())return null;
		for(int z=0;z<list.size();z++){
			FrontPopularityRankVO vo = new FrontPopularityRankVO();//每个前端实体
			/*Object[] objs = (Object[])list.get(z);
			String commentCount = objs[0]==null?"":objs[0].toString();
			String creatorId = objs[1]==null?"":objs[1].toString();*/
			HashMap map=(HashMap)list.get(z);
			String creatorId=getMapValue(map, "creator_id");
			
			if(StringUtils.isNotBlank(creatorId)){
				MemberBase user = memberBaseService.findById(creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(user.getId());
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), langCode, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());
				
				Integer followCount = communitySpaceService.getFollowCount(creatorId);
				vo.setFollowersCount(followCount.toString());
				
				vo.setActivity("Hight");
				Integer publishTopicCount = this.findPublishTopicCountByMemberId(creatorId);
				vo.setTopicCount(publishTopicCount.toString());
				
			}

			voList.add(vo);
		}
		//jsonPaging.getList().clear();
		//jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		// modify end
		
		return voList;
	}
	
	/**
	 * 获取首页 统计上周的数据，上周他帖子回帖数
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getWeekTopCommentTopicCreator(String loginMemberId,String langCode) {
		List params = new ArrayList();
		/*String hql = " SELECT COUNT(*) , b.creator.id  FROM CommunityComment a,CommunityTopic b  "
					+" WHERE a.targetId=b.id AND TO_DAYS( NOW( ) )-TO_DAYS( a.createTime)<= 7 AND a.target='topic'  "
					+" GROUP BY b.creator.id "
					+" ORDER BY COUNT(*) DESC";
		JsonPaging page = new JsonPaging();
		page.setPage(1);
		page.setRows(5);
		page.setTotal(5);
		page = this.baseDao.selectJsonPaging(hql, params.toArray(), page, false);*/
		//page = springJdbcQueryManager.springJdbcQueryForPaging(hql, null, page);
		
		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT t.creator_id,COUNT(*)  FROM community_topic t ");
		sql.append(" LEFT JOIN  community_comment c  ON t.id=c.target_id ");
		sql.append("  WHERE TO_DAYS(NOW())-TO_DAYS(c.`create_time`)<=7 AND  c.target='topic' ");
		sql.append(" AND ( t.creator_id=? or  CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.creator_id) WHEN 'client' THEN ? IN (SELECT c.");
		sql.append(" `member_id` FROM crm_customer c LEFT JOIN member_ifa i ON c.ifa_id=i.id WHERE i.`member_id`=t.creator_id) ELSE 1=1 END)   GROUP BY t.`creator_id` ORDER BY COUNT(*) DESC");
		sql.append(" limit 5");
		params.add(loginMemberId);
		params.add(loginMemberId);
		params.add(loginMemberId);
		/*params.add(loginMemberId);
		params.add(loginMemberId);*/
		List list=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		
		//显示的前端实体列表
		List<FrontPopularityRankVO> voList = new ArrayList<FrontPopularityRankVO>();
		if(null==list || list.isEmpty())return null;
		for(int z=0;z<list.size();z++){
			FrontPopularityRankVO vo = new FrontPopularityRankVO();//每个前端实体
			/*Object[] objs = (Object[])list.get(z);
			String commentCount = objs[0]==null?"":objs[0].toString();
			String creatorId = objs[1]==null?"":objs[1].toString();*/
		
			HashMap map=(HashMap)list.get(z);
			String creatorId=getMapValue(map, "creator_id");
			
			if(StringUtils.isNotBlank(creatorId)){
				MemberBase user = memberBaseService.findById(creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(user.getId());
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), langCode, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());

				Integer followCount = communitySpaceService.getFollowCount(creatorId);
				vo.setFollowersCount(followCount.toString());
				
				vo.setActivity("Hight");
				Integer publishTopicCount = this.findPublishTopicCountByMemberId(creatorId);
				vo.setTopicCount(publishTopicCount.toString());
				
			}

			voList.add(vo);
		}
		
		return voList;
	}
	
	/**
	 * 获取首页 总排名：统计截止到昨日（1年内，可设置）的数据，他帖子的回帖总数
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getTotalTopCommentTopicCreator(String loginMemberId,String langCode) {
		List params = new ArrayList();
		/*String hql = " SELECT COUNT(*),b.creator.id FROM CommunityComment a,CommunityTopic b  "
					+" WHERE a.targetId=b.id AND a.target='topic' "
					+" GROUP BY b.creator.id "
					+" ORDER BY COUNT(*) DESC";
		JsonPaging page = new JsonPaging();
		page.setPage(1);
		page.setRows(5);
		page.setTotal(5);
		page = this.baseDao.selectJsonPaging(hql, params.toArray(), page, false);*/
		
		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT t.creator_id,COUNT(*)  FROM community_topic t ");
		sql.append(" LEFT JOIN  community_comment c  ON t.id=c.target_id ");
		sql.append("  WHERE   c.target='topic' ");
		sql.append(" AND (t.creator_id=? or CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.creator_id) WHEN 'client' THEN ? IN (SELECT c.");
		sql.append(" `member_id` FROM crm_customer c LEFT JOIN member_ifa i ON c.ifa_id=i.id WHERE i.`member_id`=t.creator_id) ELSE 1=1 END )  GROUP BY t.`creator_id` ORDER BY COUNT(*) DESC");
		sql.append(" limit 5");
		params.add(loginMemberId);
		params.add(loginMemberId);
		params.add(loginMemberId);
		/*params.add(loginMemberId);
		params.add(loginMemberId);*/
		List list=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		
		//显示的前端实体列表
		List<FrontPopularityRankVO> voList = new ArrayList<FrontPopularityRankVO>();
		if(null==list || list.isEmpty())return null;
		for(int z=0;z<list.size();z++){
			FrontPopularityRankVO vo = new FrontPopularityRankVO();//每个前端实体
			/*Object[] objs = (Object[])page.getList().get(z);
			String commentCount = objs[0]==null?"":objs[0].toString();
			String creatorId = objs[1]==null?"":objs[1].toString();*/
			
			HashMap map=(HashMap)list.get(z);
			String creatorId=getMapValue(map, "creator_id");
			
			if(StringUtils.isNotBlank(creatorId)){
				MemberBase user = memberBaseService.findById(creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(user.getId());
				vo.setUserHeadUrl(userHeadUrl);
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), langCode, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());

				Integer followCount = communitySpaceService.getFollowCount(creatorId);
				vo.setFollowersCount(followCount.toString());
				
				vo.setActivity("Hight");
				Integer publishTopicCount = this.findPublishTopicCountByMemberId(creatorId);
				//System.out.println(publishTopicCount.toString());
				vo.setTopicCount(publishTopicCount.toString());
				
			}
			voList.add(vo);
		}
		
		return voList;
	}
	
	/**
	 * 获取首页统计截止到今日一周内最多人评论的帖子
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getWeekTopCommentTopicList(String lang,String loginMemberId) {
		List params = new ArrayList();
		//String hql = "  SELECT COUNT(*) AS countComment,r.targetId  FROM CommunityComment r WHERE TO_DAYS( NOW( ) )-TO_DAYS( r.createTime)<= 7 AND r.target='topic' GROUP BY r.targetId ORDER BY COUNT(*) DESC";
		
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.id,COUNT(*) ");
		sql.append(" from community_topic t left join  community_comment c  on t.id=c.target_id");
		sql.append(" left join member_base  m on t.creator_id=m.id ");
		sql.append(" left join community_section s on t.section_id=s.id");
		sql.append(" where TO_DAYS(NOW())-TO_DAYS(c.`create_time`)<=7 and  c.target='topic'");
		sql.append(" AND ( t.creator_id=? or CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=t.creator_id)");
		sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=t.creator_id) ELSE 1=1 END)");
		sql.append("   GROUP BY t.`id`");
		sql.append(" order by COUNT(*) desc limit 5");
		params.add(loginMemberId);
		params.add(loginMemberId);
		params.add(loginMemberId);
		/*params.add(loginMemberId);
		params.add(loginMemberId);*/
		
		JsonPaging page = new JsonPaging();
		page.setPage(1);
		page.setRows(5);
		//page.setTotal(5);
		//page = this.baseDao.selectJsonPaging(hql, params.toArray(), page, false);
		//page = springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), page);
		List list=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		//显示的前端实体列表
		List<FrontPopularityRankVO> voList = new ArrayList<FrontPopularityRankVO>();
		if(null==list || list.isEmpty())return null;
		for(int z=0;z<list.size();z++){
			FrontPopularityRankVO vo = new FrontPopularityRankVO();//每个前端实体
			/*Object[] objs = (Object[])page.getList().get(z);
			String commentCount = objs[0]==null?"":objs[0].toString();
			String topicId = objs[1]==null?"":objs[1].toString();*/
			HashMap<String, Object> map=(HashMap<String, Object>)list.get(z);
			String topicId=getMapValue(map, "id");
			if(StringUtils.isNotBlank(topicId)){
				CommunityTopic topic = this.findCommunityTopicById(topicId);
				if(null!=topic&&!topic.getId().isEmpty()){
					vo.setTopicId(topicId);
					MemberBase user = topic.getCreator();
					String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
					vo.setMemberId(user.getId());
					vo.setUserHeadUrl(userHeadUrl);
					
					if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
						String nickName=getCommonMemberName(user.getId(), lang, "2");
						vo.setNickName(nickName);
					}else {
						vo.setNickName(user.getNickName());
					}
					
					vo.setNickName(user.getNickName());
					vo.setMemberType(user.getMemberType().toString());
					
					if(null!=topic.getCommentCount())vo.setCommentCount(topic.getCommentCount().toString());
					else vo.setCommentCount("0");
					
					if(null!=topic.getReadCount())vo.setReadCount(topic.getReadCount().toString());
					else vo.setReadCount("0");
					
					vo.setTopicName(StringEscapeUtils.unescapeHtml(topic.getTitle()));
					if(null!=topic.getSection()&&!topic.getSection().getId().isEmpty()){
						if("en".equals(lang))vo.setSectionName(topic.getSection().getSectionNameEn());
						else if("sc".equals(lang))vo.setSectionName(topic.getSection().getSectionNameSc());
						else if("tc".equals(lang))vo.setSectionName(topic.getSection().getSectionNameTc());
					}
				}
			}

			voList.add(vo);
		}
		//jsonPaging.getList().clear();
		//jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		// modify end
		
		return voList;
	}
	
	
	/**
	 * 获取版块名称
	 * @author wwlin 
	 * @param id
	 * @return
	 */
	@Override
	public CommunitySection getCommunitySectionInfo(String id) {
		CommunitySection vo=(CommunitySection)this.baseDao.get(CommunitySection.class, id);
		return vo;
	}
	
	/**
	 * 获取贴子信息
	 * @author wwlin 
	 * @param id
	 * @return
	 */
	@Override
	public CommunityTopic findCommunityTopicById(String id) {
		CommunityTopic obj = (CommunityTopic) this.baseDao.get(CommunityTopic.class, id);
		return obj;
	}
	
	/**
	 * 保存贴子信息
	 * @author wwlin
	 * */
	public CommunityTopic updateCommunityTopic(CommunityTopic info) {
		return (CommunityTopic) this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 保存发布帖子
	 * @author mqzou 2017-03-14
	 * @param topic
	 * @param content
	 * @return
	 */
	@Override
	public CommunityTopic saveOrUpdateTopic(CommunityTopic topic, CommunityContent content) {
		topic=(CommunityTopic)baseDao.saveOrUpdate(topic);
		 if(null!=topic && null!=content){
			 content.setId(topic.getId());
			 content=(CommunityContent)baseDao.create(content);
			 //如果是转发帖子的，则需要更新被转发的帖子的转发数
			 if(null!=topic.getSourceId() && !"".equals(topic.getSourceId()) && CommonConstantsWeb.TOPIC_SOURCE_TYPE_TOPIC==topic.getSourceType()){
				 updateTransferCount(topic.getSourceId());
			 }
			 //如果是ifa,则计算人气值
			 if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==topic.getCreator().getMemberType()){
				 updateIfaPopularity(topic.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_PUBLIC);
			 }
		 }
		return topic;
	}
	
	/**
	 * 递归更新帖子的被转发数
	 * @author mqzou 2017-03-17
	 * @param topicId
	 */
	private void updateTransferCount(String topicId){
		CommunityTopic topic=findCommunityTopicById(topicId);
		if(null!=topic){
			int count=null!=topic.getTansferCount()?topic.getTansferCount():0;
			topic.setTansferCount(count+1);
			baseDao.saveOrUpdate(topic);
			if(null!=topic.getSourceId() && !"".equals(topic.getSourceId()) && CommonConstantsWeb.TOPIC_SOURCE_TYPE_TOPIC==topic.getSourceType()){
				 updateTransferCount(topic.getSourceId());
			 }
		}
	}
	
	/**
	 * 获取社区版块（供选择使用）
	 * @author mqzou
	 * @param langCode
	 * @return
	 */
	@Override
	public List<CommunitySectionVO> findSectionForSelect(String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunitySection r where  r.status='1' order by r.orderBy asc ");
		List resultList=baseDao.find(hql.toString(), null, false);
		List<CommunitySectionVO> list=new ArrayList<CommunitySectionVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CommunitySection section = (CommunitySection) it.next();
				CommunitySectionVO vo=new CommunitySectionVO();
				vo.setId(section.getId());
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					vo.setName(section.getSectionNameEn());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setName(section.getSectionNameSc());
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setName(section.getSectionNameTc());
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取帖子的详情
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param langCode
	 * @param member
	 * @return
	 */
	@Override
	public TopicDetailVO findTopicDetail(String id, String langCode, MemberBase member) {
		CommunityTopic topic=findCommunityTopicById(id);
		CommunityContent content=findCommunityContent(id);
		if(null!=topic){
			TopicDetailVO vo=new TopicDetailVO();
			vo.setClick(null!=topic.getReadCount()?topic.getReadCount():0);
			vo.setCommentCount(null!=topic.getCommentCount()?topic.getCommentCount():0);
			if(null!=content)vo.setContent(content.getContent());
			vo.setCreateDate(DateUtil.dateToDateString(topic.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
			
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==topic.getCreator().getSubMemberType()){
				String nickName=getCommonMemberName(topic.getCreator().getId(), langCode, "2");
				vo.setCreatorName(nickName);
			}else {
				vo.setCreatorName(topic.getCreator().getNickName());
			}
			//vo.setCreatorName(topic.getCreator().getNickName());
			vo.setCreatorId(topic.getCreator().getId());
			vo.setCreatorMemberType(topic.getCreator().getMemberType().toString());
			vo.setCreatorUrl(topic.getCreator().getIconUrl());
			vo.setId(id);
			vo.setLike(null!=topic.getLikeCount()?topic.getLikeCount():0);
			
			if(null!=topic.getSection()){
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					vo.setSection(topic.getSection().getSectionNameEn());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					vo.setSection(topic.getSection().getSectionNameSc());
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					vo.setSection(topic.getSection().getSectionNameTc());
				}
				
			}
			vo.setSupportComment(null!=topic.getSupportComment()?topic.getSupportComment():1);
			vo.setTitle(StringEscapeUtils.unescapeHtml(topic.getTitle()));
			vo.setUnlike(null!=topic.getUnlikeCount()?topic.getUnlikeCount():0);
		    CommunityBehavior behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_FAVORITE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
		    if(null!=behavior)
		    	vo.setIsFavorite("1");
		    behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
		    if(null!=behavior)
		    	vo.setIsLike("1");
		    behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
		    if(null!=behavior){
		    	vo.setIsUnlike("1");
		    }
		    if(topic.getCreator().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA){
		    	MemberIfa ifa=memberBaseService.findIfaMember(topic.getCreator().getId());
		    	vo.setCreatorGender(ifa.getGender());
		    }else if (topic.getCreator().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL) {
				MemberIndividual individual=memberBaseService.findIndividualMember(topic.getCreator().getId());
				vo.setCreatorGender(individual.getGender());
			}
		    
		    if(null!=topic.getSourceId() && !"".equals(topic.getSourceId())){
		    	if(CommonConstantsWeb.TOPIC_SOURCE_TYPE_TOPIC.equals(topic.getSourceType())){
		    		vo.setTopicType(1);//转发
		    		CommunityContent sourceContent=findCommunityContent(topic.getSourceId());
		    		vo.setSourceContent(null!=sourceContent?StringEscapeUtils.unescapeHtml(sourceContent.getContent()):"");
		    		CommunityTopic sourceTopic=findCommunityTopicById(topic.getSourceId());
		    		vo.setSourceMemberName(sourceTopic.getCreator().getNickName());
		    	}else {
		    		vo.setTopicType(2);//分享
		    		NewsInfo newsInfo=newsInfoService.findNewsInfo(topic.getSourceId());
		    		vo.setNewsdescription(newsInfo.getDescription());
		    		vo.setNewsUrl(newsInfo.getLitPic());
		    		
				}
		    	vo.setSourceId(topic.getSourceId());
		    	vo.setSourceType(topic.getSourceType());
		    	
		    }else{
		    	vo.setTopicType(0);//原创
		    }
		    vo.setVisitor(topic.getVisitor());
			return vo;
		}
		
		return null;
	}
	
	/**
	 * 获取帖子的内容
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	@Override
	public CommunityContent findCommunityContent(String id) {
		CommunityContent content=(CommunityContent)baseDao.get(CommunityContent.class, id);
		return content;
	}
	
	/**
	 * 获取帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public CommunityBehavior findBehavior(String id, String memberId, String type,String target) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityBehavior r where r.targetId=? and r.creator.id=? and r.behaviorType=? and r.target=?");
		List<Object> params=new ArrayList<Object>();
		params.add(id);
		params.add(memberId);
		params.add(type);
		params.add(target);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			CommunityBehavior behavior=(CommunityBehavior)list.get(0);
			return behavior;
		}
		
		return null;
	}
	
	/**
	 * 保存帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param behavior
	 * @return
	 */
	@Override
	public CommunityBehavior saveBehavior(CommunityBehavior behavior) {
		
		//帖子
		if(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC.equals(behavior.getTarget())){
			CommunityTopic topic=findCommunityTopicById(behavior.getTargetId());
			
			if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){//点赞
				
				int unlike =null!=topic.getUnlikeCount()?topic.getUnlikeCount():0;
				int like=null!=topic.getLikeCount()?topic.getLikeCount():0;
				CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(),  CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				if(null!=communityBehavior){
					this.baseDao.delete(communityBehavior);
					topic.setUnlikeCount(unlike-1);
				}
				topic.setLikeCount(like+1);
				//ifa人气值计算
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==topic.getCreator().getMemberType()){
					updateIfaPopularity(topic.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_NICE_OTHER);
				}
				
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {//踩
				int like =null!=topic.getLikeCount()?topic.getLikeCount():0;
				int unlike =null!=topic.getUnlikeCount()?topic.getUnlikeCount():0;
				CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				if(null!=communityBehavior){
					//
					this.baseDao.delete(communityBehavior);
					topic.setLikeCount(like-1);
				}
				topic.setUnlikeCount(unlike+1);
				
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_FAVORITE.equals(behavior.getBehaviorType())) {//收藏
				int favorite=null!=topic.getFavoriteCount()?topic.getFavoriteCount():0;
				topic.setFavoriteCount(favorite+1);
				//ifa人气值计算
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==topic.getCreator().getMemberType()){
					updateIfaPopularity(topic.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_FAV_OTHER);
				}
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_READ.equals(behavior.getBehaviorType())) {//阅读
				int read=null!=topic.getReadCount()?topic.getReadCount():0;
				topic.setReadCount(read+1);
				//ifa人气值计算
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==topic.getCreator().getMemberType()){
					updateIfaPopularity(topic.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_READ_OTHER);
				}
			}
			baseDao.saveOrUpdate(topic);
			baseDao.saveOrUpdate(behavior);
			
		}
		//问题
		else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION.equals(behavior.getTarget())) {
             CommunityQuestion question=findCommunityQuestion(behavior.getTargetId());
			
			if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){//点赞
				int unlike =null!=question.getUnlikeCount()?question.getUnlikeCount():0;
				int like=null!=question.getLikeCount()?question.getLikeCount():0;
				CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(),CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
				if(null!=communityBehavior){
					this.baseDao.delete(communityBehavior);
					question.setUnlikeCount(unlike-1);
				}
				question.setLikeCount(like+1);
				
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {//踩
				int like =null!=question.getLikeCount()?question.getLikeCount():0;
				int unlike =null!=question.getUnlikeCount()?question.getUnlikeCount():0;
				CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(),  CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
				if(null!=communityBehavior){
					this.baseDao.delete(communityBehavior);
					question.setLikeCount(like-1);
				}
				question.setUnlikeCount(unlike+1);
				
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_READ.equals(behavior.getBehaviorType())) {//阅读
				int read=null!=question.getReadCount()?question.getReadCount():0;
				question.setReadCount(read+1);
			}
			baseDao.saveOrUpdate(question);
			baseDao.saveOrUpdate(behavior);
		}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT.equals(behavior.getTarget())) {
		    	CommunityComment comment=findCommunityComment(behavior.getTargetId());
				
				if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){//点赞
					int unlike =null!=comment.getUnlikeCount()?comment.getUnlikeCount():0;
					int like=null!=comment.getLikeCount()?comment.getLikeCount():0;
					CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(),CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
					if(null!=communityBehavior){
						this.baseDao.delete(communityBehavior);
						comment.setUnlikeCount(unlike-1);
					}
					comment.setLikeCount(like+1);
					
				}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {//踩
					int like =null!=comment.getLikeCount()?comment.getLikeCount():0;
					int unlike =null!=comment.getUnlikeCount()?comment.getUnlikeCount():0;
					CommunityBehavior communityBehavior=findBehavior(behavior.getTargetId(), behavior.getCreator().getId(),  CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
					if(null!=communityBehavior){
						this.baseDao.delete(communityBehavior);
						comment.setLikeCount(like-1);
					}
					comment.setUnlikeCount(unlike+1);
					
				}
				baseDao.saveOrUpdate(comment);
				baseDao.saveOrUpdate(behavior);
		}
		
		
		return behavior;
	}
	
	/**
	 * 删除帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param behavior
	 * @return
	 */
	@Override
	public CommunityBehavior deleteBehavior(CommunityBehavior behavior) {
		
		if(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC.equals(behavior.getTarget())){
			
			CommunityTopic topic=findCommunityTopicById(behavior.getTargetId());
			if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){
				int like=null!=topic.getLikeCount()?topic.getLikeCount():0;
				topic.setLikeCount(like-1);
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {
				int unlike=null!=topic.getUnlikeCount()?topic.getUnlikeCount():0;
				topic.setUnlikeCount(unlike-1);
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_FAVORITE.equals(behavior.getBehaviorType())) {
				int favorite=null!=topic.getFavoriteCount()?topic.getFavoriteCount():0;
				topic.setFavoriteCount(favorite-1);
			}
			baseDao.saveOrUpdate(topic);
		}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION.equals(behavior.getTarget())) {
			CommunityQuestion question=findCommunityQuestion(behavior.getTargetId());
			if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){
				int like=null!=question.getLikeCount()?question.getLikeCount():0;
				question.setLikeCount(like-1);
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {
				int unlike=null!=question.getUnlikeCount()?question.getUnlikeCount():0;
				question.setUnlikeCount(unlike-1);
			}
			baseDao.saveOrUpdate(question);
		}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT.equals(behavior.getTarget())) {
			CommunityComment comment=findCommunityComment(behavior.getTargetId());
			if(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(behavior.getBehaviorType())){
				int like=null!=comment.getLikeCount()?comment.getLikeCount():0;
				comment.setLikeCount(like-1);
			}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(behavior.getBehaviorType())) {
				int unlike=null!=comment.getUnlikeCount()?comment.getUnlikeCount():0;
				comment.setUnlikeCount(unlike-1);
			}
			baseDao.saveOrUpdate(comment);
		}
		baseDao.delete(behavior);
		return behavior;
	}
	
	/**
	 * 获取问题实体
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	@Override
	public CommunityQuestion findCommunityQuestion(String id) {
		CommunityQuestion vo=(CommunityQuestion)baseDao.get(CommunityQuestion.class, id);
		return vo;
	}
	
	/**
	 * 获取评论
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	@Override
	public CommunityComment findCommunityComment(String id) {
		CommunityComment vo=(CommunityComment)baseDao.get(CommunityComment.class, id);
		return vo;
	}
	
	/**
	 * 
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	public int findPublishTopicCountByMemberId(String memberId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" Select count(*) as count from CommunityTopic r where r.creator.id=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			//Object[] objects=(Object[])list.get(0);
			Object obj=list.get(0);
			if(null!=obj && !"".equals(obj.toString())){
				return Integer.parseInt(obj.toString());
			}
		}
		return 0;
		
		
	}
	
	/**
	 * 保存评论
	 * @author mqzou 2017-03-15
	 * @param comment
	 * @param target 评论目标的类型  CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC，COMUNITY_BEHAVIOR_TARGET_QUESTION，COMUNITY_BEHAVIOR_TARGET_COMMENT
	 * @return
	 */
	@Override
	public CommunityCommentVO saveComment(CommunityComment comment,Object object,String langCode) {
		comment=(CommunityComment)baseDao.saveOrUpdate(comment);
		if(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC.equals(comment.getTarget())){
			CommunityTopic topic=(CommunityTopic)object;
			if(null!=topic){
				int commentCount=null!=topic.getCommentCount()?topic.getCommentCount():0;
				topic.setCommentCount(commentCount+1);
				
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==comment.getCreator().getMemberType()){
					updateIfaPopularity(comment.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_REPLY_SELF);
				}
				
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==topic.getCreator().getMemberType()){
					updateIfaPopularity(topic.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_TOPIC_REPLY_OTHER);
				}
				
				//baseDao.saveOrUpdate(topic);
			}
		}else if (CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION.equals(comment.getTarget())) {
			if(null!=object){
				CommunityQuestion question=(CommunityQuestion)object;
				int commentCount=null!=question.getCommentCount()?question.getCommentCount():0;
				question.setCommentCount(commentCount+1);
				
				//ifa回复问题
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==comment.getCreator().getMemberType()){
					updateIfaPopularity(comment.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_QUESTION_REPLY_SELF);
				}
				
				//别人回复问题
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==question.getAnswerer().getMemberType()){
					updateIfaPopularity(comment.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_QUESTION_REPLY_OTER);
				}
				
				
			}else {
				CommunityQuestion question=findCommunityQuestion(comment.getTargetId());
				if(null!=question){
					question.setIsAnswer(1);
					//ifa回答问题
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==comment.getCreator().getMemberType()){
						updateIfaPopularity(comment.getCreator().getId(), CommonConstantsWeb.IFA_POPULARITY_QUESTION_ANSWER);
					}
					//baseDao.saveOrUpdate(question);
				}
			}
		}
		if(null!=comment.getParent()){
			saveCommentCount(comment.getParent());
		}
		CommunityCommentVO vo=new CommunityCommentVO();
		vo.setId(comment.getId());
		vo.setContent(comment.getContent());
		vo.setDateTime(DateUtil.getDateTimeGoneFormatStr(comment.getCreateTime(), langCode, ""));
		vo.setCommentCount(null!=comment.getCommentCount()?comment.getCommentCount():0);
		vo.setLikeCount(null!=comment.getLikeCount()?comment.getLikeCount():0);
		vo.setUnlikeCount(null!=comment.getUnlikeCount()?comment.getUnlikeCount():0);
		vo.setMemberId(comment.getCreator().getId());
		
		if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==comment.getCreator().getSubMemberType()){
			String nickName=getCommonMemberName(comment.getCreator().getId(), langCode, "2");
			vo.setMemberName(nickName);
		}else {
			vo.setMemberName(comment.getCreator().getNickName());
		}
		
		//vo.setMemberName(comment.getCreator().getNickName());
		vo.setMemberType(comment.getCreator().getMemberType().toString());
		String gender="";
		if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==comment.getCreator().getSubMemberType()){
			MemberIfa ifa=memberBaseService.findIfaMember(comment.getCreator().getId());
			gender=ifa.getGender();
		}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==comment.getCreator().getSubMemberType()) {
			MemberIndividual individual=memberBaseService.findIndividualMember(comment.getCreator().getId());
			gender=individual.getGender();
		}
		vo.setMemberUrl(PageHelper.getUserHeadUrl(comment.getCreator().getIconUrl(), gender));
		
		vo.setReplyId(null!=comment.getParent()?comment.getParent().getId():"");
		vo.setReplyMemberName(null!=comment.getParent()?comment.getParent().getCreator().getNickName():"");
		
		return vo;
	}
	
	/**
	 * 遍历保存评论的被评论数
	 * @author mqzou 2017-03-15
	 * @param comment
	 */
	private void saveCommentCount(CommunityComment comment){
		if(null!=comment){
			int commentCount=null!=comment.getCommentCount()?comment.getCommentCount():0;
			comment.setCommentCount(commentCount+1);
			comment=(CommunityComment)baseDao.saveOrUpdate(comment);
			if(null!=comment.getParent()){
				 saveCommentCount(comment.getParent());
			}
		}
	}
	
	/**
	 * 分页获取帖子/问题的评论
	 * @author mqzou 2017-03-15
	 * @param jsonPaging
	 * @param topicId
	 * @param langCode
	 * @param member
	 * @return
	 */
	@Override
	public JsonPaging findTopicComments(JsonPaging jsonPaging, String topicId, String langCode, MemberBase member) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityComment r where r.targetId=? and r.status!=0 and r.parent is null and r.commentType='comment' ");
		List<Object> params=new ArrayList<Object>();
		params.add(topicId);
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<CommunityCommentVO> list=new ArrayList<CommunityCommentVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CommunityComment comment = (CommunityComment) it.next();
				CommunityCommentVO vo=new CommunityCommentVO();
				vo.setId(comment.getId());
				vo.setContent(StringEscapeUtils.unescapeHtml(comment.getContent()));
				vo.setDateTime(DateUtil.getDateTimeGoneFormatStr(comment.getCreateTime(), langCode, ""));
				vo.setCommentCount(null!=comment.getCommentCount()?comment.getCommentCount():0);
				vo.setLikeCount(null!=comment.getLikeCount()?comment.getLikeCount():0);
				vo.setUnlikeCount(null!=comment.getUnlikeCount()?comment.getUnlikeCount():0);
				vo.setMemberId(comment.getCreator().getId());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==comment.getCreator().getSubMemberType()){
					String nickName=getCommonMemberName(comment.getCreator().getId(), langCode, "2");
					vo.setMemberName(nickName);
				}else {
					vo.setMemberName(comment.getCreator().getNickName());
				}
				
				//vo.setMemberName(comment.getCreator().getNickName());
				vo.setMemberType(comment.getCreator().getMemberType().toString());
				
				String gender="";
			    if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==comment.getCreator().getSubMemberType()){
			    	MemberIfa ifa=memberBaseService.findIfaMember(comment.getCreator().getId());
			    	gender=ifa.getGender();
			    }else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==comment.getCreator().getSubMemberType()) {
					MemberIndividual individual=memberBaseService.findIndividualMember(comment.getCreator().getId());
					gender=individual.getGender();
				}
				
				vo.setMemberUrl(PageHelper.getUserHeadUrl(comment.getCreator().getIconUrl(), gender));
				CommunityBehavior behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
				if(null!=behavior){
					vo.setIsLike(1);
				}
				 behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
				 if(null!=behavior){
						vo.setIsUnlike(1);
				}
				 vo.setTopicId(topicId);
				 vo.setStatus(comment.getStatus());
				 vo=findCommentReply(vo, langCode,member);
				 list.add(vo);
				 
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取评论的所有回复评论
	 * @author mqzou 2017-03-09
	 */
	private CommunityCommentVO findCommentReply(CommunityCommentVO commentVO,String langCode,MemberBase memberBase){
		if(null!=commentVO){
			String sql="CALL `pro_gettopicreply`(?,?,?)";
			List<Object> params=new ArrayList<Object>();
			params.add(commentVO.getId());
			params.add(commentVO.getTopicId());
			params.add(memberBase.getId());
			List resultList=springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
			List<CommunityCommentVO> list=new ArrayList<CommunityCommentVO>();
			if(null!=resultList && !resultList.isEmpty()){
				Iterator it=resultList.iterator();
				while (it.hasNext()) {
					HashMap<String, Object> map = (HashMap<String, Object>) it.next();
					CommunityCommentVO vo=new CommunityCommentVO();
					String id=getMapValue(map, "id");
					String content=getMapValue(map, "content");
					String likeCount=getMapValue(map, "like_count");
					String unlikeCount=getMapValue(map, "unlike_count");
					String commentCount=getMapValue(map, "comment_count");
					String topicId=getMapValue(map, "topic_id");
					String memberId=getMapValue(map, "creator_id");
					String opTime=getMapValue(map, "create_time");
					String replyId=getMapValue(map, "parent_comment_id");
					String like=getMapValue(map, "behavior_type");
					String status=getMapValue(map, "status");
					MemberBase member=memberBaseService.findById(memberId);
					
					CommunityComment reply=findCommunityComment(replyId);
					
					if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==reply.getCreator().getSubMemberType()){
						String nickName=getCommonMemberName(reply.getCreator().getId(), langCode, "2");
						vo.setReplyMemberName(nickName);
					}else {
						vo.setReplyMemberName(reply.getCreator().getNickName());
					}
					//vo.setReplyMemberName(reply.getCreator().getNickName());
					vo.setTopicId(topicId);
					vo.setId(id);
					vo.setContent(StringEscapeUtils.unescapeHtml(content));
					vo.setLikeCount(null!=likeCount && !"".equals(likeCount)?Integer.valueOf(likeCount):0);
					vo.setUnlikeCount(null!=unlikeCount && !"".equals(unlikeCount)?Integer.valueOf(unlikeCount):0);
					vo.setCommentCount(null!=commentCount && !"".equals(commentCount)?Integer.valueOf(commentCount):0);
					
					vo.setMemberId(memberId);
					
					if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
						String nickName=getCommonMemberName(member.getId(), langCode, "2");
						vo.setMemberName(nickName);
					}else {
						vo.setMemberName(member.getNickName());
					}
					
					//vo.setMemberName(member.getNickName());
					vo.setMemberType(member.getMemberType().toString());
					vo.setDateTime(DateUtil.getDateTimeGoneFormatStr(DateUtil.StringToDate(opTime, DateUtil.DEFAULT_DATE_TIME_FORMAT), langCode, ""));
					//vo.setReplyCount(findCommentReplyCount(vo.getId()));
					vo.setReplyId(replyId);
					vo.setIsLike(CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE.equals(like)?1:0);
					vo.setIsUnlike(CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE.equals(like)?1:0);
					vo.setStatus(null!=status && "".equals(status)?Integer.parseInt(status):1);
					String gender="";
					if(member.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA){
						MemberIfa ifa=memberBaseService.findIfaMember(vo.getMemberId());
						gender=ifa.getGender();
					}else if (member.getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL) {
						MemberIndividual individual=memberBaseService.findIndividualMember(vo.getMemberId());
						gender=individual.getGender();
					}
					vo.setMemberUrl(PageHelper.getUserHeadUrl(member.getIconUrl(), gender));
					list.add(vo);
				}
			}
			commentVO=appendComment(commentVO, list);
		}
		return commentVO;
	}
	
	/**
	 * 递归获取评论
	 * 
	 * @author mqzou 2017-03-15
	 * @param vo
	 * @param list
	 * @return
	 */
	private CommunityCommentVO appendComment(CommunityCommentVO vo, List<CommunityCommentVO> list) {
		if (null != list && !list.isEmpty()) {
			List<CommunityCommentVO> childList = new ArrayList<CommunityCommentVO>();//vo.getChildren();
			List<String> idsList=new ArrayList<String>();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				CommunityCommentVO commentVO = (CommunityCommentVO) iterator.next();
				if (commentVO.getReplyId().equals(vo.getId()) && !idsList.contains(commentVO.getId())) {
					commentVO = appendComment(commentVO, list);
					childList.add(commentVO);
					idsList.add(commentVO.getId());
				}
			}
			vo.setChildList(childList);
		}
		return vo;
	}
	
	/**
	 * 获取问题实体
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	@Override
	public QuestionDetailVO findqQuestionDetail(String id,String langCode,MemberBase member) {
		CommunityQuestion question=findCommunityQuestion(id);
		if(null!=question){
			QuestionDetailVO vo=new QuestionDetailVO();
			vo.setId(question.getId());
			vo.setClick(null!=question.getReadCount()?question.getReadCount():0);
			vo.setCommentCount(null!=question.getCommentCount()?question.getCommentCount():0);
			vo.setCreateDate(DateUtil.getDateTimeGoneFormatStr(question.getCreateTime(), langCode, ""));
			if(question.getCreator().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA){
		    	MemberIfa ifa=memberBaseService.findIfaMember(question.getCreator().getId());
		    	vo.setCreatorGender(ifa.getGender());
		    }else if (question.getCreator().getSubMemberType()==CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL) {
				MemberIndividual individual=memberBaseService.findIndividualMember(question.getCreator().getId());
				vo.setCreatorGender(individual.getGender());
			}
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==question.getCreator().getSubMemberType()){
				String nickName=getCommonMemberName(question.getCreator().getId(), langCode, "2");
				vo.setCreatorName(nickName);
			}else {
				vo.setCreatorName(question.getCreator().getNickName());
			}
			
			//vo.setCreatorName(question.getCreator().getNickName());
			vo.setCreatorUrl(question.getCreator().getIconUrl());
			vo.setIsAnswer(question.getIsAnswer());
			
			CommunityBehavior  behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
		    if(null!=behavior)
		    	vo.setIsLike("1");
		    behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
		    if(null!=behavior){
		    	vo.setIsUnlike("1");
		    }
		    vo.setLike(null!=question.getLikeCount()?question.getLikeCount():0);
		    vo.setSupportComment(question.getSupportComment());
		    vo.setTitle(StringEscapeUtils.unescapeHtml(question.getTitle()));
		    vo.setUnlike(null!=question.getUnlikeCount()?question.getUnlikeCount():0);
		    vo.setAnswerId(question.getAnswerer().getId());
		    return vo;
		}
		return null;
	}
	
	/**
	 * 获取问题的回答列表
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param langCode
	 * @param member
	 * @return
	 */
	@Override
	public List<CommunityCommentVO> findAnswerList(String id, String langCode, MemberBase member) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityComment r where r.targetId=? and r.status=1 and r.parent is null and r.commentType='answer'");
		List<Object> params=new ArrayList<Object>();
		params.add(id);
		List resultList=baseDao.find(hql.toString(), params.toArray(), false);
		List<CommunityCommentVO> list=new ArrayList<CommunityCommentVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CommunityComment comment = (CommunityComment) it.next();
				CommunityCommentVO vo=new CommunityCommentVO();
				vo.setId(comment.getId());
				vo.setContent(comment.getContent());
				vo.setDateTime(DateUtil.getDateTimeGoneFormatStr(comment.getCreateTime(), langCode, ""));
				vo.setCommentCount(null!=comment.getCommentCount()?comment.getCommentCount():0);
				vo.setLikeCount(null!=comment.getLikeCount()?comment.getLikeCount():0);
				vo.setUnlikeCount(null!=comment.getUnlikeCount()?comment.getUnlikeCount():0);
				vo.setMemberId(comment.getCreator().getId());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==comment.getCreator().getSubMemberType()){
					String nickName=getCommonMemberName(comment.getCreator().getId(), langCode, "2");
					vo.setMemberName(nickName);
				}else {
					vo.setMemberName(comment.getCreator().getNickName());
				}
				
				//vo.setMemberName(comment.getCreator().getNickName());
				vo.setMemberUrl(comment.getCreator().getIconUrl());
				CommunityBehavior behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
				if(null!=behavior){
					vo.setIsLike(1);
				}
				 behavior=findBehavior(vo.getId(), member.getId(), CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
				 if(null!=behavior){
						vo.setIsUnlike(1);
				}
				 vo=findCommentReply(vo, langCode,member);
				 list.add(vo);
				
			}
		}
		return list;
	}
	
	/**
	 * 搜索页面-获取问题列表
	 * @author wwlin
	 * @date 2016-07-21
	 * */
	public JsonPaging getSearchQuestionListJson(JsonPaging jsonPaging,String keywords,String lang) {
		List params = new ArrayList();
		String hql = " SELECT r.id,r.title,r.createTime,r.creator.id,c.content,r.commentCount,r.readCount,r.likeCount,r.unlikeCount FROM CommunityQuestion r,CommunityContent c WHERE r.id=c.id and r.status='1' ";
		if(StringUtils.isNotBlank(keywords)){
			hql += " and r.title like ?";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY r.createTime DESC ";

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontCommunityTopicVO> voList = new ArrayList<FrontCommunityTopicVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontCommunityTopicVO vo = new FrontCommunityTopicVO();//每个前端实体
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = objs[0]==null?"":objs[0].toString();
			String title = objs[1]==null?"":objs[1].toString();
			String createTime = objs[2]==null?"":objs[2].toString();
			String creatorId = objs[3]==null?"":objs[3].toString();
			String content = objs[4]==null?"":objs[4].toString();
			String commentCount = objs[5]==null?"0":objs[5].toString();
			String readCount = objs[6]==null?"0":objs[6].toString();
			String likeCount = objs[7]==null?"0":objs[7].toString();
			String unlikeCount = objs[8]==null?"0":objs[8].toString();

			vo.setId(id);
			vo.setTitle(StringEscapeUtils.unescapeHtml(title));
			vo.setContent(content);
			vo.setCommentCount(commentCount);
			vo.setReadCount(readCount);
			vo.setLikeCount(likeCount);
			vo.setUnlikeCount(unlikeCount);
			//格式化时间格式
			if(!"".equals(createTime)){
				Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
			}
			//发布人的头像与名称
			if(!"".equals(creatorId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(creatorId);
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());
			}
			vo.setSectionName("");
			
			voList.add(vo);
		}
		jsonPaging.getList().clear();
		jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		//modify end
		
		return jsonPaging;
	}
	
	/**
	 * 搜索页面-获取观点列表
	 * @author wwlin
	 * @date 2016-07-21
	 * */
	public JsonPaging getSearchInsightListJson(JsonPaging jsonPaging,String keywords,String lang) {
		List params = new ArrayList();
		String hql = " SELECT r.id,r.title,r.createTime,r.creator.id,c.content,r.commentCount,r.readCount,r.likeCount,r.unlikeCount FROM CommunityQuestion r,CommunityContent c WHERE r.id=c.id and r.isInsight='1' and r.status='1' ";
		if(StringUtils.isNotBlank(keywords)){
			hql += " and r.title like ?";
			params.add("%"+keywords+"%");
		}
		hql += " ORDER BY r.createTime DESC ";

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//显示的前端实体列表
		List<FrontCommunityTopicVO> voList = new ArrayList<FrontCommunityTopicVO>();
		if(jsonPaging.getList().isEmpty())return jsonPaging;
		for(int z=0;z<jsonPaging.getList().size();z++){
			FrontCommunityTopicVO vo = new FrontCommunityTopicVO();//每个前端实体
			Object[] objs = (Object[])jsonPaging.getList().get(z);
			String id = objs[0]==null?"":objs[0].toString();
			String title = objs[1]==null?"":objs[1].toString();
			String createTime = objs[2]==null?"":objs[2].toString();
			String creatorId = objs[3]==null?"":objs[3].toString();
			String content = objs[4]==null?"":objs[4].toString();
			String commentCount = objs[5]==null?"0":objs[5].toString();
			String readCount = objs[6]==null?"0":objs[6].toString();
			String likeCount = objs[7]==null?"0":objs[7].toString();
			String unlikeCount = objs[8]==null?"0":objs[8].toString();

			vo.setId(id);
			vo.setTitle(StringEscapeUtils.unescapeHtml(title));
			vo.setContent(content);
			vo.setCommentCount(commentCount);
			vo.setReadCount(readCount);
			vo.setLikeCount(likeCount);
			vo.setUnlikeCount(unlikeCount);
			//格式化时间格式
			if(!"".equals(createTime)){
				Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
				vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
			}
			//发布人的头像与名称
			if(!"".equals(creatorId)){
				MemberBase user = (MemberBase)this.baseDao.get(MemberBase.class, creatorId);
				String userHeadUrl = PageHelper.getUserHeadUrl(user.getIconUrl(), "");
				vo.setMemberId(creatorId);
				vo.setUserHeadUrl(userHeadUrl);
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==user.getSubMemberType()){
					String nickName=getCommonMemberName(user.getId(), lang, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(user.getNickName());
				}
				//vo.setNickName(user.getNickName());
				vo.setMemberType(user.getMemberType().toString());
			}
			vo.setSectionName("");
			
			voList.add(vo);
		}
		jsonPaging.getList().clear();
		jsonPaging.setList(voList);
		//jsonPaging.setTotal(voList.size());
		//modify end
		
		return jsonPaging;
	}
	
	/**
	 * 搜索页面-获取user列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getSearchUserListJson(JsonPaging jsonPaging,String memberId,String keywords,String lang) {
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
	 * 保存搜索关键字
	 * @author mqzou 2017-03-14
	 * @param topic
	 * @param content
	 * @return
	 */
	@Override
	public SysSearchHistory saveOrUpdateKeyword(SysSearchHistory keyword) {
		keyword=(SysSearchHistory)baseDao.saveOrUpdate(keyword);
		return keyword;
	}
	
	/**
	 * 判断是否已存在同样的关键字
	 * @author wwlin 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	public int findSameKeywords(String keyword) {
		StringBuilder hql=new StringBuilder();
		hql.append(" Select count(*) as count from SysSearchHistory r where r.searchContent like ? ");
		List<Object> params=new ArrayList<Object>();
		params.add("%"+keyword+"%");
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object obj=list.get(0);
			if(null!=obj && !"".equals(obj.toString())){
				return Integer.parseInt(obj.toString());
			}
		}
		return 0;
		
		
	}
	
	/**
	 * 获取搜索页面的关键字列表前10位
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<SysSearchHistory> getSysSearchHistoryList() {
		List params = new ArrayList();
		String hql = " FROM SysSearchHistory a "
					+" WHERE a.searchType='community'  "
					+" ORDER BY a.createTime DESC";
		JsonPaging page = new JsonPaging();
		page.setPage(1);
		page.setRows(10);
		page.setTotal(10);
		page = this.baseDao.selectJsonPaging(hql, params.toArray(), page, false);
		//page = springJdbcQueryManager.springJdbcQueryForPaging(hql, null, page);
		//显示的前端实体列表
		List<SysSearchHistory> voList = new ArrayList<SysSearchHistory>();
		voList = page.getList();
		

		return voList;
	}
	
	/**
	 * 查看查某个IFA的观点列表
	 * @author wwlin 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findIfaInsight(JsonPaging jsonPaging, String loginMemberId, String ifaMemberId,String lang,String keyWords) {
		/*StringBuilder hql=new StringBuilder();
		hql.append(" from NewsInfo r where r.id in (select t.sourceId from CommunityTopic t where t.creator.id=? and t.status=1 ");
		hql.append(" and case  when t.visitor= 'friend'  then ? in (SELECT f.toMember.id FROM WebFriend f WHERE f.fromMember.id=?)");
		hql.append(" when t.visitor='client' then ? in (SELECT c.member.id FROM CrmCustomer c left join MemberIfa i on c.ifa.id=i.id WHERE i.member.id=?) else 1=1 end");
		hql.append(" )");*/
		List<Object> params=new ArrayList<Object>();
		StringBuilder sql=new StringBuilder();
		if(loginMemberId.equals(ifaMemberId)){//如果是同一个人
			sql.append(" SELECT t.*,e.content FROM `community_topic` t,`community_content` e  WHERE e.id=t.id and t.`is_insight`=1 AND t.`creator_id`=? and t.status=1 and t.title like '%"+keyWords+"%'   ");
			params.add(ifaMemberId);
		} else{
			sql.append(" SELECT t.*,e.content FROM `community_topic` t,`community_content` e  WHERE e.id=t.id and t.`is_insight`=1 AND t.`creator_id`=? and t.status=1 and t.title like '"+keyWords+"' ");
			sql.append("AND CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=?)");
			sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=?) ELSE 1=1 END");
			params.add(ifaMemberId);
			params.add(loginMemberId);
			params.add(ifaMemberId);
			params.add(loginMemberId);
			params.add(ifaMemberId);
		}

		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String sectionId=getMapValue(map, "section_id");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				String likeCount=getMapValue(map, "like_count");
				String unlikeCount=getMapValue(map, "unlike_count");
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				if(!StringUtils.isNotBlank(likeCount)){likeCount="0";}
				if(!StringUtils.isNotBlank(unlikeCount)){unlikeCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  getCommunitySectionInfo(sectionId); 
				if(null!=section) vo.setSectionName(section.getSectionNameEn());
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(readCount);
				vo.setLikeCount(likeCount);
				vo.setUnlikeCount(unlikeCount);
				//查看的人是否点赞或踩
				CommunityBehavior behavior=findBehavior(id, loginMemberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				if(null!=behavior){
					vo.setIsLike(1);
				}
				 behavior=findBehavior(id, loginMemberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				 if(null!=behavior){
					vo.setIsUnlike(1);
				}
				 
				
				String newContent = delHTMLTag(content);
				if(newContent.length()>150)newContent = newContent.substring(0, 150);
				vo.setContent(newContent);
				
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	public String delHTMLTag(String htmlStr){ 
        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regExHtml="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern pStyle=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE); 
        Matcher mStyle=pStyle.matcher(htmlStr); 
        htmlStr=mStyle.replaceAll(""); //过滤style标签 
         
        Pattern pHtml=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE); 
        Matcher mHtml=pHtml.matcher(htmlStr); 
        htmlStr=mHtml.replaceAll(""); //过滤html标签 
        
        htmlStr=htmlStr.replace(" ","");
        htmlStr=htmlStr.replaceAll("\\s*|\t|\r|\n","");
        htmlStr=htmlStr.replace("“","");
        htmlStr=htmlStr.replace("”","");
        htmlStr=htmlStr.replaceAll("　","");
          
        return htmlStr.trim(); //返回文本字符串 
    }

}
