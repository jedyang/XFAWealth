package com.fsll.app.investor.me.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.me.service.AppInsightInfoService;
import com.fsll.app.investor.me.vo.AppCommunityTopicItemVO;
import com.fsll.app.investor.me.vo.AppInsightInfoVo;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CommunityContent;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebRecommended;

/**
 * 观点接口
 * @author zpzhou
 * @date 2016-8-11
 */
@Service("appInsightInfoService")
//@Transactional
public class AppInsightInfoServiceImpl extends BaseService implements AppInsightInfoService {
	
//	/**
//	 * 得到观点列表信息
//	 * @param jsonPaging 分页参数
//	 * @param memberId 用户ID
//	 * @return
//	 */
//	public JsonPaging findInsightList(JsonPaging jsonPaging,String memberId,String type){
//		String hql = " from InsightInfo i ";
//		hql += " left join InsightCount c on c.id=i.id ";
//		hql += " left join WebInvestorVisit v on v.relateId=i.id and v.moduleType='insight' and v.member.id=? ";
//		if("1".equals(type)){//我的关注时不需要再关联关注表
//			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=? ";
//			hql += " where i.id in (select k.relateId from WebFollow k where k.moduleType='insight' and k.isValid='1'  and k.member.id=?) ";
//		}else if("2".equals(type)){//我的推荐时不需要再关联关注表
//			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.member.id=? ";
//			hql += " where i.id in (select y.relateId from WebRecommended y where y.moduleType='insight' and y.creator.id=?) ";
//		}else{
//			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.member.id=? ";
//			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=? ";
//		}
//		hql += " order by i.overheadTime,i.pubDate asc ";
//		List params = new ArrayList();
//		params.add(memberId);
//		params.add(memberId);
//		params.add(memberId);
//		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
//		List list=jsonPaging.getList();
//		List<AppInsightItemVo> iList=new ArrayList<AppInsightItemVo>();
//		for(int i=0;i<list.size(); i++){
//			Object[] objs = (Object[])list.get(i);
//			InsightInfo info=(InsightInfo)objs[0];
//			InsightCount count=(InsightCount)objs[1];
//			WebInvestorVisit visit=(WebInvestorVisit)objs[2];
//			WebFollow follow=null;
//			WebRecommended recommended=null;
//			AppInsightItemVo vo=new AppInsightItemVo();
//			vo.setId(info.getId());
//			vo.setRegionType(info.getGeoAllocation());
//			vo.setSectionType(info.getSector());
//			vo.setTitle(info.getTitle());
//			vo.setUpCounter(info.getUpCounter());
//			vo.setDownCounter(info.getDownCounter());
//			if(null!=info.getCreator())vo.setCreatorName(info.getCreator().getNickName());
//			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
//			if(null!=count){
//				vo.setViews(count.getViews());
//				vo.setComments(count.getComments());
//				vo.setUps(count.getUps());
//				vo.setDowns(count.getDowns());
//			}
//			if(null!=visit){
//				vo.setInvestorFlag("1");
//			}else{
//				vo.setInvestorFlag("0");
//			}
//			if("1".equals(type)){//我的关注
//				vo.setFollowFlag("1");
//				recommended=(WebRecommended)objs[3];
//				if(null!=recommended){
//					vo.setRecommendedFlag("1");
//				}else{
//					vo.setRecommendedFlag("0");
//				}
//			}else if("2".equals(type)){//我的推荐
//				vo.setRecommendedFlag("1");
//				follow=(WebFollow)objs[3];
//				if(null!=follow){
//					vo.setFollowFlag("1");
//				}else{
//					vo.setFollowFlag("0");
//				}
//			}else{
//				follow=(WebFollow)objs[3];
//				recommended=(WebRecommended)objs[4];
//				if(null!=follow){
//					vo.setFollowFlag("1");
//				}else{
//					vo.setFollowFlag("0");
//				}
//				if(null!=recommended){
//					vo.setRecommendedFlag("1");
//				}else{
//					vo.setRecommendedFlag("0");
//				}
//			}
//			iList.add(vo);
//    	}
//		jsonPaging.setList(iList);
//		return jsonPaging;
//	}
//	/**
//	 * 得到观点详细信息
//	 * @param insightId
//	 * @param memberId
//	 * @return
//	 */
//	public AppInsightInfoVo findInsightInfo(String insightId,String memberId){
//		AppInsightInfoVo vo=new AppInsightInfoVo();
//		String hql = " from InsightInfo i ";
//		hql += " left join InsightCount c on c.id=i.id ";
//		hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='insight' and w.isValid='1' and w.member.id=?";
//		hql += " left join WebInvestorVisit v on v.relateId=i.id and v.moduleType='insight' and v.member.id=?";
//		hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='insight' and r.creator.id=?";
//		hql += " where i.id=? ";
//		List params = new ArrayList();
//		params.add(memberId);
//		params.add(memberId);
//		params.add(memberId);
//		params.add(insightId);
//		List list = baseDao.find(hql, params.toArray(), false);
//		if(null!=list && !list.isEmpty()){
//			Object[] objs = (Object[])list.get(0);
//			InsightInfo info=(InsightInfo)objs[0];
//			InsightCount count=(InsightCount)objs[1];
//			WebFollow follow=(WebFollow)objs[2];
//			WebInvestorVisit visit=(WebInvestorVisit)objs[3];
//			WebRecommended recommended=(WebRecommended)objs[4];
//			vo.setId(info.getId());
//			vo.setRegionType(info.getGeoAllocation());
//			vo.setSectionType(info.getSector());
//			vo.setTitle(info.getTitle());
//			vo.setContent(info.getContent());
//			vo.setUpCounter(info.getUpCounter());
//			vo.setDownCounter(info.getDownCounter());
//			if(null!=info.getCreator())vo.setCreatorName(info.getCreator().getNickName());
//			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
//			if(null!=count){
//				vo.setViews(count.getViews());
//				vo.setComments(count.getComments());
//				vo.setUps(count.getUps());
//				vo.setDowns(count.getDowns());
//				vo.setCommentsDay(count.getCommentsDay());
//				vo.setCommentsMonth(count.getCommentsMonth());
//				vo.setCommentsWeek(count.getCommentsWeek());
//				vo.setViewsDay(count.getViewsDay());
//				vo.setViewsMonth(count.getViewsMonth());
//				vo.setViewsWeek(count.getViewsWeek());
//				vo.setUpsDay(count.getUpsDay());
//				vo.setUpsMonth(count.getUpsMonth());
//				vo.setUpsWeek(count.getUpsWeek());
//			}
//			if(null!=follow){
//				vo.setFollowFlag("1");
//			}else{
//				vo.setFollowFlag("0");
//			}
//			if(null!=visit){
//				vo.setInvestorFlag("1");
//			}else{
//				vo.setInvestorFlag("0");
//			}
//			if(null!=recommended){
//				vo.setRecommendedFlag("1");
//			}else{
//				vo.setRecommendedFlag("0");
//			}
//    	}
//		return vo;
//	}
	
	
	/**
	 * 【我的投资顾问】得到观点列表信息
	 * 使用用新的帖子数据表
	 * @author zxtan
	 * @date 2017-03-20
	 * @param memberId 用户ID
	 * @param ifaMemberId IfaMemberId
	 * @param langCode 语言
	 * @return
	 */
	public List<AppCommunityTopicItemVO> findMyIfaInsightList(String memberId,String ifaMemberId,String langCode){
		List<AppCommunityTopicItemVO> voList = new ArrayList<AppCommunityTopicItemVO>();
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunitySection s on t.section.id=s.id ");
		hql.append(" where t.status='1' and t.isInsight='1' and t.creator.id=? and ( ");
		hql.append(" 	t.visitor = 'all' ");
		hql.append(" 	or ( t.visitor = 'friend' and exists( select f.id from WebFriend f where f.fromMember.id=? and f.toMember.id=? )) ");
		hql.append(" 	or ( t.visitor = 'client' and exists( select c.id from CrmCustomer c inner join MemberIfa i on i.id=c.ifa.id where i.member.id=? and c.member.id=? )) ");
		hql.append(" ) ");
		hql.append(" order by t.isRecommand desc ,t.createTime desc ");
		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add(ifaMemberId);
		params.add(memberId);
		params.add(ifaMemberId);
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			CommunityTopic info = (CommunityTopic)objs[0];
			CommunitySection section = (CommunitySection)objs[1];
			
			AppCommunityTopicItemVO vo = new AppCommunityTopicItemVO();
			vo.setTopicId(info.getId());
			vo.setTopicTitle(info.getTitle());
			String sectionName = section.getSectionNameSc();
			if("en".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameEn();
			}else if("tc".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameTc();
			}
			vo.setSection(sectionName);
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
			vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
			vo.setCommentCount(String.valueOf(info.getCommentCount()));
			vo.setReadCount(String.valueOf(info.getReadCount()));
			vo.setLikeCount(String.valueOf(info.getLikeCount()));
			vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
			vo.setTransferCount(String.valueOf(info.getTansferCount()));
			vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
			vo.setSuportComment(String.valueOf(info.getSupportComment()));
			voList.add(vo);
    	}
		
		return voList;
	}
	
	/**
	 * 【我的投资顾问】得到观点信息
	 * 使用用新的帖子数据表
	 * @author zxtan
	 * @date 2017-03-20
	 * @param topicId 用户ID
	 * @param langCode 语言
	 * @return
	 */
	public AppCommunityTopicItemVO findInsightInfo(String topicId,String langCode){
		StringBuilder hql = new StringBuilder();		
		hql.append(" from CommunityTopic t ");
		hql.append(" inner join CommunityContent c on c.id=t.id ");
		hql.append(" inner join CommunitySection s on s.id=t.section.id ");
		hql.append(" where t.status='1' and t.isInsight='1' and t.id=? ");

		List params = new ArrayList();
		params.add(topicId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);

		AppCommunityTopicItemVO vo = new AppCommunityTopicItemVO();
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			CommunityTopic info = (CommunityTopic)objs[0];
			CommunityContent content = (CommunityContent)objs[1];
			CommunitySection section = (CommunitySection)objs[2];
			
			vo.setTopicId(info.getId());
			vo.setTopicTitle(info.getTitle());
			String sectionName = section.getSectionNameSc();
			if("en".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameEn();
			}else if("tc".equalsIgnoreCase(langCode)){
				sectionName = section.getSectionNameTc();
			}
			vo.setSection(sectionName);
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setIsRecommand(String.valueOf(info.getIsRecommand()));
			vo.setRecommandReason(String.valueOf(info.getRecommandReason()));
			vo.setCommentCount(String.valueOf(info.getCommentCount()));
			vo.setReadCount(String.valueOf(info.getReadCount()));
			vo.setLikeCount(String.valueOf(info.getLikeCount()));
			vo.setUnlikeCount(String.valueOf(info.getUnlikeCount()));
			vo.setTransferCount(String.valueOf(info.getTansferCount()));
			vo.setFavoriteCount(String.valueOf(info.getFavoriteCount()));
			vo.setSuportComment(String.valueOf(info.getSupportComment()));
			vo.setContent(content.getContent());
    	}
		
		return vo;
	}
	
	
}
