package com.fsll.wmes.news.action.front;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.community.vo.CommunitySectionVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.news.service.NewsInfoService;
import com.fsll.wmes.news.vo.NewsCategoryVO;
import com.fsll.wmes.news.vo.NewsCommentVO;
import com.fsll.wmes.news.vo.NewsInfoForListVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.news.vo.NewsSimpleInfoVO;

@Controller
@RequestMapping("/front/news/info")
public class FrontNewsInfoAction extends WmesBaseAct {

	@Autowired
	private NewsInfoService newsInfoService;
	@Autowired
	private CommunityService communityService;
	
	/**
	 * 新闻首页
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/newsIndex")
	public String newsIndex(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		//JsonPaging jsonPaging=new JsonPaging();
		List<NewsSimpleInfoVO> topList=newsInfoService.getTopNews(langCode);
		model.put("topList", topList);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		
		List<NewsInfoForListVO> policyList =newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_POLICY, langCode,1,dateFormat);
		model.put("policyList", policyList);
		
		List<NewsInfoForListVO> economyList=newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_ECONOMY, langCode,1,dateFormat);
		model.put("economyList", economyList);
		
		List<NewsInfoForListVO> marketList=newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_MARKETS, langCode,3,dateFormat);	
		model.put("marketList", marketList);
		
		List<NewsInfoForListVO> industriesList=newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_INDUSTRIES, langCode,2,dateFormat);	
		model.put("industriesList", industriesList);
		
		List<NewsInfoForListVO> worldList=newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_WORLD, langCode,3,dateFormat);	
		model.put("worldList", worldList);
		
		List<NewsInfoForListVO> indepthList=newsInfoService.findNewsByCatagoryCode(CommonConstantsWeb.NEWS_CATEGORY_INDEPTH, langCode,3,dateFormat);	
		model.put("indepthList", indepthList);
		
		
		List<NewsCategoryVO> categoryList=newsInfoService.findNewsCategoryList(langCode);
		model.put("categoryList", categoryList);
		
		return "ifa/discover/news/newsIndex";
	}
	
	/**
	 * 新闻列表
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/newsList")
	public String newsList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode=getLoginLangFlag(request);
		String catagoryId=request.getParameter("id");
		String code=request.getParameter("code");
		if(null!=code && !"".equals(code)){
			/*NewsCategoryVO vo=newsInfoService.findCategoryByCode(code, langCode);
			model.put("info", vo);*/
			model.put("name", code);
		}else {
			NewsCategoryVO vo=newsInfoService.findCategoryById(catagoryId,langCode);
			model.put("parentList", vo.getParent());
			model.put("name", vo.getCode());
		}
		//String langCode=getLoginLangFlag(request);
		//jsonPaging=newsInfoService.findNewsByCatagoryId(jsonPaging, catagoryId, langCode);
		
		List<NewsCategoryVO> categoryList=newsInfoService.findNewsCategoryList(langCode);
		model.put("categoryList", categoryList);
		
		return "ifa/discover/news/newsList";
	}
	
	/**
	 * 新闻列表
	 * @author mqzou 2017-03-07
	 */
	@RequestMapping(value = "/newsListJson")
	public void newListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String categoryId=request.getParameter("id");
		String code=request.getParameter("code");
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		jsonPaging=getJsonPaging(request);
		if(null!=code && !"".equals(code)){
		    if(CommonConstantsWeb.NEWS_CATEGORY_RECOMMEND.equals(code)){
		    	jsonPaging=newsInfoService.findRecommendNews(jsonPaging, langCode,dateFormat);
		    	
		    }else if (CommonConstantsWeb.NEWS_CATEGORY_HEADLINES.equals(code)) {
		    	jsonPaging=newsInfoService.findHeadlineNews(jsonPaging,langCode,dateFormat);
			}
		}else {
			jsonPaging=newsInfoService.findNewsByCatagoryId(jsonPaging, categoryId, langCode,dateFormat);	
		}
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 新闻内容
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/newsContent")
	public String newsContent(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (null != loginUser) {
			String dateFormat = "";

			dateFormat = loginUser.getDateFormat();
			model.put("memberType", loginUser.getMemberType());

			if (null == dateFormat || "".equals(dateFormat)) {
				dateFormat = CommonConstants.FORMAT_DATE;
			}
			dateFormat = dateFormat + " " + CommonConstants.FORMAT_TIME;

			String id = request.getParameter("id");
			String langCode = getLoginLangFlag(request);
			NewsInfoVO vo = newsInfoService.findNewsInfoVO(id);

			vo.setUpOrDown(newsInfoService.checkNewsUpOrDown(id, loginUser));

			vo.setLastUpdate(DateUtil.dateToDateString(DateUtil.StringToDate(vo.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT), dateFormat));

			NewsBehavior behaviorVo = newsInfoService.findNewsBehavior(vo.getId(), CommonConstantsWeb.NEWS_BEHAVIOR_FAVORITE, loginUser.getId());
			if (null != behaviorVo) {
				vo.setIsFavorite("1");
			}

			model.put("info", vo);

			List<NewsCategoryVO> categoryList = newsInfoService.findNewsCategoryList(langCode);
			model.put("categoryList", categoryList);

			NewsCategoryVO categoryVO = newsInfoService.findCategoryByNews(id, langCode);
			model.put("categoryInfo", categoryVO);

			List<NewsInfoForListVO> list = newsInfoService.findListForProbably(vo.getId(), 4, langCode);
			model.put("recommendList", list);
			
			List<CommunitySectionVO> sectionList=communityService.findSectionForSelect(langCode);
			model.put("sectionList", sectionList);
			
			Object newsSession= request.getSession().getAttribute(id);
			if(null==newsSession || "".equals(newsSession)){
				NewsBehavior behavior=new NewsBehavior();
				behavior.setBehaviorType(CommonConstantsWeb.NEWS_BEHAVIOR_READ);
				behavior.setTargetId(id);
				behavior.setTarget(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS);
				behavior.setCreateTime(DateUtil.getCurDate());
				behavior.setCreator(loginUser);
				
				
				behavior=newsInfoService.saveBehavior(behavior);
				request.getSession().setAttribute(id, "1");
			}
			
			
		}
		return "ifa/discover/news/newsContent";
	}
	
	/**
	 * 编辑推荐新闻列表
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/getRecommendNewsList")
	public void getRecommendNewsList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat ||"".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		/*jsonPaging=new JsonPaging();
		jsonPaging.setRows(5);
		jsonPaging.setPage(1);*/
		jsonPaging=getJsonPaging(request);
		jsonPaging=newsInfoService.findRecommendNews(jsonPaging, langCode,dateFormat);
		
		JsonUtil.toWriter(jsonPaging.getList(), response);
	}
	
	/**
	 * 最热门的新闻列表
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/getHotNewsList")
	public void getHotNewsList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat ||"".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		jsonPaging=new JsonPaging();
		jsonPaging.setRows(10);
		jsonPaging.setPage(1);
		jsonPaging=newsInfoService.findHotNews(jsonPaging, langCode,dateFormat);
		
		JsonUtil.toWriter(jsonPaging.getList(), response);
	}
	
	/**
	 * 最新的新闻列表
	 * @author mqzou 2017-03-06
	 */
	@RequestMapping(value = "/getRecentNewsList")
	public void getRecentNewsList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat ||"".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		jsonPaging=new JsonPaging();
		jsonPaging.setRows(5);
		jsonPaging.setPage(1);
		jsonPaging=newsInfoService.findRecentNews(jsonPaging, langCode,dateFormat);
		
		JsonUtil.toWriter(jsonPaging.getList(), response);
	}
	
	/**
	 * 新闻详情页面推荐的新闻
	 * @author mqzou 2017-03-08
	 *//*
	@RequestMapping(value = "/getNewsListForRecommend")
	public void getNewsListForRecommend(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("id");
		if(null!=id && !"".equals(id)){
			NewsInfo info=newsInfoService.findNewsInfo(id);
			if(null!=info){
				List<NewsInfoForListVO> list=newsInfoService.findListForProbably(DateUtil.dateToDateString(info.getPubDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT), 4);
				JsonUtil.toWriter(list, response);
			}
		}
		
	}*/
	
	/**
	 * 收藏新闻列表
	 */
	@RequestMapping(value = "/newsCollection")
	public String newsCollection(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/news/newsCollection";
	}
	
	/**
	 *  新闻的评论内容
	 *  @author mqzou 2017-03-09
	 */
	@RequestMapping(value = "/newsCommentJson")
	public void getNewsComment(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String infoId=request.getParameter("infoId");//新闻
		MemberBase loginUser=getLoginUser(request);
		//String commentId=request.getParameter("id");//评论id
		String langCode=getLoginLangFlag(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging=newsInfoService.findNewsComment(jsonPaging, infoId, langCode,loginUser);
		JsonUtil.toWriter(jsonPaging, response);
		
	}
	
	/**
	 *  保存新闻评论内容
	 *  @author mqzou 2017-03-09
	 */
	@RequestMapping(value = "/addNewsComment")
	public void addNewsComment(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		HashMap<String, Object> result=new HashMap<String, Object>();
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		try {
			
			String infoId=request.getParameter("infoId");//新闻
			String commentId=request.getParameter("id");//评论id
			String comment=request.getParameter("comment");
			NewsComment replyComment=null;
			if(null!=commentId && !"".equals(comment)){
				replyComment=newsInfoService.findNewsComment(commentId);
			}
			NewsInfo newsInfo=newsInfoService.findNewsInfo(infoId);
			NewsComment vo=new NewsComment();
			vo.setChecked("1");//已通过审核
			vo.setComment(comment);
			vo.setIp(getRemoteHost(request));
			vo.setMember(loginUser);
			vo.setReplyComment(replyComment);
			/*if(null!=replyComment)
				vo.setReplyMember(replyComment.getMember());*/
			vo.setOpTime(DateUtil.getCurDate());
			vo.setNewsInfo(newsInfo);
			vo.setStatus("1");
			vo=newsInfoService.saveComment(vo);
			vo.setFType(CommonConstantsWeb.NEWS_COMMENT_TYPE_FEEDBACK);//评论回复
			//vo.setFace("0");
			result.put("result", true);
			
			NewsCommentVO commentVO=new NewsCommentVO(vo);
			commentVO.setOpTime(DateUtil.getDateTimeGoneFormatStr(DateUtil.StringToDate(commentVO.getOpTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT), langCode, DateUtil.DEFAULT_DATE_TIME_FORMAT));
			commentVO.setMemberUrl(PageHelper.getUserHeadUrl(commentVO.getMemberUrl(), ssoVO.getGender()));
			result.put("info", commentVO);
		} catch (Exception e) {
			result.put("result", false);
		}
		
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  保存新闻顶或踩
	 *  @author mqzou 2017-03-10
	 */
	@RequestMapping(value = "/newsUpOrDown")
	public void newsUpOrDown(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		try {
			String id=request.getParameter("infoId");//新闻id
			String type=request.getParameter("type");//1顶 0踩
			String commentId=request.getParameter("commentId");
			
			NewsBehavior behavior=new NewsBehavior();
			if("1".equals(type)){
				behavior.setBehaviorType(CommonConstantsWeb.NEWS_BEHAVIOR_LIKE);
			}else if ("0".equals(type)) {
				behavior.setBehaviorType(CommonConstantsWeb.NEWS_BEHAVIOR_UNLIKE);
			}
			
			if(null!=commentId && !"".equals(commentId)){
				behavior.setTargetId(commentId);
				behavior.setTarget(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_COMMENT);
			}else {
				behavior.setTargetId(id);
				behavior.setTarget(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS);
			}
			behavior.setCreateTime(DateUtil.getCurDate());
			behavior.setCreator(loginUser);
			
			
			behavior=newsInfoService.saveUpOrDown(behavior);
			result.put("result", true);
		} catch (Exception e) {
			result.put("result", false);
			
		}
		
		JsonUtil.toWriter(result, response);
		
		
	}
	
	/**
	 *  取消新闻点赞或踩
	 *  @author mqzou 2017-03-10
	 */
	@RequestMapping(value = "/cancelNewsUpOrDown")
	public void cancelUpOrDown(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		try {

			String infoId=request.getParameter("infoId");
			NewsBehavior behavior=newsInfoService.saveCancelUpOrDown(infoId, loginUser);
			if(null!=behavior){
				result.put("result", true);
			}else {
				result.put("result", false);
			}
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	/**
	 *  收藏或者取消收藏新闻
	 *  @author mqzou 2017-03-10
	 */
	@RequestMapping(value = "/saveNewsFavorite")
	public void saveNewsFavorite(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		  HashMap<String, Object> result=new HashMap<String, Object>();
	      MemberBase loginUser=getLoginUser(request);
	      if(null!=loginUser){
	    	  try {
	    		  String infoId=request.getParameter("infoId");
		    	  String status=request.getParameter("status");//操作状态  0：取消收藏 1：收藏
		    	  NewsInfo info=newsInfoService.findNewsInfo(infoId);
		    	  if(null!=info){
		    		  if("1".equals(status)){
		    			  NewsBehavior behavior=new NewsBehavior();
			    		  behavior.setBehaviorType(CommonConstantsWeb.NEWS_BEHAVIOR_FAVORITE);
			    		  behavior.setCreateTime(DateUtil.getCurDate());
			    		  behavior.setCreator(loginUser);
			    		  behavior.setTarget(CommonConstantsWeb.NEWS_BEHAVIOR_TARGET_NEWS);
			    		  behavior.setTargetId(infoId);
			    		  newsInfoService.saveBehavior(behavior);
		    		  }else {
						NewsBehavior vo=newsInfoService.findNewsBehavior(infoId, CommonConstantsWeb.NEWS_BEHAVIOR_FAVORITE, loginUser.getId());
						newsInfoService.deleteBehavior(vo);
					}
		    	  }
	    		  result.put("result", true);
			} catch (Exception e) {
				 result.put("result", false);
			}
	      }
	      
	      JsonUtil.toWriter(result, response);
	      
	}
	
	/**
	 *  搜索新闻
	 *  @author mqzou 2017-03-13
	 */
	@RequestMapping(value = "/searchNews")
	public String searchNews(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode=getLoginLangFlag(request);
		List<NewsCategoryVO> categoryList=newsInfoService.findNewsCategoryList(langCode);
		model.put("categoryList", categoryList);
		return "ifa/discover/news/newsSearch";
	}
	
	/**
	 *  搜索新闻
	 *  @author mqzou 2017-03-13
	 */
	@RequestMapping(value = "/searchNewsJson")
	public void searchNewsJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyword=request.getParameter("key");
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat ||"".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		if(null!=keyword && !"".equals(keyword)){
			keyword=toUTF8String(keyword);
			jsonPaging=getJsonPaging(request);
			jsonPaging.setOrder("desc");
			jsonPaging.setSort("r.pubDate");
			jsonPaging=newsInfoService.findNewsList(jsonPaging, keyword, langCode,dateFormat);
			JsonUtil.toWriter(jsonPaging, response);
		}
	}
	
	/**
	 * 获取收藏的新闻列表数据
	 * @author mqzou 2017-03-23
	 */
	@RequestMapping(value = "/favouriteNewsJson")
	public void getFavouriteNews(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		jsonPaging=getJsonPaging(request);
		jsonPaging=newsInfoService.findFavouriteNews(jsonPaging, loginUser.getId());
		JsonUtil.toWriter(jsonPaging, response);
	}
	

	@RequestMapping(value = "/community")
	public String community(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/community";
	}
	
	@RequestMapping(value = "/topicDetail")
	public String topicDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/topicDetail";
	}
	
	@RequestMapping(value = "/publishTopic")
	public String publishTopic(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "community/publishTopic";
	}
	
	@RequestMapping(value = "/space")
	public String space(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "community/space";
	}
	
	@RequestMapping(value = "/myzone")
	public String myzone(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/myzone";
	}
	
	@RequestMapping(value = "/myAssets")
	public String myAssets(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/myAssets";
	}
	
	@RequestMapping(value = "/noticelist")
	public String noticelist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/noticelist";
	}
	
	@RequestMapping(value = "/myzoneone")
	public String myzoneone(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/community/myzoneone";
	}
}
