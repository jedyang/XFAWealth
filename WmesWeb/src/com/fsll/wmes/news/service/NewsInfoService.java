package com.fsll.wmes.news.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsBehavior;
import com.fsll.wmes.entity.NewsCategory;
import com.fsll.wmes.entity.NewsComment;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.news.vo.NewsCategoryVO;
import com.fsll.wmes.news.vo.NewsInfoForListVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.news.vo.NewsSimpleInfoVO;

public interface NewsInfoService {

	/**
	 * 获取首页幻灯片新闻
	 * @author mqzou 2017-03-06
	 * @return
	 */
	public List<NewsSimpleInfoVO> getTopNews(String langCode);
	
	/**
	 * 根据栏目获取新闻列表
	 * @author mqzou 2017-03-06
	 * @param flag
	 * @param langCode
	 * @return
	 */
	public JsonPaging findNewsByCatagoryId(JsonPaging jsonPaging,String catogoryId,String langCode,String dateFormat);
	
	/**
	 * 根据栏目编码获取新闻列表
	 * @author mqzou 2017-03-06
	 * @param flag
	 * @param langCode
	 * @return
	 */
	public List<NewsInfoForListVO> findNewsByCatagoryCode(String code,String langCode,int rows,String dateFormat);
	
	/**
	 * 获取新闻的详细内容
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	public NewsInfoVO findNewsInfoVO(String id);
	
	/**
	 * 根据属性获取文章列表
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param flag
	 * @param langCode
	 * @return
	 */
	public JsonPaging findNewsByFlag(JsonPaging jsonPaging,String flag,String langCode);
	
	/**
	 * 根据id获取栏目
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	public NewsCategoryVO findCategoryById(String id,String langCode);
	
	/**
	 * 根据编码code获取栏目
	 * @author mqzou 2017-03-06
	 * @param id
	 * @return
	 */
	public NewsCategoryVO findCategoryByCode(String code,String langCode);
	
	/**
	 * 获取编辑推荐的新闻列表
	 *  @author mqzou 2017-03-06
	 * @param jsoPaging
	 * @param langCode
	 * @return
	 */
	public JsonPaging findRecommendNews(JsonPaging jsoPaging,String langCode,String dateFormat);
	
	/**
	 * 获取最热门的新闻列表
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param langCode
	 * @return
	 */
	public JsonPaging findHotNews(JsonPaging jsonPaging,String langCode,String dateFormat);
	
	/**
	 * 获取最新的新闻列表
	 * @author mqzou 2017-03-06
	 * @param jsonPaging
	 * @param langCode
	 * @return
	 */
	public JsonPaging findRecentNews(JsonPaging jsonPaging,String langCode,String dateFormat);
	
	/**
	 * 获取栏目列表
	 * @author mqzou 2017-03-07
	 * @param langCode
	 * @return
	 */
	public List<NewsCategoryVO> findNewsCategoryList(String langCode);
	
	/**
	 * 根据新闻获取新闻所属的栏目
	 * @author mqzou 2017-03-07
	 * @param infoId
	 * @return
	 */
	public NewsCategoryVO findCategoryByNews(String infoId,String langCode);
	
	/**
	 * 获取头条新闻列表
	 * @author mqzou 2017-03-07
	 * @param langCode
	 * @return
	 */
	public JsonPaging findHeadlineNews(JsonPaging jsonPaging, String langCode,String dateFormat);
	
	/**
	 * 获取某个新闻相近的推荐新闻
	 * @author mqzou 2017-03-08
	 * @param infoId
	 * @return
	 */
	public List<NewsInfoForListVO> findListForProbably(String infoId,int rows,String langCode);
	
	/**
	 * 获取新闻实体
	 * @author mqzou 2017-03-08
	 * @param id
	 * @return
	 */
	public NewsInfo findNewsInfo(String id);
	
	/**
	 * 获取新闻的评论列表
	 *  @author mqzou 2017-03-09
	 * @param jsonPaging
	 * @param infoId
	 * @return
	 */
	public JsonPaging findNewsComment(JsonPaging jsonPaging,String infoId,String langCode,MemberBase member);
	
	/**
	 * 保存新闻评论信息
	 * @author mqzou 2017-03-09
	 * @param comment
	 * @return
	 */
	public NewsComment saveComment(NewsComment comment);
	
	/**
	 * 获取评论实体
	 *  @author mqzou 2017-03-09
	 * @param id
	 * @return
	 */
	public NewsComment findNewsComment(String id);
	
	/**
	 * 保存新闻或评论的顶或者踩
	 * @author mqzou 2017-03-10
	 * @param comment
	 * @return
	 */
	public NewsBehavior saveUpOrDown(NewsBehavior comment);
	
	/**
	 * 取消新闻的点赞或者踩
	 * @author mqzou 2017-03-10
	 * @param infoId
	 * @param member
	 * @return
	 */
	public NewsBehavior saveCancelUpOrDown(String targetId,MemberBase member);
	
	
	/**
	 * 检测新闻是否被赞或者被踩
	 * @author mqzou 2017-03-10
	 * @param infoId
	 * @return
	 */
	public String checkNewsUpOrDown(String targetId,MemberBase member);
	
	/**
	 * 保存新闻的操作动作（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	public NewsBehavior saveBehavior(NewsBehavior behavior);
	
	/**
	 * 删除新闻的操作动作（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	public NewsBehavior deleteBehavior(NewsBehavior behavior);
	
	/**
	 * 获取新闻的操作动作实体（如收藏）
	 * @author mqzou 2017-03-10
	 * @param behavior
	 * @return
	 */
	public NewsBehavior findNewsBehavior(String tartgetId,String type,String memberId);
	
	/**
	 * 模糊搜索新闻
	 * @author mqzou 2017-03-13
	 * @param jsonPaging
	 * @param keyWord
	 * @param langCode
	 * @return
	 */
	public JsonPaging findNewsList(JsonPaging jsonPaging,String keyWord,String langCode,String dateFormat);
	
	/**
	 * 获取用户收藏的新闻列表
	 * @author mqzou 2017-03-23
	 * @param jsonPaging
	 * @param memberId
	 * @return
	 */
	public JsonPaging findFavouriteNews(JsonPaging jsonPaging,String memberId);
	
}
