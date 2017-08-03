package com.fsll.wmes.community.action.front;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.community.vo.CommunityCommentVO;
import com.fsll.wmes.community.vo.CommunitySectionVO;
import com.fsll.wmes.community.vo.FrontPopularityRankVO;
import com.fsll.wmes.community.vo.QuestionDetailVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.community.vo.TopicShareParamtersVO;
import com.fsll.wmes.entity.CommunityBehavior;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.CommunityContent;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.SysSearchHistory;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.vo.IfaInfoVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;


/**社区action类
 * @author wwlin
 * 
 */
@Controller
@RequestMapping("/front/community/info")
public class CommunityAct extends WmesBaseAct{
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private IfaInfoService ifaInfoService;
	
	/**
	 * 分享新闻、贴子、组合、策略等到贴子
	 * 
	 * @author wwlin
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/saveTopicFromShare", method = RequestMethod.POST)
	public void saveTopicFromShare(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String sourceType = request.getParameter("sourceType");
		String sourceId = request.getParameter("sourceId");
		String sectionId = request.getParameter("sectionId");
		String visitor = request.getParameter("visitor");
		//当前登录用户
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		//组装参数
		TopicShareParamtersVO paramVO = new TopicShareParamtersVO();
		paramVO.setContent(content);
		paramVO.setCreator(loginUser);
		paramVO.setSectionId(sectionId);
		paramVO.setSourceId(sourceId);
		paramVO.setSourceType(sourceType);
		paramVO.setTitle(title);
		paramVO.setVisitor(visitor);
		
		CommunityTopic topic = communityService.saveTopicFromShare(paramVO);
		if (null != topic&&StringUtils.isNotBlank(topic.getId())) {
			result.put("result", true);
			result.put("code", "global.success.save");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success.save", null));
		} else {
			result.put("result", false);
			result.put("code", "global.failed.save");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.failed.save", null));
		}
		
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取我的Dynamic贴子数据显示页（我发的贴子跟我关注的人发的贴子）
	 * @return
	 */
	@RequestMapping(value = "/getMyDynamicTopic", method = RequestMethod.GET)
	public String getMyDynamicTopic(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		/*String lang = this.getLoginLangFlag(request);
		String loginMemberId = "";
		MemberBase curMember = this.getLoginUser(request);*/
		
		return "ifa/info/ifaList";
	}
	
	/**
	 *  获取我的Dynamic贴子数据的分页列表（我发的贴子跟我关注的人发的贴子）
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getMyDynamicTopicListJson", method = RequestMethod.POST)
	public void getMyDynamicTopicListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		
		jsonPaging = communityService.getMyDynamicTopicList(jsonPaging, loginUser,null, keywords,langCode);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取版块贴子列表
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getSetionTypeTopicListJson", method = RequestMethod.POST)
	public void getFundTopicListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		String sectionId = request.getParameter("secitionid");
		jsonPaging = this.getJsonPaging(request);
		
		//jsonPaging = communityService.getSetionTypeTopicListJson(jsonPaging, baseMem.getId(),keywords,langCode,sectionId);
		jsonPaging = communityService.getSetionTypeTopicListJson2(jsonPaging, baseMem,langCode	,keywords,sectionId);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取管理员推荐的贴子数据的分页列表（我发的贴子跟我关注的人发的贴子）
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getRecommandTopicListJson", method = RequestMethod.POST)
	public void getRecommandTopicListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		//String sectionId = request.getParameter("secitionid");
		jsonPaging = this.getJsonPaging(request);
		
		//jsonPaging = communityService.getSetionTypeTopicListJson(jsonPaging, baseMem.getId(),keywords,langCode,sectionId);
		jsonPaging=communityService.getRecommandTopicListJson(jsonPaging, baseMem, keywords, langCode);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取贴子数据的分页列表，关键字搜索
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getTopicListJson", method = RequestMethod.POST)
	public void getTopicListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		
		jsonPaging = communityService.getTopicListJson(jsonPaging, baseMem.getId(),keywords,langCode,false);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取问题数据的分页列表，关键字搜索
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getSearchQuestionListJson", method = RequestMethod.POST)
	public void getSearchQuestionListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		//MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		
		jsonPaging = communityService.getSearchQuestionListJson(jsonPaging,keywords,langCode);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取观点数据的分页列表，关键字搜索
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getSearchInsightListJson", method = RequestMethod.POST)
	public void getSearchInsightListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		
		jsonPaging = communityService.getTopicListJson(jsonPaging, baseMem.getId(),keywords,langCode,true);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  获取IFA与投资人数据的分页列表，关键字搜索
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/getSearchUserListJson", method = RequestMethod.POST)
	public void getSearchUserListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String keywords = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		
		jsonPaging = communityService.getSearchUserListJson(jsonPaging, baseMem.getId(),keywords,langCode);
		
		this.toJsonString(response, jsonPaging);
	}
	
	
	/**
	 *  显示社区主页
	 * @author wwlin 
	 * */
	@RequestMapping(value = "/community")
	public String community(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberBase baseMem = this.getLoginUser(request);
		//获取版块列表
		List<CommunitySectionVO> communitySectionList =  communityService.findSectionForSelect(langCode);
		model.put("communitySectionList", communitySectionList);
		
		List<FrontPopularityRankVO> frontPopularityRankList = communityService.getYesterdayTopCommentTopicCreator(baseMem.getId(),langCode);
		model.put("frontPopularityRankList", frontPopularityRankList);
		
		List<FrontPopularityRankVO> weekTopCommentTopicCreatorList = communityService.getWeekTopCommentTopicCreator(baseMem.getId(),langCode);
		model.put("weekTopCommentTopicCreatorList", weekTopCommentTopicCreatorList);
		
		List<FrontPopularityRankVO> totalTopCommentTopicCreatorList = communityService.getTotalTopCommentTopicCreator(baseMem.getId(),langCode);
		model.put("totalTopCommentTopicCreatorList", totalTopCommentTopicCreatorList);
		
		List<FrontPopularityRankVO> weekTopCommentTopicList = communityService.getWeekTopCommentTopicList(langCode,baseMem.getId());
		model.put("weekTopCommentTopicList", weekTopCommentTopicList);
		
		model.put("loginMemberId", baseMem.getId());
		model.put("loginMemberType", baseMem.getMemberType());
		//获取
		return "community/community";
	}
	
	@RequestMapping(value = "/search")
	public String search(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		//String langCode = this.getLoginLangFlag(request);
		String keywords = com.fsll.common.util.RequestUtils.getQueryParam(request,"keywords"); //request.getParameter("keywords");
		
		SysSearchHistory keyword = new SysSearchHistory();
		keyword.setCreateTime(new Date());
		
		keyword.setSearchContent(keywords);
		keyword.setSearchType("community");
		if(null!=loginUser)keyword.setCreator(loginUser);
		
		
		List<SysSearchHistory> historyList = communityService.getSysSearchHistoryList();
		model.put("historyList", historyList);
		
		if(communityService.findSameKeywords(keywords)==0)communityService.saveOrUpdateKeyword(keyword);
		
		model.put("loginMemberId", loginUser.getId());
		model.put("loginMemberType", loginUser.getMemberType());
		
		return "community/search";
	}
	
	/**
	 * 点赞与扔鸡蛋
	 * @author wwlin
	 * 
	 */
	@RequestMapping(value="/updateLikeOrUnlikeCount",method= RequestMethod.POST)
	public void updateLikeOrUnlikeCount(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		CommunityTopic topic = new CommunityTopic();
		try {
			String topicId = request.getParameter("topicid");
			String type = request.getParameter("type");
			if (StringUtils.isNotBlank(topicId) && StringUtils.isNotBlank(type)) {
				topic = communityService.findCommunityTopicById(topicId);
				if("1".equals(type)){
					Integer likeCounter = topic.getLikeCount();
					if(likeCounter == null){
						likeCounter = 1;
					}else{
						likeCounter++;
					}
					topic.setLikeCount(likeCounter);
					topic = communityService.updateCommunityTopic(topic);
					flag = true;
				}else{
					Integer downCounter = topic.getUnlikeCount();
					if(downCounter == null){
						downCounter = 1;
					}else{
						downCounter++;
					}
					topic.setUnlikeCount(downCounter);
					topic = communityService.updateCommunityTopic(topic);
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		result.put("insightInfo", topic);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  保存发帖内容
	 * @author mqzou 2017-03-14 
	 * */
	@RequestMapping(value = "/saveTopic")
	public void saveTopic(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			String title=request.getParameter("title");
			String content=request.getParameter("content");
			String isInsight=request.getParameter("isInsight");
			String sectionId=request.getParameter("sectionId");
			String publish=request.getParameter("publish");
			title=toUTF8String(StringEscapeUtils.unescapeHtml(title.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
			content=toUTF8String(StringEscapeUtils.unescapeHtml(content.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
			
		    CommunitySection section=communityService.getCommunitySectionInfo(sectionId);
			CommunityTopic topic=new CommunityTopic();
			topic.setCreateTime(DateUtil.getCurDate());
			topic.setCreator(loginUser);
			topic.setIsInsight(StringUtils.isNoneBlank(isInsight)?Integer.valueOf(isInsight):0);
			topic.setVisitor(publish);
			topic.setSection(section);
			topic.setTitle(StringEscapeUtils.unescapeHtml(title));
			topic.setSupportComment(1);
			topic.setStatus(1);
			CommunityContent communityContent=new CommunityContent();
			communityContent.setContent(content);
			topic=communityService.saveOrUpdateTopic(topic, communityContent);
			result.put("result", true);
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	/**
	 * 发布帖子页面
	 * @author mqzou 2017-03-14 
	 */
	@RequestMapping(value = "/publishTopic")
	public String publishTopic(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode=getLoginLangFlag(request);
		List<CommunitySectionVO> sectionList=communityService.findSectionForSelect(langCode);
		model.put("sectionList", sectionList);
		MemberBase loginUser=getLoginUser(request);
		if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType()){
			model.put("memberType", 1);
		}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==loginUser.getSubMemberType()) {
			model.put("memberType", 2);
		}
		return "community/publishTopic";
	}
	
	/**
	 * 帖子或者问题的详情
	 * @author mqzou 2017-03-15
	 */
	@RequestMapping(value = "/topicDetail")
	public String topicDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		if(null!=loginUser){
			String id=request.getParameter("id");
			String type=request.getParameter("type");
			if("t".equals(type)){//帖子
				String dateFormat=loginUser.getDateFormat();
				if(null==dateFormat || "".equals(dateFormat)){
					dateFormat=CommonConstants.FORMAT_DATE;
				}
				TopicDetailVO vo=communityService.findTopicDetail(id,langCode , loginUser);
				vo.setCreateDate(DateUtil.getDateTimeGoneFormatStr(DateUtil.StringToDate(vo.getCreateDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT), langCode, dateFormat));
				vo.setContent(StringEscapeUtils.unescapeHtml(vo.getContent()));
				//公开的并且是原文的才可以转发
				if("news".equals(vo.getSourceType())){
					model.put("allowTran", "1");
				}else {
					model.put("allowTran", "0");
				}
			//	vo.setCreateDate(DateUtil.getDateStr(vo.getCreateDate(),  CommonConstants.FORMAT_DATE_TIME, dateFormat+" "+CommonConstants.FORMAT_TIME));
				model.put("detail", vo);
				try {
					saveVisit(loginUser, "", id, CommonConstantsWeb.WEB_VISIT_BUS_VIEW, CommonConstantsWeb.WEB_VISIT_COMMUNITY_TOPIC, getRemoteHost(request));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}else if ("q".equals(type)) {//问题
				QuestionDetailVO vo=communityService.findqQuestionDetail(id, langCode, loginUser);
				model.put("detail", vo);
				if(vo.getAnswerId().equals(loginUser.getId())){
					model.put("allowAns", true);
				}else {
					model.put("allowAns", false);
				}
				try {
					saveVisit(loginUser, "", id, CommonConstantsWeb.WEB_VISIT_BUS_VIEW, CommonConstantsWeb.WEB_VISIT_COMMUNITY_QUESTION, getRemoteHost(request));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			model.put("type", type);
			model.put("loginMemberType", loginUser.getMemberType());
			model.put("loginMemberId", loginUser.getId());
			
			
		}
		return "community/topicDetail";
	}
	
	/**
	 * 保存对帖子的动作
	 * @author mqzou 2017-03-15
	 */
	@RequestMapping(value = "/saveBehavior")
	public void saveBehavior(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			String id=request.getParameter("id");
			String behaviorType=request.getParameter("behavior");
			String target=request.getParameter("type");
			String isCancel=request.getParameter("isCancel");
			if("t".equals(target)){
				target=CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC;
			}else if ("q".equals(target)) {
				target=CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION;
			}else if ("c".equals(target)) {
				target=CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT;
			}
			if(null==isCancel || !"1".equals(isCancel)){//如果不是取消动作
				CommunityBehavior behavior=new CommunityBehavior();
				behavior.setBehaviorType(behaviorType);
				behavior.setCreateTime(DateUtil.getCurDate());
				behavior.setCreator(loginUser);
				behavior.setTarget(target);
				behavior.setTargetId(id);
				behavior=communityService.saveBehavior(behavior);
				result.put("result", true);
			}else {
				CommunityBehavior behavior=communityService.findBehavior(id, loginUser.getId(), behaviorType, target);
				if(null!=behavior){
					communityService.deleteBehavior(behavior);
					result.put("result", true);
				}
				
			}
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result,response);
		
	}
	/**
	 * 保存帖子的回复或评论
	 * @author mqzou 2017-03-15
	 */
	@RequestMapping(value = "/saveComment")
	public void saveComment(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			String target=request.getParameter("target");//帖子t、问题a、评论c  针对被评论的对象 
			String id=request.getParameter("id");
			String type=request.getParameter("type");//帖子t、问题q //针对来源 topicId
			String content=request.getParameter("content");
			String isAnswer=request.getParameter("isAnswer");
			if("t".equals(target)){
				CommunityTopic topic=communityService.findCommunityTopicById(id);
				if(null!=topic){
					CommunityComment comment=new CommunityComment();
					comment.setContent(content);
					comment.setCreateTime(DateUtil.getCurDate());
					comment.setCreator(loginUser);
					comment.setStatus(1);
					comment.setTargetId(topic.getId());
					comment.setCommentType("comment");
					comment.setTarget(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
					CommunityCommentVO vo=communityService.saveComment(comment,topic, langCode);
					result.put("result", true);
					result.put("info", vo);
				}
				
			}else if ("a".equals(target)) {
				CommunityQuestion question=communityService.findCommunityQuestion(id);
				if(null!=question){
					CommunityComment comment=new CommunityComment();
					comment.setContent(content);
					comment.setCreateTime(DateUtil.getCurDate());
					comment.setCreator(loginUser);
					comment.setStatus(1);
					comment.setTargetId(question.getId());
					comment.setTarget(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
					if("1".equals(isAnswer)){//如果是回答问题
						comment.setCommentType("answer");
						CommunityCommentVO vo=communityService.saveComment(comment,null,langCode);
						result.put("result", true);
						result.put("info", vo);
					}else{
						comment.setCommentType("comment");
						CommunityCommentVO vo=communityService.saveComment(comment,question,langCode);
						result.put("result", true);
						result.put("info", vo);
					}
				}
				
			}else if ("c".equals(target)) {
				CommunityComment communityComment=communityService.findCommunityComment(id);
				if(null!=communityComment){
					CommunityComment comment=new CommunityComment();
					comment.setContent(content);
					comment.setCreateTime(DateUtil.getCurDate());
					comment.setCreator(loginUser);
					comment.setStatus(1);
					comment.setTargetId(communityComment.getTargetId());
					comment.setParent(communityComment);
					comment.setCommentType("comment");
					comment.setTarget(communityComment.getTarget());
					if("t".equals(type)){
						CommunityTopic topic=communityService.findCommunityTopicById(communityComment.getTargetId());
						CommunityCommentVO vo=communityService.saveComment(comment,  topic,langCode);
						result.put("info", vo);
						result.put("result", true);
					}else if ("q".equals(type)) {
						CommunityQuestion question=communityService.findCommunityQuestion(communityComment.getTargetId());
						CommunityCommentVO vo=communityService.saveComment(comment, question,langCode);
						result.put("result", true);
						result.put("info", vo);
					}
				}
			}
			
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取帖子或问题的所有评论
	 * @author mqzou 2017-03-15
	 */
	@RequestMapping(value="/getCommentList")
	public void getCommentList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase member=getLoginUser(request);
		String id=request.getParameter("id");
		jsonPaging=getJsonPaging(request);
		jsonPaging.setOrder("desc");
		jsonPaging.setSort("createTime");
		jsonPaging=communityService.findTopicComments(jsonPaging, id, langCode, member);
		JsonUtil.toWriter(jsonPaging, response);
	}
	/**
	 * 获取问题的所有回答
	 * @author mqzou 2017-03-15
	 */
	@RequestMapping(value="/getAnswerList")
	public void getAnswerList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase member=getLoginUser(request);
		String id=request.getParameter("id");
		List list=communityService.findAnswerList(id, langCode, member);
		JsonUtil.toWriter(list, response);
	}
	
	/**
	 * 保存帖子转发内容
	 * @author mqzou 2017-03-17
	 */
	@RequestMapping(value="/topicForward")
	public void topicForward(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			MemberBase loginUser=getLoginUser(request);
			String id=request.getParameter("id");
			String content=request.getParameter("content");
			String publishTo=request.getParameter("publishTo");
			CommunityTopic source=communityService.findCommunityTopicById(id);
			if(null!=source){
	             content=toUTF8String(StringEscapeUtils.unescapeHtml(content.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
				CommunityTopic topic=new CommunityTopic();
				topic.setCreateTime(DateUtil.getCurDate());
				topic.setCreator(loginUser);
				topic.setIsInsight(0);
				topic.setVisitor(publishTo);
				topic.setSection(source.getSection());
				topic.setTitle(source.getTitle());
				topic.setSupportComment(1);
				topic.setSourceId(source.getId());
				topic.setSourceType(CommonConstantsWeb.TOPIC_SOURCE_TYPE_TOPIC);
				CommunityContent communityContent=new CommunityContent();
				communityContent.setContent(content);
				topic=communityService.saveOrUpdateTopic(topic, communityContent);
				result.put("result", true);
			}else{
				result.put("result", false);
			}
			
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	/**
	 * 我的观点列表页面
	 * @author wwlin
	 * */
	@RequestMapping(value = "/insihtList", method = RequestMethod.GET)
	public String insightList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=this.getLoginLangFlag(request);
		String myself = "0";
		MemberBase loginUser=getLoginUser(request);
		String ifaMemberId=request.getParameter("id");//当前查看的IFA
		IfaInfoVO ifa=ifaInfoService.findIfaInfo(ifaMemberId, langCode);
		model.put("ifa", ifa);
    	String dateFormat=loginUser.getDateFormat();
    	if(null==dateFormat || "".equals(dateFormat) )
    	   dateFormat=CommonConstants.FORMAT_DATE;
    	model.put("dateFormat", dateFormat);
    	model.put("myself", myself);

		return "community/insihtList";
	}
	
	/**
	 * 观点列表
	 * @author wwlin
	 * */
	@RequestMapping(value = "/insightListJson", method = RequestMethod.POST)
	public void insightListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String creatorId = request.getParameter("id");
		String keywords = request.getParameter("keywords");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = communityService.findIfaInsight(jsonPaging, baseMem.getId(),creatorId,langCode,keywords);
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 编辑器示例
	 * @author wwlin
	 * */
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		

		return "community/demo";
	}
}
