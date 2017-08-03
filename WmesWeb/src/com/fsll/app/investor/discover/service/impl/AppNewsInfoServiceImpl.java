package com.fsll.app.investor.discover.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.app.investor.discover.vo.AppNewsCategoryVO;
import com.fsll.app.investor.discover.vo.AppNewsCommentVO;
import com.fsll.app.investor.discover.vo.AppNewsInfoItemVO;
import com.fsll.app.investor.discover.vo.AppNewsItemVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsCategory;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.NewsInfoCategory;
import com.fsll.wmes.entity.NewsInfoDoc;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebRecommended;

/**
 * 新闻接口
 * @author zpzhou
 * @date 2016-8-11
 */
@Service("appNewsInfoService")
//@Transactional
public class AppNewsInfoServiceImpl extends BaseService implements AppNewsInfoService {
	
//	/**
//	 * 得到新闻列表信息
//	 * @param jsonPaging 分页参数
//	 * @param memberId 用户ID
//	 * @return
//	 */
//	public JsonPaging findNewsList(JsonPaging jsonPaging,String memberId,String type){
//		String hql = " from NewsInfo i ";
//		hql += " left join NewsCount c on c.id=i.id ";
//		hql += " left join WebInvestorVisit v on v.relateId=i.id and v.moduleType='news' and v.member.id=? ";
//		hql += " left join NewsInfoCategory n on n.newsInfo.id=i.id ";//关联新闻栏目
//		if("1".equals(type)){//我的关注时不需要再关联关注表
//			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='news' and r.creator.id=? ";
//			hql += " where i.id in (select k.relateId from WebFollow k where k.moduleType='news' and k.isValid='1' and k.member.id=?) ";
//		}else if("2".equals(type)){//我的推荐时不需要再关联推荐表
//			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='news' and w.isValid='1'  and w.member.id=? ";
//			hql += " where i.id in (select y.relateId from WebRecommended y where y.moduleType='news' and y.creator.id=?) ";
//		}else{
//			hql += " left join WebFollow w on w.relateId=i.id and w.moduleType='news' and w.isValid='1'  and w.member.id=? ";
//			hql += " left join WebRecommended r on r.relateId=i.id and r.moduleType='news' and r.creator.id=? ";
//		}
//		hql += " order by i.orderBy asc ";
//		List params = new ArrayList();
//		params.add(memberId);
//		params.add(memberId);
//		params.add(memberId);
//		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
//		List list=jsonPaging.getList();
//		List<AppNewsItemVo> iList=new ArrayList<AppNewsItemVo>();
//		for(int i=0;i<list.size(); i++){
//			Object[] objs = (Object[])list.get(i);
//			NewsInfo info=(NewsInfo)objs[0];
//			//NewsCount count=(NewsCount)objs[1];
//			WebInvestorVisit visit=(WebInvestorVisit)objs[2];
//			NewsInfoCategory category=(NewsInfoCategory)objs[3];
//			WebFollow follow=null;
//			WebRecommended recommended=null;
//			AppNewsItemVo vo=new AppNewsItemVo();
//			vo.setId(info.getId());
//			vo.setRegionType(info.getRegionType());
//			vo.setSectionType(info.getSectionType());
//			//vo.setXfaNewsId(info.getXfaNewsId());
//			vo.setTitle(info.getTitle());
//			//vo.setUrl(info.getUrl());
//			//vo.setUpCounter(info.getUpCounter());
//			//vo.setDownCounter(info.getDownCounter());
//			if(null!=category && null!=category.getCategory()){
//				vo.setCategoryName(category.getCategory().getNameSc());//得到栏目名称
//			}
//			//if(null!=info.getCreator())vo.setCreatorName(info.getCreator().getNickName());
//			//vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//			//if(null!=count){
//			//	vo.setViews(count.getViews());
//			//	vo.setComments(count.getComments());
//			//	vo.setUps(count.getUps());
//			//	vo.setDowns(count.getDowns());
//			//}
//			if(null!=visit){
//				vo.setInvestorFlag("1");
//			}else{
//				vo.setInvestorFlag("0");
//			}
//			if("1".equals(type)){//我的关注
//				vo.setFollowFlag("1");
//				recommended=(WebRecommended)objs[4];
//				if(null!=recommended){
//					vo.setRecommendedFlag("1");
//				}else{
//					vo.setRecommendedFlag("0");
//				}
//			}else if("2".equals(type)){//我的推荐
//				vo.setRecommendedFlag("1");
//				follow=(WebFollow)objs[4];
//				if(null!=follow){
//					vo.setFollowFlag("1");
//				}else{
//					vo.setFollowFlag("0");
//				}
//			}else{
//				follow=(WebFollow)objs[4];
//				recommended=(WebRecommended)objs[5];
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


	public NewsInfo findNewsInfoById(String id) {
		NewsInfo vo=(NewsInfo)this.baseDao.get(NewsInfo.class, id);
		return vo;
	}
	
	/**
	 * 获取新闻栏目列表
	 * @author zxtan
	 * @date 2017-03-09
	 * @param langCode
	 * @return
	 */
	public List<AppNewsCategoryVO> findNewsCategoryList(String langCode){
		List<AppNewsCategoryVO> voList = new ArrayList<AppNewsCategoryVO>();
		String hql = "from NewsCategory m where m.parent is null order by m.orderBy ";
		List<NewsCategory> list = baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				NewsCategory category = list.get(i);
				AppNewsCategoryVO vo = new AppNewsCategoryVO();
				vo.setId(category.getId());
				vo.setCode(category.getCode());
				if("en".equalsIgnoreCase(langCode)){
					vo.setName(category.getNameEn());
				}else if("sc".equalsIgnoreCase(langCode)){
					vo.setName(category.getNameSc());
				}else if("tc".equalsIgnoreCase(langCode)){
					vo.setName(category.getNameTc());
				}
				vo.setIconUrl(category.getIconUrl());
				vo.setOrderBy(String.valueOf(category.getOrderBy()));
				
				List<AppNewsCategoryVO> subCategoryList = new ArrayList<AppNewsCategoryVO>();
				String hqlSub = "from NewsCategory m where m.parent.id=? order by m.orderBy ";
				List<Object> params = new ArrayList<Object>();
				params.add(category.getId());
				List<NewsCategory> subList = baseDao.find(hqlSub, params.toArray(), false);
				if(null != subList && !subList.isEmpty()){
					
					for (int j = 0; j < subList.size(); j++) {
						NewsCategory subCategory = subList.get(j);
						AppNewsCategoryVO subVO = new AppNewsCategoryVO();
						subVO.setId(subCategory.getId());
						subVO.setCode(subCategory.getCode());
						if("en".equalsIgnoreCase(langCode)){
							subVO.setName(subCategory.getNameEn());
						}else if("sc".equalsIgnoreCase(langCode)){
							subVO.setName(subCategory.getNameSc());
						}else if("tc".equalsIgnoreCase(langCode)){
							subVO.setName(subCategory.getNameTc());
						}
						subVO.setIconUrl(subCategory.getIconUrl());
						subVO.setOrderBy(String.valueOf(subCategory.getOrderBy()));
						
						subCategoryList.add(subVO);
					}
				}
				vo.setSubCategoryList(subCategoryList);
				voList.add(vo);
			}
		}
		return voList; 		
	}
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-09
	 * @param jsonPaging 分页参数
	 * @param categoryId 栏目ID
	 * @param langCode 语言
	 * @param keyword 搜索关键字
	 * @return
	 */
	public JsonPaging findNewsListByCategory(JsonPaging jsonPaging,String categoryId,String langCode,String keyword){
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo m ");
		hql.append(" inner join NewsInfoCategory r on r.newsInfo.id=m.id ");
		hql.append(" inner join NewsCategory c on c.id=r.category.id ");
		hql.append(" where m.langCode=? and ( c.id=? or c.parent.id=? ) ");
		List params = new ArrayList();
		params.add(langCode);
		params.add(categoryId);
		params.add(categoryId);
		if(!"".equals(keyword)){
			hql.append(" and ( m.title like ? or m.description like ? or m.keywords like ? ) ");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		hql.append(" order by m.pubDate desc ");
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list=jsonPaging.getList();
		List<AppNewsInfoItemVO> iList=new ArrayList<AppNewsInfoItemVO>();
		for(int i=0;i<list.size(); i++){
			AppNewsInfoItemVO vo=new AppNewsInfoItemVO();
			Object[] objs = (Object[])list.get(i);
			NewsInfo info=(NewsInfo)objs[0];
			
			vo.setId(info.getId());
			vo.setRegionType(info.getRegionType());
			vo.setSectionType(info.getSectionType());
			vo.setFlag(info.getFlag());
			vo.setTitle(info.getTitle());
			vo.setWriter(info.getWriter());
			vo.setSource(info.getSource());
			vo.setLitPic(info.getLitPic());
			vo.setPubDate(DateUtil.dateToDateString(info.getPubDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setSendDate(DateUtil.dateToDateString(info.getSendDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setKeywords(info.getKeywords());
			vo.setDescription(info.getDescription());
			vo.setClick(String.valueOf(info.getClick()));
			vo.setGoodPost(String.valueOf(info.getGoodPost()));
			vo.setBadPost(String.valueOf(info.getBadPost()));			

			iList.add(vo);
    	}
		jsonPaging.setList(iList);
		return jsonPaging;
	}
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param id 新闻Id
	 * @return
	 */
	public AppNewsInfoItemVO findNewsInfo(String memberId,String id){
		AppNewsInfoItemVO vo=new AppNewsInfoItemVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo m ");
		hql.append(" inner join NewsInfoDoc r on r.id=m.id ");
		hql.append(" left join NewsBehavior b on b.targetId=m.id and b.target='news' and FIND_IN_SET(b.behaviorType,'like,unlike')>0 and b.creator.id=? ");
		hql.append(" where m.id=? ");
		List params = new ArrayList();
		params.add(memberId);
		params.add(id);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty())	{
			Object[] objs = (Object[])list.get(0);
			NewsInfo info=(NewsInfo)objs[0];
			NewsInfoDoc doc = (NewsInfoDoc) objs[1];
			
			vo.setId(info.getId());
			vo.setRegionType(info.getRegionType());
			vo.setSectionType(info.getSectionType());
			vo.setFlag(info.getFlag());
			vo.setTitle(info.getTitle());
			vo.setWriter(info.getWriter());
			vo.setSource(info.getSource());
			vo.setLitPic(info.getLitPic());
			vo.setPubDate(DateUtil.dateToDateString(info.getPubDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setSendDate(DateUtil.dateToDateString(info.getSendDate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setKeywords(info.getKeywords());
			vo.setDescription(info.getDescription());
			vo.setClick(String.valueOf(info.getClick()));
			vo.setGoodPost(String.valueOf(info.getGoodPost()));
			vo.setBadPost(String.valueOf(info.getBadPost()));			
			vo.setBody(doc.getBody());
			if(null != objs[2]){
				NewsBehavior behavior = (NewsBehavior) objs[2];
				vo.setBehaviorType(behavior.getBehaviorType());
			}
    	}
		
		return vo;
	}
	
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param jsonPaging 分页参数
	 * @param newsInfoId 新闻ID
	 * @return
	 */
	public JsonPaging findNewsCommentList(JsonPaging jsonPaging,String memberId,String newsInfoId){
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsComment c ");
		hql.append(" left join MemberBase m on m.id=c.member.id ");
		hql.append(" left join NewsComment r on r.id=c.replyComment.id ");
		hql.append(" left join MemberBase f on f.id=r.member.id ");
		hql.append(" left join NewsBehavior b on b.targetId=c.id and b.target='comment' and FIND_IN_SET(b.behaviorType,'like,unlike')>0 and b.creator.id=? ");
		hql.append(" where c.checked='1' and c.newsInfo.id=? ");
		hql.append(" order by c.opTime desc ");
		List params = new ArrayList();
		params.add(memberId);
		params.add(newsInfoId);
				
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list=jsonPaging.getList();
		List<AppNewsCommentVO> iList=new ArrayList<AppNewsCommentVO>();
		for(int i=0;i<list.size(); i++){
			AppNewsCommentVO vo=new AppNewsCommentVO();
			Object[] objs = (Object[])list.get(i);
			NewsComment info = (NewsComment)objs[0];
			MemberBase member = null;
			if(null != objs[1]){
				member = (MemberBase) objs[1]; 
				vo.setMemberId(member.getId());
				vo.setNickname(member.getNickName()==null?member.getLangCode():member.getNickName());
				String iconUrl = PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null);
				vo.setMemberIconUrl(iconUrl);
			}
			
			MemberBase replyMember = null;
			if(null != objs[3]){
				replyMember = (MemberBase) objs[3]; 
				vo.setReplyMemberId(replyMember.getId());
				vo.setReplyNickname(replyMember.getNickName()==null?replyMember.getLangCode():replyMember.getNickName());
				String iconUrl = PageHelper.getUserHeadUrlForWS(replyMember.getIconUrl(), null);
				vo.setReplyMemberIconUrl(iconUrl);
			}
			
			String id = info.getId();			
			vo.setId(id);
			vo.setNewsInfoId(info.getNewsInfo().getId());
			vo.setComment(info.getComment());
			vo.setGood(String.valueOf(info.getGood()));
			vo.setBad(String.valueOf(info.getBad()));
			vo.setOpTime(DateUtil.dateToDateString(info.getOpTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setIP(info.getIp());
			vo.setFType(String.valueOf(info.getFType()));
			vo.setFace(String.valueOf(info.getFace()));
			vo.setChecked(info.getChecked());
			if(null != objs[4]){
				NewsBehavior behavior = (NewsBehavior) objs[4];
				vo.setBehaviorType(behavior.getBehaviorType());
			}
//			List<AppNewsCommentVO> replyList = findReplyList(id);
//			vo.setReplyList(replyList);

			iList.add(vo);
    	}
		jsonPaging.setList(iList);
		return jsonPaging;
	}
	
	/**
	 * 获取某条评论的回复
	 * @author zxtan
	 * @date 2017-03-10
	 * @param commentId
	 * @return
	 */
	private List<AppNewsCommentVO> findReplyList(String commentId){
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsComment c ");
		hql.append(" left join MemberBase m on m.id=c.member.id ");
		hql.append(" where c.replyComment.id=? ");
		hql.append(" order by c.opTime desc ");
		List params = new ArrayList();
		params.add(commentId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppNewsCommentVO> voList = new ArrayList<AppNewsCommentVO>();
		if(null != list && !list.isEmpty()){
			
			for(int i=0;i<list.size(); i++){
				AppNewsCommentVO vo=new AppNewsCommentVO();
				Object[] objs = (Object[])list.get(i);
				NewsComment info = (NewsComment)objs[0];
				MemberBase member = null;
				if(null != objs[1]){
					member = (MemberBase) objs[1]; 
					vo.setMemberId(member.getId());
					vo.setNickname(member.getNickName()==null?member.getLangCode():member.getNickName());
					String iconUrl = PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null);
					vo.setMemberIconUrl(iconUrl);
				}
				
				String id = info.getId();			
				vo.setId(id);
				vo.setNewsInfoId(info.getNewsInfo().getId());
				vo.setComment(info.getComment());
				vo.setGood(String.valueOf(info.getGood()));
				vo.setBad(String.valueOf(info.getBad()));
				vo.setOpTime(DateUtil.dateToDateString(info.getOpTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setIP(info.getIp());
				vo.setFType(String.valueOf(info.getFType()));
				vo.setFace(String.valueOf(info.getFace()));
				vo.setChecked(info.getChecked());
				
//				List<AppNewsCommentVO> replyList = findReplyList(id);
//				vo.setReplyList(replyList);
					
				voList.add(vo);
	    	}
		}
		
		return voList;
	}
	
	/**
	 * 新闻点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId 新闻id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean addNewsBehavior(String memberId,String targetId,String behaviorType){
		
		NewsInfo newsInfo = (NewsInfo) baseDao.get(NewsInfo.class, targetId);
		if(null == newsInfo) return false;
		NewsBehavior info = null;
		String hql = "from NewsBehavior t where t.target='news' and FIND_IN_SET(t.behaviorType,'like,unlike')>0 and t.creator.id=? and t.targetId=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(targetId);
		
		List<NewsBehavior> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			info = list.get(0);
			if(info.getBehaviorType().equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				baseDao.delete(info);
				if("like".equalsIgnoreCase(behaviorType)){
					int goodPost = newsInfo.getGoodPost()==null?0:newsInfo.getGoodPost()-1;
					newsInfo.setGoodPost(goodPost);
				}else {
					int badPost = newsInfo.getBadPost()==null?0:newsInfo.getBadPost()-1;
					newsInfo.setBadPost(badPost);
				}
			}else {
				info.setBehaviorType(behaviorType);
				info.setCreateTime(new Date());
				baseDao.saveOrUpdate(info);
				
				int goodPost = newsInfo.getGoodPost()==null?0:newsInfo.getGoodPost();
				int badPost = newsInfo.getBadPost()==null?0:newsInfo.getBadPost();
				
				if("like".equalsIgnoreCase(behaviorType)){
					newsInfo.setGoodPost(goodPost+1);
					newsInfo.setBadPost(badPost-1);
				}else {
					newsInfo.setGoodPost(goodPost-1);
					newsInfo.setBadPost(badPost+1);
				}
//				baseDao.saveOrUpdate(newsInfo);
			}
		}else {
			info = new NewsBehavior();
			info.setId(null);
			info.setTarget("news");
			info.setTargetId(targetId);
			info.setBehaviorType(behaviorType);
			MemberBase creator = (MemberBase) baseDao.get(MemberBase.class, memberId);
			info.setCreator(creator);
			info.setCreateTime(new Date());
			baseDao.saveOrUpdate(info);
			
			if("like".equalsIgnoreCase(behaviorType)){
				int goodPost = newsInfo.getGoodPost()==null?1:newsInfo.getGoodPost()+1;
				newsInfo.setGoodPost(goodPost);
			}else {
				int badPost = newsInfo.getBadPost()==null?1:newsInfo.getBadPost()+1;
				newsInfo.setBadPost(badPost);
			}
		}

		baseDao.saveOrUpdate(newsInfo);
		
		return true;
	}
	
	
	/**
	 * 新闻评论点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId 新闻id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean addNewsCommentBehavior(String memberId,String targetId,String behaviorType){
		
		NewsComment newsComment = (NewsComment) baseDao.get(NewsComment.class, targetId);
		if(null == newsComment) return false;
		NewsBehavior info = null;
		String hql = "from NewsBehavior t where t.target='comment' and FIND_IN_SET(t.behaviorType,'like,unlike')>0 and t.creator.id=? and t.targetId=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(targetId);
		
		List<NewsBehavior> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			info = list.get(0);
			if(info.getBehaviorType().equalsIgnoreCase(behaviorType)){
				//相同行为，则删除原来的
				baseDao.delete(info);
				if("like".equalsIgnoreCase(behaviorType)){
					int goodPost = newsComment.getGood()==null?0:newsComment.getGood()-1;
					goodPost = goodPost<0?0:goodPost;
					newsComment.setGood(goodPost);
				}else {
					int badPost = newsComment.getBad()==null?0:newsComment.getBad()-1;
					badPost = badPost<0?0:badPost;
					newsComment.setBad(badPost);
				}
				baseDao.saveOrUpdate(newsComment);
			}else {
				info.setBehaviorType(behaviorType);
				info.setCreateTime(new Date());
				baseDao.saveOrUpdate(info);
				
				int goodPost = newsComment.getGood()==null?0:newsComment.getGood();
				int badPost = newsComment.getBad()==null?0:newsComment.getBad();
				
				if("like".equalsIgnoreCase(behaviorType)){
					newsComment.setGood(goodPost+1);
					newsComment.setBad(badPost-1);
				}else {
					newsComment.setGood(goodPost-1);
					newsComment.setBad(badPost+1);
				}
				baseDao.saveOrUpdate(newsComment);
			}
		}else {
			info = new NewsBehavior();
			info.setId(null);
			info.setTarget("comment");
			info.setTargetId(targetId);
			info.setBehaviorType(behaviorType);
			MemberBase creator = (MemberBase) baseDao.get(MemberBase.class, memberId);
			info.setCreator(creator);
			info.setCreateTime(new Date());
			baseDao.saveOrUpdate(info);
			
			if("like".equalsIgnoreCase(behaviorType)){
				int goodPost = newsComment.getGood()==null?1:newsComment.getGood()+1;
				newsComment.setGood(goodPost);
			}else {
				int badPost = newsComment.getBad()==null?1:newsComment.getBad()+1;
				newsComment.setBad(badPost);
			}
			baseDao.saveOrUpdate(newsComment);
		}
		
		return true;
	}
	

	/**
	 * 添加评论
	 * @author zxtan
	 * @date 2017-05-03
	 * @param infoId
	 * @param memberId
	 * @param comment
	 * @param replyCommentId
	 * @param ip
	 * @return
	 */
	public boolean addNewsComment(String infoId, String memberId,String comment,String replyCommentId,String ip){
		
		NewsInfo newsInfo = (NewsInfo) baseDao.get(NewsInfo.class, infoId);
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
		if(null == newsInfo || null == member) return false;
		
		NewsComment info = new NewsComment();
		info.setId(null);
		info.setNewsInfo(newsInfo);
		info.setMember(member);
		info.setComment(comment);
		info.setOpTime(new Date());
		if(null != replyCommentId && !"".equals(replyCommentId)){
			NewsComment replyComment = (NewsComment) baseDao.get(NewsComment.class, replyCommentId);
			info.setReplyComment(replyComment);
			
			while (null != replyComment && null != replyComment.getId()) {
				int totalPost = replyComment.getTotalPost()==null?1:replyComment.getTotalPost()+1;
				replyComment.setTotalPost(totalPost);
				baseDao.saveOrUpdate(replyComment);
				
				replyComment = replyComment.getReplyComment();
			}
			
		}
		
		info.setIp(ip);
		info.setChecked("1");
		baseDao.saveOrUpdate(info);
				
		int totalPost = newsInfo.getTotalPost()==null?1:newsInfo.getTotalPost()+1;
		newsInfo.setTotalPost(totalPost);
		baseDao.saveOrUpdate(newsInfo);
		
		return true;
	}
}
