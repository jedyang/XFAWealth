package com.fsll.wmes.community.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.common.CommonConstantsWeb;
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
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.SysSearchHistory;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.IfaCorner;
import com.fsll.wmes.ifa.vo.IfaCountryVO;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifa.vo.IfaSearchItemVO;
import com.fsll.wmes.ifa.vo.IfafirmVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;


/**
 * 社区服务接口
 * @author wwlin
 * @date 2017-03-13
 */
public interface CommunityService {
	
	/**分享新闻、贴子、组合、策略等到贴子
	 * @param TopicShareParamtersVO 分享参数
	 * @return
	 */
	public CommunityTopic saveTopicFromShare(TopicShareParamtersVO paramVO);
	
	/**
	 * 获取我的Dynamic贴子数据分页数据（我发的贴子跟我关注的人发的贴子）
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getMyDynamicTopicList(JsonPaging jsonPaging,MemberBase member,List<String>focusList,String keywords,String lang);
	
	/**
	 * 获取各个版块的贴子列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getSetionTypeTopicListJson(JsonPaging jsonPaging,MemberBase member,String keywords,String lang,String sectionId);
	
	/**
	 * 获取管理员推荐的贴子列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getRecommandTopicListJson(JsonPaging jsonPaging,MemberBase member,String keywords,String lang);
	
	/**
	 * 获取贴子信息
	 * @author wwlin 
	 * @param id
	 * @return
	 */

	public CommunityTopic findCommunityTopicById(String id);
	
	/**
	 * 保存贴子信息
	 * @author wwlin
	 * */
	public CommunityTopic updateCommunityTopic(CommunityTopic info);
	
	/**
	 * 保存发布帖子
	 * @author mqzou 2017-03-14
	 * @param communityTopic
	 * @param communityContent
	 * @return
	 */
	public CommunityTopic saveOrUpdateTopic(CommunityTopic topic,CommunityContent content);
	
	/**
	 * 获取社区版块（供选择使用）
	 * @author mqzou
	 * @param langCode
	 * @return
	 */
	public List<CommunitySectionVO> findSectionForSelect(String langCode);
	
	/**
	 * 获取版块名称
	 * @author wwlin 
	 * @param id
	 * @return
	 */
	public CommunitySection getCommunitySectionInfo(String id);
	
	/**
	 * 获取帖子的详情
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param langCode
	 * @param member
	 * @return
	 */
	public TopicDetailVO findTopicDetail(String id,String langCode,MemberBase member);
	
	/**
	 * 获取帖子的内容
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	public CommunityContent findCommunityContent(String id);
	
	/**
	 * 获取帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	public CommunityBehavior findBehavior(String id,String memberId,String type,String target);
	
	/**
	 * 保存帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param behavior
	 * @return
	 */
	public CommunityBehavior saveBehavior(CommunityBehavior behavior);
	
	/**
	 * 删除帖子的behavior
	 * @author mqzou 2017-03-15
	 * @param behavior
	 * @return
	 */
	public CommunityBehavior deleteBehavior(CommunityBehavior behavior);
	
	/**
	 * 获取问题实体
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	public CommunityQuestion findCommunityQuestion(String id);
	
	/**
	 * 获取评论
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 * @param id
	 * @return
	 */
	public CommunityComment findCommunityComment(String id);
	
	/**
	 * 获取首页昨天排行回复贴子最高的前面5位
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getYesterdayTopCommentTopicCreator(String loginMemberId,String langCode);
	
	/**
	 * 
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	public int findPublishTopicCountByMemberId(String memberId);
	
	/**
	 * 获取首页统计截止到今日一周内最多人评论的帖子
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getWeekTopCommentTopicList(String lang,String loginMemberId);
	
	/**
	 * 获取首页 统计上周的数据，上周他帖子回帖数
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getWeekTopCommentTopicCreator(String loginMemberId,String langCode);
	
	/**
	 * 获取首页 总排名：统计截止到昨日（1年内，可设置）的数据，他帖子的回帖总数
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<FrontPopularityRankVO> getTotalTopCommentTopicCreator(String loginMemberId,String langCode);
	
	/**
	 * 保存评论
	 * @author mqzou 2017-03-15
	 * @param comment
	
	 * @return
	 */
	public CommunityCommentVO saveComment(CommunityComment comment,Object object,String langCode);
	
	/**
	 * 分页获取帖子/问题的评论
	 * @author mqzou 2017-03-15
	 * @param jsonPaging
	 * @param topicId
	 * @param langCode
	 * @param member
	 * @return
	 */
	public JsonPaging findTopicComments(JsonPaging jsonPaging ,String topicId,String langCode,MemberBase member);
	
	/**
	 * 获取问题实体
	 * @author mqzou 2017-03-15
	 * @param id
	 * @return
	 */
	public QuestionDetailVO findqQuestionDetail(String id,String langCode,MemberBase member);
	
	/**
	 * 获取问题的回答列表
	 * @author mqzou 2017-03-15
	 * @param id
	 * @param langCode
	 * @param member
	 * @return
	 */
	public List<CommunityCommentVO> findAnswerList(String id,String langCode,MemberBase member);
	
	/**
	 * 获取贴子分页列表
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public JsonPaging getTopicListJson(JsonPaging jsonPaging,String memberId,String keywords,String lang,Boolean isInsight);
	
	/**
	 * 搜索页面-获取问题列表
	 * @author wwlin
	 * @date 2016-07-21
	 * */
	public JsonPaging getSearchQuestionListJson(JsonPaging jsonPaging,String keywords,String lang);
	
	/**
	 * 搜索页面-获取观点列表
	 * @author wwlin
	 * @date 2016-07-21
	 * */
	public JsonPaging getSearchInsightListJson(JsonPaging jsonPaging,String keywords,String lang);
	
	/**
	 * 搜索页面-获取user列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging getSearchUserListJson(JsonPaging jsonPaging,String memberId,String keywords,String lang);
	
	/**
	 * 保存搜索关键字
	 * @author wwlin 2017-03-14
	 * @param keyword
	 * @return
	 */
	public SysSearchHistory saveOrUpdateKeyword(SysSearchHistory keyword);
	
	/**
	 * 判断是否已存在同样的关键字
	 * @author wwlin 2017-03-15
	 * @param id
	 * @param memberId
	 * @param type
	 * @return
	 */
	public int findSameKeywords(String keyword) ;
	
	/**
	 * 获取搜索页面的关键字列表前10位
	 * @author wwlin
	 * @date 2017-03-13
	 * */
	public List<SysSearchHistory> getSysSearchHistoryList();
	
	/**
	 * 查看查某个IFA的观点列表
	 * @author wwlin 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findIfaInsight(JsonPaging jsonPaging, String loginMemberId, String ifaMemberId,String lang,String keyWords);
	
	/**
	 * 获取各个版块的贴子列表
	 * @author wwlin 2017-03-17
	 * @param jsonPaging
	 * @param loginMemberId 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging getSetionTypeTopicListJson2(JsonPaging jsonPaging, MemberBase member,String lang,String keyWords,String sectionId);
}
