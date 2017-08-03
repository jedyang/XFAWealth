package com.fsll.wmes.community.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.community.vo.CommunitySpaceVO;
import com.fsll.wmes.community.vo.FocusMemberVO;
import com.fsll.wmes.community.vo.QuestionAnswerListVO;
import com.fsll.wmes.entity.CommunityFocus;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.MemberBase;

public interface CommunitySpaceService {

	/**
	 * 获取ifa空间的基本数据
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	public CommunitySpaceVO getIfaSpaceVO(String memberId,String langCode,String currency,String loginMemberId);
	
	/**
	 * 获取投资人空间的基本数据
	 * @author mqzou 2017-03-21
	 * @param memberId
	 * @param langCode
	 * @param loginMemberId
	 * @return
	 */
	public CommunitySpaceVO getInvestorSpaceVO(String memberId,String langCode,String loginMemberId);
	/**
	 * 获取粉丝的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	public int getFollowCount(String memberId);
	
	/**
	 * 获取关注的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	public int getFocusCount(String memberId);
	
	/**
	 * 获取帖子的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	public int getTopciCount(String memberId);
	
	/**
	 * 获取ifa推荐的新闻列表
	 * @author mqzou 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findIfaRecommendNews(JsonPaging jsonPaging,String loginMemberId,String ifaMemberId,String keyWord);
	
	/**
	 * 获取观点列表
	 * @author mqzou 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findIfaRecommendInsight(JsonPaging jsonPaging, String loginMemberId, String ifaMemberId,String lang);
	
	/**
	 * 获取贴子列表
	 * @author mqzou 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findIfaRecommendTopic(JsonPaging jsonPaging, MemberBase member, String ifaMemberId,String lang);
	
	/**
	 * 获取ifa空间的Q&A 列表
	 * @author mqzou 2017-03-20
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public JsonPaging findIfaQuestionList(JsonPaging jsonPaging,String ifaMemberId,String memberId,String langCode);
	
	/**
	 * 获取投资人空间的Q&A 列表
	 * @author mqzou 2017-03-21
	 * @param jsonPaging
	 * @param invMemberId
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public JsonPaging findInvestorQuestionList(JsonPaging jsonPaging,String invMemberId,String memberId,String langCode);
	
	/**
	 * 获取问题的所有回复列表
	 * @author mqzou 2017-03-20
	 * @param questionId
	 * @return
	 */
	public List<QuestionAnswerListVO> findQuestionAnswerList(String questionId,String langCode,String memberId);
	
	/**
	 * 获取ifa的收藏列表
	 * @author mqzou 2017-03-20
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public JsonPaging findIfaFavoriteList(JsonPaging jsonPaging,String ifaMemberId,String memberId,String langCode,String dateFormat);
	
	/**
	 * 获取所关注的人的列表
	 * @author mqzou 2017-03-20
	 * @param memberId
	 * @return
	 */
	public List<FocusMemberVO> findFocusMemberList(String memberId,String curMemberId,String langCode);
	
	/**
	 * 获取所有粉丝列表
	 * @author mqzou 2017-03-20
	 * @param memberId
	 * @return
	 */
	public List<FocusMemberVO> findFollowerList(String memberId,String curMemberId,String langCode);
	
	/**
	 * 获取关注实体
	 * @author mqzou 2017-03-21
	 * @param loginMemberId
	 * @param targetMemberId
	 * @return
	 */
	public CommunityFocus findCommunityFocus(String curMemberId,String targetMemberId);
	
	/**
	 * 加关注
	 * @author mqzou 2017-03-21
	 * @param focus
	 * @return
	 */
	public CommunityFocus saveFocus(CommunityFocus focus);
	
	/**
	 * 取消关注
	 * @author mqzou 2017-03-21
	 * @param focus
	 * @return
	 */
	public boolean deleteFocus(CommunityFocus focus);
	
	/**
	 * 保存问题
	 * @author mqzou 2017-03-21
	 * @param question
	 * @return
	 */
	public CommunityQuestion saveCommunityQuestion(CommunityQuestion question);
}
