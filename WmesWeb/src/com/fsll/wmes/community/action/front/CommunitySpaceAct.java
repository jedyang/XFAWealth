package com.fsll.wmes.community.action.front;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.CommunitySpaceVO;
import com.fsll.wmes.community.vo.FocusMemberVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.entity.CommunityFocus;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.vo.IfaInfoVO;
import com.fsll.wmes.ifa.vo.IfaSpaceFundVO;
import com.fsll.wmes.ifa.vo.IfaSpacePortfoliosVO;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.news.vo.NewsInfoForListVO;
import com.fsll.wmes.web.service.WebFriendService;

@Controller
@RequestMapping("/front/community/space")
public class CommunitySpaceAct extends WmesBaseAct {
	
	@Autowired
	private CommunitySpaceService communitySpaceService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private IfaInfoService ifaInfoService;

	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private SysParamService sysParamService;
	/**
	 * 访问ifa的space
	 * @author mqzou 2017-03-16
	 */
	@RequestMapping(value = "/ifaSpace", method = RequestMethod.GET)
	public String ifaSpace(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String lang = this.getLoginLangFlag(request);
		String loginMemberId = "";//如果有登录，当前的登录人ID，用于后面判断该登录人是否有权限查看某些数据，比如策略，组合等
		Boolean viewerIsSpaceOwner = false;//用于判断当前浏览的是否自己的空间，用来判断是否显示某些管理按钮
		Boolean isShowAddFriendBtn = true;//是否显示添加好友按钮，默认显示
		String memberId=request.getParameter("id");//被查看人ID
		if(null!=loginUser){
			int memberType = loginUser.getMemberType();//当前登录者类型
			loginMemberId = loginUser.getId();
			if (null != loginUser.getMemberType()&&memberId.equals(loginUser.getId())) {
				viewerIsSpaceOwner = true; // 访问的ID与登录者的ID一样，则表示访问空间者刚好是自己
				//curDefCurrency = curMember.getDefCurrency();
				isShowAddFriendBtn = false;//自己访问自己，不需要显示添加好友按钮
			} else{
				//判断是否已添加该IFA为好友
				//isShowAddFriendBtn = !webFriendService.checkTwoMemberIsFriend(loginMemberId,memberId);
			}
			//获取当前登录者的涨跌
			String defDisplayColor = loginUser.getDefDisplayColor();
			model.put("defDisplayColor", defDisplayColor);
			model.put("loginMemberType", memberType);
			model.put("loginMemberId", loginUser.getMemberType());
			
			String langCode=getLoginLangFlag(request);
			String currency=loginUser.getDefCurrency();
			String currencyName = "";
			if(null==currency || "".equals(currency)){
				currency=CommonConstants.DEF_CURRENCY;
			}
			currencyName = sysParamService.findNameByCode(currency, langCode);
				
			CommunitySpaceVO vo=communitySpaceService.getIfaSpaceVO(memberId, langCode, currency,loginMemberId);
			if(null!=vo && memberId.equals(loginUser.getId())){
					vo.setIsCurMember(1);
					
			}
			vo.setBaseCurrency(currency);
			vo.setBaseCurrencyName(currencyName);
			
			model.put("currencyName", currencyName);
			model.put("memberId", memberId);
			model.put("loginMemberId", loginMemberId);
			model.put("memberType", loginUser.getMemberType());
			model.put("viewerIsSpaceOwner", viewerIsSpaceOwner);
			model.put("isShowAddFriendBtn", isShowAddFriendBtn);
			model.put("info", vo);
			
			//获取推荐策略实体
		    List<IfaSpaceStrategyInfoVO> ifaSpaceStrategyInfoList = vo.getRecommendedStrategies();
		    //获取推荐投资组合
		    List<IfaSpacePortfoliosVO> ifaSpacePortfoliosVO = vo.getRecommendedPortfolios();
		    //获取推荐基金
		    List<IfaSpaceFundVO> ifaSpaceFundList = vo.getFundList();
		    //输出新闻
		    jsonPaging=new JsonPaging();
			jsonPaging.setRows(3);
			jsonPaging.setPage(1);
			jsonPaging=communitySpaceService.findIfaRecommendNews(jsonPaging, loginUser.getId(), memberId,"");
			List<CommunityNewsListVO> newsList = jsonPaging.getList();
			model.put("newsList",newsList);
			//输出观点
		    jsonPaging=new JsonPaging();
			jsonPaging.setRows(3);
			jsonPaging.setPage(1);
			jsonPaging=communitySpaceService.findIfaRecommendInsight(jsonPaging, loginUser.getId(), memberId,lang);
			List<FrontCommunityTopicVO> insightList = jsonPaging.getList();
			model.put("insightList",insightList);
			//输出贴子
		    /*jsonPaging=new JsonPaging();
			jsonPaging.setRows(3);
			jsonPaging.setPage(1);
			jsonPaging=communitySpaceService.findIfaRecommendTopic(jsonPaging, loginUser.getId(), memberId,lang);
			List<FrontCommunityTopicVO> topicList = jsonPaging.getList();
			model.put("topicList",topicList);*/
		      
			//输出策略
	        model.put("IfaSpaceStrategyInfoList", ifaSpaceStrategyInfoList);
	        model.put("IfaSpaceStrategyInfoListCount",ifaSpaceStrategyInfoList==null?0:ifaSpaceStrategyInfoList.size());
	        model.put("latestStrategyProductCount",0);
	        if(null!=ifaSpaceStrategyInfoList&&!ifaSpaceStrategyInfoList.isEmpty())
	        {
	      	  model.put("latestStrategyId", ifaSpaceStrategyInfoList.get(0).getId());
	      	  model.put("latestStrategyName", ifaSpaceStrategyInfoList.get(0).getStrategyName());
	      	  model.put("latestStrategyProductCount", ifaSpaceStrategyInfoList.get(0).getContainProCount());
	        }
	        //输出组合
	        model.put("IfaSpacePortfoliosCount",ifaSpacePortfoliosVO==null?0:ifaSpacePortfoliosVO.size());
	        model.put("latestPortfoliosRate",0);
	        if(null!=ifaSpacePortfoliosVO&&!ifaSpacePortfoliosVO.isEmpty())
	        {
	      	  model.put("latestPortfoliosId", ifaSpacePortfoliosVO.get(0).getId());
	      	  model.put("latestPortfoliosName", ifaSpacePortfoliosVO.get(0).getPortfoliosName());
	      	  model.put("latestPortfoliosRate", ifaSpacePortfoliosVO.get(0).getReturnRate());
	        }
	      //输出推荐基金
	        model.put("recommendedFundCount",ifaSpaceFundList==null?0:ifaSpaceFundList.size());
	        if(null!=ifaSpaceFundList&&!ifaSpaceFundList.isEmpty())
	        {
	      	  model.put("latestFundId", ifaSpaceFundList.get(0).getFundId());
		    	  model.put("latestFundName", ifaSpaceFundList.get(0).getFundName());
		    	  model.put("latestFundincreaseRate", ifaSpaceFundList.get(0).getIncreaseRate());
		    	  model.put("latestPeriodName", ifaSpaceFundList.get(0).getPeriodName());
	        }
	        
	        //关注人列表
	        List<FocusMemberVO> focusList=communitySpaceService.findFocusMemberList(memberId,loginMemberId,langCode);
	        model.put("focusList", focusList);
	        //粉丝列表
	        List<FocusMemberVO> followersList=communitySpaceService.findFollowerList(memberId,loginMemberId,langCode);
	        model.put("followersList", followersList);
	        
	        if(loginMemberId.equals(memberId)){//如果查看的是自己的空间
	        	model.put("myself", "1");
	        }else{
	        	model.put("myself", "0");
	        }
	        
	        CommunityFocus focus=communitySpaceService.findCommunityFocus(loginMemberId, memberId);
	        if(null!=focus){
	        	model.put("focused", "1");//已关注
	        }else {
	        	model.put("focused", "0");//未关注
			}
	        boolean isFriend=webFriendService.checkTwoMemberIsFriend(loginMemberId, memberId);
	        model.put("isFriend", isFriend);
	        
	        String dateTimeFmt=loginUser.getDateFormat();
			if(null==dateTimeFmt || "".equals(dateTimeFmt))
				dateTimeFmt=CommonConstants.FORMAT_DATE;
			model.put("dateFormt", dateTimeFmt+" "+CommonConstants.FORMAT_TIME);
		}
		
		return "community/space";
	}
	
	
	/**
	 * 访问投资人的space
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value = "/investorSpace", method = RequestMethod.GET)
	public String investorSpace(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		//String lang = this.getLoginLangFlag(request);
		String loginMemberId = "";//如果有登录，当前的登录人ID，用于后面判断该登录人是否有权限查看某些数据，比如策略，组合等
		//Boolean viewerIsSpaceOwner = false;//用于判断当前浏览的是否自己的空间，用来判断是否显示某些管理按钮
		//Boolean isShowAddFriendBtn = true;//是否显示添加好友按钮，默认显示
		String memberId=request.getParameter("id");//被查看人ID
		if(null!=loginUser){
			int memberType = loginUser.getMemberType();//当前登录者类型
			loginMemberId = loginUser.getId();
			/*if (null != loginUser.getMemberType()&&memberId.equals(loginUser.getId())) {
				viewerIsSpaceOwner = true; // 访问的ID与登录者的ID一样，则表示访问空间者刚好是自己
				//curDefCurrency = curMember.getDefCurrency();
				isShowAddFriendBtn = false;//自己访问自己，不需要显示添加好友按钮
			} else{
				//判断是否已添加该IFA为好友
				//isShowAddFriendBtn = !webFriendService.checkTwoMemberIsFriend(loginMemberId,memberId);
			}*/
			//获取当前登录者的涨跌
			String defDisplayColor = loginUser.getDefDisplayColor();
			model.put("defDisplayColor", defDisplayColor);
			model.put("loginMemberType", memberType);
			model.put("loginMemberId", loginUser.getMemberType());
		}
		String langCode=getLoginLangFlag(request);
		String currency=loginUser.getDefCurrency();
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		
		CommunitySpaceVO vo=communitySpaceService.getInvestorSpaceVO(memberId, langCode, loginMemberId);//(memberId, langCode, currency,loginMemberId);
		if(null!=vo){
			if(memberId.equals(loginUser.getId())){
				vo.setIsCurMember(1);
			}
			vo.setBaseCurrency(currency);
		}else{
			vo = new CommunitySpaceVO();
		}
		
		model.put("info", vo);
		
        //关注人列表
        List<FocusMemberVO> focusList=communitySpaceService.findFocusMemberList(memberId,loginMemberId,langCode);
        model.put("focusList", focusList);
        //粉丝列表
        List<FocusMemberVO> followersList=communitySpaceService.findFollowerList(memberId,loginMemberId,langCode);
        model.put("followersList", followersList);
        
        if(loginMemberId.equals(memberId)){//如果查看的是自己的空间
        	model.put("myself", "1");
        }else{
        	model.put("myself", "0");
        }
        
        CommunityFocus focus=communitySpaceService.findCommunityFocus(loginMemberId, memberId);
        if(null!=focus){
        	model.put("focused", "1");//已关注
        }else {
        	model.put("focused", "0");//未关注
		}
        boolean isFriend=webFriendService.checkTwoMemberIsFriend(loginMemberId, memberId);
        model.put("isFriend", isFriend);
    	model.put("memberId", loginMemberId);
		model.put("memberType", loginUser.getMemberType());
		
		String dateTimeFmt=loginUser.getDateFormat();
		if(null==dateTimeFmt || "".equals(dateTimeFmt))
			dateTimeFmt=CommonConstants.FORMAT_DATE;
		model.put("dateFormt", dateTimeFmt+" "+CommonConstants.FORMAT_TIME);
		
		return "community/investorspace";
	}
	
	/**
	 * 获取ifa推荐的新闻列表
	 * @author mqzou 2017-03-17
	 */
	@RequestMapping(value = "/getIfaRecommendNews")
	public void getIfaRecommendNews(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
	    String keyWord=request.getParameter("keyWord");
	    if(null!=keyWord && !"".equals(keyWord))
	    	keyWord=toUTF8String(keyWord);
		//String id=request.getParameter("id");
		jsonPaging=getJsonPaging(request);
		jsonPaging=communitySpaceService.findIfaRecommendNews(jsonPaging, loginUser.getId(),loginUser.getId(),keyWord);
		JsonUtil.toWriter(jsonPaging,response);
	}
	
	/**
	 * 获取ifa推荐的新闻列表管理
	 * @author mqzou 2017-03-17
	 */
	@RequestMapping(value = "/newsManage")
	public String getIfaRecommendNewsManage(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		return "community/newslistmanage";
	}
	
	/**
	 * 查看ifa推荐的新闻列表
	 * @author mqzou 2017-03-17
	 */
	@RequestMapping(value = "/recommendNews")
	public String ifaRecommendNews(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		
		 MemberBase loginUser=getLoginUser(request);
			String ifaMemberId=request.getParameter("id");
			String langCode=getLoginLangFlag(request);
			IfaInfoVO ifa=ifaInfoService.findIfaInfo(ifaMemberId, langCode);
			model.put("ifa", ifa);
	    	String dateFormat=loginUser.getDateFormat();
	    	if(null==dateFormat || "".equals(dateFormat) )
	    	   dateFormat=CommonConstants.FORMAT_DATE;
	    	model.put("dateFormat", dateFormat);
		return "community/ifaRecommendNewsList";
	}
	
	/**
	 * 查看ifa推荐的新闻列表
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value = "/ifaRecommendNews")
	public void getIfaRecommendNewsList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		String ifaMemberId=request.getParameter("id");
		MemberBase loginUser=getLoginUser(request);
		//String id=request.getParameter("id");
		jsonPaging=getJsonPaging(request);
		jsonPaging=communitySpaceService.findIfaRecommendNews(jsonPaging, loginUser.getId(),ifaMemberId,"");
		JsonUtil.toWriter(jsonPaging,response);
	}
	
	/**
	 * ifa删除推荐新闻
	 * @author mqzou 2017-03-17
	 */
	@RequestMapping(value = "/delIfaRecommendNews")
	public void delIfaRecommendNews(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		//MemberBase loginUser=getLoginUser(request);
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			String id=request.getParameter("id");
			CommunityTopic topic=communityService.findCommunityTopicById(id);
			if(null!=topic){
				topic.setStatus(-1);//删除
				communityService.saveOrUpdateTopic(topic, null);
				result.put("result", true);
			}
		} catch (Exception e) {
			result.put("result", false);
		}
		
		
		JsonUtil.toWriter(result,response);
	}
	
	
	/**
	 * 获取获取用户的帖子列表
	 * @author mqzou 2017-03-20
	 */
	@RequestMapping(value = "/getTopicList")
	public void getTopicList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String ifaMemberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging.setSort(" t.create_time");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findIfaRecommendTopic(jsonPaging, loginUser, ifaMemberId,langCode);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	
	/**
	 * 获取ifa的问题列表
	 * @author mqzou 2017-03-20
	 */
	@RequestMapping(value = "/getIfaQuestionList")
	public void getIfaQuestionList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String ifaMemberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging.setSort(" r.createTime");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findIfaQuestionList(jsonPaging, ifaMemberId, loginUser.getId(), langCode);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	/**
	 * 获取投资人的问题列表
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value = "/getInvQuestionList")
	public void getInvQuestionList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String invMemberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging.setSort(" r.createTime");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findInvestorQuestionList(jsonPaging, invMemberId, loginUser.getId(), langCode);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	
	/**
	 * 获取ifa的收藏帖子列表
	 * @author mqzou 2017-03-20
	 */
	@RequestMapping(value = "/getFavoriteList")
	public void getFavoriteList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String memberId=request.getParameter("id");
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		String langCode=getLoginLangFlag(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging.setSort(" b.create_time");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findIfaFavoriteList(jsonPaging, memberId, loginUser.getId(), langCode,dateFormat);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	
	/**
	 * 获取ifa的收藏帖子列表
	 * @author mqzou 2017-03-20
	 */
	@RequestMapping(value = "/getMyFavoriteList")
	public void getMyFavoriteList(HttpServletResponse response,HttpServletRequest request,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		//String memberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging.setSort(" b.create_time");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findIfaFavoriteList(jsonPaging, loginUser.getId(), loginUser.getId(), langCode,dateFormat);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	
	/**
	 * 加关注
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value="/addFocus")
	public void addFocus(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		String memberId=request.getParameter("id");
		if(null!=memberId && !"".equals(memberId)){
			CommunityFocus focus=communitySpaceService.findCommunityFocus(loginUser.getId(), memberId);
			if(null==focus){
				focus=new CommunityFocus();
				focus.setCreateTime(DateUtil.getCurDate());
				focus.setCreator(loginUser);
				MemberBase focusMember=memberBaseService.findById(memberId);
				focus.setFocus(focusMember);
				focus=communitySpaceService.saveFocus(focus);
				if(null!=focus){
					result.put("result", true);
				}else {
					result.put("result", false);
				}
			}else {
				result.put("result", false);
			}
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	/**
	 * 取消关注
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value="/cancelFocus")
	public void cancelFocus(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		String memberId=request.getParameter("id");
		if(null!=memberId && !"".equals(memberId)){
			CommunityFocus focus=communitySpaceService.findCommunityFocus(loginUser.getId(), memberId);
			if(null!=focus){
				boolean b=communitySpaceService.deleteFocus(focus);
				if(b){
					result.put("result", true);
				}else {
					result.put("result", false);
				}
			}else {
				result.put("result", false);
			}
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 投资人向ifa提问
	 * @author mqzou 2017-03-21
	 */
	@RequestMapping(value="/sendQuestion")
	public void sendQuestion(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			MemberBase loginUser=getLoginUser(request);
			String ifaMmeberId=request.getParameter("id");
			String content=request.getParameter("content");
			content=toUTF8String(StringEscapeUtils.unescapeHtml(content.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
			
			MemberBase ifaMember=memberBaseService.findById(ifaMmeberId);
			if(null!=ifaMember){
				CommunityQuestion question=new CommunityQuestion();
				question.setAnswerCount(0);
				question.setAnswerer(ifaMember);
				question.setCommentCount(0);
				question.setCreateTime(DateUtil.getCurDate());
				question.setCreator(loginUser);
				question.setIsAnswer(0);
				question.setIsClose(0);
				question.setLikeCount(0);
				question.setReadCount(0);
				question.setStatus(1);
				question.setSupportComment(1);
				question.setTitle(content);
				question.setUnlikeCount(0);
				question=communitySpaceService.saveCommunityQuestion(question);
				if(null!=question){
					result.put("result", true);
				}else {
					result.put("result", false);
				}
			}
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	/**
	 * 更新用户的highlight
	 * @author mqzou 2017-05-17
	 */
	@RequestMapping(value="/updateHighlight")
	public void updateHighlight(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> map=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		try {
			String highlight=request.getParameter("highlight");
			loginUser.setHighlight(highlight);
			memberBaseService.saveOrUpdate(loginUser);
			map.put("result", true);
		} catch (Exception e) {
			map.put("false", false);
		}
		JsonUtil.toWriter(map, response);
		
		
	}
	
	
}

